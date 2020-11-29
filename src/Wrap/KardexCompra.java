package Wrap;

public class KardexCompra {
	private Long codalmacen;
	private String xfvencimiento,xfingreso,xtiponota,numnota;
	private Float pc_unit;
	private Integer cantidad;
	public Integer getCantidad() {
		return cantidad;
	}
	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}
	public Long getCodalmacen() {
		return codalmacen;
	}
	public void setCodalmacen(Long codalmacen) {
		this.codalmacen = codalmacen;
	}
	public String getXfvencimiento() {
		return xfvencimiento;
	}
	public void setXfvencimiento(String xfvencimiento) {
		this.xfvencimiento = xfvencimiento;
	}
	public String getXfingreso() {
		return xfingreso;
	}
	public void setXfingreso(String xfingreso) {
		this.xfingreso = xfingreso;
	}
	public String getXtiponota() {
		return xtiponota;
	}
	public void setXtiponota(String xtiponota) {
		this.xtiponota = xtiponota;
	}
	public String getNumnota() {
		return numnota;
	}
	public void setNumnota(String numnota) {
		this.numnota = numnota;
	}
	public Float getPc_unit() {
		return pc_unit;
	}
	public void setPc_unit(Float pc_unit) {
		this.pc_unit = pc_unit;
	}
}
