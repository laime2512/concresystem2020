package Utiles;

public class Constantes {
	public static final String COMPANY = "Farmacia MIA SUPER";
	public static final String SIGLA = "MIA SUPER";
	public static class Salida {
		public static final boolean TIPO_ENTRADA =true;
		public static final boolean TIPO_SALIDA =false;
	}
	public static class Archivo {
		public static final String RAIZ = "/archivos";
		public static final String RUTA_SALIDA = RAIZ + "/salida";
		public static final String RUTA_ENTRADA = RAIZ + "/entrada";
		public static final String RUTA_PRODUCTO = RAIZ + "/producto";
	}
	public static class Backup{
		public static final String PG_DUMP ="C:\\Program Files\\PostgreSQL\\11\\bin\\";
		public static final String IP = "localhost";
		public static final String USER = "postgres";
		public static final String DATABASE = "miasuper";
		public static final String PASSWORD = "postgres";
	}
	public static class Sesiones{
		public static final String USER = "user";
		public static final String ROLES = "roles";
		public static final String ROL = "rol";
		public static final String PEDIDO = "pedido";
		public static final String DETALLE = "detalle";
		public static final String SUCURSAL = "sucursal";
		public static final int GESTION = 2020;
	}
	public static class Compras{
		public static final int AL_CONTADO = 1;
		public static final int AL_CREDITO = 2;
		public final static String FORMA_PAGO[]= {"","AL CONTADO","AL CREDITO"};
	}
	public static class Ventas{
		public static final String CODE = "codigosVenta";
		public static final String DETALLE = "posDetalle";
		public static final String PRODUCTS = "codigos";
		public static final String CANTIDADES = "cantidades";
		public static final String PRECIOS = "precios";
		public static final int CAJA = 1;
		public static final int UNIDAD = 0;
	}
	public static class Rol{
		public static final int PUBLICO = 5;
	}
	public static class Pedidos{
		public static final int MAX_CANTIDAD_PEDIDO = 50;
		public static final int RECHAZADO = 0;
		public static final int PENDIENTE = 1;
		public static final int ENVIADO = 2;
		public static final int ENTREGADO = 3;
		public static final String ESTADO[] = {"RECHAZADOS","PENDIENTES","ENVIADOS","ENTREGADOS"};
	}
	public static class ESTADO{
		public static final int PENDIENTE = 1;
		public static final int ACEPTADO = 2;
		public static final int RECHAZADO = 0;
	}
	public final static String CIUDAD = "Tarija";
	public final static String PAIS = "Bolivia";
	public final static String DIR_REPORTES="/reportes/";
	public final static String ROL_PUBLICO="5";
	public final static String DIR_UPLOAD="/archivos";
	public final static String DIR_UPLOAD_PRODUCTOS="/producto";
	public final static String DIR_UPLOAD_USER="";
	public final static String FORMAT_DATE="dd/MM/yyyy";
	public final static String FORMAT_TIME="HH:mm:ss";
	public final static String FORMAT_DATETIME="dd/MM/YYYY HH:mm:ss";
	
	public final static String IMG_DEFAULT="notimage.png";
	public final static String IMG_PRODUCTO_DEFAULT="producto.png";
	public final static String FORMAT_IMG_PRODUCTO="producto-";
	public final static String IMG_USER_DEFAULT="user.png";
	public final static String FORMAT_IMG_USER="usuario-";
	
	
	public final static String TIPO_NOTA[]= {"","NOTA DE REMISION","FACTURA"};
	
	public final static int TIPO_CUENTA_DEBE=1;
	public final static int TIPO_CUENTA_HABER=0;
	
	public final static int SIN_POSICION=0;
	public final static int SIN_MUEBLE=0;
	public final static int SIN_TIPO_MUEBLE=0;
	public final static int SIN_TIPO_PRODUCTO=0;
	public final static int SIN_CATEGORIA=0;
	public final static int SIN_LABORATORIO=0;
	public final static int SIN_AREA=0;
	public final static int SIN_MEDIDA=0;
	public final static int SIN_PRESENTACION=0;
	
	public final static int CUENTA_INGRESO=101;
	public final static int CUENTA_VENTA=102;
	public final static int CUENTA_POR_COBRAR=103;
	public final static int CUENTA_TRASPASO_INGRESO=104;
	public final static int CUENTA_REVERSION_COMPRA=105;
	public final static int CUENTA_EGRESO=201;
	public final static int CUENTA_COMPRA=202;
	public final static int CUENTA_POR_PAGAR=203;
	public final static int CUENTA_TRASPASO_EGRESO=204;
	public final static int CUENTA_REVERSION_VENTA=205;
	
	public final static int SIZE_REDUCCION=500;
	
	public final static String TIPO_SALIDA[]= {"","Productos en mal estado","Salida a otra farmacia","Medicamentos vencidos"
			,"Perdida de medicamentos","Uso de farmacia","Disminucion de medicamento por usuario",
			"Aumento de medicamento por usuario","Entrada de medicamento"};
	
}
