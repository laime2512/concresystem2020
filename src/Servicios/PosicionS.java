package Servicios;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import Modelos.Posicion;

public interface PosicionS {
	/**
	 * Devuelve Lista de Posicions segun filtro de busqueda=search, tamaniio=length, posicion=start y estado=activo o inactivo
	 * @param start
	 * @param estado
	 * @param search
	 * @param length
	 * @return Lista de laboratorios
	 * @throws SQLException
	 */
	public List<Map<String, Object>> listar(int start, Integer estado, String search, int length) throws SQLException ;
	/**
	 * Devuelve todos las Posicions activos
	 * @return List<Map<String,Object>> Lista de laboratorios
	 */
	public List<Map<String, Object>> listar();
	/**
	 * Devuelve una lista de posiciones por el codigo del mueble
	 * @param cod
	 * @return
	 * @throws SQLException
	 */
	public List<Map<String, Object>> listarPorMueble(Integer cod)throws SQLException;
	/**
	 * Devuelve una Posicion segun id especificado
	 * @param obj Codigo del area que se pretende obtener
	 * @return Map Posicion
	 * @throws SQLException
	 */
	public Map<String, Object> obtener(Posicion obj) throws SQLException ;
	/**
	 * Adiciona una Posicion a la B.D.
	 * @param obj area que se desea adicionar
	 * @return Boolean true= se guardo correctamente, false=no se logro guardar
	 */
	public boolean adicionar(Posicion obj);
	/**
	 * Modifica los datos de una area especificada
	 * @param obj boolean, true=Se modifico correctamente, false=no se logro modificar
	 */
	public void modificar(Posicion obj) ;
	/**
	 * Elimina
	 * @param obj Posicion que se pretende eliminar
	 * @return Boolean true=se modifico a estado inactivo, false, no se logro dar de baja
	 * @throws SQLException
	 */
	public boolean eliminar(Posicion obj) throws SQLException;
	/**
	 * Elimina todas lass posiciones de un mueble
	 * @param codmueble
	 * @throws SQLException
	 */
	public void eliminarTodoPorMueble(Integer codmueble)throws SQLException;
	/**
	 * Lista todas las posiciones de un mueble
	 * @param codmueble
	 * @return
	 */
	public List<Map<String, Object>> listarPosicionesPorMueble(Integer codmueble);
}
