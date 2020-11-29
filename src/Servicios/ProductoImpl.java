package Servicios;

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

import Modelos.Inventario;
import Modelos.Producto;
import Modelos.Sucursal;
import Modelos.ViewAlmacen;
import Modelos.ViewProducto;
import Utiles.UtilDataTableS;
import pagination.DataTableResults;
import pagination.SqlBuilder;


@Service
public class ProductoImpl implements ProductoS{
	private static final int ACTIVE = 1;
	@Autowired
	private UtilDataTableS utilDataTableS;
	@Autowired
	private SessionS sessionS;
	private String sql;
	
	JdbcTemplate db;
	@Autowired
	public void setJdbcTemplate(DataSource dataSource) {
		this.db = new JdbcTemplate(dataSource);
	}
	
	private static final Logger LOGGER = Logger.getLogger("ProductoImpl");
	public ViewProducto obtenerPorAlmacen(Long codAlmacen) {
		try {
			sql = "select view_producto.* from almacen " + 
					"join lugar on lugar.codlugar = almacen.codlugar " + 
					"join view_producto on view_producto.codpro = lugar.codpro " + 
					"where almacen.codalmacen=?;";
			return db.queryForObject(sql, new BeanPropertyRowMapper<ViewProducto>(ViewProducto.class), codAlmacen);
		} catch (DataAccessException e) {
			LOGGER.info("obtenerPorAlmacen:"+e.toString());
			return null;
		}
	}
	
