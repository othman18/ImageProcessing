package surfaces;

import myUtils.Vector;

import myUtils.Point;

public class Sphere extends Surfaces {

	double radius;
	Point center;
	public Point SpherePoint; // used for calculating the normal

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
		Vector L = new Vector(p, center);
		double tca = Vector.dotProduct(L, direction);
		if (tca < 0)
			return -1;
		double d = Vector.dotProduct(L, L) - tca * tca;
		if (d > radius * radius)
			return -1;
		System.out
				.println("d=" + d + ", tca=" + tca + ", l*l=" + Vector.dotProduct(L, L) + ", rad2=" + radius * radius);
		double t = tca - Math.sqrt(radius * radius - d);
		System.out.println("t=" + t);
		return t;
	}

	public void setNormalPoint(Point p) {
		SpherePoint = p;
	}

	public Vector getNormal() {
		Vector normal = new Vector(center, SpherePoint);
		normal.normalise();
		return normal;
	}
}
