import myUtils.*;
import surfaces.*;

public class exec {
	public static void main(String[] args) {
		Vector v1 = new Vector(1, 2, 3);
		Point p1 = new Point(1, 2, 3);
		Surfaces s1 = new Sphere(p1, 0);
		Surfaces s2 = new Triangle(v1, v1, v1);
		Surfaces s3 = new InfinitePlane();
		System.out.println(s1.getType());
		System.out.println(s2.getType());
		System.out.println(s3.getType());
	}
}
