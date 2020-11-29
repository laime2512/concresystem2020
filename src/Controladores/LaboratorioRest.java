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
import Modelos.Laboratorio;
import Servicios.LaboratorioS;
import Utiles.DashBoard;

@Controller
@RequestMapping("/laboratorio/*")
public class LaboratorioRest {
	
	@Autowired
	LaboratorioS laboratorioS;
	private String className=LaboratorioRest.class.getName();
	private static final Logger LOGGER = Logger.getLogger("LaboratorioRest");
	
	
	@RequestMapping("gestion")
	public String gestion(HttpServletRequest request,Model model){
		return "laboratorio/gestion";
	}
	@RequestMapping("lista")
	public @ResponseBody Map<?, ?> lista(HttpServletRequest request, Integer draw,Integer length, Integer start, Boolean estado)throws IOException, SQLException{
		String search = request.getParameter("search[value]");
		List<Map<String, Object>> lista = laboratorioS.listar(start, estado, search,length);
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
	public @ResponseBody Map<String, Object> guardar(HttpServletRequest request,Laboratorio obj){
		Usuario user=(Usuario)request.getSession().getAttribute("user");
		Map<String,Object> Data=new HashMap<>();
		if(user!=null){
			try {
				Data.put("status", laboratorioS.adicionar(obj));
			} catch (Exception e) {
				LOGGER.error("adicionar "+className+"="+e.toString());
				Data.put("status", false);
			}
		}
		return Data;
	}
	
	@RequestMapping("obtener")
	public @ResponseBody Map<String, Object> obtener(Laboratorio obj){
		Map<String, Object> Data = new HashMap<String, Object>();
		try {
			Data.put("data", laboratorioS.obtener(obj));
			Data.put("status", true);
		} catch (Exception e) {
			Data.put("status", false);
			LOGGER.error("obtener="+e.toString());
		}
		return Data;
	}

	@RequestMapping("actualizar")
	public @ResponseBody Map<String, Object> actualizar(HttpServletRequest request,Laboratorio obj)throws IOException{
		Usuario user=(Usuario)request.getSession().getAttribute("user");
		Map<String, Object> Data = new HashMap<String, Object>();
		if(user!=null){
			try {
				laboratorioS.modificar(obj);
				Data.put("status", true);
			}catch(Exception e){
				LOGGER.info("actualizar:"+e.toString());
				Data.put("status", false);
			}
		}
		return Data;
	}
	@RequestMapping("eliminar")
	public @ResponseBody Map<String, Object> eliminar(HttpServletRequest request,Laboratorio obj)throws IOException{
		Usuario us=(Usuario)request.getSession().getAttribute("user");
		Map<String, Object> Data = new HashMap<String, Object>();
		if(us!=null){
				try {
					Data.put("status", laboratorioS.eliminar(obj));
				} catch (SQLException e) {
					e.printStackTrace();
					LOGGER.error("eliminar: "+e.toString());
				}
		}
		return Data;
	}
	
}
