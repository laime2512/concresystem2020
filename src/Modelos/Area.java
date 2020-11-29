package Modelos;

public class Area {
	private Integer codare;
	private String nombre;
	private boolean estado;
	
	public Integer getCodare() {
		return codare;
	}
	public void setCodare(Integer codare) {
		this.codare = codare;
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
		return "Area [codare=" + codare + ", nombre=" + nombre + ", estado=" + estado + "]";
	}
}
