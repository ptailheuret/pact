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

		/*
		 * JPanel main = new JPanel(); main.setLayout(new BoxLayout(main,
		 * BoxLayout.Y_AXIS)); JPanel vidpanel = new JPanel(); JPanel vidpanel1
		 * = new JPanel();
		 */

		JFrame jframe = new JFrame("DETECTION DE CONTOURS POUR LES TRISOMIQUES");
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JLabel label0 = new JLabel();
		jframe.setSize(640, 480);
		jframe.setContentPane(label0);
		jframe.setVisible(true);

		Mat frame = new Mat();
		Size sz = new Size(640*0.8, 480*0.8);

		JFrame jframe1 = new JFrame("DETECTION DE CONTOURS POUR LES TRISOMIQUES 2");
		jframe1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JLabel label1 = new JLabel();
		jframe1.setContentPane(label1);
		jframe1.setSize(640, 480);
		jframe1.setVisible(true);

		VideoCapture camera = new VideoCapture(0);
		int i = 0;
		int x = 0;
		int y = 0;
		int r = 0;
		int xsaved = 0;
		int ysaved = 0;

		// Creer les objets
		SobelEdgeDetectorLive detector = new SobelEdgeDetectorLive();
		BufferedImage edges = null;
		Circle cercle = new Circle();
		ImageProcessor imageProcessor = new ImageProcessor();

		ArrayList<Point> listeDePoints = new ArrayList<Point>();

		// Distance au cercle pour être inliner
		cercle.setDistanceInliners(100);

		// Nombre d'inliners suffisant pour arrêter
		cercle.setNombreInliners(1000);
		cercle.setNombreDIterations(1500);
		// Niveau de gradient
		detector.setGradientLevel(100);
		detector.setCercleTrace(false);

		while (true) {
			if (camera.read(frame)) {

				Imgproc.resize(frame, frame, sz);
				imag = frame.clone();
				Imgproc.resize(imag, imag, sz);
				Imgproc.cvtColor(frame, frame, Imgproc.COLOR_BGR2GRAY);
				Imgproc.GaussianBlur(frame, frame, new Size(3, 3), 0);

				if (i == 0) {
					jframe.setSize(frame.width(), frame.height());
					jframe1.setSize(imag.width(), imag.height());
					// detector.setSourceImage(Mat2bufferedImage(frame));
					// detector.process("Optimisation");

					// listeDePoints=SobelEdgeDetectorLive.getListPoints();
				}

				if (i == 1) {

					detector.setSourceImage(Mat2bufferedImage(frame));
					detector.process("Optimisation");

					listeDePoints = SobelEdgeDetectorLive.getListPoints();
					// cercle.ransac(listeDePoints, "Optimis�");

					edges = detector.getEdgesImage();

					ImageIcon image0 = new ImageIcon(edges);
					label0.setIcon(image0);
					label0.repaint();

					/*
					 * if(compteur%1 == 0){ //Definir le meilleur cercle Circle
					 * bestCircle = cercle.getBestCircle(); x =
					 * bestCircle.circleCenter().getX(); y =
					 * bestCircle.circleCenter().getY(); r = (int)
					 * bestCircle.radius(); }
					 * 
					 * 
					 * 
					 * //Dessine le cercle obtenu BufferedImage imag0 =
					 * Mat2bufferedImage(imag); if(100<r && r<200 &&
					 * Math.abs(x-xsaved)<100) imageProcessor.drawCircle(imag0,
					 * x, frame.height() - y, r);
					 * 
					 * if(compteur%1 == 0){ xsaved = x; ysaved = y; }
					 */

					BufferedImage imag1 = Mat2bufferedImage(imag);
					ImageIcon image1 = new ImageIcon(imag1);
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
