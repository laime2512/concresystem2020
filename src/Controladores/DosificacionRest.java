package Controladores;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import Modelos.Dosificacion;
import Modelos.Factura;
import Modelos.General;
import Modelos.Sucursal;
import Servicios.DosificacionS;
import Servicios.SucursalS;
import Utiles.Constantes;
import Utiles.DashBoard;
import Utiles.Fechas;
import Utiles.GeneradorReportes;
import Wrap.ClienteNit;
import pagination.DataTableResults;

@RestController
@RequestMapping("/dosificacion/*")
public class DosificacionRest extends DashBoard {
	@Autowired
	private DosificacionS dosificacionS;
	@Autowired
	private SucursalS sucursalS;
	
	private static final Logger LOGGER = Logger.getLogger("DosificacionRest");
	@RequestMapping("gestionFacturas/{id}")
	public ModelAndView gestion(Model model,@PathVariable("id")int cod){
		model.addAttribute("dosificacion",dosificacionS.obtener(cod));
		return new ModelAndView("dosificacion/gestionFacturas");
	}
	@RequestMapping("gestion")
	public ModelAndView gestionFacturas(Model model){
		model.addAttribute("sucursales", sucursalS.listar());
		return new ModelAndView("dosificacion/gestion");
	}
	@RequestMapping("listar")
	public @ResponseBody  DataTableResults<Dosificacion> lista(HttpServletRequest request)throws IOException{
		return dosificacionS.listar(request);
	}
	@RequestMapping("listarFacturas")
	public DataTableResults<Factura> listaFacturas(HttpServletRequest request,Integer coddosificacion,String xestado)throws IOException{
		return dosificacionS.listarFacturas(request,xestado,coddosificacion);
	}
	@RequestMapping("listaNit")
	public DataTableResults<ClienteNit> listaNit(HttpServletRequest request)throws IOException{
		return dosificacionS.listarNit(request);
	}
	@RequestMapping(value = "adicionar")
	public ResponseEntity<Map<String, Object>> adicionar(HttpServletRequest request, Dosificacion obj){
		Map<String, Object> Data=new HashMap<String, Object>();
		try {
			//Sucursal sucursal = (Sucursal) request.getSession().getAttribute(Constantes.Sesiones.SUCURSAL);
			Data.put("status", dosificacionS.adicionar(obj));
			Data.put("status", true);
		} catch (Exception e) {
			LOGGER.error("error en el controlador adicionar dosificacion "+e.toString());
			Data.put("status", false);
		}
		return new ResponseEntity<Map<String,Object>>(Data, HttpStatus.OK);
	}
	@RequestMapping(value = "modificar")
	public ResponseEntity<Map<String, Object>> modificar(HttpServletRequest request,Dosificacion dosificacion){
		Map<String, Object> Data=new HashMap<String, Object>();
		try {
			Data.put("status", dosificacionS.modificar(dosificacion));
		} catch (Exception e) {
			LOGGER.error("error en el contdosificacionador adicionar "+e.toString());
			Data.put("status", false);
		}
		return new ResponseEntity<Map<String,Object>>(Data, HttpStatus.OK);
	}
	@RequestMapping ("eliminar")
	public ResponseEntity<Map<String, Object>> eliminar(HttpServletRequest request,Integer coddosificacion,int tipo2){
		Map<String, Object> Data=new HashMap<String, Object>();
		try {
			if(tipo2==1)
				Data.put("status", dosificacionS.eliminar(coddosificacion));
			else
				Data.put("status", dosificacionS.finalizar(coddosificacion));
		} catch (Exception e) {
			LOGGER.error("error en el contdosificacionador dar estado "+e.toString());
			Data.put("status", false);
		}
		return new ResponseEntity<Map<String,Object>>(Data, HttpStatus.OK);
	}
	@RequestMapping ("obtener")
	public ResponseEntity<Dosificacion> obtener(HttpServletRequest request, HttpServletResponse response,Integer coddosificacion){
		Dosificacion obj=new Dosificacion();
		try {
			obj=dosificacionS.obtener(coddosificacion);
		} catch (Exception e) {
			LOGGER.error("obtener"+e.toString());
		}
		return new ResponseEntity<Dosificacion>(obj,HttpStatus.OK);
	}
	@RequestMapping ("getLastData")
	public ResponseEntity<Dosificacion> getLastData(HttpServletRequest request, HttpServletResponse response){
		Dosificacion obj=new Dosificacion();
		try {
			obj=dosificacionS.getLastData();
		} catch (Exception e) {
			LOGGER.error("getLastData: "+e.toString());
		}
		return new ResponseEntity<Dosificacion>(obj,HttpStatus.OK);
	}
	@RequestMapping ("buscarxnit/{id}")
	public ResponseEntity<Map<String, Object>> buscarxnit(HttpServletRequest request, HttpServletResponse response,@PathVariable("id")String nit){
		Map<String, Object> Data=new HashMap<String,Object>();
		try {
			Data.put("data", dosificacionS.buscarClientexnit(nit));
			Data.put("status", true);
		} catch (Exception e) {
			LOGGER.error("buscarxnit "+e.toString());
			Data.put("status", false);
		}
		return new ResponseEntity<Map<String,Object>>(Data, HttpStatus.OK);
	}
	@Autowired
	DataSource dataSource;
	
