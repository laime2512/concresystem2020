package Wrap;

import java.sql.Date;

public class AlmacenWrap {
	private Long codalmacen;
	private Long codlugar;
	private Integer cantidad;
	private Date fingreso,fvencimiento;
	public Long getCodalmacen() {
		return codalmacen;
	}
	public void setCodalmacen(Long codalmacen) {
		this.codalmacen = codalmacen;
	}
	public Long getCodlugar() {
		return codlugar;
	}
	public void setCodlugar(Long codlugar) {
		this.codlugar = codlugar;
	}
	public Integer getCantidad() {
		return cantidad;
	}
	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}
	public Date getFingreso() {
		return fingreso;
	}
	public void setFingreso(Date fingreso) {
		this.fingreso = fingreso;
	}
	public Date getFvencimiento() {
		return fvencimiento;
	}
	public void setFvencimiento(Date fvencimiento) {
		this.fvencimiento = fvencimiento;
	}
}
