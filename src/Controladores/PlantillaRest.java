/**
 * 
 */
package Controladores;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author CARLOS
 * Controla las diferentes plantillas front que se utilizaran en el sistema
 * Estandares para el front del sistema
 */
@Controller
@RequestMapping("template")
public class PlantillaRest {
	@GetMapping("form")
	public String temmplateForm() {
		return "plantillas/system-form";
	}
}
