package Servicios;

import java.util.List;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import Modelos.Almacen;
import Modelos.Lugar;
import Modelos.Producto;
import Utiles.Constantes;

@Service
@Transactional
public class SistemaImpl implements SistemaS{
	private static final Logger LOGGER = Logger.getLogger("SistemaImpl");
	
	JdbcTemplate db;
	@Autowired
	public void setJdbcTemplate(DataSource dataSource) {
		this.db = new JdbcTemplate(dataSource);
	}
	
	@Autowired
	private AlmacenS almacenS;
	@Autowired
	private LugarS lugarS;
	@Autowired
	private ProductoS productoS;
	
	public void resetTransaccion(Integer codsuc) {
		try {
			db.update("delete from detallepedido where detallepedido.codped in (select pedido.codped from pedido where pedido.codped=detallepedido.codped and pedido.codsuc=?);",codsuc);
			db.update("delete from pedido where codsuc=?",codsuc);
			db.update("delete from detalleventa where detalleventa.codven in(select venta.codven from venta where venta.codven=detalleventa.codven and venta.codsuc=?)",codsuc);
			db.update("delete from factura where factura.codven in(select venta.codven from venta where venta.codven=factura.codven and venta.codsuc=?)",codsuc);
			db.update("delete from venta where codsuc=?;",codsuc);
			db.update("delete from detallecaja where detallecaja.codcaja in (select caja.codcaja from caja where caja.codcaja=detallecaja.codcaja and caja.codsuc=?);",codsuc);
			db.update("delete from caja where caja.codsuc =?;",codsuc);
			
			db.update("delete from detallecompra where detallecompra.codcom in (select compra.codcom from compra where compra.codcom=detallecompra.codcom and compra.codsuc=?);",codsuc);
			db.update("delete from pagocredito where pagocredito.codcom in (select compra.codcom from compra where compra.codcom=pagocredito.codcom and compra.codsuc=?);;",codsuc);
			db.update("delete from compra where codsuc =?;",codsuc);
			db.update("delete from detalle_salida where detalle_salida.codsal in (select salida.codsal from salida where salida.suc_origen=?)",codsuc);
			db.update("delete from salida where suc_origen=?;",codsuc);
		} catch (DataAccessException e) {
			LOGGER.info("resetTransaccion:"+e.toString());
		}
		
	}
	public void resetAlmacenTable(Integer codsuc) {
		try {
			db.update("delete from almacen where almacen.codlugar in (select lugar.codlugar from lugar where lugar.codsuc=?);",codsuc);
			db.update("delete from lugar where codsuc=?;",codsuc);
		} catch (DataAccessException e) {
			LOGGER.info("resetAlmacenTable:"+e.toString());
		}
	}
	public String generateProductoAlmacen(Integer cantidad, Integer sucursal){
		String msgResponse = "";
			resetTransaccion(sucursal);
			resetAlmacenTable(sucursal);
			//Listar codigos de los productos
			List<Producto> productoList = productoS.listarTodos();
			long countRegister = 0L;
			for (Producto product : productoList) {
				if(validProduct(product)) {
					try {
						Lugar lugar = new Lugar();
						lugar.setCodposicion(Constantes.SIN_POSICION);
						lugar.setCodpro(product.getCodpro());
						lugar.setCodsuc(sucursal);
						Long codeLugar = lugarS.adicionar(lugar);
						Almacen almacen = new Almacen();
						almacen.setCodlugar(codeLugar);
						almacen.setCantidad(cantidad);
						almacenS.adicionar(almacen);
						countRegister++;
						} catch (Exception e) {
							LOGGER.error("generateProductoAlmacen:"+e.toString());
							msgResponse += "Transaccion fallida, producto codigo: "+product.getCodpro()+"\n";
						}
				}
				
			}
			msgResponse += "Ejecucion completada, "+countRegister+" registros guardados.";
			return msgResponse;
	}
	private boolean validProduct(Producto obj) {
		if(obj.getNombre() == null) return false;
		if(obj.getEstado() == null) return false;
		if(obj.getCodtip() == null) return false;
		if(obj.getGenerico() == null) return false;
		if(obj.getCodlab() == null) return false;
		if(obj.getCodmed() == null) return false;
		if(obj.getCodare() == null) return false;
		if(obj.getPcUnit() == null) return false;
		if(obj.getPcCaja() == null) return false;
		if(obj.getPcPaquete() == null) return false;
		if(obj.getPvUnit() == null) return false;
		if(obj.getPvCaja() == null) return false;
		if(obj.getPvPaquete() == null) return false;
		if(obj.getPorcentajeUnidad() == null) return false;
		if(obj.getPorcentajeCaja() == null) return false;
		if(obj.getPorcentajePaquete() == null) return false;
		if(obj.getUnixcaja() == null) return false;
		if(obj.getUnixpaquete() == null) return false;
		if(obj.getUniEnPaquete() == null) return false;
		if(obj.getPorcentajeUnidad() == null) return false;
		if(obj.getPorcentajeCaja() == null) return false;
		if(obj.getPorcentajePaquete() == null) return false;
		if(obj.getPresentacionUnidad() == null) return false;
		if(obj.getPresentacionCaja() == null) return false;
		if(obj.getPresentacionPaquete() == null) return false;
		if(obj.getTipoCompra() == null) return false;
		if(obj.getInventarioMinimoUnidad() == null) return false;
		if(obj.getInventarioMinimoCaja() == null) return false;
		if(obj.getInventarioMinimoPaquete() == null) return false;
		return true;
	}
}
