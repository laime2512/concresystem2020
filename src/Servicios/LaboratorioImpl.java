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

import Modelos.Laboratorio;

@Service
@Transactional
public class LaboratorioImpl implements LaboratorioS {
	private String consulta;
	private Logger LOGGER = Logger.getLogger("LaboratorioImpl");
	
	JdbcTemplate db;
	@Autowired
	public void setJdbcTemplate(DataSource dataSource) {
		this.db = new JdbcTemplate(dataSource);
	}
	
	public List<Map<String, Object>> listar(int start, boolean estado, String search, int length) throws SQLException {
		if (estado) {
			int tot = db.queryForObject("select count(*) from laboratorio where estado=?", Integer.class, estado);
			if (search == null)
				search = "";
			consulta = "select laboratorio.*,row_number() OVER(ORDER BY laboratorio.codlab) as RN,? as Tot from laboratorio where laboratorio.estado=? and upper(nombre) like concat('%',upper(?),'%') LIMIT ? OFFSET ?";
			return db.queryForList(consulta, tot, estado, search, length, start);
		} else {
			LOGGER.info("No tiene estado o es falso");
			return null;
		}
	}
	public List<Map<String, Object>> listar() {
		try {
			return db.queryForList("select * from laboratorio where estado=true;");
		} catch (Exception e) {
			LOGGER.info("listar:"+e.toString());
			return null;
		}
	}
	public Map<String, Object> obtener(Laboratorio obj) throws SQLException {
		try {
			consulta = "select * from laboratorio where codlab=?";
			return db.queryForMap(consulta, obj.getCodlab());
		} catch (Exception e) {
			LOGGER.info("obtener:" + e.toString());
			return null;
		}
	}
	public boolean adicionar(Laboratorio obj) {
		try {
			consulta = "insert into laboratorio(codlab,nombre,estado) values((select coalesce(max(codlab),0)+1 from laboratorio),?,true);";
			db.update(consulta, obj.getNombre());
			return true;
		} catch (Exception e) {
			LOGGER.info("adicionar:" + e.toString());
			return false;
		}
	}
	public void modificar(Laboratorio obj) {
		try {
			consulta = "update laboratorio set nombre=? where codlab=?;";
			db.update(consulta, obj.getNombre(), obj.getCodlab());
		} catch (Exception e) {
			LOGGER.info("modificar:" + e.toString());
		}
	}
	public boolean eliminar(Laboratorio obj) throws SQLException{
		try {
			db.update("update laboratorio set estado=false where codlab=?", obj.getCodlab());
			return true;
		} catch (Exception e) {
			LOGGER.info("eliminar:" + e.toString());
			return false;
		}
	}
}
