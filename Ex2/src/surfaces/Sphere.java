package surfaces;

import myUtils.Point;

public class Sphere extends Surfaces {

	double radius;
	Point center;

	public Sphere(Point center, double radius) {
		this.center = center;
		this.radius = radius;
	}

	public type getType() {
		return type.sphere;
	}
	
	@Override
	public String toString() {
		return "Sp.: r="+radius+", c="+center;
	}

}
