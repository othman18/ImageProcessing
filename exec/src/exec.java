import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class exec {
	public static void main(String[] args) throws IOException {

		if (args.length < 5) {
			System.out.println("args error");
			return;
		}
		BufferedImage image = null;
		File inputFile = null;
		String inputPath = args[0];
		String outputPath = args[4];

		int inputWidth = 0, inputHeight = 0, seamType;

		if (isInteger(args[1]) && isInteger(args[2]) && isInteger(args[3])) {
			inputWidth = Integer.parseInt(args[1]);
			inputHeight = Integer.parseInt(args[2]);
			seamType = Integer.parseInt(args[3]);
		} else {
			System.out.println("input rows/cols error");
			return;
		}
		if (inputWidth <= 0 || inputHeight <= 0) {
			System.out.println("input rows/cols error");
			return;

		}
		// read image
		try {
			inputFile = new File(inputPath);
			image = ImageIO.read(inputFile);
			System.out.println("Reading complete.");

		} catch (IOException e) {
			System.out.println("bad input path");
			e.printStackTrace();
		}

		if (inputHeight < 0 || inputWidth < 0) {
			System.out.println("resize too small");
			return;
		}

		int newHeight = Math.abs(inputHeight - image.getHeight()), newWidth = Math.abs(inputWidth - image.getWidth());
		if (newHeight == 0 && newWidth == 0) {
			System.out.println("same dimensions");
			return;
		}

		EnergyFunctions aa = new EnergyFunctions(image.getWidth(), image.getHeight());
		aa.calculateCells(image);
		seamCalculate s = null;

		switch (seamType) {
		case 0:
			break;
		case 1:
			aa.doEntropy = true;
			break;
		case 2:
			s = new seamCalculate(aa, seamShape.generalForward);
			aa.doBlend = false;
			break;
		default:
			System.out.println("energy error");
			return;
		}

		if (s == null)
			s = new seamCalculate(aa, seamShape.generalBackward);
		
		boolean flag = true;
		// change to new width
				
		if (inputWidth > image.getWidth()) {
			int factorWidth = image.getWidth() / 2;
			while (flag) {
				if (newWidth < factorWidth) {
					factorWidth = newWidth;
					flag = false;
				}
				image = aa.addKVerticalSeams(factorWidth, s, image); 	///////////////////// __________________________
				System.out.println("added " + factorWidth + " vertical seams");
				newWidth -= factorWidth;
			}
		} else if (inputWidth < image.getWidth()) {
			for (int i = 0; i < newWidth; i++) {
				image = aa.removeVerticalSeam(s, image);
			}
			System.out.println("removed " + newWidth + " vertical seams");
		}
		
		// change to new height
		flag = true;
		if (inputHeight > image.getHeight()) {
			
			while (flag) {
				int factorHeight = image.getHeight() / 2;
				System.out.println(factorHeight);
				if (newHeight < factorHeight) {
					factorHeight = newHeight;
					flag = false;
				}
				image = aa.transposeImageRight(s, image);
				aa.updateEnergyMatrix(image.getWidth(), image.getHeight());
				aa.calculateCells(image);

				s = new seamCalculate(aa, s.shape); // save the s in the heap

				image = aa.addKVerticalSeams(factorHeight, s, image); ///////////////////// __________________________
				image = aa.transposeImageRight(s, image);
				System.out.println("added " + factorHeight + " horizontal seams");
				newHeight -= factorHeight;
			}
		} else if (inputHeight < image.getHeight()) {
			image = aa.transposeImageRight(s, image);
			for (int i = 0; i < newHeight; i++) {
				aa.updateEnergyMatrix(image.getWidth(), image.getHeight());
				aa.calculateCells(image);
				image = aa.removeVerticalSeam(s, image);
				s = new seamCalculate(aa, s.shape); // save the s in the heap
			}
			image = aa.transposeImageRight(s, image);
			System.out.println("removed " + newHeight + " horizontal seams");
		}

		// write image
		inputFile = new File(outputPath + "_entropy_" + aa.doEntropy +"__"
				+ s.shape + "__" + inputWidth + "X" + inputHeight + ".jpg");
		try {
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
