package Servicios;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import Modelos.Rol;
import Modelos.Sucursal;
import Modelos.Usuario;

@Service
public class SessionImpl implements SessionS {
	@Override
	public Sucursal getSucursal(HttpServletRequest request) {
		Sucursal sucursal = (Sucursal) request.getSession().getAttribute("sucursal");
		return sucursal;
	}
	@Override
	public Usuario getUser(HttpServletRequest request) {
		Usuario user = (Usuario) request.getSession().getAttribute("user");
		return user;
	}
	@Override
	public Rol getRol(HttpServletRequest request) {
		Rol rol = (Rol) request.getSession().getAttribute("rol");
		return rol;
	}
}
