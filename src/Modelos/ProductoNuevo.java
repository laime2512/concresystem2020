package Modelos;

public class ProductoNuevo{
	private Long cod_pro_nuevo,codusu;
	private String nombre,descripcion,xusuario;
	private Boolean estado;
	public String getXusuario() {
		return xusuario;
	}
	public void setXusuario(String xusuario) {
		this.xusuario = xusuario;
	}
	public Long getCod_pro_nuevo() {
		return cod_pro_nuevo;
	}
	public void setCod_pro_nuevo(Long cod_pro_nuevo) {
		this.cod_pro_nuevo = cod_pro_nuevo;
	}
	public Long getCodusu() {
		return codusu;
	}
	public void setCodusu(Long codusu) {
		this.codusu = codusu;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public Boolean getEstado() {
		return estado;
	}
	public void setEstado(Boolean estado) {
		this.estado = estado;
	}
}
