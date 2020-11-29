package Servicios;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import Modelos.Margen;
import Wrap.MargenWrap;
import pagination.DataTableResults;

public interface MargenS {
	DataTableResults<Margen> listar(HttpServletRequest request);
	List<Map<String, Object>> listar();
	Margen obtener(Margen obj) throws SQLException;
	boolean adicionar(MargenWrap obj);
	void modificar(MargenWrap obj);
	boolean eliminar(Margen obj) throws SQLException;
	Margen getLastMargen() throws SQLException;
}
