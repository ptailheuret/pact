import java.awt.Graphics2D;
import java.util.ArrayList;

public class Circle {

	private int x;
	private int y;
	private double radius;
	private Point circleCenter;

	private Point A;
	private Point B;
	private Point C;

	public Circle(Point A, Point B, Point C) {
		this.A = A;
		this.B = B;
		this.C = C;
	}

	public Circle() {
	}

	public void CircleCaracteristic() {
		circleCenter = this.circleCenter();
		x = circleCenter.getX();
		y = circleCenter.getY();
		radius = this.radius();
	}

	/**
	 * To find the circleCenter from 3 points
	 * @return
	 */
	
	public Point circleCenter() {

		float yDelta_a = B.getY() - A.getY();
		float xDelta_a = B.getX() - A.getX();
		float yDelta_b = C.getY() - B.getY();
		float xDelta_b = C.getX() - B.getX();
		Point center = new Point(0, 0, 0);

		float aSlope = yDelta_a / xDelta_a;
		float bSlope = yDelta_b / xDelta_b;

		Point AB_Mid = new Point((A.getX() + B.getX()) / 2, (A.getY() + B.getY()) / 2, 0);
		Point BC_Mid = new Point((B.getX() + C.getX()) / 2, (B.getY() + C.getY()) / 2, 0);

		if (yDelta_a == 0) // aSlope == 0
		{
			center.setX(AB_Mid.getX());
			if (xDelta_b == 0) // bSlope == infini
			{
				center.setY(BC_Mid.getY());
			} else {
				center.setY((int) (BC_Mid.getY() + (BC_Mid.getX() - center.getX()) / bSlope));
			}
		} else if (yDelta_b == 0) // bSlope == 0
		{
			center.setX(BC_Mid.getX());
			if (xDelta_a == 0) // aSlope == infini
			{
				center.setY(AB_Mid.getY());
			} else {
				center.setY((int) (AB_Mid.getY() + (AB_Mid.getX() - center.getX()) / aSlope));
			}
		} else if (xDelta_a == 0) // aSlope == infini
		{
			center.setY(AB_Mid.getY());
			center.setX((int) (bSlope * (BC_Mid.getY() - center.getY()) + BC_Mid.getX()));
		} else if (xDelta_b == 0) // bSlope == infini
		{
			center.setY(BC_Mid.getY());
			center.setX((int) (aSlope * (AB_Mid.getY() - center.getY()) + AB_Mid.getX()));
		} else {
			center.setX((int) ((aSlope * bSlope * (AB_Mid.getY() - BC_Mid.getY()) - aSlope
					* BC_Mid.getX() + bSlope * AB_Mid.getX()) / (bSlope - aSlope)));
			center.setY((int) (AB_Mid.getY() - (center.getX() - AB_Mid.getX()) / aSlope));
		}
		circleCenter = center;
		return center;
	}

	/**
	 * Give the radius of a circle
	 * @return
	 */
	public double radius() {
		circleCenter();
		double dx = circleCenter.getX() - A.getX();
		double dy = circleCenter.getY() - A.getY();
		radius = Math.hypot(dx, dy);
		return radius;
	}

	/**
	 * Calculate the distance between a point and a circle
	 * @param O
	 * @return
	 */
	public double distanceCircle(Point O) {

		double dx = x - O.getX();
		double dy = y - O.getY();
		return Math.abs(Math.hypot(dx, dy) - radius);
	}
	
	/**
	 * Draw a circle centered in (x,y) with a radius of r
	 * @param g
	 * @param x
	 * @param y
	 * @param r
	 */
	public void drawCenteredCircle(Graphics2D g, int x, int y, int r) {
		x = x - (r / 2);
		y = y - (r / 2);
		g.fillOval(x, y, r, r);
	}

	/**
	 * New method to compare points
	 * 
	 * On a simple example the execution time went from 220s to 12s
	 * In general the the execution time is divided by 20 
	 * 
	 * @param point
	 * @param seuil
	 */
	public void trier(Point point, int seuil) {

		int dx = x - point.getX();
		int dy = y - point.getY();
		int prod = dx * dx + dy * dy;
		double distanceCarre = prod - radius * radius;
		double diff = radius - seuil;

		// Le point est dans le cercle
		if (distanceCarre < 0) {
			if (diff >= 0) {
				if ((diff * diff) < prod)
					point.setInliner(1);
				else
					point.setInliner(0);
			}

			else {
				if (distanceCircle(point) < seuil)
					point.setInliner(1);
				else
					point.setInliner(0);
			}
		}

		// Le point est à l'extérieur du cercle
		else if (distanceCarre > 0) {
			if (prod < (seuil + radius) * (seuil + radius))
				point.setInliner(1);
			else
				point.setInliner(0);
		}

		else if (distanceCarre == 0)
			point.setInliner(1);
	}

	// Calculer le nombre d'Inliner
	public int numberOfInliers(ArrayList<Point> listePoints) {
		int numberOfInliers = 0;
		for (int i = 0; i < listePoints.size(); i++) {
			numberOfInliers = numberOfInliers + listePoints.get(i).getInliner();
		}
		return numberOfInliers;
	}
}
