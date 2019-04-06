import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.imageio.ImageIO;

/*
 * othman's notes 6.4.19
 * update seam does not work when we trim an image, or add anything to it.
 * we should make the energy function obj dynamic or we would have to create a new obj each time
 * we remove a seam. (see example below)
 * 
 * */
public class exec {
	public static void main(String[] args) {
		EnergyFunctions aa = new EnergyFunctions(3, 3);
		/*
		 * aa.cellMatrix[1][1].setRGB(0xf); aa.cellMatrix[1][2].setRGB(0x0);
		 * aa.cellMatrix[1][3].setRGB(0xa); aa.cellMatrix[2][1].setRGB(0x2);
		 * aa.cellMatrix[2][2].setRGB(0x5); aa.cellMatrix[2][3].setRGB(0xc);
		 * aa.cellMatrix[3][1].setRGB(0xf); aa.cellMatrix[3][2].setRGB(0x0);
		 * aa.cellMatrix[3][3].setRGB(0x6); aa.caclculatedCellsAlready=true; for(int
		 * i=1;i<4;i++){ for (int j = 1; j < 4; j++) { aa.calculateEnergy(i, j); } } //
		 * System.out.println(Arrays.deepToString(aa.cellMatrix));
		 * 
		 * seamCalculate s=new seamCalculate(aa, seamShape.general); s.updateSeam();
		 * aa.printer(); System.out.println(Arrays.deepToString(s.coors)); s=new
		 * seamCalculate(aa, seamShape.straight); s.updateSeam();
		 * System.out.println(Arrays.deepToString(s.coors));
		 */

		// read image
		BufferedImage image = null;
		File inputFile = null;
		try {

			inputFile = new File("/Users/othman/Downloads/image_test/dude.jpg");
			image = ImageIO.read(inputFile);
			System.out.println("Reading complete.");

		} catch (IOException e) {

			e.printStackTrace();
		}
		int width = image.getWidth();
		int height = image.getHeight();
		System.out.println(width + " , " + height);
		aa = new EnergyFunctions(width, height);
		aa.calculateCells(image);

		seamCalculate s = new seamCalculate(aa, seamShape.general);
		s.updateSeam();
		aa.colorSeam(s.coors, image);
		// write image
		try {
			inputFile = new File("/Users/othman/Downloads/image_test/out.jpg");
			ImageIO.write(image, "jpg", inputFile);
			inputFile = new File("/Users/othman/Downloads/image_test/removedSeam.jpg");
			for(int i = 0; i< 50;i++) {
				aa.colorSeam(s.coors, image);
				inputFile = new File("/Users/othman/Downloads/image_test/colorout"+i+".jpg");
				ImageIO.write(image, "jpg", inputFile);
				image = aa.removeVerticalSeam(s.coors, image);
				inputFile = new File("/Users/othman/Downloads/image_test/out"+i+".jpg");
				ImageIO.write(image, "jpg", inputFile);
				aa = new EnergyFunctions(image.getWidth(), image.getHeight());
				aa.calculateCells(image);
				s = new seamCalculate(aa, seamShape.general);
				s.updateSeam();
			}
			ImageIO.write(image, "jpg", inputFile);
		} catch (IOException e) {
			System.out.println(e);
		}
		System.out.println("writing complete");
	}
}
