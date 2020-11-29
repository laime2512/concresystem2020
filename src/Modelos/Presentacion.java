package Modelos;

public class Presentacion {
	private Integer codpre;
	private String nombre;
	private boolean estado;
	public Integer getCodpre() {
		return codpre;
	}
	public void setCodpre(Integer codpre) {
		this.codpre = codpre;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public boolean isEstado() {
		return estado;
	}
	public void setEstado(boolean estado) {
		this.estado = estado;
	}
	@Override
	public String toString() {
		return "Presentacion [codpre=" + codpre + ", nombre=" + nombre + ", estado=" + estado + "]";
	}
	
}
