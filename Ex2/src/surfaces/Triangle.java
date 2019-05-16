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
	private boolean checkIfInTriangle(Point p0,Point p){
		Boolean side1=checkSide(this.p2,this.p1,p0,p);
		Boolean side2=checkSide(this.p3,this.p2,p0,p);
		Boolean side3=checkSide(this.p1,this.p3,p0,p);
		System.out.println(side1 +","+side2+","+side3);
		
		return side1 & side2 & side3;
	}
	private Boolean checkSide(Point t1, Point t2, Point p0, Point p) {
		// TODO Auto-generated method stub
		Vector v1=new Vector(p0,t1);
		Vector v2=new Vector(p0,t2);
		Vector N=Vector.crossProduct(v2, v1);
		N=Vector.normalized(N);
		double d1=-(Vector.dotProduct(N,p0));
		if((Vector.dotProduct(N,p)+d1)<0)
			return false;
		return true;
	}

	@Override
	public Point getIntersection(Point p,Vector dir) {
		// TODO Auto-generated method stub
		Vector v1=new Vector(p1,p2);
		Vector v2=new Vector(p1,p3);
		System.out.println("v1:"+v1+", v2:"+v2);
		InfinitePlane plane=new InfinitePlane(v1,v2);
		plane.updateOffset(p1);
		System.out.println(plane);
		Point intersectPoint=plane.getIntersection(p,dir);
		System.out.println("1");
		if (intersectPoint==null )
				return null;
		/*double acc=0.0;
		acc+=triangleArea(p1,p2,intersectPoint);
		acc+=triangleArea(p1,p3,intersectPoint);
		acc+=triangleArea(p2,p3,intersectPoint);
		double originalArea=triangleArea(p1,p2,p3);
		System.out.println(originalArea +"-----" + acc);
		if (originalArea!=acc)
			return null;
		System.out.println("trueeeeeeeeeeeeeeeeeeeee");*/
		if(checkIfInTriangle(p,intersectPoint)==false)
			return null;
		return intersectPoint;
	}


}

