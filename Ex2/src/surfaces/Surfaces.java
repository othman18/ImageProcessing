package surfaces;


import myUtils.Point;
import myUtils.Vector;

public abstract class Surfaces {
	type myType;
	enum type {triangle,sphere,infinitePlane};
	
	public abstract type getType();
	public abstract double getIntersection(Point p,Vector dir);
	public abstract  String toString();

}
