package Servicios;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import Modelos.Proveedor;
import Utiles.Constantes;
import Utiles.UtilDataTableS;
import pagination.DataTableResults;
import pagination.SqlBuilder;

@Service
public class ProveedorImplS implements ProveedorS  {
	private static final Logger LOGGER = Logger.getLogger("ProveedorImplS");
	@Autowired
	private UtilDataTableS utilDataTableS;
	JdbcTemplate db;
	@Autowired
	public void setJdbcTemplate(DataSource dataSource) {
		this.db = new JdbcTemplate(dataSource);
	}
	
	@Override
	public Map<String, Object> obtener(Long codproveedor){
		try {
			return db.queryForMap("select p.* from proveedor p where codproveedor=?", codproveedor);
		} catch (Exception e) {
			LOGGER.error("error obtenerProvedor="+e.toString());
			return null;
		}
	}
	@Override
	public boolean adicionar(Proveedor p){
		try {
			String sql;
			int codproveedor=generarCodigo();
			sql="insert into usuario(codusu,nombre,ap,am,genero,tipoper,foto) values(?,upper(?),upper(?),upper(?),?,'P',?);";
			db.update(sql,codproveedor,p.getNombre(),"","","M",Constantes.IMG_DEFAULT);
			sql="insert into proveedor(codproveedor,nombre,nit,direccion,telefono) values(?,upper(?),?,?,?);";
			db.update(sql,codproveedor,p.getNombre(),p.getNit(),p.getDireccion(),p.getTelefono());
			return true;
		} catch (Exception e) {
			LOGGER.error("error proveedor="+e.toString());
			return false;
		}
	}
	@Override
	public boolean modificar(Proveedor p){
		try {
			String sql;
			sql="update proveedor set (nombre,nit,direccion,telefono)=(upper(?),?,?,?) where codproveedor=?;";
			db.update(sql,p.getNombre(),p.getNit(),p.getDireccion(),p.getTelefono(),p.getCodproveedor());
			return true;
		} catch (Exception e) {
			LOGGER.error("error proveedor="+e.toString());
			return false;
		}
	}
	@Override
	public int generarCodigo(){
		String sql="select COALESCE(max(codusu),0)+1 from usuario";
		return db.queryForObject(sql, Integer.class);
	}
	@Override
	public DataTableResults<Proveedor> listado(HttpServletRequest request){
		SqlBuilder sqlBuilder = new SqlBuilder("proveedor");
		sqlBuilder.setSelect("*");
		sqlBuilder.setWhere("estado=1");
		try {
			return utilDataTableS.list(request, Proveedor.class, sqlBuilder);
		} catch (Exception e) {
			LOGGER.error("error listado: "+e.toString());
			return null;
		}
	}
	@Override
	public List<Map<String, Object>> listar(int start,int estado,String search,int length){
		int tot=db.queryForObject("select count(*) from proveedor where estado=?", Integer.class,estado);
		if(search==null)search="";
			
			return db.queryForList("select proveedor.*,row_number() OVER(ORDER BY proveedor.codproveedor desc) as RN,? as Tot from proveedor where estado=? and concat(upper(nombre),' ',upper(direccion),' ',upper(telefono),' ') like concat('%',upper(?),'%') LIMIT ? OFFSET ?",tot,estado,search,length,(start*10));
	}
	@Override
	public List<Map<String, Object>> listar(){
		try {
			
				return db.queryForList("select * from proveedor where estado=1 order by nombre asc;");
		} catch (Exception e) {
			LOGGER.error(e.toString());
			return null;
		}
		
	}
	@Override
	public boolean darestado(Long codproveedor, int estado){
		try {
			db.update("update proveedor set estado=? where codproveedor=?",estado, codproveedor);
			return true;
		} catch (Exception e) {
			LOGGER.error("error al eliminar proveedor="+e.toString());
			return false;
		}
	}
	
}
