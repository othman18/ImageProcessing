import java.awt.Transparency;
import java.awt.color.*;
import java.awt.image.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;

public class RayTracer {

	public int imageWidth, imageHeight;
	public int superSampleLevel, maxRecursionNum, root_shadow_rays;
	public double[] background_RGB = new double[3];
	Camera cam;
	Random random = new Random();
	List<Material> mat_list;
	List<Surfaces> surfaces_list;
	List<Light> lgt_list;
	double epsilon = 0.000001;

	/**
	 * Runs the ray tracer. Takes scene file, output image file and image size as
	 * input.
	 */
	public static void main(String[] args) {

		try {
			RayTracer tracer = new RayTracer();
			// Default values:
			tracer.imageWidth = 500;
			tracer.imageHeight = 500;

			if (args.length < 2)
				throw new RayTracerException(
						"Not enough arguments provided. Please specify an input scene file and an output image file for rendering.");

			String sceneFileName = args[0];
			String outputFileName = args[1];

			if (args.length > 3) {
				tracer.imageWidth = Integer.parseInt(args[2]);
				tracer.imageHeight = Integer.parseInt(args[3]);
			}

			// Parse scene file:
			tracer.parseScene(sceneFileName);

			// Render scene:
			tracer.renderScene(outputFileName);

		} catch (RayTracerException e) {
			System.out.println("exception 1");
			System.out.println(e.getMessage());
		} catch (Exception e) {
			System.out.println("exception 2");
			System.out.println(e.getMessage());
		}

	}

	/**
	 * Parses the scene file and creates the scene. Change this function so it
	 * generates the required objects.
	 */
	public void parseScene(String sceneFileName) throws IOException, RayTracerException {
		FileReader fr = new FileReader(sceneFileName);

		BufferedReader r = new BufferedReader(fr);
		String line = null;
		int lineNum = 0;
		// System.out.println("Started parsing scene file " + sceneFileName);

		// init the lists
		mat_list = new ArrayList<>();
		surfaces_list = new ArrayList<>();
		lgt_list = new ArrayList<>();

		while ((line = r.readLine()) != null) {
			line = line.trim();
			++lineNum;

			if (line.isEmpty() || (line.charAt(0) == '#')) {
				// This line in the scene file is a comment
				continue;
			} else {
				String code = line.substring(0, 3).toLowerCase();
				// Split according to white space characters:
				String[] params = line.substring(3).trim().toLowerCase().split("\\s+");

				System.out.println(Arrays.deepToString(params));

				if (code.equals("cam")) {
					// Add code here to parse camera parameters
					if (params.length != 11) {
						System.out.println("cam input file error");
						r.close();
						return;
					}
					Point position = new Point(Double.parseDouble(params[0]), Double.parseDouble(params[1]),
							Double.parseDouble(params[2]));
					Point lookAtPoint = new Point(Double.parseDouble(params[3]), Double.parseDouble(params[4]),
							Double.parseDouble(params[5]));
					Vector upVector = new Vector(Double.parseDouble(params[6]), Double.parseDouble(params[7]),
							Double.parseDouble(params[8]));
					double screenDistance = Double.parseDouble(params[9]);
					double screenWidth = Double.parseDouble(params[10]);
					cam = new Camera(position, lookAtPoint, upVector, screenDistance, screenWidth, imageWidth,
							imageHeight);
					System.out.println(String.format("Parsed camera parameters (line %d)", lineNum));

				} else if (code.equals("set")) {
					// Add code here to parse general settings parameters
					if (params.length != 6) {
						System.out.println("general settings input file error");
						r.close();
						return;
					}
					background_RGB[0] = Double.parseDouble(params[0]);
					background_RGB[1] = Double.parseDouble(params[1]);
					background_RGB[2] = Double.parseDouble(params[2]);
					root_shadow_rays = Integer.parseInt(params[3]);
					maxRecursionNum = Integer.parseInt(params[4]);
					superSampleLevel = Integer.parseInt(params[5]);
					System.out.println(String.format("Parsed general settings (line %d)", lineNum));

				} else if (code.equals("mtl")) {
					// Add code here to parse material parameters
					if (params.length != 11) {
						System.out.println("material input file error");
						r.close();
						return;
					}
					double[] diffuse_color = { Double.parseDouble(params[0]), Double.parseDouble(params[1]),
							Double.parseDouble(params[2]) };
					double[] specular_color = { Double.parseDouble(params[3]), Double.parseDouble(params[4]),
							Double.parseDouble(params[5]) };
					double[] reflection_color = { Double.parseDouble(params[6]), Double.parseDouble(params[7]),
							Double.parseDouble(params[8]) };
					double phong_coe = Double.parseDouble(params[9]);
					double transparency_value = Double.parseDouble(params[10]);
					mat_list.add(new Material(diffuse_color, specular_color, reflection_color, phong_coe,
							transparency_value));
					System.out.println(String.format("Parsed material (line %d)", lineNum));

				} else if (code.equals("sph")) {
					if (params.length != 5) {
						System.out.println("sphere input file error");
						r.close();
						return;
					}
					// Add code here to parse sphere parameters

					Point center = new Point(Double.parseDouble(params[0]), Double.parseDouble(params[1]),
							Double.parseDouble(params[2]));
					double rad = Double.parseDouble(params[3]);
					int mat_index = Integer.parseInt(params[4]);
					surfaces_list.add(new Sphere(center, rad, mat_index));

					System.out.println(String.format("Parsed sphere (line %d)", lineNum));
				} else if (code.equals("pln")) {
					// Add code here to parse plane parameters
					if (params.length != 5) {
						System.out.println("plane input file error");
						r.close();
						return;
					}
					double a, b, c, offset;
					a = Double.parseDouble(params[0]);
					b = Double.parseDouble(params[1]);
					c = Double.parseDouble(params[2]);
					offset = Double.parseDouble(params[3]);
					int mat_index = Integer.parseInt(params[4]);
					surfaces_list.add(new InfinitePlane(a, b, c, offset, mat_index));

					System.out.println(String.format("Parsed plane (line %d)", lineNum));

				} else if (code.equals("trg")) {
					// Add code here to parse light parameters
					if (params.length != 10) {
						System.out.println("triangle input file error");
						r.close();
						return;
					}

					Point p1 = new Point(Double.parseDouble(params[0]), Double.parseDouble(params[1]),
							Double.parseDouble(params[2]));
					Point p2 = new Point(Double.parseDouble(params[3]), Double.parseDouble(params[4]),
							Double.parseDouble(params[5]));
					Point p3 = new Point(Double.parseDouble(params[6]), Double.parseDouble(params[7]),
							Double.parseDouble(params[8]));
					int mat_index = Integer.parseInt(params[9]);
					surfaces_list.add(new Triangle(p1, p2, p3, mat_index));

					System.out.println(String.format("Parsed triangle (line %d)", lineNum));

				} else if (code.equals("lgt")) {
					// Add code here to parse light parameters
					if (params.length != 9) {
						System.out.println("light input file error");
						r.close();
						return;
					}
					double spec_intensity, shadow_indensity, light_rad;
					Point pos = new Point(Double.parseDouble(params[0]), Double.parseDouble(params[1]),
							Double.parseDouble(params[2]));
					double[] light_color = { Double.parseDouble(params[3]), Double.parseDouble(params[4]),
							Double.parseDouble(params[5]) };
					spec_intensity = Double.parseDouble(params[6]);
					shadow_indensity = Double.parseDouble(params[7]);
					light_rad = Double.parseDouble(params[8]);
					lgt_list.add(new Light(pos, light_color, spec_intensity, shadow_indensity, light_rad));

					System.out.println(String.format("Parsed light (line %d)", lineNum));
				} else {
					System.out.println(String.format("ERROR: Did not recognize object: %s (line %d)", code, lineNum));
				}
			}
		}
		r.close();
		System.out.println("Finished parsing scene file " + sceneFileName);

	}

