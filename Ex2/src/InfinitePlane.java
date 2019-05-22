
public class InfinitePlane extends Surfaces {
	double a,b,c,d;
	

	public InfinitePlane(double a, double b, double c, double d, int index) {
		Vector normal=new Vector(a,b,c);
		normal.normalise();
		this.a = normal.x;
		this.b = normal.y;
		this.c = normal.z;
		this.d =d;
		this.material_index = index - 1;
		
		myType = type.infinitePlane;

	}

	@Override
	public type getType() {
		return type.infinitePlane;
	}

	@Override
	public double getIntersection(Point p, Vector dir) {
		Vector normal=getNormal();
		double t=-(Vector.pointMulVector(p,normal)-this.d)/(Vector.dotProduct(dir, normal));
		return t;
	}

	@Override
	public String toString() {
		return "IP(a="+a+", b="+b+", c="+c+", d="+d+")";
	}

	public Vector getNormal() {
		return new Vector(a,b,c);
	}

}
