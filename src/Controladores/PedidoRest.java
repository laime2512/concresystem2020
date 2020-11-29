package Controladores;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import Modelos.Caja;
import Modelos.DetallePedido;
import Modelos.Dosificacion;
import Modelos.Factura;
import Modelos.Lugar;
import Modelos.Pedido;
import Modelos.Sucursal;
import Modelos.Usuario;
import Modelos.Venta;
import Servicios.CajaS;
import Servicios.DosificacionS;
import Servicios.LugarS;
import Servicios.PedidoS;
import Servicios.VentaImpl;
import Utiles.Constantes;
import Utiles.Fechas;
import Utiles.GeneradorReportes;
import Utiles.Facturacion.ControlCode;

@Controller
@RequestMapping("/pedido/*")
public class PedidoRest {

	@Autowired
	private VentaImpl ventacS;
	@Autowired
	private DosificacionS dosificacionS;
	@Autowired
	private CajaS cajaS;
	@Autowired
	private PedidoS pedidoS;
	@Autowired
	private LugarS lugarS;
	
	private static final Logger LOGGER = Logger.getLogger("PedidoRest");

	@RequestMapping("gestion_x_cliente")
	public String gestion_x_cliente(HttpServletRequest request, Model model) {
		return "pedido/gestion_cliente";
	}
	@RequestMapping("gestion_x_usuario")
	public String gestion_x_usuario(HttpServletRequest request, Model model) {
		return "pedido/gestion_usuario";
	}

	
	
	@SuppressWarnings("unchecked")
	@RequestMapping("lista_x_usuario")
	public @ResponseBody Map<?, ?> lista_x_usuario(HttpServletRequest request, Integer draw, int length, Integer start,
			int estado,Integer tipo) throws IOException {
		String total;
		if(tipo ==null)
			tipo = -1;
		Map<String, Object> Data = new HashMap<String, Object>();
		String search = request.getParameter("search[value]");
		List<?> lista = ventacS.listar_usuario(start, estado, search, length, tipo);
		try {
			if(lista != null && !lista.isEmpty())
				total = ((Map<String, Object>) lista.get(0)).get("Tot").toString();
			else
				total="0";
		} catch (Exception e) {
			total = "0";
			e.printStackTrace();
			LOGGER.error("lista_x_usuario:"+e.getMessage());
		}
		Data.put("draw", draw);
		Data.put("recordsTotal", total);
		Data.put("data", lista);
		if (!search.equals(""))
			Data.put("recordsFiltered", lista.size());
		else
			Data.put("recordsFiltered", total);
		return Data;

	}

	@RequestMapping("guardar")
	public @ResponseBody Map<String, Object> guardar(HttpServletRequest request,Pedido obj) {
		Map<String, Object> Data = new HashMap<String, Object>();
		Usuario user = (Usuario) request.getSession().getAttribute(Constantes.Sesiones.USER);
		if (user != null) {
			try {
				HttpSession sesion = request.getSession();
				@SuppressWarnings("unchecked")
				List<DetallePedido> detallePedidoList =  (List<DetallePedido>) sesion.getAttribute(Constantes.Sesiones.DETALLE);
				Pedido pedido = (Pedido)request.getSession().getAttribute(Constantes.Sesiones.PEDIDO);
				
				pedido.setCelular(obj.getCelular());
				pedido.setDireccion(obj.getDireccion());
				pedido.setNit(obj.getNit());
				pedido.setRazon_nit(obj.getRazon_nit());
				
				Long codPed = pedidoS.adicionar(pedido, detallePedidoList);
				if(codPed > 0) {
					detallePedidoList.clear();
					sesion.setAttribute(Constantes.Sesiones.DETALLE, detallePedidoList);
				}
				Data.put("cantidad", "");
				Data.put("status", true);
				Data.put("codped", codPed);
			} catch (Exception e) {
				LOGGER.error("error al adicionar rol" + e.toString());
				Data.put("status", false);
			}
		}
		return Data;
	}

	@Autowired
	DataSource dataSource;

