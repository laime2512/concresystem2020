package Controladores;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import Modelos.Caja;
import Modelos.DetallePedido;
import Modelos.Dosificacion;
import Modelos.Factura;
import Modelos.Pedido;
import Modelos.Rol;
import Modelos.Sucursal;
import Modelos.Usuario;
import Modelos.Venta;
import Modelos.ViewVenta;
import Servicios.AccesoUsuarioS;
import Servicios.CajaS;
import Servicios.ClienteImpl;
import Servicios.DosificacionS;
import Servicios.PedidoS;
import Servicios.SessionS;
import Servicios.SucursalS;
import Servicios.VentaS;
import Utiles.Constantes;
import Utiles.Fechas;
import Utiles.GeneradorReportes;
import Utiles.Numeros;
import Utiles.Facturacion.ControlCode;
import Wrap.VentaProductoWrap;
import pagination.DataTableResults;

@Controller
@RequestMapping("/venta/*")
public class VentaRest {

	@Autowired
	private VentaS ventaS;
	@Autowired
	private ClienteImpl clienteS;
	@Autowired
	private AccesoUsuarioS usuarioS;
	@Autowired
	private DosificacionS dosificacionS;
	@Autowired
	private CajaS cajaS;
	@Autowired
	private PedidoS pedidoS;
	@Autowired
	private SessionS sessionS;
	@Autowired
	private SucursalS sucursalS;
	private static final Logger LOGGER = Logger.getLogger("VentaRest");

	@RequestMapping("gestion")
	public String gestion(HttpServletRequest request, Model model) {
		Usuario user = sessionS.getUser(request);
		Sucursal sucursal = sessionS.getSucursal(request);
		Rol rol = sessionS.getRol(request);
		Caja caja = cajaS.verificarUsuarioActivo(user.getCodusu(), sucursal.getCodsuc());
		if (caja == null) {
			return "venta/blanco";
		} else {
			model.addAttribute("rol", rol);
			model.addAttribute("clientes", clienteS.listar());
			model.addAttribute("fecha", Fechas.obtenerFecha("dd/MM/yy"));
			return "venta/gestion";
		}
	}

	@RequestMapping("gestion_x_cliente")
	public String gestion_x_cliente(HttpServletRequest request, Model model) {
		return "venta/gestion_cliente";
	}
	@RequestMapping("gestion_x_usuario")
	public String gestion_x_usuario(HttpServletRequest request, Model model) {
		return "venta/gestion_usuario";
	}
	@RequestMapping("lista")
	public @ResponseBody DataTableResults<ViewVenta> lista(HttpServletRequest request) throws IOException {
		Usuario user = sessionS.getUser(request);
		Sucursal sucursal = sessionS.getSucursal(request);
		Rol rol = sessionS.getRol(request);
		Caja caja = cajaS.verificarUsuarioActivo(user.getCodusu(), sucursal.getCodsuc());
		if (rol.isAcceso_venta()) {
			return ventaS.listar(request);
		} else {
			return ventaS.listarPorCaja(request, caja);
		}
	}
	@RequestMapping("listaVentaProducto")
	public @ResponseBody DataTableResults<VentaProductoWrap> listaVentaProducto(HttpServletRequest request,Long codpro) throws IOException {
		return ventaS.listarVentaProducto(request,codpro);
	}

