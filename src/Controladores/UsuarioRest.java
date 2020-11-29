package Controladores;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
import org.springframework.web.multipart.MultipartFile;

import Modelos.Sucursal;
import Modelos.Usuario;
import Servicios.AccesoSucursalS;
import Servicios.AccesoUsuarioS;
import Servicios.RolS;
import Servicios.SucursalS;
import Utiles.Constantes;
import Utiles.DashBoard;
import Utiles.Utils;

@Controller
@RequestMapping("/usuario/*")
public class UsuarioRest {

	private static final Logger LOGGER = Logger.getLogger("UsuarioRest");
	@Autowired
	AccesoUsuarioS usuarioS;
	@Autowired
	SucursalS sucursalS;
	@Autowired
	AccesoSucursalS accesoSucursalS;
	@Autowired
	RolS rolS;

	@RequestMapping("cambiarFoto")
	public @ResponseBody Map<String, Object> cambiarFoto(HttpServletRequest request, MultipartFile foto, String ci)
			throws Exception {
		Usuario p = (Usuario) request.getSession().getAttribute("user");
		Map<String, Object> Data = new HashMap<>();

		if (p != null) {
			String nombreArchivo = Constantes.IMG_USER_DEFAULT;
			if (foto != null && foto.getSize() > 0) {
				nombreArchivo = Constantes.DIR_UPLOAD_USER + p.getCodusu() + Utils.getExtensionFile(foto);
				
				if (!p.getFoto().equals(Constantes.IMG_USER_DEFAULT))
					Utils.eliminarArchivo(request, Constantes.DIR_UPLOAD_USER, nombreArchivo);
				if (!Utils.SubirArchivo(request, foto, Constantes.DIR_UPLOAD_USER, nombreArchivo))
					nombreArchivo = Constantes.IMG_USER_DEFAULT;
				Data.put("status", usuarioS.guardarFoto(nombreArchivo, p.getCodusu()));
				Data.put("usuario", usuarioS.obtenerUsuario(p.getCodusu()));
			}
		} else {
			LOGGER.error("sesion cerrada.");
		}
		return Data;
	}

	@RequestMapping("gestion")
	public String gestion(HttpServletRequest request, Model model) throws SQLException{
		model.addAttribute("usuarios", usuarioS.listar());
		model.addAttribute("sucursales", sucursalS.listar());
		model.addAttribute("roles",rolS.listarTodos());
		return "usuario/gestion";
	}

	@RequestMapping("guardar")
	public @ResponseBody Map<String, Object> guardar(HttpServletRequest request, Usuario persona, String ci,
			MultipartFile fotito, Integer[] vsucursales) throws SQLException {
		Usuario user = (Usuario) request.getSession().getAttribute("user");
		Map<String, Object> Data = new HashMap<>();
		if (user != null) {
			persona.setCodusu(usuarioS.generarCodigo());
			try {
				String nombreArchivo = Constantes.IMG_USER_DEFAULT;
				if (fotito != null && fotito.getSize() > 0) {
					nombreArchivo = Constantes.FORMAT_IMG_USER + persona.getCodusu() + Utils.getExtensionFile(fotito);
					if (!nombreArchivo.equals(Constantes.IMG_USER_DEFAULT))
						Utils.eliminarArchivo(request, Constantes.DIR_UPLOAD_USER, nombreArchivo);
					if (!Utils.SubirArchivo(request, fotito, Constantes.DIR_UPLOAD_USER, nombreArchivo))
						nombreArchivo = Constantes.IMG_USER_DEFAULT;
				}
				persona.setFoto(nombreArchivo);
				if (usuarioS.adicionar(persona, ci)) {
					if (vsucursales != null)
						for (int i = 0; i < vsucursales.length; i++) {
							accesoSucursalS.adicionar(persona, new Sucursal(vsucursales[i]));
						}
					Data.put("status", true);
				} else {
					Data.put("status", false);
				}
			} catch (Exception e) {
				LOGGER.error("error al adicionar" + e.toString());
				Data.put("status", false);
			}
		}
		return Data;
	}
	@RequestMapping("lista")
	public @ResponseBody Map<?, ?> lista(HttpServletRequest request, Integer draw, int length, Integer start,
			int estado, String tipo) throws IOException,SQLException {
		String search = request.getParameter("search[value]");
		List<Map<String,Object>> lista = usuarioS.listar(start, estado, search, tipo, length);
		Long total;
		try {
			total = lista!=null?Long.parseLong(lista.get(0).get("tot").toString()) :0L;
		} catch (Exception e) {
			total = 0L;
		}
		Map<String, Object> Data = DashBoard.listado(draw, start, estado, length, search, lista,total);
		return Data;
	}