	/**
	 * Renders the loaded scene and saves it to the specified file location.
	 */
	public void renderScene(String outputFileName) {
		long startTime = System.currentTimeMillis();

		// Create a byte array to hold the pixel data:
		byte[] rgbData = new byte[this.imageWidth * this.imageHeight * 3];

		Point p0 = cam.findLeftLowerPoint();
		Vector ray;
		Point p, samplePoint;

		for (int i = 0; i < imageHeight; i++) {
			for (int j = 0; j < imageWidth; j++) {
				p = Point.findPoint(p0, cam.x_Axis, j * cam.screenWidth / imageWidth);
				p = Point.findPoint(p, cam.y_Axis, i * cam.screenHeight / imageHeight);

				double[] pColor = { 0, 0, 0 };

				for (int sample_x = 0; sample_x < superSampleLevel; sample_x++) {
					for (int sample_y = 0; sample_y < superSampleLevel; sample_y++) {

						samplePoint = Point.findPoint(p, cam.x_Axis,
								(sample_x + random.nextDouble()) * cam.screenWidth / (superSampleLevel * imageWidth));
						samplePoint = Point.findPoint(samplePoint, cam.y_Axis,
								(sample_y + random.nextDouble()) * cam.screenHeight / (superSampleLevel * imageHeight));
						ray = new Vector(cam.position, samplePoint);
						ray.normalise();

						double[] sampleColor = intersectionColor(cam.position, ray, 0);

						for (int k = 0; k < 3; k++)
							pColor[k] += sampleColor[k];
					}
				}
				/*
				 * according to the way we iterate over the indexes, we matched them in the
				 * saved array (RGBData)
				 */
				for (int k = 0; k < 3; k++)
					rgbData[((imageHeight - i - 1) * this.imageWidth + (j)) * 3 + k] = (byte) Math
							.round(255 * pColor[k] / (superSampleLevel * superSampleLevel));
			}
		}
		long endTime = System.currentTimeMillis();
		Long renderTime = endTime - startTime;
		System.out.println("Finished Rendering scrnr in " + renderTime.toString() + " milliseconds.");

		saveImage(imageWidth, rgbData, outputFileName);
		System.out.println("Saved in " + outputFileName);
	}

