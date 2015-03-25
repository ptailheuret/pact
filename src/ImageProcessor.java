import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import javax.imageio.ImageIO;

public class ImageProcessor {

	private BufferedImage inputfile;
	private BufferedImage outputfile;

	// Threshold, above this distance from the circle points are outliers
	int thresholdInlier;

	// Number of points which allow RANSAC to finish before the end
	int p;

	// Number of loop
	int numberOfLoops;
	
	//Best circle found
	static Circle bestCircle = null;

	public static Circle getBestCircle() {
		return bestCircle;
	}

	public void setThresholdInlier(int thresholdInlier) {
		this.thresholdInlier = thresholdInlier;
	}

	public void setEnoughInliers(int p) {
		this.p = p;
	}

	public void setNumberOfLoops(int numberOfLoops) {
		this.numberOfLoops = numberOfLoops;
	}

	public BufferedImage getInputfile() {
		return inputfile;
	}

	public void setInputfile(BufferedImage inputfile) {
		this.inputfile = inputfile;
	}

	public BufferedImage getOutputfile() {
		return outputfile;
	}

	public void setOutputfile(BufferedImage outputfile) {
		this.outputfile = outputfile;
	}

	public ImageProcessor() {
		setInputfile(null);
		setOutputfile(null);
	}

	/**
	 * Load a picture
	 * @param cheminDAcces
	 */
	public void loadImage(String cheminDAcces) {
		BufferedImage inputfile = null;
		try {
			inputfile = ImageIO.read(new File(cheminDAcces));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		setInputfile(inputfile);
	}

	/**
	 * Save a picture
	 * @param outputfile
	 * @param cheminDAcces
	 */
	public void saveImage(BufferedImage outputfile, String cheminDAcces) {
		File savedfile = new File(cheminDAcces);
		try {
			ImageIO.write(outputfile, "png", savedfile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		setOutputfile(outputfile);
	}

	// Tracer un cercle sur une image frame
	public void drawCircle(BufferedImage frame, int x, int y, int r) {
		Graphics2D g2d = frame.createGraphics();
		g2d.setColor(Color.red);
		g2d.fillOval(x - r, y - r, 2 * r, 2 * r);
		g2d.drawImage(frame, 0, 0, null);
		g2d.dispose();
	}

	// Redimensionner image

	/**
	 * Implementation of RANSAC algorithm to detect circles
	 * from a list of points
	 * 
	 * @param listePoints
	 * @param value
	 * @return
	 */
	public Circle ransac(ArrayList<Point> listePoints, String value) {

		int i, j = 0;
		int n = listePoints.size();
		boolean stop = false;
		int indexGreatestModel = 0;
		double distance;

		System.out
				.println("Paramï¿½tres: Distance inliners, Nombre d'inliners suffisants, nombre d'itï¿½rations");
		System.out.println(thresholdInlier);
		System.out.println(p);
		System.out.println(numberOfLoops);
		System.out.println();

		ArrayList<Circle> listCircles = new ArrayList<Circle>();
		ArrayList<Integer> listNumberOfInliers = new ArrayList<Integer>();
		Circle circle = null;

		while (j < numberOfLoops && stop == false) {

			// Generer 3 entiers aleatoires
			Random r = new Random();
			int a = r.nextInt(n);
			int b = r.nextInt(n);
			int c = r.nextInt(n);

			// Selectionner les 3 points
			Point A = listePoints.get(a);
			Point B = listePoints.get(b);
			Point C = listePoints.get(c);

			// Draw the circle from 3 points, calculate its caracteristics
			// (radius, center)
			circle = new Circle(A, B, C);
			circle.CircleCaracteristic();

			// Classify points, inliers or outliers
			for (i = 0; i < n; i++) {

				if (value == "Optimisé")
					circle.trier(listePoints.get(i), thresholdInlier);

				else if (value == "Naif") {
					distance = circle.distanceCircle(listePoints.get(i));

					if (distance < thresholdInlier)
						listePoints.get(i).setInliner(1);
					else
						listePoints.get(i).setInliner(0);
				}
			}

			if (circle.numberOfInliers(listePoints) < p) {
				listCircles.add(circle);
				listNumberOfInliers.add(circle.numberOfInliers(listePoints));
			} else if (j > 200)
				stop = true;
			j++;
		}

		if (stop == true) {
			bestCircle = circle;
			System.out.println("Arret avant la fin, nombre d'itï¿½rations");
			System.out.println(j);
			System.out.println();
		} else {
			indexGreatestModel = listNumberOfInliers.indexOf(Collections.max(listNumberOfInliers));
			bestCircle = listCircles.get(indexGreatestModel);
			System.out.println("Arret");
			System.out.println();
		}

		System.out.println("Rayon obtenu");
		System.out.println(bestCircle.radius());
		System.out.println();
		System.out.println("Centre du cercle suivant le format (x,y)");
		System.out.println(bestCircle.circleCenter().getX());
		System.out.println(bestCircle.circleCenter().getY());

		return bestCircle;
	}

}
