package Wrap;

import java.sql.Date;

import Utiles.Constantes;

public class SalidaProductoWrap {
	private Long codsal;
	private Date fsalida;
	private Integer cantidad,tipo;
	private Boolean inOut,inOutSalida;
	private String fingreso,fvencimiento;
	public Integer getTipo() {
		return tipo;
	}
	public void setTipo(Integer tipo) {
		this.tipo = tipo;
	}
	public Long getCodsal() {
		return codsal;
	}
	public void setCodsal(Long codsal) {
		this.codsal = codsal;
	}
	public Date getFsalida() {
		return fsalida;
	}
	public void setFsalida(Date fsalida) {
		this.fsalida = fsalida;
	}
	public Integer getCantidad() {
		return cantidad;
	}
	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}
	public Boolean getInOut() {
		return inOut;
	}
	public void setInOut(Boolean inOut) {
		this.inOut = inOut;
	}
	public Boolean getInOutSalida() {
		return inOutSalida;
	}
	public void setInOutSalida(Boolean inOutSalida) {
		this.inOutSalida = inOutSalida;
	}
	public String getFingreso() {
		return fingreso;
	}
	public void setFingreso(String fingreso) {
		this.fingreso = fingreso;
	}
	public String getFvencimiento() {
		return fvencimiento;
	}
	public void setFvencimiento(String fvencimiento) {
		this.fvencimiento = fvencimiento;
	}
	public String getXtipo() {
		if(tipo !=null) {
			return Constantes.TIPO_SALIDA[tipo];
		}
		return "";
	}
}
