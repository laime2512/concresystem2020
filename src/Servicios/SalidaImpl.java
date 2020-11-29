package Servicios;

import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import Modelos.Almacen;
import Modelos.Salida;
import Modelos.Sucursal;
import Modelos.ViewDetalleSalida;
import Modelos.ViewProducto;
import Utiles.Fechas;
import Utiles.UtilDataTableS;
import Wrap.SalidaProductoWrap;
import enumeraciones.SalidaE;
import pagination.DataTableResults;
import pagination.SqlBuilder;

@Service
@Transactional
public class SalidaImpl implements SalidaS {
	
	JdbcTemplate db;
	@Autowired
	public void setJdbcTemplate(DataSource dataSource) {
		this.db = new JdbcTemplate(dataSource);
	}
	
	private String sqlString;
	private Logger LOGGER = Logger.getLogger("SalidaImpl");
	private static final boolean AUMENTO_ALMACEN = true;
	private static final boolean DISMINUCION_ALMACEN = false;
	private static final String ACEPTADO = "a";
	private static final String PENDIENTE = "p";
	@Autowired
	private AlmacenS almacenS;
	@Autowired
	private UtilDataTableS utilDataTableS;
	@Autowired
	private ProductoS productoS;
	@Autowired
	private LugarS lugarS;
	@Autowired
	private SucursalS sucursalS;

	public Salida obtener(Long codsal) throws SQLException {
		sqlString = "select salida.*,to_char(salida.fsalida,'dd/MM/YY') as xfsalida,concat(usuario.nombre,' ',usuario.ap) as xusuario " + 
				"from salida " + 
				"join usuario on usuario.codusu=salida.codusu " + 
				"where salida.codsal=?;";
		try {
			Salida salida = db.queryForObject(sqlString, new BeanPropertyRowMapper<Salida>(Salida.class), codsal);
			if(salida.getTipo() == 2 || salida.getTipo() == 10) {//transacciones entre sucursales
				Sucursal origen = sucursalS.obtener(salida.getSucOrigen());
				Sucursal destino = sucursalS.obtener(salida.getSucDestino());
				salida.setXsuc_origen(origen.getNombre());
				salida.setXsuc_destino(destino.getNombre());
			}
			salida.setDetalleSalidaList(obtenerDetalles(codsal));
			return salida;
		} catch (Exception e) {
			LOGGER.info("obtener:"+e.toString());
			return null;
		}
	}
	public DataTableResults<SalidaProductoWrap> listarSalidaProducto(HttpServletRequest request, Long codpro){
		try {
			Sucursal sucursal=(Sucursal)request.getSession().getAttribute("sucursal");
			SqlBuilder sqlBuilder = new SqlBuilder("salida s");
			sqlBuilder.setSelect("s.codsal,s.fsalida,ds.cantidad,ds.in_out,ds.xfingreso as fingreso,ds.xfvencimiento as fvencimiento,");
			sqlBuilder.setSelectConcat("s.in_out as in_out_salida,s.tipo");
			sqlBuilder.addJoin("view_detalle_salida ds on ds.codsal = s.codsal and ds.codpro = :xcodpro");
			sqlBuilder.setWhere("s.est=true and (s.suc_origen=:codsucursal or s.suc_destino=:codsucursal) and s.estado='a'");
			sqlBuilder.setOrderBy("s.codsal asc");
			sqlBuilder.addParameter("codsucursal", sucursal.getCodsuc());
			sqlBuilder.addParameter("xcodpro", codpro);
			return utilDataTableS.list(request, SalidaProductoWrap.class, sqlBuilder);
		} catch (Exception e) {
			LOGGER.error("listar"+e.getMessage());
			return null;
		}
		
	}
	public List<ViewDetalleSalida> obtenerDetalles(Long codsal){
		try {
			sqlString = "select * from view_detalle_salida where codsal=?";
			List<ViewDetalleSalida> detalle = db.query(sqlString, 
					new BeanPropertyRowMapper<ViewDetalleSalida>(ViewDetalleSalida.class),codsal);
			if(detalle != null && !detalle.isEmpty()) {
				detalle.forEach(item->{
					ViewProducto producto = productoS.obtenerPorAlmacen(item.getCodalmacen());
					item.setProducto(producto);
				});
			}
			return detalle;
		} catch (Exception e) {
			LOGGER.info("obtenerDetalles:"+e.toString());
			return null;
		}
	}
	public DataTableResults<Salida> filter(HttpServletRequest request,int tipo){
		try {
			Sucursal sucursal=(Sucursal)request.getSession().getAttribute("sucursal");
			SqlBuilder sqlBuilder = new SqlBuilder("salida");
			sqlBuilder.setSelect("salida.*,sucursal.nombre as xsuc_origen,to_char(salida.fsalida,'dd/MM/YY') as xfsalida,");
			sqlBuilder.setSelectConcat("concat(usuario.nombre,' ',usuario.ap) as xusuario");
			sqlBuilder.addJoin("usuario on usuario.codusu=salida.codusu");
			sqlBuilder.addJoin("sucursal on sucursal.codsuc=salida.suc_origen");
			sqlBuilder.setWhere("(salida.tipo=:xtipo or 0=:xtipo) and (salida.suc_origen=:xsucursal) and salida.est=true");
			sqlBuilder.addParameter("xsucursal", sucursal.getCodsuc());
			sqlBuilder.addParameter("xtipo", tipo);
			return utilDataTableS.list(request, Salida.class, sqlBuilder);
		} catch (Exception e) {
			LOGGER.error("filter"+e.getMessage());
			return null;
		}
	}
	public void adicionar(Salida obj) {
		try {
			sqlString = "select coalesce(max(numero),0)+1 from salida where salida.suc_origen=?";
			Long numero = db.queryForObject(sqlString, Long.class, obj.getSucOrigen());
			obj.setNumero(numero);
			sqlString = "select coalesce(max(codsal),0)+1 from salida";
			Long codSal = db.queryForObject(sqlString, Long.class);
			obj.setCodsal(codSal);
			sqlString = "insert into salida(codsal,fsalida,codusu,tipo,descripcion,estado,est,numero,suc_origen,suc_destino,in_out)  values(?,?,?,?,?,?,?,?,?,?,?)";
			Date fechaSalida = Fechas.convertirStringToDate(obj.getFsalida(), "dd/MM/yy");
			ValidarSalidaEntrada(obj);
			db.update(sqlString, obj.getCodsal(), fechaSalida, obj.getCodusu(), obj.getTipo(),
					obj.getDescripcion(), obj.getEstado(), true, obj.getNumero(), obj.getSucOrigen(),
					obj.getSucDestino(), obj.getInOut());		
			//Adicionar detalles
			obj.setCodsal(codSal);
			if(obj.getTipo()==SalidaE.AUMENTO_ALMACEN_POR_USUARIO.getTipo() || obj.getTipo() == SalidaE.ENTRADA.getTipo()) {
				crearAlmacenDetalleSalidaSucursal(obj, obj.getSucOrigen(),obj.getInOut());
			}else
				adicionarListaDetalles(obj);
			//Revisar si es aceptado la salida
			if(obj.getEstado().equals(ACEPTADO))
				adicionarRevision(obj);
		} catch (Exception e) {
			LOGGER.error("adicionar:"+e.toString());
		}
	}

