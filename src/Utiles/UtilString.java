package Utiles;

public class UtilString {
	/**
	 * Reemplaza cuendo se tiene el formato {{i}} por las palabras enviadas en el segundo parametro
	 * @param cad
	 * @param vec
	 * @return
	 */
	public static String replaceFormat(String cad,Object... vec) {
		if(vec!=null) {
			for (int i = 0; i < vec.length; i++) {
				cad=cad.replace("{{"+i+"}}", String.valueOf(vec[i]));
			}
		}
		return cad;
	}
	/**
	 * Convierte la primera letra de una cadena en mayuscula
	 * @param palabra
	 * @return
	 */
	public static  String firstUpperLetter(String palabra){
		if(palabra==null)return "";
		String correct = "";
		
		if (!palabra.isEmpty() && !palabra.equals("")) {
		
			for (String string : palabra.toLowerCase().split(" ")) {
			
				correct += string.toUpperCase().charAt(0);
				
				for(int i = 0; i < string.length(); i++){
				
					if(i > 0) correct += string.charAt(i);
				
				}
			
				correct += " ";
			
			}
		
		}
		return correct.trim();
	}
}
