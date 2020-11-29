package Wrap;

import java.sql.Date;

public class CompraProductoWrap {
	private Long codcom;
	private Date fecha;
	private Integer cantidad;
	private Float precio;
	private String tipoCompra;
	private Float subtotal;
	public String getXtipoCompra() {
		if(tipoCompra!=null) {
			switch (tipoCompra) {
			case "1":
				return "unidades";
			case "2":
				return "cajas";
			case "3":
				return "paquetes";
			default:
				return "";
			}
		}
		return "";
	}
	public String getTipoCompra() {
		return tipoCompra;
	}
	public void setTipoCompra(String tipoCompra) {
		this.tipoCompra = tipoCompra;
	}
	public Long getCodcom() {
		return codcom;
	}
	public void setCodcom(Long codcom) {
		this.codcom = codcom;
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
}
