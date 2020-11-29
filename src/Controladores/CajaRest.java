package Controladores;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import Modelos.Caja;
import Modelos.DetalleCaja;
import Modelos.Rol;
import Modelos.Sucursal;
import Modelos.Usuario;
import Servicios.AccesoUsuarioS;
import Servicios.CajaS;
import Utiles.Constantes;
import Utiles.DashBoard;
import Utiles.Fechas;
import Utiles.GeneradorReportes;
import pagination.DataTableResults;

@Controller
@RequestMapping("/caja/*")
public class CajaRest {
	
	@Autowired
	private CajaS cajaS;
	@Autowired
	private AccesoUsuarioS accesoUsuarioS;
	private String className=CajaRest.class.getName();
	private Logger log=Logger.getLogger(className);
	
	@RequestMapping("gestion")
	public String gestion(HttpServletRequest request,Model model) throws SQLException{
		Sucursal sucursal=(Sucursal) request.getSession().getAttribute("sucursal");
		if(sucursal!=null) {
			model.addAttribute("xfecha",Fechas.obtenerFecha("dd/MM/yy"));
			model.addAttribute("usuarios",accesoUsuarioS.obtenerPorSucursal(sucursal.getCodsuc()));
			return "caja/gestion";
		}
		model.addAttribute("msg","Sesion invalida");
		return "caja/gestion";
	}
	@RequestMapping("gestionVendedor")
	public String gestionUsuario(HttpServletRequest request,Model model) throws SQLException{
		Usuario user = (Usuario) request.getSession().getAttribute("user");
		Sucursal sucursal=(Sucursal) request.getSession().getAttribute("sucursal");
		Rol rol = (Rol) request.getSession().getAttribute("rol");
		if(sucursal!=null && user!=null) {
			Caja caja=cajaS.verificarUsuarioActivo(user.getCodusu(), sucursal.getCodsuc());
			model.addAttribute("isActive", caja!=null);
			if(caja != null) {//Si existe caja activa
				model.addAttribute("caja",caja);
			}else {
				Caja ultimaCaja = cajaS.getLastCajaFinallyUser(user.getCodusu(),sucursal.getCodsuc());
				model.addAttribute("lastCaja", ultimaCaja);
				model.addAttribute("existLastCaja", (ultimaCaja != null));
			}
			model.addAttribute("user",user);
			model.addAttribute("rol",rol);
			model.addAttribute("sucursal",sucursal);
			model.addAttribute("xfecha",Fechas.obtenerFecha("dd/MM/yy"));
			model.addAttribute("fecha_literal",Fechas.obtenerFechaLiteral(LocalDate.now()));
			return "caja/gestionVendedor";
		}
		model.addAttribute("msg","Sesion invalida");
		return "mensaje/msgSucursal";
	}
	@RequestMapping("gestionDetalle")
	public String gestionDetalles(HttpServletRequest request,Model model,Caja obj) throws SQLException{
		model.addAttribute("caja",cajaS.obtener(obj));
		return "caja/gestionDetalle";
	}
	@RequestMapping("listaDetalles")
	public @ResponseBody DataTableResults<DetalleCaja> listaDetalles(HttpServletRequest request,Long codcaja)throws IOException{
		return cajaS.listarDetalles(request, codcaja);
	}
	@RequestMapping("lista")
	public @ResponseBody DataTableResults<Caja> lista(HttpServletRequest request)throws IOException, SQLException{
		return cajaS.lista(request);
	}
	@RequestMapping("guardar")
	public @ResponseBody Map<String, Object> guardar(HttpServletRequest request,Caja obj){
		Usuario user=(Usuario)request.getSession().getAttribute("user");
		Sucursal sucursal=(Sucursal)request.getSession().getAttribute("sucursal");
		Map<String,Object> Data=new HashMap<>();
		if(user!=null && sucursal!=null){
			try {
				obj.setCodsuc(sucursal.getCodsuc());
				obj.setMonsistema(obj.getMonini());
				obj.setTipo(1);
				Data.put("data", cajaS.adicionar(obj));
				Data.put("status", true);
			} catch (Exception e) {
				log.info("error al adicionar "+className+"="+e.toString());
				Data.put("status", false);
			}
		}
		return Data;
	}
	
