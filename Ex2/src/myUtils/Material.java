package myUtils;

public class Material {
	Color diffuseColor, specularColor, reflectionColor; // RGB
	double transparency, PhongSpecularityCoefficient;
	
	public Material(double[] diffuse_color, double[] specular_color, double[] reflection_color, double phong_coe,
			double transparency_value) {
		diffuseColor=new Color(diffuse_color);
		specularColor=new Color(specular_color);
		reflectionColor=new Color(reflection_color);
		PhongSpecularityCoefficient=phong_coe;
		transparency=transparency_value;
	}
	
	
}
