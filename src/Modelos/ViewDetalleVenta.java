package Modelos;

public class ViewDetalleVenta {
	private Long codpro,codven,codalmacen;
	private Integer cantidad,posDetalle;
	private Float precio,subtotal;
	private String xproducto;
	private String tipoVenta;
	private Integer unixcaja;
	private Float unixpaquete;
	private Float cantidadTipoVenta;
	private String xtipoVenta;
	public String getXtipoVenta() {
		return xtipoVenta;
	}
	public void setXtipoVenta(String xtipoVenta) {
		this.xtipoVenta = xtipoVenta;
	}
	public Float getCantidadTipoVenta() {
		return cantidadTipoVenta;
	}
	public void setCantidadTipoVenta(Float cantidadTipoVenta) {
		this.cantidadTipoVenta = cantidadTipoVenta;
	}
	public Integer getUnixcaja() {
		return unixcaja;
	}
	public void setUnixcaja(Integer unixcaja) {
		this.unixcaja = unixcaja;
	}
	public Float getUnixpaquete() {
		return unixpaquete;
	}
	public void setUnixpaquete(Float unixpaquete) {
		this.unixpaquete = unixpaquete;
	}
	private Long codpromo;
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
	public Integer getPosDetalle() {
		return posDetalle;
	}
	public void setPosDetalle(Integer posDetalle) {
		this.posDetalle = posDetalle;
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
}