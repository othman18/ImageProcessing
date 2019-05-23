
public class Light {
	public Point position;
	public Color color;
	public double specularIntensity, shadowIntensity, radius;

	public Light(Point pos, double[] col, double spec_intensity, double shadow_indensity, double light_rad) {
		this.position = pos;
		this.color = new Color(col);
		this.specularIntensity = spec_intensity;
		this.shadowIntensity = shadow_indensity;
		this.radius = light_rad;
	}
}