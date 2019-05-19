package myUtils;

public class Light {
	Point position;
	Color color; // RGB
	double specularIntensity, shadowIntensity, lightRadius;

	// The light received by a surface which is hidden
	// from the light is multiplied by (1-shadow intensity).
	public Light(Point pos, double[] col, double spec_intensity, double shadow_indensity, double light_rad) {
		this.position = pos;
		this.color = new Color(col);
		this.specularIntensity = spec_intensity;
		this.shadowIntensity = shadow_indensity;
		this.lightRadius = light_rad;

	}
}
