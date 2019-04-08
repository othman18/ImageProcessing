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
			inputFile = new File("/Users/othman/Downloads/image_test/1test.jpg");
			image = ImageIO.read(inputFile);
			System.out.println("Reading complete.");

		} catch (IOException e) {
			e.printStackTrace();
		}

		int width = image.getWidth();
		int height = image.getHeight();
		aa = new EnergyFunctions(width, height);
		aa.calculateCells(image);
		seamCalculate s = new seamCalculate(aa, seamShape.general);
		s.updateSeam();
		// write image
		try {
			BufferedImage tmp;
			int simNum = 100;
			tmp = aa.insertSeams(simNum, s, image);
			inputFile = new File("/Users/othman/Downloads/image_test/out_"+simNum+"_insert.jpg");
			ImageIO.write(tmp, "jpg", inputFile);
			/*
			for (int i = 0; i < simNum; i++) {
				image = aa.addVerticalSeam(s, image);
			}
			inputFile = new File("/Users/othman/Downloads/image_test/out_normal_insert.png");
			ImageIO.write(image, "jpeg", inputFile);
			*/

		} catch (IOException e) {
			System.out.println(e);
		}
		System.out.println("writing complete");
	}
}
