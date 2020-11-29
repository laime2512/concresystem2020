package Utiles;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Fechas {
	private static final String MES_LITERAL[]= {"","Enero","Febrero","Marzo","Abril","Mayo","Junio","Julio","Agosto","Septiembre","Octubre","Noviembre","Diciembre"};
	public static DateTimeFormatter formatDate(String pattern) {
		DateTimeFormatter formatter=DateTimeFormatter.ofPattern(pattern);
		return formatter;
	}
	
	public static String obtenerFecha(String pattern){
		LocalDate date=LocalDate.now();
		return date.format(formatDate(pattern));
	}
	public static int obtenerDiaSemanaFecha(Date d){
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(d);
		return cal.get(Calendar.DAY_OF_WEEK);
		
	}
	public static java.sql.Date ConvertirFecha(String fecha){
		return java.sql.Date.valueOf(fecha.substring(6,10)+"-"+fecha.substring(3,5)+"-"+fecha.substring(0,2));
	}
	public static String obtenerFechaLiteral(LocalDate localDate){
		String fecha=Constantes.CIUDAD+", ";
		fecha += formatDate("dd").format(localDate)+" de ";
		fecha += MES_LITERAL[localDate.getMonthValue()]+" del ";
		fecha += formatDate("yyyy").format(localDate);
		return fecha;
	}
	public static String obtenerFechaLiteralActual(){
		LocalDate localDate =LocalDate.now();
		String fecha=Constantes.CIUDAD+", ";
		fecha += formatDate("dd").format(localDate)+" de ";
		fecha += MES_LITERAL[localDate.getMonthValue()]+" del ";
		fecha += formatDate("yyyy").format(localDate);
		return fecha;
	}
	public static String obtenerFechaLiteral(String stringDate, String format){
		LocalDate localDate= convertStringToLocalDate(stringDate, format);
		String fecha="";
		fecha += formatDate("dd").format(localDate)+" de ";
		fecha += MES_LITERAL[localDate.getMonthValue()]+" del ";
		fecha += formatDate("yyyy").format(localDate);
		return fecha;
	}
	public Date sumarRestarDiasFecha(Date fecha,int dias){
		Calendar calendar=Calendar.getInstance();
		calendar.setTime(fecha);
		calendar.add(Calendar.DAY_OF_YEAR, dias);
		return calendar.getTime();
	}
	
	public Date sumarRestarHorasFecha(Date fecha,int horas){
		Calendar calendar=Calendar.getInstance();
		calendar.setTime(fecha);
		calendar.add(Calendar.HOUR, horas);
		return calendar.getTime();
	}
	public static long DiferenciaFechas(String vinicio, String vfinal){
		 
        Date dinicio = null, dfinal = null;
        long milis1, milis2, diff;

        SimpleDateFormat sdf= new SimpleDateFormat("dd/MM/yyyy");

        try {
                // PARSEO STRING A DATE
                dinicio = sdf.parse(vinicio);
                dfinal = sdf.parse(vfinal);                    
               
        } catch (ParseException e) {

                System.out.println("Se ha producido un error en el parseo");
        }
       
        //INSTANCIA DEL CALENDARIO GREGORIANO
        Calendar cinicio = Calendar.getInstance();
        Calendar cfinal = Calendar.getInstance();

        //ESTABLECEMOS LA FECHA DEL CALENDARIO CON EL DATE GENERADO ANTERIORMENTE
         cinicio.setTime(dinicio);
         cfinal.setTime(dfinal);


     milis1 = cinicio.getTimeInMillis();

     milis2 = cfinal.getTimeInMillis();


     diff = milis2-milis1;


 // calcular la diferencia en dias

 long diffdias = Math.abs ( diff / (24 * 60 * 60 * 1000) );

        return diffdias+1;
}
	
	public static Date cambio(String F)
	   {
		Date d2=null;
	       DateFormat df2 = new SimpleDateFormat("dd/MM/yyyy HH:mm");
	       try
	       {
	           d2 = df2.parse(F);
	       }
	       catch (Exception ex ){
	          System.out.println(ex);
	       }
	       return  new java.sql.Timestamp(d2.getTime());
	   }
	public static String ultimodiaMes(int mes, int anio){
		 switch(mes){
		  case 0:  // Enero
		  case 2:  // Marzo
		  case 4:  // Mayo
		  case 6:  // Julio
		  case 7:  // Agosto
		  case 9:  // Octubre
		  case 11: // Diciembre
		   return "31";
		  case 3:  // Abril
		  case 5:  // Junio
		  case 8:  // Septiembre
		  case 10: // Noviembre
		   return "30";
		  case 1:  // Febrero
		   if ( ((anio%100 == 0) && (anio%400 == 0)) ||
		        ((anio%100 != 0) && (anio%  4 == 0))   )
		    return "29";  // A�o Bisiesto
		   else
		    return "28";
		  default:
		 		 }
		 return "0";
}
	/**
	 * Devuelve un String de la fecha actual con el formato especificado
	 * @param format
	 * @return
	 */
	public static String getDateNowToString(String format){
		LocalDate date =LocalDate.now();
		return date.format(DateTimeFormatter.ofPattern(format));
	}
	/**
	 * Devuelve un String de la fecha actual con el formato especificado en el proyecto
	 * @return
	 */
	public static String getDateNowToString(){
		LocalDate date =LocalDate.now();
		return date.format(DateTimeFormatter.ofPattern(Constantes.FORMAT_DATE));
	}
	/**
	 * Devuelve un String de la fecha actual con el formato especificado en el proyecto
	 * @return
	 */
	public static String getDateTimeNowToString(){
		LocalDate date =LocalDate.now();
		return date.format(DateTimeFormatter.ofPattern(Constantes.FORMAT_DATETIME));
	}
	/**
	 * Devuelve un String a partir de un LocalDate con el formato especificado en el proyecto
	 * @param localDate
	 * @return
	 */
	public static String convertLocalDateToString(LocalDate localDate) {
		DateTimeFormatter formato=DateTimeFormatter.ofPattern(Constantes.FORMAT_DATE);
		return localDate.format(formato);
	}
	/**
	 * Devuelve un String a partir de un LocalDate y un formato especificado
	 * @param localDate
	 * @param format
	 * @return
	 */
	public static String convertLocalDateToString(LocalDate localDate,String format) {
		DateTimeFormatter formato=DateTimeFormatter.ofPattern(format);
		return localDate.format(formato);
	}
	/**
	 * Devuelve un LocalDate a partir de un Date y un formato especificado
	 * @param sDate
	 * @param format
	 * @return
	 */
	public static LocalDate convertStringToLocalDate(String sDate,String format) {
		DateTimeFormatter formato=DateTimeFormatter.ofPattern(format);
		return LocalDate.parse(sDate, formato);
	}
	/**
	 * Devuelve un LocalDate a partir de una fecha indicada y con el formato especificado en el proyecto
	 * @param sDate
	 * @return
	 */
	public static LocalDate convertStringToLocalDate(String sDate) {
		DateTimeFormatter formato=DateTimeFormatter.ofPattern(Constantes.FORMAT_DATE);
		return LocalDate.parse(sDate, formato);
	}
	/**
	 * Devuelve un Date a partir a partir de una fecha indicada pero teniendo en cuenta que debeestar en el formato del proyecto
	 * @param sDate
	 * @return
	 * @throws ParseException
	 */
	public static Date convertirStringToDate(String sDate) {
		try {
			SimpleDateFormat format=new SimpleDateFormat(Constantes.FORMAT_DATE);
			return format.parse(sDate);
		} catch (ParseException e) {
			return null;
		}
		
	}
	/**
	 * Devuelve un Date a partir de una fecha y con el formato especificado 
	 * @param sDate
	 * @param pattern
	 * @return
	 * @throws ParseException
	 */
	public static Date convertirStringToDate(String sDate,String pattern){
		try {
			SimpleDateFormat format=new SimpleDateFormat(pattern);
			return format.parse(sDate);
		} catch (ParseException e) {
			return null;
		}
		
	}
	/**
	 * Devuelve un String a partir de un LocaldateTime segun el formato especificado en el sistema
	 * @param localDateTime
	 * @return
	 */
	public static String convertLocalDateTimeToString(LocalDateTime localDateTime) {
		DateTimeFormatter formato=DateTimeFormatter.ofPattern(Constantes.FORMAT_DATE);
		return localDateTime.format(formato);
	}
	/**
	 * Devuelve un String a partir de un LocalDateTime y formato especificado
	 * @param localDateTime
	 * @param format
	 * @return
	 */
	public static String convertLocalDateTimeToString(LocalDateTime localDateTime,String format) {
		DateTimeFormatter formato=DateTimeFormatter.ofPattern(format);
		return localDateTime.format(formato);
	}
	/**
	 * Devuelve un LocalDateTime a partir de una fecha String y con un formato especificado
	 * @param sDate
	 * @param format
	 * @return
	 */
	public static LocalDateTime convertStringToLocalDateTime(String sDate,String format) {
		DateTimeFormatter formato=DateTimeFormatter.ofPattern(format);
		return LocalDateTime.parse(sDate, formato);
	}
	/**
	 * Devuelve un LocalDateTime a partir de una Fecha String especificado pero esta fecha debe estar en el formato del proyecto 
	 * @param sDate
	 * @return
	 */
	public static LocalDateTime convertStringToLocalDateTime(String sDate) {
		DateTimeFormatter formato=DateTimeFormatter.ofPattern(Constantes.FORMAT_DATE);
		return LocalDateTime.parse(sDate, formato);
	}
	
	}