	@RequestMapping("obtener")
	public @ResponseBody Map<String, Object> obtener(HttpServletRequest request, Long codusu) {
		Map<String, Object> Data = new HashMap<String, Object>();
		try {
			Map<String, Object> us = usuarioS.obtenerXcodusu(codusu);
			Data.put("data", us);
			Usuario usuario = new Usuario();
			usuario.setCodusu(codusu);
			Data.put("sucursales", accesoSucursalS.obtenerSucursalesPorUsuario(usuario));
			Data.put("roles", usuarioS.obtenerRoles(codusu));
			Data.put("status", true);
		} catch (Exception e) {
			Data.put("status", false);
			LOGGER.error("error al obtener=" + e.toString());
		}
		return Data;
	}

	@RequestMapping("actualizar")
	public @ResponseBody Map<String, Object> actualizar(HttpServletRequest request, Usuario per,
			MultipartFile fotito, Integer vsucursales[]) throws IOException, SQLException {
		Map<String, Object> Data = new HashMap<String, Object>();
		Map<String, Object> usuario = usuarioS.obtenerXcodusu(per.getCodusu());
		String nombreArchivo;
		if (fotito != null && fotito.getSize() > 0) {
			nombreArchivo = Constantes.FORMAT_IMG_USER + usuario.get("foto").toString()
					+ Utils.getExtensionFile(fotito);
			if (!usuario.get("foto").toString().equals(Constantes.IMG_USER_DEFAULT))
				Utils.eliminarArchivo(request, Constantes.DIR_UPLOAD_USER, nombreArchivo);
			if (!Utils.SubirArchivo(request, fotito, Constantes.DIR_UPLOAD_USER, nombreArchivo))
				nombreArchivo = Constantes.IMG_USER_DEFAULT;
		} else
			nombreArchivo = Constantes.IMG_USER_DEFAULT;
		per.setFoto(nombreArchivo);
		if (usuarioS.modificar(per)) {
			if (vsucursales != null) {
				if (vsucursales.length > 0) {
					String vectorToString = "(";
					for (int i = 0; i < vsucursales.length; i++) {
						Sucursal sucursal = new Sucursal(vsucursales[i]);
						vectorToString += vsucursales[i] + ",";
						if (!accesoSucursalS.existe(per, sucursal)) {
							accesoSucursalS.adicionar(per, sucursal);
							
						}
					}
					vectorToString = vectorToString.substring(0, vectorToString.length() - 1) + ")";
					accesoSucursalS.eliminarOtros(per, vectorToString);
				}
			}
			Data.put("status", true);
		} else {
			Data.put("status", false);
		}
		return Data;
	}

	@RequestMapping("eliminar")
	public @ResponseBody Map<String, Object> eliminar(HttpServletRequest request, Model model, Long codusu)
			throws IOException,SQLException{
		Usuario us = (Usuario) request.getSession().getAttribute("user");
		Map<String, Object> Data = new HashMap<String, Object>();
		if (us.getCodusu() == codusu)
			Data.put("status", false);
		else {
			Map<String, Object> usuario = usuarioS.obtenerXcodusu(codusu);
			if (!usuario.get("foto").toString().equals(Constantes.IMG_USER_DEFAULT))
				Utils.eliminarArchivo(request, Constantes.FORMAT_IMG_USER, usuario.get("foto").toString());
			Data.put("status", usuarioS.eliminar(codusu));
		}
		return Data;
	}

	@RequestMapping("habilitar")
	public @ResponseBody Map<String, Object> habilitar(HttpServletRequest request, Model model, Long codusu)
			throws IOException,SQLException {
		Map<String, Object> Data = new HashMap<String, Object>();
		if (usuarioS.habilitar(codusu))
			Data.put("status", true);
		else
			Data.put("status", false);
		return Data;
	}

	@RequestMapping("asignar")
	public @ResponseBody Map<String, Object> asignar(HttpServletRequest request, Model model, Long codusu, String login,
			String passwd) throws IOException,SQLException {
		Map<String, Object> Data = new HashMap<String, Object>();
			Data.put("status", usuarioS.asignar(codusu, login, passwd));
		return Data;
	}
	@RequestMapping("asignarRoles")
	public @ResponseBody Map<String, Object> asignarRoles(HttpServletRequest request, Model model, String login, Integer roles[]) throws IOException,SQLException {
		Map<String, Object> Data = new HashMap<String, Object>();
		if(login ==null) {
			Data.put("status", false);
			Data.put("msg","El usuario no tiene claves de acceso, adicionar clave.");
		}else			
			Data.put("status", usuarioS.asignarRoles(login, roles));
		return Data;
	}

	@RequestMapping("reasignar")
	public @ResponseBody Map<String, Object> reasignar(HttpServletRequest request, Model model, Long codusu,
			String login, String passwd1) throws IOException,SQLException{
		Map<String, Object> Data = new HashMap<String, Object>();
		if (usuarioS.reasignar(codusu, login, passwd1))
			Data.put("status", true);
		else
			Data.put("status", false);
		return Data;
	}
	
