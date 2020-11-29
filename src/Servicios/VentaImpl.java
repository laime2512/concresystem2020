package Servicios;

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

import Modelos.Caja;
import Modelos.DetalleCaja;
import Modelos.Pedido;
import Modelos.Sucursal;
import Modelos.Venta;
import Modelos.ViewAlmacen;
import Modelos.ViewDetalleVenta;
import Modelos.ViewVenta;
import Utiles.Constantes;
import Utiles.UtilDataTableS;
import Wrap.AlmacenWrap;
import Wrap.VentaProductoWrap;
import pagination.DataTableResults;
import pagination.SqlBuilder;


@Service
public class VentaImpl implements VentaS{
	private static final Logger LOGGER=Logger.getLogger("VentaImpl");
	private String sqlString;
	@Autowired
	private CajaS cajaS;
	@Autowired
	private AlmacenS almacenS;
	@Autowired
	private ProductoS productoS;
	@Autowired
	private UtilDataTableS utilDataTableS;
	
	JdbcTemplate db;
	@Autowired
	public void setJdbcTemplate(DataSource dataSource) {
		this.db = new JdbcTemplate(dataSource);
	}
	
	public ViewVenta obtener(Long codv){
		try {
			sqlString="select * from view_venta where codven=?";
			ViewVenta obj= db.queryForObject(sqlString,new BeanPropertyRowMapper<ViewVenta>(ViewVenta.class), codv);
			return obj;
		} catch (Exception e) {
			LOGGER.info("obtnerVenta:"+e.getMessage());
			return null;
		}
	}
	public List<ViewDetalleVenta> obtenerDetalles(Long codv){
		try {
			sqlString="select * from view_detalle_venta where codven=?";
			return db.query(sqlString,new BeanPropertyRowMapper<ViewDetalleVenta>(ViewDetalleVenta.class), codv);
		} catch (Exception e) {
			LOGGER.info("obtenerDetalles:"+e.getMessage());
			return null;
		}
	}
	public DataTableResults<VentaProductoWrap> listarVentaProducto(HttpServletRequest request,Long codpro){
		try {
			Sucursal sucursal=(Sucursal)request.getSession().getAttribute("sucursal");
			SqlBuilder sql = new SqlBuilder();
			sql.setSelect("v.codven,v.fecha,dv.cantidad,dv.subtotal");
			sql.setFrom("venta v");
			sql.addJoin("detalleventa dv on dv.codven = v.codven and dv.codpro = :xcodpro");
			sql.setWhere("v.codsuc = :codsucursal and v.estado=1");
			sql.addParameter("codsucursal", sucursal.getCodsuc());
			sql.addParameter("xcodpro", codpro);
			return utilDataTableS.list(request, VentaProductoWrap.class, sql);
		} catch (Exception e) {
			LOGGER.error("listar"+e.getMessage());
			return null;
		}
	}
	public DataTableResults<ViewVenta> listar(HttpServletRequest request){
		try {
			Sucursal sucursal=(Sucursal)request.getSession().getAttribute("sucursal");
			SqlBuilder sql = new SqlBuilder();
			sql.setSelect("*");
			sql.setFrom("view_venta");
			sql.setWhere("estado=1 and codsuc=:codsucursal");
			sql.addParameter("codsucursal", sucursal.getCodsuc());
			return utilDataTableS.list(request, ViewVenta.class, sql);
		} catch (Exception e) {
			LOGGER.error("listar"+e.getMessage());
			return null;
		}
	}
	public DataTableResults<ViewVenta> listarPorCaja(HttpServletRequest request,Caja caja){
		try {
			try {
				sqlString = " v1.estado=1 and v1.codcaja=:codigoCaja and ";
				sqlString = sqlString.concat("(((select coalesce(max(v2.codven),0) from view_venta as v2 join factura f2 on f2.codven=v2.codven where v2.estado=1 and v2.codcaja=:codigoCaja)=v1.codven) ");
				sqlString = sqlString.concat("or (((select coalesce(max(v3.codven),0) from view_venta as v3 where v3.estado=1 and v3.codcaja=:codigoCaja))=v1.codven))");
				SqlBuilder sql = new SqlBuilder();
				sql.setSelect("v1.*");
				sql.setFrom("view_venta v1");
				sql.setWhere(sqlString);
				sql.addParameter("codigoCaja", caja.getCodcaja());
				return utilDataTableS.list(request, ViewVenta.class, sql);
			} catch (Exception e) {
				LOGGER.error("listar"+e.getMessage());
				return null;
			}
		} catch (Exception e) {
			LOGGER.error("listar"+e.getMessage());
			return null;
		}
	}
	public List<Map<String, Object>> listar_usuario(int start,int estado,String search,int length,Integer tipo){
		if(estado==2){
			int tot=db.queryForObject("select count(*) from pedido", Integer.class);
			if(search==null)search="";
			return db.queryForList("select pedido.*,to_char(pedido.fecha,'dd/MM/yyyy') xfecha,row_number() OVER(ORDER BY pedido.codped) as RN,? as Tot from pedido where upper(cast(codped as varchar(50))) like concat('%',upper(?),'%') and (pedido.estado=? or -1=?) LIMIT ? OFFSET ?",tot,search,tipo,tipo,length,start);
		}else{
			int tot=db.queryForObject("select count(*) from pedido where estado=? or -1 = ?", Integer.class,tipo,tipo);
			if(search==null)search="";
			return db.queryForList("select pedido.*,to_char(pedido.fecha,'dd/MM/yy HH:mm:ss') xfecha,pedido.direccion,pedido.celular,row_number() OVER(ORDER BY pedido.codped desc) as RN,? as Tot from pedido join cliente on cliente.codcli=pedido.codusu where (pedido.estado=? or -1=?) and upper(cast(codped as text)) like concat('%',upper(?),'%') LIMIT ? OFFSET ?",tot,tipo,tipo,search,length,start);
		}
	}
	
