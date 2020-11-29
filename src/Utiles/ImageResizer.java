package Utiles;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageResizer {
	//Ancho máximo
    public static int MAX_WIDTH=800;
    //Alto máximo
    public static int MAX_HEIGHT=800;
 
    /*Este método es el de la magia recibe la ruta al archivo original y la ruta donde vamos a guardar la copia
    copyImage("C:\\Users\\IngenioDS\\Desktop\\test.png","C:\\Users\\IngenioDS\\Desktop\\Copia\\test2.png");*/
 
    public static void copyImage(String filePath, String copyPath) {
        BufferedImage bimage = loadImage(filePath);
        if(bimage.getHeight()>bimage.getWidth()){
            int heigt = (bimage.getHeight() * MAX_WIDTH) / bimage.getWidth();
            bimage = resize(bimage, MAX_WIDTH, heigt);
            int width = (bimage.getWidth() * MAX_HEIGHT) / bimage.getHeight();
            bimage = resize(bimage, width, MAX_HEIGHT);
        }else{
            int width = (bimage.getWidth() * MAX_HEIGHT) / bimage.getHeight();
            bimage = resize(bimage, width, MAX_HEIGHT);
            int heigt = (bimage.getHeight() * MAX_WIDTH) / bimage.getWidth();
            bimage = resize(bimage, MAX_WIDTH, heigt);
        }
        saveImage(bimage, copyPath);
    }
     
    /*
    Este método se utiliza para cargar la imagen de disco
    */
    public static BufferedImage loadImage(String pathName) {
        BufferedImage bimage = null;
        try {
            bimage = ImageIO.read(new File(pathName));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bimage;
    }
 
    /*
    Este método se utiliza para almacenar la imagen en disco
    */
    public static boolean saveImage(BufferedImage bufferedImage, String pathName) {
        try {
            String format = (pathName.endsWith(".png")) ? "png" : "jpg";
            File file =new File(pathName);
            file.getParentFile().mkdirs();
            ImageIO.write(bufferedImage, format, file);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
     
    /*
    Este método se utiliza para redimensionar la imagen
    */
    public static BufferedImage resize(BufferedImage bufferedImage, int newW, int newH) {
        int w = bufferedImage.getWidth();
        int h = bufferedImage.getHeight();
        BufferedImage bufim = new BufferedImage(newW, newH, bufferedImage.getType());
        Graphics2D g = bufim.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(bufferedImage, 0, 0, newW, newH, 0, 0, w, h, null);
        g.dispose();
        return bufim;
    }
    public SizeImage calcularReduccion(int width,int height) {
    	SizeImage sizeImage=new SizeImage(width,height);
    	if(width>Constantes.SIZE_REDUCCION && height>Constantes.SIZE_REDUCCION) {
    		double x=0;
    		if(width>height)
    			x=(Constantes.SIZE_REDUCCION*100.0)/width;
    		else
    			x=(Constantes.SIZE_REDUCCION*100.0)/height;
    		double valX=width*(x/100.0);
    		double valY=height*(x/100.0);
    		sizeImage.setWidth((int)valX);
    		sizeImage.setHeight((int)valY);
    	}
    	return sizeImage;
    }
    
    public class SizeImage{
    	private int width;
    	private int heght;
    	
		public SizeImage() {
			super();
		}
		
		public SizeImage(int width, int heght) {
			super();
			this.width = width;
			this.heght = heght;
		}

		@Override
		public String toString() {
			return "SizeImage [width=" + width + ", heght=" + heght + "]";
		}
		public int getWidth() {
			return width;
		}
		public void setWidth(int width) {
			this.width = width;
		}
		public int getHeight() {
			return heght;
		}
		public void setHeight(int heght) {
			this.heght = heght;
		}
    	
    }
}