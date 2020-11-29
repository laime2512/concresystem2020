
package Controladores;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import Modelos.Producto;
import Modelos.Usuario;
import Modelos.ViewAlmacen;
import Servicios.AreaS;
import Servicios.CategoriaS;
import Servicios.LaboratorioS;
import Servicios.MedidaS;
import Servicios.MuebleS;
import Servicios.PresentacionS;
import Servicios.ProductoS;
import Servicios.TipoS;
import Utiles.Constantes;
import Utiles.Utils;
import enumeraciones.TipoUsuarioE;
import pagination.DataTableResults;

@Controller
@RequestMapping("/producto/*")
public class ProductoRest {

	@Autowired
	private ProductoS productoS;
	@Autowired
	private TipoS tipoS;
	@Autowired
	private CategoriaS categoriaS;
	@Autowired
	private AreaS areaS;
	@Autowired
	private MedidaS medidaS;
	@Autowired
	private LaboratorioS laboratorioS;
	@Autowired
	private PresentacionS presentacionS;
	@Autowired
	private MuebleS muebleS;

	private final Logger LOGGER = Logger.getLogger(ProductoRest.class.getName());

	@RequestMapping("cambiarFoto")
	public @ResponseBody Map<String, Object> cambiarFoto(HttpServletRequest request, Long idproducto, MultipartFile foto)
			throws Exception {
		Producto p = (Producto) request.getSession().getAttribute("user");
		Map<String, Object> Data = new HashMap<>();
		if (p != null) {
			if (foto != null && foto.getSize() > 0) {
				String nombreArchivo = Constantes.FORMAT_IMG_PRODUCTO + idproducto + Utils.getExtensionFile(foto);
				try {
					if (!nombreArchivo.equals(Constantes.IMG_PRODUCTO_DEFAULT))
						Utils.eliminarArchivo(request, Constantes.DIR_UPLOAD_PRODUCTOS, nombreArchivo);// Elimina archivo si existe
					if (!Utils.SubirArchivo(request, foto, Constantes.DIR_UPLOAD_PRODUCTOS, nombreArchivo))
						nombreArchivo = Constantes.IMG_PRODUCTO_DEFAULT;
				} catch (IllegalStateException e) {
					e.printStackTrace();
					LOGGER.error("cambiarFoto: "+e.toString());
				}
				Data.put("status", productoS.guardarFoto(nombreArchivo, p.getCodpro()));
				Data.put("Producto", productoS.obtener(idproducto));
			}
		} else {
			LOGGER.error("sesion cerrada.");
		}
		return Data;
	}

	@RequestMapping("gestion")
	public String gestion(HttpServletRequest request, Model model) {
		Usuario usuario = (Usuario) request.getSession().getAttribute("user");
		model.addAttribute("CON_PERMISO",usuario.getTipoper().equals(TipoUsuarioE.ADMINISTRATIVO.value));
		model.addAttribute("categorias", categoriaS.listar_todos());
		model.addAttribute("tipos", tipoS.listar_todos());
		model.addAttribute("areas", areaS.listar());
		model.addAttribute("medidas", medidaS.listar());
		model.addAttribute("laboratorios", laboratorioS.listar());
		model.addAttribute("presentaciones",presentacionS.listar());
		model.addAttribute("muebles",muebleS.listar());
		model.addAttribute("RUTA_PRODUCTOS", Constantes.Archivo.RUTA_PRODUCTO);
		model.addAttribute("IMG_DEFAULT", Constantes.IMG_PRODUCTO_DEFAULT);
		return "producto/gestion";
	}
	@RequestMapping("gestionIncompletos")
	public String gestionIncompletos(HttpServletRequest request, Model model) {
		model.addAttribute("RUTA_PRODUCTOS", Constantes.Archivo.RUTA_PRODUCTO);
		model.addAttribute("IMG_DEFAULT", Constantes.IMG_PRODUCTO_DEFAULT);
		return "producto/gestion-incompletos";
	}
	@RequestMapping("adicionar")
	public String adicionar2(HttpServletRequest request, Model model,Long codpro,Boolean esIncompleto) {
		model.addAttribute("urlVolver","../producto/gestion"+(esIncompleto?"Incompletos":""));
		model.addAttribute("isUpdate", codpro!=null);
		model.addAttribute("codpro", codpro);
		model.addAttribute("categorias", categoriaS.listar_todos());
		model.addAttribute("tipos", tipoS.listar_todos());
		model.addAttribute("areas", areaS.listar());
		model.addAttribute("medidas", medidaS.listar());
		model.addAttribute("laboratorios", laboratorioS.listar());
		model.addAttribute("presentaciones",presentacionS.listar());
		model.addAttribute("muebles",muebleS.listar());
		model.addAttribute("RUTA_PRODUCTOS", Constantes.Archivo.RUTA_PRODUCTO);
		model.addAttribute("IMG_DEFAULT", Constantes.IMG_PRODUCTO_DEFAULT);
		return "producto/adicionar";
	}

