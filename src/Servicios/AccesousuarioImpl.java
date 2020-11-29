package Servicios;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import Modelos.Accesousuario;
import Modelos.Backup;
import Modelos.Usuario;

@Service
public class AccesousuarioImpl implements AccesoUsuarioS{

	@Autowired
	private SucursalS sucursalS;
	@Autowired
	private RolS rolS;
	private final static Logger LOGGER=Logger.getLogger("AccesousuarioImpl");
	JdbcTemplate db;
	@Autowired
	public void setJdbcTemplate(DataSource dataSource) {
		this.db = new JdbcTemplate(dataSource);
	}
	public Usuario obtenerUser(Long cod) {
		try {
			return db.queryForObject("select * from usuario where codusu=?", new BeanPropertyRowMapper<Usuario>(Usuario.class),cod);
		} catch (Exception e) {
			LOGGER.info("obtenerUser:"+e.toString());
			return null;
		}
	}
	public String obtenerCi(Long codusu) throws SQLException{
		return db.queryForObject("select ci from datos where codusu=?", String.class, codusu);
	}

	public Accesousuario obtenerUsuario(Long codusu)  throws SQLException{
		return db.queryForObject("select * from accesousuario where codusu=?",
				new BeanPropertyRowMapper<Accesousuario>(Accesousuario.class), codusu);
	}

	public Usuario iniciarSesion(String usuario, String clave)  throws SQLException{
		try {
			Usuario us = db.queryForObject(
					"select p.* from usuario p join accesousuario u on u.codusu=p.codusu and u.login=? and u.passwd=? and u.estado=1  where p.estado=1",
					new BeanPropertyRowMapper<Usuario>(Usuario.class), usuario, clave);
			if(us==null)
				return null;
			us.setUsuario(obtener(us.getCodusu()));
			us.setSucursales(sucursalS.obtenerPorUsuario(us.getCodusu()));
			if(us.getUsuario()!=null)
				us.setRoles(rolS.listarRolesXlogin(us.getUsuario().getLogin()));
			else
				us.setRoles(null);
			return us;
		} catch (Exception e) {
			LOGGER.info("error iniciar_sesion:" + e.toString());
			return null;
		}
	}

	public Accesousuario obtener(Long codusu)  throws SQLException{
		try {
			return db.queryForObject("select * from accesousuario where codusu=?",
					new BeanPropertyRowMapper<Accesousuario>(Accesousuario.class), codusu);
		} catch (Exception e) {
			return null;
		}
	}

	public List<Map<String, Object>> obtenerRoles(Long cod)  throws SQLException{
		try {
			return db.queryForList(
					"select rol.codr,rol.nombre from rolusu join rol on rol.codr=rolusu.codr join accesousuario on accesousuario.login=rolusu.login and codusu=?", cod);
		} catch (Exception e) {
			LOGGER.info("obtenerRoles:"+e.toString());
			return null;
		}
	}

	public boolean guardarFoto(String foto, Long codusu)  throws SQLException{
		try {
			db.update("update usuario set foto=? where codusu=?", foto, codusu);
			return true;
		} catch (Exception e) {
			LOGGER.info("guardar foto:"+e.toString());
			return false;
		}
	}

	public List<Map<String, Object>> listar()  throws SQLException{
		return db.queryForList(
				"select p.*, case when((select count(*) from accesousuario u where u.codusu=p.codusu and u.estado=1)>0)then 1 else 0 end as es_usuario from usuario p");
	}

	public Map<String, Object> obtener(String ci) {
		try {
			return db.queryForMap("select p.* from usuario p where p.ci=?", ci);
		} catch (Exception e) {
			LOGGER.info("obtenerUsuario:" + e.toString());
			return null;
		}
	}

