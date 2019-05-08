package surfaces;
import myUtils.Point;
import myUtils.Vector;
public class InfinitePlane extends Surfaces {

	double a, b, c, d=0; // a*x+b*y+ c*z +d = 0
	int material_index;

	/** make a plane from three points*/
	public InfinitePlane(double a, double b, double c, double d,int index) {
		this.a = a;
		this.b = b;
		this.c = c;
		this.d = d;
		this.material_index=index;
	}
	/** make a plane from two vectors */
	public InfinitePlane(Vector v1, Vector v2) {
		Vector normal=Vector.crossProduct(v1,v2);
		this.a=normal.x;
		this.b=normal.y;
		this.c=normal.z;
//		Point p1=normal.p1;
//		this.d=-(a*p1.x+b*p1.y+c*p1.z);
	}
	/** make an perpendicular plane*/
	public InfinitePlane perPlaneOfVector(Vector v){
		
		return new InfinitePlane(v.x,v.y,v.z,0,0);	
	}
	/** update the offset*/
	public InfinitePlane updateOffset(Point p){
		this.d=-(a*p.x+b*p.y+c*p.z);;
		return this;
	}
	
	public type getType() {
		return type.infinitePlane;
	}

	@Override
	public String toString() {
		return "IP.: " + a + "*x + " + b + "*y " + c + " = 0";

	}

}
