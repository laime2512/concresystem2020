package Utiles;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import Modelos.Salida;
 
public class UtilJson {
	public static void saveFileJson(String ruta,String nameArchive,Salida value)throws IOException{
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Writer writer = new FileWriter(ruta+ "/" +nameArchive);
        writer.write(gson.toJson(value));
        writer.close();
	}
	public static List<Salida> readFileJson(HttpServletRequest request,String nameArchive)throws IOException{
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		Reader reader = new FileReader(request.getSession().getServletContext().getRealPath(Constantes.Archivo.RUTA_SALIDA)+ "/"+ nameArchive);
        Type type = new TypeToken<Collection<Salida>>() {
        }.getType();
        
		List<Salida> result = gson.fromJson(reader, type);
		reader.close();
		return result;
	}
}