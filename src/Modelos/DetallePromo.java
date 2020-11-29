package Modelos;

public class DetallePromo {
	private Long codpromo;
	private Integer codpro;
	private Float descuentoUnidad,descuentoCaja,descuentoPaquete;
	public Long getCodpromo() {
		return codpromo;
	}
	public void setCodpromo(Long codpromo) {
		this.codpromo = codpromo;
	}
	public Integer getCodpro() {
		return codpro;
	}
	public void setCodpro(Integer codpro) {
		this.codpro = codpro;
	}
	public Float getDescuentoUnidad() {
		return descuentoUnidad;
	}
	public void setDescuentoUnidad(Float descuentoUnidad) {
		this.descuentoUnidad = descuentoUnidad;
	}
	public Float getDescuentoCaja() {
		return descuentoCaja;
	}
	public void setDescuentoCaja(Float descuentoCaja) {
		this.descuentoCaja = descuentoCaja;
	}
	public Float getDescuentoPaquete() {
		return descuentoPaquete;
	}
	public void setDescuentoPaquete(Float descuentoPaquete) {
		this.descuentoPaquete = descuentoPaquete;
	}
}
