package Modelos;

public class Lugar {
	private Long codlugar;
	private Integer codsuc;
	private Long codpro;
	private Integer codposicion;
	private String xposicion,xmueble,xtipo_mueble,xproducto,xtipo,
	xcategoria,xarea,concentracion,xlaboratorio,xmedida,xpresentacion;
	public String getXposicion() {
		return xposicion;
	}
	public void setXposicion(String xposicion) {
		this.xposicion = xposicion;
	}
	public String getXmueble() {
		return xmueble;
	}
	public void setXmueble(String xmueble) {
		this.xmueble = xmueble;
	}
	public String getXtipo_mueble() {
		return xtipo_mueble;
	}
	public void setXtipo_mueble(String xtipo_mueble) {
		this.xtipo_mueble = xtipo_mueble;
	}
	public String getXproducto() {
		return xproducto;
	}
	public void setXproducto(String xproducto) {
		this.xproducto = xproducto;
	}
	public String getXtipo() {
		return xtipo;
	}
	public void setXtipo(String xtipo) {
		this.xtipo = xtipo;
	}
	public String getXcategoria() {
		return xcategoria;
	}
	public void setXcategoria(String xcategoria) {
		this.xcategoria = xcategoria;
	}
	public String getXarea() {
		return xarea;
	}
	public void setXarea(String xarea) {
		this.xarea = xarea;
	}
	public String getConcentracion() {
		return concentracion;
	}
	public void setConcentracion(String concentracion) {
		this.concentracion = concentracion;
	}
	public String getXlaboratorio() {
		return xlaboratorio;
	}
	public void setXlaboratorio(String xlaboratorio) {
		this.xlaboratorio = xlaboratorio;
	}
	public String getXmedida() {
		return xmedida;
	}
	public void setXmedida(String xmedida) {
		this.xmedida = xmedida;
	}
	public String getXpresentacion() {
		return xpresentacion;
	}
	public void setXpresentacion(String xpresentacion) {
		this.xpresentacion = xpresentacion;
	}
	public Long getCodlugar() {
		return codlugar;
	}
	public void setCodlugar(Long codlugar) {
		this.codlugar = codlugar;
	}
	public Integer getCodsuc() {
		return codsuc;
	}
	public void setCodsuc(Integer codsuc) {
		this.codsuc = codsuc;
	}
	public Long getCodpro() {
		return codpro;
	}
	public void setCodpro(Long codpro) {
		this.codpro = codpro;
	}
	public Integer getCodposicion() {
		return codposicion;
	}
	public void setCodposicion(Integer codposicion) {
		this.codposicion = codposicion;
	}
	@Override
	public String toString() {
		return "Lugar [codlugar=" + codlugar + ", codsuc=" + codsuc + ", codpro=" + codpro + ", codposicion="
				+ codposicion + "]";
	}
	
}
