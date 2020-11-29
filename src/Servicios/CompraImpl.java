	package Servicios;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import Modelos.Almacen;
import Modelos.Compra;
import Modelos.DetalleCompra;
import Modelos.Lugar;
import Modelos.PagoCredito;
import Modelos.Producto;
import Modelos.Sucursal;
import Modelos.Valuacion;
import Modelos.ViewCompra;
import Utiles.Constantes;
import Utiles.Fechas;
import Utiles.Numeros;
import Utiles.UtilDataTableS;
import Wrap.CompraProductoWrap;
import pagination.DataTableResults;
import pagination.SqlBuilder;

@Service
public class CompraImpl implements CompraS{

	private static final Logger LOGGER = Logger.getLogger("CompraImpl");
	
	JdbcTemplate db;
	@Autowired
	public void setJdbcTemplate(DataSource dataSource) {
		this.db = new JdbcTemplate(dataSource);
	}
	
	@Autowired
	private LugarS lugarS;
	@Autowired
	private AlmacenS almacenS;
	@Autowired
	private ProductoS productoS;
	@Autowired
	private UtilDataTableS utilDataTableS;
	private String sqlString;
	public ViewCompra obtener(Long codcompra) throws SQLException {
		try {
			sqlString="select * from view_compra where codcom = ?";
			return db.queryForObject(sqlString,new BeanPropertyRowMapper<ViewCompra>(ViewCompra.class), codcompra);
		} catch (Exception e) {
			LOGGER.info("obtener:" + e.toString());
			return null;
		}
	}
	public List<DetalleCompra> obtenerDetalleCompra(Long codcom){
		try {
			return db.query("select * from detallecompra where codcom=?",new BeanPropertyRowMapper<DetalleCompra>(DetalleCompra.class),codcom);
		} catch (DataAccessException e) {
			LOGGER.info("obtenerDetalleCompra:"+e.toString());
			return null;
		}
	}
	public DataTableResults<CompraProductoWrap> listarCompraProducto(HttpServletRequest request, Long codpro){
		try {
			Sucursal sucursal=(Sucursal)request.getSession().getAttribute("sucursal");
			SqlBuilder sql = new SqlBuilder();
			sql.setSelect("c.codcom,c.fecha,dc.cantidad,dc.precio,dc.tipo_compra,dc.subtotal");
			sql.setFrom("compra c");
			sql.addJoin("detallecompra dc on dc.codcom = c.codcom and dc.codpro = :xcodpro");
			sql.setWhere("c.estado=1 and c.codsuc=:codsucursal");
			sql.addParameter("codsucursal", sucursal.getCodsuc());
			sql.addParameter("xcodpro", codpro);
			return utilDataTableS.list(request, CompraProductoWrap.class, sql);
		} catch (Exception e) {
			LOGGER.error("listar"+e.getMessage());
			return null;
		}
		
	}
	public DataTableResults<ViewCompra> listar(HttpServletRequest request){
		try {
			Sucursal sucursal=(Sucursal)request.getSession().getAttribute("sucursal");
			SqlBuilder sql = new SqlBuilder("view_compra");
			sql.setSelect("*");
			sql.setWhere("estado=1 and codsuc=:codsucursal");
			sql.addParameter("codsucursal", sucursal.getCodsuc());
			return utilDataTableS.list(request, ViewCompra.class, sql);
		} catch (Exception e) {
			LOGGER.error("listar"+e.getMessage());
			return null;
		}
	}

