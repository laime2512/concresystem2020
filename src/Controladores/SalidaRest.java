package Controladores;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import Modelos.Salida;
import Modelos.Sucursal;
import Modelos.Usuario;
import Modelos.ViewDetalleSalida;
import Servicios.SalidaS;
import Servicios.SessionS;
import Servicios.SucursalS;
import Utiles.Constantes;
import Utiles.Fechas;
import Utiles.Archivos.Util;
import Wrap.SalidaProductoWrap;
import enumeraciones.SalidaE;
import pagination.DataTableResults;

@Controller
@RequestMapping("/salida/*")
public class SalidaRest {
	
	@Autowired
	private SalidaS salidaS;
	@Autowired
	private SucursalS sucursalS;
	@Autowired
	private SessionS sessionS;
	
	private static final Logger LOGGER = Logger.getLogger("SalidaRest");
	/**
	 * Subir archivo de salida de otra sucursal
	 * @param request
	 * @param archivoJson
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("subirArchivo")
	public @ResponseBody Map<String, Object> subirArchivo(HttpServletRequest request, MultipartFile archivoJson)
			throws Exception {
		Usuario user = sessionS.getUser(request);
		Map<String, Object> Data = new HashMap<>();
		if (user != null) {
			if (archivoJson != null && archivoJson.getSize() > 0) {
				Gson gson = new GsonBuilder().setPrettyPrinting().create();
				Reader  reader = new InputStreamReader(archivoJson.getInputStream());
				Type type = new TypeToken<Salida>() {}.getType();
		        
				Salida result = gson.fromJson(reader, type);
				reader.close();
				Data.put("status", true);
				Data.put("salida", result);
			}
		} else {
			LOGGER.error("sesion cerrada.");
		}
		return Data;
	}
	/**
	 * Visualiza la gestion de salidas y entradas
	 * @return
	 */
	@RequestMapping("gestion")
	public String gestion(){
		return "salida/gestion";
	}
	/**
	 * Visualiza la pantalla de adicionar salida
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("adicionar")
	public String adicionar(HttpServletRequest request,Model model){
		model.addAttribute("fecha",Fechas.obtenerFecha("dd/MM/yy"));
		model.addAttribute("sucursales",sucursalS.listar());
		model.addAttribute("sucursal",sessionS.getSucursal(request));
		return "salida/adicionar";
	}
	/**
	 * Visualiza la pantalla de revision
	 * @param request
	 * @param model
	 * @param codsal
	 * @return
	 * @throws SQLException
	 */
	@RequestMapping("revisar")
	public String revisar(HttpServletRequest request,Model model,Long codsal) throws SQLException{
		Salida salida = salidaS.obtener(codsal);
		model.addAttribute("salida",salida);
		model.addAttribute("fecha_literal",Fechas.obtenerFechaLiteral(salida.getXfsalida(),"dd/MM/yy"));
		model.addAttribute("esTraspaso", salidaS.esTraspasoFarmacias(salida.getTipo()));
		return "salida/revisar";
	}
	/**
	 * Retorna la lista de salidas con filtro
	 * @param request
	 * @param draw
	 * @param length
	 * @param start
	 * @param estado
	 * @param tipo
	 * @return
	 * @throws IOException
	 * @throws SQLException
	 */
	@RequestMapping("lista")
	public @ResponseBody DataTableResults<Salida> lista(HttpServletRequest request,int tipo)throws IOException, SQLException{
		return salidaS.filter(request, tipo);
	}
	/**
	 * Descarga el archivo generado de salida
	 * @param request
	 * @param response
	 * @param nameArchive
	 */
	@RequestMapping("descarga")
	public void descarga(HttpServletRequest request,HttpServletResponse response,String nameArchive){
		Usuario user = sessionS.getUser(request);
		if(user!=null){
			try {				
				//Generacion del archivo y guardar en ruta del servidor
				String jsonFilePath = Util.getFilePath(request) + File.separator+ nameArchive;
				File downloadFile = new File(jsonFilePath);
				if(downloadFile.exists()) {
					Util.downloadFileProperties(request, response, jsonFilePath, downloadFile);
				} else {
					LOGGER.info("No se encontro el archivo");
				}
			} catch (Exception e) {
				LOGGER.error("descarga: "+e.toString());
			}
		}
	}
	/**
	 * Guarda una salida
	 * @param request
	 * @param response
	 * @param obj
	 * @param detalles
	 * @return
	 */
	@RequestMapping("guardar")
	public @ResponseBody Map<String, Object> guardar(HttpServletRequest request,HttpServletResponse response,@RequestBody Salida obj){
		Usuario user= sessionS.getUser(request);
		Sucursal sucursal=sessionS.getSucursal(request);
		Map<String,Object> Data=new HashMap<>();
		if(user!=null && sucursal != null){
			try {
				obj.setCodusu(user.getCodusu());
				obj.setSucOrigen(sucursal.getCodsuc());
				salidaS.adicionar(obj);
				//Generacion del archivo y guardar en ruta del servidor
//				if(obj.getTipo() == SalidaE.TRASPASO_ENTRE_FARMACIA_EGRESO.getTipo()) {
//					String ruta = request.getSession().getServletContext().getRealPath(Constantes.Archivo.RUTA_SALIDA);
//					String nameArchive = Utils.generateNameArchiveSalida(obj.getSucOrigen(), obj.getCodsal());
//					UtilJson.saveFileJson(ruta, nameArchive, obj);
//					Data.put("archive", nameArchive);
//					Data.put("existArchive", true);
//				}else
//					Data.put("existArchive", false);
				Data.put("status", obj.getCodsal() > 0);
			} catch (Exception e) {
				LOGGER.error("guardar: "+e.toString());
				Data.put("status", false);
			}
		}
		return Data;
	}

