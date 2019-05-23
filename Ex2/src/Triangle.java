
public class Triangle extends Surfaces {

	public Point p1, p2, p3;

	public Triangle(Point p1, Point p2, Point p3, int index) {
		this.p1 = p1;
		this.p2 = p2;
		this.p3 = p3;
		this.material_index = index - 1;
		myType = type.triangle;
	}

	public type getType() {
		return type.triangle;
	}

	@Override
	public String toString() {
		return "Tr.: v1=" + p1 + ", v2=" + p2 + ", v3=" + p3;
	}

	public double getIntersection(Point p, Vector dir) {
		Vector normal = getNormal();
		double d = Vector.pointMulVector(p1, normal);
		double t = -((Vector.pointMulVector(p, normal)) - d) / Vector.dotProduct(dir, normal);
		/* checking if the point in the triangle */
		Boolean bool = checkIfInTriangle(p, dir, t);
		if (bool == false)
			return -1;
		return t;
	}
	
	private Boolean checkIfInTriangle(Point p0, Vector dir, double t) {
		return checkSide(p0, dir, t, p1, p2) && checkSide(p0, dir, t, p2, p3) && checkSide(p0, dir, t, p3, p1)
				|| checkSide(p0, dir, t, p2, p1) && checkSide(p0, dir, t, p3, p2) && checkSide(p0, dir, t, p1, p3);
	}

	private boolean checkSide(Point p0, Vector dir, double t, Point t1, Point t2) {
		Point intersection = Point.findPoint(p0, dir, t);
		Vector v1 = new Vector(p0, t1);
		Vector v2 = new Vector(p0, t2);
		Vector N1 = Vector.crossProduct(v2, v1);
		double d1 = -(Vector.pointMulVector(p0, N1));
		if (Vector.pointMulVector(intersection, N1) + d1 < 0)
			return false;
		return true;
	}

	public Vector getNormal() {
		Vector v1 = new Vector(p1, p2);
		Vector v2 = new Vector(p1, p3);
		Vector normal = Vector.crossProduct(v1, v2);
		normal.normalise();
		return normal;
	}

}