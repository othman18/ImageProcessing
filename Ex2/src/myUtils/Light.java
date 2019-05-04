package myUtils;

public class Light {
	Vector position;
	int color; // RGB
	double specularIntensity, shadowIntensity, lightRadius;
	// The light received by a surface which is hidden
	// from the light is multiplied by (1-shadow intensity).
}
