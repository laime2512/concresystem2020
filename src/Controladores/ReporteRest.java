package Controladores;

import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import Modelos.Sucursal;
import Modelos.Tipo;
import Modelos.Usuario;
import Modelos.ViewAlmacen;
import Servicios.AccesoUsuarioS;
import Servicios.AlmacenS;
import Servicios.ClienteImpl;
import Servicios.DosificacionS;
import Servicios.LaboratorioS;
import Servicios.TipoS;
import Utiles.Constantes;
import Utiles.ExcelReportFacturacion;
import Utiles.ExcelReportView;
import Utiles.Fechas;
import Utiles.GeneradorReportes;
import Wrap.FacturaDosificacionWrap;
import pagination.DataTableResults;

@Controller
@RequestMapping("/reporte/*")
public class ReporteRest {

	@Autowired
	DataSource dataSource;
	@Autowired
	private AccesoUsuarioS usuarioS;
	@Autowired
	private AlmacenS almacenS;
	@Autowired
	private ClienteImpl clienteS;
	@Autowired
	private TipoS tipoS;
	@Autowired
	private LaboratorioS laboratorioS;
	@Autowired
	private DosificacionS dosificacionS;
	
	private static final Logger LOGGER = Logger.getLogger("ResporteRest");
	@RequestMapping("searchLaboratory")
	public String searchByLaboratory(Model model) {
		model.addAttribute("laboratorios", laboratorioS.listar());
		return "reporte/search-laboratorio";
	}
	@RequestMapping("responseSearchLaboratory")
	public @ResponseBody DataTableResults<ViewAlmacen> lista(HttpServletRequest request, Integer codlab, String clasification,Integer dias) {
		return almacenS.searchByLaboratoryAndClasification(request, codlab, clasification, dias);
	}
	
