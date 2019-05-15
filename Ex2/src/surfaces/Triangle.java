package surfaces;

import myUtils.Point;
import myUtils.Vector;
import myUtils.Vector;
public class Triangle extends Surfaces {

	public Point p1, p2, p3;
	int mat_index;
	public Triangle(Point p1, Point p2, Point p3,int index) {
		this.p1 = p1;
		this.p2 = p2;
		this.p3 = p3;
		this.mat_index=index;
	}

	public type getType() {
		return type.triangle;
	}

	@Override
	public String toString() {
		return "Tr.: v1="+p1+", v2="+p2+", v3="+p3;
	}

	private double triangleArea(Point p1,Point p2,Point p3){
		Vector v1=new Vector(p1.x-p2.x,p1.y-p2.y,p1.z-p2.z);
		Vector v2=new Vector(p1.x-p3.x,p1.y-p3.y,p1.z-p3.z);
		Vector vNew=Vector.crossProduct(v1, v2);
		
		return 0.5*vNew.length;
		}
	@Override
	public Point getIntersection(Point p,Vector dir) {
		// TODO Auto-generated method stub
		Vector v1=new Vector(p1,p2);
		Vector v2=new Vector(p1,p3);
		InfinitePlane plane=new InfinitePlane(v1,v2);
		plane.updateOffset(p1);
		Point intersectPoint=plane.getIntersection(p,dir);
		if (intersectPoint==null )
				return null;
		double acc=0.0;
		acc+=triangleArea(p1,p2,intersectPoint);
		acc+=triangleArea(p1,p3,intersectPoint);
		acc+=triangleArea(p2,p3,intersectPoint);
		double originalArea=triangleArea(p1,p2,p3);
		
		if (originalArea!=acc)
			return null;
			
		return intersectPoint;
	}


}

