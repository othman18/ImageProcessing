import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class exec {
	public static void main(String[] args) throws IOException {
		// read image
		
		BufferedImage image = null;
		File inputFile = null;
/*		
		int inputWidth = 0, inputHeight = 0, saemType;
		if (isInteger(args[1]) && isInteger(args[2]) && isInteger(args[3])) {
			inputWidth = Integer.parseInt(args[1]);
			inputHeight = Integer.parseInt(args[2]);
			saemType = Integer.parseInt(args[3]);
		} else {
			System.out.println("input  rows/cols error");
			return;
		}
*/
		try {
			inputFile = new File("/Users/othman/Downloads/image_test/_test.jpg");
			image = ImageIO.read(inputFile);
			System.out.println("Reading complete.");

		} catch (IOException e) {
			System.out.println("bad input path");
			e.printStackTrace();
		}
/*		

		if (inputHeight < 0  || inputWidth < 0) {
			System.out.println("resize too small");
			return;
		}
		EnergyFunctions aa = new EnergyFunctions(image.getWidth(), image.getHeight());
		seamCalculate s = null;

		switch (saemType) {
		case 1:
			aa.doEntropy = true;
			break;
		case 2:
			s = new seamCalculate(aa, seamShape.generalForward);
			break;
		default:
			System.out.println("energy error");
			return;
		}
		if (s == null)
			s = new seamCalculate(aa, seamShape.generalBackward);

		// aa.doEntropy = true;
		// aa.doBlend = false;
		aa.calculateCells(image);
		int newHeight = Math.abs(inputHeight)-image.getHeight(),
				newWidth= Math.abs(inputWidth-image.getWidth());
		
		//change to new width
		if(inputWidth>image.getWidth())
			image = aa.addKVerticalSeams(newWidth, s, image);
		else if(inputWidth<image.getWidth()){
			for(int i=0;i<newWidth;i++)
				image = aa.removeVerticalSeam(s, image);	
		}
		//change to new height
		if(inputHeight>image.getHeight())
			image = aa.addKHorizontalSeams(newHeight, s, image);
		else if(inputHeight<image.getHeight()){
			for(int i=0;i<newHeight;i++)
				image = aa.removeHorizontalSeam(s, image);	
		}
*/

		//-----------------------------remove this____________________________________
		EnergyFunctions aa = new EnergyFunctions(image.getWidth(), image.getHeight());
		seamCalculate s = new seamCalculate(aa, seamShape.generalForward);
		int simNum = 300;
		//-----------------------------remove this____________________________________

		// write image
		try {
			image = aa.addKVerticalSeams(simNum, s, image);
			inputFile = new File("/Users/othman/Downloads/image_test/z__entropy_" + aa.doEntropy + "__blend_"
					+ aa.doBlend + "__seamNum_" + simNum + "__" + s.shape + "_insert.jpg");
			ImageIO.write(image, "jpg", inputFile);
		} catch (IOException e) {
			System.out.println("bad output path");
			System.out.println(e);
		}
		System.out.println("writing complete");
	}

	public static boolean isInteger(String s) {
		try {
			Integer.parseInt(s);
		} catch (NumberFormatException e) {
			return false;
		} catch (NullPointerException e) {
			return false;
		}
		return true;
	}
}
