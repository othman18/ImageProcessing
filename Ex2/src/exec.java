import myUtils.*;
import surfaces.*;

public class exec {
	public static void main(String[] args) {
		
		//Point p1=new Point(1,-2.5,2);
		//Point p2=new Point(2,-5,0);
		//Point p3=new Point(4,2,0);
		//Vector v1=new Vector(5,1,0);
		//Vector v2=new Vector(300000,1,-1);
		//InfinitePlane plane=new InfinitePlane(v1,v2);
		//Triangle T=new Triangle(p1,p2,p3,0);
	//	Sphere s=new Sphere(new Point(3,2,1),1,0);
		InfinitePlane plane=new InfinitePlane(-1,-5,-3,-12,0);
		System.out.println(plane.getIntersection(new Point(0,0,0), new Vector(-3.7,-3.45,2.98)));
		return ;
	}
}
