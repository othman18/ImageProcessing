package myUtils;

public class Point {
	public double x, y, z;

	public Point(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;

	}

	public static double distance(Point p1, Point p2) {
		double dx = Math.pow(p1.x - p2.x, 2);
		double dy = Math.pow(p1.y - p2.y, 2);
		double dz = Math.pow(p1.z - p2.z, 2);
		return Math.sqrt(dx + dy + dz);
	}

	public static Point findPoint(Point p0, Vector v, double t) {
		Vector tv = v.mult(t);
		Point p = new Point(tv.x + p0.x, tv.y + p0.y, tv.z + p0.z);
		return p;
	}

	public void addByVector(Vector v, int num) {
		this.x += v.x * num;
		this.y += v.y * num;
		this.z += v.z * num;

	}

	@Override
	public String toString() {
		return "P(" + x + ", " + y + ", " + z + ")";
	}

}
