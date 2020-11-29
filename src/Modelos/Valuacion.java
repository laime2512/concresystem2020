package Modelos;

public class Valuacion {
	private String fecha;
	private Float pcosto;
	private Integer cantidad;
	private Float total;
	public String getFecha() {
		return fecha;
	}
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}
	public Float getPcosto() {
		return pcosto;
	}
	public void setPcosto(Float pcosto) {
		this.pcosto = pcosto;
	}
	public Integer getCantidad() {
		return cantidad;
	}
	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}
	public Float getTotal() {
		return total;
	}
	public void setTotal(Float total) {
		this.total = total;
	}
}
