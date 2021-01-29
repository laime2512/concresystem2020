package Modelos;

public class AlmacenXls {
	private Long codpro;
	private String xproducto,generico,sucursal;
	private String xlaboratorio;
	private String xcodigo;
	private String xarea;
	private String xtipo;
	private String xposicion;
	private Float pvUnit;
	private Integer sumCanUnid;
	
	public String getSucursal() {
		return sucursal;
	}
	public void setSucursal(String sucursal) {
		this.sucursal = sucursal;
	}
	public Long getCodpro() {
		return codpro;
	}
	public void setCodpro(Long codpro) {
		this.codpro = codpro;
	}
	public String getXproducto() {
		return xproducto;
	}
	public void setXproducto(String xproducto) {
		this.xproducto = xproducto;
	}
	public String getGenerico() {
		return generico;
	}
	public void setGenerico(String generico) {
		this.generico = generico;
	}
	public String getXlaboratorio() {
		return xlaboratorio;
	}
	public void setXlaboratorio(String xlaboratorio) {
		this.xlaboratorio = xlaboratorio;
	}
	public String getXcodigo() {
		return xcodigo;
	}
	public void setXcodigo(String xcodigo) {
		this.xcodigo = xcodigo;
	}
	public String getXarea() {
		return xarea;
	}
	public void setXarea(String xarea) {
		this.xarea = xarea;
	}
	public String getXtipo() {
		return xtipo;
	}
	public void setXtipo(String xtipo) {
		this.xtipo = xtipo;
	}
	public String getXposicion() {
		return xposicion;
	}
	public void setXposicion(String xposicion) {
		this.xposicion = xposicion;
	}
	public Float getPvUnit() {
		return pvUnit;
	}
	public void setPvUnit(Float pvUnit) {
		this.pvUnit = pvUnit;
	}
	public Integer getSumCanUnid() {
		return sumCanUnid;
	}
	public void setSumCanUnid(Integer sumCanUnid) {
		this.sumCanUnid = sumCanUnid;
	}
	@Override
	public String toString() {
		return "AlmacenXls [codpro=" + codpro + ", xproducto=" + xproducto + ", generico=" + generico
				+ ", xlaboratorio=" + xlaboratorio + ", xcodigo=" + xcodigo + ", xarea=" + xarea + ", xtipo=" + xtipo
				+ ", xposicion=" + xposicion + ", pvUnit=" + pvUnit + ", sumCanUnid=" + sumCanUnid + ", sucursal" + sucursal + "]";
	}
}
