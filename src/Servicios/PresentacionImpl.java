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

import Modelos.Presentacion;

@Service
@Transactional
public class PresentacionImpl implements PresentacionS{
	private String consulta;
	private Logger LOGGER = Logger.getLogger("PresentacionImpl");
	
	JdbcTemplate db;
	@Autowired
	public void setJdbcTemplate(DataSource dataSource) {
		this.db = new JdbcTemplate(dataSource);
	}
	public List<Map<String, Object>> listar(int start, boolean estado, String search, int length) throws SQLException {
		if (estado) {
			int tot = db.queryForObject("select count(*) from presentacion where estado=?", Integer.class, estado);
			if (search == null)
				search = "";
			consulta = "select presentacion.*,row_number() OVER(ORDER BY presentacion.codpre) as RN,? as Tot from presentacion where presentacion.estado=? and upper(nombre) like concat('%',upper(?),'%') LIMIT ? OFFSET ?";
			return db.queryForList(consulta, tot, estado, search, length, start);
		} else
			return null;
	}

	public List<Map<String, Object>> listar() {
		try {
			return db.queryForList("select * from presentacion where estado=true;");
		} catch (Exception e) {
			LOGGER.info("listar:"+e.toString());
			return null;
		}
	}

	public Map<String, Object> obtener(Presentacion obj) throws SQLException {
		try {
			consulta = "select * from presentacion where codpre=?";
			return db.queryForMap(consulta, obj.getCodpre());
		} catch (Exception e) {
			LOGGER.info("obtener:"+e.toString());
			return null;
		}
	}

	public boolean adicionar(Presentacion obj) {
		try {
			consulta = "insert into presentacion(codpre,nombre,estado) values((select coalesce(max(codpre),0)+1 from presentacion),?,true);";
			db.update(consulta, obj.getNombre());
			return true;
		} catch (Exception e) {
			LOGGER.error("adicionar:"+e.toString());
			return false;
		}
	}

	public void modificar(Presentacion obj) {
		try {
			consulta = "update presentacion set nombre=? where codpre=?;";
			db.update(consulta, obj.getNombre(), obj.getCodpre());
		} catch (Exception e) {
			LOGGER.error("modificar:"+e.toString());
		}
	}


	public boolean eliminar(Presentacion obj) throws SQLException{
		try {
			db.update("update presentacion set estado=false where codpre=?", obj.getCodpre());
			return true;
		} catch (Exception e) {
			LOGGER.error("eliminar:"+e.toString());
			return false;
		}
	}
}
