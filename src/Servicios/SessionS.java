package Servicios;

import javax.servlet.http.HttpServletRequest;

import Modelos.Rol;
import Modelos.Sucursal;
import Modelos.Usuario;

public interface SessionS {

	Sucursal getSucursal(HttpServletRequest request);

	Usuario getUser(HttpServletRequest request);
	
	public Rol getRol(HttpServletRequest request);
}