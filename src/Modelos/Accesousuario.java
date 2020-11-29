package Modelos;



public class Accesousuario {
	private String login;
	private String passwd;
	private Integer estado;
	private Long codusu;
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public String getPasswd() {
		return passwd;
	}
	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}
	public Integer getEstado() {
		return estado;
	}
	public void setEstado(Integer estado) {
		this.estado = estado;
	}
	public Long getCodusu() {
		return codusu;
	}
	public void setCodusu(Long codusu) {
		this.codusu = codusu;
	}
	@Override
	public String toString() {
		return "Accesousuario [login=" + login + ", passwd=" + passwd + ", estado=" + estado + ", codusu=" + codusu
				+ "]";
	}
	
}