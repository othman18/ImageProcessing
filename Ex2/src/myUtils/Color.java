package myUtils;

public class Color {

	public double R, G, B;

	public Color(double[] RGB) {
		R =  RGB[0];
		G =  RGB[1];
		B = RGB[2];
	}
	public void mult(double c){
		R=R*c;
		G=G*c;
		B=B*c;
		
	}
	public void mult(Color c){
		R*=c.R;
		G*=c.G;
		B*=c.B;
	}

	public String toString() {
		return "C(" + R + ", " + G + ", " + B + ")";
	}

}
