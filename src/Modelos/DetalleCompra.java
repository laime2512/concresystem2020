package Modelos;

import java.sql.Date;

public class DetalleCompra {
	private Long codcom,codpro;
	private Integer cantidad;
	private Float precio,subtotal;
	private Date fingreso,fvencimiento;
	private Long codalmacen;
	private Integer coddcom;
	private Float impuestos;
	private Boolean devolucion;
	private Float descuento, porcentajeUnidad, porcentajeCaja, porcentajePaquete;
	private String tipoCompra, xproducto;
	public Long getCodcom() {
		return codcom;
	}
	public void setCodcom(Long codcom) {
		this.codcom = codcom;
	}
	public Long getCodpro() {
		return codpro;
	}
	public void setCodpro(Long codpro) {
		this.codpro = codpro;
	}
	public Integer getCantidad() {
		return cantidad;
	}
	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}
	public Float getPrecio() {
		return precio;
	}
	public void setPrecio(Float precio) {
		this.precio = precio;
	}
	public Float getSubtotal() {
		return subtotal;
	}
	public void setSubtotal(Float subtotal) {
		this.subtotal = subtotal;
	}
	public Date getFingreso() {
		return fingreso;
	}
	public void setFingreso(Date fingreso) {
		this.fingreso = fingreso;
	}
	public Date getFvencimiento() {
		return fvencimiento;
	}
	public void setFvencimiento(Date fvencimiento) {
		this.fvencimiento = fvencimiento;
	}
	public Long getCodalmacen() {
		return codalmacen;
	}
	public void setCodalmacen(Long codalmacen) {
		this.codalmacen = codalmacen;
	}
	public Integer getCoddcom() {
		return coddcom;
	}
	public void setCoddcom(Integer coddcom) {
		this.coddcom = coddcom;
	}
	public Float getImpuestos() {
		return impuestos;
	}
	public void setImpuestos(Float impuestos) {
		this.impuestos = impuestos;
	}
	public Boolean getDevolucion() {
		return devolucion;
	}
	public void setDevolucion(Boolean devolucion) {
		this.devolucion = devolucion;
	}
	public Float getDescuento() {
		return descuento;
	}
	public void setDescuento(Float descuento) {
		this.descuento = descuento;
	}
	public Float getPorcentajeUnidad() {
		return porcentajeUnidad;
	}
	public void setPorcentajeUnidad(Float porcentajeUnidad) {
		this.porcentajeUnidad = porcentajeUnidad;
	}
	public Float getPorcentajeCaja() {
		return porcentajeCaja;
	}
	public void setPorcentajeCaja(Float porcentajeCaja) {
		this.porcentajeCaja = porcentajeCaja;
	}
	public Float getPorcentajePaquete() {
		return porcentajePaquete;
	}
	public void setPorcentajePaquete(Float porcentajePaquete) {
		this.porcentajePaquete = porcentajePaquete;
	}
	public String getTipoCompra() {
		return tipoCompra;
	}
	public void setTipoCompra(String tipoCompra) {
		this.tipoCompra = tipoCompra;
	}
	public String getXproducto() {
		return xproducto;
	}
	public void setXproducto(String xproducto) {
		this.xproducto = xproducto;
	}
}
