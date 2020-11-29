package Modelos;

public class General {
	private Integer codgen;
	private String nomgen,dirgen,telgen,luggen,sucgen,loggen,urlgen;
	private String gerente;

	public General() {
	}
	
	public General(int ges) {
		super();
		this.codgen = ges;
		this.nomgen = "Gestion 2018";
		this.dirgen = "Atilio Ruiz ";
		this.telgen = "75034101";
		this.luggen = "Tarija-Bolivia";
		this.sucgen = "Matriz Central";
	}

	public String getGerente() {
		return gerente;
	}
	public void setGerente(String gerente) {
		this.gerente = gerente;
	}
	public String getUrlgen() {
		return urlgen;
	}
	public void setUrlgen(String urlgen) {
		this.urlgen = urlgen;
	}
	public Integer getCodgen() {
		return codgen;
	}
	public void setCodgen(Integer codgen) {
		this.codgen = codgen;
	}
	public String getNomgen() {
		return nomgen;
	}
	public void setNomgen(String nomgen) {
		this.nomgen = nomgen;
	}
	public String getDirgen() {
		return dirgen;
	}
	public void setDirgen(String dirgen) {
		this.dirgen = dirgen;
	}
	public String getTelgen() {
		return telgen;
	}
	public void setTelgen(String telgen) {
		this.telgen = telgen;
	}
	public String getLuggen() {
		return luggen;
	}
	public void setLuggen(String luggen) {
		this.luggen = luggen;
	}
	public String getSucgen() {
		return sucgen;
	}
	public void setSucgen(String sucgen) {
		this.sucgen = sucgen;
	}
	public String getLoggen() {
		return loggen;
	}
	public void setLoggen(String loggen) {
		this.loggen = loggen;
	}
}
