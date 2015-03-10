import java.util.ArrayList;


public class Point {
private int x;
private int y;

//z=1 si Inliner, z=0 si Outliner
private int z;



public Point(int x, int y, int z) {
	this.x=x;
	this.y=y;
	this.z=z;
}


//Getters et Setters
public int getX() {
	return x;
}
public void setX(int x) {
	this.x = x;
}
public int getY() {
	return y;
}
public void setY(int y) {
	this.y = y;
}
public int getInliner() {
	return z;
}
public void setInliner(int z) {

	this.z = z;
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
