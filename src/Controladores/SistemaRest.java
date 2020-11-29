package Controladores;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import Modelos.Sucursal;
import Servicios.SessionS;
import Servicios.SistemaS;

@Controller
@RequestMapping("/sistema/*")
public class SistemaRest {
	@Autowired
	private SistemaS sistemaS;
	@Autowired
	private SessionS sessionS;
	
	private static Integer CANTIDAD_ALMACEN = 1000;
	
	@RequestMapping("script-sistema")
	public String backup(HttpServletRequest request, Model model) throws Exception {
		return "sistema/script-sistema";
	}
	@RequestMapping("scriptRegisterAlmacen")
	public @ResponseBody Map<String, Object> guardarClaveNueva(HttpServletRequest request) throws IOException,SQLException {
		Map<String, Object> Data = new HashMap<String, Object>();		
		Sucursal sucursal = sessionS.getSucursal(request);
		if(sucursal != null) {
			Data.put("status", true);
			
			Data.put("msgResp", sistemaS.generateProductoAlmacen(CANTIDAD_ALMACEN, sucursal.getCodsuc()));
		}else {
			Data.put("status", false);
			Data.put("msgResp", "Transaccion fallida.");
		}
		return Data;
	}
	
}
