
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;


public class Circle {

	private double radius;
	private double radius1;
	
	private Point circleCenter;
	private int x;
	private int y;
	
	private Point A;
	private Point B;
	private Point C;
	public static Circle bestCircle;
	
	//Distance au cercle pour être inliner
	int d;
	
	//Nombre d'inliners suffisant pour arrêter
	int p;
	int nombreDIterations;

	
	public void setDistanceInliners(int distance){
		d=distance;
	}
	public void setNombreInliners(int nombre){
		p=nombre;
	}
	public void setNombreDIterations(int iterations){
		nombreDIterations=iterations;
	}
	
	public Circle getBestCircle(){
		return bestCircle;
	}
	
	public Circle(Point A, Point B, Point C){
		this.A=A;
		this.B=B;
		this.C=C;
	}
	
	public Circle(){
	}
	
	public void CircleCaracteristic(Circle cercle){
		circleCenter = cercle.circleCenter();
		x = circleCenter.getX();
		y = circleCenter.getY();
		radius = cercle.radius();
		radius1 = cercle.radiusToCompare();
	}
	
	//Trouver le centre du cercle
	public Point circleCenter(){
	
	    float yDelta_a = B.getY() - A.getY();
	    float xDelta_a = B.getX() - A.getX();
	    float yDelta_b = C.getY() - B.getY();
	    float xDelta_b = C.getX() - B.getX();
	    Point center = new Point(0,0,0);
	
	    float aSlope = yDelta_a/xDelta_a;
	    float bSlope = yDelta_b/xDelta_b;  
	    
	    Point AB_Mid = new Point((A.getX()+B.getX())/2, (A.getY()+B.getY())/2,0);
	    Point BC_Mid = new Point((B.getX()+C.getX())/2, (B.getY()+C.getY())/2,0);
	    
	    if(yDelta_a == 0)         //aSlope == 0
	    {
	        center.setX(AB_Mid.getX());
	        if (xDelta_b == 0)         //bSlope == infini
	        {
	            center.setY(BC_Mid.getY());
	        }
	        else
	        {
	            center.setY((int)(BC_Mid.getY() + (BC_Mid.getX()-center.getX())/bSlope));
	        }
	    }
	    else if (yDelta_b == 0)               //bSlope == 0
	    {
	        center.setX(BC_Mid.getX());
	        if (xDelta_a == 0)             //aSlope == infini
	        {
	            center.setY(AB_Mid.getY());
	        }
	        else
	        {
	            center.setY((int)(AB_Mid.getY() + (AB_Mid.getX()-center.getX())/aSlope));
	        }
	    }
	    else if (xDelta_a == 0)        //aSlope == infini
	    {
	        center.setY(AB_Mid.getY());
	        center.setX((int)(bSlope*(BC_Mid.getY()-center.getY()) + BC_Mid.getX()));
	    }
	    else if (xDelta_b == 0)        //bSlope == infini
	    {
	        center.setY(BC_Mid.getY());
	        center.setX((int)(aSlope*(AB_Mid.getY()-center.getY()) + AB_Mid.getX()));
	    }
	    else
	    {
	        center.setX((int)((aSlope*bSlope*(AB_Mid.getY()-BC_Mid.getY()) - aSlope*BC_Mid.getX() + bSlope*AB_Mid.getX())/(bSlope-aSlope)));
	        center.setY((int)(AB_Mid.getY() - (center.getX() - AB_Mid.getX())/aSlope));
	    }
	    circleCenter = center;
	    return center;
	  }
	
	//Trouver le rayon du cercle
	public double radius(){
		circleCenter();
		double dx=circleCenter.getX()-A.getX();
		double dy=circleCenter.getY()-A.getY();
		radius=Math.hypot(dx,dy);
		return radius;
	}
	
	public double radiusToCompare(){
		circleCenter();
		double dx=circleCenter.getX()-A.getX();
		double dy=circleCenter.getY()-A.getY();
		radius1=dx*dx+dy*dy;
		return radius1;
	}
	
	//Calculer la distance d'un point a un cercle
	public double distanceCircle(Point O){
		
		double dx=x-O.getX();
		double dy=y-O.getY();
		return Math.abs(Math.hypot(dx,dy)-radius);
	}
	
	public double distanceToCompareCircle(Point O){
		
		double dx=x-O.getX();
		double dy=y-O.getY();
		return Math.abs(dx*dx+dy*dy-radius1);
	}
	
