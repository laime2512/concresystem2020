package Servicios;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import Modelos.TipoMueble;

public interface TipoMuebleS {
	/**
	 * Devuelve Lista de TipoMuebles segun filtro de busqueda=search, tamaniio=length, posicion=start y estado=activo o inactivo
	 * @param start
	 * @param estado
	 * @param search
	 * @param length
	 * @return Lista de laboratorios
	 * @throws SQLException
	 */
	public List<Map<String, Object>> listar(int start, boolean estado, String search, int length) throws SQLException ;
	/**
	 * Devuelve todos las TipoMuebles activos
	 * @return List<Map<String,Object>> Lista de laboratorios
	 */
	public List<Map<String, Object>> listar();
	/**
	 * Devuelve una TipoMueble segun id especificado
	 * @param obj Codigo del area que se pretende obtener
	 * @return Map TipoMueble
	 * @throws SQLException
	 */
	public Map<String, Object> obtener(TipoMueble obj) throws SQLException ;
	/**
	 * Adiciona una TipoMueble a la B.D.
	 * @param obj area que se desea adicionar
	 * @return Boolean true= se guardo correctamente, false=no se logro guardar
	 */
	public boolean adicionar(TipoMueble obj);
	/**
	 * Modifica los datos de una area especificada
	 * @param obj boolean, true=Se modifico correctamente, false=no se logro modificar
	 */
	public void modificar(TipoMueble obj) ;
	/**
	 * Elimina
	 * @param obj TipoMueble que se pretende eliminar
	 * @return Boolean true=se modifico a estado inactivo, false, no se logro dar de baja
	 * @throws SQLException
	 */
	public boolean eliminar(TipoMueble obj) throws SQLException;
}
