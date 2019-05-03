package surfaces;

import myUtils.Vector;

public class Triangle extends Surfaces {

	public Vector v1, v2, v3;

	public Triangle(Vector v1, Vector v2, Vector v3) {
		this.v1 = v1;
		this.v2 = v2;
		this.v3 = v3;
	}

	public type getType() {
		return type.triangle;
	}

	@Override
	public String toString() {
		return "Tr.: v1="+v1+", v2="+v2+", v3="+v3;
	}


}

