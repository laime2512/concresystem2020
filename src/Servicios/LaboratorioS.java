package Servicios;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import Modelos.Laboratorio;

public interface LaboratorioS {
	/**
	 * Devuelve Lista de laboratorios segun filtro de busqueda=search, tamaniio=length, posicion=start y estado=activo o inactivo
	 * @param start
	 * @param estado
	 * @param search
	 * @param length
	 * @return Lista de laboratorios
	 * @throws SQLException
	 */
	public List<Map<String, Object>> listar(int start, boolean estado, String search, int length) throws SQLException ;
	/**
	 * Devuelve todos los laboratorios activos
	 * @return List<Map<String,Object>> Lista de laboratorios
	 */
	public List<Map<String, Object>> listar();
	/**
	 * Devuelve un laboratorio segun id especificado
	 * @param obj COdigo del laboratorio que se pretende obtener
	 * @return Map Laboratorio
	 * @throws SQLException
	 */
	public Map<String, Object> obtener(Laboratorio obj) throws SQLException ;
	/**
	 * Adiciona un laboratorio a la B.D.
	 * @param obj laboratorio que se desea adicionar
	 * @return Boolean true= se guardo correctamente, false=no se logro guardar
	 */
	public boolean adicionar(Laboratorio obj);
	/**
	 * Modifica los datos de un laboratorio especificado
	 * @param obj boolean, true=Se modifico correctamente, false=no se logro modificar
	 */
	public void modificar(Laboratorio obj) ;
	/**
	 * Elimina
	 * @param obj Laboratorio que se pretende eliminar
	 * @return Boolean true=se modifico a estado inactivo, false, no se logro dar de baja
	 * @throws SQLException
	 */
	public boolean eliminar(Laboratorio obj) throws SQLException;
}
