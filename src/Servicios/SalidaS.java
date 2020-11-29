package Servicios;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import Modelos.Salida;
import Modelos.ViewDetalleSalida;
import Wrap.SalidaProductoWrap;
import pagination.DataTableResults;

public interface SalidaS {
	Salida obtener(Long codsal) throws SQLException;
	public List<ViewDetalleSalida> obtenerDetalles(Long codsal);
	DataTableResults<Salida> filter(HttpServletRequest request,int tipo);
	void adicionar(Salida obj);
	Long adicionarRevision(Salida obj);
	Map<String,Object> eliminar(Salida obj);
	public boolean esTraspasoFarmacias(int tipo);
//	List<Salida> listarPorProducto(Long codpro,boolean inOut, Integer codsuc);
	DataTableResults<SalidaProductoWrap> listarSalidaProducto(HttpServletRequest request, Long codpro);
}
