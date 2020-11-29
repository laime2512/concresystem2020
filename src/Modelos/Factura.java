package Modelos;

public class Factura{
	private Integer coddosificacion,numfac;
	private String fecfac,nitfac,clienteNit,codcontrol,estado;
	private Long codven,codcom;
	private Float total,cfiscal,descuento;
	public String getClienteNit() {
		return clienteNit;
	}
	public void setClienteNit(String clienteNit) {
		this.clienteNit = clienteNit;
	}
	public Integer getCoddosificacion() {
		return coddosificacion;
	}
	public void setCoddosificacion(Integer coddosificacion) {
		this.coddosificacion = coddosificacion;
	}
	public Long getCodcom() {
		return codcom;
	}
	public void setCodcom(Long codcom) {
		this.codcom = codcom;
	}
	public Long getCodven() {
		return codven;
	}
	public void setCodven(Long codven) {
		this.codven = codven;
	}
	public Float getTotal() {
		return total;
	}
	public void setTotal(Float total) {
		this.total = total;
	}
	public Float getCfiscal() {
		return cfiscal;
	}
	public void setCfiscal(Float cfiscal) {
		this.cfiscal = cfiscal;
	}
	public Float getDescuento() {
		return descuento;
	}
	public void setDescuento(Float descuento) {
		this.descuento = descuento;
	}
	public Integer getNumfac() {
		return numfac;
	}
	public void setNumfac(Integer numfac) {
		this.numfac = numfac;
	}
	public String getFecfac() {
		return fecfac;
	}
	public void setFecfac(String fecfac) {
		this.fecfac = fecfac;
	}
	public String getNitfac() {
		return nitfac;
	}
	public void setNitfac(String nitfac) {
		this.nitfac = nitfac;
	}
	public String getCodcontrol() {
		return codcontrol;
	}
	public void setCodcontrol(String codcontrol) {
		this.codcontrol = codcontrol;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	@Override
	public String toString() {
		return "Factura [coddosificacion=" + coddosificacion + ", numfac=" + numfac + ", fecfac=" + fecfac + ", nitfac="
				+ nitfac + ", cliente_nit=" + clienteNit + ", codcontrol=" + codcontrol + ", estado=" + estado
				+ ", codven=" + codven + ", codcom=" + codcom + ", total=" + total + ", cfiscal=" + cfiscal
				+ ", descuento=" + descuento + "]";
	}
}
