
public class Vector {
	public double x = 0.0, y = 0.0, z = 0.0;
	public double length;

	/** make a specific vector */
	public Vector(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;

		length = Math.sqrt(x * x + y * y + z * z);
	}

	/** make a vector from two points */
	public Vector(Point startPoint, Point endPoint) {
		this.x = endPoint.x - startPoint.x;
		this.y = endPoint.y - startPoint.y;
		this.z = endPoint.z - startPoint.z;
		length = Math.sqrt(x * x + y * y + z * z);
	}

	/** copy constructor */
	public Vector(Vector v) {
		this.x = v.x;
		this.y = v.y;
		this.z = v.z;
		this.length = v.length;
	}

	/** regular vector scaler */
	public static Vector scaleMult(Vector v, double t) {
		return new Vector(v.x * t, v.y * t, v.z * t);
	}

	/** dot product */
	public static double dotProduct(Vector v1, Vector v2) {
		return v1.x * v2.x + v1.y * v2.y + v1.z * v2.z;
	}

	/** cross product */
	public static Vector crossProduct(Vector v1, Vector v2) {
		Vector v3 = new Vector(0, 0, 0);
		v3.x = v1.y * v2.z - v1.z * v2.y;
		v3.y = v1.z * v2.x - v1.x * v2.z;
		v3.z = v1.x * v2.y - v1.y * v2.x;
		v3.normalise();
		return v3;
	}

	/** adding tow vectors */
	public static Vector add(Vector v1, Vector v2) {
		return new Vector(v1.x + v2.x, v1.y + v2.y, v1.z + v2.z);

	}

	/** normalise the vector */
	public void normalise() {
		length=Math.sqrt(x * x + y * y + z * z);
		if (length != 0) {
			this.x = this.x / this.length;
			this.y = this.y / this.length;
			this.z = this.z / this.length;
			this.length = 1;
		}
	}

	/** dot product between a point and a vector */
	public static double pointMulVector(Point p, Vector v) {
		return p.x * v.x + p.y * v.y + p.z * v.z;
	}

	@Override
	public String toString() {
		return "V(" + x + ", " + y + ", " + z + ")";
	}
}
