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

import Modelos.ProductoNuevo;
import Modelos.Usuario;
import Servicios.ProductoNuevoS;
import Utiles.DashBoard;

@Controller
@RequestMapping("/producto_nuevo/*")
public class ProductoNuevoRest {
	
	@Autowired
	ProductoNuevoS producto_nuevoS;
	
	private static final Logger LOGGER = Logger.getLogger("ProductoNuevoRest");
	
	@RequestMapping("gestion")
	public String gestion(HttpServletRequest request,Model model){
		return "producto_nuevo/gestion";
	}
	@RequestMapping("lista")
	public @ResponseBody Map<?, ?> lista(HttpServletRequest request, Integer draw,int length, Integer start, boolean estado)throws IOException{
		String search = request.getParameter("search[value]");
		List<Map<String, Object>> lista=producto_nuevoS.listar(start, estado, search,length);
		Long total;
		try {
			total = lista!=null?Long.parseLong(lista.get(0).get("tot").toString()) :0L;
		} catch (Exception e) {
			total = 0L;
		}
		Map<String, Object> Data = DashBoard.listado(draw, start, estado?1:0, length, search, lista,total);
		return Data;

	}
	@RequestMapping("guardar")
	public @ResponseBody Map<String, Object> guardar(HttpServletRequest request,ProductoNuevo obj){
		Usuario user=(Usuario)request.getSession().getAttribute("user");
		Map<String,Object> Data=new HashMap<>();
		if(user!=null){
			try {
				obj.setCodusu(user.getCodusu());
				Data.put("status", producto_nuevoS.adicionar(obj));
			} catch (Exception e) {
				LOGGER.error("guardar: "+e.toString());
				Data.put("status", false);
			}
		}
		return Data;
	}
	
	@RequestMapping("obtener")
	public @ResponseBody Map<String, Object> obtener(HttpServletRequest request,Long cod_pro_nuevo){
		Map<String, Object> Data = new HashMap<String, Object>();
		try {
			ProductoNuevo dato=producto_nuevoS.obtener(cod_pro_nuevo);
			Data.put("data", dato);
			Data.put("status", true);
		} catch (Exception e) {
			Data.put("status", false);
			LOGGER.error("obtener: "+e.toString());
		}
		return Data;
	}
	
	@RequestMapping("actualizar")
	public @ResponseBody Map<String, Object> actualizar(HttpServletRequest request,Model model,ProductoNuevo obj)throws IOException{
		Usuario user=(Usuario)request.getSession().getAttribute("user");
		Map<String, Object> Data = new HashMap<String, Object>();
		if(user!=null){
			try {
				obj.setCodusu(user.getCodusu());
				Data.put("status", producto_nuevoS.modificar(obj));
			}catch(Exception e){
				LOGGER.error("actualizar: "+e.toString());
				Data.put("status", false);
			}
		}
		return Data;		
	}
	@RequestMapping("eliminar")
	public @ResponseBody Map<String, Object> eliminar(HttpServletRequest request,Model model,Long cod_pro_nuevo)throws IOException{
		Usuario us=(Usuario)request.getSession().getAttribute("user");
		Map<String, Object> Data = new HashMap<String, Object>();
		if(us!=null){
			Data.put("status", producto_nuevoS.darEstado(cod_pro_nuevo, false));
		}
		return Data;
	}
	
}
