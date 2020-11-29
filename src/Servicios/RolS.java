package Servicios;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import Modelos.Rol;
import pagination.DataTableResults;

public interface RolS {
	public DataTableResults<Rol> listar(HttpServletRequest request);
	public List<Rol> listarTodos();
	public Rol obtener(Integer codr);
	public int generarCodigo();
	public boolean adicionar(Rol r);
	public boolean modificar(Rol r);
	public boolean darEstado(int codr,int estado);
	public boolean asignar(int codr,Integer menus[]);
	public List<Rol> listarRolesXlogin(String login);
}
