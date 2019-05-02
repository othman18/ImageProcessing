package surfaces;
import myUtils.Vector;

public class Sphere extends Surfaces {
	
	double radius;
	Vector center;
	
	public Sphere(Vector center, double radius){
		setType(type.sphere);
		this.center = center;
		this.radius = radius;
	}
}