	@RequestMapping("guardar")
	public @ResponseBody Map<String, Object> guardar(HttpServletRequest request, Venta v, Long productos[],
			Float cantidades[], Float precios[], Float totales[], Float total, String clienteNit,
			String nit_fac, String con_factura, String tipoVenta[], Long desc[]) {
		Usuario user = sessionS.getUser(request);
		Sucursal sucursal = sessionS.getSucursal(request);
		Map<String, Object> Data = new HashMap<>();
		if (user != null && sucursal != null) {
			Caja caja = cajaS.verificarUsuarioActivo(user.getCodusu(), sucursal.getCodsuc());
			if (caja != null && sucursal !=null) {
				try {
					if(v.getCodcli()==null)
						v.setCodcli(0L);
					v.setCodsuc(sucursal.getCodsuc());
					v.setCodusu(user.getCodusu());
					v.setCodcaja(caja.getCodcaja());
					v.setFecha(Fechas.getDateNowToString("dd/MM/yy"));
					Long codv = ventaS.adicionar(v,sucursal.getCodsuc(), productos, cantidades, precios, totales, tipoVenta, desc);
					// Si recuperamos el codigo de venta, realizamos la factura
					if (codv > 0) {
						if (con_factura != null) {
							Factura fac = new Factura();
							fac.setCodven(codv);
							if (clienteNit != null)
								fac.setClienteNit(clienteNit);
							else
								fac.setClienteNit("SIN NOMBRE");
							if (nit_fac != null)
								fac.setNitfac(nit_fac);
							else
								fac.setNitfac("0");
							fac.setTotal(v.getTotal());
							Dosificacion d = dosificacionS.dosificacionactual(sucursal.getCodsuc());
							fac.setCoddosificacion(d.getCoddosificacion());
							fac.setCfiscal(0f);
							fac.setDescuento(0f);
							fac.setNumfac(dosificacionS.obtenerNumFac(d.getCoddosificacion()));
							ControlCode control = new ControlCode();
							String codcontrol = control.generate(d.getNumaut(), fac.getNumfac().toString(),
									fac.getNitfac(),Fechas.getDateNowToString("yyyyMMdd"), fac.getTotal().toString(), d.getLlave());
							fac.setCodcontrol(codcontrol);
							dosificacionS.adicionarFactura(fac);
						}
					}

					Data.put("status", codv > 0);
					Data.put("codven", codv);
				} catch (Exception e) {
					LOGGER.error("getMessage=" + e.getMessage());
					Data.put("status", false);
					e.printStackTrace();
				}
			} else
				Data.put("status", false);
		}
		return Data;
	}

	@RequestMapping("guardarc")
	public @ResponseBody Map<String, Object> guardarc(HttpServletRequest request) {
		Map<String, Object> Data = new HashMap<String, Object>();
		Usuario user = sessionS.getUser(request);
		Sucursal sucursal = sessionS.getSucursal(request);
		if (user != null && sucursal != null) {
			try {
				@SuppressWarnings("unchecked")
				List<DetallePedido> detallePedidoList =  (List<DetallePedido>) request.getSession().getAttribute(Constantes.Sesiones.DETALLE);
				Pedido pedido = (Pedido)request.getSession().getAttribute(Constantes.Sesiones.PEDIDO);
				pedido.setCodsuc(sucursal.getCodsuc());
				Long codPed = pedidoS.adicionar(pedido, detallePedidoList);
				Data.put("cantidad", "");
				Data.put("status", true);
				Data.put("codped", codPed);
			} catch (Exception e) {
				LOGGER.error("guardarc: " + e.toString());
				Data.put("status", false);
			}
		}
		return Data;
	}

	@RequestMapping("obtener")
	public @ResponseBody Map<String, Object> obtener(HttpServletRequest request, Long codven) {
		Map<String, Object> Data = new HashMap<String, Object>();
		try {
			ViewVenta dato = ventaS.obtener(codven);
			Data.put("data", dato);
			Data.put("fecha", Fechas.getDateNowToString());
			Data.put("status", true);
		} catch (Exception e) {
			Data.put("status", false);
			LOGGER.error("obtener=" + e.toString());
		}
		return Data;
	}

	@RequestMapping("obtenerDetalle")
	public @ResponseBody Map<String, Object> obtenerVenta(HttpServletRequest request, Long codven) {
		Map<String, Object> Data = new HashMap<String, Object>();
		try {
			Data.put("data", ventaS.obtenerDetalles(codven));
			Data.put("status", true);
		} catch (Exception e) {
			Data.put("status", false);
			LOGGER.error("obtenerVenta=" + e.toString());
		}
		return Data;
	}

	@RequestMapping("eliminar")
	public @ResponseBody Map<String, Object> eliminar(HttpServletRequest request, Model model, Long codven)
			throws IOException {
		Map<String, Object> Data = new HashMap<String, Object>();
		Usuario us = sessionS.getUser(request);
		Sucursal sucursal = sessionS.getSucursal(request);
		Caja caja = cajaS.verificarUsuarioActivo(us.getCodusu(), sucursal.getCodsuc());
		if (us != null & caja != null) {
			Data.put("status", ventaS.eliminar(codven, caja.getCodcaja()));
		}
		return Data;
	}

	@Autowired
	private DataSource dataSource;