	@RequestMapping("lista")
	public @ResponseBody DataTableResults<Producto> lista(HttpServletRequest request) throws IOException {
		return productoS.listar(request);
	}
	@RequestMapping("listaIncompletos")
	public @ResponseBody DataTableResults<Producto> listaIncompletos(HttpServletRequest request) throws IOException {
		return productoS.listarIncompletos(request);
	}
	@RequestMapping("guardar")
	public @ResponseBody Map<String, Object> guardar(HttpServletRequest request, Producto c, MultipartFile fotito) {
		Usuario user = (Usuario) request.getSession().getAttribute("user");
		Map<String, Object> Data = new HashMap<>();
		if (user != null) {
			String nombreArchivo;
			c.setCodpro(productoS.generarCodigo());
			if (Utils.existeArchivo(fotito)) {
				nombreArchivo = Constantes.FORMAT_IMG_PRODUCTO + c.getCodpro() + Utils.getExtensionFile(fotito);
				if (!Utils.SubirArchivo(request, fotito, Constantes.DIR_UPLOAD_PRODUCTOS, nombreArchivo))
					nombreArchivo = Constantes.IMG_PRODUCTO_DEFAULT;
			} else
				nombreArchivo = Constantes.IMG_PRODUCTO_DEFAULT;
			c.setFoto(nombreArchivo);
			Data.put("status", productoS.adicionar(c));

		}
		return Data;
	}
	@RequestMapping("guardar2")
	public @ResponseBody Map<String, Object> guardar2(HttpServletRequest request, Producto c, MultipartFile fotito) throws IOException {
		Usuario user = (Usuario) request.getSession().getAttribute("user");
		Map<String, Object> Data = new HashMap<>();
		if (user != null) {
			String nombreArchivo;
			if(c.getCodpro() != null) {//Actualizacion de producto
				Producto producto = productoS.obtener(c.getCodpro());
				if (Utils.existeArchivo(fotito)) {
					nombreArchivo = Constantes.FORMAT_IMG_PRODUCTO + producto.getCodpro() + Utils.getExtensionFile(fotito);
					Utils.eliminarArchivo(request, Constantes.DIR_UPLOAD_PRODUCTOS, nombreArchivo);
					String imgActual = producto.getFoto();
					if (!imgActual.equals(Constantes.IMG_PRODUCTO_DEFAULT))// Elimina imagen si es diferente a la imagen por defecto
						Utils.eliminarArchivo(request, Constantes.DIR_UPLOAD_PRODUCTOS, producto.getFoto());
					if (!Utils.SubirArchivo(request, fotito, Constantes.DIR_UPLOAD_PRODUCTOS, nombreArchivo))
						nombreArchivo = Constantes.IMG_PRODUCTO_DEFAULT;
				} else
					nombreArchivo = producto.getFoto();
				c.setFoto(nombreArchivo);
				Data.put("status", productoS.modificar(c));
			}else {//Adicion de producto
				c.setCodpro(productoS.generarCodigo());
				if (Utils.existeArchivo(fotito)) {
					nombreArchivo = Constantes.FORMAT_IMG_PRODUCTO + c.getCodpro() + Utils.getExtensionFile(fotito);
					if (!nombreArchivo.equals(Constantes.IMG_PRODUCTO_DEFAULT))
						Utils.eliminarArchivo(request, Constantes.DIR_UPLOAD_PRODUCTOS, nombreArchivo);
					if (!Utils.SubirArchivo(request, fotito, Constantes.DIR_UPLOAD_PRODUCTOS, nombreArchivo)) {
						nombreArchivo = Constantes.IMG_PRODUCTO_DEFAULT;
					}
				} else
					nombreArchivo = Constantes.IMG_PRODUCTO_DEFAULT;
				c.setFoto(nombreArchivo);
				Data.put("status", productoS.adicionar(c));
			}
		}
		return Data;
	}

	@RequestMapping("obtener")
	public @ResponseBody Map<String, Object> obtener(HttpServletRequest request, Long codpro) {
		Map<String, Object> Data = new HashMap<String, Object>();
		try {
			Producto dato = productoS.obtener(codpro);
			Data.put("data", dato);
			Data.put("status", true);
		} catch (Exception e) {
			Data.put("status", false);
			LOGGER.error("obtener= " + e.toString());
		}
		return Data;
	}

	@RequestMapping("eliminar")
	public @ResponseBody Map<String, Object> eliminar(HttpServletRequest request, Model model, Long codpro)
			throws IOException {
		Usuario us = (Usuario) request.getSession().getAttribute("user");
		Map<String, Object> Data = new HashMap<String, Object>();
		if (us != null) {
			Producto obj = productoS.obtener(codpro);
			Data.put("status", productoS.eliminar(codpro));
			if(obj!=null) {
				String fotoActual = obj.getFoto();
				if (!fotoActual.equals(Constantes.IMG_PRODUCTO_DEFAULT))
					Utils.eliminarArchivo(request, Constantes.DIR_UPLOAD_PRODUCTOS, fotoActual);
			}
		}
		return Data;
	}

	@RequestMapping("habilitar")
	public @ResponseBody Map<String, Object> habilitar(HttpServletRequest request, Model model, Long idproducto)
			throws IOException {
		Map<String, Object> Data = new HashMap<String, Object>();
		Data.put("status", productoS.darestado(idproducto, 1));
		return Data;
	}
	@RequestMapping("listaKardex")
	public @ResponseBody DataTableResults<ViewAlmacen> listaKardex(HttpServletRequest request,Long codpro, String nombre, String codigo,  Integer codlab) throws IOException {
		return productoS.listPorKardex(request, codpro, nombre, codigo, codlab);
	}
	@GetMapping("listAll")
	public @ResponseBody Map<String, Object> listAll(HttpServletRequest request){
		Map<String, Object> Data = new HashMap<String, Object>();
		try { 
			Data.put("list", productoS.listarTodos());
			Data.put("status", true);
		} catch (Exception e) {
			LOGGER.error("listAll"+e.getMessage());
			Data.put("status",false);
		}
		return Data;
	}
}
