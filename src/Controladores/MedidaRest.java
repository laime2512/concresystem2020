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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import Modelos.Usuario;
import Modelos.Medida;
import Servicios.MedidaS;
import Utiles.DashBoard;

@Controller
@RequestMapping("/medida/*")
public class MedidaRest {
	
	@Autowired
	MedidaS medidaS;
	private String className=MedidaRest.class.getName();
	private static final Logger LOGGER=Logger.getLogger("MedidaRest");
	
	
	@RequestMapping("gestion")
	public String gestion(HttpServletRequest request,Model model){
		return "medida/gestion";
	}
	@RequestMapping("lista")
	public @ResponseBody Map<?, ?> lista(HttpServletRequest request, Integer draw,Integer length, Integer start, Boolean estado)throws IOException, SQLException{
		String search = request.getParameter("search[value]");
		List<Map<String, Object>>	lista=medidaS.listar(start, estado, search,length);
		Long total;
		try {
			total = lista!=null?Long.parseLong(lista.get(0).get("tot").toString()) :0L;
		} catch (Exception e) {
			total = 0L;
		}
		Map<String, Object> Data = DashBoard.listado(draw, start, estado ? 1 : 0, length, search, lista,total);
		return Data;
	}
	@RequestMapping("guardar")
	public @ResponseBody Map<String, Object> guardar(HttpServletRequest request,Medida obj){
		Usuario user=(Usuario)request.getSession().getAttribute("user");
		Map<String,Object> Data=new HashMap<>();
		if(user!=null){
			try {
				Data.put("status", medidaS.adicionar(obj));
			} catch (Exception e) {
				LOGGER.error("error al adicionar "+className+"="+e.toString());
				Data.put("status", false);
			}
		}
		return Data;
	}
	
	@RequestMapping("obtener")
	public @ResponseBody Map<String, Object> obtener(Medida obj){
		Map<String, Object> Data = new HashMap<String, Object>();
		try {
			Data.put("data", medidaS.obtener(obj));
			Data.put("status", true);
		} catch (Exception e) {
			Data.put("status", false);
			LOGGER.error("error al obtener="+e.toString());
		}
		return Data;
	}

	@RequestMapping("actualizar")
	public @ResponseBody Map<String, Object> actualizar(HttpServletRequest request,Medida obj)throws IOException{
		Usuario user=(Usuario)request.getSession().getAttribute("user");
		Map<String, Object> Data = new HashMap<String, Object>();
		if(user!=null){
			try {
				medidaS.modificar(obj);
				Data.put("status", true);
			}catch(Exception e){
				LOGGER.error("error al modificar"+e.toString());
				Data.put("status", false);
			}
		}
		return Data;
	}
	@RequestMapping("eliminar")
	public @ResponseBody Map<String, Object> eliminar(HttpServletRequest request,Medida obj)throws IOException{
		Usuario us=(Usuario)request.getSession().getAttribute("user");
		Map<String, Object> Data = new HashMap<String, Object>();
		if(us!=null){
				try {
					Data.put("status", medidaS.eliminar(obj));
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					LOGGER.error("Error al eliminar "+className+"="+e.toString());
				}
		}
		return Data;
	}
	
}
