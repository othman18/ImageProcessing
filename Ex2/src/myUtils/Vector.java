package myUtils;

public class Vector {

	double x, y, z;

	public Vector(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
		System.out.println("RGB not added yet!");
	}

	/** chain dot product */
	public double dotProduct(Vector v) {
		return this.x * v.x + this.y * v.y + this.z * v.z;
	}

	/** return dot product of two vectors */
	public double dotProduct(Vector v1, Vector v2) {
		return v1.x * v2.x + v1.y * v2.y + v1.z * v2.z;
	}

	/** chain cross product */
	public Vector crossProduct(Vector v) {
		this.x = this.y * v.z - (this.z * v.y);
		this.y = this.x * v.z - (this.z * v.x);
		this.z = this.x * v.y - (this.y * v.x);
		return this;
	}

	/** return cross product of two vectors */
	public Vector crossProduct(Vector v1, Vector v2) {
		double x, y, z;
		x = v1.y * v2.z - (v1.z * v2.y);
		y = v1.x * v2.z - (v1.z * v2.x);
		z = v1.x * v2.y - (v1.y * v2.x);
		return new Vector(x, y, z);
	}

	/** chain vector addition */
	public Vector add(Vector v) {
		this.x += v.x;
		this.y += v.y;
		this.z += v.z;
		return this;
	}

	/** add two vectors */
	public Vector add(Vector v1, Vector v2) {
		double x, y, z;
		x = v1.x + v2.x;
		y = v1.y + v2.y;
		z = v1.z + v2.z;
		return new Vector(x, y, z);
	}

	/** scale vector with c */
	public Vector mult(double c) {
		this.x *= c;
		this.y *= c;
		this.z *= c;
		return this;
	}

	/** multiply two vectors RGB component wise */
	public Vector mult(Vector v) {
		System.out.println("not impllemented yet");
		return null;
	}

	public Vector generatePerpendicular(Vector v) {
		System.out.println("not impllemented yet");
		return null;
	}

	public Vector generatePerpendicular(Vector v1, Vector v2) {
		System.out.println("not impllemented yet");
		return null;
	}

	@Override
	public String toString() {
		return "V(" + x + ", " + y + ", " + z + ")";

	}
}
