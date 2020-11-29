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

import Modelos.Usuario;
import Modelos.Rol;
import Servicios.MenuS;
import Servicios.RolImpl;
import Utiles.DashBoard;
import pagination.DataTableResults;

@Controller
@RequestMapping("/rol/*")
public class RolRest {
	
	@Autowired
	private RolImpl rolS;
	@Autowired
	private MenuS menuS;
	
	private static final Logger LOGGER = Logger.getLogger("RolRest");
	
	@RequestMapping("gestion")
	public String gestion(HttpServletRequest request,Model model){
		model.addAttribute("menus",menuS.lista());
		return "rol/gestion";
	}
	@RequestMapping("lista")
	public @ResponseBody DataTableResults<Rol> lista(HttpServletRequest request)throws IOException{
		return rolS.listar(request);
	}
	@RequestMapping("guardar")
	public @ResponseBody Map<String, Object> guardar(HttpServletRequest request,Rol r){
		Usuario user=(Usuario)request.getSession().getAttribute("user");
		Map<String,Object> Data=new HashMap<>();
		if(user!=null){
			try {			
				Data.put("status", rolS.adicionar(r));
			} catch (Exception e) {
				LOGGER.error("error al adicionar rol"+e.toString());
				Data.put("status", false);
			}
		}
		return Data;
	}
	
	@RequestMapping("obtener")
	public @ResponseBody Map<String, Object> obtener(HttpServletRequest request,int codr){
		Map<String, Object> Data = new HashMap<String, Object>();
		try {
			Rol dato=rolS.obtener(codr);
			Data.put("data", dato);
			Data.put("menus", menuS.listaMenusXrol(codr));
			Data.put("status", true);
		} catch (Exception e) {
			Data.put("status", false);
			LOGGER.error("error al obtener="+e.toString());
		}
		return Data;
	}
	
	@RequestMapping("actualizar")
	public @ResponseBody Map<String, Object> actualizar(HttpServletRequest request,Model model,Rol r)throws IOException{
		Usuario user=(Usuario)request.getSession().getAttribute("user");
		Map<String, Object> Data = new HashMap<String, Object>();
		if(user!=null){
			try {
				if(rolS.modificar(r)){
					Data.put("status", true);
				}else{
					Data.put("status", false);
				}
			}catch(Exception e){
				LOGGER.error("error al modificar"+e.toString());
				Data.put("status", false);
			}
		}
		return Data;		
	}
	@RequestMapping("eliminar")
	public @ResponseBody Map<String, Object> eliminar(HttpServletRequest request,Model model,int codr)throws IOException{
		Usuario us=(Usuario)request.getSession().getAttribute("user");
		Map<String, Object> Data = new HashMap<String, Object>();
		if(us!=null){
			Data.put("status", rolS.darEstado(codr, 0));
		}
		return Data;
	}
	
	
	@RequestMapping("habilitar")
	public @ResponseBody Map<String, Object> habilitar(HttpServletRequest request,Model model,int codr)throws IOException{
		Map<String, Object> Data = new HashMap<String, Object>();
			if(rolS.darEstado(codr,1))
				Data.put("status", true);
			else
				Data.put("status", false);
		return Data;
	}
	@RequestMapping("asignar")
	public @ResponseBody Map<String, Object> asignar(HttpServletRequest request,Model model,int codr,Integer menus[])throws IOException{
		Map<String, Object> Data = new HashMap<String, Object>();
		Data.put("status", rolS.asignar(codr,menus));	
		return Data;
	}
	@RequestMapping("reasignar")
	public @ResponseBody Map<String, Object> reasignar(HttpServletRequest request,Model model,int codr,Integer menus2[])throws IOException{
		Map<String, Object> Data = new HashMap<String, Object>();
		Data.put("status", rolS.asignar(codr,menus2));	
		return Data;
	}
}
