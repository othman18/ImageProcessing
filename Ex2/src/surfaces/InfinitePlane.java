package surfaces;
import myUtils.Point;
import myUtils.Vector;
public class InfinitePlane extends Surfaces {

	double a, b, c, d; // a*x+b*y+ c*z +d = 0

	/** make a plane from three points*/
	public InfinitePlane(double a, double b, double c, double d) {
		this.a = a;
		this.b = b;
		this.c = c;
		this.d = d;
	}
	/** make a plane from two vectors */
	public InfinitePlane(Vector v1, Vector v2) {
		Vector normal=Vector.crossProduct(v1,v2);
		this.a=normal.x;
		this.b=normal.y;
		this.c=normal.z;
		Point p1=normal.p1;
		this.d=-(a*p1.x+b*p1.y+c*p1.z);
		
		
	}
	

	public type getType() {
		return type.infinitePlane;
	}

	@Override
	public String toString() {
		return "IP.: " + a + "*x + " + b + "*y " + c + " = 0";

	}

}
