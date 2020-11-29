package Servicios;

import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import Modelos.Tipo;
import Utiles.UtilString;
@Service
public class TipoS {
	
	JdbcTemplate db;
	@Autowired
	public void setJdbcTemplate(DataSource dataSource) {
		this.db = new JdbcTemplate(dataSource);
	}
	
	@Autowired
	ProcesoS procesoS;

	String sql;

	public List<Tipo> obtenerxcategoria(int codcat){
		try {
			return db.query("select * from tipo where estado=1 and codcat=?", new BeanPropertyRowMapper<Tipo>(Tipo.class),codcat);
		} catch (Exception e) {
			return null;
		}
	}
	public List<Tipo> listar_todos(){
		sql="select * from tipo where estado=1";
		try {
			return db.query(sql, new BeanPropertyRowMapper<Tipo>(Tipo.class));
		} catch (Exception e) {
			return null;
		}
	}
	public List<Map<String, Object>> listar(int start,int estado,String search,int length){
		sql="select count(*) from tipo where estado=? and upper(nomtip) like concat('%',upper(?),'%')";
		Integer tot=db.queryForObject(sql, Integer.class,estado,search);
		return db.queryForList("select tipo.*,row_number() OVER(ORDER BY tipo.codtip) as RN,? as Tot from tipo where estado=? and upper(nomtip) like concat('%',upper(?),'%') LIMIT ? OFFSET ?",tot,estado,search,length,start);
	}
	
	public Tipo obtener(int cod){
		try {
			String sql="select * from tipo where codtip=?";
			Tipo dato=db.queryForObject(sql, new BeanPropertyRowMapper<Tipo>(Tipo.class),cod);
			return dato;
		} catch (Exception e) {
			System.out.println("error al obtener codm"+e.toString());
			return null;
		}
	}
	public int generarCodigo(){
		String sql="select COALESCE(max(codtip),0)+1 as codtip from tipo";
		return db.queryForObject(sql, Integer.class);
	}
	public boolean adicionar(Tipo obj){
		try {
			String sql;
			int codtip=generarCodigo();
			sql="insert into tipo(codtip,nomtip,codcat,estado) values(?,?,?,1);";
			db.update(sql,codtip,UtilString.firstUpperLetter(obj.getNomtip()),obj.getCodcat());
			return true;
		} catch (Exception e) {
			System.out.println("error tipo="+e.toString());
			return false;
		}
	}
	public boolean modificar(Tipo obj){
		try {
			String sql;
			sql="update tipo set nomtip=?,codcat=? where codtip=?;";
			db.update(sql,UtilString.firstUpperLetter(obj.getNomtip()),obj.getCodcat(),obj.getCodtip());
			return true;
		} catch (Exception e) {
			System.out.println("error al modificar categoria="+e.toString());
			return false;
		}
	}
	
	public boolean darEstado(int cod,int estado){
		try {
			db.update("update tipo set estado=? where codtip=?",estado,cod);
			return true;
		} catch (Exception e) {
			System.out.println("error al dar estado al tipo="+e.toString());
			return false;
		}
	}

}