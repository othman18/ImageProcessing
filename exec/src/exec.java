import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.imageio.ImageIO;
/*
 * flip el x ma3 el y
 * 
 * */
public class exec {
	public static void main(String[] args) {
		EnergyFunctions aa=new EnergyFunctions(3,3);
		aa.cellMatrix[1][1].setRGB(0xf);
		aa.cellMatrix[1][2].setRGB(0x0);
		aa.cellMatrix[1][3].setRGB(0xa);
		aa.cellMatrix[2][1].setRGB(0x2);
		aa.cellMatrix[2][2].setRGB(0x5);
		aa.cellMatrix[2][3].setRGB(0xc);
		aa.cellMatrix[3][1].setRGB(0xf);
		aa.cellMatrix[3][2].setRGB(0x0);
		aa.cellMatrix[3][3].setRGB(0x6);
		aa.caclculatedCellsAlready=true;
		for(int i=1;i<4;i++){
			for (int j = 1; j < 4; j++) {
				aa.calculateEnergy(i, j);
			}
		}
//		System.out.println(Arrays.deepToString(aa.cellMatrix));
		
		seamCalculate s=new seamCalculate(aa, seamShape.general);
		s.updateSeam();
		aa.printer();
		System.out.println(Arrays.deepToString(s.coors));
		s=new seamCalculate(aa, seamShape.straight);
		s.updateSeam();
		System.out.println(Arrays.deepToString(s.coors));
		
    /*	// read image
		BufferedImage image = null;
		File inputFile = null;
		try {

			inputFile = new File("/Users/othman/Downloads/old_photos/img.jpeg");
			image = ImageIO.read(inputFile);

			System.out.println("Reading complete.");

		} catch (IOException e) {

			e.printStackTrace();
		}
		int  height= image.getWidth();
		int  width= image.getHeight();

		aa = new EnergyFunctions(height,width);
		aa.calculateCells(image);
		// write image
		try {
			inputFile = new File("/Users/othman/Downloads/old_photos/out.jpeg");
			ImageIO.write(image, "jpg", inputFile);
		} catch (IOException e) {
			System.out.println(e);
		}*/
	}
}
