package Servicios;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import Modelos.Inventario;
import Modelos.Producto;
import Modelos.ViewAlmacen;
import Modelos.ViewProducto;
import pagination.DataTableResults;

public interface ProductoS {
	public ViewProducto obtenerPorAlmacen(Long codAlmacen);
	/**
	 * Obtiene el producto
	 * @param idproducto
	 * @return
	 */
	Producto obtener(Long idproducto);
	boolean adicionar(Producto p);
	boolean modificar(Producto p);
	boolean guardarFoto(String foto,Long idproducto);
	Long generarCodigo();
	DataTableResults<Producto> listar(HttpServletRequest request);
	DataTableResults<Producto> listarIncompletos(HttpServletRequest request);
	List<Map<String, Object>> posicion();
	List<Map<String, Object>> muebles();
	List<Map<String, Object>> listarxcategoria(int codcat);
	List<Map<String, Object>> listarxtipo(int codtip);
	boolean eliminar(Long idproducto);
	boolean darestado(Long idproducto, int estado);
	List<Map<String, Object>> buscarproducto(String search);
	List<Map<String, Object>> buscarproductoc(String search);
	DataTableResults<ViewAlmacen> listPorKardex(HttpServletRequest request,Long codpro,String nombre,String codigo,Integer codlab);
	List<Producto> listarTodos();
	DataTableResults<Inventario> listarInventario(HttpServletRequest request);
	int obtenerCantidadEnUnidades(Long codpro, Float cantidad, String tipoCantidad);
	Inventario buscarPorCodigoBarra(HttpServletRequest request,String codebar);
}