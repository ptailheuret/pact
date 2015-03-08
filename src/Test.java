import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

//Test de changement, deuxième

public class Test {

	public static void main(String[] args) {
			
	//Selectionner les 3 points
		Point A=new Point(-10,0,0);
		Point B=new Point(10,12,1);
		Point C=new Point(351,1,1);
			
	//Tracer un cercle a partir de 3 points
		Circle cercle=new Circle(A,B,C);
		
	//Creer le detecteur
		SobelEdgeDetector detector = new SobelEdgeDetector();
			
		
	//Choix des parametres
		//Distance au cercle pour être inliner
		cercle.setDistanceInliners(25);
				
		//Nombre d'inliners suffisant pour arrêter
		cercle.setNombreInliners(15000);
		cercle.setNombreDIterations(10000);
		
		//Niveau de gradient
		detector.setGradientLevel(100);
		
		//Nombre de points permettant de dire qu'il y a un objet sur la table
		int nbMaximal = 2000;
		
		//Selection du dossier des images selon le systeme d'exploitation
		
		String dossierImages = new String();
		
		String OS = System.getProperty("os.name").toLowerCase();
				
		if(OS.indexOf("win") >= 0){
		dossierImages = System.getProperty("user.home") + "//workspace//data//";
		System.out.println("C'est windows");
		System.out.println(dossierImages);
		}
		
		else if(OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0 ){
		dossierImages = System.getProperty("user.home") + "/workspace/data/";
		System.out.println("C'est linux");
		}
		
		//Choix du format de sortie
		String format = new String(".png");
		
		String formatInitial = new String(".jpg");
		String imageACharger = new String("perspective-quadrilateral-src-img");
		String imageContours = new String("imageContours_1");
		String imageDetectionObjTable = new String("imageContours_2");
		String imageTraceCercle = new String("imageTraceCercle");
		
		
		
				
	//Charge l'image
		BufferedImage frame = null;
		try {
			frame = ImageIO.read(new File(dossierImages + imageACharger + formatInitial));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	//S'applique a l'image
		detector.setSourceImage(frame);
		detector.process();
		BufferedImage edges = detector.getEdgesImage();
			
	//Enregistre l'image des contours
		File outputfile1 = new File(dossierImages + imageACharger + "_" + imageContours + format);
		try {
			ImageIO.write(edges, "png", outputfile1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}			 		
		
	//Application de RANSAC
		ArrayList<Point> listeDePoints = new ArrayList<Point>();
		listeDePoints=SobelEdgeDetector.getListPoints();
		cercle.ransac(listeDePoints);
		//Cercle detecte
		detector.setCercleTrace(1);
		
	//Nouvelle application de SOBEL dans le cercle detecte plus haut afin de detecter la presence ou non d'objets
		detector.process();
		BufferedImage edges2 = detector.getEdgesImage();
		
	//Enregistre l'image des contours à l'intérieur du cercle
		File outputfile2 = new File(dossierImages + imageACharger + "_" + imageDetectionObjTable + format);
		try {
			ImageIO.write(edges2, "png", outputfile2);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	//Examen du nombre de points
		int nbPoints = detector.getNbPoints();
		if(nbPoints>nbMaximal){
			System.out.println("Il y a des objets sur la table");
		}
		else
			System.out.println("Il n'y a pas d'objets");
		
		
	//Definir le meilleur cercle
		Circle bestCircle = cercle.getBestCircle();
		int x = bestCircle.circleCenter().getX();
		int y = bestCircle.circleCenter().getY();
		int r = (int) bestCircle.radius();
			
	//Dessine le cercle obtenu
			
		Graphics2D g2d = frame.createGraphics();
		g2d.setColor(Color.red);
		g2d.fillOval(x-r,y-r,2*r,2*r);
		g2d.drawImage(frame, 0, 0, null);			
		g2d.dispose();
			
	//Enregistre l'image avec trac� du cercle
		File outputfile3 = new File(dossierImages + imageACharger + "_" + imageTraceCercle + format);
		try {
			ImageIO.write(frame, "png", outputfile3);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
		
}