	@RequestMapping("gestion")
	public String gestion(Model model, HttpServletRequest request) throws SQLException {
		Sucursal sucursal = (Sucursal)request.getSession().getAttribute(Constantes.Sesiones.SUCURSAL);
		model.addAttribute("usuarios", usuarioS.listar());
		model.addAttribute("clientes", clienteS.listar());
		model.addAttribute("tipos", tipoS.listar_todos());
		model.addAttribute("xsucursal", sucursal.getNombre());
		return "reporte/gestion";
	}
	@RequestMapping("dashboard")
	public String dashboard(Model model, HttpServletRequest request) throws SQLException {
		Sucursal sucursal = (Sucursal)request.getSession().getAttribute(Constantes.Sesiones.SUCURSAL);
		model.addAttribute("sucursal",sucursal.getNombre());
		return "reporte/dashboard";
	}
	@RequestMapping("plantilla1")
	public void plantilla1(HttpServletRequest request,HttpServletResponse response,Integer tipo,String fini,String ffin,
			String display) {
		String nameReport="";
		switch (tipo) {
		case 1:
			nameReport="informe_compra";
			break;
		case 2:
			nameReport="informe_venta";
			break;
		case 3:
			nameReport="informe_producto";
			break;
		case 4:
			nameReport="informe_caja";
			break;
		case 5:
			nameReport="informe_detalle_compra";
			break;
		case 6:
			nameReport="informe_detalle_venta";
			break;
		case 7:
			nameReport="informe_detalle_caja";
			break;
		case 8:
			nameReport="informe_factura";
			break;
		case 9:
			nameReport="informe_pedido";
			break;
		default:
			break;
		}
		if(!nameReport.isEmpty()) {
			String fileReport = nameReport+"_"+fini+"_"+ffin;
			String typeReport ="pdf";
			String reportUrl=Constantes.DIR_REPORTES+nameReport+".jasper";
			Map<String, Object> parametros = new HashMap<String, Object>();
			Date fechaIni=Fechas.convertirStringToDate(fini);
			Date fechaFin=Fechas.convertirStringToDate(ffin);
			parametros.put("fini", fechaIni);
			parametros.put("ffin", fechaFin);
			obtenerEncabezado(request, parametros);
			GeneradorReportes g = new GeneradorReportes();
			try {
				g.generarReporte(response, getClass().getResource(reportUrl),typeReport, parametros, dataSource.getConnection(),
						fileReport, display);
			} catch (Exception e) {
				e.printStackTrace();
				LOGGER.error("plantilla1 : "+e.toString());
			}
		}
	}
	@RequestMapping("plantilla2")
	public void plantilla2(HttpServletRequest request,HttpServletResponse response,Integer tipo,String fini,String ffin,
			String display,Long user) throws SQLException {
		String nameReport="";
		switch (tipo) {
		case 1:
			nameReport="informe_compra_x_usuario";
			break;
		case 2:
			nameReport="informe_detalle_compra_x_usuario";
			break;
		case 3:
			nameReport="informe_venta_x_usuario";
			break;
		case 4:
			nameReport="informe_detalle_venta_x_usuario";
			break;
		case 5:
			nameReport="informe_caja_x_usuario";
			break;
		case 6:
			nameReport="informe_detalle_caja_x_usuario";
			break;
		default:
			break;
		}
		if(!nameReport.isEmpty()) {
			Usuario usuario = usuarioS.obtenerUser(user);
			if(usuario!=null) {
				String fileReport = nameReport+"_"+fini+"_"+ffin;
				String typeReport ="pdf";
				String reportUrl=Constantes.DIR_REPORTES+nameReport+".jasper";
				Map<String, Object> parametros = new HashMap<String, Object>();
				Date fechaIni=Fechas.convertirStringToDate(fini);
				Date fechaFin=Fechas.convertirStringToDate(ffin);
				parametros.put("fini", fechaIni);
				parametros.put("ffin", fechaFin);
				parametros.put("refUsuario", usuario.getNombre() +" "+usuario.getAp());
				parametros.put("refIdUsuario", user);
				obtenerEncabezado(request, parametros);
				GeneradorReportes g = new GeneradorReportes();
				try {
					g.generarReporte(response, getClass().getResource(reportUrl),typeReport, parametros, dataSource.getConnection(),
							fileReport, display);
				} catch (Exception e) {
					e.printStackTrace();
					LOGGER.error("plantilla2: "+e.toString());
				}
			}
		}
	}
	@RequestMapping("plantilla3")
	public void plantilla3(HttpServletRequest request,HttpServletResponse response,Integer tipo,
			String display,String fini,String ffin,Integer limite) throws SQLException {
		String nameReport="";
		switch (tipo) {
		case 1:
			nameReport="producto_mas_vendido";
			break;
		case 3:
			nameReport="compra_mayores";
			break;
		case 2:
			nameReport="venta_mayores";
			break;
		default:
			break;
		}
		if(!nameReport.isEmpty()) {
			Sucursal sucursal=(Sucursal)request.getSession().getAttribute("sucursal");
			if(sucursal!=null) {
				String fileReport = nameReport+"_"+fini+"_"+ffin;
				String typeReport ="pdf";
				String reportUrl=Constantes.DIR_REPORTES+nameReport+".jasper";
				Map<String, Object> parametros = new HashMap<String, Object>();
				Date fechaIni=Fechas.convertirStringToDate(fini);
				Date fechaFin=Fechas.convertirStringToDate(ffin);
				parametros.put("fini", fechaIni);
				parametros.put("ffin", fechaFin);
				parametros.put("xsucursal", sucursal.getNombre());
				parametros.put("codsuc", sucursal.getCodsuc());
				obtenerEncabezado(request, parametros);
				parametros.put("limite", limite);
				GeneradorReportes g = new GeneradorReportes();
				try {
					g.generarReporte(response, getClass().getResource(reportUrl),typeReport, parametros, dataSource.getConnection(),
							fileReport, display);
				} catch (Exception e) {
					e.printStackTrace();
					LOGGER.error("plantilla3"+e.toString());
				}
			}
		}
	}
	
