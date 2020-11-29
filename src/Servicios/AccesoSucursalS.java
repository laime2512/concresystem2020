package Servicios;

import java.sql.SQLException;
import java.util.List;

import Modelos.Sucursal;
import Modelos.Usuario;

public interface AccesoSucursalS {
	/**
	 * Registra una asignacion de un usuario a una sucursal
	 * @param usuario
	 * @param sucursal
	 * @throws SQLException
	 */
	public void adicionar(Usuario usuario,Sucursal sucursal)throws SQLException;
	/**
	 * Da de baja a un usuario de una sucursal especifica 
	 * @param usuario
	 * @param sucursal
	 * @throws SQLException
	 */
	public void eliminar(Usuario usuario,Sucursal sucursal)throws SQLException;
	/**
	 * Da de baja de todas las sucursales de un usuario especifico
	 * @param usuario
	 * @throws SQLException
	 */
	public void eliminarTodosPorUsuario(Usuario usuario)throws SQLException;
	/**
	 * Sa de baja a todos los usuarios de una sucursal especifica
	 * @param sucursal
	 * @throws SQLException
	 */
	public void eliminarTodosPorSucursal(Sucursal sucursal)throws SQLException;
	/**
	 * Verifica si existe un usuario con una sucursal en especifico
	 * @param usuario
	 * @param sucursal
	 * @return
	 * @throws SQLException
	 */
	public Boolean existe(Usuario usuario,Sucursal sucursal)throws SQLException;
	/**
	 * Obtiene todas las sucursales por usuario
	 * @param usuario
	 * @return
	 * @throws SQLException
	 */
	public List<Sucursal> obtenerSucursalesPorUsuario(Usuario usuario)throws SQLException;
	/**
	 * Elimina las otras sucursales
	 * @param usuario
	 * @param vector
	 * @throws SQLException
	 */
	public void eliminarOtros(Usuario usuario,String vector)throws SQLException;
}
