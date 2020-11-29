package Modelos;

import Utiles.Constantes;

public class Pedido{
	
	private Long codped,codusu,coddelivery,codven;
	private String direccion,xusuario;
	private String fecha,xfecha,fentrega,xfentrega,observacion;
	private Integer estado;
	private String nit, razon_nit,celular;
	private Cliente cliente;
	private Integer codsuc;
	public Integer getCodsuc() {
		return codsuc;
	}
	public void setCodsuc(Integer codsuc) {
		this.codsuc = codsuc;
	}
	public void setEstado(Integer estado) {
		this.estado = estado;
	}
	public String xestado() {
		if(estado == null)
			return "";
		if(estado == Constantes.Pedidos.PENDIENTE)
			return "Pendiente";
		if(estado == Constantes.Pedidos.ENVIADO)
			return "Enviado";
		if(estado == Constantes.Pedidos.ENTREGADO)
			return "Entregado";
		return "";
	}
	public Long getCodven() {
		return codven;
	}
	public void setCodven(Long codven) {
		this.codven = codven;
	}
	public Cliente getCliente() {
		return cliente;
	}
	public String getNit() {
		return nit;
	}
	public void setNit(String nit) {
		this.nit = nit;
	}
	public String getRazon_nit() {
		return razon_nit;
	}
	public void setRazon_nit(String razon_nit) {
		this.razon_nit = razon_nit;
	}
	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}
	public String getXusuario() {
		return xusuario;
	}
	public void setXusuario(String xusuario) {
		this.xusuario = xusuario;
	}
	public Long getCoddelivery() {
		return coddelivery;
	}
	public void setCoddelivery(Long coddelivery) {
		this.coddelivery = coddelivery;
	}
	public String getFecha() {
		return fecha;
	}
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}
	public String getXfecha() {
		return xfecha;
	}
	public void setXfecha(String xfecha) {
		this.xfecha = xfecha;
	}
	public String getFentrega() {
		return fentrega;
	}
	public void setFentrega(String fentrega) {
		this.fentrega = fentrega;
	}
	public String getXfentrega() {
		return xfentrega;
	}
	public void setXfentrega(String xfentrega) {
		this.xfentrega = xfentrega;
	}
	public String getObservacion() {
		return observacion;
	}
	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}
	public Long getCodusu() {
		return codusu;
	}
	public void setCodusu(Long codusu) {
		this.codusu = codusu;
	}
	public Long getCodped() {
		return codped;
	}
	public void setCodped(Long codped) {
		this.codped = codped;
	}
	public int getEstado() {
		return estado;
	}
	public void setEstado(int estado) {
		this.estado = estado;
	}
	public String getDireccion() {
		return direccion;
	}
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}
	public String getCelular() {
		return celular;
	}
	public void setCelular(String celular) {
		this.celular = celular;
	}
}
