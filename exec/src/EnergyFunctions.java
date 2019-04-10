import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.Math;
import javax.imageio.ImageIO;

public class EnergyFunctions {

	Cell[][] cellMatrix;
	int cols, rows;
	boolean caclculatedCellsAlready = false, doEntropy = false;

	EnergyFunctions(int width, int height) {
		cellMatrix = new Cell[width + 2][height + 2];

		this.cols = width;
		this.rows = height;

		for (int x = 0; x < this.cols + 2; x++) {
			for (int y = 0; y < this.rows + 2; y++) {
				cellMatrix[x][y] = new Cell();
			}
		}
	}

	public void printer() {
		for (int i = 0; i < rows + 2; i++) {
			for (int j = 0; j < cols + 2; j++) {
				System.out.print(cellMatrix[j][i] + "\t\t");
			}
			System.out.println();
		}
	}

	void calculateEnergy(int x, int y) {
		if (!caclculatedCellsAlready) {
			System.out.println("Can't calculate energy before calculating the RGB");
			return;
		}

		Cell current, original = cellMatrix[x][y];
		double value = 0, counter = 0;
		for (int i = x - 1; i <= x + 1; i++) {
			for (int j = y - 1; j <= y + 1; j++) {
				if (i == x && j == y) // avoid central cell
					continue;
				current = cellMatrix[i][j];
				if (current.blue == -1) // avoid empty cells
					continue;
				value += (Math.abs(current.red - original.red) + Math.abs(current.green - original.green)
						+ Math.abs(current.blue - original.blue)) / 3;
				counter++;
			}
		}
		value /= counter;
		original.setEnergy(value);
	}

	void calculateCells(BufferedImage image) {

		// calculating RBG values for each pixel in the input image
		for (int x = 1; x <= cols; x++) {
			for (int y = 1; y <= rows; y++) {
				int currentPixel = image.getRGB(x - 1, y - 1);
				cellMatrix[x][y].setRGB(currentPixel);
			}
		}
		caclculatedCellsAlready = true;
		// calculating the energy of the input image
		for (int x = 1; x <= cols; x++) {
			for (int y = 1; y <= rows; y++) {
				calculateEnergy(x, y);
			}
		}
		if (doEntropy)
			calculateCellEntropy();
	}

	/*----------------------remove/add seam functions -----------------*/
	public void updateEnertgy(int width, int height) {
		cellMatrix = new Cell[width + 2][height + 2];

		this.cols = width;
		this.rows = height;

		for (int x = 0; x < this.cols + 2; x++) {
			for (int y = 0; y < this.rows + 2; y++) {
				cellMatrix[x][y] = new Cell();
			}
		}
	}

	public void colorSeam(Coordinates[] coor, BufferedImage img) {
		for (Coordinates c : coor) {
			img.setRGB(c.col, c.row, 0xFFFF33);
		}
		System.out.println("colored seam");
	}

	public BufferedImage insertSeams(int k, seamCalculate s, BufferedImage img) throws IOException {
		System.out.println("adding " + k + " vertical seam(s)");
		Coordinates[][] seams = s.pick_seams(k,img);

		File inputFile = null;
		BufferedImage tmp = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
		for (int y = 0; y < img.getHeight(); y++) {
			for (int x = 0; x < img.getWidth(); x++) { // smaller after removing the seam
				tmp.setRGB(x, y, img.getRGB(x, y));
			}
		}

		for (int i = 0; i < k; i++) {
			s.coors = seams[i];
			img = addVerticalSeam(s, img);

			inputFile = new File("/Users/othman/Downloads/image_test/" + i + "_color.jpg");
			colorSeam(s.coors, tmp);
			ImageIO.write(tmp, "jpg", inputFile);

		}
		System.out.println("done");
		calculateCells(img);
		return img;
	}

	public BufferedImage insertSeamsNew(int k, seamCalculate s, BufferedImage img) throws IOException {
		System.out.println("adding " + k + " vertical seam(s)");
		s.pick_seams(k,img);
		img = addVerticalSeamNew(k, s, img);
		System.out.println("done");
		calculateCells(img);
		return img;
	}

