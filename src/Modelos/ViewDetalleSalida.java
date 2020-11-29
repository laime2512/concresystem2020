package Modelos;

import java.sql.Date;

public class ViewDetalleSalida {
	private Long codsal;
	private Long codalmacen,codlugar;
	private Integer cantidad;
	private Date fingreso,fvencimiento;
	private Boolean isResponse,inOut;
	private String xfingreso, xfvencimiento,nombre;
	private Long codpro;
	private ViewProducto producto;
	public Long getCodpro() {
		return codpro;
	}
	public void setCodpro(Long codpro) {
		this.codpro = codpro;
	}
	public Long getCodlugar() {
		return codlugar;
	}
	public void setCodlugar(Long codlugar) {
		this.codlugar = codlugar;
	}
	public ViewProducto getProducto() {
		return producto;
	}
	public void setProducto(ViewProducto producto) {
		this.producto = producto;
	}
	public Long getCodsal() {
		return codsal;
	}
	public void setCodsal(Long codsal) {
		this.codsal = codsal;
	}
	public Long getCodalmacen() {
		return codalmacen;
	}
	public void setCodalmacen(Long codalmacen) {
		this.codalmacen = codalmacen;
	}
	public Integer getCantidad() {
		return cantidad;
	}
	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}
	public Date getFingreso() {
		return fingreso;
	}
	public void setFingreso(Date fingreso) {
		this.fingreso = fingreso;
	}
	public Boolean getIsResponse() {
		return isResponse;
	}
	public void setIsResponse(Boolean isResponse) {
		this.isResponse = isResponse;
	}
	public Boolean getInOut() {
		return inOut;
	}
	public void setInOut(Boolean inOut) {
		this.inOut = inOut;
	}
	public String getXfingreso() {
		return xfingreso;
	}
	public void setXfingreso(String xfingreso) {
		this.xfingreso = xfingreso;
	}
	public String getXfvencimiento() {
		return xfvencimiento;
	}
	public void setXfvencimiento(String xfvencimiento) {
		this.xfvencimiento = xfvencimiento;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public Date getFvencimiento() {
		return fvencimiento;
	}
	public void setFvencimiento(Date fvencimiento) {
		this.fvencimiento = fvencimiento;
	}
}
