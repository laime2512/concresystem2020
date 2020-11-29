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
import org.springframework.transaction.annotation.Transactional;

import Modelos.Sucursal;

@Service
@Transactional
public class SucursalImpl implements SucursalS {
	private String sqlString;
	private Logger LOGGER = Logger.getLogger("SucursalImpl");

	JdbcTemplate db;
	@Autowired
	public void setJdbcTemplate(DataSource dataSource) {
		this.db = new JdbcTemplate(dataSource);
	}

	public List<Map<String, Object>> listar(int start, boolean estado, String search, int length) throws SQLException {
		if (estado) {
			int tot = db.queryForObject("select count(*) from sucursal where estado=?", Integer.class, estado);
			if (search == null)
				search = "";
			sqlString = "select sucursal.*,row_number() OVER(ORDER BY sucursal.codsuc) as RN,? as Tot from sucursal where sucursal.estado=? and upper(nombre) like concat('%',upper(?),'%') LIMIT ? OFFSET ?";
			return db.queryForList(sqlString, tot, estado, search, length, start);
		} else
			return null;
	}

	public List<Sucursal> listar() {
		try {
			return db.query("select * from sucursal where estado=true;", new BeanPropertyRowMapper<Sucursal>(Sucursal.class));
		} catch (Exception e) {
			LOGGER.info("listar:"+e.toString());
			return null;
		}
	}
	public Sucursal obtener(Integer codsuc) {
		try {
			sqlString = "select * from sucursal where codsuc=?";
			return db.queryForObject(sqlString,new BeanPropertyRowMapper<Sucursal>(Sucursal.class), codsuc);
		} catch (Exception e) {
			LOGGER.info("obtener:"+e.toString());
			return null;
		}
	}

	public boolean adicionar(Sucursal obj) {
		try {
			sqlString = "insert into sucursal(codsuc,nombre,telefono,direccion,estado) values((select coalesce(max(codsuc),0)+1 from sucursal),?,?,?,true);";
			db.update(sqlString, obj.getNombre(), obj.getTelefono());
			return true;
		} catch (Exception e) {
			LOGGER.error("adicionar:"+e.toString());
			return false;
		}
	}

	public void modificar(Sucursal obj) {
		try {
			sqlString = "update sucursal set nombre=?,telefono=?,direccion=? where codsuc=?;";
			db.update(sqlString, obj.getNombre(), obj.getTelefono(),obj.getDireccion(), obj.getCodsuc());
		} catch (Exception e) {
			LOGGER.error("modificar:"+e.toString());
		}
	}


	public boolean eliminar(Sucursal obj) throws SQLException{
		try {
			db.update("update sucursal set estado=false where codsuc=?", obj.getCodsuc());
			return true;
		} catch (Exception e) {
			LOGGER.error("eliminar:"+e.toString());
			return false;
		}
	}
	public List<Sucursal> obtenerPorUsuario(Long cod)throws SQLException{
		try {
			return db.query("select sucursal.* from sucursal join acceso_sucursal on acceso_sucursal.codsuc=sucursal.codsuc and acceso_sucursal.codusu=? where acceso_sucursal.fecbaja is null",new BeanPropertyRowMapper<Sucursal>(Sucursal.class),cod);
		} catch (Exception e) {
			LOGGER.info("obtenerPorUsuario:"+e.toString());
			return null;
		}
	}
}
