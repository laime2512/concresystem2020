package Wrap;

import Modelos.Margen;

public class MargenWrap {
	private Margen margen;
	private String[] conceptVec;
	private String[] typeMarginVec;
	private Float[] porcentajeUnidadVec;
	public Margen getMargen() {
		return margen;
	}
	public void setMargen(Margen margen) {
		this.margen = margen;
	}
	public String[] getConceptVec() {
		return conceptVec;
	}
	public void setConceptVec(String[] conceptVec) {
		this.conceptVec = conceptVec;
	}
	public String[] getTypeMarginVec() {
		return typeMarginVec;
	}
	public void setTypeMarginVec(String[] typeMarginVec) {
		this.typeMarginVec = typeMarginVec;
	}
	public Float[] getPorcentajeUnidadVec() {
		return porcentajeUnidadVec;
	}
	public void setPorcentajeUnidadVec(Float[] porcentajeUnidadVec) {
		this.porcentajeUnidadVec = porcentajeUnidadVec;
	}
}
