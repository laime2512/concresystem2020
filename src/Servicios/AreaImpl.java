package Servicios;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import Modelos.Area;

@Service
@Transactional
public class AreaImpl implements AreaS{
	private String consulta;
	private static final Logger LOGGER = Logger.getLogger("AreaImpl");
	
	JdbcTemplate db;
	@Autowired
	public void setJdbcTemplate(DataSource dataSource) {
		this.db = new JdbcTemplate(dataSource);
	}

	public List<Map<String, Object>> listar(int start, boolean estado, String search, int length) throws SQLException {
		if (estado) {
			int tot = db.queryForObject("select count(*) from area where estado=?", Integer.class, estado);
			if (search == null)
				search = "";
			consulta = "select area.*,row_number() OVER(ORDER BY area.codare) as RN,? as Tot from area where area.estado=? and upper(nombre) like concat('%',upper(?),'%') LIMIT ? OFFSET ?";
			return db.queryForList(consulta, tot, estado, search, length, start);
		} else
			return null;
	}
	public List<Map<String, Object>> listar() {
		try {
			return db.queryForList("select * from area where estado=true;");
		} catch (Exception e) {
			LOGGER.info("listar"+e.toString());
			return null;
		}
	}
	public Map<String, Object> obtener(Area obj) throws SQLException {
		try {
			consulta = "select * from area where codare=?";
			return db.queryForMap(consulta, obj.getCodare());
		} catch (Exception e) {
			LOGGER.info("obtener:" + e.toString());
			return null;
		}
	}
	public boolean adicionar(Area obj) {
		try {
			consulta = "insert into area(codare,nombre,estado) values((select coalesce(max(codare),0)+1 from area),?,true);";
			db.update(consulta, obj.getNombre());
			return true;
		} catch (Exception e) {
			LOGGER.info("adicionar:" + e.toString());
			return false;
		}
	}
	public void modificar(Area obj) {
		try {
			consulta = "update area set nombre=? where codare=?;";
			db.update(consulta, obj.getNombre(), obj.getCodare());
		} catch (Exception e) {
			LOGGER.info("modificar:" + e.toString());
		}
	}
	public boolean eliminar(Area obj) throws SQLException{
		try {
			db.update("update area set estado=false where codare=?", obj.getCodare());
			return true;
		} catch (Exception e) {
			LOGGER.info("eliminar:" + e.toString());
			return false;
		}
	}
}
