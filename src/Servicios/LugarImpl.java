package Servicios;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import Modelos.Lugar;
import Utiles.Constantes;
import Utiles.UtilDataTableS;
import pagination.DataTableResults;
import pagination.SqlBuilder;

@Service
@Transactional
public class LugarImpl implements LugarS{
	private String consulta;
	private static final Logger LOGGER = Logger.getLogger("LugarImpl");
	@Autowired
	private UtilDataTableS utilDataTableS;
	@Autowired
	private SessionS sessionS;
	
	JdbcTemplate db;
	@Autowired
	public void setJdbcTemplate(DataSource dataSource) {
		this.db = new JdbcTemplate(dataSource);
	}
	
	public DataTableResults<Lugar> listar(HttpServletRequest request){
		SqlBuilder sqlBuilder = new SqlBuilder("lugar");
		sqlBuilder.setSelect("lugar.*,posicion.nombre xposicion,mueble.nombre xmueble,tipo_mueble.nombre xtipo_mueble,vp.nombre xproducto,");
		sqlBuilder.setSelectConcat("vp.xcategoria,vp.xarea,vp.concentracion,vp.xlaboratorio,vp.xmedida,vp.xtipo,");  
		sqlBuilder.setSelectConcat("concat(vp.presentacion_unidad,'/',vp.presentacion_caja,'/',vp.presentacion_paquete) as xpresentacion");
		sqlBuilder.addJoin("posicion on posicion.codposicion=lugar.codposicion");
		sqlBuilder.addJoin("mueble on mueble.codmueble=posicion.codmueble");
		sqlBuilder.addJoin("tipo_mueble on tipo_mueble.codtimu=mueble.codtimu");
		sqlBuilder.addJoin("view_producto vp on vp.codpro = lugar.codpro");
		sqlBuilder.setWhere("lugar.codsuc=:codsuc");
		try {
			sqlBuilder.addParameter("codsuc", sessionS.getSucursal(request).getCodsuc());
			return utilDataTableS.list(request, Lugar.class, sqlBuilder);
		} catch (Exception e) {
			LOGGER.error("listar"+e.getMessage());
			return null;
		}
	}

	public List<Map<String, Object>> listar(int start, Integer estado, String search, int length,Integer codsuc) throws SQLException {
		try {
			int tot = db.queryForObject("select count(*) from lugar join producto on lugar.codpro=producto.codpro and producto.estado=? where codsuc=?", Integer.class, estado,codsuc);
			if(tot==0)
				return null;
			if (search == null)
				search = "";
			consulta = "select lugar.*,posicion.nombre xposicion,mueble.nombre xmueble,tipo_mueble.nombre xtipo_mueble,producto.nombre xproducto,tipo.nomtip xtipo,"
					+ "categoria.nomcat xcategoria,area.nombre xarea,producto.concentracion,laboratorio.nombre xlaboratorio,medida.nombre xmedida,"
					+ "presentacion.nombre xpresentacion,row_number() OVER(ORDER BY lugar.codlugar desc) as RN,? as Tot "
					+ "from lugar join posicion on posicion.codposicion=lugar.codposicion "
					+ "join mueble on mueble.codmueble=posicion.codmueble "
					+ "join tipo_mueble on tipo_mueble.codtimu=mueble.codtimu "
					+ "join producto on producto.codpro=lugar.codpro and producto.estado=? "
					+ "join tipo on tipo.codtip=producto.codtip "
					+ "join categoria on categoria.codcat=tipo.codcat left "
					+ "join laboratorio on laboratorio.codlab=producto.codlab left "
					+ "join medida on medida.codmed=producto.codmed left "
					+ "join presentacion on presentacion.codpre=producto.codpre "
					+ "left join area on area.codare=producto.codare "
					+ "where lugar.codsuc=? and upper(concat(tipo.nomtip,' ',producto.nombre,' ',area.nombre,' ',categoria.nomcat)) like concat('%',upper(?),'%') LIMIT ? OFFSET ?";
			return db.queryForList(consulta, tot, estado,codsuc, search, length, start);
		} catch (Exception e) {
			LOGGER.info("Listar:"+e.toString());
			return null;
		}
	}
	public Map<String, Object> obtener(Lugar obj) {
		try {
			consulta = "select lugar.*,mueble.codmueble from lugar join posicion on posicion.codposicion=lugar.codposicion join mueble on mueble.codmueble=posicion.codmueble where codlugar=?";
			return db.queryForMap(consulta, obj.getCodlugar());
		} catch (Exception e) {
			LOGGER.info("obtener" + e.toString());
			return null;
		}
	}
	public Lugar obtenerPorSucursalProducto(Integer codsuc,Long codpro){
		consulta="select * from lugar where codsuc=? and codpro=?";
		try {
			return db.queryForObject(consulta, new BeanPropertyRowMapper<Lugar>(Lugar.class),codsuc,codpro);
		} catch (Exception e) {
			LOGGER.info("obtenerPorSucursalProducto:"+e.toString());
			return null;
		}		
	}
	public Long adicionar(Lugar obj) {
		try {
			Long cod=db.queryForObject("(select coalesce(max(codlugar),0)+1 from lugar)", Long.class);
			consulta = "insert into lugar(codlugar,codsuc,codpro,codposicion) values(?,?,?,?);";
			db.update(consulta, cod,obj.getCodsuc(),obj.getCodpro(),obj.getCodposicion());
			return cod;
		} catch (Exception e) {
			LOGGER.error("adicionar:" + e.toString());
			return -1L;
		}
	}
	public Long obtenerOrAdicionarLugarPorProducto(Integer codsuc,Long codpro){
		Lugar lugar = obtenerPorSucursalProducto(codsuc, codpro);
		if(lugar == null) {
			lugar = new Lugar();
			lugar.setCodsuc(codsuc);
			lugar.setCodpro(codpro);
			lugar.setCodposicion(Constantes.SIN_POSICION);
			return adicionar(lugar);
		}
		return lugar.getCodlugar();
	}
	public void modificar(Lugar obj) {
		try {
			consulta = "update lugar set codposicion=? where codlugar=?;";
			db.update(consulta, obj.getCodposicion(),obj.getCodlugar());
		} catch (Exception e) {
			LOGGER.info("modificar:" + e.toString());
		}
	}
	public boolean eliminar(Lugar obj) throws SQLException{
		try {
			db.update("delete from lugar where codlugar=?", obj.getCodlugar());
			return true;
		} catch (Exception e) {
			LOGGER.info("eliminar:" + e.toString());
			return false;
		}
	}
	public Map<String, Object> verificar(Lugar obj){
		try {
			String sql="select lugar.*,posicion.nombre xposicion,mueble.nombre xmueble,tipo_mueble.nombre xtipo_mueble from lugar "
					+ "join posicion on posicion.codposicion=lugar.codposicion "
					+ "join mueble on mueble.codmueble=posicion.codmueble "
					+ "join tipo_mueble on tipo_mueble.codtimu=mueble.codtimu "
					+ "where codpro=? and codsuc=?";
			return db.queryForMap(sql,obj.getCodpro(),obj.getCodsuc());
		} catch (Exception e) {
			LOGGER.info("verificar:" + e.toString());
			return null;
		}
	}
}
