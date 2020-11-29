package Servicios;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import Modelos.Presentacion;

public interface PresentacionS {
	/**
	 * Devuelve Lista de Presentaciones segun filtro de busqueda=search, tamaniio=length, posicion=start y estado=activo o inactivo
	 * @param start
	 * @param estado
	 * @param search
	 * @param length
	 * @return Lista de laboratorios
	 * @throws SQLException
	 */
	public List<Map<String, Object>> listar(int start, boolean estado, String search, int length) throws SQLException ;
	/**
	 * Devuelve todos las Presentaciones activos
	 * @return List<Map<String,Object>> Lista de laboratorios
	 */
	public List<Map<String, Object>> listar();
	/**
	 * Devuelve una Presentacion segun id especificado
	 * @param obj Codigo del area que se pretende obtener
	 * @return Map Presentacion
	 * @throws SQLException
	 */
	public Map<String, Object> obtener(Presentacion obj) throws SQLException ;
	/**
	 * Adiciona una Presentacion a la B.D.
	 * @param obj area que se desea adicionar
	 * @return Boolean true= se guardo correctamente, false=no se logro guardar
	 */
	public boolean adicionar(Presentacion obj);
	/**
	 * Modifica los datos de una area especificada
	 * @param obj boolean, true=Se modifico correctamente, false=no se logro modificar
	 */
	public void modificar(Presentacion obj) ;
	/**
	 * Elimina
	 * @param obj Presentacion que se pretende eliminar
	 * @return Boolean true=se modifico a estado inactivo, false, no se logro dar de baja
	 * @throws SQLException
	 */
	public boolean eliminar(Presentacion obj) throws SQLException;
}
