package Servicios;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import Modelos.Caja;
import Modelos.Venta;
import Modelos.ViewDetalleVenta;
import Modelos.ViewVenta;
import Wrap.VentaProductoWrap;
import pagination.DataTableResults;

public interface VentaS {
	ViewVenta obtener(Long codv);
	List<ViewDetalleVenta> obtenerDetalles(Long codv);
	DataTableResults<VentaProductoWrap> listarVentaProducto(HttpServletRequest request,Long codpro);
	DataTableResults<ViewVenta> listar(HttpServletRequest request);
	DataTableResults<ViewVenta> listarPorCaja(HttpServletRequest request,Caja caja);
	Long adicionar(Venta v,Integer codsuc,Long productos[],Float cantidades[],Float precios[],Float subtotales[],String tipoVenta[],Long promociones[]);
	Long adicionarPedido(Venta v,Long productos[],Integer cantidades[],Float precios[],Float subtotales[],Long[] lugares,Integer codsuc,Integer opt_cant[],Boolean desc[]);
	Long generarCodigo();
	boolean eliminar(Long codv,Long codcaja);
	Map<String, Object> obtenerVentaProducto(Long codpro, int meses, Integer codsuc);
	List<Map<String, Object>> reporteVentasSemanal();
	List<Map<String, Object>> reporteVentasMensual();
}
