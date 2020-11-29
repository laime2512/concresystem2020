package Modelos;

import java.util.List;

public class Promocion {
	private Long codpromo;
	private String fcreacion,fini,ffin,xfcreacion,xfini,xffin,xusuario;
	private String titulo,descripcion;
	private Long codusu;
	private Boolean estpromo;
	private Integer gestion;
	private List<DetallePromo> detallePromo;
	
	public List<DetallePromo> getDetallePromo() {
		return detallePromo;
	}
	public void setDetallePromo(List<DetallePromo> detallePromo) {
		this.detallePromo = detallePromo;
	}
	public Long getCodusu() {
		return codusu;
	}
	public void setCodusu(Long codusu) {
		this.codusu = codusu;
	}
	public Integer getGestion() {
		return gestion;
	}
	public void setGestion(Integer gestion) {
		this.gestion = gestion;
	}
	public String getXusuario() {
		return xusuario;
	}
	public void setXusuario(String xusuario) {
		this.xusuario = xusuario;
	}
	public Long getCodpromo() {
		return codpromo;
	}
	public void setCodpromo(Long codpromo) {
		this.codpromo = codpromo;
	}
	public String getFcreacion() {
		return fcreacion;
	}
	public void setFcreacion(String fcreacion) {
		this.fcreacion = fcreacion;
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
	public String getXfcreacion() {
		return xfcreacion;
	}
	public void setXfcreacion(String xfcreacion) {
		this.xfcreacion = xfcreacion;
	}
	public String getXfini() {
		return xfini;
	}
	public void setXfini(String xfini) {
		this.xfini = xfini;
	}
	public String getXffin() {
		return xffin;
	}
	public void setXffin(String xffin) {
		this.xffin = xffin;
	}
	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public Boolean getEstpromo() {
		return estpromo;
	}
	public void setEstpromo(Boolean estpromo) {
		this.estpromo = estpromo;
	}
	@Override
	public String toString() {
		return "Promocion [codpromo=" + codpromo + ", fcreacion=" + fcreacion + ", fini=" + fini + ", ffin=" + ffin
				+ ", xfcreacion=" + xfcreacion + ", xfini=" + xfini + ", xffin=" + xffin + ", titulo=" + titulo
				+ ", descripcion=" + descripcion + ", coudu=" + codusu + ", estpromo=" + estpromo + "]";
	}
}
