package myUtils;

public class Point {
	double x, y, z;

	public Point(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;

	}

	@Override
	public String toString() {
		return "P(" + x + ", " + y + ", " + z + ")";
	}

}
