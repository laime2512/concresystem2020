package Modelos;

public class TipoMueble {
	private Integer codtimu;
	private String nombre;
	private boolean estado;
	public Integer getCodtimu() {
		return codtimu;
	}
	public void setCodtimu(Integer codtimu) {
		this.codtimu = codtimu;
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
		return "TipoMueble [codTiMu=" + codtimu + ", nombre=" + nombre + ", estado=" + estado + "]";
	}
}
