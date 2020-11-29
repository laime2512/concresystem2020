package Servicios;

import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import Modelos.Menu;
import Modelos.Proceso;
import Utiles.UtilString;
@Service
public class MenuS {
	
	JdbcTemplate db;
	@Autowired
	public void setJdbcTemplate(DataSource dataSource) {
		this.db = new JdbcTemplate(dataSource);
	}

	String sql;
	public List<Menu> obtenerXusuario(String login){
		sql="select DISTINCT m.* from menu m join rolme rm on rm.codm=m.codm join rol r on rm.codr=r.codr join rolusu ru on ru.codr=r.codr join accesousuario u on u.login=ru.login where m.estado=1 and u.login=? order by codm";
		List<Menu> menus= db.query(sql, new BeanPropertyRowMapper<Menu>(Menu.class),login);
		for (Menu menu : menus) {
			menu.setProcesos(obtenerXmenu(menu.getCodm()));
		}
		return menus;
	}
	public List<Proceso> obtenerXmenu(int menu){
		sql="select DISTINCT p.* from proceso p join mepro mp on mp.codp=p.codp and mp.codm=?";
		return db.query(sql, new BeanPropertyRowMapper<Proceso>(Proceso.class) ,menu);
	}
	public List<Map<String, Object>> lista(){
		return db.queryForList("select * from menu");
	}
	public List<Map<String, Object>> listaMenusXrol(int codr){
		return db.queryForList("select *,case when (select count(*) from rolme where codr=? and rolme.codm=menu.codm)>0 THEN 'selected' else '' end as sel from menu",codr);
	}
	
	public List<Map<String, Object>> listar(int start,int estado,String search,int length){
		if(estado==2){
			int tot=db.queryForObject("select count(*) from menu", Integer.class);
			if(search==null)search="";
			return db.queryForList("select menu.*,(select count(*) from mepro where mepro.codm=menu.codm) proceso,row_number() OVER(ORDER BY menu.codm) as RN,? as Tot from menu where upper(nombre) like concat('%',upper(?),'%') LIMIT ? OFFSET ?",tot,search,length,start);
		}else{
			int tot=db.queryForObject("select count(*) from menu where estado=?", Integer.class,estado);
			if(search==null)search="";
			return db.queryForList("select menu.*,(select count(*) from mepro where mepro.codm=menu.codm) proceso,row_number() OVER(ORDER BY menu.codm) as RN,? as Tot from menu where estado=? and upper(nombre) like concat('%',upper(?),'%') LIMIT ? OFFSET ?",tot,estado,search,length,start);
		}
	}
	
	public Map<String, Object> obtener(int codm){
		try {
			String sql="select * from menu where codm=?";
			Map<String, Object> dato=db.queryForMap(sql,codm);
			return dato;
		} catch (Exception e) {
			System.out.println("error al obtener codm"+e.toString());
			return null;
		}
	}
	public int generarCodigo(){
		String sql="select COALESCE(max(codm),0)+1 as codm from menu";
		return db.queryForObject(sql, Integer.class);
	}
	public boolean adicionar(Menu m){
		try {
			String sql;
			int codm=generarCodigo();
			sql="insert into menu(codm,nombre) values(?,?);";
			db.update(sql,codm,UtilString.firstUpperLetter(m.getNombre()));
			return true;
		} catch (Exception e) {
			System.out.println("error menu="+e.toString());
			return false;
		}
	}
	public boolean modificar(Menu m){
		try {
			String sql;
			sql="update menu set nombre=? where codm=?;";
			db.update(sql,UtilString.firstUpperLetter(m.getNombre()),m.getCodm());
			return true;
		} catch (Exception e) {
			System.out.println("error al modificar menu="+e.toString());
			return false;
		}
	}
	
	public boolean darEstado(int codm,int estado){
		try {
			db.update("update menu set estado=? where codm=?",estado,codm);
			return true;
		} catch (Exception e) {
			System.out.println("error al dar estado al menu="+e.toString());
			return false;
		}
	}
	public boolean asignar(int codm,Integer procesos[]){
		try {
			db.update("delete from mepro where codm=?",codm);
			if(procesos!=null)
				for (int i = 0; i < procesos.length; i++)
					db.update("insert into mepro(codm,codp) values(?,?)",codm,procesos[i]);
			return true;
		} catch (Exception e) {
			System.out.println("error al asignar="+e.toString());
			return false;
		}
	}
}