package Servicios;

import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import Modelos.Categoria;
import Utiles.UtilString;
@Service
public class CategoriaImpl implements CategoriaS {
	private static final Logger LOGGER = Logger.getLogger("CategoriaImpl");

	JdbcTemplate db;
	@Autowired
	public void setJdbcTemplate(DataSource dataSource) {
		this.db = new JdbcTemplate(dataSource);
	}
	
	String sql;
	public List<Categoria> listar_todos(){
		try {
			return db.query("select * from categoria where estado=1", new BeanPropertyRowMapper<Categoria>(Categoria.class));
		} catch (Exception e) {
			LOGGER.info("listar_todos:"+e.toString());
			return null;
		}
	}
	
	public List<Map<String, Object>> listar(int start,int estado,String search,int length){
		sql="select count(*) from categoria where estado=? and upper(nomcat) like concat('%',upper(?),'%')";
		Integer tot=db.queryForObject(sql, Integer.class,estado,search);
		return db.queryForList("select categoria.*,row_number() OVER(ORDER BY categoria.codcat) as RN,? as Tot from categoria where estado=? and upper(nomcat) like concat('%',upper(?),'%') LIMIT ? OFFSET ?",tot,estado,search,length,start);
	}
	
	public Categoria obtener(int cod){
		try {
			String sql="select * from categoria where codcat=?";
			Categoria dato=db.queryForObject(sql, new BeanPropertyRowMapper<Categoria>(Categoria.class),cod);
			return dato;
		} catch (Exception e) {
			LOGGER.info("obtener"+e.toString());
			return null;
		}
	}
	public int generarCodigo(){
		String sql="select COALESCE(max(codcat),0)+1 as codcat from categoria";
		return db.queryForObject(sql, Integer.class);
	}
	public boolean adicionar(Categoria m){
		try {
			String sql;
			int codcat=generarCodigo();
			sql="insert into categoria(codcat,nomcat,estado) values(?,?,1);";
			db.update(sql,codcat,UtilString.firstUpperLetter(m.getNomcat()));
			return true;
		} catch (Exception e) {
			LOGGER.error("adicionar:"+e.toString());
			return false;
		}
	}
	public boolean modificar(Categoria obj){
		try {
			String sql;
			sql="update categoria set nomcat=? where codcat=?;";
			db.update(sql,UtilString.firstUpperLetter(obj.getNomcat()),obj.getCodcat());
			return true;
		} catch (Exception e) {
			LOGGER.error("modificar:"+e.toString());
			return false;
		}
	}
	
	public boolean darEstado(int cod,int estado){
		try {
			db.update("update categoria set estado=? where codcat=?",estado,cod);
			return true;
		} catch (Exception e) {
			LOGGER.error("darEstado:"+e.toString());
			return false;
		}
	}

}