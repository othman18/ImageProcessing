
public class Color {

	public double R, G, B;

	public Color(double[] RGB) {
		R = RGB[0];
		G = RGB[1];
		B = RGB[2];
	}

	public void scale(double c) {
		R *= c;
		G *= c;
		B *= c;
	}
	
	public void mult(Color c) {
		R *= c.R;
		G *= c.G;
		B *= c.B;
	}
	

	public void updateColor(Color col, double trans) {
		R += col.R * trans;
		G += col.G * trans;
		B += col.B * trans;
	}
	
	public void normalise(){
		R = Math.min(1,R);
		G = Math.min(1,G);
		B = Math.min(1,B);
	}
	
	public double[] getValues() {
		return new double[] {R,G,B};
	}

	public String toString() {
		return "C(" + R + ", " + G + ", " + B + ")";
	}

}