package Servicios;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import Modelos.Lugar;
import pagination.DataTableResults;

public interface LugarS {
	DataTableResults<Lugar> listar(HttpServletRequest request);
	List<Map<String, Object>> listar(int start, Integer estado, String search, int length,Integer codsuc) throws SQLException ;
	Map<String, Object> obtener(Lugar obj) ;
	Long adicionar(Lugar obj);
	Long obtenerOrAdicionarLugarPorProducto(Integer codsuc,Long codpro) ;
	void modificar(Lugar obj) ;
	boolean eliminar(Lugar obj) throws SQLException;
	Map<String, Object> verificar(Lugar obj);
	Lugar obtenerPorSucursalProducto(Integer codsuc,Long codpro)throws SQLException;
}