	@RequestMapping("plantilla4")
	public String plantilla4(HttpServletRequest request,HttpServletResponse response,Integer categoria,
			String display,Long cod) throws SQLException {
		if(categoria==1)
			return "redirect:compra/ver?codcom="+cod;
		if(categoria==2)
			return "redirect:venta/ver?codven="+cod;
		if(categoria==3)
			return "redirect:caja/ver?codcaja="+cod;
		if(categoria==4)
			return "redirect:producto/ver?codpro="+cod;
		if(categoria==5)
			return "redirect:venta/imprimirfactura?codven="+cod;
		return "";
		
	}
	@RequestMapping("plantilla5")
	public void plantilla5(HttpServletRequest request,HttpServletResponse response,Integer tipo,
			String display) throws SQLException {
		String nameReport="";
		switch (tipo) {
		case 1:
			nameReport="informe_almacen";
			break;
		case 2:
			nameReport="informe_almacen_incompleto";
			break;
		case 3:
			nameReport="informe_producto_repetido";
			break;
		case 4:
			nameReport="informe_";
			break;
		default:
			break;
		}
		if(tipo==4) {
//			List<AlmacenXls> almacenList=almacenS.obtenerListaAlmacenXls(1);
//			new ModelAndView(new ExcelReportView(),"almacenList",almacenList);
			
		}else {
			if(!nameReport.isEmpty()) {
				Sucursal sucursal=(Sucursal)request.getSession().getAttribute("sucursal");
				if(sucursal!=null) {
					String fileReport = nameReport;
					String typeReport ="pdf";
					String reportUrl=Constantes.DIR_REPORTES+nameReport+".jasper";
					Map<String, Object> parametros = new HashMap<String, Object>();
					parametros.put("xsucursal", sucursal.getNombre());
					parametros.put("codsuc", sucursal.getCodsuc());
					obtenerEncabezado(request, parametros);
					GeneradorReportes g = new GeneradorReportes();
					try {
						g.generarReporte(response, getClass().getResource(reportUrl),typeReport, parametros, dataSource.getConnection(),
								fileReport, display);
					} catch (Exception e) {
						e.printStackTrace();
						LOGGER.error("plantilla5"+e.toString());
					}
				}
			}
		}
	}
	@RequestMapping("plantilla6")
	public void plantilla6(HttpServletRequest request,HttpServletResponse response,String mesInicio,String mesFin,
			String display) throws SQLException {
		String nameReport = "valuacionInventario";
		Sucursal sucursal=(Sucursal)request.getSession().getAttribute("sucursal");
		Integer gestion = Constantes.Sesiones.GESTION;
		if(sucursal!=null) {
			String fileReport = nameReport+"_"+mesInicio+"-"+mesFin+"_"+gestion;
			String typeReport ="pdf";
			String reportUrl=Constantes.DIR_REPORTES+nameReport+".jasper";
			Map<String, Object> parametros = new HashMap<String, Object>();
			Date fechaIni=Fechas.convertirStringToDate("01/"+mesInicio+"/"+gestion);
			Date fechaFin=Fechas.convertirStringToDate("30/"+mesFin+"/"+gestion);
			parametros.put("fini", fechaIni);
			parametros.put("ffin", fechaFin);
			parametros.put("xsucursal", sucursal.getNombre());
			parametros.put("codsuc", sucursal.getCodsuc());
			obtenerEncabezado(request, parametros);
			GeneradorReportes g = new GeneradorReportes();
			try {
				g.generarReporte(response, getClass().getResource(reportUrl),typeReport, parametros, dataSource.getConnection(),
						fileReport, display);
			} catch (Exception e) {
				e.printStackTrace();
				LOGGER.error("plantilla6: "+e.toString());
			}
		}
	}
	@RequestMapping("plantilla7")
	public void plantilla7(HttpServletRequest request,HttpServletResponse response,Integer estado,String fini,String ffin) throws SQLException {
		String nameReport="informe_pedido_estado";
		String display ="inline";
		
		if(!nameReport.isEmpty()) {
			Sucursal sucursal=(Sucursal)request.getSession().getAttribute("sucursal");
			if(sucursal!=null) {
				String fileReport = nameReport+"_"+fini+"_"+ffin;
				String typeReport ="pdf";
				String reportUrl=Constantes.DIR_REPORTES+nameReport+".jasper";
				Map<String, Object> parametros = new HashMap<String, Object>();
				Date fechaIni=Fechas.convertirStringToDate(fini);
				Date fechaFin=Fechas.convertirStringToDate(ffin);
				parametros.put("fini", fechaIni);
				parametros.put("ffin", fechaFin);
				parametros.put("xsucursal", sucursal.getNombre());
				parametros.put("codsuc", sucursal.getCodsuc());
				obtenerEncabezado(request, parametros);
				parametros.put("estado", estado);
				parametros.put("xestado", Constantes.Pedidos.ESTADO[estado]);
				GeneradorReportes g = new GeneradorReportes();
				try {
					g.generarReporte(response, getClass().getResource(reportUrl),typeReport, parametros, dataSource.getConnection(),
							fileReport, display);
				} catch (Exception e) {
					e.printStackTrace();
					LOGGER.error("plantilla7: "+e.toString());
				}
			}
		}
	}
	@RequestMapping("plantilla8")
	public void plantilla8(HttpServletRequest request,HttpServletResponse response,Long codcli,String fini,String ffin) throws SQLException {
		String nameReport="informe_pedido_cliente";
		String display ="inline";
		
		if(!nameReport.isEmpty()) {
			Sucursal sucursal=(Sucursal)request.getSession().getAttribute("sucursal");
			if(sucursal!=null) {
				String fileReport = nameReport+"_"+fini+"_"+ffin;
				String typeReport ="pdf";
				String reportUrl=Constantes.DIR_REPORTES+nameReport+".jasper";
				Map<String, Object> parametros = new HashMap<String, Object>();
				Date fechaIni=Fechas.convertirStringToDate(fini);
				Date fechaFin=Fechas.convertirStringToDate(ffin);
				parametros.put("fini", fechaIni);
				parametros.put("ffin", fechaFin);
				parametros.put("xsucursal", sucursal.getNombre());
				parametros.put("codsuc", sucursal.getCodsuc());
				obtenerEncabezado(request, parametros);
				parametros.put("cliente", codcli);
				Usuario cliente = usuarioS.obtenerUser(codcli);
				parametros.put("xcliente", cliente.getNombre()+" "+cliente.getAp());
				GeneradorReportes g = new GeneradorReportes();
				try {
					g.generarReporte(response, getClass().getResource(reportUrl),typeReport, parametros, dataSource.getConnection(),
							fileReport, display);
				} catch (Exception e) {
					e.printStackTrace();
					LOGGER.error("plantilla8: "+e.toString());
				}
			}
		}
	}
	@RequestMapping("plantilla9")
	public void plantilla9(HttpServletRequest request,HttpServletResponse response,Integer codtip) throws SQLException {
		String nameReport="informe_almacen_categoria";
		String display ="inline";
		
		if(!nameReport.isEmpty()) {
			Sucursal sucursal=(Sucursal)request.getSession().getAttribute("sucursal");
			if(sucursal!=null) {
				String fileReport = nameReport+"_"+codtip;
				String typeReport ="pdf";
				String reportUrl=Constantes.DIR_REPORTES+nameReport+".jasper";
				Map<String, Object> parametros = new HashMap<String, Object>();
				parametros.put("xsucursal", sucursal.getNombre());
				parametros.put("codsuc", sucursal.getCodsuc());
				obtenerEncabezado(request, parametros);
				parametros.put("categoria", codtip);
				Tipo tipo = tipoS.obtener(codtip);
				parametros.put("xcategoria", tipo.getNomtip());
				GeneradorReportes g = new GeneradorReportes();
				try {
					g.generarReporte(response, getClass().getResource(reportUrl),typeReport, parametros, dataSource.getConnection(),
							fileReport, display);
				} catch (Exception e) {
					e.printStackTrace();
					LOGGER.error("plantilla9: "+e.toString());
				}
			}
		}
	}
	@RequestMapping("plantilla10")
	public void plantilla10(HttpServletRequest request,HttpServletResponse response,String mesInicio,String mesFin,
			String display) throws SQLException {
		String nameReport = "informe_producto_vencido";
		Sucursal sucursal=(Sucursal)request.getSession().getAttribute("sucursal");
		Integer gestion = Constantes.Sesiones.GESTION;
		if(sucursal!=null) {
			String fileReport = nameReport+"_"+mesInicio+"-"+mesFin+"_"+gestion;
			String typeReport ="pdf";
			String reportUrl=Constantes.DIR_REPORTES+nameReport+".jasper";
			Map<String, Object> parametros = new HashMap<String, Object>();
			Date fechaIni=Fechas.convertirStringToDate("01/"+mesInicio+"/"+gestion);
			Date fechaFin=Fechas.convertirStringToDate("30/"+mesFin+"/"+gestion);
			parametros.put("fini", fechaIni);
			parametros.put("ffin", fechaFin);
			parametros.put("xsucursal", sucursal.getNombre());
			parametros.put("codsuc", sucursal.getCodsuc());
			obtenerEncabezado(request, parametros);
			GeneradorReportes g = new GeneradorReportes();
			try {
				g.generarReporte(response, getClass().getResource(reportUrl),typeReport, parametros, dataSource.getConnection(),
						fileReport, display);
			} catch (Exception e) {
				e.printStackTrace();
				LOGGER.error("plantilla10: "+e.toString());
			}
		}
	}
	@RequestMapping("reportExcel")
	public ModelAndView reportarExcel(HttpServletRequest request) {
		Sucursal sucursal = (Sucursal)request.getSession().getAttribute(Constantes.Sesiones.SUCURSAL);
		List<ViewAlmacen> almacenList=almacenS.listViewAlmacen(sucursal.getCodsuc());
		ModelAndView model = new ModelAndView();
		model.setView(new ExcelReportView());
		model.addObject("almacenList", almacenList);
		model.addObject("company", Constantes.COMPANY);
		model.addObject("branch", sucursal.getNombre());
		model.addObject("dateReport", Fechas.obtenerFechaLiteralActual());
		model.addObject("title", "Inventario de productos");
		return model;
	}
	@RequestMapping("reportStockCero")
	public ModelAndView reportStockCero(HttpServletRequest request) {
		Sucursal sucursal = (Sucursal)request.getSession().getAttribute(Constantes.Sesiones.SUCURSAL);
		List<ViewAlmacen> almacenList=almacenS.listarProductosStockCero(sucursal.getCodsuc());
		ModelAndView model = new ModelAndView();
		model.setView(new ExcelReportView());
		model.addObject("almacenList", almacenList);
		model.addObject("company", Constantes.COMPANY);
		model.addObject("branch", sucursal.getNombre());
		model.addObject("dateReport", Fechas.obtenerFechaLiteralActual());
		model.addObject("title", "Producto con stock Cero (0)");
		return model;
	}
	@RequestMapping("reportFactura")
	public ModelAndView reportFacturas(HttpServletRequest request,HttpServletResponse response,String fini,String ffin,
			String display) throws SQLException {
			Sucursal sucursal = (Sucursal)request.getSession().getAttribute(Constantes.Sesiones.SUCURSAL);
			List<FacturaDosificacionWrap> facturaDosificacionList = dosificacionS.listFacturaDosificacion(fini, ffin, sucursal.getCodsuc());
			ModelAndView model = new ModelAndView();
			model.setView(new ExcelReportFacturacion());
			model.addObject("itemList", facturaDosificacionList);
			model.addObject("title", "Facturacion_"+fini+"_"+ffin+"_"+sucursal.getNombre());
			return model;
	}
	public void obtenerEncabezado(HttpServletRequest request,Map<String, Object> param) {
		Usuario usuario=(Usuario)request.getSession().getAttribute("user");
		if(usuario!=null) {
			Sucursal sucursal=(Sucursal)request.getSession().getAttribute("sucursal");
			param.put("empresa","MIA SUPER");
			param.put("usuario",usuario.getNombre()+" "+usuario.getAp()+(usuario.getAm()!=null?"":(" "+usuario.getAm())));
			if(sucursal!=null) {
				param.put("sucursal", sucursal.getNombre());
				param.put("telefono", sucursal.getTelefono());
				param.put("direccion", sucursal.getDireccion());
			}
		}
	}
}
