package Modelos;

import java.util.List;

public class Caja{
	private Long codcaja;
	private Long codusu;
	private String fini;
	private String ffin;
	private Float monini;
	private Float monfin;
	private Float monsistema;
	private Integer codsuc;
	private boolean estado;
	private Integer tipo;
	private String observacion;
	private String xfecha,xffin;
	private String xsucursal,xusuario;
	private List<DetalleCaja> detalles;
	public List<DetalleCaja> getDetalles() {
		return detalles;
	}
	public void setDetalles(List<DetalleCaja> detalles) {
		this.detalles = detalles;
	}
	public String getXfecha() {
		return xfecha;
	}
	public void setXfecha(String xfecha) {
		this.xfecha = xfecha;
	}
	public String getXffin() {
		return xffin;
	}
	public void setXffin(String xffin) {
		this.xffin = xffin;
	}
	public String getObservacion() {
		return observacion;
	}
	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}
	
	public Integer getTipo() {
		return tipo;
	}
	public void setTipo(Integer tipo) {
		this.tipo = tipo;
	}
	public String getFini() {
		return fini;
	}
	public void setFini(String fini) {
		this.fini = fini;
	}
	public String getFfin() {
		return ffin;
	}
	public void setFfin(String ffin) {
		this.ffin = ffin;
	}
	public String getXsucursal() {
		return xsucursal;
	}
	public void setXsucursal(String xsucursal) {
		this.xsucursal = xsucursal;
	}
	public String getXusuario() {
		return xusuario;
	}
	public void setXusuario(String xusuario) {
		this.xusuario = xusuario;
	}
	public Long getCodcaja() {
		return codcaja;
	}
	public void setCodcaja(Long codcaja) {
		this.codcaja = codcaja;
	}
	public Long getCodusu() {
		return codusu;
	}
	public void setCodusu(Long codusu) {
		this.codusu = codusu;
	}
	public Float getMonini() {
		return monini;
	}
	public void setMonini(Float monini) {
		this.monini = monini;
	}
	public Float getMonfin() {
		return monfin;
	}
	public void setMonfin(Float monfin) {
		this.monfin = monfin;
	}
	public Float getMonsistema() {
		return monsistema;
	}
	public void setMonsistema(Float monsistema) {
		this.monsistema = monsistema;
	}
	public Integer getCodsuc() {
		return codsuc;
	}
	public void setCodsuc(Integer codsuc) {
		this.codsuc = codsuc;
	}
	public boolean isEstado() {
		return estado;
	}
	public void setEstado(boolean estado) {
		this.estado = estado;
	}
	@Override
	public String toString() {
		return "Caja [codcaja=" + codcaja + ", codusu=" + codusu + ", fini=" + fini + ", ffin=" + ffin + ", monini="
				+ monini + ", monfin=" + monfin + ", monsistema=" + monsistema + ", codsuc=" + codsuc + ", estado="
				+ estado + ", tipo=" + tipo + ", observacion=" + observacion + ", xfecha=" + xfecha + ", xffin=" + xffin
				+ ", xsucursal=" + xsucursal + ", xusuario=" + xusuario + "]";
	}
}