	public Producto obtener(Long idproducto){
		try {
			sql="select p.*,t.nomtip xtipo,c.nomcat xcategoria,l.nombre xlaboratorio,a.nombre xarea,m.nombre xmedida "
					+ "from producto p "
					+ "join tipo t on t.codtip=p.codtip  "
					+ "join categoria c on c.codcat=t.codcat "
					+ "join laboratorio l on l.codlab=p.codlab "
					+ "join medida m on m.codmed=p.codmed "
					+ "join area a on a.codare=p.codare "
					+ "where p.codpro=?";
			return db.queryForObject(sql,new BeanPropertyRowMapper<Producto>(Producto.class), idproducto);
		} catch (DataAccessException e) {
			LOGGER.info("obtener:"+e.toString());
			return null;
		}
	}
	public boolean adicionar(Producto p){
		try {
			sql="insert into producto(codpro,nombre,foto,codtip,generico,codigobarra,codlab,"//7
					+ "concentracion,codmed,codare,pc_unit,pv_unit,porcentaje_unidad,estado,controlado,"//9
					+ "inventario_minimo_unidad,pareto,unixcaja,pc_caja,pv_caja,pv_descuento_caja,unixpaquete,"//7
					+ "uni_en_paquete,inventario_minimo_caja,inventario_minimo_paquete,pc_paquete,pv_paquete,"//5
					+ "porcentaje_caja,porcentaje_paquete,tipo_compra,pv_descuento_paquete,presentacion_unidad,"//5
					+ "presentacion_caja,presentacion_paquete,margen,porcentaje_descuento_caja,porcentaje_descuento_paquete) "//2 total 35
					+ "values(?,upper(?),?,?,upper(?),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";
			int success=db.update(sql,p.getCodpro(),p.getNombre(),p.getFoto(),p.getCodtip(),p.getGenerico(),p.getCodigobarra(),
					p.getCodlab(),p.getConcentracion(),p.getCodmed(),p.getCodare(),p.getPcUnit(),p.getPvUnit(),
					p.getPorcentajeUnidad(),ACTIVE,p.getControlado(),p.getInventarioMinimoUnidad(),
					p.getPareto(),p.getUnixcaja(),p.getPcCaja(),p.getPvCaja(),p.getPvDescuentoCaja(),p.getUnixpaquete(),
					p.getUniEnPaquete(),p.getInventarioMinimoCaja(),p.getInventarioMinimoPaquete(),p.getPcPaquete(),
					p.getPvPaquete(),p.getPorcentajeCaja(),p.getPorcentajePaquete(),p.getTipoCompra(),
					p.getPvDescuentoPaquete(),p.getPresentacionUnidad(),p.getPresentacionCaja(),p.getPresentacionPaquete(),p.getMargen(),
					p.getPorcentajeDescuentoCaja(),p.getPorcentajeDescuentoPaquete());
			return success==1;
		} catch (DataAccessException e) {
			LOGGER.error("adicionar:"+e.toString());
			return false;
		}
	}
	public boolean modificar(Producto p){
		try {
			sql="update producto set (nombre,foto,codtip,generico,codigobarra,codlab,"
					+ "concentracion,codmed,codare,pc_unit,pv_unit,porcentaje_unidad,controlado,"
					+ "inventario_minimo_unidad,pareto,unixcaja,pc_caja,pv_caja,pv_descuento_caja,unixpaquete,"
					+ "uni_en_paquete,inventario_minimo_caja,inventario_minimo_paquete,pc_paquete,pv_paquete,"
					+ "porcentaje_caja,porcentaje_paquete,tipo_compra,pv_descuento_paquete,presentacion_unidad,"
					+ "presentacion_caja,presentacion_paquete,margen,porcentaje_descuento_caja,porcentaje_descuento_paquete) "
					+ "=(upper(?),?,?,upper(?),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) where codpro=?;";
			int success=db.update(sql,p.getNombre(),p.getFoto(),p.getCodtip(),p.getGenerico(),p.getCodigobarra(),
					p.getCodlab(),p.getConcentracion(),p.getCodmed(),p.getCodare(),p.getPcUnit(),p.getPvUnit(),
					p.getPorcentajeUnidad(),p.getControlado(),p.getInventarioMinimoUnidad(),
					p.getPareto(),p.getUnixcaja(),p.getPcCaja(),p.getPvCaja(),p.getPvDescuentoCaja(),p.getUnixpaquete(),
					p.getUniEnPaquete(),p.getInventarioMinimoCaja(),p.getInventarioMinimoPaquete(),p.getPcPaquete(),
					p.getPvPaquete(),p.getPorcentajeCaja(),p.getPorcentajePaquete(),p.getTipoCompra(),
					p.getPvDescuentoPaquete(),p.getPresentacionUnidad(),p.getPresentacionCaja(),p.getPresentacionPaquete(),
					p.getMargen(),p.getPorcentajeDescuentoCaja(),p.getPorcentajeDescuentoPaquete(),p.getCodpro());
			return success==1;
		} catch (DataAccessException e) {
			LOGGER.error("modificar:"+e.toString());
			return false;
		}
	}
	public boolean guardarFoto(String foto,Long idproducto){
		try {
			db.update("update producto set foto=? where codpro=?",foto,idproducto);
			return true;
		} catch (DataAccessException e) {
			LOGGER.info("guardarFoto:"+e.toString());
			return false;
		}
	}
	public Long generarCodigo(){
		sql="select COALESCE(max(codpro),0)+1 from producto";
		return db.queryForObject(sql, Long.class);
	}

