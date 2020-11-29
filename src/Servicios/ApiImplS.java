package Servicios;

import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import Modelos.Producto;
import Modelos.UserWebService;
import Modelos.Venta;

@Service("apiS")
public class ApiImplS implements ApiS{
	
	private static final Logger LOGGER = Logger.getLogger("ApiImplS");
	JdbcTemplate db;
	@Autowired
	public void setJdbcTemplate(DataSource dataSource) {
		this.db = new JdbcTemplate(dataSource);
	}
	@Override
	public List<Producto> lista_productos(String cat) {
		try {
			return db.query("select * from producto where estado=1 and (categoria=? or 'todos'=?)", new BeanPropertyRowMapper<Producto>(Producto.class),cat,cat);
		} catch (Exception e) {
			LOGGER.info("lista_productos:"+e.toString());
			return null;
		}
	}
	public List<Venta> lista_ventas(Long codusu) {
		try {
			return db.query("select codv,to_char(fecha, 'DD/MM/YYYY') fecha,celular,direccion from venta v where v.codcli=? and estado=1", new BeanPropertyRowMapper<Venta>(Venta.class),codusu);
		} catch (Exception e) {
			LOGGER.info("lista_ventas:"+e.toString());
			return null;
		}
	}
	public List<UserWebService> lista_usuarios() {
		try {
			return db.query("select coalesce(ci,''),nombre,ap,coalesce(am,''),login from usuarios u JOIN accesousuario ac on ac.codusu=u.codusu where u.estado=1", new BeanPropertyRowMapper<UserWebService>(UserWebService.class));
		} catch (Exception e) {
			LOGGER.info("lista_usuarios"+e.toString());
			return null;
		}
	}
	
	public Map<String, Object> iniciar_sesion(String log,String cla){
		try {
			String sql="select count(*) from usuarios u \r\n" + 
					"join accesousuario au on au.codusu=u.codusu and au.login=? and au.passwd=?\r\n" + 
					"where u.estado=1";
			int res=db.queryForObject(sql, Integer.class,log,cla);
			if(res==0)
				return null;
			sql="select au.login,au.passwd,u.codusu,u.nombre,u.ap,u.tipoper,u.foto from usuarios u \r\n" + 
					"join accesousuario au on au.codusu=u.codusu and au.login=? and au.passwd=?\r\n" + 
					"where u.estado=1";
			return db.queryForMap(sql,log,cla);
		} catch (Exception e) {
			LOGGER.info("iniciar_sesion:"+e.toString());
			return null;
		}
	}
	public String adicionar_producto(Producto obj) {
		try {
			String sql="insert into producto(idproducto,nombre,codtip,preciounitario,preciopublico,unidadmedida,porcentaje,estado,cantidad,foto)"
					+ " values((select coalesce(max(idproducto),0)+1 from producto),?,?,?,?,?,?,?,1,?,?)";
			db.update(sql,obj.getNombre(),obj.getCodtip(),0,obj.getFoto());
			return "registrado";
		} catch (Exception e) {
			LOGGER.error("adicionar_producto:"+e.toString());
			return "No";
		}
	}
}