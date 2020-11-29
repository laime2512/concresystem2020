package Servicios;

import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import Modelos.Proceso;

@Service
public class ProcesoS {
	String sql;
	
	JdbcTemplate db;
	@Autowired
	public void setJdbcTemplate(DataSource dataSource) {
		this.db = new JdbcTemplate(dataSource);
	}
	
	public List<Proceso> obtenerXmenu(int menu){
		sql="select DISTINCT p.* from proceso p join mepro mp on mp.codp=p.codp and mp.codm=?";
		return db.query(sql, new BeanPropertyRowMapper<Proceso>(Proceso.class) ,menu);
	}
	public List<Map<String, Object>> lista(){
		return db.queryForList("select * from proceso");
	}
	public List<Map<String, Object>> listaProcesosXmenu(int codm){
		return db.queryForList("select *,case when (select count(*) from mepro where codm=? and mepro.codp=proceso.codp)>0 THEN 'selected' else '' end as sel  from proceso",codm);
	}
}