	public BufferedImage addVerticalSeam(seamCalculate s, BufferedImage img) {
		// after reaching the seam cell, we would simply replicate it and continue as
		// normal the replica would take the extra cell that was created
		BufferedImage bufferedImage = new BufferedImage(img.getWidth() + 1, img.getHeight(),
				BufferedImage.TYPE_INT_RGB);
		Coordinates coor[] = s.coors;
		// coor would be start from bottom to top
		int counter = 0, bias = 0;

		boolean doAverage = false;
		for (int y = img.getHeight() - 1; y >= 0; y--) {
			for (int x = 0; x < img.getWidth(); x++) {
				if (doAverage) {
					int avg = 0;
					if (x == img.getWidth() - 1) {
						avg = averageRGB(img.getRGB(x - 1, y), img.getRGB(x - 2, y));
					} else {
						try {
							avg = averageRGB(img.getRGB(x - 1, y), img.getRGB(x + 1, y));
						} catch (IndexOutOfBoundsException e) {
							System.out.println(e);
						}
					}
					bufferedImage.setRGB(x, y, avg);
					doAverage = false;
					if (x == img.getWidth() - 1) {
						int tmp = bufferedImage.getRGB(x - 1, y);
						bufferedImage.setRGB(x - 1, y, avg);
						bufferedImage.setRGB(x, y, tmp);
					}
				} else {
					bufferedImage.setRGB(x, y, img.getRGB(x - bias, y));
				}

				if (x == coor[counter].col) {
					bias = 1; // bias would take effect at the next cell
					doAverage = true;
				}
			}
			doAverage = false;
			bias = 0;
			counter++;
		}
		updateEnertgy(bufferedImage.getWidth(), bufferedImage.getHeight());
		return bufferedImage;
	}

	public BufferedImage removeVerticalSeam(seamCalculate s, BufferedImage img) {

		// after reaching the seam cell, we would simply remove it and continue as
		// normal

		BufferedImage bufferedImage = new BufferedImage(img.getWidth() - 1, img.getHeight(),
				BufferedImage.TYPE_INT_RGB);
		Coordinates coor[] = s.coors;
		// coor would be from bottom to top
		int counter = 0, bias = 0;
		for (int y = img.getHeight() - 1; y >= 0; y--) {
			for (int x = 0; x < img.getWidth() - 1; x++) { // smaller after removing the seam
				if (x == coor[counter].col) { // ignore seam here
					bias = 1;
				}
				bufferedImage.setRGB(x, y, img.getRGB(x + bias, y));
			}
			bias = 0;
			counter++;
		}
		updateEnertgy(bufferedImage.getWidth(), bufferedImage.getHeight());
		calculateCells(bufferedImage);
		s.updateSeam();
		System.out.println("removed vertical seam");
		return bufferedImage;
	}

	public BufferedImage addVerticalSeamNew(int k, seamCalculate s, BufferedImage img) {

		BufferedImage bufferedImage = new BufferedImage(img.getWidth() + k, img.getHeight(),
				BufferedImage.TYPE_INT_RGB);
		// updateEnertgy(bufferedImage.getWidth(), bufferedImage.getHeight());
		// coor would be start from bottom to top
		int bias = 0;
		for (int y = 0; y < img.getHeight(); y++) {
			for (int x = 0; x < img.getWidth(); x++) {
				int dup = cellMatrix[x + 1][y + 1].duplicate;
				for (int i = 0; i < dup; i++) {
					if (x + bias == img.getWidth() + k - 1)
						continue;
					int px1 = img.getRGB(x, y), px2, avg;
					if (x == (img.getWidth() - 1)) {
						bufferedImage.setRGB(x + bias, y, img.getRGB(x, y));
						bias++;
						continue;
					}
					px2 = img.getRGB(x + 1, y);
					//avg = blend(px1, px2, dup, i);			//check this again for blend
					avg=px1;
					bufferedImage.setRGB(x + bias, y, avg);
					bias++;
				}

				bias--;
			}
			bias = 0;
		}
		updateEnertgy(bufferedImage.getWidth(), bufferedImage.getHeight());
		return bufferedImage;
	}

