package Modelos;

import java.util.ArrayList;
import java.util.List;

import Utiles.Constantes;

public class Salida {
	private Long codsal,codusu,numero;
	private String fsalida,xfsalida,descripcion,xusuario,xproveedor,xsuc_origen,xsuc_destino,estado;
	private Integer tipo,sucOrigen,sucDestino;
	private Boolean est,inOut;
	private Integer solucion;
	private Float monto;
	private String conclusion;
	private List<ViewDetalleSalida> detalleSalidaList;
	public void addDetalleSalidaList(ViewDetalleSalida detalle) {
		if(detalleSalidaList==null)
			detalleSalidaList = new ArrayList<ViewDetalleSalida>();
		detalleSalidaList.add(detalle);
	}
	public List<ViewDetalleSalida> getDetalleSalidaList() {
		return detalleSalidaList;
	}
	public void setDetalleSalidaList(List<ViewDetalleSalida> detalleSalidaList) {
		this.detalleSalidaList = detalleSalidaList;
	}
	public Integer getSolucion() {
		return solucion;
	}
	public void setSolucion(Integer solucion) {
		this.solucion = solucion;
	}
	public Float getMonto() {
		return monto;
	}
	public void setMonto(Float monto) {
		this.monto = monto;
	}
	public String getConclusion() {
		return conclusion;
	}
	public void setConclusion(String conclusion) {
		this.conclusion = conclusion;
	}
	public Long getCodsal() {
		return codsal;
	}
	public void setCodsal(Long codsal) {
		this.codsal = codsal;
	}
	public Long getCodusu() {
		return codusu;
	}
	public void setCodusu(Long codusu) {
		this.codusu = codusu;
	}
	public Long getNumero() {
		return numero;
	}
	public void setNumero(Long numero) {
		this.numero = numero;
	}
	public String getFsalida() {
		return fsalida;
	}
	public void setFsalida(String fsalida) {
		this.fsalida = fsalida;
	}
	public String getXfsalida() {
		return xfsalida;
	}
	public void setXfsalida(String xfsalida) {
		this.xfsalida = xfsalida;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
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
	public String getXsuc_origen() {
		return xsuc_origen;
	}
	public void setXsuc_origen(String xsuc_origen) {
		this.xsuc_origen = xsuc_origen;
	}
	public String getXsuc_destino() {
		return xsuc_destino;
	}
	public void setXsuc_destino(String xsuc_destino) {
		this.xsuc_destino = xsuc_destino;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public Integer getTipo() {
		return tipo;
	}
	public void setTipo(Integer tipo) {
		this.tipo = tipo;
	}
	public Integer getSucOrigen() {
		return sucOrigen;
	}
	public void setSucOrigen(Integer sucOrigen) {
		this.sucOrigen = sucOrigen;
	}
	public Integer getSucDestino() {
		return sucDestino;
	}
	public void setSucDestino(Integer sucDestino) {
		this.sucDestino = sucDestino;
	}
	public Boolean getEst() {
		return est;
	}
	public void setEst(Boolean est) {
		this.est = est;
	}
	public Boolean getInOut() {
		return inOut;
	}
	public void setInOut(Boolean inOut) {
		this.inOut = inOut;
	}
	public String getXtipo() {
		if(tipo !=null) {
			return Constantes.TIPO_SALIDA[tipo];
		}
		return "";
	}
	@Override
	public String toString() {
		return "Salida [codsal=" + codsal + ", codusu=" + codusu + ", numero=" + numero + ", fsalida=" + fsalida
				+ ", xfsalida=" + xfsalida + ", descripcion=" + descripcion + ", xusuario=" + xusuario + ", xproveedor="
				+ xproveedor + ", xsuc_origen=" + xsuc_origen + ", xsuc_destino=" + xsuc_destino + ", estado=" + estado
				+ ", tipo=" + tipo + ", sucOrigen=" + sucOrigen + ", sucDestino=" + sucDestino + ", est=" + est
				+ ", inOut=" + inOut + ", solucion=" + solucion + ", monto=" + monto + ", conclusion=" + conclusion
				+ ", detalleSalidaList=" + detalleSalidaList + "]";
	}
}