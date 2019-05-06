package surfaces;
import myUtils.Vector;
public class InfinitePlane extends Surfaces {

	double a, b, c, d; // a*x+b*y+ c*z +d = 0

	/**
	 *3 points make a plane
	 *2 vectors also make a plane
	 */
	public InfinitePlane(double a, double b, double c, double d) {
		this.a = a;
		this.b = b;
		this.c = c;
		this.d = d;
	}
	public InfinitePlane(Vector v1, Vector v2) {
		
	}

	public type getType() {
		return type.infinitePlane;
	}

	@Override
	public String toString() {
		return "IP.: " + a + "*x + " + b + "*y " + c + " = 0";

	}

}
