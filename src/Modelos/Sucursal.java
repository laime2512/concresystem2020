package Modelos;

public class Sucursal {
	private Integer codsuc;
	private String nombre;
	private String telefono;
	private String direccion;
	private Boolean estado;
	
	public String getDireccion() {
		return direccion;
	}
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}
	public Boolean getEstado() {
		return estado;
	}
	public Sucursal() {
		super();
	}
	public Sucursal(Integer codsuc) {
		super();
		this.codsuc = codsuc;
	}
	public Integer getCodsuc() {
		return codsuc;
	}
	public void setCodsuc(Integer codSuc) {
		this.codsuc = codSuc;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getTelefono() {
		return telefono;
	}
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	public boolean isEstado() {
		return estado;
	}
	public void setEstado(Boolean estado) {
		this.estado = estado;
	}
	@Override
	public String toString() {
		return "Sucursal [codsuc=" + codsuc + ", nombre=" + nombre + ", telefono=" + telefono + ", direccion="
				+ direccion + ", estado=" + estado + "]";
	}
	
}
