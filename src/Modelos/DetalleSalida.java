package Modelos;

public class DetalleSalida {
	private Long codsal;
	private Long codalmacen;
	private Integer cantidad;
	private ViewProducto producto;
	private Almacen almacen;
	private String fingreso;
	private String fvencimiento;
	private Boolean isResponse,inOut;
	public Boolean getInOut() {
		return inOut;
	}
	public void setInOut(Boolean inOut) {
		this.inOut = inOut;
	}
	public Long getCodsal() {
		return codsal;
	}
	public void setCodsal(Long codsal) {
		this.codsal = codsal;
	}
	public Long getCodalmacen() {
		return codalmacen;
	}
	public void setCodalmacen(Long codalmacen) {
		this.codalmacen = codalmacen;
	}
	public Integer getCantidad() {
		return cantidad;
	}
	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}
	public Almacen getAlmacen() {
		return almacen;
	}
	public void setAlmacen(Almacen almacen) {
		this.almacen = almacen;
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
	public Boolean getIsResponse() {
		return isResponse;
	}
	public void setIsResponse(Boolean isResponse) {
		this.isResponse = isResponse;
	}
	public ViewProducto getProducto() {
		return producto;
	}
	public void setProducto(ViewProducto producto) {
		this.producto = producto;
	}
}
