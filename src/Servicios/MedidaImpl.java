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

import Modelos.Medida;

@Service
@Transactional
public class MedidaImpl implements MedidaS{
	private String consulta;
	private Logger LOGGER = Logger.getLogger("MedidaImpl");

	JdbcTemplate db;
	@Autowired
	public void setJdbcTemplate(DataSource dataSource) {
		this.db = new JdbcTemplate(dataSource);
	}
	
	public List<Map<String, Object>> listar(int start, boolean estado, String search, int length) throws SQLException {
		if (estado) {
			int tot = db.queryForObject("select count(*) from medida where estado=?", Integer.class, estado);
			if (search == null)
				search = "";
			consulta = "select medida.*,row_number() OVER(ORDER BY medida.codmed) as RN,? as Tot from medida where medida.estado=? and upper(nombre) like concat('%',upper(?),'%') LIMIT ? OFFSET ?";
			return db.queryForList(consulta, tot, estado, search, length, start);
		} else
			return null;
	}
	public List<Map<String, Object>> listar() {
		try {
			return db.queryForList("select * from medida where estado=true;");
		} catch (Exception e) {
			LOGGER.info("listar"+e.toString());
			return null;
		}
	}
	public Map<String, Object> obtener(Medida obj) throws SQLException {
		try {
			consulta = "select * from medida where codmed=?";
			return db.queryForMap(consulta, obj.getCodmed());
		} catch (Exception e) {
			LOGGER.info("obtener:" + e.toString());
			return null;
		}
	}
	public boolean adicionar(Medida obj) {
		try {
			consulta = "insert into medida(codmed,nombre,estado) values((select coalesce(max(codmed),0)+1 from medida),?,true);";
			db.update(consulta, obj.getNombre());
			return true;
		} catch (Exception e) {
			LOGGER.info("adicionar:" + e.toString());
			return false;
		}
	}
	public void modificar(Medida obj) {
		try {
			consulta = "update medida set nombre=? where codmed=?;";
			db.update(consulta, obj.getNombre(), obj.getCodmed());
		} catch (Exception e) {
			LOGGER.info("modificar:" + e.toString());
		}
	}
	public boolean eliminar(Medida obj) throws SQLException{
		try {
			db.update("update medida set estado=false where codmed=?", obj.getCodmed());
			return true;
		} catch (Exception e) {
			LOGGER.info("eliminar:" + e.toString());
			return false;
		}
	}
}
