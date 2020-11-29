package Modelos;

public class ViewVenta {
	private Long codven;
	private String fecha;
	private Integer estado;
	private Long codcli;
	private Long codcaja;
	private Long coddetcaja;
	private Long codusu;
	private Integer tiponota;
	private Integer formapago;
	private Integer facturado;
	private Float total,totalPagado,cambio;
	private String xfecha,xusuario,xcliente,xformapago,xtiponota,clienteNit,nitfac,estadoFactura;
	private Integer codsuc;
	//private List<ViewDetalleVenta> detalles;
	public Long getCodven() {
		return codven;
	}
	public void setCodven(Long codven) {
		this.codven = codven;
	}
	public String getFecha() {
		return fecha;
	}
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}
	public Integer getEstado() {
		return estado;
	}
	public void setEstado(Integer estado) {
		this.estado = estado;
	}
	public Long getCodcli() {
		return codcli;
	}
	public void setCodcli(Long codcli) {
		this.codcli = codcli;
	}
	public Long getCodcaja() {
		return codcaja;
	}
	public void setCodcaja(Long codcaja) {
		this.codcaja = codcaja;
	}
	public Long getCoddetcaja() {
		return coddetcaja;
	}
	public void setCoddetcaja(Long coddetcaja) {
		this.coddetcaja = coddetcaja;
	}
	public Long getCodusu() {
		return codusu;
	}
	public void setCodusu(Long codusu) {
		this.codusu = codusu;
	}
	public Integer getTiponota() {
		return tiponota;
	}
	public void setTiponota(Integer tiponota) {
		this.tiponota = tiponota;
	}
	public Integer getFormapago() {
		return formapago;
	}
	public void setFormapago(Integer formapago) {
		this.formapago = formapago;
	}
	public Integer getFacturado() {
		return facturado;
	}
	public void setFacturado(Integer facturado) {
		this.facturado = facturado;
	}
	public Float getTotal() {
		return total;
	}
	public void setTotal(Float total) {
		this.total = total;
	}
	public Float getTotalPagado() {
		return totalPagado;
	}
	public void setTotalPagado(Float totalPagado) {
		this.totalPagado = totalPagado;
	}
	public Float getCambio() {
		return cambio;
	}
	public void setCambio(Float cambio) {
		this.cambio = cambio;
	}
	public String getXfecha() {
		return xfecha;
	}
	public void setXfecha(String xfecha) {
		this.xfecha = xfecha;
	}
	public String getXusuario() {
		return xusuario;
	}
	public void setXusuario(String xusuario) {
		this.xusuario = xusuario;
	}
	public String getXcliente() {
		return xcliente;
	}
	public void setXcliente(String xcliente) {
		this.xcliente = xcliente;
	}
	public String getXformapago() {
		return xformapago;
	}
	public void setXformapago(String xformapago) {
		this.xformapago = xformapago;
	}
	public String getXtiponota() {
		return xtiponota;
	}
	public void setXtiponota(String xtiponota) {
		this.xtiponota = xtiponota;
	}
	public String getClienteNit() {
		return clienteNit;
	}
	public void setClienteNit(String clienteNit) {
		this.clienteNit = clienteNit;
	}
	public String getNitfac() {
		return nitfac;
	}
	public void setNitfac(String nitfac) {
		this.nitfac = nitfac;
	}
	public String getEstadoFactura() {
		return estadoFactura;
	}
	public void setEstadoFactura(String estadoFactura) {
		this.estadoFactura = estadoFactura;
	}
	public Integer getCodsuc() {
		return codsuc;
	}
	public void setCodsuc(Integer codsuc) {
		this.codsuc = codsuc;
	}
//	public List<ViewDetalleVenta> getDetalles() {
//		return detalles;
//	}
//	public void setDetalles(List<ViewDetalleVenta> detalles) {
//		this.detalles = detalles;
//	}
}
