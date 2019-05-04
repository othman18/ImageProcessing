package surfaces;

public class InfinitePlane extends Surfaces {

	double a, b, c; // ax+by + c = 0

	/**
	 * input is a vector where the first two elements are the normal and the third
	 * element is the factor
	 */
	public InfinitePlane(double a, double b, double c) {
		this.a = a;
		this.b = b;
		this.c = c;

		System.out.println("should define the normal and offset");
	}

	public type getType() {
		return type.infinitePlane;
	}

	@Override
	public String toString() {
		return "IP.: " + a + "*x + " + b + "*y " + c + " = 0";

	}

}
