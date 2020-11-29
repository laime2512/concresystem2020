package Controladores;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import Modelos.Usuario;
import Servicios.AccesoUsuarioS;
import Servicios.CategoriaImpl;
import Servicios.MenuS;
//import Servicios.PedidoS;
import Servicios.ProductoImpl;
import Servicios.SucursalS;
import Utiles.Constantes;

@Controller
@RequestMapping("/principal/*")
public class PrincipalRest {

	private static final Logger LOGGER = Logger.getLogger("PrincipalRest");

	@Autowired
	private AccesoUsuarioS usuarioS;
	@Autowired
	private MenuS menuS;
	@Autowired
	private ProductoImpl productoS;
	@Autowired
	private CategoriaImpl categoriaS;
//	@Autowired
//	private PedidoS pedidoS;
	@Autowired
	private SucursalS sucursalS;

	@RequestMapping("login")
	public String login(Model model, HttpServletRequest request) {
		model.addAttribute("msg","Bienvenido, ingrese datos de acceso.");
		model.addAttribute("sucursales",sucursalS.listar());
		return "principal/login";
	}

	@RequestMapping("blanco")
	public String blanco() {
		return "principal/blanco";
	}

	@RequestMapping("validar")
	public @ResponseBody Map<String, Object> validar(HttpServletRequest request, Model model, String login,
			String password) throws SQLException {
		Usuario user = usuarioS.iniciarSesion(login, password);
		Map<String, Object> Data = new HashMap<>();
		HttpSession sesion = request.getSession();
		if (user != null) {
			LOGGER.info("Finalizacion de sesion: "+user.getNombre()+" "+user.getAp());
			sesion.setAttribute(Constantes.Sesiones.USER, user);
//			pedidoS.adicionarPedidoSesion(sesion, user);
		}else {
			Data.put("msg", "Usuario incorrecto, por favor verifique login y clave. Si persiste, comunicarse con administracion, para habilitar.");
			Data.put("status", false);
			return Data;
		}
		if (user.getSucursales() != null && user.getSucursales().size() > 0)
			sesion.setAttribute(Constantes.Sesiones.SUCURSAL, user.getSucursales().get(0));
		else {
			Data.put("msg", "Usuario sin sucursal, comunicarse con administracion, para que se le asigne.");
			Data.put("status", false);
			return Data;
		}
		if (user.getRoles() != null && user.getRoles().size() > 0) {
			sesion.setAttribute(Constantes.Sesiones.ROLES, user.getRoles());
			sesion.setAttribute(Constantes.Sesiones.ROL, user.getRoles().get(0));
		}else {
			Data.put("msg", "Usuario sin roles, comunicarse con administracion, para que se le asigne.");
			Data.put("status", false);
			return Data;
		}
		Data.put("status", true);
		return Data;
	}


	@RequestMapping("acceder")
	public String acceder(HttpServletRequest request, Model model) {
		Usuario person = (Usuario) request.getSession().getAttribute("user");
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> listaRoles = (List<Map<String, Object>>) request.getSession().getAttribute("roles");
		model.addAttribute(Constantes.Sesiones.USER, person);
		model.addAttribute(Constantes.Sesiones.ROLES, listaRoles);
		if (person.getSucursales() != null)
			model.addAttribute(Constantes.Sesiones.SUCURSAL, person.getSucursales().get(0));
		model.addAttribute("menus", menuS.obtenerXusuario(person.getUsuario().getLogin()));
		model.addAttribute("msg", "Bienvenido, " + person.getNombre() + " " + person.getAp());
		model.addAttribute("cantidad","");
		return "principal/principal";
	}

	@RequestMapping("cambiarSucursal")
	public String cambiarSucursal(HttpServletRequest request, Model model, Integer sucursal) throws IOException {
		HttpSession sesion = request.getSession();
		Usuario person = (Usuario) sesion.getAttribute(Constantes.Sesiones.USER);
		if (person != null) {
			model.addAttribute("user", person);
			if (person.getSucursales() != null) {
				model.addAttribute(Constantes.Sesiones.SUCURSAL, person.getSucursales().get(sucursal));
				sesion.removeAttribute(Constantes.Sesiones.SUCURSAL);
				sesion.setAttribute(Constantes.Sesiones.SUCURSAL, person.getSucursales().get(sucursal));
			}
			model.addAttribute("menus", menuS.obtenerXusuario(person.getUsuario().getLogin()));
			model.addAttribute("msg", "bienvenido, " + person.getNombre() + " " + person.getAp());

			return "principal/principal";
		} else {
			model.addAttribute("msg", "Sesion finalizada");
			model.addAttribute("categorias",categoriaS.listar_todos());
			model.addAttribute("isCliente", 0);
			return "principal/login";
		}
	}

	@RequestMapping("salir")
	public String salir(HttpServletRequest request, Model model) {
		HttpSession sesion = request.getSession();
		sesion.invalidate();
		model.addAttribute("msg", "Gracias por utilizar el sistema");
		model.addAttribute("isCliente", 0);
		model.addAttribute("sucursales",sucursalS.listar());
		LOGGER.info("Finalizacion de sesion: ");
		return "principal/login";
	}

	@RequestMapping("listarxcategoria")
	public String listarxcategoria(Model model, int cod) {
		model.addAttribute("productos", productoS.listarxcategoria(cod));
		return "principal/listaxcategoria";
	}

	@RequestMapping("listarxtipo")
	public String listarxtipo(Model model, int cod) {
		model.addAttribute("productos", productoS.listarxtipo(cod));
		return "principal/listaxcategoria";
	}
}