	public DataTableResults<Producto> listar(HttpServletRequest request){
		SqlBuilder sqlBuilder = new SqlBuilder();
		sqlBuilder.setSelect("p.*,t.nomtip xtipo,c.nomcat xcategoria,area.nombre xarea,m.nombre xmedida,l.nombre xlaboratorio");
		sqlBuilder.setFrom("producto p");
		sqlBuilder.addJoin("tipo t on t.codtip=p.codtip");
		sqlBuilder.addJoin("area on area.codare=p.codare");
		sqlBuilder.addJoin("categoria c on c.codcat=t.codcat");
		sqlBuilder.addJoin("medida m on m.codmed=p.codmed");
		sqlBuilder.addJoin("laboratorio l on l.codlab=p.codlab");
		sqlBuilder.setWhere("p.estado=1");
		return utilDataTableS.list(request, Producto.class, sqlBuilder);
	}
	public DataTableResults<Producto> listarIncompletos(HttpServletRequest request){
		SqlBuilder sqlBuilder = new SqlBuilder();
		sqlBuilder.setSelect("*");
		sqlBuilder.setFrom("producto");
		sqlBuilder.setWhere("concentracion is null or pc_unit is null or pc_caja is null or pc_paquete is null or pv_unit is null or pv_caja is null or " + 
				"pv_paquete is null or porcentaje_unidad is null or porcentaje_caja is null or porcentaje_paquete is null or porcentaje_paquete is null or codpre is null or unixcaja is null or unixpaquete is null or uni_en_paquete is null or inventario_minimo_unidad is null or inventario_minimo_caja is null or inventario_minimo_paquete is null or porcentaje_unidad is null or porcentaje_caja is null or porcentaje_paquete is null or presentacion_unidad is null or presentacion_caja is null or presentacion_paquete is null or margen is null or tipo_compra is null and estado = 1");
		return utilDataTableS.list(request, Producto.class, sqlBuilder);
	}
	public DataTableResults<Inventario> listarInventario(HttpServletRequest request){
		Sucursal sucursal=(Sucursal)request.getSession().getAttribute("sucursal");
		SqlBuilder sqlBuilder = new SqlBuilder();
		sqlBuilder.setSelect("p.*,t.nomtip xtipo,c.nomcat xcategoria,l.nombre xlaboratorio,a.nombre xarea,m.nombre xmedida,(select sum(coalesce(almacen.cantidad,0)) from almacen join lugar on lugar.codlugar=almacen.codlugar and lugar.codsuc=:codsuc and lugar.codpro=p.codpro) as cantidad");
		sqlBuilder.setFrom("producto p");
		sqlBuilder.addJoin("tipo t on t.codtip=p.codtip");
		sqlBuilder.addJoin("categoria c on c.codcat=t.codcat");
		sqlBuilder.addJoin("laboratorio l on l.codlab=p.codlab");
		sqlBuilder.addJoin("medida m on m.codmed=p.codmed");
		sqlBuilder.addJoin("area a on a.codare=p.codare");
		sqlBuilder.addParameter("codsuc", sucursal.getCodsuc());
		return utilDataTableS.list(request, Inventario.class, sqlBuilder);
	}
	public List<Map<String, Object>> posicion(){
		try {
				return db.queryForList("select * from posicion where estado=1");
		} catch (DataAccessException e) {
			System.out.println(e.toString());
			return null;
		}
		
	}
	public List<Map<String, Object>> muebles(){
		try {
				return db.queryForList("select * from mueble where estado=1");
		} catch (DataAccessException e) {
			System.out.println(e.toString());
			return null;
		}
		
	}
	public List<Map<String, Object>> listarxcategoria(int codcat){
		try {
				return db.queryForList("select producto.* from producto join tipo on tipo.codtip=producto.codtip join categoria on categoria.codcat=tipo.codcat and categoria.codcat=? where producto.estado=1",codcat);
		} catch (DataAccessException e) {
			System.out.println(e.toString());
			return null;
		}
		
	}
	public List<Map<String, Object>> listarxtipo(int codtip){
		try {
				return db.queryForList("select producto.* from producto where codtip=? and estado=1",codtip);
		} catch (DataAccessException e) {
			System.out.println(e.toString());
			return null;
		}
		
	}
	
