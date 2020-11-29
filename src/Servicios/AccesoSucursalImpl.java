package Servicios;

import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import Modelos.Sucursal;
import Modelos.Usuario;

@Service
@Transactional
public class AccesoSucursalImpl implements AccesoSucursalS{
	private static final Logger LOGGER=Logger.getLogger("AccesoSucursalImpl");
	JdbcTemplate db;
	@Autowired
	public void setJdbcTemplate(DataSource dataSource) {
		this.db = new JdbcTemplate(dataSource);
	}
	public void adicionar(Usuario usuario,Sucursal sucursal)throws SQLException{
		try {
			db.update("insert into acceso_sucursal(codusu,codsuc,fecalta) values(?,?,now());",usuario.getCodusu(),sucursal.getCodsuc());	
		} catch (Exception e) {
			LOGGER.error("adicionar:"+e.toString());
		}
	}
	public void eliminar(Usuario usuario,Sucursal sucursal)throws SQLException{
		try {
			db.update("update acceso_sucursal set fecbaja=now() where codusu=? and codsuc=? and fecbaja is null;",usuario.getCodusu(),sucursal.getCodsuc());	
		} catch (Exception e) {
			LOGGER.error("Eliminar:"+e.toString());
		}
	}
	public void eliminarTodosPorUsuario(Usuario usuario)throws SQLException{
		try {
			db.update("update acceso_sucursal set fecbaja=now() where codusu=? and fecbaja is null;",usuario.getCodusu());	
		} catch (Exception e) {
			LOGGER.info("eliminarTodosPorUsuario:"+e.toString());
		}
	}
	public void eliminarTodosPorSucursal(Sucursal sucursal)throws SQLException{
		try {
			db.update("update acceso_sucursal set fecbaja=now() where codsuc=? and fecbaja is null;",sucursal.getCodsuc());	
		} catch (Exception e) {
			LOGGER.info("eliminarTodosPorSucursal:"+e.toString());
		}
	}
	public Boolean existe(Usuario usuario,Sucursal sucursal)throws SQLException{
		Integer res=db.queryForObject("select count(*) from acceso_sucursal where codusu=? and codsuc=? and fecbaja is null", Integer.class,usuario.getCodusu(),sucursal.getCodsuc());
		return res>0;
	}
	public List<Sucursal> obtenerSucursalesPorUsuario(Usuario usuario)throws SQLException{
		try {
			String sql = "select s.* from sucursal s join acceso_sucursal ac_su on ac_su.codsuc=s.codsuc and ac_su.codusu=? and ac_su.fecbaja is null where s.estado=true";
			return db.query(sql, new BeanPropertyRowMapper<Sucursal>(Sucursal.class),usuario.getCodusu());
		} catch (Exception e) {
			LOGGER.info("obtenerSucursalesPorUsuario:"+e.toString());
			return null;
		}
	}
	public void eliminarOtros(Usuario usuario,String vector)throws SQLException{
		db.update("update acceso_sucursal set fecbaja=now() where codusu=? and codsuc not in "+vector,usuario.getCodusu());
	} 
}
