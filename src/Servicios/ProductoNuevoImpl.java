package Servicios;

import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import Modelos.ProductoNuevo;
import Utiles.UtilString;
@Service
public class ProductoNuevoImpl implements ProductoNuevoS {
	String sql;
	
	JdbcTemplate db;
	@Autowired
	public void setJdbcTemplate(DataSource dataSource) {
		this.db = new JdbcTemplate(dataSource);
	}
	
	private static final Logger LOGGER = Logger.getLogger("ProductoNuevoImpl");
	
	public List<Map<String, Object>> listar(int start,boolean estado,String search,int length){
		sql="select count(*) from producto_nuevo where estado=? and upper(nombre) like concat('%',upper(?),'%')";
		Integer tot=db.queryForObject(sql, Integer.class,estado,search);
		return db.queryForList("select producto_nuevo.*,concat(usuario.nombre,' ',usuario.ap) as xusuario,row_number() OVER(ORDER BY producto_nuevo.cod_pro_nuevo) as RN,? as Tot from producto_nuevo join usuario on usuario.codusu=producto_nuevo.codusu where producto_nuevo.estado=? and upper(producto_nuevo.nombre) like concat('%',upper(?),'%') LIMIT ? OFFSET ?",tot,estado,search,length,start);
	}
	
	public ProductoNuevo obtener(Long cod){
		try {
			String sql="select * from producto_nuevo where cod_pro_nuevo=?";
			return db.queryForObject(sql, new BeanPropertyRowMapper<ProductoNuevo>(ProductoNuevo.class),cod);
		} catch (Exception e) {
			LOGGER.info("obtener:"+e.toString());
			return null;
		}
	}
	public Long generarCodigo(){
		String sql="select COALESCE(max(cod_pro_nuevo),0)+1 from producto_nuevo";
		return db.queryForObject(sql, Long.class);
	}
	public boolean adicionar(ProductoNuevo obj){
		try {
			String sql;
			Long codProNuevo=generarCodigo();
			sql="insert into producto_nuevo(cod_pro_nuevo,nombre,descripcion,codusu,estado) values(?,?,?,?,true);";
			db.update(sql,codProNuevo,UtilString.firstUpperLetter(obj.getNombre()),obj.getDescripcion(),obj.getCodusu());
			return true;
		} catch (Exception e) {
			LOGGER.info("adicionar:"+e.toString());
			return false;
		}
	}
	public boolean modificar(ProductoNuevo obj){
		try {
			String sql;
			sql="update producto_nuevo set nombre=?, descripcion=?,codusu=? where cod_pro_nuevo=?;";
			db.update(sql,UtilString.firstUpperLetter(obj.getNombre()),obj.getDescripcion(),obj.getCodusu(),obj.getCod_pro_nuevo());
			return true;
		} catch (Exception e) {
			LOGGER.info("modificar:"+e.toString());
			return false;
		}
	}
	public boolean darEstado(Long cod,boolean estado){
		try {
			db.update("update producto_nuevo set estado=? where cod_pro_nuevo=?",estado,cod);
			return true;
		} catch (Exception e) {
			LOGGER.info("eliminar:"+e.toString());
			return false;
		}
	}
}