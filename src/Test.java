import java.awt.image.BufferedImage;
import java.util.ArrayList;



public class Test {

	public static void main(String[] args) {
			
		
	//Creer les objets
		Circle cercle = new Circle();
		SobelEdgeDetector detector = new SobelEdgeDetector();
		FilesManager manager = new FilesManager();
				
	//Choix des parametres
		//Distance au cercle pour être inliner
		cercle.setDistanceInliners(50);
				
		//Nombre d'inliners suffisant pour arrêter
		cercle.setNombreInliners((int)(100000));
		cercle.setNombreDIterations(10000);
		
		//Niveau de gradient
		detector.setGradientLevel(300);
		
		//Nombre de points permettant de dire qu'il y a un objet sur la table
		int nbMaximal = 2000;
		
		//Selection du dossier des images selon le systeme d'exploitation
		
		manager.SystemChoice();
		String dossierImages = manager.getDossierImages();
		
		//Choix du format de sortie
		String format = new String(".png");
		
		String formatInitial = new String(".jpg");
		String imageACharger = new String("table_tournesol_vue_de_dessus");
		String imageContours = new String("imageContours_1");
		String imageDetectionObjTable = new String("imageContours_2");
		String imageTraceCercle = new String("imageTraceCercle");
		
		
		ImageProcessor image = new ImageProcessor();
		
	//Charge l'image
		image.loadImage(dossierImages + imageACharger + formatInitial);
		BufferedImage frame = image.getInputfile();
	
	//S'applique a l'image
		detector.setSourceImage(frame);
		detector.process();
		BufferedImage edges = detector.getEdgesImage();
			
	//Enregistre l'image des contours
		image.saveImage(edges, dossierImages + imageACharger + "_" + imageContours + format);		 		
		
	//Application de RANSAC
		ArrayList<Point> listeDePoints = new ArrayList<Point>();
		listeDePoints=SobelEdgeDetector.getListPoints();
		long start = System.currentTimeMillis();
		cercle.ransac(listeDePoints);
		long duree = System.currentTimeMillis() - start;
		System.out.println("Execution time of RANSAC:" + duree/1000 + "s");
		
		//Cercle detecte
		/*	detector.setCercleTrace(1);
	
	//Nouvelle application de SOBEL dans le cercle detecte plus haut afin de detecter la presence ou non d'objets
		detector.process();
		BufferedImage edges2 = detector.getEdgesImage();
		
	//Enregistre l'image des contours à l'intérieur du cercle
		image.saveImage(edges2, dossierImages + imageACharger + "_" + imageDetectionObjTable + format);
		
	//Examen du nombre de points
		int nbPoints = detector.getNbPoints();
		if(nbPoints>nbMaximal){
			System.out.println("Il y a des objets sur la table");
		}
		else
			System.out.println("Il n'y a pas d'objets");*/
		
		
	//Definir le meilleur cercle
		Circle bestCircle = cercle.getBestCircle();
		int x = bestCircle.circleCenter().getX();
		int y = bestCircle.circleCenter().getY();
		int r = (int) bestCircle.radius();
			
	//Dessine le cercle obtenu
		image.drawCircle(frame, x, y, r);
			
	//Enregistre l'image avec trac� du cercle
		image.saveImage(frame, dossierImages + imageACharger + "_" + imageTraceCercle + format);
	}
		
}

