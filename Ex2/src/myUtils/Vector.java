package myUtils;
import java.lang.Math;;
public class Vector {

	public double x=0.0, y=0.0, z=0.0;
	public double length;
	/** make a specific vector*/
	public Vector(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;

		length=Math.sqrt(x*x+y*y+z*z);
	}
	/** make a vector from two points */
	public Vector(Point point2,Point point1){
		this.x=point1.x-point2.x;
		this.y=point1.y-point2.y;
		this.z=point1.z-point2.z;
	}

	/** chain dot product */
	public double dotProduct(Vector v) {
	//	isMadeFromPoints=0;
		return this.x * v.x + this.y * v.y + this.z * v.z;
		
	}

	/** return dot product of two vectors */
	public static double dotProduct(Vector v1, Vector v2) {
		return v1.x * v2.x + v1.y * v2.y + v1.z * v2.z;
	}
	
	
	
	/** bedha tzbeeeeeee6*/
	public static double dotProduct(Vector v1, Point v2) {
		return v1.x * v2.x + v1.y * v2.y + v1.z * v2.z;
	}

	/** chain cross product */
	public Vector crossProduct(Vector v) {
		this.x = this.y * v.z - (this.z * v.y);
		this.y = this.z * v.x - (this.x * v.z);
		this.z = this.x * v.y - (this.y * v.x);
		return this;
	}

	/** return cross product of two vectors */
	public static Vector crossProduct(Vector v1, Vector v2) {
		double x, y, z;
		x = v1.y * v2.z - (v1.z * v2.y);
		y = v1.z * v2.x - (v1.x * v2.z);
		z = v1.x * v2.y - (v1.y * v2.x);
		return new Vector(x, y, z);
	}

	/** chain vector addition */
	public Vector add(Vector v) {
	//	isMadeFromPoints=0;
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
		double x = this.x*c;
		double y = this.y*c;
		double z = this.z*c;
		return new Vector(x,y,z);
	}
	/** scale vector with c */
	public Vector mult(Point c) {
		double x = this.x*c.x;
		double y = this.y*c.y;
		double z = this.z*c.z;
		return new Vector(x,y,z);
	}

	/** multiply two vectors RGB component wise */
	public Vector mult(Vector v) {
		return new Vector(x*v.x,y*v.y,z*v.z);
	}

	public Vector generateVectorPerpendicular(Vector v) {
		
		System.out.println("not impllemented yet");
		return null;
	}

	public Vector generatePerpendicular(Vector v1, Vector v2) {
		System.out.println("not impllemented yet");
		return null;
	}
	public static Vector normalized(Vector v){
		double d=1.0/v.length;
		return new Vector(v.x*d,v.y*d,v.z*d);
		
	}

	@Override
	public String toString() {
		return "V(" + x + ", " + y + ", " + z + ")";
	}
}