	public void ValidarSalidaEntrada(Salida obj) {
		if (obj.getTipo() == SalidaE.MAL_ESTADO.getTipo()) {
			obj.setEstado(PENDIENTE);
			obj.setInOut(DISMINUCION_ALMACEN);
		}
		if (obj.getTipo() == SalidaE.TRASPASO_ENTRE_FARMACIA_EGRESO.getTipo()) {
			obj.setEstado(PENDIENTE);
			obj.setInOut(DISMINUCION_ALMACEN);
			obj.setSolucion(6);//Se realizan los cambios
		}
		if (obj.getTipo() == SalidaE.TRASPASO_ENTRE_FARMACIA_INGRESO.getTipo()) {
			obj.setEstado(PENDIENTE);
			obj.setInOut(AUMENTO_ALMACEN);
			obj.setSolucion(6);//Se realizan los cambios
		}
		if (obj.getTipo() == SalidaE.VENCIDO.getTipo()) {
			obj.setEstado(PENDIENTE);
			obj.setInOut(DISMINUCION_ALMACEN);
		}
		if (obj.getTipo() == SalidaE.PERDIDA.getTipo()) {
			obj.setEstado(ACEPTADO);
			obj.setSolucion(5);
			obj.setConclusion("Perdida sin devolucion");
			obj.setInOut(DISMINUCION_ALMACEN);
		}
		if (obj.getTipo() == SalidaE.USO_EN_FARMACIA.getTipo()) {
			obj.setEstado(ACEPTADO);
			obj.setSolucion(6);
			obj.setConclusion("Uso en farmacia.");
			obj.setInOut(DISMINUCION_ALMACEN);
		}
		if (obj.getTipo() == SalidaE.DISMINUCION_ALMACEN_POR_USUARIO.getTipo()) {
			obj.setSolucion(6);//Se realiza los cambios manuales
			obj.setConclusion("Disminucion realizado por el usuario");
			obj.setEstado(ACEPTADO);
			obj.setInOut(DISMINUCION_ALMACEN);
		}
		if (obj.getTipo() == SalidaE.AUMENTO_ALMACEN_POR_USUARIO.getTipo()) {
			obj.setSolucion(6);//Se realiza los cambios manuales
			obj.setConclusion("Aumento realizado por el usuario");
			obj.setEstado(ACEPTADO);
			obj.setInOut(AUMENTO_ALMACEN);
		}
		if (obj.getTipo() == SalidaE.ENTRADA.getTipo()) {
			obj.setEstado(PENDIENTE);
			obj.setInOut(AUMENTO_ALMACEN);
		}
		if (obj.getTipo() == SalidaE.SALIDA.getTipo()) {
			obj.setEstado(PENDIENTE);
			obj.setInOut(DISMINUCION_ALMACEN);
		}
	}
	/**
	 * Para crear detalle de una sucursal
	 * @param obj
	 */
	private void crearAlmacenDetalleSalidaSucursal(Salida obj, Integer sucursal,boolean inOut) {
		if(obj.getDetalleSalidaList()!= null && !obj.getDetalleSalidaList().isEmpty()) {
			obj.getDetalleSalidaList().forEach(item->{
				Almacen almacenNuevo = new Almacen();
				Long codLugar = lugarS.obtenerOrAdicionarLugarPorProducto(sucursal, item.getCodpro());
				almacenNuevo.setCodlugar(codLugar);
				almacenNuevo.setCantidad(0);//Creamos en cero ya que despues se hara en general los movimientos
				Long codalmacen= almacenS.adicionar(almacenNuevo);
				if(almacenNuevo.getCodalmacen() >-1L) {
					item.setCodsal(obj.getCodsal());
					item.setCodalmacen(codalmacen);
					item.setIsResponse(true);
					item.setInOut(inOut);
					adicionarDetalle(item);
				}
			});
		}else {
			LOGGER.error("No se encontro detalles para agregar");
		}
	}
	private void crearAlmacenDetalleSalida(Salida obj,boolean inOut) {
		if(esValidoDetalleSalida(obj)) {
			obj.getDetalleSalidaList().forEach(item->{
				Almacen almacenNuevo = new Almacen();
				almacenNuevo.setCodlugar(item.getCodlugar());
				almacenNuevo.setCantidad(0);//Creamos en cero ya que despues se hara en general los movimientos
				Long codalmacen= almacenS.adicionar(almacenNuevo);
				if(almacenNuevo.getCodalmacen() >-1L) {
					item.setCodalmacen(codalmacen);
					item.setIsResponse(true);
					item.setInOut(inOut);
					adicionarDetalle(item);
				}
			});
		}
	}
	private boolean esValidoDetalleSalida(Salida obj) {
		if(obj == null)
			return false;
		return obj.getEstado().equals(ACEPTADO) && obj.getDetalleSalidaList()!= null && !obj.getDetalleSalidaList().isEmpty();
	}
	public Long adicionarRevision(Salida obj) {
		try {
			if(obj.getEstado().equals(ACEPTADO) && obj.getSolucion()!=null) {
				switch (obj.getSolucion()) {
				case 1:// Cambio del mismo producto
					//Crear nuevo almacen y detalle a partir de un detalle de salida
					crearAlmacenDetalleSalida(obj,!obj.getInOut());
					break;
				case 2:// Cambio con otro producto
					crearAlmacenDetalleSalidaSucursal(obj, obj.getSucOrigen(),!obj.getInOut());
					break;
				case 3:// Cambio de Vencimiento
					crearAlmacenDetalleSalida(obj,!obj.getInOut());
					break;
				case 4:// credito
					LOGGER.info("No se realiza nuevas adiciones");
					break;
				case 5:// sin devolucion
					LOGGER.info("No se realiza nuevas adiciones");
					break;
				case 6:// Entrada o salida manual por el usuario 
					LOGGER.info("Se realiza los cambios");
					break;
				default:
					LOGGER.error("No se tiene tipo de solucion para la salida o entrada");
					break;
				}
				if(obj.getTipo() == SalidaE.TRASPASO_ENTRE_FARMACIA_EGRESO.getTipo() || obj.getTipo() == SalidaE.TRASPASO_ENTRE_FARMACIA_INGRESO.getTipo()) {
					crearAlmacenDetalleSalidaSucursal(obj, obj.getSucDestino(),!obj.getInOut());
				}
				realizarMovimientoAlmacenPorDetalleSalida(obj.getCodsal());
			}
			db.update("update salida set conclusion=?,solucion=?,monto=?,estado=? where codsal=?", obj.getConclusion(),
					obj.getSolucion(),obj.getMonto(), obj.getEstado(), obj.getCodsal());
			return obj.getCodsal();
		} catch (Exception e) {
			LOGGER.error("adicionarRevision" + e.toString());
			return -1L;
		}
	}
	private void realizarMovimientoAlmacenPorDetalleSalida(Long codsal) {
		List<ViewDetalleSalida> detalles = obtenerDetalles(codsal);
		if(detalles != null && !detalles.isEmpty())
			for (ViewDetalleSalida det : detalles) {
				movimientoAlmacen(det.getCodalmacen(), det.getCantidad(), det.getInOut());
			}
		else
			LOGGER.error("No se encontro detalles");
	}
	private void movimientoAlmacen(Long codAlmacen, Integer cantidad, boolean tipo) {
		if (tipo) {// Aumento en almacen
			db.update("update almacen set cantidad=cantidad+? where codalmacen=?", cantidad, codAlmacen);
		} else {// Disminucion en almacen
			db.update("update almacen set cantidad=cantidad-? where codalmacen=?", cantidad, codAlmacen);
		}
	}
	private boolean tieneRelacionSalidaVenta(Long codsal) {
		sqlString = "select count(*) from detalleventa dv " + 
				"where dv.codalmacen in (select codalmacen from detalle_salida ds join salida s on s.codsal=ds.codsal and s.codsal=?)";
		int contador = db.queryForObject(sqlString, Integer.class, codsal);
		return contador>0;
	}
	public Map<String,Object> eliminar(Salida obj) {
		Map<String, Object> data = new HashMap<String, Object>();
		try {
			db.update("update salida set est=false where codsal=?", obj.getCodsal());
			Salida salida = obtener(obj.getCodsal());
			//Si la salida o entrada es valida y aceptada y tiene detalles y no tiene relacion con ventas
			boolean tieneRelacionConVenta = tieneRelacionSalidaVenta(obj.getCodsal());
			if(esValidoDetalleSalida(salida) && !tieneRelacionConVenta) {
				salida.getDetalleSalidaList().forEach(item->{
						movimientoAlmacen(item.getCodalmacen(), item.getCantidad(), !obj.getInOut());
				});
				data.put("status", true);
			}else {
				if(salida.getEstado().equals(ACEPTADO)) {
					data.put("msg","Transaccion fallida. No se encontro detalles.");
				}else {
					if(tieneRelacionConVenta)
						data.put("msg","No se puede eliminar la salida, ya que algunos productos ya se vendieron, elimine las ventas relacionadas primero.");
					else
						data.put("msg","Transaccion fallida. No se encontro detalles.");
				}
				data.put("status", false);
			}
		} catch (Exception e) {
			LOGGER.info("eliminar:" + e.toString());
			data.put("status", false);
		}
		return data;
	}
	private void adicionarListaDetalles(Salida salida) {
		if(salida.getDetalleSalidaList() != null && !salida.getDetalleSalidaList().isEmpty()) {
			salida.getDetalleSalidaList().forEach(item->{
				item.setCodsal(salida.getCodsal());
				item.setInOut(salida.getInOut());
				item.setIsResponse(salida.getEstado().equals(ACEPTADO));
				adicionarDetalle(item);
			});
		}
	}
	public void adicionarDetalle(ViewDetalleSalida det) {
		if(det.getXfingreso()!=null && det.getXfingreso().isEmpty())
			det.setXfingreso(null);
		if(det.getXfvencimiento()!=null && det.getXfvencimiento().isEmpty())
			det.setXfvencimiento(null);
		sqlString = "insert into detalle_salida(codsal,codalmacen,cantidad,fingreso,fvencimiento,is_response,in_out) values(?,?,?,to_date(?,'DD/MM/YY'),to_date(?,'DD/MM/YY'),?,?);";
		db.update(sqlString,det.getCodsal(),det.getCodalmacen(),det.getCantidad(),det.getXfingreso(),det.getXfvencimiento(),det.getIsResponse(),det.getInOut());
	}
	public boolean esTraspasoFarmacias(int tipo) {
		return tipo == SalidaE.TRASPASO_ENTRE_FARMACIA_INGRESO.getTipo() 
				|| tipo == SalidaE.TRASPASO_ENTRE_FARMACIA_EGRESO.getTipo()
				|| tipo == SalidaE.ENTRADA.getTipo();
	}
}