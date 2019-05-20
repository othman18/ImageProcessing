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

import myUtils.*;

import javax.imageio.ImageIO;
import javax.swing.plaf.basic.BasicInternalFrameTitlePane.MaximizeAction;

import surfaces.*;

/**
 * Main class for ray tracing exercise.
 */
public class RayTracer {

	public int imageWidth, imageHeight;
	public int superSampleLevel, maxRecursionNum, root_shadow_rays;
	public double[] background_RGB = new double[3];
	Camera cam;
	Random random = new Random();
	List<Material> mat_list;
	List<Surfaces> surfaces_list;
	List<Light> lgt_list;

	/**
	 * Runs the ray tracer. Takes scene file, output image file and image size
	 * as input.
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
			System.out.println(e.getMessage());
		} catch (Exception e) {
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
		System.out.println("Started parsing scene file " + sceneFileName);

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
					cam = new Camera(position, lookAtPoint, upVector, screenDistance, screenWidth);
					System.out.println(String.format("Parsed camera parameters (line %d)", lineNum));

				} else if (code.equals("set")) {
					// Add code here to parse general settings parameters
					if (params.length != 6) {
						System.out.println("general settings input file error");
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

		// It is recommended that you check here that the scene is valid,
		// for example camera settings and all necessary materials were defined.

		System.out.println("Finished parsing scene file " + sceneFileName);

	}

	/**
	 * Renders the loaded scene and saves it to the specified file location.
	 */
	public void renderScene(String outputFileName) {
		long startTime = System.currentTimeMillis();

		// Create a byte array to hold the pixel data:
		byte[] rgbData = new byte[this.imageWidth * this.imageHeight * 3];

		/** camera */
		double distance = cam.getDistance(), sam_partition = (1 / ((double) superSampleLevel)), addition_height,
				addition_width;
		Point p, p0 = cam.findStartPoint(distance, imageWidth, imageHeight);
		double[] p_color;
		int super_sam_sqr = superSampleLevel * superSampleLevel;
		Vector ray;
		for (int i = 0; i < imageHeight; i++) {
			for (int j = 0; j < imageWidth; j++) {
				p_color = new double[3];
				p = Assistant.getRelevantPoint(i, j, p0, cam);
				for (int numSample = 0; numSample < super_sam_sqr; numSample++) {

					/* find the additions per area index */
					addition_height = (numSample % superSampleLevel + random.nextDouble()) * sam_partition;
					addition_width = (numSample / superSampleLevel + random.nextDouble()) * sam_partition;

					/* redirect the ray */
					ray = new Vector(p.x, p.y, p.z);
					ray.add(cam.x_Axis.mult(addition_width));
					ray.add(cam.fixedUpVector.mult(addition_height));
					ray.normalise();

					double[] sample_color = rayHitColor(p, ray, 0);

					p_color[0] += sample_color[0];
					p_color[1] += sample_color[1];
					p_color[2] += sample_color[2];

				}
				rgbData[(i * this.imageWidth + j) * 3] = (byte) Math.round(255 * p_color[0] / super_sam_sqr);
				rgbData[(i * this.imageWidth + j) * 3 + 1] = (byte) Math.round(255 * p_color[1] / super_sam_sqr);
				rgbData[(i * this.imageWidth + j) * 3 + 2] = (byte) Math.round(255 * p_color[2] / super_sam_sqr);

			}
		}
		long endTime = System.currentTimeMillis();
		Long renderTime = endTime - startTime;

		System.out.println("Finished rendering scene in " + renderTime.toString() + " milliseconds.");

		// This is already implemented, and should work without adding any code.
		saveImage(this.imageWidth, rgbData, outputFileName);

		System.out.println("Saved file " + outputFileName);

	}

	private double[] rayHitColor(Point p, Vector ray, int recursionNum) {
		if (recursionNum == maxRecursionNum)
			return new double[] { 0, 0, 0 };

		Surfaces intersectionShape = Assistant.findIntersection(p, ray, surfaces_list);
		if (intersectionShape == null)
			return background_RGB;

		double t = intersectionShape.getIntersection(p, ray);
		Point intersectionPoint = Point.findPoint(p, ray, t);

		/*
		 * 
		 * do the transparancy and the reflection !!!!!!!!!!!
		 */

		for (Light lgt : lgt_list) {
			Color p_color = getColor(intersectionPoint, ray, intersectionShape, lgt);
		}
		return null;
	}

	//////////////////////// FUNCTIONS TO SAVE IMAGES IN PNG FORMAT
	//////////////////////// //////////////////////////////////////////

	private Color getColor(Point intersectionPoint, Vector ray, Surfaces intersectionShape, Light lgt) {

		Material mat = mat_list.get(intersectionShape.material_index);
		Color diffuseColor = mat.diffuseColor;
		Color specularColor = mat.specularColor;
		Vector lightDirection = new Vector(intersectionPoint, lgt.position);
		lightDirection.normalise();
		if (intersectionShape.getType() == Surfaces.type.sphere)
			((Sphere) intersectionShape).setNormalPoint(intersectionPoint);
		
		Vector normal = intersectionShape.getNormal();
		/*if (Vector.scalarMul(visionDir, normal) > 0)
			normal.scale(-1);
		double alpha = (Vector.scalarMul(lightDir, normal));
		if (alpha <= 0) {
			diffuseColor.scale(0);
			return diffuseColor;
		}
		diffuseColor.scale(alpha);
		if (specularColor.get(0) != 0 || specularColor.get(1) != 0 || specularColor.get(2) != 0) {

			Vector reflect = new Vector(normal);
			reflect.scale(2 * Vector.scalarMul(lightDir, normal));
			reflect.add(lightDir, -1);
			alpha = Vector.scalarMul(visionDir, reflect);
			if (alpha < 0) {
				alpha = Math.pow(alpha, mat.getPhong());
				specularColor.scale(alpha);
				diffuseColor.add(specularColor, light.getSpecular());
			}
		}
		diffuseColor.normalise();
		diffuseColor.mul(light.getIntensity());
*/
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

	/*
	 * Producing a BufferedImage that can be saved as png from a byte array of
	 * RGB values.
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
		public RayTracerException(String msg) {
			super(msg);
		}
	}

}
