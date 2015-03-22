import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
 



import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

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

        /*JPanel main = new JPanel();
        main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));
        JPanel vidpanel = new JPanel();
        JPanel vidpanel1 = new JPanel();*/
		
		JFrame jframe = new JFrame("DETECTION DE CONTOURS POUR LES TRISOMIQUES");
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JLabel label0 = new JLabel();    
        jframe.setSize(640, 480);
        jframe.setContentPane(label0);   
        jframe.setVisible(true);
        
        Mat frame = new Mat();
        Size sz = new Size(640, 480);
        

        JFrame jframe1 = new JFrame("DETECTION DE CONTOURS POUR LES TRISOMIQUES 2");
        jframe1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JLabel label1 = new JLabel();
        jframe1.setContentPane(label1);
        jframe1.setSize(640, 480);
        jframe1.setVisible(true);
        
       /* JFrame jframe2 = new JFrame("DETECTION DE CONTOURS POUR LES TRISOMIQUES 3");
        jframe2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JLabel label2 = new JLabel();
        jframe2.setContentPane(label2);
        jframe2.setSize(640, 480);
        jframe2.setVisible(true);
        
       /* JFrame jframe3 = new JFrame("DETECTION DE CONTOURS POUR LES TRISOMIQUES 4");
        jframe1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JLabel label3 = new JLabel();
        jframe1.setContentPane(label3);
        jframe1.setSize(640, 480);
        jframe1.setVisible(true);*/
        
        VideoCapture camera = new VideoCapture(0);
        int i = 0;
 
      //Creer les objets
		SobelEdgeDetectorLive detector = new SobelEdgeDetectorLive();
		BufferedImage edges = null;
		BufferedImage gradx = null;
		BufferedImage grady = null;
				
		//Niveau de gradient
		detector.setGradientLevel(100); 
        

        while (true) {
            if (camera.read(frame)) {
            	
            	Imgproc.resize(frame, frame, sz);
                imag = frame.clone();
                Imgproc.cvtColor(frame, frame, Imgproc.COLOR_BGR2GRAY);
                Imgproc.GaussianBlur(frame, frame, new Size(3, 3), 0);
                
                if (i == 0) {
                    jframe.setSize(frame.width(), frame.height());
                    detector.setSourceImage(Mat2bufferedImage(frame));
    				detector.process("Optimisation");
                }
                
                if(i==1){
    		
    				detector.setSourceImage(Mat2bufferedImage(frame));
    				detector.process("Optimisation");
    				edges = detector.getEdgesImage();
    				
    				ImageIcon image0 = new ImageIcon(edges);
    	            label0.setIcon(image0);
    	            label0.repaint();
    				
    				ImageIcon image1 = new ImageIcon(Mat2bufferedImage(imag));
    	            label1.setIcon(image1);
    	            label1.repaint();    	            
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
