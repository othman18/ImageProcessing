import java.awt.image.BufferedImage;

public class EnergyFunctions {

	Cell[][] cellMatrix;
	int rows, cols;
	boolean caclculatedCellsAlready = false;

	EnergyFunctions(int rows, int cols) {
		cellMatrix = new Cell[rows + 1][cols + 1];

		this.rows = rows;
		this.cols = cols;

		for (int x = 0; x < this.rows + 1; x++) {
			for (int y = 0; y < this.cols + 1; y++) {
				cellMatrix[x][y] = new Cell();
			}
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
		int counter = 0;
		for (int x = 1; x < rows; x++) {
			for (int y = 1; y < cols; y++) {
				int currentPixel = image.getRGB(x - 1, y - 1);
				cellMatrix[x][y].setRGB(currentPixel);
				counter++;
			}
		}
		caclculatedCellsAlready = true;
		System.out.println("calculated RGB");

		// calculating the energy of the input image
		for (int x = 1; x < rows; x++) {
			for (int y = 1; y < cols; y++) {
				calculateEnergy(x, y);
			}
		}

	}
}

class Cell {
	int red = -1, green = -1, blue = -1;
	double energy = -1;

	void setRGB(int pixel) {
		red = (pixel >> 16) & 0xff;
		green = (pixel >> 8) & 0xff;
		blue = pixel & 0xff;
	}

	void setEnergy(double energy) {
		this.energy = energy;
	}

	public String toString() {
		return "energy = " + energy;

	}
}
