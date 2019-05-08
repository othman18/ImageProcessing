package surfaces;

import myUtils.Point;

public class Sphere extends Surfaces {

	double radius;
	Point center;
	int material_index;
	public Sphere(Point center, double radius,int index) {
		this.center = center;
		this.radius = radius;
		this.material_index=index;
	}

	public type getType() {
		return type.sphere;
	}
	
	@Override
	public String toString() {
		return "Sp.: r="+radius+", c="+center;
	}

}