	@RequestMapping("ver")
	public void ver(HttpServletRequest request,HttpServletResponse response,Integer coddosificacion)throws IOException{
		Map<String, Object> parametros=new HashMap<String, Object>();
		LocalDate localDate=LocalDate.now();
		General g=new General(localDate.getYear());
		String reportUrl="/reportes/dosificacion_ver.jasper";
		parametros.put("empresa", g.getNomgen());
		parametros.put("direccion", g.getDirgen());
		parametros.put("telefono", g.getTelgen());
		parametros.put("sucursal", g.getSucgen());
		parametros.put("cod", coddosificacion);
		parametros.put("fecha", Fechas.obtenerFechaLiteral(LocalDate.now()));
		GeneradorReportes rep=new GeneradorReportes();		
		try{		
			rep.generarReporte(response, getClass().getResource(reportUrl), "pdf", parametros, dataSource.getConnection(), "Dosificacion #"+coddosificacion, "inline");	
		} catch (Exception e) {
			LOGGER.error("ver: "+e.toString());
			e.printStackTrace();
		}
	}
	@RequestMapping("anularFactura")
	public ResponseEntity<Map<String, Object>> anularFactura(HttpServletRequest request,long codven){
		Map<String, Object> Data=new HashMap<String, Object>();
		try {
			Data.put("status", dosificacionS.anularFactura(codven));
		} catch (Exception e) {
			LOGGER.error("anularFactura: "+e.toString());
			Data.put("status", false);
		}
		return new ResponseEntity<Map<String,Object>>(Data, HttpStatus.OK);
	}
	@RequestMapping("obtenerDosificacionActual")
	public ResponseEntity<Map<String, Object>> obtenerDosificacionActual(HttpServletRequest request){
		Map<String, Object> Data=new HashMap<String, Object>();
		try {
			Sucursal sucursal = (Sucursal) request.getSession().getAttribute(Constantes.Sesiones.SUCURSAL);
			if(sucursal != null) {
				Data.put("data", dosificacionS.dosificacionactual(sucursal.getCodsuc()));
				Data.put("ultimo", dosificacionS.dosificacionUltimaDosificacion(sucursal.getCodsuc()));
				Data.put("status", true);
			}else {
				Data.put("status", false);
			}
			
		} catch (Exception e) {
			LOGGER.error("obtenerDosificacionActual: "+e.toString());
			Data.put("status", false);
		}
		return new ResponseEntity<Map<String,Object>>(Data, HttpStatus.OK);
	}
}
