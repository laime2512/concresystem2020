package Servicios;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import Modelos.Mueble;

public interface MuebleS {
	/**
	 * Devuelve Lista de Muebles segun filtro de busqueda=search, tamaniio=length, posicion=start y estado=activo o inactivo
	 * @param start
	 * @param estado
	 * @param search
	 * @param length
	 * @return Lista de laboratorios
	 * @throws SQLException
	 */
	public List<Map<String, Object>> listar(int start, Integer estado, String search, int length) throws SQLException ;
	/**
	 * Devuelve todos las Muebles activos
	 * @return List<Map<String,Object>> Lista de laboratorios
	 */
	public List<Map<String, Object>> listar();
	/**
	 * Devuelve una Mueble segun id especificado
	 * @param obj Codigo del area que se pretende obtener
	 * @return Map Mueble
	 * @throws SQLException
	 */
	public Map<String, Object> obtener(Mueble obj) throws SQLException ;
	/**
	 * Retorna una lista de muebles con el tipo de mueble especificado
	 * @param codtimu
	 * @return List de muebles
	 * @throws SQLException
	 */
	public List<Map<String, Object>> obtenerPorTipoMueble(Integer codtimu)throws SQLException;
	/**
	 * Adiciona una Mueble a la B.D.
	 * @param obj area que se desea adicionar
	 * @return Boolean true= se guardo correctamente, false=no se logro guardar
	 */
	public Integer adicionar(Mueble obj);
	/**
	 * Modifica los datos de una area especificada
	 * @param obj boolean, true=Se modifico correctamente, false=no se logro modificar
	 */
	public void modificar(Mueble obj) ;
	/**
	 * Elimina
	 * @param obj Mueble que se pretende eliminar
	 * @return Boolean true=se modifico a estado inactivo, false, no se logro dar de baja
	 * @throws SQLException
	 */
	public boolean eliminar(Mueble obj) throws SQLException;
}
