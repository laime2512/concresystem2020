package Controladores;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import Modelos.Proveedor;
import Modelos.Usuario;
import Servicios.ProveedorS;
import pagination.DataTableResults;

@Controller
@RequestMapping("/proveedor/*")
public class ProveedorRest {
	
	@Autowired
	ProveedorS proveedorS;
	
	private static final Logger LOGGER = Logger.getLogger("ProveedorRest");
	
	@RequestMapping("gestion")
	public String gestion(HttpServletRequest request,Model model){
		return "proveedor/gestion";
	}
	
	@RequestMapping("lista")
	public @ResponseBody DataTableResults<Proveedor> lista(HttpServletRequest request)throws IOException{
		return proveedorS.listado(request);
	}
	@RequestMapping("guardar")
	public @ResponseBody Map<String, Object> guardar(HttpServletRequest request,Proveedor c){
		Usuario user=(Usuario)request.getSession().getAttribute("user");
		Map<String,Object> Data=new HashMap<>();
		if(user!=null){
			try {			
				Data.put("status", proveedorS.adicionar(c));
			} catch (Exception e) {
				LOGGER.error("guardar: "+e.toString());
				Data.put("status", false);
			}
		}
		return Data;
	}
	@RequestMapping("obtener")
	public @ResponseBody Map<String, Object> obtener(HttpServletRequest request,Long codproveedor){
		Map<String, Object> Data = new HashMap<String, Object>();
		try {
			Map<String, Object> dato=proveedorS.obtener(codproveedor);
			Data.put("data", dato);
			Data.put("status", true);
		} catch (Exception e) {
			Data.put("status", false);
			System.out.println("obtener:"+e.toString());
		}
		return Data;
	}
	@RequestMapping("actualizar")
	public @ResponseBody Map<String, Object> actualizar(HttpServletRequest request,Model model,Proveedor c)throws IOException{
		Usuario user=(Usuario)request.getSession().getAttribute("user");
		Map<String, Object> Data = new HashMap<String, Object>();
		if(user!=null){
			try {
					Data.put("status", proveedorS.modificar(c));
			}catch(Exception e){
				LOGGER.error("actualizar: "+e.toString());
				Data.put("status", false);
			}
		}
		return Data;		
	}
	@RequestMapping("eliminar")
	public @ResponseBody Map<String, Object> eliminar(HttpServletRequest request,Model model,Long codproveedor)throws IOException{
		Usuario us=(Usuario)request.getSession().getAttribute("user");
		Map<String, Object> Data = new HashMap<String, Object>();
		if(us!=null){ 
				Data.put("status", proveedorS.darestado(codproveedor, 0));
		}
		return Data;
	}
	@RequestMapping("habilitar")
	public @ResponseBody Map<String, Object> habilitar(HttpServletRequest request,Model model,Long codproveedor)throws IOException{
		Map<String, Object> Data = new HashMap<String, Object>();
				Data.put("status", proveedorS.darestado(codproveedor,1));
		return Data;
	}
}

