import java.awt.image.BufferedImage;
import java.lang.Math;

public class EnergyFunctions {

	Cell[][] cellMatrix;
	int cols, rows;
	boolean caclculatedCellsAlready = false, doEntropy = false, doBlend = true;

	EnergyFunctions(int width, int height) {
		// constructor for the cell matrix (including external edges)
		cellMatrix = new Cell[width + 2][height + 2];
		this.cols = width;
		this.rows = height;
		for (int x = 0; x < this.cols + 2; x++) {
			for (int y = 0; y < this.rows + 2; y++) {
				cellMatrix[x][y] = new Cell();
			}
		}
	}

	void calculateEnergy(int x, int y) {
		// by giving an index, calculate it's energy
		if (!caclculatedCellsAlready) {
			System.out.println("Can't calculate energy before calculating the RGB");
			return;
		}

		Cell current, original = cellMatrix[x][y];
		double value = 0, counter = 0;
		for (int i = x - 1; i <= x + 1; i++) {
			for (int j = y - 1; j <= y + 1; j++) {
				if (i == x && j == y) // avoid central cell (not included in the neighborhood)
					continue;
				current = cellMatrix[i][j];
				if (current.blue == -1) // avoid empty cells (edge cells)
					continue;
				value += (Math.abs(current.red - original.red) + Math.abs(current.green - original.green)
						+ Math.abs(current.blue - original.blue)) / 3;
				counter++;
			}
		}
		value /= counter;
		original.setEnergy(value);
	}

	void calculateCells(BufferedImage image) { // by calling this method, we'll update the energy values
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
			// calculate the energy using the entropy functions
			calculateCellEntropy();
	}

	/*----------------------start of remove/add seam functions -----------------*/
	public void updateEnergyMatrix(int width, int height) { // used after removing or inserting seams
		// change the matrix's dimensions using the given parameters
		cellMatrix = new Cell[width + 2][height + 2];
		this.cols = width;
		this.rows = height;
		for (int x = 0; x < this.cols + 2; x++) {
			for (int y = 0; y < this.rows + 2; y++) {
				cellMatrix[x][y] = new Cell();
			}
		}
//		System.out.println("updated matrix");
	}

	public BufferedImage removeVerticalSeam(seamCalculate s, BufferedImage img) { // calling this method, remove the
																					// minimal seam
		// giving a seam object, we'll calculate the optimal seam and remove it
		BufferedImage bufferedImage = new BufferedImage(img.getWidth() - 1, img.getHeight(),
				BufferedImage.TYPE_INT_RGB); // create a new image with a lower dimension

		s.updateSeam(img);
//		System.out.println(Arrays.deepToString(s.coors));
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
		updateEnergyMatrix(bufferedImage.getWidth(), bufferedImage.getHeight());
		calculateCells(bufferedImage);

		return bufferedImage;
	}

	public BufferedImage removeHorizontalSeam(seamCalculate s, BufferedImage img) {

		img = transposeImageRight(s, img);
		img = removeVerticalSeam(s, img);
		img = transposeImageRight(s, img);
		return img;
	}

	public BufferedImage addKVerticalSeams(int k, seamCalculate s, BufferedImage img) {// calling this method, adds K
																						// seams to the input image
		BufferedImage bufferedImage = new BufferedImage(img.getWidth() + k, img.getHeight(),
				BufferedImage.TYPE_INT_RGB);

		s.pick_seams(k, img); // get K minimal seams
		int bias = 0;
		for (int y = 0; y < img.getHeight(); y++) {
			for (int x = 0; x < img.getWidth(); x++) {
				int dup = cellMatrix[x + 1][y + 1].duplicate;
				for (int i = 0; i < dup; i++) {
					// using the duplicate attribute, we'll create another loop that will repeat the
					// same pixel and at the end blend it
					if (x + bias == img.getWidth() + k - 1)
						continue;
					int px1 = img.getRGB(x, y), px2, avg;
					if (x == (img.getWidth() - 1)) {
						bufferedImage.setRGB(x + bias, y, img.getRGB(x, y));
						bias++;
						continue;
					}
					if (doBlend) {
						px2 = img.getRGB(x + 1, y);
						avg = blend(px1, px2, dup, i); // check this again for blend

					} else
						avg = px1;
					bufferedImage.setRGB(x + bias, y, avg);
					bias++;
				}
				bias--;
			}
			bias = 0;
		}
		updateEnergyMatrix(bufferedImage.getWidth(), bufferedImage.getHeight());
		calculateCells(bufferedImage);
		return bufferedImage;
	}

	public BufferedImage addKHorizontalSeams(int k, seamCalculate s, BufferedImage img) {

		System.out.println(
				"getHeight=" + img.getHeight() + ", rows=" + rows + ", getWidth=" + img.getWidth() + ", cols=" + cols);
		System.out.println(s.shape);

		img = transposeImageRight(s, img);
		System.out.println(
				"getHeight=" + img.getHeight() + ", rows=" + rows + ", getWidth=" + img.getWidth() + ", cols=" + cols);
		img = addKVerticalSeams(k, s, img);
		img = transposeImageRight(s, img);
		return img;
	}

	private int blend(int px1, int px2, int dup, int i) {
		// spread the relative average between the two pixels
		int r = (int) Math.pow(2, dup - i);
		if (i == 0) {
			return px1;
		}
		double x1 = ((px1 >> 16) & 0xff) * (r - 1); // red
		double x11 = (((px2 >> 16) & 0xff)); // red
		double x2 = (((px1 >> 8) & 0xff) * ((r - 1))); // red
		double x22 = ((px2 >> 8) & 0xff); // red
		double x3 = ((px1 & 0xff) * ((r - 1))); // red
		double x33 = (px2 & 0xff); // red
		x1 = x1 / r + x11 / r;
		x2 = x2 / r + x22 / r;
		x3 = x3 / r + x33 / r;
		return (((int) x1) << 16) + (((int) x2) << 8) + (int) x3;

	}

	public BufferedImage transposeImageRight(seamCalculate s, BufferedImage img) { // transpose a given image
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
		// for each cell we will calculate the pValue and then add the entropy
		System.out.println("calculating entropy");

		// the Pvalue
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
		int counter = 0;
		for (int x = col - 4; x <= col + 4; x++) {
			for (int y = row - 4; y <= row + 4; y++) {
				if ((x >= 1 && x <= cols) && (y >= 1 && y <= rows)) {
					counter++;
					p = cellMatrix[x][y].p;
					entropy += cellMatrix[x][y].p * (Math.log(p));
				}
			}
		}
		entropy = entropy / counter;
		return (entropy * -1);
	}

	private void calculate_PValue(int col, int row) {
		double p = 0.0;
		int counter = 0;
		for (int x = col - 4; x <= col + 4; x++) {
			for (int y = row - 4; y <= row + 4; y++) {
				if ((x >= 1 && x <= cols) && (y >= 1 && y <= rows)) {
					counter++;
					p += cellMatrix[x][y].f;
				}
			}
		}
		p = p / counter;
		p = (cellMatrix[col][row].f) / p;
		cellMatrix[col][row].p = p;
	}
	/*---------------------End of Entropy functions-----------------------*/

}

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
