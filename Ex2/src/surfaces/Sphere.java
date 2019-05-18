package surfaces;

import myUtils.Vector;

import myUtils.Point;

public class Sphere extends Surfaces {

	double radius;
	Point center;
	int material_index;

	public Sphere(Point center, double radius, int index) {
		this.center = center;
		this.radius = radius;
		this.material_index = index;
	}

	public type getType() {
		return type.sphere;
	}

	@Override
	public String toString() {
		return "Sp.: r=" + radius + ", c=" + center;
	}
	

	/** return the intersection point if there is */
	@Override
	public double getIntersection(Point p, Vector direction) {
		// TODO Auto-generated method stub
		Vector L = new Vector(p,center);
		double tca = Vector.dotProduct(L, direction);
		if (tca < 0)
			return -1;
		double d = Vector.dotProduct(L, L) - tca * tca;
		if (d > radius * radius)
			return -1;
		System.out.println("d="+d+", tca="+tca+", l*l="+Vector.dotProduct(L, L)+", rad2="+radius*radius);
		double t = tca - Math.sqrt(radius * radius - d);
		System.out.println("t="+t);
		return t;
	}

}