	public Map<String, Object> obtenerXcodusu(Long codusu)  throws SQLException{
		try {
			String sql = "select p.*,to_char(fnac,'DD/MM/YY') as xfnac,u.login,u.passwd from usuario p left join accesousuario u on u.codusu=p.codusu  where p.codusu=?";
			return db.queryForMap(sql, codusu);
		} catch (Exception e) {
			LOGGER.info("obtenerXcodusu:" + e.toString());
			return null;
		}
	}
	public List<Map<String, Object>> obtenerPorSucursal(Integer codsuc)throws SQLException{
		try {
			String sql="select u.* from usuario u join acceso_sucursal a_s on a_s.codusu=u.codusu and a_s.codsuc=? and a_s.fecbaja is null where u.estado=1";
			return db.queryForList(sql,codsuc);
		} catch (Exception e) {
			LOGGER.info("error al obtenerPorSucursal:"+e.toString());
			return null;
		}
	}

	public boolean adicionar(Usuario p, String ci)  throws SQLException{
		try {
			String sql;
			Long codusu = generarCodigo();
			p.setNombre(convertUpperCase(p.getNombre()));
			p.setAp(convertUpperCase(p.getAp()));
			p.setAm(convertUpperCase(p.getAm()));
			sql = "insert into usuario(codusu,nombre,ap,am,genero,tipoper,foto,ci,ecivil,alias,fnac) values(?,?,?,?,?,?,?,?,?,?,to_date(?,'dd/MM/yy'));";
			db.update(sql, codusu, p.getNombre(), p.getAp(), p.getAm(), p.getGenero(), p.getTipoper(), p.getFoto(), ci
					,p.getEcivil(),p.getAlias(),p.getFnac());
			return true;
		} catch (Exception e) {
			LOGGER.error("adicionar:" + e.toString());
			return false;
		}
	}
	private String convertUpperCase(String cad) {
		if(cad==null)
			return null;
		return cad.toUpperCase();
	}

	public boolean modificar(Usuario p)  throws SQLException{
		try {
			String sql;
			p.setNombre(convertUpperCase(p.getNombre()));
			p.setAp(convertUpperCase(p.getAp()));
			p.setAm(convertUpperCase(p.getAm()));
			sql = "update usuario set (nombre,ap,am,genero,tipoper,foto,ci,ecivil,alias,fnac)=(?,?,?,?,?,?,?,?,?,to_date(?,'dd/MM/yy')) where codusu=?;";
			db.update(sql, p.getNombre(), p.getAp(), p.getAm(), p.getGenero(), p.getTipoper(), p.getFoto(), p.getCi(),
					p.getEcivil(),p.getAlias(),p.getFnac(),p.getCodusu());
			return true;
		} catch (Exception e) {
			LOGGER.error("modificar:" + e.toString());
			return false;
		}
	}

	public Long generarCodigo()  throws SQLException{
		String sql = "select COALESCE(max(codusu),0)+1 as codusu from usuario";
		return db.queryForObject(sql, Long.class);
	}

