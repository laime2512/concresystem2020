package Utiles.Facturacion;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import Utiles.Facturacion.*;
/**
 * @see https://www.jc-mouse.net/
 * @author mouse
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        ControlCode controlCode = new ControlCode();                
        //direccion del archivo de texto
        String fileName = "C:\\Users\\Royeth\\WorkspaceHome\\concretar2\\src\\Utiles\\Facturacion\\5000CasosPruebaCCVer7.txt";        
        
        int count=0;        
        int fiveDigitsVerhoeffCount=0;
        int stringDKeyCount=0;
        int sumProductCount=0;
        int base64SINCount=0;
        int ccCount=0;
        
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                count+=1;
                //reemplaza "|" por "/-/" por no ser compatible con el metodo split
                line = line.replace("|", "/-/");
                String[] ary = line.split("/-/");
                //genera codigo de control
                String cc = controlCode.generate(ary[0], ary[1], ary[2], ary[3].replace("/", ""), ary[4], ary[5]);                
                //controla errores
                System.out.println(cc);
                //if(!ary[6].equals(controlCode.getFiveDigitsVerhoeff()))fiveDigitsVerhoeffCount+=1;                
                //if(!ary[7].equals(controlCode.getStringDKey()))stringDKeyCount+=1;                
                //if(!ary[8].equals(String.valueOf(controlCode.getSumProduct())))sumProductCount+=1;                      
               // if(!ary[9].equals(String.valueOf(controlCode.getBase64SIN())))base64SINCount+=1;                      
               // if(!ary[10].equals(cc))ccCount+=1;
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
                
        System.out.println("Error 5 digitos Verhoeff: " + fiveDigitsVerhoeffCount);
        System.out.println("Error Cadena de dosificación: " + stringDKeyCount);
        System.out.println("Error Suma Producto: " + sumProductCount);
        System.out.println("Error Base64: " + base64SINCount);
        System.out.println("Error codigo de control: " + ccCount);
        System.out.println("---------------------------------------------");
        System.out.println("Total Registros testeados: " + count);        
    }
    
}//end:class