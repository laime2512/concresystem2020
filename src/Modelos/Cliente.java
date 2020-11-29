package Modelos;

public class Cliente {
	private Long codcli;
	private String nit, direccion, celular, razon_nit;
	private int estado;

	public String getRazon_nit() {
		return razon_nit;
	}

	public void setRazon_nit(String razon_nit) {
		this.razon_nit = razon_nit;
	}
	public Long getCodcli() {
		return codcli;
	}

	public void setCodcli(Long codcli) {
		this.codcli = codcli;
	}

	public String getNit() {
		return nit;
	}

	public void setNit(String nit) {
		this.nit = nit;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getCelular() {
		return celular;
	}

	public void setCelular(String celular) {
		this.celular = celular;
	}

	public int getEstado() {
		return estado;
	}

	public void setEstado(int estado) {
		this.estado = estado;
	}

	@Override
	public String toString() {
		return "Cliente [codcli=" + codcli + ", nit=" + nit + ", direccion=" + direccion + ", celular=" + celular
				+ ", razon_nit=" + razon_nit + ", estado=" + estado + "]";
	}

}
