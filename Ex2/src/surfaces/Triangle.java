package surfaces;

import myUtils.Point;
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


}

