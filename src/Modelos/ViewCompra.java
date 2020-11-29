package Modelos;

import Utiles.Numeros;

public class ViewCompra {
	private Long codcom;
	private String fecha;
	private Integer estado,codsuc;
	private Long codusu;
	private Long codpro;
	private Integer formapago;
	private Float total;
	private Float descuento;
	private Long codcaja;
	private Long coddetcaja;
	private Long num;
	private Float bonificacion;
	private Float subtotal,acuenta;
	private Integer tiponota;
	private String xusuario,xproveedor,xfecha,numnota;
	private Boolean credito,estadoCredito;
	public Long getCodcom() {
		return codcom;
	}
	public void setCodcom(Long codcom) {
		this.codcom = codcom;
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
	public Integer getCodsuc() {
		return codsuc;
	}
	public void setCodsuc(Integer codsuc) {
		this.codsuc = codsuc;
	}
	public Long getCodusu() {
		return codusu;
	}
	public void setCodusu(Long codusu) {
		this.codusu = codusu;
	}
	public Long getCodpro() {
		return codpro;
	}
	public void setCodpro(Long codpro) {
		this.codpro = codpro;
	}
	public Integer getFormapago() {
		return formapago;
	}
	public void setFormapago(Integer formapago) {
		this.formapago = formapago;
	}
	public Float getTotal() {
		return total;
	}
	public void setTotal(Float total) {
		this.total = total;
	}
	public Float getDescuento() {
		return descuento;
	}
	public void setDescuento(Float descuento) {
		this.descuento = descuento;
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
	public Long getNum() {
		return num;
	}
	public void setNum(Long num) {
		this.num = num;
	}
	public Float getBonificacion() {
		return bonificacion;
	}
	public void setBonificacion(Float bonificacion) {
		this.bonificacion = bonificacion;
	}
	public Float getSubtotal() {
		return subtotal;
	}
	public void setSubtotal(Float subtotal) {
		this.subtotal = subtotal;
	}
	public Float getAcuenta() {
		return acuenta;
	}
	public void setAcuenta(Float acuenta) {
		this.acuenta = acuenta;
	}
	public Integer getTiponota() {
		return tiponota;
	}
	public void setTiponota(Integer tiponota) {
		this.tiponota = tiponota;
	}
	public String getXusuario() {
		return xusuario;
	}
	public void setXusuario(String xusuario) {
		this.xusuario = xusuario;
	}
	public String getXproveedor() {
		return xproveedor;
	}
	public void setXproveedor(String xproveedor) {
		this.xproveedor = xproveedor;
	}
	public String getXfecha() {
		return xfecha;
	}
	public void setXfecha(String xfecha) {
		this.xfecha = xfecha;
	}
	public String getNumnota() {
		return numnota;
	}
	public void setNumnota(String numnota) {
		this.numnota = numnota;
	}
	public Boolean getCredito() {
		return credito;
	}
	public void setCredito(Boolean credito) {
		this.credito = credito;
	}
	public Boolean getEstadoCredito() {
		return estadoCredito;
	}
	public void setEstadoCredito(Boolean estadoCredito) {
		this.estadoCredito = estadoCredito;
	}
	public String getXcredito() {
		return credito?("Credito"+(estadoCredito?"(Pendiente)":"(Finalizado)")):"Contado";
	}
	public Float getXsaldo() {
		if(credito) {
			return Numeros.formato2decimales(total-acuenta);
		}
		return 0f;
	}
}