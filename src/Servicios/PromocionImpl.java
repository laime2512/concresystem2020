	package Servicios;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import Modelos.DetallePromo;
import Modelos.Promocion;
import Wrap.PromoVec;

@Service
public class PromocionImpl implements PromoS{

	JdbcTemplate db;
	@Autowired
	public void setJdbcTemplate(DataSource dataSource) {
		this.db = new JdbcTemplate(dataSource);
	}
	
	private static final Logger LOGGER = Logger.getLogger("PromocionImpl");
	private String sql;
	public Promocion obtener(Long codpromo) throws SQLException {
		try {
			sql="select pr.*,concat(u.nombre,' ',u.ap) as xusuario,to_char(pr.fini,'DD/MM/YYYY') as xfini,to_char(pr.ffin,'DD/MM/YYYY') as xffin from promocion pr\r\n" + 
					"join usuario u on u.codusu = pr.codusu\r\n" + 
					"where pr.codpromo = ?;";
			return db.queryForObject(sql,new BeanPropertyRowMapper<Promocion>(Promocion.class), codpromo);
		} catch (Exception e) {
			LOGGER.info("obtener:" + e.toString());
			return null;
		}
	}
	public List<Promocion> obtenerPromocionesActuales(){
		sql = "select * from promocion where now() BETWEEN fini and ffin";
		try {
			List<Promocion> promocionList = db.query(sql, new BeanPropertyRowMapper<Promocion>(Promocion.class));
			if(promocionList!= null && !promocionList.isEmpty()) {
				promocionList.forEach(item ->{
					item.setDetallePromo(obtenerDetalles(item.getCodpromo()));
				});
			}
			return promocionList;
		} catch (Exception e) {
			LOGGER.info("obtenerPromocionesActuales:" + e.toString());
			return null;
		}
	}
	public List<DetallePromo> obtenerDetalles(Long codpromo){
		try {
			sql="select * from detallepromo where codpromo=?";
			return db.query(sql,new BeanPropertyRowMapper<DetallePromo>(DetallePromo.class),codpromo);
		} catch (DataAccessException e) {
			LOGGER.info("obtenerDetalles:" + e.toString());
			return null;
		}
	}
	public List<Map<String, Object>> listar(int start, boolean estado, String search, int length) {
			if (search == null)
				search = "";
			sql="select count(*) \r\n" + 
					"from promocion pr\r\n" + 
					"where pr.estpromo = ? and upper(cast(codpromo as varchar)) like concat('%',?,'%');";
			Integer tot = db.queryForObject(sql, Integer.class,estado,search);
			sql="select pr.*,to_char(pr.fcreacion,'DD/MM/YYYY') xfcreacion,to_char(pr.fini,'DD/MM/YYYY') xfini,\r\n" + 
					"to_char(pr.ffin,'DD/MM/YYYY') xffin,concat(u.nombre,' ',u.ap) as xusuario,\r\n" + 
					"row_number() over(order by pr.codpromo desc) as rn,? as tot\r\n" + 
					"from promocion pr\r\n" + 
					"join usuario u on u.codusu = pr.codusu\r\n" + 
					"where pr.estpromo = ? and upper(cast(codpromo as varchar)) like concat('%',?,'%')\r\n" + 
					"limit ?\r\n" + 
					"offset ?;";
			return db.queryForList(sql,tot,estado, search, length, start);
	}

	public Long adicionar(Promocion obj,PromoVec detalle) {
		try {
			Long codpromo = generarCodigo();
			sql = "insert into promocion(codpromo,fcreacion,fini,ffin,titulo,descripcion,codusu,gestion,estpromo) values(?,now(),to_date(?,'DD/MM/YY'),to_date(?,'DD/MM/YY'),?,?,?,?,?)";
			db.update(sql, codpromo,obj.getFini(),obj.getFfin(),obj.getTitulo(),obj.getDescripcion(),obj.getCodusu(),obj.getGestion(),true);
			sql = "insert into detallepromo(codpromo,codpro,pdescuento) values(?,?,?);";
			if(detalle != null && detalle.getCodProVec()!=null)
			for (int i = 0; i < detalle.getCodProVec().length; i++) {
				db.update(sql,codpromo, detalle.getCodProVec()[i],detalle.getpDescuentoVec()[i]);
			}
			return codpromo;
		} catch (Exception e) {
			LOGGER.error("adicionar:" + e.toString());
			return -1L;
		}
	}

	public Long generarCodigo() {
		return db.queryForObject("select COALESCE(max(codpromo),0)+1 as codpromo from promocion", Long.class);
	}
	public boolean eliminar(Long codcompra) {
		try {
			db.update("update promocion set estpromo=false where codpromo=?", codcompra);
			return true;
		} catch (Exception e) {
			LOGGER.info("generarCodigo:" + e.toString());
			return false;
		}
	}
}
