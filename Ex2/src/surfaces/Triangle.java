package surfaces;

import myUtils.Point;
import myUtils.Vector;
import myUtils.Vector;

public class Triangle extends Surfaces {

	public Point p1, p2, p3;
	int mat_index;

	public Triangle(Point p1, Point p2, Point p3, int index) {
		this.p1 = p1;
		this.p2 = p2;
		this.p3 = p3;
		this.mat_index = index;
	}

	public type getType() {
		return type.triangle;
	}

	@Override
	public String toString() {
		return "Tr.: v1=" + p1 + ", v2=" + p2 + ", v3=" + p3;
	}

	private double triangleArea(Point p1, Point p2, Point p3) {
		Vector v1 = new Vector(p1.x - p2.x, p1.y - p2.y, p1.z - p2.z);
		Vector v2 = new Vector(p1.x - p3.x, p1.y - p3.y, p1.z - p3.z);
		Vector vNew = Vector.crossProduct(v1, v2);

		return 0.5 * vNew.length;
	}

	/** check if a point in a triangle */
	private boolean checkIfInTriangle(Point p0, Point p) {
		Boolean side1 = checkSide(this.p2, this.p1, p0, p);
		Boolean side2 = checkSide(this.p3, this.p2, p0, p);
		Boolean side3 = checkSide(this.p1, this.p3, p0, p);
		System.out.println(side1 + "," + side2 + "," + side3);

		return side1 & side2 & side3;
	}

	/** checks if the point above the plane(the 3 points) */
	private Boolean checkSide(Point t1, Point t2, Point p0, Point p) {
		// TODO Auto-generated method stub
		Vector v1 = new Vector(p0, t1);
		Vector v2 = new Vector(p0, t2);
		Vector N = Vector.crossProduct(v2, v1);
		N.normalized();
		double d1 = -(Vector.dotProduct(N, p0));
		if ((Vector.dotProduct(N, p) + d1) < 0)
			return false;
		return true;
	}

	@Override
	/** finding the intersection point using the slides we showed in the class */
	public double getIntersection(Point p, Vector dir) {
		// TODO Auto-generated method stub
		Vector v1 = new Vector(p1, p2);
		Vector v2 = new Vector(p1, p3);
		System.out.println("v1:" + v1 + ", v2:" + v2);
		// making a plane which speared out from two vectors
		InfinitePlane plane = new InfinitePlane(v1, v2);
		plane.updateOffset(p1);
		System.out.println(plane);
		// finding the intersection point between the ray and the plane
		double t = plane.getIntersection(p, dir);
		System.out.println("1");
		if (t == -1)
			return -1;
		/*
		 * double acc=0.0; acc+=triangleArea(p1,p2,intersectPoint);
		 * acc+=triangleArea(p1,p3,intersectPoint);
		 * acc+=triangleArea(p2,p3,intersectPoint); double
		 * originalArea=triangleArea(p1,p2,p3); System.out.println(originalArea +"-----"
		 * + acc); if (originalArea!=acc) return null;
		 * System.out.println("trueeeeeeeeeeeeeeeeeeeee");
		 */
		Point intersectionPoint = Point.findPoint(p, dir, t);
		// checks if the intersect point is inside the triangle
		if (checkIfInTriangle(p, intersectionPoint) == false)
			return -1;
		return t;
	}

}
