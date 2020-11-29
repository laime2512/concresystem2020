package Controladores;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
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

import Modelos.Compra;
import Modelos.PagoCredito;
import Modelos.Sucursal;
import Modelos.Usuario;
import Modelos.ViewCompra;
import Servicios.CompraS;
import Servicios.ProveedorS;
import Servicios.SessionS;
import Utiles.Fechas;
import Utiles.GeneradorReportes;
import Wrap.CompraProductoWrap;
import pagination.DataTableResults;

@Controller
@RequestMapping("/compra/*")
public class CompraRest {
	@Autowired
	private SessionS sessionS;
	@Autowired
	private CompraS compraS;
	@Autowired
	private ProveedorS proveedorS;
	
	private static final Logger LOGGER=Logger.getLogger("CompraRest");

	@RequestMapping("gestion")
	public String gestion() {
		return "compra/gestion";
	}
	@RequestMapping("listaCompraProducto")
	public @ResponseBody DataTableResults<CompraProductoWrap> listaVentaProducto(HttpServletRequest request,Long codpro) throws IOException {
		return compraS.listarCompraProducto(request,codpro);
	}

	@RequestMapping("lista")
	public @ResponseBody DataTableResults<ViewCompra> lista(HttpServletRequest request) throws IOException {
		return compraS.listar(request);
	}
	@RequestMapping("adicionar")
	public String adicionar(Model model, HttpServletRequest request) {
		Sucursal sucursal = (Sucursal) request.getSession().getAttribute("sucursal");
		model.addAttribute("proveedor", proveedorS.listar());
		model.addAttribute("fecha", Fechas.obtenerFecha("dd/MM/yy"));
		model.addAttribute("num",compraS.generarNumero(sucursal.getCodsuc()));
		return "compra/adicionar";
	}
	@RequestMapping("guardar")
	public @ResponseBody Map<String, Object> guardar(HttpServletRequest request,Compra compra,Long proveedor, Long[] productos, Integer[] cantidades,
			Float[] precios, Float[] totales,String vencimientos[], Float porcentajes[],Float descuentos[],Float impuestos[],
			Boolean devoluciones[], String tipos[],Integer xcredito, Float acuenta, Float saldo) {
		Usuario user = (Usuario) request.getSession().getAttribute("user");
		Sucursal sucursal = (Sucursal) request.getSession().getAttribute("sucursal");
		compra.setCodsuc(sucursal.getCodsuc());
		compra.setCodpro(proveedor);
		compra.setCodusu(user.getCodusu());
		compra.setNum(compraS.generarNumero(sucursal.getCodsuc()));
		compra.setCredito(1==xcredito);
		if(xcredito == 1) {//Compra al credito
			if(compra.getTotal() == acuenta)
				compra.setEstadoCredito(false);//compra completa
			else
				compra.setEstadoCredito(true);//compra pendiente
		}else {
			compra.setEstadoCredito(false);
		}
		Map<String, Object> Data = new HashMap<>();
		if (user != null) {
			try {
				Long codcom=compraS.adicionar(compra,sucursal.getCodsuc(), productos, cantidades, precios, totales, vencimientos,
						porcentajes, descuentos, impuestos, devoluciones, tipos);
				//registrar si es compra al credito
				if(xcredito == 1) {
					if(acuenta > 0) {
						PagoCredito pago = new PagoCredito();
						pago.setCodcom(codcom);
						pago.setCodusu(user.getCodusu());
						pago.setMonto(acuenta);
						pago.setObservacion("Primer pago cuando se compra.");
						pago.setFecha(compra.getFecha());
						compraS.adicionarCredito(pago);
					}
				}
				Data.put("cod", codcom);
				Data.put("status", codcom>0);
			} catch (Exception e) {
				LOGGER.error("error al adicionar compra" + e.toString());
				Data.put("status", false);
			}
		}
		return Data;
	}

	@RequestMapping("obtener")
	public @ResponseBody Map<String, Object> obtener(HttpServletRequest request, Long codcompra) {
		Map<String, Object> Data = new HashMap<String, Object>();
		try {
			ViewCompra compra=compraS.obtener(codcompra);
			Data.put("data", compra);
			Data.put("detalles", compraS.obtenerDetalleCompra(codcompra));
			Data.put("status", true);
		} catch (Exception e) {
			Data.put("status", false);
			LOGGER.error("error al obtener=" + e.toString());
		}
		return Data;
	}

	@RequestMapping("eliminar")
	public @ResponseBody Map<String, Object> eliminar(HttpServletRequest request, Model model, Long codcom)
			throws IOException {
		Usuario us = (Usuario) request.getSession().getAttribute("user");
		Map<String, Object> Data = new HashMap<String, Object>();
		if (us != null) {
			Data.put("status", compraS.eliminar(codcom));
		}else {
			Data.put("status", false);
		}
		return Data;
	}
	@RequestMapping("revisar")
	public @ResponseBody Map<String, Object> revisar(HttpServletRequest request, Model model, Long codcom)
			throws IOException {
		Usuario us = (Usuario) request.getSession().getAttribute("user");
		Map<String, Object> Data = new HashMap<String, Object>();
		if (us != null) {
			Data.put("status", compraS.finalizarCredito(codcom));
		}else {
			Data.put("status", false);
		}
		return Data;
	}

	@Autowired
	DataSource dataSource;

	@RequestMapping("ver")
	public void ver(HttpServletRequest request, HttpServletResponse response, Long codcom) throws IOException, ParseException {
		String nombre = "reporte de compra", tipo = "pdf", estado = "inline";
		Map<String, Object> parametros = new HashMap<String, Object>();
		String url = "/reportes/compra.jasper";
		String SubRep=getClass().getResource("/reportes/dcompra.jasper").toString();
		parametros.put("codcom", codcom);
		parametros.put("path",SubRep);
		GeneradorReportes g = new GeneradorReportes();
		try {
			g.generarReporte(response, getClass().getResource(url), tipo, parametros, dataSource.getConnection(),
					nombre, estado);
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("error al ver:"+ e.toString());
		}
	}

	@RequestMapping("gestion_reporte")
	public String gestion_reporte(HttpServletRequest request, Model model) {
		model.addAttribute("proveedores", proveedorS.listar());
		model.addAttribute("fecha", Fechas.getDateNowToString());
		return "reporte/gestion_compra";
	}
	@RequestMapping("calcularCompraProducto")
	public @ResponseBody Map<String, Object> calcularCompraProducto(HttpServletRequest request, Long codpro,int meses)throws IOException {
		Sucursal sucursal = sessionS.getSucursal(request);
		Map<String, Object> Data = new HashMap<String, Object>();
		if (sucursal != null) {
			Data.put("data", compraS.obtenerCompraProducto(codpro, meses, sucursal.getCodsuc()));
			Data.put("status", true);
		}else {
			Data.put("status", false);
		}
		return Data;
	}
}
