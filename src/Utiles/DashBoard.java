package Utiles;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


//import Servicios.GeneralS;

public class DashBoard {

	public static Map<String, Object> listado( Integer draw, Integer start, Integer estado,int length,String search,List<?> lista,Long tot)throws IOException{
		Map<String, Object> Data = new HashMap<String, Object>();
		String total=tot.toString();
		Data.put("draw", draw);
		Data.put("recordsTotal", total);
		Data.put("data", lista);
		Data.put("recordsFiltered", total);
		return Data;
	}
	public String form_validacion="data-fv-framework=\"bootstrap\" data-fv-excluded=\"disabled\" data-fv-icon-valid=\"glyphicon glyphicon-ok\" data-fv-icon-invalid=\"glyphicon glyphicon-remove\" data-fv-icon-validating=\"glyphicon glyphicon-refresh\"";
	
}