	public void drawCenteredCircle(Graphics2D g, int x, int y, int r) {
		  x = x-(r/2);
		  y = y-(r/2);
		  g.fillOval(x,y,r,r);
		}
	
/** PISTE DE REFLEXION
 * Impl�mentation des reflexions effectu�es
 * @param O
 * @return
 */
	
	
	public Circle ransac(ArrayList<Point> listePoints, String value){
		
	int i,j=0;
	int n=listePoints.size();
	int arret=0;
	int indexPlusGrandNombreInliners=0;
	double distance;
	
	System.out.println("Param�tres: Distance inliners, Nombre d'inliners suffisants, nombre d'it�rations");
	System.out.println(d);
	System.out.println(p);
	System.out.println(nombreDIterations);
	System.out.println();
	
	ArrayList<Circle> listeCercles = new ArrayList<Circle>();
	ArrayList<Integer> listeNombreInliners = new ArrayList<Integer>();
	Circle cercle = null;
	Circle meilleurCercle = null;
	
	
	while(j<nombreDIterations && arret==0){
		
		//Generer 3 entiers aleatoires
		Random r=new Random();
		int a=r.nextInt(n);
		int b=r.nextInt(n);
		int c=r.nextInt(n);
	
		//Selectionner les 3 points
		Point A=listePoints.get(a);
		Point B=listePoints.get(b);
		Point C=listePoints.get(c);
	
		//Tracer un cercle a� partir de 3 points
		cercle=new Circle(A,B,C);
		CircleCaracteristic(cercle);
		
		//Classer les points en Inliners et Outliners
		for(i=0;i<n;i++)
		{
			
			if(value == "Optimis�")
				trier(listePoints.get(i),d);
			
			else if(value == "Naif"){
			distance=distanceCircle(listePoints.get(i));
			
			if(distance<(d))
				listePoints.get(i).setInliner(1);
			else
				listePoints.get(i).setInliner(0);
			}			
		}
		
		//Ajouter le cercle trouve a la liste de cercles
		//En parall�le une liste contenant le nombre d'inliners de chaque mod�le se remplit
		if(nombreInliners(listePoints)<p){
				listeCercles.add(cercle);
				listeNombreInliners.add(nombreInliners(listePoints));
		}
		else if(j>200)
			arret=1;
		j++;
	}
	
	
	
		if(arret==1){
			meilleurCercle=cercle;
			System.out.println("Arret avant la fin, nombre d'it�rations");
			System.out.println(j);
			System.out.println();
		}
		else{
			indexPlusGrandNombreInliners=listeNombreInliners.indexOf(Collections.max(listeNombreInliners));
			meilleurCercle=listeCercles.get(indexPlusGrandNombreInliners);
			System.out.println("Arret");
			System.out.println();
		}
		
		bestCircle=meilleurCercle;
		System.out.println("Rayon obtenu");
		System.out.println(meilleurCercle.radius());
		System.out.println();
		System.out.println("Centre du cercle suivant le format (x,y)");
		System.out.println(meilleurCercle.circleCenter().getX());
		System.out.println(meilleurCercle.circleCenter().getY());
		
		return meilleurCercle;
	}
	/**
	 * Nouvelle méthode pour trier de manière efficace
	 * les points
	 * Le principe est d'utiliser le moins possible le
	 * calcul via racines carrées
	 * 
	 * Sur un exemple on passe de 220s à 12s d'exécution
	 * 
	 * 
	 * @param point
	 * @param seuil
	 */
	public void trier(Point point, int seuil){
		
		int dx = x - point.getX();
		int dy = y - point.getY();
		int prod = dx*dx+dy*dy;
		double distanceCarre = prod - radius*radius;
		double diff = radius - seuil;
		
		//Le point est dans le cercle
		if(distanceCarre<0){
			if(diff>=0){
				if((diff*diff)<prod)
					point.setInliner(1);
				else
					point.setInliner(0);
			}
			
			else{
				if(distanceCircle(point)<seuil)
					point.setInliner(1);
				else
					point.setInliner(0);
			}
		}
		
		//Le point est à l'extérieur du cercle
		else if(distanceCarre>0){
			if(prod<(seuil+radius)*(seuil+radius))
				point.setInliner(1);
			else
				point.setInliner(0);
		}
		
		else if(distanceCarre==0)
			point.setInliner(1);
	}
	
	//Calculer le nombre d'Inliner
	public int nombreInliners(ArrayList<Point> listePoints) {
		int nombreInliners=0;
		for(int i=0;i<listePoints.size();i++){
			nombreInliners=nombreInliners+listePoints.get(i).getInliner();
		}	
		return nombreInliners;
	}
}
