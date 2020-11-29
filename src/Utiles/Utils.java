package Utiles;

import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.web.multipart.MultipartFile;

/**
 * Utilitarios en cuanto al manejo de archivos, Fechas, subida de archivos y alias de nombres de usuario
 * @author cgutierrez 01/02/2019
 *
 */
public abstract class Utils {
	private static final Logger LOGGER = Logger.getLogger("Utils");
	/**
	 * Obtiene un File en base al request de la sesion, la direccion donde se guardara la imagen y el nombre del archivo con el cual se guardara
	 * @param request
	 * @param direccion
	 * @param nombreArchivo
	 * @return
	 */
	public static File obtenerArchivo(HttpServletRequest request,String direccion,String nombreArchivo) {
		File file=new File(request.getSession().getServletContext().getRealPath(Constantes.DIR_UPLOAD)+direccion+"/"+nombreArchivo);
		return file;
	}
	/**
	 * Devuelve la extension del archivo enviado
	 * @param request
	 * @param archivo
	 * @param repositorio
	 * @param nombreArchivo
	 * @return
	 */
	public static String getExtensionFile(MultipartFile archivo) {
		if(archivo==null)
			return null;
		if(archivo.isEmpty())
			return null;
		return archivo.getOriginalFilename().substring(archivo.getOriginalFilename().lastIndexOf('.'));
	}
	/**
	 * Devuelve si se subio la imagen en tal directorio y nombre especificado
	 * @param request
	 * @param archivo
	 * @param repositorio
	 * @param nombreArchivo
	 * @return Boolean True= se guardo correctamente, False=no se logro guardar
	 */
	public static Boolean SubirArchivo(HttpServletRequest request, MultipartFile archivo,String repositorio,String nombreArchivo){
		if(archivo!=null && archivo.getSize()>0){
			try {
				File direccion=obtenerArchivo(request, repositorio, nombreArchivo);
  	              archivo.transferTo(direccion);
  	    	   	  return true;
			} catch (IllegalStateException|IOException e) {
  	    	   		e.printStackTrace();
  	    	   		LOGGER.error("subirArchivo:"+e.toString());
  	    	}
		}
		return false;
	}
	public static Boolean existeArchivo(MultipartFile archivo) {
		if(archivo == null)
			return false;
		if(archivo.getOriginalFilename().isEmpty())
			return false;
		return !archivo.isEmpty();
	}
	/**
	 * Elimina el archivo que se envia si es que se encuentra en el reporsitorio especificado
	 * @param request
	 * @param repositorio
	 * @param nombreArchivo
	 */
	public static void eliminarArchivo(HttpServletRequest request,String repositorio,String nombreArchivo) {	
		File direccion=obtenerArchivo(request, repositorio, nombreArchivo);
		if(direccion.exists() && direccion.canRead())
			direccion.delete();
	}
	/**
	 * Funcion que devuelve el alias en base al nombre apellido paterno y materno
	 * @param nombres
	 * @param PriApe
	 * @param SegApe
	 * @return
	 */
	public static String generarAlias(String nombres,String PriApe,String SegApe){
		String[]noms=nombres.split(" ");
		String alias="";
		if(noms.length==1)alias+=nombres.substring(0,2);
		else for(int i=0;i<noms.length;i++){
					if(noms[i].length()>2)alias+=noms[i].substring(0,1);	
				}
		if(SegApe!=null&&SegApe.length()>1)alias+=PriApe.substring(0,2)+SegApe.substring(0,2);
		else try{alias+=PriApe.substring(0,4);}catch(Exception e){alias+=PriApe;}
		return alias.toUpperCase();
	}
	
	public static String generateNameArchiveSalida(Integer codSuc,Long codSalida) {
		return "salida_"+codSuc+"_"+codSalida+".json";
	}
	
}

