package surfaces;
import myUtils.Vector;

public class Triangle extends Surfaces {

	Vector v1, v2, v3;

	public Triangle(Vector v1, Vector v2, Vector v3) {
		setType(type.triangle);
		this.v1 = v1;
		this.v2 = v2;
		this.v3 = v3;
	}

}
