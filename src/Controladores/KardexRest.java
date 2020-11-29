
package Controladores;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import Modelos.Sucursal;
import Servicios.AlmacenS;
import Servicios.LaboratorioS;
import Servicios.ProductoS;
import Servicios.SalidaS;
import Utiles.Constantes;

@Controller
@RequestMapping("/kardex/*")
public class KardexRest {

	@Autowired
	private LaboratorioS laboratorioS;
	@Autowired
	private AlmacenS almacenS;
	@Autowired
	private SalidaS salidaS;
	@Autowired
	private ProductoS productoS;
	

	@RequestMapping("gestion")
	public String gestion(HttpServletRequest request, Model model) {
		model.addAttribute("laboratorios", laboratorioS.listar());
		return "kardex/search";
	}
	@RequestMapping("buscar")
	public @ResponseBody Map<String, Object> buscar(HttpServletRequest request,Long codpro){
		Map<String, Object> Data = new HashMap<String, Object>();
		Sucursal sucursal=(Sucursal)request.getSession().getAttribute(Constantes.Sesiones.SUCURSAL);
		try {
			Data.put("producto", productoS.obtener(codpro));
//			Data.put("ventaList", almacenS.buscarKardexVenta(sucursal.getCodsuc(), codpro));
//			Data.put("compraList", almacenS.buscarKardexCompra(sucursal.getCodsuc(), codpro));
//			Data.put("entradaList", salidaS.listarPorProducto(codpro, Constantes.Salida.TIPO_ENTRADA, sucursal.getCodsuc()));
//			Data.put("salidaList", salidaS.listarPorProducto(codpro, Constantes.Salida.TIPO_SALIDA, sucursal.getCodsuc()));
//			Data.put("dataCantidad", almacenS.getSizeProduct(codpro,sucursal.getCodsuc()));
			Data.put("status", true);
		} catch (Exception e) {
			Data.put("status", false);
		}
		return Data;
	}
}
