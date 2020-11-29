package Utiles;

import java.math.BigDecimal;
import java.math.RoundingMode;


/**
 * @author Carlos Franz Gutierrez G
 */
public class Numeros {

    private static final String[] UNIDADES = { "", "uno ", "dos ", "tres ",
            "cuatro ", "cinco ", "seis ", "siete ", "ocho ", "nueve ", "diez ",
            "once ", "doce ", "trece ", "catorce ", "quince ", "dieciseis",
            "diecisiete", "dieciocho", "diecinueve", "veinte" };

    private static final String[] DECENAS = { "","","venti", "treinta ", "cuarenta ",
            "cincuenta ", "sesenta ", "setenta ", "ochenta ", "noventa ",
            "cien " };

    private static final String[] CENTENAS = { "","ciento ", "doscientos ",
            "trescientos ", "cuatrocientos ", "quinientos ", "seiscientos ",
            "setecientos ", "ochocientos ", "novecientos " };
    
    private static final int[] numeros = {100,20,0,0};
    
    private static final String[][] LITERALES={CENTENAS,DECENAS,UNIDADES};
    
    public static String convertir(int x){
    	
    	String literal="";
    	
    	int i=0;
    	
    	boolean y=false;
    	try {
		
    	while (x>0) {
    		switch (i) {
    		
    		case 0:
    			
    			if(x>numeros[i]){
    				
    				literal+=LITERALES[i][x/numeros[i]];
        			
        			x%=numeros[i];
    				
    			}
    			
    			break;
    			
    		case 1:
    			
    			if(x>numeros[i]){
    				
    				if(x/(numeros[i]-10)==2)y=false;
    				
    				else y=true;
    				
    				literal+=LITERALES[i][x/(numeros[i]-10)];
    			
    				x%=(numeros[i]-10);
    			
    			}
    			
    			break;

    		default:
    			
    			if(x>numeros[i]){
    			
    			if(y)if((x/(numeros[i]+1))==20)literal+=LITERALES[i][x/(numeros[i]+1)];
    				
    			else literal+="y "+LITERALES[i][x/(numeros[i]+1)];
    			
    			else literal+=LITERALES[i][x/(numeros[i]+1)];
    			
    			x%=(numeros[i]+1);
    			
    			break;
    		}
    		
    		}
			
    		i++;
			
		}
    	
		} catch (Exception e) {
//			System.out.println("error al convertir="+e.toString());
		}
    	return literal;
    	
    }
    
    public static String convertirALiteral(double numero){
    	
    	int entero=(int)numero;
    	
    	String cadena="";
    	
    	if(entero>=1000000){
    		
    		if(entero/1000000==1)cadena+="un millon ";
    		
    		else cadena+=convertir(entero/1000000)+"millones ";
    		
    		entero%=1000000;
    		
    	}
    	
    	if(entero>=1000){
    		
    		if(entero/1000==1)cadena+="mil ";
    		
    		else cadena+=convertir(entero/1000)+"mil ";
    		
    		entero%=1000;
    		
    	}
    	
    	if(entero>0)cadena+=convertir(entero);
    	
    	return cadena;
    	
    }
    
 public static String convertirALiteral2(float numero){
    	
    	int entero;
    	String cadena="";
    	try {
			
		
    	entero=(int)numero;
    	
    	
    	if(entero>=1000000){
    		
    		if(entero/1000000==1)cadena+="un millon ";
    		
    		else cadena+=convertir(entero/1000000)+"millones ";
    		
    		entero%=1000000;
    		
    	}
    	
    	if(entero>=1000){
    		
    		if(entero/1000==1)cadena+="mil ";
    		
    		else cadena+=convertir(entero/1000)+"mil ";
    		
    		entero%=1000;
    		
    	}
    	
    	if(entero>0)cadena+=convertir(entero);
    	} catch (Exception e) {
//			System.out.println("error al parsear="+e.toString());
		}
    	return cadena;
    	
    }
 	public static double roundMoney(double monto) {
		double entero = Math.round(monto);
		double parteDecimal = monto - entero;
		parteDecimal = parteDecimal * 100;
		double decimalRedondeado = Math.round(parteDecimal);
		double round2Decimal = entero + (Math.round(decimalRedondeado)/100f);
		String decimalString = String.valueOf(round2Decimal);
		int ind = decimalString.indexOf(".");
		decimalString = ind>-1?decimalString.substring(ind+1):"";
		String valor2 = decimalString.length()>=2?decimalString.substring(1,2):"0";
		int decimal2 = Integer.parseInt(valor2);
		if(decimal2 > 0) {
			decimal2 = 10 - decimal2;
			double resto = Float.parseFloat("0.0"+decimal2);
			round2Decimal += resto; 
		}
		return formato2decimales(round2Decimal);
	}
    public static float roundMoney(float monto) {
    	float round2Decimal = formato2decimales(monto);
    	String decimalString = String.valueOf(round2Decimal);
    	int ind = decimalString.indexOf(".");
    	decimalString = ind>-1?decimalString.substring(ind+1):"";
    	String valor2 = decimalString.length()>=2?decimalString.substring(1,2):"0";
    	int decimal2 = Integer.parseInt(valor2);
    	if(decimal2 > 0) {
    		decimal2 = 10 - decimal2;
    		float resto = Float.parseFloat("0.0"+decimal2);
    		round2Decimal += resto; 
    	}
    	return formato2decimales(round2Decimal);
    }
    public static double formato2decimales(double x){
    	BigDecimal numero = new BigDecimal(x).setScale(2,RoundingMode.HALF_EVEN);
    	return numero.doubleValue();
    }
    public static float formato2decimales(float x){
    	BigDecimal numero = new BigDecimal(x).setScale(2,RoundingMode.HALF_EVEN);
    	return numero.floatValue();
    }
    
    public static String obtenerDecimales(double numero){
    	String cadena=String.valueOf(numero);
    	int i=cadena.indexOf(".");
    	if(i>-1){
    		String subcadena=cadena.substring(i+1);
    		if(subcadena.length()>1)return subcadena.substring(0,2);
    		return subcadena+"0";
    	}
    	return "00";
    }

}