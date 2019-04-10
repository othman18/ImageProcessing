import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/*
 * TODO
 * parse image according to it's ending !!!!!
 * 
 * */

public class exec {
	public static void main(String[] args) throws IOException {
		// read image
		BufferedImage image = null;
		File inputFile = null;
		try {
			inputFile = new File("/Users/othman/Downloads/image_test/_test3.jpg");
			image = ImageIO.read(inputFile);
			System.out.println("Reading complete.");

		} catch (IOException e) {
			e.printStackTrace();
		}

		int width = image.getWidth();
		int height = image.getHeight();
		EnergyFunctions aa = new EnergyFunctions(width, height);
		aa.doEntropy = false;
		aa.calculateCells(image);
		seamCalculate s = new seamCalculate(aa, seamShape.general);
		
		//s.updateSeam();
		

		// write image
		try {
			BufferedImage tmp;
			int simNum = 700;
			
			tmp = aa.insertSeamsNew(simNum, s, image);
			if (aa.doEntropy)
				inputFile = new File("/Users/othman/Downloads/image_test/out_entropy_" + simNum + "_insert.jpg");
			else
				inputFile = new File("/Users/othman/Downloads/image_test/out_" + simNum + "_insert.jpg");
			ImageIO.write(tmp, "jpg", inputFile);
			/*
			 * for (int i = 0; i < simNum; i++) { image = aa.addVerticalSeam(s, image); }
			 * inputFile = new
			 * File("/Users/othman/Downloads/image_test/out_normal_insert.png");
			 * ImageIO.write(image, "jpeg", inputFile);
			 */

		} catch (IOException e) {
			System.out.println(e);
		}
		System.out.println("writing complete");
	}
}
