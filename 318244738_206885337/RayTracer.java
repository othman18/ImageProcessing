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

	int imageWidth, imageHeight, superSampleLevel, maxRecursionNum, root_shadow_rays;
	double[] background_RGB = new double[3];
	Camera cam;
	Random random = new Random();
	List<Material> mat_list;
	List<Surfaces> surfaces_list;
	List<Light> lgt_list;
	double epsilon = 0.00000318244738;

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
		System.out.println("rendering...");
		for (int i = 0; i < imageHeight; i++) {
			for (int j = 0; j < imageWidth; j++) {
				p = Point.findPoint(p0, cam.xAxis, j * cam.screenWidth / imageWidth);
				p = Point.findPoint(p, cam.yAxis, i * cam.screenHeight / imageHeight);

				double[] pColor = { 0, 0, 0 };
				// super sampling loop
				for (int sample_x = 0; sample_x < superSampleLevel; sample_x++) {
					for (int sample_y = 0; sample_y < superSampleLevel; sample_y++) {
						// find the random addition towards the xAxis and the yAxis in the
						// current pixel area
						samplePoint = Point.findPoint(p, cam.xAxis,
								(sample_x + random.nextDouble()) * cam.screenWidth / (superSampleLevel * imageWidth));
						samplePoint = Point.findPoint(samplePoint, cam.yAxis,
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
			if (((double) 100 * i / imageHeight) % 10 == 0)
				System.out.println(" " + Math.round(100 * (double) i / imageHeight) + "%");
		}
		System.out.println(100+"%");
		long endTime = System.currentTimeMillis();
		Long renderTime = endTime - startTime;
		System.out.println("Finished Rendering scrnr in " + renderTime.toString() + " milliseconds.");

		saveImage(imageWidth, rgbData, outputFileName);
		System.out.println("Saved in " + outputFileName);
	}

	/** return the closest surface that the ray intersects (slides) */
	public Surfaces findIntersection(Point p0, Vector rayDirection) {
		double min_t = Double.POSITIVE_INFINITY;
		Surfaces min_primitive = null;

		for (Surfaces shape : surfaces_list) {
			double t = shape.getIntersection(p0, rayDirection);
			if (t == -1) // no intersection found
				continue;
			if (t < min_t && t > 0) {
				min_t = t;
				min_primitive = shape;
			}
		}
		return min_primitive;
	}

	public double[] intersectionColor(Point position, Vector ray, int recNum) {
		if (recNum == maxRecursionNum)
			return new double[] { 0, 0, 0 };

		Surfaces intersectionSurface = findIntersection(position, ray);
		// if there is no intersection then return the back ground color
		if (intersectionSurface == null)
			return background_RGB;

		Color outputColor = new Color(new double[] { 0, 0, 0 });
		double t = intersectionSurface.getIntersection(position, ray);
		Point intersectionPoint = Point.findPoint(position, ray, t);
		Material surfaceMat = new Material(mat_list.get(intersectionSurface.material_index));
		double transparency = surfaceMat.transparency;
		Color transparancyColor = new Color(new double[] { 0, 0, 0 }),
				reflectionColor = new Color(new double[] { 0, 0, 0 });
		Point reflectedPoint;

		// if the surface is transparent, then create another transparency ray
		if (transparency != 0) {
			reflectedPoint = Point.findPoint(intersectionPoint, ray, epsilon);
			transparancyColor = new Color(intersectionColor(reflectedPoint, ray, recNum + 1));
		}
		// create a reflection ray
		double[] reflecionMat = surfaceMat.reflectionColor.getValues();
		if (reflecionMat[0] != 0 || reflecionMat[1] != 0 || reflecionMat[2] != 0) {
			Vector normal;
			if (intersectionSurface.getType() == Surfaces.type.sphere)
				// sphere has to update the intersection point in order to get the right normal
				((Sphere) intersectionSurface).setIntersectionPoint(intersectionPoint);

			normal = intersectionSurface.getNormal();
			Vector reflectionRay = Vector.scaleMult(normal, -2 * Vector.dotProduct(normal, ray));
			reflectionRay = Vector.add(ray, reflectionRay);
			reflectedPoint = Point.findPoint(intersectionPoint, reflectionRay, epsilon);
			reflectionColor = new Color(intersectionColor(reflectedPoint, reflectionRay, recNum + 1));
			reflectionColor.mult(surfaceMat.reflectionColor);
		}

		// calculate the shadow and illuminate the outputColor
		for (Light light : lgt_list) {
			Color tmpColor = getColor(intersectionPoint, intersectionSurface, ray, light);
			if (light.shadowIntensity != 0 && (tmpColor.R != 0 || tmpColor.G != 0 || tmpColor.B != 0))
				tmpColor.scale(
						1 - light.shadowIntensity + (light.shadowIntensity * softShadow(intersectionPoint, light)));
			outputColor.updateColor(tmpColor, 1);
		}

		outputColor.normalise();
		outputColor.scale(1 - transparency);
		outputColor.updateColor(transparancyColor, transparency);
		outputColor.updateColor(reflectionColor, 1);
		outputColor.normalise();
		return outputColor.getValues();
	}

	/** calculate soft shadow using the light's position and direction */
	private double softShadow(Point p, Light light) {
		Vector lightDirection = new Vector(p, light.position);
		Vector firstRay = Vector.crossProduct(lightDirection, cam.yAxis);
		Vector secondRay = Vector.crossProduct(lightDirection, firstRay);
		Point fixedLightPosition = Point.findPoint(light.position, firstRay, -light.radius / 2);
		fixedLightPosition = Point.findPoint(fixedLightPosition, secondRay, -light.radius / 2);

		Vector tmpLightDir;
		double counter = 0, rand1, rand2;
		for (int i = 0; i < root_shadow_rays; i++) {
			tmpLightDir = new Vector(fixedLightPosition.x, fixedLightPosition.y, fixedLightPosition.z);
			for (int j = 0; j < root_shadow_rays; j++) {
				rand1 = random.nextDouble();
				rand2 = random.nextDouble();
				tmpLightDir = Vector.add(tmpLightDir,
						Vector.scaleMult(firstRay, rand1 * (light.radius / root_shadow_rays)));
				tmpLightDir = Vector.add(tmpLightDir,
						Vector.scaleMult(secondRay, rand2 * (light.radius / root_shadow_rays)));
				counter += getShadowNum(p, tmpLightDir);
				tmpLightDir = Vector.add(tmpLightDir,
						Vector.scaleMult(firstRay, -rand1 * (light.radius / root_shadow_rays)));
				tmpLightDir = Vector.add(tmpLightDir,
						Vector.scaleMult(secondRay, (1 - rand2) * (light.radius / root_shadow_rays)));
			}
			fixedLightPosition = Point.findPoint(fixedLightPosition, firstRay, light.radius / root_shadow_rays);
		}
		return counter / (root_shadow_rays * root_shadow_rays);
	}

	private double getShadowNum(Point pt, Vector light) {
		Point lightPoint = new Point(light.x, light.y, light.z);
		Vector rayDirection = new Vector(lightPoint, pt);
		double length = rayDirection.length;
		rayDirection.normalise();
		double lightNum = 1, t;
		for (Surfaces currentSurface : surfaces_list) {
			t = currentSurface.getIntersection(lightPoint, rayDirection);
			if (t < length - epsilon && t > epsilon) {
				double transparancy = (mat_list.get(currentSurface.material_index)).transparency;
				if (transparancy == 0)
					return 0;
				lightNum *= transparancy;
			}
		}
		return lightNum;
	}

	private Color getColor(Point pt, Surfaces sur, Vector visionDir, Light light) {

		Material mat = new Material(mat_list.get(sur.material_index));
		Color diffuseColor = new Color(new double[] { mat.diffuseColor.R, mat.diffuseColor.G, mat.diffuseColor.B });
		Color specularColor = new Color(new double[] { mat.specularColor.R, mat.specularColor.G, mat.specularColor.B });
		// update the points
		Vector lightDirection = new Vector(pt, new Point(light.position.x, light.position.y, light.position.z));
		lightDirection.normalise();
		Vector normal;

		if (sur.getType() == Surfaces.type.sphere)
			((Sphere) sur).setIntersectionPoint(pt);

		normal = sur.getNormal();
		if (Vector.dotProduct(visionDir, normal) > 0)
			normal = Vector.scaleMult(normal, -1);
		double theta = Vector.dotProduct(lightDirection, normal);
		if (theta <= 0) {
			diffuseColor.scale(0);
			return new Color(new double[] { 0, 0, 0 });
		}
		diffuseColor.scale(theta);

		if (specularColor.R != 0 || specularColor.G != 0 || specularColor.B != 0) {
			Vector reflect = new Vector(normal);
			reflect = Vector.scaleMult(reflect, 2 * Vector.dotProduct(lightDirection, normal));
			reflect = Vector.add(reflect, Vector.scaleMult(lightDirection, -1));
			theta = Vector.dotProduct(visionDir, reflect);
			if (theta < 0) {
				theta = Math.pow(theta, mat.PhongSpecularityCoefficient);
				specularColor.scale(theta);
				diffuseColor.updateColor(specularColor, light.specularIntensity);
			}
		}
		diffuseColor.normalise();
		diffuseColor.mult(light.color);
		return diffuseColor;
	}

	/**
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

	/**
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
