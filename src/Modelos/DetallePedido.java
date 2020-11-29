package Modelos;

public class DetallePedido {
	private Long codped;
	private Long codpro;
	private Integer cantidad;
	private Float precio;
	private Float subtotal;
	private String xproducto;
	public String getXproducto() {
		return xproducto;
	}
	public void setXproducto(String xproducto) {
		this.xproducto = xproducto;
	}
	public Long getCodped() {
		return codped;
	}
	public void setCodped(Long coded) {
		this.codped = coded;
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
	@Override
	public String toString() {
		return "DetallePedido [coded=" + codped + ", codpro=" + codpro + ", cantidad=" + cantidad + ", precio=" + precio
				+ ", subtotal=" + subtotal + "]";
	}
	
}
