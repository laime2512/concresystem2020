package Servicios;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import Modelos.Rol;
import Utiles.UtilDataTableS;
import pagination.DataTableResults;
import pagination.SqlBuilder;

@Service
public class RolImpl implements RolS{
	private static final Logger LOGGER = Logger.getLogger("RolImpl");
	
	JdbcTemplate db;
	@Autowired
	public void setJdbcTemplate(DataSource dataSource) {
		this.db = new JdbcTemplate(dataSource);
	}
	
	@Autowired
	private UtilDataTableS utilDataTableS;
	public DataTableResults<Rol> listar(HttpServletRequest request){
		try {
			SqlBuilder sql = new SqlBuilder("rol");
			sql.setSelect("rol.*,(select count(*) from rolme where rolme.codr=rol.codr) menus");
			sql.setWhere("estado=1");
			return utilDataTableS.list(request, Rol.class, sql);
		} catch (Exception e) {
			LOGGER.error("listar"+e.getMessage());
			return null;
		}
	}
	public List<Rol> listarTodos(){
		try {
			return db.query("select * from rol where estado=1",new BeanPropertyRowMapper<Rol>(Rol.class));
		} catch (Exception e) {
			return null;
		}
	}
	
	public Rol obtener(Integer codr){
		try {
			String sql="select * from rol where codr=?";
			Rol dato=db.queryForObject(sql,new BeanPropertyRowMapper<Rol>(Rol.class),codr);
			return dato;
		} catch (Exception e) {
			LOGGER.info("obtener:" + e.toString());
			return null;
		}
	}
	public int generarCodigo(){
		String sql="select COALESCE(max(codr),0)+1 as codr from rol";
		return db.queryForObject(sql, Integer.class);
	}
	public boolean adicionar(Rol r){
		try {
			String sql;
			int codr=generarCodigo();
			sql="insert into rol(codr,nombre,acceso_venta) values(?,upper(?),?);";
			db.update(sql,codr,r.getNombre(),r.isAcceso_venta());
			return true;
		} catch (Exception e) {
			LOGGER.error("adicionar:" + e.toString());
			return false;
		}
	}
	public boolean modificar(Rol r){
		try {
			String sql="update rol set nombre=upper(?), acceso_venta=? where codr=?;";
			db.update(sql,r.getNombre(),r.isAcceso_venta(),r.getCodr());
			return true;
		} catch (Exception e) {
			LOGGER.error("modificar:" + e.toString());
			return false;
		}
	}
	
	public boolean darEstado(int codr,int estado){
		try {
			db.update("update rol set estado=? where codr=?",estado,codr);
			return true;
		} catch (Exception e) {
			LOGGER.info("darEstado:" + e.toString());
			return false;
		}
	}
	public boolean asignar(int codr,Integer menus[]){
		try {
			db.update("delete from rolme where codr=?",codr);
			if(menus!=null)
				for (int i = 0; i < menus.length; i++)
					db.update("insert into rolme(codr,codm) values(?,?)",codr,menus[i]);
			return true;
		} catch (Exception e) {
			LOGGER.info("asignar:" + e.toString());
			return false;
		}
	}
	public List<Rol> listarRolesXlogin(String login){
		try {
			return db.query("select rol.* from rol join rolusu on rolusu.codr=rol.codr and login=?",new BeanPropertyRowMapper<Rol>(Rol.class),login);
		} catch (Exception e) {
			LOGGER.info("listarRolesXlogin:" + e.toString());
			return null;
		}
	}
}