	@RequestMapping("obtener")
	public @ResponseBody Map<String, Object> obtener(Caja obj){
		Map<String, Object> Data = new HashMap<String, Object>();
		try {
			Data.put("data", cajaS.obtener(obj));
			Data.put("fecha", Fechas.getDateNowToString());
			Data.put("status", true);
		} catch (Exception e) {
			Data.put("status", false);
			log.info("error al obtener="+e.toString());
		}
		return Data;
	}
	@RequestMapping("finalizar")
	public @ResponseBody Map<String, Object> finalizar(HttpServletRequest request,Caja obj)throws IOException{
		Usuario us=(Usuario)request.getSession().getAttribute("user");
		Map<String, Object> Data = new HashMap<String, Object>();
		if(us!=null){
				try {
					Data.put("status", cajaS.finalizar(obj));
					Data.put("data", cajaS.obtener(obj));
				} catch (SQLException e) {
					e.printStackTrace();
					log.info("Error al eliminar "+className+"="+e.toString());
				}
		}
		return Data;
	}
	@RequestMapping("eliminar")
	public @ResponseBody Map<String, Object> eliminar(HttpServletRequest request,Caja obj)throws IOException{
		Usuario us=(Usuario)request.getSession().getAttribute("user");
		Map<String, Object> Data = new HashMap<String, Object>();
		if(us!=null){
				try {
					Data.put("status", cajaS.eliminar(obj));
				} catch (SQLException e) {
					e.printStackTrace();
					log.info("Error al eliminar "+className+"="+e.toString());
				}
		}
		return Data;
	}
	
	@Autowired
	DataSource dataSource;

	@RequestMapping("imprimir")
	public void imprimir(HttpServletRequest request, HttpServletResponse response, Model model, Caja obj)
			throws IOException, SQLException {
		String nombre = "caja " + obj.getCodcaja(), tipo = "pdf", estado = "inline";
		Map<String, Object> parametros = new HashMap<String, Object>();
		String url = "/reportes/caja.jasper";
		Caja caja = cajaS.obtener(obj);
		obtenerEncabezado(request, parametros);
		parametros.put("xfecha", caja.getXfecha());
		parametros.put("xffin", caja.getXffin());
		parametros.put("codcaja", obj.getCodcaja());
		parametros.put("xmonini", caja.getMonini());
		parametros.put("xmonreal", caja.getMonfin());
		parametros.put("xusuario", caja.getXusuario());
		parametros.put("xobservacion", caja.getObservacion());
		parametros.put("xsucursal", caja.getXsucursal());
		Integer caja_tipo=caja.getTipo();
		parametros.put("xtipo", caja_tipo==1?"Activo":"Finalizado");
		GeneradorReportes g = new GeneradorReportes();
		try {
			g.generarReporte(response, getClass().getResource(url), tipo, parametros, dataSource.getConnection(),
					nombre, estado);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@RequestMapping("imprimirSimple")
	public void imprimirSimple(HttpServletRequest request, HttpServletResponse response, Model model, Caja obj)
			throws IOException, SQLException {
		String nombre = "caja_resumen_" + obj.getCodcaja(), tipo = "pdf", estado = "inline";
		Map<String, Object> parametros = new HashMap<String, Object>();
		String url = "/reportes/caja_vendedor_75mm.jasper";
		Caja caja = cajaS.obtener(obj);
		obtenerEncabezado(request, parametros);
		parametros.put("xfecha", caja.getXfecha());
		parametros.put("xffin", caja.getXffin());
		parametros.put("codcaja", obj.getCodcaja());
		parametros.put("xmonini", caja.getMonini());
		parametros.put("subtotal", caja.getMonsistema());
		parametros.put("xmonreal", caja.getMonfin());
		parametros.put("xusuario", caja.getXusuario());
		parametros.put("xobservacion", caja.getObservacion());
		parametros.put("xsucursal", caja.getXsucursal());
		parametros.put("xtipo", 1==caja.getTipo()?"Activo":"Finalizado");
		GeneradorReportes g = new GeneradorReportes();
		try {
			g.generarReporte(response, getClass().getResource(url), tipo, parametros, dataSource.getConnection(),
					nombre, estado);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void obtenerEncabezado(HttpServletRequest request,Map<String, Object> param) {
		Usuario usuario=(Usuario)request.getSession().getAttribute("user");
		if(usuario!=null) {
			Sucursal sucursal=(Sucursal)request.getSession().getAttribute("sucursal");
			param.put("empresa",Constantes.SIGLA);
			param.put("usuario",usuario.getNombre()+" "+usuario.getAp()+(usuario.getAm()!=null?"":(" "+usuario.getAm())));
			if(sucursal!=null) {
				param.put("sucursal", sucursal.getNombre());
				param.put("telefono", sucursal.getTelefono());
				param.put("direccion", sucursal.getDireccion());
			}
		}
	}
}