	public int adicionar_c(Pedido v,int productos[],int cantidades[],float precios[],float subtotales[],int aux){
		try {
			String sql;
			int codven=db.queryForObject("select COALESCE(max(codped),0)+1 as codped from pedido", Integer.class);
			sql="insert into pedido(codped, fecha,codusu,celular,direccion,codsuc) values(?,now(),?,?,?,?);";
			db.update(sql,codven,v.getCodusu(),v.getCelular(),v.getDireccion(),v.getCodsuc());
			
			for (int i = 0; i <=aux; i++) {
				sql="insert into detallepedido(codped,codpro,cantidad,precio,subtotal) values(?,?,?,?,?);";
				db.update(sql,codven,productos[i],cantidades[i],precios[i],subtotales[i]);
			}
			return codven;
		} catch (Exception e) {
			LOGGER.info("adicionar_c:"+e.toString());
			return -1;
		}
	}
	
	public Long adicionar(Venta v,Integer codsuc,Long productos[],Float cantidades[],Float precios[],Float subtotales[],String tipoVenta[],Long promociones[]){
		try {
			//Adicionar detalle de caja
			DetalleCaja detCaja=new DetalleCaja();
			detCaja.setCodcaja(v.getCodcaja());
			detCaja.setCodcuenta(Constantes.CUENTA_VENTA);
			detCaja.setEstado(Constantes.TIPO_CUENTA_DEBE);
			detCaja.setMonto(v.getTotal());
			Long coddet=cajaS.adicionarDetalle(detCaja);
			if(coddet>0) {
				v.setCoddetcaja(coddet);
				Long codven=generarCodigo();
				sqlString="INSERT INTO venta" + 
						"(codven, fecha, estado, codcli, codcaja, coddetcaja, tiponota, formapago, codusu,total,total_pagado,cambio,codsuc) " + 
						"VALUES(?, to_date(?,'dd/MM/yy'),1, ?, ?, ?, ?, ?, ?,?,?,?,?)";
				db.update(sqlString,codven,v.getFecha(),v.getCodcli(),v.getCodcaja(),v.getCoddetcaja(),v.getTiponota(),v.getFormapago(),v.getCodusu(),v.getTotal(),v.getTotalPagado(),v.getCambio(),v.getCodsuc());

				String sql2="INSERT INTO detalleventa(codpro, cantidad, codven, precio, subtotal, codalmacen,tipo_venta,codpromo) VALUES(?, ?, ?, ?, ?, ?,?,?)";
				if(productos!=null)
					for(int i=0;i<productos.length;i++) {
						//Disminuir de almacen y guardar los detalles
						ViewAlmacen almacenProducto = almacenS.obtenerTotalProducto(productos[i], v.getCodsuc());
						List<AlmacenWrap> almacenes = almacenS.obtenerAlmacenPorProducto(productos[i], v.getCodsuc());
						int cantDetalle= productoS.obtenerCantidadEnUnidades(productos[i], cantidades[i], tipoVenta[i]);
						if(almacenProducto.getCantidad()>=cantDetalle) {
							for (AlmacenWrap almacenItem : almacenes) {
								if(cantDetalle > almacenItem.getCantidad()) {
									//Cuando hay mas detalle que el producto la fecha, se reduce a cero
									db.update("update almacen set cantidad = 0 where codalmacen=?",almacenItem.getCodalmacen());
									//Adicionar detalle
									db.update(sql2,productos[i],almacenItem.getCantidad(),codven,precios[i],subtotales[i],almacenes.get(i).getCodalmacen(),tipoVenta[i],(promociones!=null && promociones.length>0)?promociones[i]:null);
									cantDetalle=cantDetalle-almacenItem.getCantidad();
								}else {
									//Cuando el producto del almacen es mayor
									db.update("update almacen set cantidad=? where codalmacen=?",(almacenItem.getCantidad()-cantDetalle),almacenItem.getCodalmacen());
									//Adicionar Detalle
									db.update(sql2,productos[i],cantDetalle,codven,precios[i],subtotales[i],almacenItem.getCodalmacen(),tipoVenta[i],(promociones!=null && promociones.length>0)?promociones[i]:null);
									cantDetalle=0;
									break;
								}
							}
							
						}else {
							LOGGER.error("No existe cantidad para la venta");
						}
						
					}
				return codven;
			}
			return -1L;
		} catch (Exception e) {
			LOGGER.error("adicionar:"+e.toString());
			return -1L;
		}
	}
	public Long generarCodigo(){
		String sql="select COALESCE(max(codven),0)+1 as codven from venta";
		return db.queryForObject(sql, Long.class);
	}
	public boolean eliminar(Long codv,Long codcaja){
		try {
			ViewVenta venta = obtener(codv);
			if(venta!=null) {
				db.update("update venta set estado=0 where codven=?",codv);
				//Si tiene factura anular
				db.update("update factura set estado = 'anulado' where codven=? and estado='activo'",codv);
				List<ViewDetalleVenta> detalles=obtenerDetalles(codv);
				if(detalles!=null)
				for (ViewDetalleVenta detalle : detalles) {
					sqlString = "update almacen set cantidad=cantidad+? where codalmacen=?";
					db.update(sqlString,detalle.getCantidad(),detalle.getCodalmacen());
				}
				//Reversion de venta en caja
				DetalleCaja det=new DetalleCaja();
				det.setCodcaja(codcaja);
				det.setCodcuenta(Constantes.CUENTA_REVERSION_VENTA);
				det.setMonto(venta.getTotal());
				det.setEstado(Constantes.TIPO_CUENTA_HABER);
				cajaS.adicionarDetalle(det);
			}
			return true;
		} catch (Exception e) {
			LOGGER.info("eliminar:"+e.toString());
			return false;
		}
	}
	public Long adicionarPedido(Venta v,Long productos[],Integer cantidades[],Float precios[],Float subtotales[],Long[] lugares,Integer codsuc,Integer opt_cant[],Boolean desc[]){
		try {
			//Adicionar detalle de caja
			DetalleCaja detCaja=new DetalleCaja();
			detCaja.setCodcaja(v.getCodcaja());
			detCaja.setCodcuenta(Constantes.CUENTA_VENTA);
			detCaja.setEstado(Constantes.TIPO_CUENTA_DEBE);
			detCaja.setMonto(v.getTotal());
			Long coddet=cajaS.adicionarDetalle(detCaja);
			if(coddet>0) {
				v.setCoddetcaja(coddet);
				Long codven=generarCodigo();
				sqlString="INSERT INTO public.venta\r\n" + 
						"(codven, fecha, estado, codcli, codcaja, coddetcaja, tiponota, formapago, codusu,total,total_pagado,cambio,codsuc)\r\n" + 
						"VALUES(?, to_date(?,'dd/MM/yy'), 1, ?, ?, ?, ?, ?, ?,?,?,?,?);";
				db.update(sqlString,codven,v.getFecha(),v.getCodcli(),v.getCodcaja(),v.getCoddetcaja(),v.getTiponota(),v.getFormapago(),v.getCodusu(),v.getTotal(),v.getTotalPagado(),v.getCambio(),v.getCodsuc());

				String sql2="INSERT INTO detalleventa(codpro, cantidad, codven, precio, subtotal, codalmacen,opt_cant,desc_) VALUES(?, ?, ?, ?, ?, ?,?,?);";
				if(productos!=null)
					for(int i=0;i<productos.length;i++) {
						db.update(sql2,productos[i],cantidades[i],codven,precios[i],subtotales[i],null, opt_cant[i],desc[i]);
					}
				return codven;
			}
			return -1L;
		} catch (Exception e) {
			LOGGER.info("adicionarPedido:"+e.toString());
			return -1L;
		}
	}
	public Map<String, Object> obtenerVentaProducto(Long codpro, int meses, Integer codsuc){
		Map<String, Object> data = new HashMap<String, Object>();
		try {
			sqlString = "select dv.codpro,dv.tipo_venta,sum(dv.cantidad) as cantidadVenta,sum(dv.subtotal) as totalVenta from venta v " + 
					"join detalleventa dv on dv.codven = v.codven and dv.codpro = ? " + 
					"where v.codsuc = ? and v.estado=1 and " + 
					"cast(v.fecha as date) between cast(now() - interval '"+meses+" month' as date) and cast(now() as date)" + 
					"GROUP BY dv.codpro,dv.tipo_venta";
			List<Map<String, Object>> lista =  db.queryForList(sqlString, codpro, codsuc);
			if(lista != null && !lista.isEmpty()) {
				int cantidad = 0; 
				float total = 0f;
				for (Map<String, Object> map : lista) {
					if(map.get("tipo_venta").toString().equals("1")) {
						cantidad += Integer.parseInt(map.get("cantidadventa").toString());
						total += Float.parseFloat(map.get("totalventa").toString());
					}
					if(map.get("tipo_venta").toString().equals("2")) {
						int cantidadAux = Integer.parseInt(map.get("cantidadventa").toString());
						cantidad += cantidadAux;
						total += Float.parseFloat(map.get("totalventa").toString());
					}
					if(map.get("tipo_venta").toString().equals("3")) {
						int cantidadAux = Integer.parseInt(map.get("cantidadventa").toString());
						cantidad += cantidadAux;
						total += Float.parseFloat(map.get("totalventa").toString());
					}
				}
				data.put("codpro", codpro);
				data.put("cantidadventa",cantidad);
				data.put("totalventa",total);
			}else {
				data.put("codpro", codpro);
				data.put("cantidadventa",0);
				data.put("totalventa",0);
			}
			return data;
		} catch (Exception e) {
			LOGGER.info("obtenerCompraProducto"+e.getMessage());
			data.put("codpro", codpro);
			data.put("cantidadventa",0);
			data.put("totalventa",0);
			return data;
		}
	}
	@Override
	public List<Map<String, Object>> reporteVentasSemanal(){
		try {
			sqlString = "select cast(fini as date),date_part('dow',fini) as ndia,sum(coalesce((monfin-monini),0)) " + 
					"from caja c " + 
					"where estado=1 and codsuc =2 group by cast(fini as date),date_part('dow',fini) order by fini desc limit 7;";
			return db.queryForList(sqlString);
		} catch (Exception e) {
			// TODO: handle exception
			LOGGER.error("reporteVentasSemanal:"+e.toString());
			return null;
		}
	}
	@Override
	public List<Map<String, Object>> reporteVentasMensual(){
		try {
			sqlString = "select cast(fini as date),date_part('dow',fini) as ndia,sum(coalesce((monfin-monini),0)) " + 
					"from caja c " + 
					"where estado=1 and codsuc =2 group by cast(fini as date),date_part('dow',fini) order by fini desc limit 30;";
			return db.queryForList(sqlString);
		} catch (Exception e) {
			// TODO: handle exception
			LOGGER.error("reporteVentasMensual:"+e.toString());
			return null;
		}
	}
}