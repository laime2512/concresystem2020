package Modelos;

import java.util.List;

public class Menu {
	private Integer codm;
	private String nombre;
	private int estado;
	private List<Proceso> procesos;
	public List<Proceso> getProcesos() {
		return procesos;
	}
	public void setProcesos(List<Proceso> procesos) {
		this.procesos = procesos;
	}
	public Integer getCodm() {
		return codm;
	}
	public void setCodm(Integer codm) {
		this.codm = codm;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public int getEstado() {
		return estado;
	}
	public void setEstado(int estado) {
		this.estado = estado;
	}
	
	}
