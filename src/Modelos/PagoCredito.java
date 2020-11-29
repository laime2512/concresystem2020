package Modelos;

public class PagoCredito {
	private Long codcom;
	private String fecha,fecreg,xfecha,xfecreg,observacion;
	private Float monto;
	private Long codusu;
	public Long getCodcom() {
		return codcom;
	}
	public void setCodcom(Long codcom) {
		this.codcom = codcom;
	}
	public String getFecha() {
		return fecha;
	}
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}
	public String getFecreg() {
		return fecreg;
	}
	public void setFecreg(String fecreg) {
		this.fecreg = fecreg;
	}
	public String getXfecha() {
		return xfecha;
	}
	public void setXfecha(String xfecha) {
		this.xfecha = xfecha;
	}
	public String getXfecreg() {
		return xfecreg;
	}
	public void setXfecreg(String xfecreg) {
		this.xfecreg = xfecreg;
	}
	public String getObservacion() {
		return observacion;
	}
	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}
	public Float getMonto() {
		return monto;
	}
	public void setMonto(Float monto) {
		this.monto = monto;
	}
	public Long getCodusu() {
		return codusu;
	}
	public void setCodusu(Long codusu) {
		this.codusu = codusu;
	}
}
