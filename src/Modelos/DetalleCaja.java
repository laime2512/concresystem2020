package Modelos;

public class DetalleCaja{
	private Long codcaja;
	private Long coddetcaja;
	private Float monto;
	private String fecha;
	private Integer estado;
	private Integer codcuenta;
	private String xcuenta;
	private String xtipo;
	public String getXtipo() {
		return xtipo;
	}
	public void setXtipo(String xtipo) {
		this.xtipo = xtipo;
	}
	public Long getCodcaja() {
		return codcaja;
	}
	public void setCodcaja(Long codcaja) {
		this.codcaja = codcaja;
	}
	public Long getCoddetcaja() {
		return coddetcaja;
	}
	public void setCoddetcaja(Long coddetcaja) {
		this.coddetcaja = coddetcaja;
	}
	public Float getMonto() {
		return monto;
	}
	public void setMonto(Float monto) {
		this.monto = monto;
	}
	public String getFecha() {
		return fecha;
	}
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}
	public Integer getEstado() {
		return estado;
	}
	public void setEstado(Integer estado) {
		this.estado = estado;
	}
	public Integer getCodcuenta() {
		return codcuenta;
	}
	public void setCodcuenta(Integer codcuenta) {
		this.codcuenta = codcuenta;
	}
	public String getXcuenta() {
		return xcuenta;
	}
	public void setXcuenta(String xcuenta) {
		this.xcuenta = xcuenta;
	}
}