	public Surfaces findIntersection(Point p0, Vector direction) {
		double min_t = Double.POSITIVE_INFINITY;
		Surfaces min_primitive = null;

		for (Surfaces shape : surfaces_list) {
			double t = shape.getIntersection(p0, direction);
			if (t == -1)
				continue;
			if (t < min_t && t > 0) {
				min_t = t;
				min_primitive = shape;
			}
		}

		return min_primitive;
	}

	private double[] intersectionColor(Point position, Vector ray, int recLvl) {
		if (recLvl == maxRecursionNum) {
			return new double[] { 0, 0, 0 };
		}
		Point closestPoint = new Point(position.x, position.y, position.z);
		Surfaces closesSurface = findIntersection(position, ray);

		Color pixelColor = new Color(new double[] { 0, 0, 0 });
		if (closesSurface == null)
			return background_RGB;

		double t = closesSurface.getIntersection(position, ray);
		closestPoint = Point.findPoint(position, ray, t);

		Material surfaceMaterial = new Material(mat_list.get(closesSurface.material_index));
		double transparancy = surfaceMaterial.transparency;
		Color transparancyColor = new Color(new double[] { 0, 0, 0 }),
				reflectionColor = new Color(new double[] { 0, 0, 0 });
		Point proxyPoint;

		if (transparancy != 0) {
			proxyPoint = Point.findPoint(closestPoint, ray, epsilon);
			transparancyColor = new Color(intersectionColor(proxyPoint, ray, recLvl + 1));
		}

		double[] reflectionMat = surfaceMaterial.reflectionColor.getValues();
		if (reflectionMat[0] != 0 || reflectionMat[1] != 0 || reflectionMat[2] != 0) {
			Vector normal;
			if (closesSurface.getType() == Surfaces.type.sphere) {
				normal = ((Sphere) closesSurface).getNormal(closestPoint);
			} else if (closesSurface.getType() == Surfaces.type.triangle) {
				normal = ((Triangle) closesSurface).getNormal();
			} else {
				normal = ((InfinitePlane) closesSurface).getNormal();
			}

			Vector reflectionRay = Vector.scaleMult(normal, -2 * Vector.dotProduct(normal, ray));
			reflectionRay = Vector.add(ray, reflectionRay);
			proxyPoint = Point.findPoint(closestPoint, reflectionRay, epsilon);
			reflectionColor = new Color(intersectionColor(proxyPoint, reflectionRay, recLvl + 1));
			reflectionColor.mult(surfaceMaterial.reflectionColor);
		}

		for (Light l : lgt_list) {
			Color curColor = getColor(closestPoint, closesSurface, ray, l);
			if (l.shadowIntensity != 0 && (curColor.R != 0 || curColor.G != 0 || curColor.B != 0)) {
				curColor.scale(1 - l.shadowIntensity + (l.shadowIntensity * getSoftShadow(closestPoint, l)));
			}
			pixelColor.updateColor(curColor, 1);
		}

		pixelColor.normalise();
		pixelColor.scale(1 - transparancy);
		pixelColor.updateColor(transparancyColor, transparancy);
		pixelColor.updateColor(reflectionColor, 1);
		pixelColor.normalise();
		return pixelColor.getValues();
	}

	private double getSoftShadow(Point pt, Light light) {
		Vector lightDirection = new Vector(pt, light.position);
		Vector dir1 = Vector.crossProduct(lightDirection, cam.y_Axis);
		Vector dir2 = Vector.crossProduct(lightDirection, dir1);
		Point lightPosition = Point.findPoint(light.position, dir1, -light.radius / 2);
		lightPosition = Point.findPoint(lightPosition, dir2, -light.radius / 2);

		Vector copyLightDir = new Vector(0, 0, 0);
		double counter = 0, rand1, rand2;
		for (int i = 0; i < root_shadow_rays; i++) {
			copyLightDir = new Vector(light.position.x, light.position.y, light.position.z);
			for (int j = 0; j < root_shadow_rays; j++) {
				rand1 = random.nextDouble();
				rand2 = random.nextDouble();
				copyLightDir = Vector.add(copyLightDir,
						Vector.scaleMult(dir1, rand1 * (light.radius / root_shadow_rays)));
				copyLightDir = Vector.add(copyLightDir,
						Vector.scaleMult(dir2, rand2 * (light.radius / root_shadow_rays)));
				counter += shadowAmount(pt, copyLightDir);
				copyLightDir = Vector.add(copyLightDir,
						Vector.scaleMult(dir1, -rand1 * (light.radius / root_shadow_rays)));
				copyLightDir = Vector.add(copyLightDir,
						Vector.scaleMult(dir2, (1 - rand2) * (light.radius / root_shadow_rays)));
			}
			lightPosition = Point.findPoint(lightPosition, dir1, light.radius / root_shadow_rays);
		}
		return counter / (root_shadow_rays * root_shadow_rays);
	}