	@RequestMapping("acceso")
	public String datoClave(HttpServletRequest request, Model model) throws Exception {
		Usuario user = (Usuario) request.getSession().getAttribute("user");
		if(user !=null) {
			model.addAttribute("xuser", user);
		}
		return "usuario/acceso";
	}
	@RequestMapping("guardarClaveNueva")
	public @ResponseBody Map<String, Object> guardarClaveNueva(HttpServletRequest request, Model model,String login, Long codusu, String claveAnterior,
			String claveNueva,String claveRepetida) throws IOException,SQLException {
		Map<String, Object> Data = new HashMap<String, Object>();
		if(claveNueva.equals(claveRepetida))
			Data.put("status", usuarioS.reasignar(codusu, login, claveNueva));
		else {
			Data.put("status", false);
			Data.put("msg", "Las claves no coinciden. Vuelva a intentarlo.");
		}
		return Data;
	}
	@RequestMapping("backup")
	public String backup(HttpServletRequest request, Model model) throws Exception {
		
		return "backup/acceso";
	}
	@RequestMapping("generarBackup")
	public @ResponseBody Map<String, Object> guardarClaveNueva(HttpServletRequest request) throws IOException,SQLException {
		Map<String, Object> Data = new HashMap<String, Object>();
		String directoryBackup = backupPGSQL();		
		if(!directoryBackup.isEmpty()) {
			Usuario user = (Usuario) request.getSession().getAttribute("user");
			usuarioS.adicionarBackup(directoryBackup.substring(directoryBackup.lastIndexOf("\\")+1), user.getNombre()+" "+user.getAp());
			Data.put("status", true);
			Data.put("lista", usuarioS.listarBackup());
			Data.put("msgResp", "Se realizo con exito el backup en el directorio: "+ directoryBackup);
		}else {
			Data.put("status", false);
			Data.put("msgResp", "Las claves no coinciden. Vuelva a intentarlo.");
		}
		return Data;
	}
	public String backupPGSQL(){
		try{
			
			new java.io.File(System.getProperty("user.home"), "backups").mkdirs();
			String directoryBackup = new java.io.File(System.getProperty("user.home"), "backups").getPath()+"\\";
			//Path to the place we store our backup
			String rutaCT = Constantes.Backup.PG_DUMP;
			//PostgreSQL variables
			String IP = Constantes.Backup.IP;
			String user = Constantes.Backup.USER;
			String dbase = Constantes.Backup.DATABASE;
			String password = Constantes.Backup.PASSWORD;
			Process p;
			ProcessBuilder pb;
//			InputStreamReader reader;
//			BufferedReader buf_reader;
//			String line;
			//We build a string with today's date (This will be the backup's filename)
			java.util.TimeZone zonah = java.util.TimeZone.getTimeZone("GMT+1");
			java.util.Calendar Calendario = java.util.GregorianCalendar.getInstance( zonah, new java.util.Locale("es"));
			java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyyMMdd_hhmmss");
			StringBuffer date = new StringBuffer();
			String dateNow =df.format(Calendario.getTime());
			date.append(dateNow);
			
			directoryBackup += dateNow + ".backup";
			LOGGER.error("backupPGSQL: "+directoryBackup);
			
			java.io.File file = new java.io.File(rutaCT);
			// We test if the path to our programs exists
			if(file.exists()){
				// We then test if the file we're going to generate exist too. If so we will delete it
				StringBuffer fechafile = new StringBuffer();
				fechafile.append(rutaCT);
				fechafile.append(date.toString());
				fechafile.append(".backup");
				java.io.File ficherofile = new java.io.File(fechafile.toString());
				
				if(ficherofile.exists()){
					ficherofile.delete();
				}
//				Runtime r =Runtime.getRuntime();
				pb = new ProcessBuilder(rutaCT + "pg_dump.exe", "-f", directoryBackup,
						"-F", "c", "-Z", "9", "-v", "-o", "-h",IP, "-U", user, dbase);
				pb.environment().put("PGPASSWORD", password);
				pb.redirectErrorStream(true);
				p = pb.start();
				try{
					InputStream is = p.getInputStream();
					InputStreamReader isr = new InputStreamReader(is);
					BufferedReader br = new BufferedReader(isr);
					String ll;
					while ((ll = br.readLine()) != null) {
						LOGGER.info(ll);
					}
					return directoryBackup;
				} catch (IOException e) {
					LOGGER.error("backupPGSQL"+e.toString());
					return "";
				}
			}
			return "";
		} catch(IOException x) {
			LOGGER.error("Could not invoke browser, command=");
			LOGGER.error("Caught: " + x.getMessage());
			return "";
		}
	}
}