	public Long adicionar(Compra compra,Integer sucursal, Long[] productos, Integer[] cantidades, Float[] precios, Float[] totales,String vencimientos[],
			Float porcentajes[],Float descuentos[],Float impuestos[],Boolean devoluciones[], String tipos[]) {
		try {
			Long codcompra = generarCodigo();
			sqlString = "insert into compra(codcom, fecha, codusu, codpro, formapago, total,descuento,codcaja,coddetcaja,num,bonificacion,subtotal,tiponota,estado,numnota,codsuc,credito,estado_credito) "
					+ "values(?,to_date(?,'dd/MM/yy'),?,?,?,?,?,?,?,?,?,?,?,1,?,?,?,?);";
			db.update(sqlString, codcompra, compra.getFecha(),compra.getCodusu(),compra.getCodpro(),compra.getFormapago(),compra.getTotal(),
					compra.getDescuento(),compra.getCodcaja(),compra.getCoddetcaja(),compra.getNum(),
					compra.getBonificacion(),compra.getSubtotal(),compra.getTiponota(),compra.getNumnota(),compra.getCodsuc(),compra.getCredito(),compra.getEstadoCredito());
			
			sqlString = "insert into detallecompra(codcom,codpro,cantidad,precio,subtotal,fingreso,fvencimiento,codalmacen,coddcom,impuestos,devolucion,descuento,"
					+ "porcentaje_unidad,porcentaje_caja,porcentaje_paquete,tipo_compra) "
					+ "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";
			String fingreso=Fechas.getDateNowToString();
			for (int i = 0; i < productos.length; i++) {
				Producto producto = productoS.obtener(productos[i]);
				//Verificar si existe el lugar
				Lugar buscarLugar=new Lugar();
				buscarLugar.setCodpro(productos[i]);
				buscarLugar.setCodsuc(sucursal);
				Long codlugar=-1L;
				Integer codDet=i+1;
				Map<String, Object> lugar=lugarS.verificar(buscarLugar);
				if(lugar!=null) {
					codlugar=Long.parseLong(lugar.get("codlugar").toString());
				}else {
					buscarLugar.setCodposicion(Constantes.SIN_POSICION);
					codlugar=lugarS.adicionar(buscarLugar);
				}
				int cantidadEnUnidad = obtenerCantidadUnitariaDetalleCompra(cantidades[i], producto);
				//Guardar en almacen
				Almacen almacen=new Almacen();
				almacen.setCodlugar(codlugar);
				almacen.setCantidad(cantidadEnUnidad);
				Long codalm=almacenS.adicionar(almacen);
				//adicion de detalle de compra
				db.update(sqlString, codcompra, productos[i], cantidades[i], precios[i], totales[i],Fechas.convertirStringToDate(fingreso),
						Fechas.convertirStringToDate(vencimientos[i], "dd/MM/yy"),codalm,codDet,impuestos[i],devoluciones[i],descuentos[i],
						producto.getPorcentajeUnidad(),producto.getPorcentajeCaja(), producto.getPorcentajePaquete(),producto.getTipoCompra());
				//actualizar precios en producto
				String sql2 = "update producto set pv_unit=?,pv_caja=?,pv_paquete=?,pc_unit=?,pc_caja=?,pc_paquete=? where codpro = ?";
				Float pcUnit=null,pcCaja=null,pcPaquete=null;
				switch(Integer.parseInt(producto.getTipoCompra())) {
					case 1://unidades
						pcUnit = Numeros.roundMoney(precios[i]);
						pcCaja = Numeros.roundMoney(precios[i] * producto.getUnixcaja());
						pcPaquete = Numeros.roundMoney(pcCaja * producto.getUnixpaquete());
						break;
					case 2://cajas
						pcCaja = Numeros.roundMoney(precios[i]);
						pcPaquete = Numeros.roundMoney(precios[i]* producto.getUnixpaquete());
						pcUnit = Numeros.roundMoney(pcCaja / producto.getUnixcaja());
						break;
					case 3://paquetes
						pcPaquete = Numeros.roundMoney(precios[i]);
						pcCaja = Numeros.roundMoney(pcPaquete / producto.getUnixpaquete());
						pcUnit = Numeros.roundMoney(pcCaja / producto.getUnixcaja());						
						break;
					default:
						break;
				}
				if(pcUnit != null && pcCaja != null && pcPaquete != null) {
					Float pvUnit = obtenerPrecioVenta(pcUnit, producto.getPorcentajeUnidad());
					Float pvCaja = obtenerPrecioVenta(pcCaja, producto.getPorcentajeCaja());
					Float pvPaquete = obtenerPrecioVenta(pcPaquete, producto.getPorcentajePaquete());
					db.update(sql2, new Object[] {pvUnit, pvCaja, pvPaquete, pcUnit, pcCaja, pcPaquete, producto.getCodpro()} );
				}
			}
			return codcompra;
		} catch (Exception e) {
			LOGGER.info("adicionar:" + e.toString());
			return -1L;
		}
	}
	private Float obtenerPrecioVenta(float precio, float porcentaje) {
		return Numeros.roundMoney((precio * (porcentaje/100f)) + precio);
	}
	private int obtenerCantidadUnitariaDetalleCompra(Integer cantidad, Producto producto) {
		float cant;
		switch(Integer.parseInt(producto.getTipoCompra())) {
			case 1:
				return cantidad;
			case 2:
				cant = cantidad * producto.getUnixcaja();
				return (int)cant;
			case 3:
				cant = cantidad * producto.getUnixcaja() * producto.getUnixpaquete();
				return (int)cant;
			default:
				return -1;
		}
	}
	public Long generarCodigo() {
		return db.queryForObject("select COALESCE(max(codcom),0)+1 as codcom from compra", Long.class);
	}
	public Long generarNumero(Integer codsuc) {
		return db.queryForObject("select COALESCE(max(num),0)+1 as num from compra where codsuc=?", Long.class,codsuc);
	}
	public boolean eliminar(Long codcompra) {
		try {
			db.update("update compra set estado=0 where codcom=?", codcompra);
			List<DetalleCompra> detalles=obtenerDetalleCompra(codcompra);
			for (DetalleCompra det : detalles) {
				Producto producto = productoS.obtener(det.getCodpro());
				int cantidad =0;
				switch (Integer.parseInt(det.getTipoCompra())) {
				case 1:
					cantidad = det.getCantidad();
					break;
				case 2:
					cantidad = det.getCantidad() * producto.getUnixcaja();
					break;
				default:
					cantidad = det.getCantidad() * producto.getUnixcaja() * producto.getUnixpaquete().intValue();
					break;
				}
				db.update("update almacen set cantidad=cantidad-? where codalmacen=?",cantidad,det.getCodalmacen());
			}
			db.update("update detallecompra set codalmacen=null where codcom=?",codcompra);
			db.update("delete from almacen where cantidad<=0");
			return true;
		} catch (Exception e) {
			LOGGER.info("error al eliminar compra=" + e.toString());
			return false;
		}
	}
	public boolean finalizarCredito(Long codcompra) {
		try {
			db.update("update compra set estado_credito=false where codcom=?", codcompra);
			return true;
		} catch (Exception e) {
			LOGGER.info("finalizarCredito:" + e.toString());
			return false;
		}
	}
	public void adicionarCredito(PagoCredito obj) {
		sqlString = "insert into pagocredito(codcom,fecha,fecreg,monto,codusu,observacion) values(?,to_date(?,'DD/MM/YYYY'),now(),?,?,?);";
		db.update(sqlString, obj.getCodcom(),obj.getFecha(),obj.getMonto(),obj.getCodusu(),obj.getObservacion());
	}
	public List<Valuacion> valuacionCompra(Long codpro, Integer codsuc){
		String sql = "select to_char(c.fecha,'DD/MM/YYYY') fecha,p.pc_unit,pc_caja,pc_paquete,coalesce(sum(dc.cantidad),0) cantidad," + 
				"(p.pcosto*coalesce(sum(dc.can_unit),0)) total " + 
				"from compra c " + 
				"join detallecompra dc on dc.codcom=c.codcom and dc.codpro=? " + 
				"join producto p on p.codpro = dc.codpro " + 
				"group by to_char(c.fecha,'DD/MM/YYYY'),p.pc_unit,p.pc_caja,pc_paquete " + 
				"order by to_char(c.fecha,'DD/MM/YYYY') asc;";
		try {
			return db.query(sql, new BeanPropertyRowMapper<Valuacion>(Valuacion.class),codpro);
		} catch (Exception e) {
			LOGGER.info("valuacionCompra"+e.toString());
			return null;
		}
	}
	public List<Valuacion> valuacionVenta(Long codpro, Integer codsuc){
		String sql = "select to_char(v.fecha,'DD/MM/YYYY') fecha,p.pcosto,coalesce(sum(dv.cantidad),0) cantidad,\r\n" + 
				"(p.pcosto*coalesce(sum(dv.cantidad),0)) total\r\n" + 
				"from venta v\r\n" + 
				"join detalleventa dv on dv.codven=v.codven and dv.codpro=2982\r\n" + 
				"join producto p on p.codpro = dv.codpro\r\n" + 
				"group by to_char(v.fecha,'DD/MM/YYYY'),p.pcosto\r\n" + 
				"order by to_char(v.fecha,'DD/MM/YYYY') asc;";
		try {
			return db.query(sql, new BeanPropertyRowMapper<Valuacion>(Valuacion.class),codpro);
		} catch (Exception e) {
			LOGGER.info("valuacionVenta:"+e.toString());
			return null;
		}
	}
	public Map<String, Object> obtenerCompraProducto(Long codpro, int meses, Integer codsuc){
		try {
			sqlString = "select dc.codpro,sum(dc.cantidad) as cantidadcompra,sum(dc.subtotal) as totalcompra from compra c " + 
					"join detallecompra dc on dc.codcom = c.codcom and dc.codpro = ? " + 
					"where c.codsuc = ? and c.estado=1 and " + 
					"cast(c.fecha as date) between cast(now() - interval '"+meses+" month' as date) and cast(now() as date)" + 
					"GROUP BY dc.codpro";
			return db.queryForMap(sqlString, codpro, codsuc);
		} catch (Exception e) {
			LOGGER.info("obtenerCompraProducto"+e.getMessage());
			Map<String, Object> data = new HashMap<String, Object>();
			data.put("codpro", codpro);
			data.put("cantidadcompra",0);
			data.put("totalcompra",0);
			return data;
		}
	}
}
