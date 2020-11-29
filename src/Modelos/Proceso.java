package Modelos;

public class Proceso {
	private Integer codp;
	private String nombre,enlace;
	private int estado;
	
	public Integer getCodp() {
		return codp;
	}
	public void setCodp(Integer codp) {
		this.codp = codp;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getEnlace() {
		return enlace;
	}
	public void setEnlace(String enlace) {
		this.enlace = enlace;
	}
	public int getEstado() {
		return estado;
	}
	public void setEstado(int estado) {
		this.estado = estado;
	}
	
}
