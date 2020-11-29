package Modelos;

public class Laboratorio {
	private Integer codlab;
	private String nombre;
	private boolean estado;
	
	public Integer getCodlab() {
		return codlab;
	}
	public void setCodlab(Integer codlab) {
		this.codlab = codlab;
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
		return "Laboratorio [codlab=" + codlab + ", nombre=" + nombre + ", estado=" + estado + "]";
	}
}
