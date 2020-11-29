package Controladores;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import Modelos.Cliente;
import Modelos.Usuario;
import Servicios.AccesousuarioImpl;
import Servicios.ClienteImpl;
import Utiles.DashBoard;

@Controller
@RequestMapping("/cliente/*")
public class ClienteRest {
	private static final Logger LOGGER = Logger.getLogger("ClienteRest");
	@Autowired
	ClienteImpl clienteS;
	@Autowired
	AccesousuarioImpl usuarioS;
	
	@RequestMapping("gestion")
	public String gestion(HttpServletRequest request,Model model){
		return "cliente/gestion";
	}
	@RequestMapping("gestionBuscar")
	public String buscar(HttpServletRequest request,Model model){
		model.addAttribute("categorias", clienteS.listarcategoria());
		return "cliente/gestionBuscar";
	}
	@RequestMapping("busqueda")
	public String buscar(HttpServletRequest request,Model model,String search,Integer searchc){
		model.addAttribute("productos", clienteS.buscarproducto(search,searchc));
		return "cliente/busqueda";
	}
	@RequestMapping("lista")
	public @ResponseBody Map<?, ?> lista(HttpServletRequest request, Integer draw,int length, Integer start, int estado)throws IOException{
		String search = request.getParameter("search[value]");
		List<Map<String, Object>> lista=clienteS.listar(start, estado, search,length);
		Long total;
		try {
			total = lista!=null?Long.parseLong(lista.get(0).get("tot").toString()) :0L;
		} catch (Exception e) {
			total = 0L;
		}
		Map<String, Object> Data = DashBoard.listado(draw, start, estado, length, search, lista,total);
		return Data;
	}
	@RequestMapping("guardar")
	public @ResponseBody Map<String, Object> guardar(HttpServletRequest request,Cliente c,Usuario persona,String ci,String login,String passwd){
		Map<String,Object> Data=new HashMap<>();
			try {			
				persona.setFoto("user.png");
				Data.put("status", clienteS.adicionar(c,persona,login,passwd));
			} catch (Exception e) {
				LOGGER.error("error al adicionar cliente"+e.toString());
				Data.put("status", false);
			}
		return Data;
	}
	@RequestMapping("obtener")
	public @ResponseBody Map<String, Object> obtener(HttpServletRequest request,Long codcli){
		Map<String, Object> Data = new HashMap<String, Object>();
		try {
			Map<String, Object> dato=clienteS.obtener(codcli);
			Data.put("data", dato);
			Data.put("status", true);
		} catch (Exception e) {
			Data.put("status", false);
			LOGGER.error("error al obtener="+e.toString());
		}
		return Data;
	}
	@RequestMapping("actualizar")
	public @ResponseBody Map<String, Object> actualizar(HttpServletRequest request,Model model,Cliente c,Usuario p,String login,String passwd)throws IOException{
		Usuario user=(Usuario)request.getSession().getAttribute("user");
		Map<String, Object> Data = new HashMap<String, Object>();
		if(user!=null){
			try {
				clienteS.modificar(c, p, login, passwd);
				Data.put("status", true);
			}catch(Exception e){
				LOGGER.error("error al modificar"+e.toString());
				Data.put("status", false);
			}
		}
		return Data;		
	}
	@RequestMapping("eliminar")
	public @ResponseBody Map<String, Object> eliminar(HttpServletRequest request,Model model,Long codcli)throws IOException{
		Usuario us=(Usuario)request.getSession().getAttribute("user");
		Map<String, Object> Data = new HashMap<String, Object>();
		if(us!=null){ 
				Data.put("status", clienteS.darestado(codcli, 0));
		}
		return Data;
	}
	@RequestMapping("habilitar")
	public @ResponseBody Map<String, Object> habilitar(HttpServletRequest request,Model model,Long codcli)throws IOException{
		Map<String, Object> Data = new HashMap<String, Object>();
				Data.put("status", clienteS.darestado(codcli,1));
		return Data;
	}
	@RequestMapping("obtenerFactura")
	public @ResponseBody Map<String, Object> obtenerFactura(HttpServletRequest request,String factura){
		Map<String, Object> Data = new HashMap<String, Object>();
		try {
			String dato=clienteS.obtenerFactura(factura);
			Data.put("data", dato);
			Data.put("status", true);
		} catch (Exception e) {
			Data.put("status", false);
			LOGGER.error("error al obtener="+e.toString());
		}
		return Data;
	}
}
