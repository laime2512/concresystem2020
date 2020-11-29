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

import Modelos.TipoMueble;

@Service
@Transactional
public class TipoMuebleImpl implements TipoMuebleS{
	private String consulta;
	private static final Logger LOGGER = Logger.getLogger("TipoMuebleImpl");
	
	JdbcTemplate db;
	@Autowired
	public void setJdbcTemplate(DataSource dataSource) {
		this.db = new JdbcTemplate(dataSource);
	}
	
	public List<Map<String, Object>> listar(int start, boolean estado, String search, int length) throws SQLException {
		if (estado) {
			int tot = db.queryForObject("select count(*) from tipo_mueble where estado=?", Integer.class, estado);
			if (search == null)
				search = "";
			consulta = "select tipo_mueble.*,row_number() OVER(ORDER BY tipo_mueble.codtimu) as RN,? as Tot from tipo_mueble where tipo_mueble.estado=? and upper(nombre) like concat('%',upper(?),'%') LIMIT ? OFFSET ?";
			return db.queryForList(consulta, tot, estado, search, length, start);
		} else
			return null;
	}
	public List<Map<String, Object>> listar() {
		try {
			return db.queryForList("select * from tipo_mueble where estado=true;");
		} catch (Exception e) {
			LOGGER.info("listar:"+e.toString());
			return null;
		}
	}
	public Map<String, Object> obtener(TipoMueble obj) throws SQLException {
		try {
			consulta = "select * from tipo_mueble where codtimu=?";
			return db.queryForMap(consulta, obj.getCodtimu());
		} catch (Exception e) {
			LOGGER.info("obtener:"+e.toString());
			return null;
		}
	}
	public boolean adicionar(TipoMueble obj) {
		try {
			consulta = "insert into tipo_mueble(codtimu,nombre,estado) values((select coalesce(max(codtimu),0)+1 from tipo_mueble),?,true);";
			db.update(consulta, obj.getNombre());
			return true;
		} catch (Exception e) {
			LOGGER.error("adicionar:"+e.toString());
			return false;
		}
	}
	public void modificar(TipoMueble obj) {
		try {
			consulta = "update tipo_mueble set nombre=? where codtimu=?;";
			db.update(consulta, obj.getNombre(), obj.getCodtimu());
		} catch (Exception e) {
			LOGGER.info("modificar:"+e.toString());
		}
	}
	public boolean eliminar(TipoMueble obj) throws SQLException{
		try {
			db.update("update tipo_mueble set estado=false where codtimu=?", obj.getCodtimu());
			return true;
		} catch (Exception e) {
			LOGGER.info("eliminar:"+e.toString());
			return false;
		}
	}
}
