import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
 

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
 

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;
import org.opencv.imgproc.Imgproc;


public class Main {

	
	static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }
	
	static Mat imag = null;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		
		JFrame jframe = new JFrame("DETECTION DE CONTOURS POUR LES TRISOMIQUES");
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JLabel vidpanel = new JLabel();
        jframe.setContentPane(vidpanel);
        jframe.setSize(640, 480);
        jframe.setVisible(true);
        
        Mat frame = new Mat();
        Size sz = new Size(320, 240);
        VideoCapture camera = new VideoCapture(0);
        int i = 0;
        ImageProcessor image1 = new ImageProcessor();
 
      //Creer les objets
		Circle cercle = new Circle();
		SobelEdgeDetector detector = new SobelEdgeDetector();
		FilesManager manager = new FilesManager();
		BufferedImage edges = null;
				
		//Niveau de gradient
		detector.setGradientLevel(100); 
        

        while (true) {
            if (camera.read(frame)) {
            	
            	Imgproc.resize(frame, frame, sz);
                imag = frame.clone();
                
                if (i == 0) {
                    jframe.setSize(frame.width(), frame.height());
                    detector.setSourceImage(Mat2bufferedImage(frame));
    				detector.process();	
                }
                
                if(i==1){
    		
    				detector.setSourceImage(Mat2bufferedImage(frame));
    				detector.process();	
    				edges = detector.getEdgesImage();
    				ImageIcon image = new ImageIcon(edges);
    	            vidpanel.setIcon(image);
    	            vidpanel.repaint();
                }
 
            }
            i = 1;
                        
            
        }
    }
 
    public static BufferedImage Mat2bufferedImage(Mat image) {
        MatOfByte bytemat = new MatOfByte();
        Highgui.imencode(".jpg", image, bytemat);
        byte[] bytes = bytemat.toArray();
        InputStream in = new ByteArrayInputStream(bytes);
        BufferedImage img = null;
        try {
            img = ImageIO.read(in);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return img;
    }				
				
}
