
public class Point {
	public double x, y, z;

	public Point(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/** @return p: p = p0 + t * v */
	public static Point findPoint(Point p0, Vector v, double t) {
		Vector tv = Vector.scaleMult(v, t);
		Point p = new Point(tv.x + p0.x, tv.y + p0.y, tv.z + p0.z);
		return p;
	}

	/** copy constructor */
	public Point(Point p) {
		this.x = p.x;
		this.y = p.y;
		this.z = p.z;
	}

	public String toString() {
		return "P(" + x + ", " + y + ", " + z + ")";
	}
}
