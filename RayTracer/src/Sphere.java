
public class Sphere extends Surfaces {

	double radius;
	Point center, intersectionPoint; // used for calculating the normal

	public Sphere(Point center, double radius, int index) {
		this.center = center;
		this.radius = radius;
		this.material_index = index - 1;
		myType = type.sphere;
	}

	/** return the intersection point if there is */
	public double getIntersection(Point p, Vector rayDirection) {
		// the variables L and tca were taken from the slides
		Vector L = new Vector(p, center);
		double tca = Vector.dotProduct(L, rayDirection);
		if (tca < 0)
			return -1;
		double d = Vector.dotProduct(L, L) - tca * tca;
		if (d > radius * radius)
			return -1;
		double t = tca - Math.sqrt(radius * radius - d);
		return t;
	}

	
	public void setIntersectionPoint(Point intersectionPoint) {
		this.intersectionPoint = intersectionPoint;
	}
	
	
	/** using the intersectionPoint with the sphere, create the sphere's normal */
	public Vector getNormal() {
		Vector normal = new Vector(center, intersectionPoint);
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
