package Servicios;

import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import Modelos.Caja;
import Modelos.DetalleCaja;
import Utiles.Numeros;
import Utiles.UtilDataTableS;
import pagination.DataTableResults;
import pagination.SqlBuilder;

@Service
@Transactional
public class CajaImpl implements CajaS {
	private String consulta;
	@Autowired
	private UtilDataTableS utilDataTableS;
	private static final Logger LOGGER = Logger.getLogger("CajaImpl");
	JdbcTemplate db;
	@Autowired
	public void setJdbcTemplate(DataSource dataSource) {
		this.db = new JdbcTemplate(dataSource);
	}
	public DataTableResults<Caja> lista(HttpServletRequest request){
		SqlBuilder sqlBuilder = new SqlBuilder("caja");
		sqlBuilder.setSelect("caja.*,sucursal.nombre xsucursal,concat(usuario.nombre,' ',ap,' ',am) xusuario");
		sqlBuilder.setSelectConcat(",to_char(caja.fini,'DD/MM/YYYY') as xfecha");
		sqlBuilder.addJoin("usuario on usuario.codusu=caja.codusu");
		sqlBuilder.addJoin("sucursal on sucursal.codsuc=caja.codsuc");
		sqlBuilder.setWhere("caja.estado=1");
		try {
			return utilDataTableS.list(request, Caja.class, sqlBuilder);
		} catch (Exception e) {
			LOGGER.error("listar" + e.toString());
			return null;
		}
	}
	public DataTableResults<DetalleCaja> listarDetalles(HttpServletRequest request,Long codcaja) {
		try {
			SqlBuilder sqlBuilder = new SqlBuilder("detallecaja");
			sqlBuilder.setSelect("detallecaja.*,cuenta.nombre xcuenta,to_char(detallecaja.fecha,'DD/MM/YYYY HH:mi:ss') as xfecha");
			sqlBuilder.setSelectConcat(",cuenta.estado as xtipo");
			sqlBuilder.addJoin("cuenta on cuenta.codcuenta=detallecaja.codcuenta");
			sqlBuilder.setWhere("detallecaja.codcaja=:codcaja");
			sqlBuilder.addParameter("codcaja", codcaja);
			return utilDataTableS.list(request, DetalleCaja.class, sqlBuilder);
		} catch (Exception e) {
			LOGGER.error("listarDetalles" + e.toString());
			return null;
		}
	}

