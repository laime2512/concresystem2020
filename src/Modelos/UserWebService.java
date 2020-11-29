package Modelos;

public class UserWebService {
	private String nombre,ap,am,ci,login;

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
		return (am!=null?am:"");
	}

	public void setAm(String am) {
		this.am = am;
	}

	public String getCi() {
		return (ci!=null?ci:"");
	}

	public void setCi(String ci) {
		this.ci = ci;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}
}