	public boolean eliminar(Long idproducto){
		try {
			db.update("update producto set estado=0,foto='user.png' where codpro=?",idproducto);
			return true;
		} catch (DataAccessException e) {
			System.out.println("error al eliminar producto="+e.toString());
			return false;
		}
	}
	public boolean darestado(Long idproducto, int estado){
		try {
			db.update("update producto set estado=? where codpro =?",estado, idproducto);
			return true;
		} catch (DataAccessException e) {
			System.out.println("error al eliminar producto="+e.toString());
			return false;
		}
	}
	public List<Map<String, Object>> buscarproducto(String search){
		try {
			return db.queryForList("select * from producto where estado=1 and upper(nombre) like concat('%',upper(?),'%')",search);
		} catch (DataAccessException e) {
			System.out.println("error al buscar"+e.toString());
			return null;
		}
	}
	public List<Map<String, Object>> buscarproductoc(String search){
		try {
			return db.queryForList("select * from producto where upper(categoria) like concat('%',upper(?),'%')",search);
		} catch (DataAccessException e) {
			System.out.println("error al buscar"+e.toString());
			return null;
		}
	}
	public DataTableResults<ViewAlmacen> listPorKardex(HttpServletRequest request,Long codpro,String nombre,String codigo,Integer codlab){
		Sucursal sucursal = sessionS.getSucursal(request);
		SqlBuilder sqlBuilder = new SqlBuilder("view_almacen va");
		sqlBuilder.setSelect("va.*");
		sqlBuilder.setWhere("codsuc=:xcodsuc and estado=1 and (codlab = :xcodlab or 0 = :xcodlab) and cast(codpro as text) like concat('%',:xcodpro,'%') and upper(nombre) like concat('%',upper(:xnombre),'%') and upper(coalesce(codigobarra,'')) like concat('%',upper(:xcodebar),'%')");
		sqlBuilder.addParameter("xcodpro", codpro==null?"":codpro.toString());
		sqlBuilder.addParameter("xnombre", nombre);
		sqlBuilder.addParameter("xcodebar", codigo);
		sqlBuilder.addParameter("xcodlab", codlab==null?"":codlab.toString());
		sqlBuilder.addParameter("xcodsuc", sucursal.getCodsuc());
		try {
			return utilDataTableS.list(request, ViewAlmacen.class, sqlBuilder);
		} catch (Exception e) {
			LOGGER.error("listaPorKadex"+e.toString());
			return null;
		}
	}
	public List<Producto> listarTodos(){
		try {
			sql ="select producto.*,nomcat xcategoria,nomtip xtipo,medida.nombre xmedida,laboratorio.nombre xlaboratorio " + 
					"from producto join tipo on tipo.codtip=producto.codtip join categoria on categoria.codcat=tipo.codcat join medida on medida.codmed=producto.codmed join laboratorio on laboratorio.codlab=producto.codlab where producto.estado=1;";
			return db.query(sql, new BeanPropertyRowMapper<Producto>(Producto.class));
		} catch (DataAccessException e) {
			System.out.println("error ProductoImpl listarTodos: "+e.toString());
			return null;
		}
	}
	public int obtenerCantidadEnUnidades(Long codpro, Float cantidad, String tipoCantidad) {
		Producto producto = obtener(codpro);
		if(producto == null)
			return -1;
		switch(tipoCantidad) {
		case "1":
			return cantidad.intValue();
		case "2":
			return (int)(cantidad * producto.getUnixcaja());
		case "3":
			return (int)(cantidad * producto.getUnixcaja() * producto.getUnixpaquete());
		default:
			return -1;
		}
	}
	public Inventario buscarPorCodigoBarra(HttpServletRequest request,String codebar) {
		Sucursal sucursal=(Sucursal)request.getSession().getAttribute("sucursal");
		SqlBuilder sqlBuilder = new SqlBuilder("producto p");
		sqlBuilder.setSelect("p.*,t.nomtip xtipo,c.nomcat xcategoria,l.nombre xlaboratorio,a.nombre xarea,m.nombre xmedida,(select sum(coalesce(almacen.cantidad,0)) from almacen join lugar on lugar.codlugar=almacen.codlugar and lugar.codsuc=:codsuc and lugar.codpro=p.codpro) as cantidad");
		sqlBuilder.addJoin("tipo t on t.codtip=p.codtip");
		sqlBuilder.addJoin("categoria c on c.codcat=t.codcat");
		sqlBuilder.addJoin("laboratorio l on l.codlab=p.codlab");
		sqlBuilder.addJoin("medida m on m.codmed=p.codmed");
		sqlBuilder.addJoin("area a on a.codare=p.codare");
		sqlBuilder.setWhere("p.estado = 1 and p.codigobarra = ? limit 1");
		sqlBuilder.addParameter("codsuc", sucursal.getCodsuc());
		try {
			return db.queryForObject(sqlBuilder.generate(), new BeanPropertyRowMapper<Inventario>(Inventario.class), codebar);
		} catch (Exception e) {
			LOGGER.info("buscarPorCodigoBarra"+e.getMessage());
			return null;
		}
	}
}