	@RequestMapping("imprimir")
	public void imprimir(HttpServletRequest request, HttpServletResponse response, int codped) throws IOException {
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
			LOGGER.error("imprimir:" + e.toString());
		}
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
		return "pedido/adicionarPedido";
	}
	@RequestMapping("eliminarDetalle")
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
	@RequestMapping("obtener")
	public @ResponseBody Map<String, Object> obtener(HttpServletRequest request, Long cod)
			throws IOException {
		Map<String, Object> Data = new HashMap<String, Object>();
		try {
			Pedido pedido = pedidoS.obtenerPedido(cod);
			List<DetallePedido> detallePedidoList = pedidoS.obtenerDetallePedido(cod);
			Data.put("data", pedido);
			Data.put("detalles", detallePedidoList);
			Data.put("status", true);
		} catch (Exception e) {
			Data.put("status", false);
		}
		return Data;
	}
	
	@RequestMapping("adicionarVentaPedido")
	public @ResponseBody Map<String, Object> adicionarVentaPedido(HttpServletRequest request, Long codped) {
		Usuario user = (Usuario) request.getSession().getAttribute("user");
		Sucursal sucursal = (Sucursal) request.getSession().getAttribute("sucursal");
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
					v.setClienteNit(pedido.getRazon_nit());
					v.setCodcli(pedido.getCodusu());
					v.setCambio(0f);
					v.setEstado(1);
					v.setFecha(Fechas.getDateNowToString("dd/MM/yy"));
					v.setTiponota(Constantes.ESTADO.ACEPTADO);
					v.setFormapago(1);
					Float total=pedidoS.obtenerTotal(detalles);
					v.setTotal(total);
					v.setTotalPagado(total);
					Long productos[]=new Long[detalles.size()];
					Integer cantidades[]=new Integer[detalles.size()];
					Float precios[]=new Float[detalles.size()];
					Float totales[]=new Float[detalles.size()];
					Long lugares[]=new Long[detalles.size()];
					Integer opt_cants[]=new Integer[detalles.size()];
					Boolean desc[]=new Boolean[detalles.size()];
					
					int index = 0;
					for (DetallePedido det : detalles) {
						productos[index] = det.getCodpro();
						cantidades[index] = det.getCantidad();
						precios[index] = det.getPrecio();
						totales[index] = det.getSubtotal();
						opt_cants[index] = Constantes.Ventas.UNIDAD;
						desc[index] = false;
						Lugar lugar = lugarS.obtenerPorSucursalProducto(sucursal.getCodsuc(), det.getCodpro());
						lugares[index] = lugar != null ? lugar.getCodlugar():null;
						index++;
					}
					
					String cliente_nit = pedido.getRazon_nit();
					String nit_fac = pedido.getNit();
					Pedido pedidoEstado = new Pedido();
					
					pedidoEstado.setCodped(codped);
					pedidoEstado.setEstado(Constantes.ESTADO.ACEPTADO);
					pedidoS.cambiarEstado(pedidoEstado);
					Long codv = null;//ventaS.adicionar(v, productos, cantidades, precios, totales, lugares,
							//sucursal.getCodsuc(), opt_cants, desc);
					//Asignar venta y pedido
					pedido.setCodven(codv);
					pedidoS.asignarVenta(pedido);
					// Si recuperamos el codigo de venta, realizamos la factura
					if (codv > 0) {
							Factura fac = new Factura();
							fac.setCodven(codv);
							if (cliente_nit != null)
								fac.setClienteNit(cliente_nit);
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
					LOGGER.error("adicionarVentaPedido=" + e.toString());
					Data.put("status", false);
				}
			}
		}
		return Data;
	}
	@RequestMapping("confirmarEntrega")
	public @ResponseBody Map<String, Object> confirmarEntrega(HttpServletRequest request, Pedido obj)
			throws IOException {
		Map<String, Object> Data = new HashMap<String, Object>();
		try {
			obj.setEstado(Constantes.Pedidos.ENTREGADO);
			pedidoS.cambiarEstado(obj);
			Data.put("status", true);
		} catch (Exception e) {
			Data.put("status", false);
		}
		return Data;
	}
	@RequestMapping("anular")
	public @ResponseBody Map<String, Object> anularEnvio(HttpServletRequest request, Pedido obj)
			throws IOException {
		Map<String, Object> Data = new HashMap<String, Object>();
		try {
			obj.setEstado(Constantes.ESTADO.PENDIENTE);
			pedidoS.adicionarObservacion(obj);
			pedidoS.cambiarEstado(obj);
			Data.put("status", dosificacionS.anularFactura(obj.getCodven()));
		} catch (Exception e) {
			Data.put("status", false);
		}
		return Data;
	}
	@RequestMapping("anularPendiente")
	public @ResponseBody Map<String, Object> anularPendiente(HttpServletRequest request, Pedido obj)
			throws IOException {
		Map<String, Object> Data = new HashMap<String, Object>();
		try {
			obj.setEstado(Constantes.ESTADO.RECHAZADO);
			pedidoS.adicionarObservacion(obj);
			pedidoS.cambiarEstado(obj);
			Data.put("status", dosificacionS.anularFactura(obj.getCodven()));
		} catch (Exception e) {
			Data.put("status", false);
		}
		return Data;
	}
	@RequestMapping("eliminar")
	public @ResponseBody Map<String, Object> eliminar(HttpServletRequest request, Pedido obj)
			throws IOException {
		Map<String, Object> Data = new HashMap<String, Object>();
		try {
			obj.setEstado(Constantes.ESTADO.RECHAZADO);
			pedidoS.adicionarObservacion(obj);
			pedidoS.cambiarEstado(obj);
			Data.put("status", true);
		} catch (Exception e) {
			Data.put("status", false);
		}
		return Data;
	}
}