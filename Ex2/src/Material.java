
public class Material {
	public Color diffuseColor, specularColor, reflectionColor; // RGB
	public double transparency, PhongSpecularityCoefficient;

	public Material(double[] diffuse_color, double[] specular_color, double[] reflection_color, double phong_coe,
			double transparency_value) {
		diffuseColor = new Color(diffuse_color);
		specularColor = new Color(specular_color);
		reflectionColor = new Color(reflection_color);
		PhongSpecularityCoefficient = phong_coe;
		transparency = transparency_value;
	}
	public Material(Material mat) {
		diffuseColor=mat.diffuseColor;
		specularColor=mat.specularColor;
		reflectionColor=mat.reflectionColor;
		PhongSpecularityCoefficient=mat.PhongSpecularityCoefficient;
		transparency=mat.transparency;
	}
}