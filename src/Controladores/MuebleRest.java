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
import Modelos.Mueble;
import Modelos.Posicion;
import Servicios.MuebleS;
import Servicios.PosicionS;
import Servicios.TipoMuebleS;
import Utiles.DashBoard;

@Controller
@RequestMapping("/mueble/*")
public class MuebleRest {
	
	@Autowired
	MuebleS muebleS;
	@Autowired
	TipoMuebleS tipoMuebleS;
	@Autowired
	PosicionS posicionS;
	private String className=MuebleRest.class.getName();
	private static final Logger LOGGER = Logger.getLogger("MuebleRest");
	
	
	@RequestMapping("gestion")
	public String gestion(HttpServletRequest request,Model model){
		model.addAttribute("tipos",tipoMuebleS.listar());
		return "mueble/gestion";
	}
	@RequestMapping("lista")
	public @ResponseBody Map<?, ?> lista(HttpServletRequest request, Integer draw,Integer length, Integer start, Integer estado)throws IOException, SQLException{
		String search = request.getParameter("search[value]");
		List<Map<String, Object>> lista=muebleS.listar(start, estado, search,length);
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
	public @ResponseBody Map<String, Object> guardar(HttpServletRequest request,Mueble obj,String posiciones[]){
		Usuario user=(Usuario)request.getSession().getAttribute("user");
		Map<String,Object> Data=new HashMap<>();
		if(user!=null){
			try {
				Integer codMueble=muebleS.adicionar(obj);
				if(posiciones!=null) {
					for (int i = 0; i < posiciones.length; i++) {
						Posicion posicion=new Posicion();
						posicion.setCodmueble(codMueble);
						posicion.setNombre(posiciones[i]);
						posicionS.adicionar(posicion);
					}
				}
				
				Data.put("status", true);
			} catch (Exception e) {
				LOGGER.error("error al adicionar "+className+"="+e.toString());
				Data.put("status", false);
			}
		}
		return Data;
	}
	@RequestMapping("listarTodos")
	public @ResponseBody Map<String, Object> listarTodos(){
		Map<String, Object> Data = new HashMap<String, Object>();
		try {
			Data.put("data", muebleS.listar());
			Data.put("status", true);
		} catch (Exception e) {
			Data.put("status", false);
			LOGGER.error("error al obtener="+e.toString());
		}
		return Data;
	}
	@RequestMapping("obtener")
	public @ResponseBody Map<String, Object> obtener(Mueble obj){
		Map<String, Object> Data = new HashMap<String, Object>();
		try {
			Data.put("data", muebleS.obtener(obj));
			Data.put("posiciones",posicionS.listarPosicionesPorMueble(obj.getCodmueble()));
			Data.put("status", true);
		} catch (Exception e) {
			Data.put("status", false);
			LOGGER.error("error al obtener="+e.toString());
		}
		return Data;
	}
	@RequestMapping("obtenerPorTipoMueble")
	public @ResponseBody Map<String, Object> obtenerPorTipoMueble(Integer codtimu){
		Map<String, Object> Data = new HashMap<String, Object>();
		try {
			Data.put("data", muebleS.obtenerPorTipoMueble(codtimu));
			Data.put("status", true);
		} catch (Exception e) {
			Data.put("status", false);
			LOGGER.error("error al obtener por tipo de mueble="+e.toString());
		}
		return Data;
	}

	@RequestMapping("actualizar")
	public @ResponseBody Map<String, Object> actualizar(HttpServletRequest request,Mueble obj,String posiciones[])throws IOException{
		Usuario user=(Usuario)request.getSession().getAttribute("user");
		Map<String, Object> Data = new HashMap<String, Object>();
		if(user!=null){
			try {
				muebleS.modificar(obj);
				posicionS.eliminarTodoPorMueble(obj.getCodmueble());
				if(posiciones!=null) {
					for (int i = 0; i < posiciones.length; i++) {
						if(posiciones[i]!=null)
							if(!posiciones[i].equals("")) {
								Posicion posicion=new Posicion();
								posicion.setCodmueble(obj.getCodmueble());
								posicion.setNombre(posiciones[i]);
								posicionS.adicionar(posicion);
							}
					}
				}
				Data.put("status", true);
			}catch(Exception e){
				LOGGER.error("error al modificar"+e.toString());
				Data.put("status", false);
			}
		}
		return Data;
	}
	@RequestMapping("eliminar")
	public @ResponseBody Map<String, Object> eliminar(HttpServletRequest request,Mueble obj)throws IOException{
		Usuario us=(Usuario)request.getSession().getAttribute("user");
		Map<String, Object> Data = new HashMap<String, Object>();
		if(us!=null){
				try {
					Data.put("status", muebleS.eliminar(obj));
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					LOGGER.error("Error al eliminar "+className+"="+e.toString());
				}
		}
		return Data;
	}
	
}
