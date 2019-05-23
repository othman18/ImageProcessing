
public class Color {
	public double R, G, B;

	public Color(double[] RGB) {
		R = RGB[0];
		G = RGB[1];
		B = RGB[2];
	}

	/** scale the RGB with a const c */
	public void scale(double c) {
		R *= c;
		G *= c;
		B *= c;
	}

	/** scale the RGB with the RGB from another color */
	public void mult(Color color) {
		R *= color.R;
		G *= color.G;
		B *= color.B;
	}

	/**
	 * multiply the transparency constant with the RGB of color then add them to the
	 * RGB
	 */
	public void updateColor(Color color, double transparency) {
		R += color.R * transparency;
		G += color.G * transparency;
		B += color.B * transparency;
	}

	public void normalise() {
		R = Math.min(1, R);
		G = Math.min(1, G);
		B = Math.min(1, B);
	}

	public double[] getValues() {
		return new double[] { R, G, B };
	}

	public String toString() {
		return "C(" + R + ", " + G + ", " + B + ")";
	}
}