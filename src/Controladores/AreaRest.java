package Controladores;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import Modelos.Usuario;
import Modelos.Area;
import Servicios.AreaS;
import Utiles.DashBoard;

@Controller
@RequestMapping("/area/*")
public class AreaRest {
	
	@Autowired
	AreaS areaS;
	private String className=AreaRest.class.getName();
	private Logger log=Logger.getLogger(className);
	
	
	@RequestMapping("gestion")
	public String gestion(HttpServletRequest request,Model model){
		return "area/gestion";
	}
	@RequestMapping("lista")
	public @ResponseBody Map<?, ?> lista(HttpServletRequest request, Integer draw,Integer length, Integer start, Boolean estado)throws IOException, SQLException{
		String search = request.getParameter("search[value]");
		List<Map<String, Object>> lista= areaS.listar(start, estado, search,length);
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
	public @ResponseBody Map<String, Object> guardar(HttpServletRequest request,Area obj){
		Usuario user=(Usuario)request.getSession().getAttribute("user");
		Map<String,Object> Data=new HashMap<>();
		if(user!=null){
			try {
				Data.put("status", areaS.adicionar(obj));
			} catch (Exception e) {
				log.info("error al adicionar "+className+"="+e.toString());
				Data.put("status", false);
			}
		}
		return Data;
	}
	
	@RequestMapping("obtener")
	public @ResponseBody Map<String, Object> obtener(Area obj){
		Map<String, Object> Data = new HashMap<String, Object>();
		try {
			Data.put("data", areaS.obtener(obj));
			Data.put("status", true);
		} catch (Exception e) {
			Data.put("status", false);
			log.info("error al obtener="+e.toString());
		}
		return Data;
	}

	@RequestMapping("actualizar")
	public @ResponseBody Map<String, Object> actualizar(HttpServletRequest request,Area obj)throws IOException{
		Usuario user=(Usuario)request.getSession().getAttribute("user");
		Map<String, Object> Data = new HashMap<String, Object>();
		if(user!=null){
			try {
				areaS.modificar(obj);
				Data.put("status", true);
			}catch(Exception e){
				log.info("error al modificar"+e.toString());
				Data.put("status", false);
			}
		}
		return Data;
	}
	@RequestMapping("eliminar")
	public @ResponseBody Map<String, Object> eliminar(HttpServletRequest request,Area obj)throws IOException{
		Usuario us=(Usuario)request.getSession().getAttribute("user");
		Map<String, Object> Data = new HashMap<String, Object>();
		if(us!=null){
				try {
					Data.put("status", areaS.eliminar(obj));
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					log.info("Error al eliminar "+className+"="+e.toString());
				}
		}
		return Data;
	}
	
}