	private int blend(int px1, int px2, int dup, int i) {
		/* we do the relative average between the two pixels */
		double x1 = ((px1 >> 16) & 0xff) * (dup-i)  ; // red
		double x11 = (((px2 >> 16) & 0xff)) * (i); // red
		double x2 = (((px1 >> 8) & 0xff) * (dup-i)); // red
		double x22 = ((px2 >> 8) & 0xff) * ((i)); // red
		double x3 = ((px1 & 0xff) * (dup-i) ); // red
		double x33 = (px2 & 0xff) * ((i)); // red
		x1=x1/dup+x11/dup;
		x2=x2/dup+x22/dup;
		x3=x3/dup+x33/dup;
		
		return (((int) x1 )<< 16) + (((int) x2) << 8) + (int) x3; 
	}

	int averageRGB(int pix1, int pix2) {
		int x1 = (((pix1 >> 16) & 0xff) + ((pix2 >> 16) & 0xff)) / 2; // red
		int x2 = (((pix1 >> 8) & 0xff) + ((pix2 >> 8) & 0xff)) / 2; // red
		int x3 = ((pix1 & 0xff) + (pix2 & 0xff)) / 2; // red
		return (x1 << 16) + (x2 << 8) + x3;
	}

	public BufferedImage transposeImageRight(BufferedImage img) {
		BufferedImage transposedImage = new BufferedImage(img.getHeight(), img.getWidth(), BufferedImage.TYPE_INT_RGB);

		for (int y = 0; y < img.getWidth(); y++) {
			for (int x = 0; x < img.getHeight(); x++) {
				transposedImage.setRGB(x, y, img.getRGB(y, x));
			}
		}
		return transposedImage;
	}

	/*---------------------- end of remove/add seam functions -----------------*/

	/*---------------------start of Entropy functions-----------------------*/
	void calculateCellEntropy() {// call this function after calculating the energy
		/*
		 * for each cell we will calculate the pValue and then adding the entropy
		 */
		// the pvalue
		System.out.println("calculating entropy");

		for (int x = 1; x <= cols; x++) {
			for (int y = 1; y <= rows; y++) {
				calculate_PValue(x, y);
			}
		}
		// adding the entropy after we computed the pvalue
		for (int x = 1; x <= cols; x++) {
			for (int y = 1; y <= rows; y++) {
				cellMatrix[x][y].energy += entropy(x, y);
			}
		}
		System.out.println("done");
	}

	private double entropy(int col, int row) {
		double entropy = 0.0;
		double p;
		int counter = 0; // ??????????????????? check if must be (normalization)
		for (int x = col - 4; x <= col + 4; x++) {
			for (int y = row - 4; y <= row + 4; y++) {
				if ((x >= 1 && x <= cols) && (y >= 1 && y <= rows)) {
					counter++;// ????????????
					p = cellMatrix[x][y].p;
					entropy += cellMatrix[x][y].p * (Math.log(p));
				}
			}
		}
		entropy = entropy / counter;// ?????????????????????
		return (entropy * -1);
	}

	private void calculate_PValue(int col, int row) {
		double p = 0.0;
		int counter = 0; // ??????????????????? check if must be (normalization)
		for (int x = col - 4; x <= col + 4; x++) {
			for (int y = row - 4; y <= row + 4; y++) {
				if ((x >= 1 && x <= cols) && (y >= 1 && y <= rows)) {
					counter++;// ????????????
					p += cellMatrix[x][y].f;
				}
			}
		}
		p = p / counter;// ????????????????????????
		p = (cellMatrix[col][row].f) / p;
		cellMatrix[col][row].p = p;
	}

}

/*---------------------End of Entropy functions-----------------------*/
class Cell {
	int red = -1, green = -1, blue = -1;
	double energy = -1;
	double M = -1;// this will be used for the dynamic programming computation
	double f = -1;// this is the greyscale for a pixel
	double p = -1; // this is for the entropy
	int duplicate = 1; // how often the seam traverses this cell

	void setRGB(int pixel) {
		red = (pixel >> 16) & 0xff;
		green = (pixel >> 8) & 0xff;
		blue = pixel & 0xff;
		f = ((double) (red + green + blue)) / 3;
	}

	void setEnergy(double energy) {
		this.energy = energy;
	}

	public String toString() {
		return "(" + M + "," + energy + ")";

	}
}