	private double shadowAmount(Point pt, Vector light) {
		Point lightPoint = new Point(light.x,light.y,light.z);
		Vector dir = new Vector(lightPoint, pt);
		double length = dir.length;
		dir.normalise();
		double lightLvl = 1, t;
		for (Surfaces s:surfaces_list) {
			t = s.getIntersection(lightPoint, dir);
			if(t<length-epsilon && t>epsilon) {
				double transparancy = (mat_list.get(s.material_index)).transparency;
				if(transparancy == 0)
					return 0;
				lightLvl*= transparancy;
			}
		}
		return lightLvl;
	}

	private Color getColor(Point pt, Surfaces sur, Vector visionDir, Light light) {

		Material mat = new Material(mat_list.get(sur.material_index));
		Color diffuseColor = new Color(new double[] { mat.diffuseColor.R, mat.diffuseColor.G, mat.diffuseColor.B });

		Color specularColor = new Color(new double[] { mat.specularColor.R, mat.specularColor.G, mat.specularColor.B });
		// update the points
		Vector lightDir = new Vector(pt, new Point(light.position.x, light.position.y, light.position.z));
		lightDir.normalise();
		Vector normal;
		if (sur.getType() == Surfaces.type.sphere) {
			normal = ((Sphere) sur).getNormal(pt);
		} else if (sur.getType() == Surfaces.type.triangle) {
			normal = ((Triangle) sur).getNormal();
		} else {
			normal = ((InfinitePlane) sur).getNormal();
		}
		if (Vector.dotProduct(visionDir, normal) > 0)
			normal = Vector.scaleMult(normal, -1);
		double alpha = Vector.dotProduct(lightDir, normal);
		// System.out.println(alpha);
		if (alpha <= 0) {
			diffuseColor.scale(0);
			return new Color(new double[] { 0, 0, 0 });
		}
		diffuseColor.scale(alpha);

		if (specularColor.R != 0 || specularColor.G != 0 || specularColor.B != 0) {
			Vector reflect = new Vector(normal);
			reflect = Vector.scaleMult(reflect, 2 * Vector.dotProduct(lightDir, normal));
			reflect = Vector.add(reflect, Vector.scaleMult(lightDir, -1));
			alpha = Vector.dotProduct(visionDir, reflect);
			if (alpha < 0) {
				alpha = Math.pow(alpha, mat.PhongSpecularityCoefficient);
				specularColor.scale(alpha);
				diffuseColor.updateColor(specularColor, light.specularIntensity);
			}
		}
		diffuseColor.normalise();
		diffuseColor.mult(light.color);
		return diffuseColor;
	}

	/*
	 * Saves RGB data as an image in png format to the specified location.
	 */
	public static void saveImage(int width, byte[] rgbData, String fileName) {
		try {

			BufferedImage image = bytes2RGB(width, rgbData);
			ImageIO.write(image, "png", new File(fileName));

		} catch (IOException e) {
			System.out.println("ERROR SAVING FILE: " + e.getMessage());
		}

	}
	//////////////////////// FUNCTIONS TO SAVE IMAGES IN PNG FORMAT
	//////////////////////// //////////////////////////////////////////

	/*
	 * Producing a BufferedImage that can be saved as png from a byte array of RGB
	 * values.
	 */
	public static BufferedImage bytes2RGB(int width, byte[] buffer) {
		int height = buffer.length / width / 3;
		ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_sRGB);
		ColorModel cm = new ComponentColorModel(cs, false, false, Transparency.OPAQUE, DataBuffer.TYPE_BYTE);
		SampleModel sm = cm.createCompatibleSampleModel(width, height);
		DataBufferByte db = new DataBufferByte(buffer, width * height);
		WritableRaster raster = Raster.createWritableRaster(sm, db, null);
		BufferedImage result = new BufferedImage(cm, raster, false, null);

		return result;
	}

	public static class RayTracerException extends Exception {

		private static final long serialVersionUID = 1L;

		public RayTracerException(String msg) {
			super(msg);
		}
	}

}
