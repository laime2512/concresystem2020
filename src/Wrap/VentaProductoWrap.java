package Wrap;

import java.sql.Date;

public class VentaProductoWrap {
	private Long codven;
	private Date fecha;
	private Integer cantidad;
	private Float subtotal;
	public Long getCodven() {
		return codven;
	}
	public void setCodven(Long codven) {
		this.codven = codven;
	}
	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	public Integer getCantidad() {
		return cantidad;
	}
	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}
	public Float getSubtotal() {
		return subtotal;
	}
	public void setSubtotal(Float subtotal) {
		this.subtotal = subtotal;
	}
}
