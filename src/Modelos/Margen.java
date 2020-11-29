package Modelos;

import java.util.List;

public class Margen{
	private Integer codMargen;
	private Long codusu;
	private String dateRegister,observacion,xusuario;
	private Boolean estado;
	private List<DetalleMargen> detalleList;
	public String getXusuario() {
		return xusuario;
	}
	public void setXusuario(String xusuario) {
		this.xusuario = xusuario;
	}
	public List<DetalleMargen> getDetalleList() {
		return detalleList;
	}
	public void setDetalleList(List<DetalleMargen> detalleList) {
		this.detalleList = detalleList;
	}
	public Integer getCodMargen() {
		return codMargen;
	}
	public void setCodMargen(Integer codMargen) {
		this.codMargen = codMargen;
	}
	public Long getCodusu() {
		return codusu;
	}
	public void setCodusu(Long codusu) {
		this.codusu = codusu;
	}
	public String getDateRegister() {
		return dateRegister;
	}
	public void setDateRegister(String dateRegister) {
		this.dateRegister = dateRegister;
	}
	public String getObservacion() {
		return observacion;
	}
	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}
	public Boolean getEstado() {
		return estado;
	}
	public void setEstado(Boolean estado) {
		this.estado = estado;
	}
	@Override
	public String toString() {
		return "Margen [codMargen=" + codMargen + ", codusu=" + codusu + ", dateRegister=" + dateRegister
				+ ", observacion=" + observacion + ", estado=" + estado + "]";
	}
}
