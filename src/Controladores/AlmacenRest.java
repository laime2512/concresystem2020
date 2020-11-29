
package Controladores;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import Modelos.Inventario;
import Modelos.Sucursal;
import Modelos.Usuario;
import Modelos.ViewAlmacen;
import Servicios.AlmacenS;
import Servicios.ProductoS;
import Utiles.Constantes;
import Wrap.AlmacenUngroupWrap;
import pagination.DataTableResults;

@Controller
@RequestMapping("/almacen/*")
public class AlmacenRest {

	@Autowired
	AlmacenS almacenS;
	@Autowired
	ProductoS productoS;
	private final Logger LOGGER = Logger.getLogger(AlmacenRest.class.getName());

	@RequestMapping("reporteInventario")
	public String reporteInventario(HttpServletRequest request, Model model) {
		Usuario user=(Usuario)request.getSession().getAttribute(Constantes.Sesiones.USER);
		Sucursal sucursal=(Sucursal)request.getSession().getAttribute(Constantes.Sesiones.SUCURSAL);
		model.addAttribute("sucursal", sucursal);
		model.addAttribute("user", user);
		return "almacen/reporteInventario";
	}
	@RequestMapping("gestionInventario")
	public String gestionInventario(HttpServletRequest request, Model model) {
		Usuario user=(Usuario)request.getSession().getAttribute(Constantes.Sesiones.USER);
		model.addAttribute("user", user);
		return "almacen/gestionInventario";
	}
	@RequestMapping("gestionAlmacen")
	public String gestionAlmacen(HttpServletRequest request, Model model) {
		Usuario user=(Usuario)request.getSession().getAttribute(Constantes.Sesiones.USER);
		model.addAttribute("user", user);
		return "almacen/gestionAlmacen";
	}
	@GetMapping("listaInventario")
	public @ResponseBody DataTableResults<Inventario> listaInventario(HttpServletRequest request) throws IOException, SQLException {
		return productoS.listarInventario(request);
	}
	@GetMapping("listaAlmacen")
	public @ResponseBody DataTableResults<ViewAlmacen> listaAlmacen(HttpServletRequest request) throws IOException, SQLException {
		return almacenS.listar(request);
	}
	@GetMapping("listaAlmacenDesagrupado")
	public @ResponseBody DataTableResults<AlmacenUngroupWrap> listaAlmacenDesagrupado(HttpServletRequest request) throws IOException, SQLException {
		return almacenS.listarAlmacenDesagrupado(request);
	}
	@GetMapping("obtenerAlmacenPorProducto/{id}")
	public @ResponseBody Map<String, Object> obtenerAlmacenPorProducto(HttpServletRequest request,@PathVariable(name = "id")Long codpro) throws IOException, SQLException {
		Map<String, Object> Data = new HashMap<String, Object>();
		try {
			Sucursal sucursal=(Sucursal)request.getSession().getAttribute("sucursal");
			Data.put("data", almacenS.obtenerAlmacenPorProducto(codpro, sucursal.getCodsuc()));
			Data.put("status", true);
		} catch (Exception e) {
			LOGGER.error("obtenerAlmacenPorProducto"+e.getMessage());
			Data.put("status", true);
		}
		return Data;
	}
	@GetMapping("buscarCodigoBarra/{id}")
	public @ResponseBody Map<String, Object> buscarCodigoBarra(HttpServletRequest request,@PathVariable(name = "id")String codebar) throws IOException, SQLException {
		Map<String, Object> Data = new HashMap<String, Object>();
		try {
			Data.put("data", productoS.buscarPorCodigoBarra(request, codebar));
			Data.put("status", true);
		} catch (Exception e) {
			LOGGER.error("obtenerAlmacenPorProducto"+e.getMessage());
			Data.put("status", true);
		}
		return Data;
	}
}