	public List<Map<String, Object>> listar(int start, int estado, String search, String tipo, int length) throws SQLException {
		int tot = db.queryForObject("select count(*) from usuario where estado=?", Integer.class, estado);
		if (search == null)
			search = "";
		if (tipo.equals("T"))
			return db.queryForList(
					"select usuario.*,(select string_agg(sucursal.nombre,', ') from acceso_sucursal join sucursal on sucursal.codsuc=acceso_sucursal.codsuc where acceso_sucursal.codusu=usuario.codusu and acceso_sucursal.fecbaja is null) xsucursales,(select string_agg(rol.nombre,', ') from rolusu join rol on rol.codr=rolusu.codr join accesousuario on accesousuario.login=rolusu.login and accesousuario.codusu=usuario.codusu) xroles,row_number() OVER(ORDER BY usuario.codusu) as RN,? as Tot,(select count(*) from rolusu join accesousuario on accesousuario.login=rolusu.login and accesousuario.codusu=usuario.codusu) rol,(select count(*) from accesousuario where accesousuario.codusu=usuario.codusu and accesousuario.estado=1) clave from usuario where estado=? and concat(upper(nombre),' ',upper(ap),' ',upper(am)) like concat('%',upper(?),'%') LIMIT ? OFFSET ?",
					tot, estado, search, length, (start));
		else
			return db.queryForList(
					"select usuario.*,(select string_agg(sucursal.nombre,', ') from acceso_sucursal join sucursal on sucursal.codsuc=acceso_sucursal.codsuc where acceso_sucursal.codusu=usuario.codusu and acceso_sucursal.fecbaja is null) xsucursales,(select string_agg(rol.nombre,', ') from rolusu join rol on rol.codr=rolusu.codr join accesousuario on accesousuario.login=rolusu.login and accesousuario.codusu=usuario.codusu) xroles,row_number() OVER(ORDER BY usuario.codusu) as RN,? as Tot,(select count(*) from rolusu join accesousuario on accesousuario.login=rolusu.login and accesousuario.codusu=usuario.codusu) rol,(select count(*) from accesousuario where accesousuario.codusu=usuario.codusu and accesousuario.estado=1) clave from usuario where estado=? and tipoper=? and concat(upper(nombre),' ',upper(ap),' ',upper(am)) like concat('%',upper(?),'%') LIMIT ? OFFSET ?",
					tot, estado, tipo, search, length, (start));
	}

	public boolean eliminar(Long codusu)  throws SQLException{
		try {
			db.update("update usuario set estado=0,foto='user.png' where codusu=?", codusu);
			return true;
		} catch (Exception e) {
			LOGGER.error("eliminar:" + e.toString());
			return false;
		}
	}

	public boolean habilitar(Long codusu)  throws SQLException{
		try {
			db.update("update usuario set estado=1 where codusu=?", codusu);
			return true;
		} catch (Exception e) {
			LOGGER.info("habilitar:" + e.toString());
			return false;
		}
	}

	public boolean asignar(Long codusu, String login, String password) throws SQLException {
		try {
			db.update("insert into accesousuario(login,passwd,codusu,estado) values(?,?,?,1)", login, password, codusu);
			return true;
		} catch (Exception e) {
			LOGGER.error("asignar:" + e.toString());
			return false;
		}
	}
	public boolean asignarRoles(String login, Integer roles[]) throws SQLException {
		try {
			db.update("delete from rolusu where login=?",login);
			if(roles!=null)
			for (int i = 0; i < roles.length; i++) {
				db.update("insert into rolusu(login,codr) values(?,?)",login,roles[i]);
			}
			return true;
		} catch (Exception e) {
			LOGGER.info("asignarRoles:" + e.toString());
			return false;
		}
	}

	public boolean reasignar(Long codusu, String login, String password)  throws SQLException{
		try {
			db.update("update accesousuario set (login,passwd)=(?,?) where codusu=?", login, password, codusu);
			return true;
		} catch (Exception e) {
			LOGGER.info("reasignar:" + e.toString());
			return false;
		}
	}

	public boolean darestado(Long codusu, int estado)  throws SQLException{
		try {
			db.update("update usuario set estado=? where codusu =?", estado, codusu);
			return true;
		} catch (Exception e) {
			LOGGER.info("darestado:" + e.toString());
			return false;
		}

	}
	public Long adicionarBackup(String descripcion, String usuario) throws SQLException {
		try {
			Long cod = db.queryForObject("select coalesce(max(cod),0)+1 from backup", Long.class);
			db.update("insert into backup(cod,xuser,descripcion) values(?,?,?)", cod,descripcion,usuario);
			return cod;
		} catch (Exception e) {
			LOGGER.error("adicionarBackup:" + e.toString());
			return -1L;
		}
	}
	public List<Backup> listarBackup(){
		try {
			return db.query("select * from backup order by cod desc;", new BeanPropertyRowMapper<Backup>(Backup.class));
		} catch (Exception e) {
			LOGGER.error("listarBackup:"+e.toString());
			return null;
		}
	}
}