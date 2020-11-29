package Servicios;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import Modelos.Almacen;
import Modelos.ViewAlmacen;
import Wrap.AlmacenUngroupWrap;
import Wrap.AlmacenWrap;
import pagination.DataTableResults;

public interface AlmacenS {
	Long adicionar(Almacen a);
	DataTableResults<ViewAlmacen> listar(HttpServletRequest request);
	DataTableResults<AlmacenUngroupWrap> listarAlmacenDesagrupado(HttpServletRequest request);
	List<AlmacenWrap> obtenerAlmacenPorProducto(Long codpro, Integer codsuc);
	ViewAlmacen obtenerTotalProducto(Long codpro, Integer codSuc);
	Almacen obtener(Long codalmacen);
	List<ViewAlmacen> listarProductosStockCero(Integer codSuc);
	List<ViewAlmacen> listViewAlmacen(Integer codSuc);
	DataTableResults<ViewAlmacen> searchByLaboratoryAndClasification(HttpServletRequest request, Integer codlab, String clasification, Integer dias);
}