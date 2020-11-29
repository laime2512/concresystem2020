package Modelos;

public class Medida {
	private Integer codmed;
	private String nombre;
	private boolean estado;
	
	public Integer getCodmed() {
		return codmed;
	}
	public void setCodmed(Integer codmed) {
		this.codmed = codmed;
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
		return "Medida [codmed=" + codmed + ", nombre=" + nombre + ", estado=" + estado + "]";
	}
}