	/**
	 * Actualiza el estado del almacen
	 * @param request
	 * @param almacenSelected
	 * @param productSelected
	 * @param cantidadEntrada
	 * @return
	 */
	@RequestMapping("actualizarAlmacen")
	public @ResponseBody Map<String, Object> actualizarAlmacen(HttpServletRequest request,ViewDetalleSalida detalle){
		Usuario user=(Usuario)request.getSession().getAttribute(Constantes.Sesiones.USER);
		Sucursal sucursal=(Sucursal)request.getSession().getAttribute(Constantes.Sesiones.SUCURSAL);
		Map<String,Object> Data=new HashMap<>();
		if(user!=null && sucursal != null){
			try {
				Salida obj = new Salida();
				obj.setCodusu(user.getCodusu());
				obj.setSucOrigen(sucursal.getCodsuc());
				obj.setDescripcion("Actualizacion de almacen por "+user.getAlias());
				obj.setFsalida(Fechas.getDateNowToString("dd/MM/yy"));
				if(detalle.getInOut()) {//Se aumento la cantidad existente
					obj.setTipo(SalidaE.AUMENTO_ALMACEN_POR_USUARIO.getTipo());
				}else {
					obj.setTipo(SalidaE.DISMINUCION_ALMACEN_POR_USUARIO.getTipo());
				}
				obj.addDetalleSalidaList(detalle);
				salidaS.adicionar(obj);
				Data.put("status", obj.getCodsal() > 0);
			} catch (Exception e) {
				LOGGER.error("actualizarAlmacen: "+e.toString());
				Data.put("status", false);
			}
		}
		return Data;
	}
	/**
	 * Guarda una revision de entrada o salida
	 * @param request
	 * @param obj
	 * @return
	 */
	@RequestMapping("guardarRevision")
	public @ResponseBody Map<String, Object> guardarRevision(HttpServletRequest request,Salida obj,
			String vencimientos[],String ingresos[],Integer cantidades[],Long codalmacenes[],Long codlugares[],Long productos[]){
		Usuario user=(Usuario)request.getSession().getAttribute(Constantes.Sesiones.USER);
		Map<String,Object> Data=new HashMap<>();
		if(user!=null){
			try {
				Salida encontrado = salidaS.obtener(obj.getCodsal());
				encontrado.setConclusion(obj.getConclusion());
				encontrado.setSolucion(obj.getSolucion());
				encontrado.setCodusu(user.getCodusu());
				encontrado.setMonto(obj.getMonto());
				encontrado.setEstado(obj.getEstado());
				int solucion = obj.getSolucion();
				if(solucion != 4 && solucion != 5 && solucion != 6) {//Soluciones sin necesidad de crear detalles
					List<ViewDetalleSalida> listDetail = new ArrayList<ViewDetalleSalida>();
					if(solucion == 2) {
						for (int i = 0; i < productos.length; i++) {
							ViewDetalleSalida det = new ViewDetalleSalida();
							det.setCodpro(productos[i]);
							det.setCantidad(cantidades[i]);
							det.setXfingreso(ingresos[i]);
							det.setXfvencimiento(vencimientos[i]);
							listDetail.add(det);
						}
					}else {
						if(codalmacenes!=null && codalmacenes.length > 0) {
							for (int i = 0; i < codalmacenes.length; i++) {
								ViewDetalleSalida det = new ViewDetalleSalida();
								det.setCodsal(obj.getCodsal());
								det.setCodalmacen(codalmacenes[i]);
								det.setCodlugar(codlugares[i]);
								det.setXfingreso(ingresos[i]);
								det.setXfvencimiento(vencimientos[i]);
								det.setCantidad(cantidades[i]);
								listDetail.add(det);
							}
							encontrado.setDetalleSalidaList(listDetail);
						}
					}
				}
				
				Data.put("status", salidaS.adicionarRevision(encontrado));
			} catch (Exception e) {
				LOGGER.error("guardarRevision: "+e.toString());
				Data.put("status", false);
			}
		}
		return Data;
	}
	/**
	 * Elimina una salida
	 * @param request
	 * @param obj
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("eliminar")
	public @ResponseBody Map<String, Object> eliminar(HttpServletRequest request,Salida obj)throws IOException{
		return salidaS.eliminar(obj);
	}
	@RequestMapping("listarDetalle")
	public @ResponseBody Map<String, Object> listarDetalle(HttpServletRequest request,Long codsal)throws IOException{
		Map<String,Object> Data=new HashMap<>();
		try {
			Data.put("detalleSalidaList", salidaS.obtenerDetalles(codsal));
			Data.put("status",true);
		} catch (Exception e) {
			Data.put("status",false);
		}
		return Data;
	}
	@GetMapping("listaSalidaProducto")
	public @ResponseBody DataTableResults<SalidaProductoWrap> listaSalidaProducto(HttpServletRequest request,Long codpro) throws IOException, SQLException {
		return salidaS.listarSalidaProducto(request, codpro);
	}
}
