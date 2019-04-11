import java.awt.image.BufferedImage;
import java.util.Arrays;

enum seamShape {
	straight, generalBackward, generalForward;
}

public class seamCalculate {
	Coordinates[] coors; // the seam coordinates
	seamShape shape; // which way we want to calculate
	EnergyFunctions energyObj;

	public seamCalculate(EnergyFunctions energy, seamShape shape) {
		this.shape = shape;
		this.coors = new Coordinates[energy.rows];
		for (int i = 0; i < energy.rows; i++) {
			coors[i] = new Coordinates(-1, -1);
		}
		this.energyObj = energy;
	}

	void updateSeam(BufferedImage img) { // this method will calculate the seam's energy
		if (this.shape == seamShape.straight)
			straightCalculation();
		else if (this.shape == seamShape.generalForward) {
			generalForwardCalculation(img);
		} else {
			generalBackwardCalculation();
		}
	}

	public void generalForwardCalculation(BufferedImage img) {

		int rows = this.energyObj.rows;
		int cols = this.energyObj.cols;
		double CL = 0, CR = 0, CU = 0;
		Cell currCell = null, upLeftCell = null, upCell = null, upRightCell = null;

		for (int x = 0; x < cols; x++) {// the first row "M"'s value will be
										// initialize to its energy value
			currCell = energyObj.cellMatrix[x + 1][1];
			currCell.M = currCell.energy;
		}
		// the dynamic programming

		for (int y = 1; y < rows; y++) {// for each row but the first
			for (int x = 0; x < cols; x++) {// for each column
				currCell = energyObj.cellMatrix[x + 1][y + 1];
				upLeftCell = energyObj.cellMatrix[x][y];
				upCell = energyObj.cellMatrix[x + 1][y];
				upRightCell = energyObj.cellMatrix[x + 2][y];
				currCell.M = currCell.energy;
				// System.out.println("x=" + x + ", y=" + y);
				if (x == 0 || x == (cols - 1)) {
					CU = 0;
					if (x == 0) {
						CL = 0;
						CR = Math.abs(img.getRGB(x, y - 1) - img.getRGB(x + 1, y));
					}
					if (x == (cols - 1)) {
						CR = 0;
						CL = Math.abs(img.getRGB(x, y - 1) - img.getRGB(x - 1, y));
					}
				} else {
					CU = Math.abs(img.getRGB(x + 1, y) - img.getRGB(x - 1, y));
					CL = CU + Math.abs(img.getRGB(x, y - 1) - img.getRGB(x - 1, y));
					CR = CU + Math.abs(img.getRGB(x, y - 1) - img.getRGB(x + 1, y));

				}

				if (x == 0) {// if we are in the first column as we don't have
								// the upper left pixel
					currCell.M += min(upCell.M + CU, upRightCell.M + CR);
				}

				else if (x == (cols - 1)) {// if we are in the last column as we
											// don't have the upper right pixel
					currCell.M += min(upCell.M + CU, upLeftCell.M + CL);
				} else {
					currCell.M += min(upCell.M + CU, min(upRightCell.M + CR, upLeftCell.M + CL));
				}
			}
		}
		// normalize the matrix

		// Initialize variables to help us with the back tracing
		int row_index = rows - 1, min_index = 0, x, coors_index = 0;
		Cell minCell = energyObj.cellMatrix[1][row_index + 1];
		for (x = 0; x < cols; x++) {
			currCell = energyObj.cellMatrix[x + 1][row_index + 1];
			if (currCell.M < minCell.M) {
				minCell = currCell;
				min_index = x;
			}
		} // found the sim's start (from the bottom)
		coors[coors_index].col = min_index;
		coors[coors_index].row = rows - 1;
		coors_index++;
		row_index--;
		updateCoors(coors);
	}

	private void generalBackwardCalculation() {
		int rows = this.energyObj.rows;
		int cols = this.energyObj.cols;
		Cell currCell = null, upLeftCell = null, upCell = null, upRightCell = null;

		for (int x = 0; x < cols; x++) {// the first row "M"'s value will be initialize to its energy value
			currCell = energyObj.cellMatrix[x + 1][1];
			currCell.M = currCell.energy;
		}
		// the dynamic programming
		// **currCell.M=currCell.energy+min(upCell.M,upRightCell.M,upLeftCell.M)**
		for (int y = 1; y < rows; y++) {// for each row but the first
			for (int x = 0; x < cols; x++) {// for each column
				currCell = energyObj.cellMatrix[x + 1][y + 1];
				upLeftCell = energyObj.cellMatrix[x][y];
				upCell = energyObj.cellMatrix[x + 1][y];
				upRightCell = energyObj.cellMatrix[x + 2][y];
				currCell.M = currCell.energy;

				if (x == 0) {// if we are in the first column as we don't have
								// the upper left pixel
					currCell.M += min(upCell.M, upRightCell.M);
				}

				else if (x == (cols - 1)) {// if we are in the last column as we
											// don't have the upper right pixel
					currCell.M += min(upCell.M, upLeftCell.M);
				} else {
					currCell.M += min(upCell.M, min(upRightCell.M, upLeftCell.M));
				}
			}
		}
		// Initialize variables to help us with the back tracing
		int row_index = rows - 1, min_index = 0, x, coors_index = 0;
		Cell minCell = energyObj.cellMatrix[1][row_index + 1];
		for (x = 0; x < cols; x++) {
			currCell = energyObj.cellMatrix[x + 1][row_index + 1];
			if (currCell.M < minCell.M) {
				minCell = currCell;
				min_index = x;
			}
		} // found the sim's start (from the bottom)
		coors[coors_index].col = min_index;
		coors[coors_index].row = rows - 1;
		coors_index++;
		row_index--;
		updateCoors(coors);
	}

