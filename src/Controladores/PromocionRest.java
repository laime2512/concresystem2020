package Controladores;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import Modelos.Promocion;
import Modelos.Sucursal;
import Modelos.Usuario;
import Servicios.ProductoS;
import Servicios.PromoS;
import Utiles.DashBoard;
import Utiles.Fechas;
import Utiles.GeneradorReportes;
import Wrap.PromoVec;

@Controller
@RequestMapping("/promocion/*")
public class PromocionRest {

	@Autowired
	PromoS promoS;

	@Autowired
	ProductoS productoS;
	
	private static final Logger LOGGER=Logger.getLogger("PromocionRest");

	@RequestMapping("gestion")
	public String gestion() {
		return "promocion/gestion";
	}

	@RequestMapping("lista")
	public @ResponseBody Map<?, ?> lista(HttpServletRequest request, Integer draw, int length, Integer start,
			boolean estado) throws IOException {
		String search = request.getParameter("search[value]");
		List<Map<String, Object>> lista = promoS.listar(start, estado, search, length);
		Long total;
		try {
			total = lista!=null?Long.parseLong(lista.get(0).get("tot").toString()) :0L;
		} catch (Exception e) {
			total = 0L;
		}
		Map<String, Object> Data = DashBoard.listado(draw, start, 1, length, search, lista,total);
		return Data;
	}
	@RequestMapping("adicionar")
	public String adicionar(Model model, HttpServletRequest request) {
		model.addAttribute("productos", productoS.listarTodos());
		model.addAttribute("fecha", Fechas.obtenerFecha("dd/MM/yy"));
		return "promocion/adicionar";
	}
	@RequestMapping("guardar")
	public @ResponseBody Map<String, Object> guardar(HttpServletRequest request, Promocion promocion,PromoVec promoVec) {
		Usuario user = (Usuario) request.getSession().getAttribute("user");
		@SuppressWarnings("deprecation")
		int gestion =new Date().getYear();
		promocion.setCodusu(user.getCodusu());
		promocion.setGestion(gestion);
		
		Map<String, Object> Data = new HashMap<>();
		if (user != null) {
			try {
				Long codpromo=promoS.adicionar(promocion,promoVec);
				Data.put("cod", codpromo);
				Data.put("status", codpromo>0);
			} catch (Exception e) {
				LOGGER.error("error al adicionar promocion" + e.toString());
				Data.put("status", false);
			}
		}
		return Data;
	}

	@RequestMapping("obtener")
	public @ResponseBody Map<String, Object> obtener(HttpServletRequest request, Long codpromocion) {
		Map<String, Object> Data = new HashMap<String, Object>();
		try {
			Promocion promocion=promoS.obtener(codpromocion);
			Data.put("data", promocion);
			Data.put("detalles", promoS.obtenerDetalles(codpromocion));
			Data.put("status", true);
		} catch (Exception e) {
			Data.put("status", false);
			LOGGER.error("error al obtener=" + e.toString());
		}
		return Data;
	}

	@RequestMapping("eliminar")
	public @ResponseBody Map<String, Object> eliminar(HttpServletRequest request, Model model, Long codpromo)
			throws IOException {
		Usuario us = (Usuario) request.getSession().getAttribute("user");
		Map<String, Object> Data = new HashMap<String, Object>();
		if (us != null) {
			Data.put("status", promoS.eliminar(codpromo));
		}
		return Data;
	}

	@Autowired
	DataSource dataSource;

	@RequestMapping("ver")
	public void ver(HttpServletRequest request, HttpServletResponse response, Long codpromo) throws IOException, ParseException, SQLException {
		String nombre = "reporte de promocion", tipo = "pdf", estado = "inline";
		Map<String, Object> parametros = new HashMap<String, Object>();
		String url = "/reportes/promocion.jasper";
		Promocion obj = promoS.obtener(codpromo);
		parametros.put("codpromo", codpromo);
		parametros.put("xtitulo",obj.getTitulo());
		parametros.put("xfini",obj.getXfini());
		parametros.put("xffin",obj.getXffin());
		parametros.put("xdescripcion",obj.getDescripcion());
		parametros.put("xusuario",obj.getXusuario());
		obtenerEncabezado(request, parametros);
		GeneradorReportes g = new GeneradorReportes();
		try {
			g.generarReporte(response, getClass().getResource(url), tipo, parametros, dataSource.getConnection(),
					nombre, estado);
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("ver: "+e.toString());
		}
	}
	public void obtenerEncabezado(HttpServletRequest request,Map<String, Object> param) {
		Usuario usuario=(Usuario)request.getSession().getAttribute("user");
		if(usuario!=null) {
			Sucursal sucursal=(Sucursal)request.getSession().getAttribute("sucursal");
			param.put("empresa","MIAFARMA");
			param.put("usuario",usuario.getNombre()+" "+usuario.getAp()+(usuario.getAm()!=null?"":(" "+usuario.getAm())));
			if(sucursal!=null) {
				param.put("sucursal", sucursal.getNombre());
				param.put("telefono", sucursal.getTelefono());
				param.put("direccion", sucursal.getDireccion());
			}
		}
	}
	@RequestMapping("obtenerPromoActual")
	public @ResponseBody Map<String, Object> obtenerPromoActual(HttpServletRequest request) {
		Map<String, Object> Data = new HashMap<String, Object>();
		try {
			List<Promocion> promocion=promoS.obtenerPromocionesActuales();
			Data.put("data", promocion);
			Data.put("status", true);
		} catch (Exception e) {
			Data.put("status", false);
			LOGGER.error("error al obtener=" + e.toString());
		}
		return Data;
	}
}
