package Modelos;

import java.util.List;

public class Usuario {
	private Long codusu;
	private String nombre;
	private String ap;
	private String am;
	private String genero;
	private Integer estado;
	private String tipoper;
	private String foto;
	private String ci;
	private String fnac,xfnac,ecivil,alias;
	private List<Sucursal> sucursales;
	private List<Rol> roles;
	
	public String getFnac() {
		return fnac;
	}
	public void setFnac(String fnac) {
		this.fnac = fnac;
	}
	public String getXfnac() {
		return xfnac;
	}
	public void setXfnac(String xfnac) {
		this.xfnac = xfnac;
	}
	public String getEcivil() {
		return ecivil;
	}
	public void setEcivil(String ecivil) {
		this.ecivil = ecivil;
	}
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	public List<Rol> getRoles() {
		return roles;
	}
	public void setRoles(List<Rol> roles) {
		this.roles = roles;
	}
	public List<Sucursal> getSucursales() {
		return sucursales;
	}
	public void setSucursales(List<Sucursal> sucursales) {
		this.sucursales = sucursales;
	}
	public String getCi() {
		return ci;
	}
	public void setCi(String ci) {
		this.ci = ci;
	}
	private Accesousuario usuario;
	public Accesousuario getUsuario() {
		return usuario;
	}
	public void setUsuario(Accesousuario usuario) {
		this.usuario = usuario;
	}
	public Long getCodusu() {
		return codusu;
	}
	public void setCodusu(Long codusu) {
		this.codusu = codusu;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getAp() {
		return ap;
	}
	public void setAp(String ap) {
		this.ap = ap;
	}
	public String getAm() {
		return am;
	}
	public void setAm(String am) {
		this.am = am;
	}
	public String getGenero() {
		return genero;
	}
	public void setGenero(String genero) {
		this.genero = genero;
	}
	public Integer getEstado() {
		return estado;
	}
	public void setEstado(Integer estado) {
		this.estado = estado;
	}
	public String getTipoper() {
		return tipoper;
	}
	public void setTipoper(String tipoper) {
		this.tipoper = tipoper;
	}
	public String getFoto() {
		return foto;
	}
	public void setFoto(String foto) {
		this.foto = foto;
	}
	@Override
	public String toString() {
		return nombre+" "+ap+" "+(am!=null?am:"");
	}
	public String toString2() {
		return "Usuario [codusu=" + codusu + ", nombre=" + nombre + ", ap=" + ap + ", am=" + am + ", genero=" + genero
				+ ", estado=" + estado + ", tipoper=" + tipoper + ", foto=" + foto + ", ci=" + ci + ", fnac=" + fnac
				+ ", xfnac=" + xfnac + ", ecivil=" + ecivil + ", alias=" + alias + ", sucursales=" + sucursales
				+ ", roles=" + roles + ", usuario=" + usuario + "]";
	}
	
}
