package Servicios;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import Modelos.Sucursal;

public interface SucursalS {
	/**
	 * Listar todas las sucursales segun filtro
	 * 
	 * @param start
	 * @param estado
	 * @param search
	 * @param length
	 * @return
	 * @throws SQLException
	 */
	public List<Map<String, Object>> listar(int start, boolean estado, String search, int length) throws SQLException ;

	/**
	 * Lista todas las sucursales activas
	 * 
	 * @return
	 */
	public List<Sucursal> listar() ;
	/**
	 * Obtiene la sucursal en base al codigo de sucursal enviado
	 * @param codsuc
	 * @return
	 */
	public Sucursal obtener(Integer codsuc);

	/**
	 * Adicionar sucursal
	 * 
	 * @param obj
	 * @return
	 */
	public boolean adicionar(Sucursal obj) ;

	/**
	 * Modificar una sucursal
	 * 
	 * @param c
	 * @param p
	 * @param log
	 * @param pass
	 */
	public void modificar(Sucursal obj);

	/**
	 * Eliminar una sucursal en base al codigo de la sucursal
	 * 
	 * @param obj
	 * @return
	 */
	public boolean eliminar(Sucursal obj) throws SQLException;
	/**
	 * Devuelve una lista de sucursales al cual esta asignado un usuario
	 * @param cod
	 * @return
	 * @throws SQLException
	 */
	public List<Sucursal> obtenerPorUsuario(Long cod)throws SQLException;
}
