package Modelos;

public class DetalleVenta {
	private Long codpro,codven,codalmacen;
	private Integer cantidad,posDetalle;
	private Float precio,subtotal;
	private String xproducto;
	private String tipoVenta;
	private Long codpromo;
	
	public Integer getPosDetalle() {
		return posDetalle;
	}
	public void setPosDetalle(Integer posDetalle) {
		this.posDetalle = posDetalle;
	}
	public Long getCodalmacen() {
		return codalmacen;
	}
	public void setCodalmacen(Long codalmacen) {
		this.codalmacen = codalmacen;
	}
	public Long getCodpro() {
		return codpro;
	}
	public void setCodpro(Long codpro) {
		this.codpro = codpro;
	}
	public Long getCodven() {
		return codven;
	}
	public void setCodven(Long codven) {
		this.codven = codven;
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
	public String getXproducto() {
		return xproducto;
	}
	public void setXproducto(String xproducto) {
		this.xproducto = xproducto;
	}
	public String getTipoVenta() {
		return tipoVenta;
	}
	public void setTipoVenta(String tipoVenta) {
		this.tipoVenta = tipoVenta;
	}
	public Long getCodpromo() {
		return codpromo;
	}
	public void setCodpromo(Long codpromo) {
		this.codpromo = codpromo;
	}
	@Override
	public String toString() {
		return "DetalleVenta [codpro=" + codpro + ", codven=" + codven + ", codalmacen=" + codalmacen + ", cantidad="
				+ cantidad + ", posDetalle=" + posDetalle + ", precio=" + precio + ", subtotal=" + subtotal
				+ ", xproducto=" + xproducto + ", tipoVenta=" + tipoVenta + ", codpromo=" + codpromo + "]";
	}
}
