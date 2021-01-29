package Servicios;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import Modelos.Almacen;
import Modelos.Sucursal;
import Modelos.ViewAlmacen;
import Utiles.UtilDataTableS;
import Wrap.AlmacenUngroupWrap;
import Wrap.AlmacenWrap;
import pagination.DataTableResults;
import pagination.SqlBuilder;


@Service
public class AlmacenImpl implements AlmacenS{
	@Autowired
	private UtilDataTableS utilDataTableS;
	private String sql;
	private static final Logger LOGGER = Logger.getLogger("AlmacenImpl");
	JdbcTemplate db;
	@Autowired
	public void setJdbcTemplate(DataSource dataSource) {
		this.db = new JdbcTemplate(dataSource);
	}
	public Long adicionar(Almacen a){
		try {
			Long cod=db.queryForObject("select coalesce(max(codalmacen),0)+1 from almacen", Long.class);
			sql="insert into almacen(codalmacen,codlugar,cantidad) values(?,?,?)";
			db.update(sql, cod, a.getCodlugar(), a.getCantidad());
			a.setCodalmacen(cod);
			return cod;
		} catch (Exception e) {
			LOGGER.error("adicionar:"+e.toString());
			return -1L;
		}
	}
	public DataTableResults<ViewAlmacen> listar(HttpServletRequest request){
		Sucursal sucursal=(Sucursal)request.getSession().getAttribute("sucursal");
		SqlBuilder sql = new SqlBuilder();
		sql.setSelect("*");
		sql.setFrom("view_almacen");
		sql.setWhere("codsuc=:codsuc");
		sql.addParameter("codsuc", sucursal.getCodsuc());
		return utilDataTableS.list(request, ViewAlmacen.class, sql);
	}
	private String sqlClasification(String clasificacion) {
		if(clasificacion.equals("menorInventarioMinimoUnidad"))
			return " and cantidad <= inventario_minimo_unidad";
		if(clasificacion.equals("menorInventarioMinimoCaja"))
			return " and cantidad <= inventario_minimo_caja";
		if(clasificacion.equals("menorInventarioMinimoPaquete"))
			return " and cantidad <= inventario_minimo_paquete";
		if(clasificacion.equals("mayorInventarioMinimoUnidad"))
			return " and cantidad > inventario_minimo_unidad";
		if(clasificacion.equals("mayorInventarioMinimoCaja"))
			return " and cantidad > inventario_minimo_caja";
		if(clasificacion.equals("mayorInventarioMinimoPaquete"))
			return " and cantidad > inventario_minimo_paquete";
		if(clasificacion.equals("todos"))
			return "";
		return "";
	}
	@Override
	public DataTableResults<ViewAlmacen> searchByLaboratoryAndClasification(HttpServletRequest request,Integer codlab, String clasification, Integer dias){
		Sucursal sucursal=(Sucursal)request.getSession().getAttribute("sucursal");
		SqlBuilder sql = new SqlBuilder();
		String sqlLaboratorio = " and codlab=" + codlab;
		String aux = "view_almacen_completo.*,(select coalesce(sum(d.subtotal),0) from detalleventa d " + 
				"inner join venta v on v.codven = d.codven and v.estado = 1 and v.fecha between cast(current_date + interval '-"+dias+" day' as date) and current_date " + 
				"where d.codpro=view_almacen_completo.codpro and d.subtotal is not null group by d.codpro) as total_venta";
		sql.setSelect(aux);
		sql.setFrom("view_almacen_completo");
		sql.setWhere("codsuc=:codsuc"+ sqlLaboratorio + sqlClasification(clasification));
		sql.addParameter("codsuc", sucursal.getCodsuc());
		sql.addParameter("codlab", codlab);
		
		return utilDataTableS.list(request, ViewAlmacen.class, sql);
	}
	@Override
	public List<ViewAlmacen> listarProductosStockCero(Integer codSuc){
		sql = "select * from view_almacen where codsuc=? and cantidad = 0";
		return db.query(sql, new BeanPropertyRowMapper<ViewAlmacen>(ViewAlmacen.class),codSuc);
	}
	@Override
	public List<ViewAlmacen> listViewAlmacen(Integer codSuc){
		System.out.println("El codigo "+codSuc);
		sql = "Select A.*, "
				+ "       B.nombre as sucursal "
				+ "From view_almacen A Inner Join sucursal B"
				+ "     On A.codsuc = ? and B.codsuc = ?";
		return db.query(sql, new BeanPropertyRowMapper<ViewAlmacen>(ViewAlmacen.class),codSuc,codSuc);
	}
	public ViewAlmacen obtenerTotalProducto(Long codpro, Integer codSuc){
		try {
			return db.queryForObject("select * from view_almacen where codpro=? and codsuc=?", new BeanPropertyRowMapper<ViewAlmacen>(ViewAlmacen.class),codpro,codSuc);
		} catch (Exception e) {
			return null;
		}
	}
	public DataTableResults<AlmacenUngroupWrap> listarAlmacenDesagrupado(HttpServletRequest request){
		Sucursal sucursal=(Sucursal)request.getSession().getAttribute("sucursal");
		SqlBuilder sqlBuilder=new SqlBuilder("almacen a");
		sqlBuilder.setSelect("a.*,vp.*,dc.devolucion,case when(fvencimiento is null) then (select max(ds.fvencimiento) from detalle_salida ds ");
		sqlBuilder.setSelectConcat("inner join salida s on s.codsal=ds.codsal and s.est=true and s.estado='a' ");
		sqlBuilder.setSelectConcat("where ds.codalmacen=a.codalmacen) else fvencimiento end fvencimiento,");
		sqlBuilder.setSelectConcat("case when(fingreso is null) then (select max(ds.fingreso) from detalle_salida ds inner join salida s ");
		sqlBuilder.setSelectConcat("on s.codsal=ds.codsal and s.est=true and s.estado='a' where ds.codalmacen=a.codalmacen) else fingreso end fingreso ");
		sqlBuilder.addJoin("lugar l on l.codlugar=a.codlugar and l.codsuc=:codsuc");
		sqlBuilder.addJoin("view_producto vp on vp.codpro=l.codpro");
		sqlBuilder.addLeftJoin("detallecompra dc on a.codalmacen = dc.codalmacen and dc.codpro=l.codpro");
		sqlBuilder.setWhere("a.cantidad > 0");
		sqlBuilder.addParameter("codsuc", sucursal.getCodsuc());
		try {
			return utilDataTableS.list(request, AlmacenUngroupWrap.class, sqlBuilder);
		} catch (Exception e) {
			LOGGER.error("listarAlmacenDesagrupado"+e.toString());
			return null;
		}
	}
	public List<AlmacenWrap> obtenerAlmacenPorProducto(Long codpro, Integer codsuc) {
		sql = "select almacen.*,case when(fvencimiento is null) then (select max(ds.fvencimiento) from detalle_salida ds "
				+ "inner join salida s on s.codsal=ds.codsal and s.est=true and s.estado='a' "
				+ "where ds.codalmacen=almacen.codalmacen) else fvencimiento end fvencimiento," + 
				"case when(fingreso is null) then (select max(ds.fingreso) from detalle_salida ds "
				+ "inner join salida s on s.codsal=ds.codsal and s.est=true and s.estado='a' "
				+ "where ds.codalmacen=almacen.codalmacen) else fingreso end fingreso "
				+ "from almacen " + 
				"join lugar on lugar.codlugar = almacen.codlugar and lugar.codpro=? and lugar.codsuc=? " + 
				"left join detallecompra on almacen.codalmacen=detallecompra.codalmacen and detallecompra.codpro = lugar.codpro " + 
				"where almacen.cantidad > 0 order by fvencimiento asc";
		try {
			return db.query(sql, new BeanPropertyRowMapper<AlmacenWrap>(AlmacenWrap.class),codpro,codsuc);
		} catch (Exception e) {
			LOGGER.info("obtenerAlmacenPorProducto:"+e.getMessage());
			return null;
		}
	}
	public Almacen obtener(Long codalmacen) {
		try {
			return db.queryForObject("select * from almacen where codalmacen=?", 
					new BeanPropertyRowMapper<Almacen>(Almacen.class),codalmacen);
		} catch (DataAccessException e) {
			LOGGER.info("obtener"+e.toString());
			return null;
		}
	}
}
