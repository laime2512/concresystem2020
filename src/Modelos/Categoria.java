package Modelos;

public class Categoria {
	private String nomcat;
	private int codcat,estado;
	public int getCodcat() {
		return codcat;
	}

	public void setCodcat(int codcat) {
		this.codcat = codcat;
	}

	public int getEstado() {
		return estado;
	}

	public void setEstado(int estado) {
		this.estado = estado;
	}

	public String getNomcat() {
		return nomcat;
	}

	public void setNomcat(String nomcat) {
		this.nomcat = nomcat;
	}

	@Override
	public String toString() {
		return "Categoria [nomcat=" + nomcat + ", codcat=" + codcat + ", estado=" + estado + "]";
	}
}
