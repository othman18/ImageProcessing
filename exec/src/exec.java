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

		int inputWidth = 0, inputHeight = 0, saemType;
		if (isInteger(args[1]) && isInteger(args[2]) && isInteger(args[3])) {
			inputWidth = Integer.parseInt(args[1]);
			inputHeight = Integer.parseInt(args[2]);
			saemType = Integer.parseInt(args[3]);
		} else {
			System.out.println("input rows/cols error");
			return;
		}
		if(inputWidth <= 0 || inputHeight<=0) {
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

		if(newHeight == 0 && newWidth==0) {
			System.out.println("same dimensions");
			return;
		}

		EnergyFunctions aa = new EnergyFunctions(image.getWidth(), image.getHeight());
		aa.calculateCells(image);
		seamCalculate s = null;

		switch (saemType) {
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

		// aa.doEntropy = true;
		// aa.doBlend = false;
//		aa.calculateCells(image);
		
		// change to new width
		if (inputWidth > image.getWidth()) {
			image = aa.addKVerticalSeams(newWidth, s, image);
			System.out.println("added "+newWidth+" vertical seams");
		}else if (inputWidth < image.getWidth()) {
			for (int i = 0; i < newWidth; i++) {
				image = aa.removeVerticalSeam(s, image);
			}
			System.out.println("removed "+newWidth+" vertical seams");
		}
		// change to new height
		System.out.println("newWidth="+newWidth+", newHeight="+newHeight);
		if (inputHeight > image.getHeight()) {
			image = aa.transposeImageRight(s, image);
			aa.updateEnergyMatrix(image.getWidth(), image.getHeight());
			aa.calculateCells(image);
			
			s=new seamCalculate(aa, s.shape); //save the s in the heap
			
			image = aa.addKVerticalSeams(newHeight, s, image);
			image = aa.transposeImageRight(s, image);
			System.out.println("added "+newHeight+" horizontal seams");
			}
		else if (inputHeight < image.getHeight()) {
			for (int i = 0; i < newHeight; i++) {
				image = aa.removeHorizontalSeam(s, image);
			}
			System.out.println("removed "+newWidth+" horizontal seams");
		}

		/*/ -----------------------------remove this____________________________________
		EnergyFunctions aa = new EnergyFunctions(image.getWidth(), image.getHeight());
		seamCalculate s = new seamCalculate(aa, seamShape.generalForward);
		aa.doBlend = false;
		aa.calculateCells(image);
		int simNum = 100;
		image = aa.addKVerticalSeams(simNum, s, image);
		*/// -----------------------------remove this____________________________________

		// write image
		inputFile = new File(outputPath+"z__entropy_" + aa.doEntropy + "__blend_"
				+ aa.doBlend + "__seamNum_" + "__" + s.shape + "_insert.jpg");
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
