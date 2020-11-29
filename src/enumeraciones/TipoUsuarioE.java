package enumeraciones;

public enum TipoUsuarioE {
	ADMINISTRATIVO("A"),
	PUBLICO("P");
	public final String value;
	private TipoUsuarioE(String value) {
		this.value = value;
	}
}
