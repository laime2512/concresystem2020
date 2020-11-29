package Servicios;

import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import Modelos.Cliente;
import Modelos.Usuario;
import Utiles.Constantes;


@Service
public class ClienteImpl implements ClienteS{
	private static final Logger LOGGER = Logger.getLogger("ClienteImpl");
	
	JdbcTemplate db;
	@Autowired
	public void setJdbcTemplate(DataSource dataSource) {
		this.db = new JdbcTemplate(dataSource);
	}
	
	public List<Map<String, Object>> listar(int start,int estado,String search,int length){
		String sql="";
		if(estado==2){
			int tot=db.queryForObject("select count(*) from cliente where estado=?", Integer.class,estado);
			if(search==null)search="";
			sql="select cliente.*,row_number() OVER(ORDER BY cliente.codusu) as RN,? as Tot,concat(nombre,' ',ap) xcliente from cliente join usuario on usuario.codusu=cliente.codcli where cliente.estado=? upper(nombre) like concat('%',upper(?),'%') LIMIT ? OFFSET ?";
			return db.queryForList(sql,tot,estado,search,length,start);
		}else{
			int tot=db.queryForObject("select count(*) from cliente where estado=?", Integer.class,estado);
			if(search==null)search="";
			sql="select cliente.*,usuario.nombre,row_number() OVER(ORDER BY cliente.codcli) as RN,? as Tot,concat(nombre,' ',ap) as xcliente\r\n" + 
					"from cliente join usuario on usuario.codusu=cliente.codcli\r\n" + 
					"where cliente.estado=? and upper(usuario.nombre) like concat('%',upper(?),'%') LIMIT ? OFFSET ?";
			return db.queryForList(sql,tot,estado,search,length,start);
		}
	}
	public List<Map<String, Object>> buscarproducto(String searchp,int searchc){
		try {
			return db.queryForList("select producto.*,categoria.nomcat,tipo.nomtip from producto "
					+ "join tipo on tipo.codtip=producto.codtip "
					+ "join lugar on lugar.codpro=producto.codpro "
					+ "join almacen on almacen.codlugar=lugar.codlugar "
					+ "join categoria on categoria.codcat=tipo.codcat "
					+ "where almacen.cantidad >= 1 and producto.estado=1 and upper(nombre) like concat('%',upper(?),'%') and (categoria.codcat=? or ?=0) group by producto.codpro,categoria.nomcat,tipo.nomtip",searchp,searchc,searchc);
		} catch (Exception e) {
			LOGGER.info("buscarproducto:"+e.toString());
			return null;
		}
	}
	public List<Map<String, Object>> listarcategoria(){
		try {
			return db.queryForList("select * from categoria where estado=1 ");
		} catch (Exception e) {
			LOGGER.info("listarcategoria"+e.toString());
			return null;
		}
		
	}
	public List<Map<String, Object>> listar(){
		try {
			
			return db.queryForList("select * from cliente join usuario on usuario.codusu=cliente.codcli where cliente.estado=1 ");
		} catch (Exception e) {
			LOGGER.info("listar:"+e.toString());
			return null;
		}
		
	}
	public Map<String, Object> obtener(Long codusu){
		try {
			String sql="select cliente.*,accesousuario.*,usuario.* from cliente " + 
					"join usuario on usuario.codusu=cliente.codcli " + 
					"left join accesousuario on accesousuario.codusu=usuario.codusu " + 
					"where cliente.codcli=?";
			return db.queryForMap(sql, codusu);
		} catch (Exception e) {
			LOGGER.info("obtener:"+e.toString());
			return null;
		}
	}
	public Cliente obtenerCliente(Long codusu) {
		try {
			return db.queryForObject("select * from cliente where codcli = ?", new BeanPropertyRowMapper<Cliente>(Cliente.class),codusu);
		} catch (Exception e) {
			LOGGER.info("obtenerCliente:"+e.toString());
			return null;
		}
	}
	public String obtenerFactura(String nit) {
		try {
			return db.queryForObject("select cliente_nit from factura where nitfac=? and estado='activo' order by numfac desc limit 1", String.class,nit);
		} catch (DataAccessException e) {
			LOGGER.info("obtenerFactura:"+e.toString());
			return "";
		}
	}
	public boolean adicionar(Cliente c,Usuario p,String log,String pass){
		try {
			String sql;
			Long codusu=generarCodigo();
			sql="insert into usuario(codusu,nombre,ap,am,genero,tipoper,foto) values(?,upper(?),upper(?),upper(?),?,'P',?);";
			db.update(sql,codusu,p.getNombre(),p.getAp(),p.getAm(),p.getGenero(),p.getFoto());
			sql="insert into cliente(nit,celular,direccion,codcli) values(?,?,?,?);";
			db.update(sql,c.getNit(),c.getCelular(),c.getDireccion(),codusu);
			db.update("insert into accesousuario(login,passwd,codusu,estado) values(?,?,?,1)",log,pass,codusu);
			db.update("insert into rolusu(codr,login) values(?,?)",5,log);
			return true;
		} catch (Exception e) {
			LOGGER.error("adicionar:"+e.toString());
			return false;
		}
	}
	public void modificar(Cliente c,Usuario p,String log,String pass){
		try {
			String sql;
			sql="update usuario set (nombre,ap,am,genero,tipoper,foto)=(upper(?),upper(?),upper(?),?,'P',?) where codusu=?;";
			db.update(sql,p.getNombre(),p.getAp(),p.getAm(),p.getGenero(),p.getFoto(),c.getCodcli());
			sql="update cliente set (nit,celular,direccion)=(?,?,?) where codcli=?;";
			db.update(sql,c.getNit(),c.getCelular(),c.getDireccion(),c.getCodcli());
			int res=db.queryForObject("select count(*) from accesousuario where codusu=?", Integer.class,c.getCodcli());
			if(res>0) {
				db.update("update accesousuario set passwd=? where codusu=?",pass,c.getCodcli());
				
			}else {
				db.update("insert into accesousuario(login,passwd,estado,codusu) values(?,?,?,?)",log,pass,1,c.getCodcli());
				db.update("insert into rolusu(codr,login) values(?,?)",Constantes.Rol.PUBLICO,log);
			}
		} catch (Exception e) {
			LOGGER.error("modificar:"+e.toString());
		}
	}

	public Long generarCodigo(){
		String sql="select COALESCE(max(codusu),0)+1 as codusu from usuario";
		return db.queryForObject(sql, Long.class);
	}
	public Long generarCodigoCliente(){
		String sql="select COALESCE(max(codcli),0)+1 as codcli from cliente";
		return db.queryForObject(sql, Long.class);
	}
	public boolean eliminar(Long codcli){
		try {
			db.update("update cliente set estado=0, where codcli=?",codcli);
			return true;
		} catch (Exception e) {
			LOGGER.error("eliminar:"+e.toString());
			return false;
		}
	}
	
	public boolean darestado(Long codcli, int estado){
		try {
			db.update("update cliente set estado=? where codcli =?",estado, codcli);
			return true;
		} catch (Exception e) {
			LOGGER.error("darEstado:"+e.toString());
			return false;
		}
	
}
}
	

