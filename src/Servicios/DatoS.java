package Servicios;

import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class DatoS {
	
	JdbcTemplate db;
	@Autowired
	public void setJdbcTemplate(DataSource dataSource) {
		this.db = new JdbcTemplate(dataSource);
	}
	
	public String iniciar(String login,String clave){
		try {
			Map<String, Object> v=db.queryForMap("select * from datos where login=? and clave=?",login,clave);
			return v.get("resultado").toString();
		} catch (Exception e) {
			System.out.println(e.toString());
			return null;
		}
		
	}
}
