package Modelos;

public class DetalleMargen {
	private Integer codMargen;
	private Short numberMargin;
	private String concept,typeMargin;
	private Float porcentajeUnidad;
	public Integer getCodMargen() {
		return codMargen;
	}
	public void setCodMargen(Integer codMargen) {
		this.codMargen = codMargen;
	}
	public Short getNumberMargin() {
		return numberMargin;
	}
	public void setNumberMargin(Short numberMargin) {
		this.numberMargin = numberMargin;
	}
	public String getConcept() {
		return concept;
	}
	public void setConcept(String concept) {
		this.concept = concept;
	}
	public String getTypeMargin() {
		return typeMargin;
	}
	public void setTypeMargin(String typeMargin) {
		this.typeMargin = typeMargin;
	}
	public Float getPorcentajeUnidad() {
		return porcentajeUnidad;
	}
	public void setPorcentajeUnidad(Float porcentajeUnidad) {
		this.porcentajeUnidad = porcentajeUnidad;
	}
	@Override
	public String toString() {
		return "DetalleMargen [codMargen=" + codMargen + ", numberMargin=" + numberMargin + ", concept=" + concept
				+ ", typeMargin=" + typeMargin + ", porcentajeUnidad=" + porcentajeUnidad + "]";
	}
}