	private double min(double m1, double m2) {
		if (m1 > m2)
			return m2;
		return m1;
	}

	public void updateCoors(Coordinates[] coors) {
		int x, y, row_index = energyObj.rows - 2, coors_index = 1;
		Cell upLeftCell = null, upCell = null, upRightCell = null;
		int cols = energyObj.cols;
		while (row_index >= 0) {
			x = coors[coors_index - 1].col;
			y = coors[coors_index - 1].row;
			if (this.shape == seamShape.generalBackward) {
				energyObj.cellMatrix[x + 1][y + 1].M += 10;
			} else {
				energyObj.cellMatrix[x][y + 1].M += 10;
				energyObj.cellMatrix[x + 1][y + 1].M += 10;
			}
			energyObj.cellMatrix[x + 1][y + 1].duplicate++;
			upLeftCell = energyObj.cellMatrix[x][y];
			upCell = energyObj.cellMatrix[x + 1][y];
			upRightCell = energyObj.cellMatrix[x + 2][y];
			try {
				coors[coors_index].row = row_index;
			} catch (ArrayIndexOutOfBoundsException e) {
				System.out.println("error");
			}
			if (x == 0) {// first col
				if (min(upCell.M, upRightCell.M) == upCell.M) {
					coors[coors_index].col = 0;
				} else {
					coors[coors_index].col = 1;
				}
			} else if (x == cols - 1) {// last col

				if (min(upCell.M, upLeftCell.M) == upCell.M) {
					coors[coors_index].col = cols - 1;
				} else {
					coors[coors_index].col = cols - 2;
				}
			} else {
				if (min(upCell.M, min(upLeftCell.M, upRightCell.M)) == upCell.M) {
					coors[coors_index].col = x;
				} else if (min(upRightCell.M, upLeftCell.M) == upRightCell.M) {
					coors[coors_index].col = x + 1;
				} else {
					coors[coors_index].col = x - 1;
				}
			}

			coors_index++;
			row_index--;
		}

		energyObj.cellMatrix[coors[coors_index - 1].col + 1][1].duplicate++;

	}

	private void straightCalculation() {
		int rows = this.energyObj.rows;
		int cols = this.energyObj.cols;
		Cell cell = null;
		int index = 0;
		// this array will contain the energy sum for each col
		double[] arrayOfSums = new double[this.energyObj.cols];
		// computing for each column what is the energy sum
		for (int x = 0; x < cols; x++) {// for each column
			for (int y = 0; y < rows; y++) {// for each row
				cell = energyObj.cellMatrix[x + 1][y + 1];
				arrayOfSums[x] += cell.energy;
			}
		}
		for (int x = 1; x < cols; x++) {// finding the minimum energy seam
			if (arrayOfSums[x] < arrayOfSums[index])
				index = x;
		}
		for (int y = 0; y < rows; y++) {
			coors[y].row = y;
			coors[y].col = index;
		}
	}

	public Coordinates[][] pick_seams(int k, BufferedImage img) {
		Coordinates[][] seams = new Coordinates[k][energyObj.rows];
		Coordinates[] last_row_values = new Coordinates[energyObj.cols];

		this.updateSeam(img);

		for (int i = 0; i < energyObj.cols; i++) {// init the coordinates
			last_row_values[i] = new Coordinates(i, energyObj.rows - 1);
			last_row_values[i].M = energyObj.cellMatrix[i + 1][energyObj.rows].M;
		}
		Arrays.sort(last_row_values);

		for (int i = 0; i < k; i++) {
			for (int j = 0; j < energyObj.rows; j++) {
				seams[i][j] = new Coordinates(0, 0);
			}
			seams[i][0] = last_row_values[i];
			updateCoors(seams[i]);
		}
		return seams;
	}
}

class Coordinates implements Comparable<Coordinates> {
	int row = -1;
	int col = -1;
	double M = -1;

	Coordinates(int m, int n) {
		this.row = n;
		this.col = m;
	}

	public String toString() {
		return "[x=" + col + " ,y=" + row + "]";

	}

	@Override
	public int compareTo(Coordinates O) {
		return Double.compare(this.M, O.M);
	}

}