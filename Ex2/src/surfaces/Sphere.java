package surfaces;
import myUtils.Vector;

public class Sphere extends Surfaces {
	
	double radius;
	Vector center;
	
	public Sphere(Vector center, double radius){
		this.center = center;
		this.radius = radius;
	}
	public type getType() {
		return type.sphere;
	}
}
