package Modelos;

import java.io.Serializable;

public class Rol implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer Codr,estado;
	private String nombre;
	private Integer menus;
	private Boolean acceso_venta;
	public Integer getMenus() {
		return menus;
	}
	public void setMenus(Integer menus) {
		this.menus = menus;
	}
	public Boolean isAcceso_venta() {
		return acceso_venta;
	}
	public void setAcceso_venta(Boolean acceso_venta) {
		this.acceso_venta = acceso_venta;
	}
	public Integer getCodr() {
		return Codr;
	}
	public void setCodr(Integer codr) {
		Codr = codr;
	}
	public Integer getEstado() {
		return estado;
	}
	public void setEstado(Integer estado) {
		this.estado = estado;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
}