	public Caja obtener(Caja obj) throws SQLException {
		try {
			consulta = "select caja.codcaja,caja.codusu,fini,monini,\n" + 
					"case when monfin is null then 0 else monfin end as monfin,\n" + 
					"caja.estado,caja.codsuc,\n" + 
					"case when observacion is null then '' else observacion end as observacion,\n" + 
					"case when ffin is null then '' else to_char(caja.ffin,'DD/MM/YYYY HH:mm:ss') end as xffin\n" + 
					",tipo,sucursal.nombre xsucursal,\n" + 
					"concat(usuario.nombre,' ',ap,' ',am) xusuario,to_char(caja.fini,'DD/MM/YYYY HH:mm:ss') as xfecha \n" + 
					"from caja \n" + 
					"join usuario on usuario.codusu=caja.codusu \n" + 
					"join sucursal on sucursal.codsuc=caja.codsuc \n" + 
					"where codcaja=?";
			Caja map = null;
			try {
				map=db.queryForObject(consulta, new BeanPropertyRowMapper<Caja>(Caja.class), obj.getCodcaja());
			} catch (DataAccessException e) {
				// TODO: handle exception
				LOGGER.info("No se encontro una caja");
			}
			if(map!=null) {
				Float mInicial=map.getMonini();
				Float mTransaccional= Float.parseFloat(obtenerTotal(obj.getCodcaja()));
				Float mActual=mInicial + mTransaccional;
				map.setMonsistema(Numeros.formato2decimales(mActual));
				List<DetalleCaja> lista=obtenerDetalles(obj.getCodcaja());
				map.setDetalles(lista);
			}
			return map;
		} catch (Exception e) {
			LOGGER.error("obtener:"+ e.toString());
			return null;
		}
	}
	public String obtenerTotal(Long codcaja) {
		consulta="select round(cast((select coalesce(sum(monto),0) from detallecaja where codcaja=? and estado=1)-(select coalesce(sum(monto),0) from detallecaja where codcaja=? and estado=0) as numeric),2)";
		try {
			return db.queryForObject(consulta, String.class,codcaja,codcaja);
		} catch (Exception e) {
			LOGGER.error("obtenerTotal"+e.toString());
			return "0";
		}
	}
	public List<DetalleCaja> obtenerDetalles(Long codcaja){
		try {
			consulta = "select detallecaja.*,cuenta.nombre as xcuenta from detallecaja " + 
					"join cuenta on cuenta.codcuenta=detallecaja.codcuenta " + 
					"where detallecaja.codcaja=? and detallecaja.estado = 1";
					return db.query(consulta, new BeanPropertyRowMapper<DetalleCaja>(DetalleCaja.class),codcaja);
		} catch (Exception e) {
			LOGGER.info("detallecaja:" + e.toString());
			return null;
		}
	}
	public Long adicionar(Caja obj) {
		try {
			Long codCaja=db.queryForObject("select coalesce(max(codcaja),0)+1 from caja", Long.class);
			consulta = "insert into caja(codcaja,codusu,fini,monini,monfin,monsistema,codsuc,estado,tipo) values(?,?,now(),?,?,?,?,1,?);";
			db.update(consulta,codCaja, obj.getCodusu(), obj.getMonini(),null, obj.getMonsistema(), obj.getCodsuc(),obj.getTipo());
			return codCaja;
		} catch (Exception e) {
			LOGGER.info("adicionar " + e.toString());
			return -1L;
		}
	}
	public Long adicionarDetalle(DetalleCaja det) {
		try {
			Long coddet = db.queryForObject("select coalesce(max(coddetcaja),0)+1 from detallecaja where codcaja=?",
					Long.class, det.getCodcaja());
			consulta = "insert into detallecaja(codcaja,coddetcaja,monto,fecha,codcuenta,estado) values(?,?,?,now(),?,?)";
			db.update(consulta, det.getCodcaja(), coddet, det.getMonto(), det.getCodcuenta(),det.getEstado());
			return coddet;
		} catch (Exception e) {
			LOGGER.error("adicionarDetalle:" + e.toString());
			return -1L;
		}
	}
	public boolean eliminar(Caja obj) throws SQLException {
		try {
			db.update("update caja set estado = 0 where codcaja=?", obj.getCodcaja());
			return true;
		} catch (Exception e) {
			LOGGER.error("eliminar" + e.toString());
			return false;
		}
	}
	public boolean finalizar(Caja obj) throws SQLException {
		try {
			String tot =obtenerTotal(obj.getCodcaja());
			Float totalSistema = tot!=null?Float.parseFloat(tot):0;
			db.update("update caja set tipo=2,ffin=now(),observacion=?,monfin=?,monsistema=? where codcaja=?",obj.getObservacion(),obj.getMonfin(),totalSistema,obj.getCodcaja());
			return true;
		} catch (Exception e) {
			LOGGER.error("finalizar:"+ e.toString());
			return false;
		}
	}
	public Caja verificarUsuarioActivo(Long codusu,Integer codsuc) {
		try {
			consulta = "select codcaja from caja where caja.estado=1 and cast(fini as date)<=cast(now() as date) and tipo=1 and caja.codusu=? and caja.codsuc=? order by codcaja desc limit 1";
			Long codcaja = db.queryForObject(consulta, Long.class, codusu, codsuc);
			if(codcaja != null) {
				Caja cajaActiva = new Caja();
				cajaActiva.setCodcaja(codcaja);
				return obtener(cajaActiva);
			}else
				return null;
		} catch (Exception e) {
			LOGGER.info("verificarUsuarioActivo:"+e.toString());
			return null;
		}
	}
	public Caja getLastCajaFinallyUser(Long codusu, Integer codsuc) {
		try {
			consulta = "select codcaja from caja where codusu = ? and codsuc=? and estado = 1 and tipo=2 ORDER BY codcaja desc limit 1";
			Long codcaja = db.queryForObject(consulta, Long.class,codusu, codsuc);
			if(codcaja !=null) {
				Caja lastCaja = new Caja();
				lastCaja.setCodcaja(codcaja);
				return obtener(lastCaja);
			}else
				return null;
				
		} catch (Exception e) {
			LOGGER.info("No se encontro ninguna caja activa para usuario "+codusu+"- "+codsuc+", error:"+e.toString());
			return null;
		}
	}
}
