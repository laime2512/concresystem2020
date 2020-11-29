package Servicios;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import Modelos.Accesousuario;
import Modelos.Backup;
import Modelos.Usuario;

public interface AccesoUsuarioS {
	/**
	 * Devuelve el carnet de identidad del codigo de usuario que se envia como parametro
	 * @param codusu
	 * @return String
	 */
	public String obtenerCi(Long codusu) throws SQLException;
	/**
	 * Devuelve los datos de acceso del usuario
	 * @param codusu
	 * @return
	 */
	public Accesousuario obtenerUsuario(Long codusu) throws SQLException;
	/**
	 * Devuelve al usuario , si envio correctamente los datos de acceso
	 * @param usuario
	 * @param clave
	 * @return Usuario
	 */
	public Usuario iniciarSesion(String usuario, String clave) throws SQLException;
	/**
	 * Devuelve los datos de acceso del usuario mediante su codigo
	 * @param codusu
	 * @return
	 */
	public Accesousuario obtener(Long codusu) throws SQLException;
	/**
	 * Obtiene los roles del usuario
	 * @param cod
	 * @return
	 */
	public List<Map<String, Object>> obtenerRoles(Long cod) throws SQLException;
	/**
	 * Guarda la foto del usuario
	 * @param foto
	 * @param codusu
	 * @return boolean, True=se guardo correctamente, false=no se logro guardar
	 */
	public boolean guardarFoto(String foto, Long codusu) throws SQLException;
	/**
	 * Lista los usuarios, con una columna bandera si tiene datos o no.
	 * @return
	 */
	public List<Map<String, Object>> listar() throws SQLException;
	/**
	 * Obtener usuario por carnet de identidad
	 * @param ci
	 * @return
	 */
	public Map<String, Object> obtener(String ci) throws SQLException;
	/**
	 * Obtiene un usuario cod codigo de usuario
	 * @param codusu
	 * @return
	 */
	public Map<String, Object> obtenerXcodusu(Long codusu) throws SQLException;
	/**
	 * Adiciona un usuario
	 * @param p
	 * @param ci
	 * @return
	 */
	public boolean adicionar(Usuario p, String ci) throws SQLException;
	/**
	 * Modifica a un usuario
	 * @param p
	 * @return
	 */
	public boolean modificar(Usuario p) throws SQLException;
	/**
	 * Genera un codigo
	 * @return
	 */
	public Long generarCodigo() throws SQLException;
	/**
	 * Lista de usuario bajo un rango y filtro de busqueda
	 * @param start
	 * @param estado
	 * @param search
	 * @param tipo
	 * @param length
	 * @return
	 */
	public List<Map<String, Object>> listar(int start, int estado, String search, String tipo, int length)  throws SQLException;
	/**
	 * Elimina a un usuario
	 * @param codusu
	 * @return
	 */
	public boolean eliminar(Long codusu)  throws SQLException;
	/**
	 * Habilita a un usuario
	 * @param codusu
	 * @return
	 */
	public boolean habilitar(Long codusu) throws SQLException;
	/**
	 * Asigna a un usuario
	 * @param codusu
	 * @param login
	 * @param password
	 * @return
	 */
	public boolean asignar(Long codusu, String login, String password)  throws SQLException;
	/**
	 * Devuelve true= si guardo correctamente los roles para un usuario, false= no logro guardar
	 * @param login
	 * @param roles
	 * @return
	 * @throws SQLException
	 */
	public boolean asignarRoles(String login, Integer roles[]) throws SQLException;
	/**
	 * Reasigna a un usuario
	 * @param codusu
	 * @param login
	 * @param password
	 * @return
	 */
	public boolean reasignar(Long codusu, String login, String password) throws SQLException;
	/**
	 * Cambia el estado de un usuario
	 * @param codusu
	 * @param estado
	 * @return
	 */
	public boolean darestado(Long codusu, int estado) throws SQLException;
	
	public List<Map<String, Object>> obtenerPorSucursal(Integer codsuc)throws SQLException;
	public Long adicionarBackup(String descripcion, String usuario) throws SQLException;
	public List<Backup> listarBackup();
	public Usuario obtenerUser(Long cod) ;
}
