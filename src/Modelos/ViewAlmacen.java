package Modelos;

import Utiles.Numeros;

public class ViewAlmacen {
	private Long codpro;
	private Integer codlab,codmed,codare,estado,codtip,unixcaja;
	private String nombre,tipoCompra,presentacionUnidad,presentacionCaja,presentacionPaquete;
	private String generico,concentracion,codigobarra,margen,foto,pareto,sucursal;
	
	private String xtipo, xlaboratorio,xmedida,xarea,xcategoria;
	private Float pcUnit,pvUnit,pvCaja,pcCaja,unixpaquete,uniEnPaquete,inventarioMinimoCaja,inventarioMinimoPaquete,inventarioMinimoUnidad;
	private Float pcPaquete,pvPaquete,pvDescuentoPaquete,pvDescuentoCaja,porcentajeUnidad,porcentajeCaja,porcentajePaquete;
	private Float porcentajeDescuentoCaja, porcentajeDescuentoPaquete;
	private Boolean controlado;
	private Integer cantidad;
	private Double totalVenta;
	
	public String getSucursal() {
		return sucursal;
	}
	public void setSucursal(String sucursal) {
		this.sucursal = sucursal;
	}
	
	public Float getCantidadCaja() {
		if(cantidad!=null && unixcaja != null && unixcaja > 0f) {
			return Numeros.formato2decimales((float)cantidad/(float)unixcaja);
		}
		return 0f;
	}
	public Float getCantidadPaquete() {
		if(cantidad != null && unixcaja != null && unixcaja > 0f && unixpaquete != null && unixpaquete > 0f) {
			return Numeros.formato2decimales((float)cantidad/(float)unixcaja/(float)unixpaquete);
		}
		return 0f;
	}
	public Long getCodpro() {
		return codpro;
	}
	public void setCodpro(Long codpro) {
		this.codpro = codpro;
	}
	public Integer getCodlab() {
		return codlab;
	}
	public void setCodlab(Integer codlab) {
		this.codlab = codlab;
	}
	public Integer getCodmed() {
		return codmed;
	}
	public void setCodmed(Integer codmed) {
		this.codmed = codmed;
	}
	public Integer getCodare() {
		return codare;
	}
	public void setCodare(Integer codare) {
		this.codare = codare;
	}
	public Integer getEstado() {
		return estado;
	}
	public void setEstado(Integer estado) {
		this.estado = estado;
	}
	public Integer getCodtip() {
		return codtip;
	}
	public void setCodtip(Integer codtip) {
		this.codtip = codtip;
	}
	public Integer getUnixcaja() {
		return unixcaja;
	}
	public void setUnixcaja(Integer unixcaja) {
		this.unixcaja = unixcaja;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getTipoCompra() {
		return tipoCompra;
	}
	public void setTipoCompra(String tipoCompra) {
		this.tipoCompra = tipoCompra;
	}
	public String getPresentacionUnidad() {
		return presentacionUnidad;
	}
	public void setPresentacionUnidad(String presentacionUnidad) {
		this.presentacionUnidad = presentacionUnidad;
	}
	public String getPresentacionCaja() {
		return presentacionCaja;
	}
	public void setPresentacionCaja(String presentacionCaja) {
		this.presentacionCaja = presentacionCaja;
	}
	public String getPresentacionPaquete() {
		return presentacionPaquete;
	}
	public void setPresentacionPaquete(String presentacionPaquete) {
		this.presentacionPaquete = presentacionPaquete;
	}
	public String getGenerico() {
		return generico;
	}
	public void setGenerico(String generico) {
		this.generico = generico;
	}
	public String getConcentracion() {
		return concentracion;
	}
	public void setConcentracion(String concentracion) {
		this.concentracion = concentracion;
	}
	public String getCodigobarra() {
		return codigobarra;
	}
	public void setCodigobarra(String codigobarra) {
		this.codigobarra = codigobarra;
	}
	public String getMargen() {
		return margen;
	}
	public void setMargen(String margen) {
		this.margen = margen;
	}
	public String getFoto() {
		return foto;
	}
	public void setFoto(String foto) {
		this.foto = foto;
	}
	public String getPareto() {
		return pareto;
	}
	public void setPareto(String pareto) {
		this.pareto = pareto;
	}
	public String getXtipo() {
		return xtipo;
	}
	public void setXtipo(String xtipo) {
		this.xtipo = xtipo;
	}
	public String getXlaboratorio() {
		return xlaboratorio;
	}
	public void setXlaboratorio(String xlaboratorio) {
		this.xlaboratorio = xlaboratorio;
	}
	public String getXmedida() {
		return xmedida;
	}
	public void setXmedida(String xmedida) {
		this.xmedida = xmedida;
	}
	public String getXarea() {
		return xarea;
	}
	public void setXarea(String xarea) {
		this.xarea = xarea;
	}
	public String getXcategoria() {
		return xcategoria;
	}
	public void setXcategoria(String xcategoria) {
		this.xcategoria = xcategoria;
	}
	public Float getPcUnit() {
		return pcUnit;
	}
	public void setPcUnit(Float pcUnit) {
		this.pcUnit = pcUnit;
	}
	public Float getPvUnit() {
		return pvUnit;
	}
	public void setPvUnit(Float pvUnit) {
		this.pvUnit = pvUnit;
	}
	public Float getPvCaja() {
		return pvCaja;
	}
	public void setPvCaja(Float pvCaja) {
		this.pvCaja = pvCaja;
	}
	public Float getPcCaja() {
		return pcCaja;
	}
	public void setPcCaja(Float pcCaja) {
		this.pcCaja = pcCaja;
	}
	public Float getUnixpaquete() {
		return unixpaquete;
	}
	public void setUnixpaquete(Float unixpaquete) {
		this.unixpaquete = unixpaquete;
	}
	public Float getUniEnPaquete() {
		return uniEnPaquete;
	}
	public void setUniEnPaquete(Float uniEnPaquete) {
		this.uniEnPaquete = uniEnPaquete;
	}
	public Float getInventarioMinimoCaja() {
		return inventarioMinimoCaja;
	}
	public void setInventarioMinimoCaja(Float inventarioMinimoCaja) {
		this.inventarioMinimoCaja = inventarioMinimoCaja;
	}
	public Float getInventarioMinimoPaquete() {
		return inventarioMinimoPaquete;
	}
	public void setInventarioMinimoPaquete(Float inventarioMinimoPaquete) {
		this.inventarioMinimoPaquete = inventarioMinimoPaquete;
	}
	public Float getInventarioMinimoUnidad() {
		return inventarioMinimoUnidad;
	}
	public void setInventarioMinimoUnidad(Float inventarioMinimoUnidad) {
		this.inventarioMinimoUnidad = inventarioMinimoUnidad;
	}
	public Float getPcPaquete() {
		return pcPaquete;
	}
	public void setPcPaquete(Float pcPaquete) {
		this.pcPaquete = pcPaquete;
	}
	public Float getPvPaquete() {
		return pvPaquete;
	}
	public void setPvPaquete(Float pvPaquete) {
		this.pvPaquete = pvPaquete;
	}
	public Float getPvDescuentoPaquete() {
		return pvDescuentoPaquete;
	}
	public void setPvDescuentoPaquete(Float pvDescuentoPaquete) {
		this.pvDescuentoPaquete = pvDescuentoPaquete;
	}
	public Float getPvDescuentoCaja() {
		return pvDescuentoCaja;
	}
	public void setPvDescuentoCaja(Float pvDescuentoCaja) {
		this.pvDescuentoCaja = pvDescuentoCaja;
	}
	public Float getPorcentajeUnidad() {
		return porcentajeUnidad;
	}
	public void setPorcentajeUnidad(Float porcentajeUnidad) {
		this.porcentajeUnidad = porcentajeUnidad;
	}
	public Float getPorcentajeCaja() {
		return porcentajeCaja;
	}
	public void setPorcentajeCaja(Float porcentajeCaja) {
		this.porcentajeCaja = porcentajeCaja;
	}
	public Float getPorcentajePaquete() {
		return porcentajePaquete;
	}
	public void setPorcentajePaquete(Float porcentajePaquete) {
		this.porcentajePaquete = porcentajePaquete;
	}
	public Float getPorcentajeDescuentoCaja() {
		return porcentajeDescuentoCaja;
	}
	public void setPorcentajeDescuentoCaja(Float porcentajeDescuentoCaja) {
		this.porcentajeDescuentoCaja = porcentajeDescuentoCaja;
	}
	public Float getPorcentajeDescuentoPaquete() {
		return porcentajeDescuentoPaquete;
	}
	public void setPorcentajeDescuentoPaquete(Float porcentajeDescuentoPaquete) {
		this.porcentajeDescuentoPaquete = porcentajeDescuentoPaquete;
	}
	public Boolean getControlado() {
		return controlado;
	}
	public void setControlado(Boolean controlado) {
		this.controlado = controlado;
	}
	public Integer getCantidad() {
		return cantidad;
	}
	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}
	public Double getTotalVenta() {
		return totalVenta;
	}
	public void setTotalVenta(Double totalVenta) {
		this.totalVenta = totalVenta;
	}
	public String convertLiteral(Float inventario, Integer cantidad) {
		if(cantidad <= inventario) {
			if(cantidad == 0)
				return "Muy bajo";
			else
				return "Bajo";
		} else {
			if(cantidad > (inventario * 2)) {
				return "Muy alto";
			} else {
				return "Alto";
			}
		}
	}
	public String convertColor(Float inventario, Integer cantidad) {
		if(cantidad <= inventario) {
			if(cantidad == 0)
				return "danger";
			else
				return "warning";
		} else {
			if(cantidad > (inventario * 2)) {
				return "primary";
			} else {
				return "success";
			}
		}
	}
	public String getClasificacionUnidadLiteral() {
		if(inventarioMinimoUnidad != null && cantidad != null) {
			return convertLiteral(inventarioMinimoUnidad, cantidad);
		}
		return "";
	}
	public String getClasificacionCajaLiteral() {
		if(inventarioMinimoCaja != null && cantidad != null) {
			return convertLiteral(inventarioMinimoCaja, cantidad);
		}
		return "";
	}
	public String getClasificacionPaqueteLiteral() {
		if(inventarioMinimoPaquete != null && cantidad != null) {
			return convertLiteral(inventarioMinimoPaquete, cantidad);
		}
		return "";
	}
	public String getClasificacionUnidadColor() {
		if(inventarioMinimoUnidad != null && cantidad != null) {
			return convertColor(inventarioMinimoUnidad, cantidad);
		}
		return "";
	}
	public String getClasificacionCajaColor() {
		if(inventarioMinimoCaja != null && cantidad != null) {
			return convertColor(inventarioMinimoCaja, cantidad);
		}
		return "";
	}
	public String getClasificacionPaqueteColor() {
		if(inventarioMinimoPaquete != null && cantidad != null) {
			return convertColor(inventarioMinimoPaquete, cantidad);
		}
		return "";
	}
}