	@RequestMapping("ver")
	public void ver(HttpServletRequest request, HttpServletResponse response, Long codven)
			throws IOException {
		String nombre = "Venta " + codven, tipo = "pdf", estado = "inline";
		Map<String, Object> parametros = new HashMap<String, Object>();
		String url = "/reportes/ventav.jasper";
		ViewVenta venta = ventaS.obtener(codven);

		parametros.put("fecha", venta.getXfecha());
		parametros.put("codven", codven);
		parametros.put("total", venta.getTotal());
		parametros.put("usuario", venta.getXusuario());
		parametros.put("cliente", venta.getXcliente());
		GeneradorReportes g = new GeneradorReportes();
		try {
			g.generarReporte(response, getClass().getResource(url), tipo, parametros, dataSource.getConnection(),
					nombre, estado);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@RequestMapping("gestion_reporte")
	public String gestion_reporte(HttpServletRequest request, Model model) throws SQLException {
		model.addAttribute("usuarios", usuarioS.listar());
		model.addAttribute("fecha", Fechas.obtenerFecha("dd/MM/yy"));
		return "reporte/gestion_venta";
	}

	@RequestMapping("imprimir")
	public void imprimir(HttpServletRequest request, HttpServletResponse response, int codven) throws IOException {
		Usuario user = sessionS.getUser(request);
		Map<String, Object> parametros = new HashMap<String, Object>();
		String reportUrl = "/reportes/ventav.jasper";
		String SubRep1 = getClass().getResource("/reportes/ventav.jasper").toString();
		parametros.put("subrep", SubRep1.substring(0, SubRep1.lastIndexOf("/")) + "/");
		parametros.put("usuario", user.getNombre() + " " + user.getAp());
		parametros.put("codven", codven);
		GeneradorReportes rep = new GeneradorReportes();
		try {
			rep.generarReporte(response, getClass().getResource(reportUrl), "pdf", parametros,
					dataSource.getConnection(), "Lista de ventas", "inline");
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("imprimir: " + e.toString());
		}
	}

	@RequestMapping("imprimirc")
	public void imprimirc(HttpServletRequest request, HttpServletResponse response, int codped) throws IOException {
		Map<String, Object> parametros = new HashMap<String, Object>();
		String reportUrl = "/reportes/venta.jasper";
		Usuario user = (Usuario) request.getSession().getAttribute("user");
		parametros.put("usuario", user.getNombre() + " " + user.getAp());
		parametros.put("codped", codped);
		GeneradorReportes rep = new GeneradorReportes();
		try {
			rep.generarReporte(response, getClass().getResource(reportUrl), "pdf", parametros,
					dataSource.getConnection(), "Lista de ventas", "inline");
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("imprimirc: " + e.toString());
		}
	}

	@GetMapping("imprimirfactura")
	public void imprimirFactura(HttpServletRequest request, HttpServletResponse response, Long codven)
			throws IOException {
		try {
		Map<String, Object> parametros = new HashMap<String, Object>();
		Usuario usuario = sessionS.getUser(request);
		String reportUrl = "/reportes/factura_ver.jasper";
		Factura fac = dosificacionS.obtenerfacturaxventa(codven);
		ViewVenta venta = ventaS.obtener(codven);
		Sucursal sucursal = sucursalS.obtener(venta.getCodsuc());
		Dosificacion dos = dosificacionS.obtener(fac.getCoddosificacion());
		String SubRep1 = getClass().getResource("/reportes/factura_ver_subreporte.jasper").toString();
		parametros.put("path", SubRep1.substring(0, SubRep1.lastIndexOf("/")) + "/");
		parametros.put("empresa", dos.getRazonsocial());
		parametros.put("direccion", dos.getDireccion());
		parametros.put("telefono", dos.getTelefono());
		parametros.put("sucursal", sucursal.getNombre());
		parametros.put("lugar", dos.getLugar());
		parametros.put("xmensaje", dos.getMensaje());
		parametros.put("xleyenda", dos.getLeyenda());
		parametros.put("usuario", usuario.getAlias());
		parametros.put("flimite", dos.getFlimite());
		parametros.put("codven", codven);
		parametros.put("xnit", dos.getNit());
		parametros.put("xfactura", fac.getNumfac());
		parametros.put("xactividad", dos.getActividad());
		parametros.put("xautorizacion", dos.getNumaut());
		parametros.put("sigla", dos.getSigla());
		parametros.put("monto_pagado", venta.getTotalPagado());
		parametros.put("cambio", venta.getCambio());
		parametros.put("total_literal",
				Numeros.convertirALiteral(venta.getTotal()) + " " + Numeros.obtenerDecimales(venta.getTotal()) + "/100 Bolivianos");
		GeneradorReportes rep = new GeneradorReportes();
		
			rep.generarReporte(response, getClass().getResource(reportUrl), "pdf", parametros,
					dataSource.getConnection(), "Factura " + fac.getNumfac(), "inline");
		} catch (Exception e) {
			LOGGER.error("imprimirFactura: "+e.toString());
			e.printStackTrace();
		}
	}

	@PostMapping(value = "guardarfactura")
	public ResponseEntity<Map<String, Object>> guardarfactura(HttpServletRequest request, Factura factura) {
		Map<String, Object> Data = new HashMap<String, Object>();
		try {
			Usuario usuario = sessionS.getUser(request);
			Sucursal sucursal = sessionS.getSucursal(request);
			if (usuario != null) {
				ViewVenta v = ventaS.obtener(factura.getCodven());
				factura.setTotal(v.getTotal());
				Dosificacion d = dosificacionS.dosificacionactual(sucursal.getCodsuc());
				factura.setCoddosificacion(d.getCoddosificacion());
				factura.setCfiscal(0f);
				factura.setDescuento(0f);
				factura.setNumfac(dosificacionS.obtenerNumFac(d.getCoddosificacion()));
				ControlCode control = new ControlCode();
				String codcontrol = control.generate(d.getNumaut(), factura.getNumfac().toString(), factura.getNitfac(),
						Fechas.getDateNowToString("yyyyMMdd"), factura.getTotal().toString(), d.getLlave());
				factura.setCodcontrol(codcontrol);
				dosificacionS.adicionarFactura(factura);
				Data.put("status", true);
			} else
				Data.put("status", false);
		} catch (Exception e) {
			LOGGER.error("guardarFactura: " + e.toString());
			Data.put("status", false);
		}
		return new ResponseEntity<Map<String, Object>>(Data, HttpStatus.OK);
	}

	@PostMapping("anularFactura")
	public ResponseEntity<Map<String, Object>> anularFactura(HttpServletRequest request, Long codven) {
		Map<String, Object> Data = new HashMap<String, Object>();
		try {
			Data.put("status", dosificacionS.anularFactura(codven));
		} catch (Exception e) {
			LOGGER.error("anularFactura: " + e.toString());
			Data.put("status", false);
		}
		return new ResponseEntity<Map<String, Object>>(Data, HttpStatus.OK);
	}

	@RequestMapping("agregarProducto")
	public @ResponseBody Map<String, Object> agregarProducto(HttpServletRequest request, DetallePedido detalle) {
		Map<String, Object> Data = new HashMap<String, Object>();
		try {
			pedidoS.adicionarDetalleSesion(request, detalle);
			Data.put("status", true);
		} catch (Exception e) {
			LOGGER.error("agregarProducto: " + e.toString());
			Data.put("status", false);
		}
		return Data;
	}

	@RequestMapping("adicionarVentaSimple")
	public String adicionarVentaSimple(HttpServletRequest request, Model model) {
		model.addAttribute("fecha",Fechas.obtenerFecha("dd/MM/yy"));
		return "venta/adicionar-venta";
	}
	@RequestMapping("adicionar")
	public String adicionar(HttpServletRequest request, Model model) {
		try {
			@SuppressWarnings("unchecked")
			List<DetallePedido> detallePedidoList = (List<DetallePedido>) request.getSession()
					.getAttribute(Constantes.Sesiones.DETALLE);
			Pedido pedido = (Pedido) request.getSession().getAttribute(Constantes.Sesiones.PEDIDO);
			model.addAttribute("detalles", detallePedidoList);
			model.addAttribute("pedido", pedido);
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("adicionar: "+e.getMessage());
		}
		return "venta/adicionarPedido";
	}
	@RequestMapping("eliminarDetallePedido")
	public @ResponseBody Map<String, Object> eliminarDetallePedido(HttpServletRequest request, Model model, DetallePedido detalle)
			throws IOException {
		Map<String, Object> Data = new HashMap<String, Object>();
		try {
			pedidoS.eliminarDetalleSesion(request, detalle);
			Data.put("total", pedidoS.obtenerTotalPedidoSesion(request));
			Data.put("status", true);
		} catch (Exception e) {
			Data.put("status", false);
		}
		return Data;
	}
	
	@RequestMapping("adicionarVentaPedido")
	public @ResponseBody Map<String, Object> adicionarVentaPedido(HttpServletRequest request, Long codped) {
		Usuario user = sessionS.getUser(request);
		Sucursal sucursal = sessionS.getSucursal(request);
		Map<String, Object> Data = new HashMap<>();
		if (user != null && sucursal != null) {
			Caja caja = cajaS.verificarUsuarioActivo(user.getCodusu(), sucursal.getCodsuc());
			Pedido pedido = pedidoS.obtenerPedido(codped);
			List<DetallePedido> detalles = pedidoS.obtenerDetallePedido(codped);
			if (caja != null && pedido!=null && detalles != null) {
				try {
					Venta v=new Venta();
					v.setCodusu(user.getCodusu());
					v.setCodcaja(caja.getCodcaja());
					//cargando de pedidos
					v.setClienteNit("sss");
					v.setCodcli(pedido.getCodusu());
					v.setCambio(0f);
					v.setEstado(1);
					v.setFecha(Fechas.getDateNowToString("dd/MM/yy"));
					v.setTiponota(1);
					v.setFormapago(1);
					Float total=pedidoS.obtenerTotal(detalles);
					v.setTotal(total);
					v.setTotalPagado(total);
					Long productos[]=new Long[detalles.size()];
					Float cantidades[]=new Float[detalles.size()];
					Float precios[]=new Float[detalles.size()];
					Float totales[]=new Float[detalles.size()];
					String tipoVentas[]=new String[detalles.size()];
					Long desc[]=new Long[detalles.size()];
					String cliente_nit ="S/N";
					String nit_fac ="0";
					Pedido pedidoEstado = new Pedido();
					pedidoEstado.setCodped(codped);
					pedidoEstado.setEstado(Constantes.ESTADO.ACEPTADO);
					pedidoS.cambiarEstado(pedidoEstado);
					Long codv = ventaS.adicionar(v,sucursal.getCodsuc(), productos, cantidades, precios, totales,tipoVentas, desc);
					// Si recuperamos el codigo de venta, realizamos la factura
					if (codv > 0) {
							Factura fac = new Factura();
							fac.setCodven(codv);
							fac.setClienteNit(cliente_nit!=null?cliente_nit:"SIN NOMBRE");
							fac.setNitfac(nit_fac!=null?nit_fac:"0");
							fac.setTotal(v.getTotal());
							Dosificacion d = dosificacionS.dosificacionactual(sucursal.getCodsuc());
							fac.setCoddosificacion(d.getCoddosificacion());
							fac.setCfiscal(0f);
							fac.setDescuento(0f);
							fac.setNumfac(dosificacionS.obtenerNumFac(d.getCoddosificacion()));
							SimpleDateFormat form = new SimpleDateFormat("yyyyMMdd");
							ControlCode control = new ControlCode();
							String codcontrol = control.generate(d.getNumaut(), fac.getNumfac().toString(),
									fac.getNitfac(), form.format(new Date()), fac.getTotal().toString(), d.getLlave());
							fac.setCodcontrol(codcontrol);
							dosificacionS.adicionarFactura(fac);
					}
					Data.put("status", codv > 0);
					Data.put("codven", codv);
				} catch (Exception e) {
					LOGGER.error("adicionarVentaPedido: " + e.toString());
					Data.put("status", false);
				}
			}
		}
		return Data;
	}
	@RequestMapping("calcularVentaProducto")
	public @ResponseBody Map<String, Object> calcularVentaProducto(HttpServletRequest request, Long codpro,int meses)throws IOException {
		Sucursal sucursal = sessionS.getSucursal(request);
		Map<String, Object> Data = new HashMap<String, Object>();
		if (sucursal != null) {
			Data.put("data", ventaS.obtenerVentaProducto(codpro, meses, sucursal.getCodsuc()));
			Data.put("status", true);
		}else {
			Data.put("status", false);
		}
		return Data;
	}
	@RequestMapping("reporteVenta")
	public @ResponseBody Map<String, Object> reporteVenta(HttpServletRequest request)
			throws IOException {
		Map<String, Object> Data = new HashMap<String, Object>();
		try {
			Data.put("lista", ventaS.reporteVentasSemanal());
			Data.put("listaMensual", ventaS.reporteVentasMensual());
			Data.put("status", true);
		} catch (Exception e) {
			Data.put("status", false);
		}
		return Data;
	}
}