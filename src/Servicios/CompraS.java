package Servicios;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import Modelos.Compra;
import Modelos.DetalleCompra;
import Modelos.ViewCompra;
import Wrap.CompraProductoWrap;
import Modelos.PagoCredito;
import Modelos.Valuacion;
import pagination.DataTableResults;

public interface CompraS {
	public ViewCompra obtener(Long codcompra) throws SQLException;
	public List<DetalleCompra> obtenerDetalleCompra(Long codcom);
	public DataTableResults<CompraProductoWrap> listarCompraProducto(HttpServletRequest request, Long codpro);
	public DataTableResults<ViewCompra> listar(HttpServletRequest request);
	public Long adicionar(Compra compra,Integer sucursal, Long[] productos, Integer[] cantidades, Float[] precios, Float[] totales,String vencimientos[],
			Float porcentajes[],Float descuentos[],Float impuestos[],Boolean devoluciones[], String tipos[]);
	public Long generarCodigo();
	public Long generarNumero(Integer codsuc);
	public boolean eliminar(Long codcompra);
	public void adicionarCredito(PagoCredito obj) ;
	/**
	 * Valuacion de inventario
	 * @param codpro
	 * @return
	 */
	public List<Valuacion> valuacionVenta(Long codpro, Integer codsuc);
	public List<Valuacion> valuacionCompra(Long codpro, Integer codsuc);
	public boolean finalizarCredito(Long codcompra);
	public Map<String, Object> obtenerCompraProducto(Long codpro, int meses, Integer codsuc);
}
