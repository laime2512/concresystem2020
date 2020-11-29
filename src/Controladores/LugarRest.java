
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import Modelos.Lugar;
import Modelos.Sucursal;
import Modelos.Usuario;
import Servicios.LugarS;
import Servicios.MuebleS;
import Servicios.ProductoS;
import pagination.DataTableResults;

@Controller
@RequestMapping("/lugar/*")
public class LugarRest {

	@Autowired
	private ProductoS productoS;
	@Autowired
	private LugarS lugarS;
	@Autowired
	private MuebleS muebleS;
	private static final Logger LOGGER = Logger.getLogger("LugarRest");

	@RequestMapping("gestion")
	public String gestion(HttpServletRequest request, Model model) {
		model.addAttribute("muebles",muebleS.listar());
		model.addAttribute("productos",productoS.listarTodos());
		return "lugar/gestion";
	}
//	@RequestMapping("lista")
//	public @ResponseBody Map<?, ?> lista(HttpServletRequest request, Integer draw, int length, Integer start,
//			Integer estado) throws IOException,SQLException {
//		String search = request.getParameter("search[value]");
//		HttpSession sesion=request.getSession();
//		Sucursal sucursal=(Sucursal)sesion.getAttribute("sucursal");
//		if(sucursal!=null) {
//			List<Map<String,Object>> lista = lugarS.listar(start, estado, search, length,sucursal.getCodsuc());
//			Long total;
//			try {
//				total = lista!=null?Long.parseLong(lista.get(0).get("tot").toString()) :0L;
//			} catch (Exception e) {
//				total = 0L;
//			}
//			Map<String, Object> Data = DashBoard.listado(draw, start, estado, length, search, lista,total);
//			return Data;
//		}else {
//			return null;
//		}
//	}
	@RequestMapping("lista")
	public @ResponseBody DataTableResults<Lugar> lista(HttpServletRequest request) {
		return lugarS.listar(request);
	}

	@RequestMapping("guardar")
	public @ResponseBody Map<String, Object> guardar(HttpServletRequest request, Lugar obj) {
		Usuario user = (Usuario) request.getSession().getAttribute("user");
		Sucursal sucursal=(Sucursal) request.getSession().getAttribute("sucursal");
		Map<String, Object> Data = new HashMap<>();
		if (user != null && sucursal!=null) {
			obj.setCodsuc(sucursal.getCodsuc());
			Data.put("status", lugarS.adicionar(obj)>0);
		}
		return Data;
	}

	@RequestMapping("obtener")
	public @ResponseBody Map<String, Object> obtener(HttpServletRequest request, Lugar obj) {
		Map<String, Object> Data = new HashMap<String, Object>();
		try {
			Map<String, Object> dato = lugarS.obtener(obj);
			Data.put("data", dato);
			Data.put("status", true);
		} catch (Exception e) {
			Data.put("status", false);
			LOGGER.error("obtener=" + e.toString());
		}
		return Data;
	}
	
	@RequestMapping("verificar")
	public @ResponseBody Map<String, Object> verificar(HttpServletRequest request, Lugar obj) {
		Map<String, Object> Data = new HashMap<String, Object>();
		try {
			Sucursal sucursal=(Sucursal) request.getSession().getAttribute("sucursal");
			if(sucursal!=null) {
				obj.setCodsuc(sucursal.getCodsuc());
				Map<String, Object> dato = lugarS.verificar(obj);
				Data.put("data", dato);
				Data.put("status", true);
			}
		} catch (Exception e) {
			Data.put("status", false);
			LOGGER.error("verificar:" + e.toString());
		}
		return Data;
	}
	@RequestMapping("actualizar")
	public @ResponseBody Map<String, Object> actualizar(HttpServletRequest request, Lugar obj) {
		Usuario user = (Usuario) request.getSession().getAttribute("user");
		Map<String, Object> Data = new HashMap<>();
		if (user != null ) {
			lugarS.modificar(obj);
			Data.put("status", true);
		}
		return Data;
	}

	@RequestMapping("eliminar")
	public @ResponseBody Map<String, Object> eliminar(HttpServletRequest request, Model model, Lugar obj)
			throws IOException,SQLException {
		Usuario us = (Usuario) request.getSession().getAttribute("user");
		Map<String, Object> Data = new HashMap<String, Object>();
		if (us != null) {
			Data.put("status", lugarS.eliminar(obj));
		}
		return Data;
	}

}
