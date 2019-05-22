
public class Sphere extends Surfaces {

	double radius;
	Point center;
	public Point SpherePoint; // used for calculating the normal

	public Sphere(Point center, double radius, int index) {
		this.center = center;
		this.radius = radius;
		this.material_index = index - 1;
		myType = type.sphere;
	}

	/** return the intersection point if there is */
	public double getIntersection(Point p, Vector direction) {
		Vector L = new Vector(p, center);
		double tca = Vector.dotProduct(L, direction);
		if (tca < 0)
			return -1;
		double d = Vector.dotProduct(L, L) - tca * tca;
		if (d > radius * radius )
			return -1;
		double t = tca - Math.sqrt(radius * radius - d);
		return t;
	}

	
	public Vector getNormal(Point SpherePoint) {
		Vector normal = new Vector(center, SpherePoint);
		normal.normalise();
		return normal;
	}

	public type getType() {
		return type.sphere;
	}

	public String toString() {
		return "Sp.: r=" + radius + ", c=" + center;
	}

	
	}
