import myUtils.Vector;
import surfaces.*;

public class exec {
	public static void main(String[] args) {
		Vector v1 = new Vector(1,2,3);
		Surfaces s = new Triangle(v1,v1,v1);
		Surfaces s2 = new Sphere(v1, 2);
		System.out.println(s.getType());
		System.out.println(s2.getType());
		
	}
}
