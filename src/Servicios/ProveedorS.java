package Servicios;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import Modelos.Proveedor;
import pagination.DataTableResults;

public interface ProveedorS {

	Map<String, Object> obtener(Long codproveedor);

	boolean adicionar(Proveedor p);

	boolean modificar(Proveedor p);

	int generarCodigo();

	DataTableResults<Proveedor> listado(HttpServletRequest request);

	List<Map<String, Object>> listar(int start, int estado, String search, int length);

	List<Map<String, Object>> listar();

	boolean darestado(Long codproveedor, int estado);

}