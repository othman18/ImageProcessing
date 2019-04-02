import java.awt.image.BufferedImage;

public class EnergyFunctions {

	Cell[][] cellMatrix;
	int cols, rows;
	boolean caclculatedCellsAlready = false;

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
		for (int i = 0; i < rows+2; i++) {
			for (int j = 0; j < cols+2; j++) {
				System.out.print(cellMatrix[j][i]+"\t\t");
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
				// System.out.println("i="+i+" ,j="+j);
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
		System.out.println("calculated RGB");

		// calculating the energy of the input image
		for (int x = 1; x <= cols; x++) {
			for (int y = 1; y <= rows; y++) {
				calculateEnergy(x, y);
			}
		}

	}
}

class Cell {
	int red = -1, green = -1, blue = -1;
	double energy = -1;
	double M = -1;// this will be used for the dynamic programming computation

	void setRGB(int pixel) {
		red = (pixel >> 16) & 0xff;
		green = (pixel >> 8) & 0xff;
		blue = pixel & 0xff;
	}

	void setEnergy(double energy) {
		this.energy = energy;
	}

	public String toString() {
		return "(" + M+","+energy+")";

	}
}
