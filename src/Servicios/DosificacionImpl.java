package Servicios;


import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import Modelos.Dosificacion;
import Modelos.Factura;
import Utiles.Fechas;
import Utiles.UtilDataTableS;
import Wrap.ClienteNit;
import Wrap.FacturaDosificacionWrap;
import pagination.DataTableResults;
import pagination.SqlBuilder;

@Service("dosificacionS")
@Transactional
public class DosificacionImpl implements DosificacionS {
	
	JdbcTemplate db;
	@Autowired
	public void setJdbcTemplate(DataSource dataSource) {
		this.db = new JdbcTemplate(dataSource);
	}
	
	private static final Logger LOGGER = Logger.getLogger("DosificacionSImpl");
	@Autowired
	private UtilDataTableS utilDataTableS;
//	@Autowired
//	private SessionS sessionS;
	public DataTableResults<Dosificacion> listar(HttpServletRequest request){
		try {
//			Sucursal sucursal = sessionS.getSucursal(request);
			SqlBuilder sqlBuilder = new SqlBuilder("dosificacion d");
			sqlBuilder.setSelect("d.coddosificacion,d.sigla,d.razonsocial,d.direccion,d.telefono,d.lugar,d.nit,d.numaut,d.actividad,d.llave,");
			sqlBuilder.setSelectConcat("d.leyenda,d.mensaje,to_char(d.flimite,'DD/MM/YYYY') flimite,to_char(d.ftramite,'DD/MM/YYYY') ftramite,");
			sqlBuilder.setSelectConcat("d.numtramite,d.sfc,d.est,d.numinifac,d.numfinfac,s.nombre as xsucursal,d.codsuc");
			sqlBuilder.addJoin("sucursal s on s.codsuc = d.codsuc");
//			sqlBuilder.setWhere("d.est=1 and d.codsuc=:codsucursal");
			sqlBuilder.setWhere("d.est=1");
//			sqlBuilder.addParameter("codsucursal", sucursal.getCodsuc());
			return utilDataTableS.list(request, Dosificacion.class, sqlBuilder);
		} catch (Exception e) {
			LOGGER.error("listar"+e.getMessage());
			return null;
		}
	}
	public List<Dosificacion> listar(int inicio, int limite,String buscar,int estado){
		try {
			String sql="select count(*) from dosificacion where est=?";
			int tot=db.queryForObject(sql, Integer.class,estado);
			sql="select \r\n" +  
					"		where d.est=? and UPPER(d.razonsocial) like concat('%',upper(?),'%') order by d.coddosificacion desc LIMIT ? OFFSET ?;";
			return db.query(sql,new BeanPropertyRowMapper<Dosificacion>(Dosificacion.class),tot,estado,buscar,limite,inicio);
		} catch (Exception e) {
			LOGGER.info("listar:"+e.toString());
			return null;
		}
	}
	public Dosificacion obtener(Integer cod){
		try {
			return db.queryForObject("select coddosificacion,sigla,razonsocial,direccion,telefono,lugar,nit,numaut,actividad,llave,leyenda,mensaje,to_char(flimite,'DD/MM/YYYY') flimite,to_char(ftramite,'DD/MM/YYYY') ftramite,numtramite,sfc,est,numinifac,numfinfac from dosificacion where coddosificacion=?",new BeanPropertyRowMapper<Dosificacion>(Dosificacion.class),cod);
		} catch (Exception e) {
			LOGGER.info("obtener:"+e.toString()); 
			return null;
		}
	}
	public Dosificacion getLastData(){
		try {
			return db.queryForObject("select coddosificacion,sigla,razonsocial,direccion,telefono,lugar,nit,numaut,actividad,llave,leyenda,mensaje,to_char(flimite,'DD/MM/YYYY') flimite,to_char(ftramite,'DD/MM/YYYY') ftramite,numtramite,sfc,est, numinifac,numfinfac from dosificacion where coddosificacion in (select max(c2.coddosificacion) from dosificacion c2);",new BeanPropertyRowMapper<Dosificacion>(Dosificacion.class));
		} catch (Exception e) {
			LOGGER.info("getLastData:"+e.toString()); 
			return null;
		}
	}
	public Factura obtenerfacturaxventa(Long codven) {
		try {
			return db.queryForObject("select * from factura where codven=? and estado='activo'",new BeanPropertyRowMapper<Factura>(Factura.class),codven);
		} catch (Exception e) {
			LOGGER.info("obtenerfacturaxventa:"+e.toString()); 
			return null;
		}
	}
	public Dosificacion dosificacionactual(Integer codsuc){
		try {
			int res=db.queryForObject("select count(*) from dosificacion where CURRENT_DATE BETWEEN ftramite and flimite and est=1 and codsuc=?", Integer.class,codsuc);
			if(res>0)
				return db.queryForObject("select * from dosificacion where CURRENT_DATE BETWEEN ftramite and flimite and est=1 and codsuc=? ORDER BY coddosificacion desc LIMIT 1",new BeanPropertyRowMapper<Dosificacion>(Dosificacion.class),codsuc);
			else
				return null;
		} catch (Exception e) {
			LOGGER.info("dosificacionActual:"+e.toString()); 
			return null;
		}
	}
	public Dosificacion dosificacionUltimaDosificacion(Integer codsuc){
		try {
			int res=db.queryForObject("select count(*) from dosificacion where codsuc=? and flimite in (select max(d.flimite) from dosificacion d where d.est = 1 and d.codsuc=?);", Integer.class,codsuc,codsuc);
			if(res>0)
				return db.queryForObject("select * from dosificacion where flimite in (select max(d.flimite) from dosificacion d where d.est = 1 and d.codsuc=?) and codsuc=?;",new BeanPropertyRowMapper<Dosificacion>(Dosificacion.class),codsuc,codsuc);
			else
				return null;
		} catch (Exception e) {
			LOGGER.info("dosificacionUltimaDosificacion:"+e.toString()); 
			return null;
		}
	}
	public int adicionar(Dosificacion obj){
		try {
			String sql="select coalesce(max(coddosificacion),0)+1 from dosificacion";
			int cod=db.queryForObject(sql, Integer.class);
			sql="insert into dosificacion(coddosificacion,razonsocial,direccion,telefono,lugar,nit,numaut,actividad,llave,leyenda,mensaje,flimite,ftramite,numtramite,sfc,est,sigla,numinifac,numfinfac,codsuc) \r\n" + 
					"VALUES(?,?,?,?,?,?,?,?,?,?,?,to_date(?,'DD/MM/YYYY'),to_date(?,'DD/MM/YYYY'),?,?,1,?,?,?,?);";
			db.update(sql,cod, obj.getRazonsocial(),obj.getDireccion(),obj.getTelefono(),obj.getLugar(),obj.getNit(),obj.getNumaut(),
					obj.getActividad(),obj.getLlave(),obj.getLeyenda(),obj.getMensaje(),obj.getFlimite(),obj.getFtramite(),
					obj.getNumtramite(),obj.getSfc(),obj.getSigla(),obj.getNuminifac(),obj.getNumfinfac(), obj.getCodsuc());
			return cod;
		} catch (Exception e) {
			LOGGER.error("adicionar:"+e.toString());
			return -1;
		}
	}
	public boolean modificar(Dosificacion obj){
		try {
			String sql="update dosificacion set razonsocial=?,direccion=?,telefono=?,lugar=?,nit=?,"
					+ "numaut=?,actividad=?,llave=?,leyenda=?,mensaje=?,flimite=to_date(?,'DD/MM/YYYY'),"
					+ "ftramite=to_date(?,'DD/MM/YYYY'),numtramite=?,sfc=?,sigla=?,numinifac=?,numfinfac=?,codsuc=? where coddosificacion=?";
			db.update(sql,obj.getRazonsocial(),obj.getDireccion(),obj.getTelefono(),obj.getLugar(),obj.getNit(),
					obj.getNumaut(),obj.getActividad(),obj.getLlave(),obj.getLeyenda(),obj.getMensaje(),obj.getFlimite(),
					obj.getFtramite(),obj.getNumtramite(),obj.getSfc(),obj.getSigla(),obj.getNuminifac(),obj.getNumfinfac(), obj.getCodsuc(),obj.getCoddosificacion());
			return true;
		} catch (Exception e) {
			LOGGER.error("modificar:"+e.toString());
			return false;
		}
	}
	public boolean eliminar(Integer cod){
		try {
			db.update("update dosificacion set est=0 where coddosificacion=?", cod);
			return true;
		} catch (Exception e) {
			LOGGER.error("eliminar:"+e.toString());
			return false;
		}
	}
	public boolean finalizar(Integer cod){
		try {
			int num = obtenerUltimaFactura(cod);
			if(num < 1)
				num = 0;
			db.update("update dosificacion set est=2,numfinfac=? where coddosificacion=?", num, cod);
			return true;
		} catch (Exception e) {
			LOGGER.error("finalizar:"+e.toString());
			return false;
		}
	}
	public boolean anularFactura(Long codven){
		try {
			db.update("update factura set estado='anulado' where codven=?", codven);
			return true;
		} catch (Exception e) {
			LOGGER.error("anularFactura:"+e.toString());
			return false;
		}
	}
	public DataTableResults<Factura> listarFacturas(HttpServletRequest request,String estado,Integer dosificacion){
		try {
			SqlBuilder sqlBuilder = new SqlBuilder("factura");
			sqlBuilder.setSelect("factura.coddosificacion,numfac,to_char(fecfac,'DD/MM/YYYY hh:mm:ss') fecfac,");
			sqlBuilder.setSelectConcat("nitfac,cliente_nit,codcontrol,estado,codven,factura.total");
			sqlBuilder.setWhere("factura.coddosificacion=:dosificacion and estado=:estado");
			sqlBuilder.addParameter("estado", estado);
			sqlBuilder.addParameter("dosificacion", dosificacion);
			return utilDataTableS.list(request, Factura.class, sqlBuilder);
		} catch (Exception e) {
			LOGGER.error("listarFacturas"+e.getMessage());
			return null;
		}
	}
	public DataTableResults<ClienteNit> listarNit(HttpServletRequest request){
		try {
			SqlBuilder sqlBuilder = new SqlBuilder("factura");
			sqlBuilder.setSelect("nitfac,cliente_nit");
			return utilDataTableS.list(request, ClienteNit.class, sqlBuilder);
		} catch (Exception e) {
			LOGGER.error("listarNit"+e.getMessage());
			return null;
		}
	}
	public String buscarClientexnit(String nit) {
		try {
			int res=db.queryForObject("select count(*) from factura where nitfac=?", Integer.class,nit);
			if(res>0)
				return db.queryForObject("select cliente from factura where nitfac=? order by factura.coddosificacion,numfac desc LIMIT 1", String.class,nit);
			else
				return "";
		} catch (Exception e) {
			LOGGER.info("buscarClientexnit:"+e.toString());
			return "";
		}
	}
	public int adicionarFactura(Factura f){
		try {
			db.update("insert into factura(coddosificacion,numfac,fecfac,nitfac,cliente_nit,codcontrol,estado,codven,total) values(?,?,CURRENT_TIMESTAMP,?,?,?,?,?,?	)",
									f.getCoddosificacion(),f.getNumfac(),f.getNitfac(),f.getClienteNit(),f.getCodcontrol(),"activo",f.getCodven(),f.getTotal());
			return 1;
		} catch (Exception e) {
			LOGGER.error("adicionarFactura:"+e.toString());
			return -1;
		}
	}
	public int obtenerNumFac(Integer cod){
		try {
			int cantidad = db.queryForObject("select count(*) from factura where factura.coddosificacion=?", Integer.class,cod);
			if(cantidad > 0)
				return db.queryForObject("select coalesce(max(numfac),0)+1 num from factura where factura.coddosificacion=?", Integer.class,cod);
			else
				return db.queryForObject("select coalesce(max(numinifac),1) from dosificacion where coddosificacion=?", Integer.class,cod);
		} catch (Exception e) {
			LOGGER.info("obtenerNumFac:"+e.toString());
			return -1;
		}
	}
	public int obtenerUltimaFactura(Integer cod){
		try {
			int cantidad = db.queryForObject("select count(*) from factura where factura.coddosificacion=?", Integer.class,cod);
			if(cantidad > 0)
				return db.queryForObject("select coalesce(max(numfac),0) num from factura where factura.coddosificacion=?", Integer.class,cod);
			else
				return db.queryForObject("select coalesce(max(numinifac),0) from dosificacion where coddosificacion=?", Integer.class,cod);
		} catch (Exception e) {
			LOGGER.info("obtenerUltimaFactura:"+e.toString());
			return -1;
		}
	}
	@Override
	public List<FacturaDosificacionWrap> listFacturaDosificacion(String fini, String ffin, Integer codsuc){
		try {
			Date fechaIni=Fechas.convertirStringToDate(fini);
			Date fechaFin=Fechas.convertirStringToDate(ffin);
			String sql = "select f.*,d.razonsocial,d.nit,d.numaut,d.flimite,d.sigla,s.nombre as xsucursal from factura f " + 
					"inner join dosificacion d on d.coddosificacion = f.coddosificacion and d.codsuc = ? " + 
					"inner join sucursal s on s.codsuc = d.codsuc " + 
					"where f.fecfac between ? and ?";
			return db.query(sql, new BeanPropertyRowMapper<FacturaDosificacionWrap>(FacturaDosificacionWrap.class),codsuc, fechaIni, fechaFin);
		} catch (Exception e) {
			LOGGER.error("listFacturaDosificacion: "+e.toString());
			return null;
		}
	} 
}