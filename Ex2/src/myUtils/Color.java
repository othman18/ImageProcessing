package myUtils;

public class Color {

	int R;
	int G;
	int B;

	public Color(double[] RGB) {
		R = (int) RGB[0] * 255;
		G = (int) RGB[1] * 255;
		B = (int) RGB[2] * 255;

	}

}
