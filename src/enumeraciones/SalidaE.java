package enumeraciones;
/**
 * 
 * @author CARLOS
1= Productos en mal estado,
2= Salida a otra farmacia egreso,
3=Medicamentos vencidos,
4= Perdida de medicamentos,
5= Uso de farmacia,
6= Disminucion de medicamento por usuario
7=Aumento de medicamento por usuario
8=Entrada especial como regalo de proveedor
9=salida especial por x motivo
10=entrada de otra farmacia

 */
public enum SalidaE {
	MAL_ESTADO(1),
	TRASPASO_ENTRE_FARMACIA_EGRESO(2),
	VENCIDO(3),
	PERDIDA(4),
	USO_EN_FARMACIA(5),
	DISMINUCION_ALMACEN_POR_USUARIO(6),
	AUMENTO_ALMACEN_POR_USUARIO(7),
	ENTRADA(8),
	SALIDA(9),
	TRASPASO_ENTRE_FARMACIA_INGRESO(10);
	private final Integer tipo;
	private SalidaE(Integer tipo) {
		this.tipo = tipo;
	}
	public Integer getTipo() {
		return tipo;
	}
	
}
