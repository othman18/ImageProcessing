import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class exec {
	public static void main(String[] args) {
		EnergyFunctions aa;

		// read image
		BufferedImage image = null;
		File inputFile = null;
		try {

			inputFile = new File("/Users/othman/Downloads/old_photos/img.jpeg");
			image = ImageIO.read(inputFile);

			System.out.println("Reading complete.");

		} catch (IOException e) {

			e.printStackTrace();
		}
		int width = image.getWidth();
		int height = image.getHeight();

		aa = new EnergyFunctions(width, height);
		aa.calculateCells(image);
		// write image
		try {
			inputFile = new File("/Users/othman/Downloads/old_photos/out.jpeg");
			ImageIO.write(image, "jpg", inputFile);
		} catch (IOException e) {
			System.out.println(e);
		}
	}
}
