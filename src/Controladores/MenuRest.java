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

import Modelos.Menu;
import Modelos.Usuario;
import Servicios.MenuS;
import Servicios.ProcesoS;
import Utiles.DashBoard;

@Controller
@RequestMapping("/menu/*")
public class MenuRest {
	
	@Autowired
	MenuS menuS;
	@Autowired
	ProcesoS procesoS;
	
	private static final Logger LOGGER = Logger.getLogger("MenuRest");
	@RequestMapping("gestion")
	public String gestion(HttpServletRequest request,Model model){
		model.addAttribute("procesos",procesoS.lista());
		return "menu/gestion";
	}
	@RequestMapping("lista")
	public @ResponseBody Map<?, ?> lista(HttpServletRequest request, Integer draw,int length, Integer start, int estado)throws IOException{
		String search = request.getParameter("search[value]");
		List<Map<String, Object>> lista=menuS.listar(start, estado, search,length);
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
	public @ResponseBody Map<String, Object> guardar(HttpServletRequest request,Menu m){
		Usuario user=(Usuario)request.getSession().getAttribute("user");
		Map<String,Object> Data=new HashMap<>();
		if(user!=null){
			try {			
					Data.put("status", menuS.adicionar(m));
			} catch (Exception e) {
				LOGGER.error("error al adicionar rol"+e.toString());
				Data.put("status", false);
			}
		}
		return Data;
	}
	
	@RequestMapping("obtener")
	public @ResponseBody Map<String, Object> obtener(HttpServletRequest request,int codm){
		Map<String, Object> Data = new HashMap<String, Object>();
		try {
			Map<String, Object> dato=menuS.obtener(codm);
			Data.put("data", dato);
			Data.put("procesos", procesoS.listaProcesosXmenu(codm));
			Data.put("status", true);
		} catch (Exception e) {
			Data.put("status", false);
			LOGGER.error("error al obtener="+e.toString());
		}
		return Data;
	}
	
	@RequestMapping("actualizar")
	public @ResponseBody Map<String, Object> actualizar(HttpServletRequest request,Model model,Menu m)throws IOException{
		Usuario user=(Usuario)request.getSession().getAttribute("user");
		Map<String, Object> Data = new HashMap<String, Object>();
		if(user!=null){
			try {
					Data.put("status", menuS.modificar(m));

			}catch(Exception e){
				LOGGER.error("error al modificar"+e.toString());
				Data.put("status", false);
			}
		}
		return Data;		
	}
	@RequestMapping("eliminar")
	public @ResponseBody Map<String, Object> eliminar(HttpServletRequest request,Model model,int codm)throws IOException{
		Usuario us=(Usuario)request.getSession().getAttribute("user");
		Map<String, Object> Data = new HashMap<String, Object>();
		if(us!=null){
				Data.put("status", menuS.darEstado(codm, 0));
		}
		return Data;
	}
	
	
	@RequestMapping("habilitar")
	public @ResponseBody Map<String, Object> habilitar(HttpServletRequest request,Model model,int codm)throws IOException{
		Map<String, Object> Data = new HashMap<String, Object>();
				Data.put("status", menuS.darEstado(codm,1));
		return Data;
	}
	@RequestMapping("asignar")
	public @ResponseBody Map<String, Object> asignar(HttpServletRequest request,Model model,int codm,Integer procesos[])throws IOException{
		Map<String, Object> Data = new HashMap<String, Object>();
		Data.put("status", menuS.asignar(codm,procesos));	
		return Data;
	}
	@RequestMapping("reasignar")
	public @ResponseBody Map<String, Object> reasignar(HttpServletRequest request,Model model,int codm,Integer procesos2[])throws IOException{
		Map<String, Object> Data = new HashMap<String, Object>();
		Data.put("status", menuS.asignar(codm,procesos2));	
		return Data;
	}
}
