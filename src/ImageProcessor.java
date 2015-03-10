import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


public class ImageProcessor {

	private int height;
	private int width;
	private int picsize;
	
	
	public ImageProcessor(int height, int width, int picsize){
		this.height=height;
		this.width=width;
		this.picsize=picsize;
	}
	
	//Charge l'image
	public void loadImage(BufferedImage inputfile, String cheminDAcces){
			inputfile = null;
			try {
				inputfile = ImageIO.read(new File(cheminDAcces));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
	//Enregistre l'image
	public void saveImage(BufferedImage outputfile, String cheminDAcces){
			File savedfile = new File(cheminDAcces);
			try {
				ImageIO.write(outputfile, "png", savedfile);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
	//Tracer un cercle sur une image frame
	public void drawCircle(BufferedImage frame, int x, int y, int r){
		Graphics2D g2d = frame.createGraphics();
		g2d.setColor(Color.red);
		g2d.fillOval(x-r,y-r,2*r,2*r);
		g2d.drawImage(frame, 0, 0, null);			
		g2d.dispose();
	}
	
	//Redimensionner image
	
	
}
