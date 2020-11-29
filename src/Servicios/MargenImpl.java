package Servicios;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import Modelos.DetalleMargen;
import Modelos.Margen;
import Utiles.UtilDataTableS;
import Wrap.MargenWrap;
import pagination.DataTableResults;
import pagination.SqlBuilder;

@Service
@Transactional
public class MargenImpl implements MargenS{
	private String consulta;
	private static final Logger LOGGER = Logger.getLogger("MargenImpl");
	
	JdbcTemplate db;
	@Autowired
	public void setJdbcTemplate(DataSource dataSource) {
		this.db = new JdbcTemplate(dataSource);
	}
	
	@Autowired
	private UtilDataTableS utilDataTableS;
	public DataTableResults<Margen> listar(HttpServletRequest request){
		try {
			SqlBuilder sqlBuilder = new SqlBuilder("margen");
			sqlBuilder.setSelect("margen.*,concat(nombre,' ',ap) as xusuario");
			sqlBuilder.addJoin("usuario on usuario.codusu=margen.codusu");
			sqlBuilder.setWhere("margen.estado=true");
			return utilDataTableS.list(request, Margen.class, sqlBuilder);
		} catch (Exception e) {
			LOGGER.error("listar"+e.getMessage());
			return null;
		}
	}
	public List<Map<String, Object>> listar() {
		try {
			return db.queryForList("select * from margen where estado=true;");
		} catch (Exception e) {
			LOGGER.info("Listar:"+e.toString());
			return null;
		}
	}
	public Margen obtener(Margen obj) throws SQLException {
		try {
			consulta = "select margen.*,concat(nombre,' ',ap) as xusuario from margen join usuario on usuario.codusu=margen.codusu where cod_margen=?";
			Margen margen = db.queryForObject(consulta, new BeanPropertyRowMapper<Margen>(Margen.class), obj.getCodMargen());
			if(margen != null) {
				margen.setDetalleList(obtenerDetalle(margen.getCodMargen()));
			}
			return margen;
		} catch (Exception e) {
			LOGGER.info("obtener:" + e.toString());
			return null;
		}
	}
	public Margen getLastMargen() throws SQLException {
		try {
			consulta = "select margen.* from margen where estado=true order by cod_margen desc limit 1";
			Margen margen = db.queryForObject(consulta, new BeanPropertyRowMapper<Margen>(Margen.class));
			if(margen != null) {
				margen.setDetalleList(obtenerDetalle(margen.getCodMargen()));
			}
			return margen;
		} catch (Exception e) {
			LOGGER.info("getLastMargen:" + e.toString());
			return null;
		}
	}
	public List<DetalleMargen> obtenerDetalle(Integer cod) {
		try {
			consulta = "select * from detalle_margen where cod_margen = ? order by number_margin asc";
			return db.query(consulta, new BeanPropertyRowMapper<DetalleMargen>(DetalleMargen.class),cod);
		} catch (DataAccessException e) {
			LOGGER.info("obtenerDetalle:"+e.toString());
			return null;
		}
	}
	public boolean adicionar(MargenWrap obj) {
		try {
			consulta = "select coalesce(max(cod_margen),0)+1 from margen";
			Integer cod = db.queryForObject(consulta, Integer.class);
			obj.getMargen().setCodMargen(cod);
			consulta = "insert into margen(cod_margen,codusu,date_register,observacion,estado) values(?,?,now(),?,true);";
			int isSuccess = db.update(consulta, cod, obj.getMargen().getCodusu(),obj.getMargen().getObservacion());
			if(isSuccess == 1) {//Se adiciono correctamente
				adicionarDetalle(obj);
			}
			return isSuccess == 1;
		} catch (Exception e) {
			LOGGER.info("adicionar:" + e.toString());
			return false;
		}
	}
	public void adicionarDetalle(MargenWrap margenWrap) {
		consulta = "insert into detalle_margen(cod_margen,number_margin,concept,type_margin,porcentaje_unidad) values(?,?,?,upper(?),?)";
		if(margenWrap.getConceptVec()!=null && margenWrap.getConceptVec().length>0) {
			for (int i = 0; i < margenWrap.getConceptVec().length; i++) {
				db.update(consulta,margenWrap.getMargen().getCodMargen(),(i+1),margenWrap.getConceptVec()[i],
						margenWrap.getTypeMarginVec()[i],margenWrap.getPorcentajeUnidadVec()[i]);
			}
		}
	}
	public void modificar(MargenWrap margenWrap) {
		try {
			consulta = "update margen set codusu=?,observacion=?,date_register=now() where cod_margen=?;";
			db.update(consulta, margenWrap.getMargen().getCodusu(), margenWrap.getMargen().getObservacion(),margenWrap.getMargen().getCodMargen());
			consulta = "delete from detalle_margen where cod_margen = ?";
			db.update(consulta, margenWrap.getMargen().getCodMargen());
			adicionarDetalle(margenWrap);
		} catch (Exception e) {
			LOGGER.info("modificar:" + e.toString());
		}
	}
	public boolean eliminar(Margen obj) throws SQLException{
		try {
			db.update("update margen set estado=false where cod_margen=?", obj.getCodMargen());
			return true;
		} catch (Exception e) {
			LOGGER.info("Eliminar:" + e.toString());
			return false;
		}
	}
}
