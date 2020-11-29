package Controladores;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import Modelos.Margen;
import Modelos.Usuario;
import Servicios.MargenS;
import Utiles.Constantes;
import Utiles.DashBoard;
import Wrap.MargenWrap;
import pagination.DataTableResults;

@Controller
@RequestMapping("/margen/*")
public class MargenRest {
	
	@Autowired
	private MargenS margenS;

	private static final Logger LOGGER = Logger.getLogger("MargenRest");
	
	
	@RequestMapping("gestion")
	public String gestion(HttpServletRequest request,Model model){
		Usuario user=(Usuario)request.getSession().getAttribute(Constantes.Sesiones.USER);
		model.addAttribute("xusuario",user.getNombre()+" "+user.getAp());
		return "margen/gestion";
	}
	@RequestMapping("lista")
	public @ResponseBody DataTableResults<Margen> lista(HttpServletRequest request)throws IOException, SQLException{
		return margenS.listar(request);
	}
	@RequestMapping("guardar")
	public @ResponseBody Map<String, Object> guardar(HttpServletRequest request,Integer codMargen,String observacion,Margen margen,MargenWrap obj){
		Usuario user=(Usuario)request.getSession().getAttribute(Constantes.Sesiones.USER);
		Map<String,Object> Data=new HashMap<>();
		if(user!=null){
			try {
				if(codMargen ==null) {
					margen.setCodusu(user.getCodusu());
					obj.setMargen(margen);
					Data.put("status", margenS.adicionar(obj));
				}else {//modificar margen
					obj.setMargen(margen);
					margenS.modificar(obj);
					Data.put("status", true);
				}
			} catch (Exception e) {
				LOGGER.error("guardar: "+e.toString());
				Data.put("status", false);
			}
		}
		return Data;
	}
	@RequestMapping("obtener")
	public @ResponseBody Map<String, Object> obtener(Margen obj){
		Map<String, Object> Data = new HashMap<String, Object>();
		try {
			Data.put("data", margenS.obtener(obj));
			Data.put("status", true);
		} catch (Exception e) {
			Data.put("status", false);
			LOGGER.error("obtener="+e.toString());
		}
		return Data;
	}
	@GetMapping("getLastMargen")
	public @ResponseBody Map<String, Object> getLastMargen(){
		Map<String, Object> Data = new HashMap<String, Object>();
		try {
			Data.put("data", margenS.getLastMargen());
			Data.put("status", true);
		} catch (Exception e) {
			Data.put("status", false);
			LOGGER.error("getLastMargen="+e.toString());
		}
		return Data;
	}
	@RequestMapping("eliminar")
	public @ResponseBody Map<String, Object> eliminar(HttpServletRequest request,Margen obj)throws IOException{
		Usuario us=(Usuario)request.getSession().getAttribute("user");
		Map<String, Object> Data = new HashMap<String, Object>();
		if(us!=null){
				try {
					Data.put("status", margenS.eliminar(obj));
				} catch (SQLException e) {
					e.printStackTrace();
					LOGGER.error("eliminar: "+e.toString());
				}
		}
		return Data;
	}
}
