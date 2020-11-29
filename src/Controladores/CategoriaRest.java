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

import Modelos.Categoria;
import Modelos.Usuario;
import Servicios.CategoriaS;
import Utiles.DashBoard;
/**
 * Administra todas las categorias del sistema
 * @author CARLOS
 *
 */
@Controller
@RequestMapping("/categoria/*")
public class CategoriaRest {
	
	@Autowired
	CategoriaS categoriaS;
	private static final Logger LOGGER = Logger.getLogger("CategoriaRest");
	
	@RequestMapping("gestion")
	public String gestion(HttpServletRequest request,Model model){
		return "categoria/gestion";
	}
	@RequestMapping("lista")
	public @ResponseBody Map<?, ?> lista(HttpServletRequest request, Integer draw,int length, Integer start, int estado)throws IOException{
		String search = request.getParameter("search[value]");
		List<Map<String, Object>> lista=categoriaS.listar(start, estado, search,length);
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
	public @ResponseBody Map<String, Object> guardar(HttpServletRequest request,Categoria cat){
		Usuario user=(Usuario)request.getSession().getAttribute("user");
		Map<String,Object> Data=new HashMap<>();
		if(user!=null){
			try {			
				if(categoriaS.adicionar(cat)){
					Data.put("status", true);
				}else{
					Data.put("status", false);
				}
			} catch (Exception e) {
				LOGGER.error("error al adicionar categoria "+e.toString());
				Data.put("status", false);
			}
		}
		return Data;
	}
	
	@RequestMapping("obtener")
	public @ResponseBody Map<String, Object> obtener(HttpServletRequest request,int codcat){
		Map<String, Object> Data = new HashMap<String, Object>();
		try {
			Categoria dato=categoriaS.obtener(codcat);
			Data.put("data", dato);
			Data.put("status", true);
		} catch (Exception e) {
			Data.put("status", false);
			LOGGER.error("error al obtener="+e.toString());
		}
		return Data;
	}
	
	@RequestMapping("actualizar")
	public @ResponseBody Map<String, Object> actualizar(HttpServletRequest request,Model model,Categoria obj)throws IOException{
		Usuario user=(Usuario)request.getSession().getAttribute("user");
		Map<String, Object> Data = new HashMap<String, Object>();
		if(user!=null){
			try {
				if(categoriaS.modificar(obj)){
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
	public @ResponseBody Map<String, Object> eliminar(HttpServletRequest request,Model model,int codcat)throws IOException{
		Usuario us=(Usuario)request.getSession().getAttribute("user");
		Map<String, Object> Data = new HashMap<String, Object>();
		if(us!=null){
			if(categoriaS.darEstado(codcat, 0))
				Data.put("status", true);
			else 
				Data.put("status", false);
		}
		return Data;
	}
	
}
