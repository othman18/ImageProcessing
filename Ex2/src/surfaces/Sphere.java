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
	public Point getIntersection(Point p, Vector direction) {
		// TODO Auto-generated method stub
		Vector L = new Vector(center.x - p.x, center.y - p.y, center.z - p.z);
		double tca = Vector.dotProduct(L, direction);
		if (tca < 0)
			return null;
		double d = Vector.dotProduct(L, L) - tca * tca;
		if (d > radius * radius)
			return null;
		double t = tca - Math.sqrt(radius * radius - d);
		return Point.findPoint(p, direction, t);
	}

}
