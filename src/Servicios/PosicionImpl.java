package Servicios;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import Modelos.Posicion;

@Service
@Transactional
public class PosicionImpl implements PosicionS{
	private String consulta;
	private String TABLE="posicion";
	private Logger log = Logger.getLogger(PosicionImpl.class.getName());

	JdbcTemplate db;
	@Autowired
	public void setJdbcTemplate(DataSource dataSource) {
		this.db = new JdbcTemplate(dataSource);
	}

	public List<Map<String, Object>> listar(int start, Integer estado, String search, int length) throws SQLException {
		if (estado==1) {
			int tot = db.queryForObject("select count(*) from posicion where estado=?", Integer.class, estado);
			if (search == null)
				search = "";
			consulta = "select posicion.*,row_number() OVER(ORDER BY posicion.codposicion) as RN,? as Tot from posicion where posicion.estado=? and upper(nombre) like concat('%',upper(?),'%') LIMIT ? OFFSET ?";
			return db.queryForList(consulta, tot, estado, search, length, start);
		} else
			return null;
	}

	public List<Map<String, Object>> listar() {
		try {
			return db.queryForList("select * from posicion where estado=1;");
		} catch (Exception e) {
			log.info("Error al listar todos "+TABLE+"="+e.toString());
			return null;
		}
	}
	public List<Map<String, Object>> listarPorMueble(Integer cod)throws SQLException{
		try {
			consulta="select * posicion where codmueble=?";
			return db.queryForList(consulta,cod);
		} catch (Exception e) {
			log.info("error listarPorMueble "+TABLE+"="+e.toString());
			return null;
		}
	}

	public Map<String, Object> obtener(Posicion obj) throws SQLException {
		try {
			consulta = "select * from posicion where codposicion=?";
			return db.queryForMap(consulta, obj.getCodposicion());
		} catch (Exception e) {
			log.info("error obtener "+TABLE+"=" + e.toString());
			return null;
		}
	}

	public boolean adicionar(Posicion obj) {
		try {
			consulta = "insert into posicion(codposicion,nombre,codmueble,estado) values((select coalesce(max(codposicion),0)+1 from posicion),?,?,1);";
			db.update(consulta, obj.getNombre(),obj.getCodmueble());
			return true;
		} catch (Exception e) {
			log.info("error adicionar "+TABLE+"=" + e.toString());
			return false;
		}
	}

	public void modificar(Posicion obj) {
		try {
			consulta = "update posicion set nombre=?,codmueble=? where codposicion=?;";
			db.update(consulta, obj.getNombre(),obj.getCodmueble(), obj.getCodposicion());
		} catch (Exception e) {
			log.info("error al modificar "+TABLE+"=" + e.toString());
		}
	}


	public boolean eliminar(Posicion obj) throws SQLException{
		try {
			db.update("update posicion set estado=false where codposicion=?", obj.getCodposicion());
			return true;
		} catch (Exception e) {
			log.info("error al eliminar "+TABLE+"=" + e.toString());
			return false;
		}
	}
	public void eliminarTodoPorMueble(Integer codmueble)throws SQLException{
		db.update("delete from posicion where codmueble=?",codmueble);
	}
	public List<Map<String, Object>> listarPosicionesPorMueble(Integer codmueble) {
		try {
			return db.queryForList("select * from posicion where codmueble=?;",codmueble);
		} catch (Exception e) {
			log.info("Error al eliminarTodoPorMueble "+TABLE+"="+e.toString());
			return null;
		}
	}
}
