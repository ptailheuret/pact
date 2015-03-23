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

}
