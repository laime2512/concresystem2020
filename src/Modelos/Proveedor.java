package Modelos;


public class Proveedor {
private Long codproveedor;
private String nombre;
private Integer nit;
private String direccion;
private String telefono;
private Integer estado;
public Long getCodproveedor() {
	return codproveedor;
}
public void setCodproveedor(Long codproveedor) {
	this.codproveedor = codproveedor;
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
public Integer getNit() {
	return nit;
}
public void setNit(Integer nit) {
	this.nit = nit;
}
public String getDireccion() {
	return direccion;
}
public void setDireccion(String direccion) {
	this.direccion = direccion;
}
public String getTelefono() {
	return telefono;
}
public void setTelefono(String telefono) {
	this.telefono = telefono;
}
public int getEstado() {
	return estado;
}
public void setEstado(int estado) {
	this.estado = estado;
}
public String toString() {
	return nombre+" "+direccion+" "+telefono+" ";
}
}
