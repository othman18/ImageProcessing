
/*aubaida's notes 31/3/19 at 2:58 PM
 * 1)check if the access to the (cell[i+1][j+1]; from i=0 ;to i=(col-1)) is okk !! 
 * 2)the straight seam i computed from the first row
 * 3)I added the @M@ local variable to the (class cell in energyFunctions)
 * 4)change the enum's variables names... Ask Othman for that 
 */
enum seamShape // there is two ways which we want to compare
{
	straight, general;
}

public class seamCalculate {
	Coordinates[] coors; // the seam coordinates
	seamShape shape; // which way we want to calculate
	EnergyFunctions energy;

	seamCalculate(EnergyFunctions energy, seamShape shape) {
		this.shape = shape;
		this.coors = new Coordinates[energy.rows];
		this.energy = energy;
	}

	void updateSeam() {
		if (this.shape == seamShape.straight)
			straightCalculation();
		else {
			generalCalculation();
		}
	}

	private void generalCalculation() {

		int cols = this.energy.cols;
		int rows = this.energy.rows;
		Cell currCell = null, upLeftCell = null, upCell = null, upRightCell = null;

		for (int i = 0; i < cols; i++) {// the first row "M"'s value will be
										// initialize to its energy value
			currCell = (energy.cellMatrix)[1][i + 1];
			currCell.M = currCell.energy;
		}
		// the dynamical programming
		// **currCell.M=currCell.energy+min(upCell.M,upRightCell.M,upLeftCell.M)**
		for (int i = 0; i < cols; i++) {// for each column
			for (int j = 1; j < rows; j++) {// for each row but the first
				currCell = (energy.cellMatrix)[j + 1][i + 1];
				upLeftCell = (energy.cellMatrix)[j][i];
				upCell = (energy.cellMatrix)[j][i + 1];
				upRightCell = (energy.cellMatrix)[j][i + 2];
				currCell.M = currCell.energy;

				if (i == 0) {// if we are in the first column as we don't have
								// the upper left pixel
					currCell.M += min(upCell.M, upRightCell.M);
				}

				else if (i == (cols - 1)) {// if we are in the last column as we
											// don't have the upper right pixel
					currCell.M += min(upCell.M, upLeftCell.M);
				} else {
					currCell.M += min(upCell.M, min(upRightCell.M, upLeftCell.M));
				}
			}
		}
		// Initialize variables to help us with the back tracing
		int row_index = rows - 1, min_index = 0, i, j, coors_index = 0;
		Cell minCell = (energy.cellMatrix)[row_index + 1][1];
		for (i = 0; i < cols; i++) {
			currCell = (energy.cellMatrix)[row_index + 1][i + 1];
			if (currCell.M < minCell.M) {
				minCell = currCell;
				min_index = i;
			}
		}
		coors[coors_index].col = min_index;
		coors[coors_index].row = rows - 1;
		coors_index++;
		row_index--;
		while (row_index >= 0) {
			i = coors[coors_index - 1].col;
			j = coors[coors_index - 1].row;
			currCell = (energy.cellMatrix)[j + 1][i + 1];
			upLeftCell = (energy.cellMatrix)[j][i];
			upCell = (energy.cellMatrix)[j][i + 1];
			upRightCell = (energy.cellMatrix)[j][i + 2];

			coors[coors_index].row = row_index;
			if (i == 0) {// first col
				if (min(upCell.M, upRightCell.M) == upCell.M) {
					coors[coors_index].col = 0;
				} else {
					coors[coors_index].col = 1;
				}
			} else if (i == cols - 1) {// last col
				if (min(upCell.M, upLeftCell.M) == upCell.M) {
					coors[coors_index].col = cols - 1;
				} else {
					coors[coors_index].col = cols - 2;
				}
			} else {
				if (min(upCell.M, min(upLeftCell.M, upRightCell.M)) == upCell.M) {
					coors[coors_index].col = i;
				} else if (min(upRightCell.M, upLeftCell.M) == upRightCell.M) {
					coors[coors_index].col = i + 1;
				} else {
					coors[coors_index].col = i - 1;
				}
			}
			coors_index++;
			row_index--;
		}
	}

	private double min(double m1, double m2) {
		if (m1 > m2)
			return m2;
		return m1;
	}

	private void straightCalculation() {// **i ignored that we must start with
										// the second row**

		int cols = this.energy.cols;
		int rows = this.energy.rows;
		Cell cell = null;
		int index = 0;
		double[] arrayOfSums = new double[this.energy.cols];// this array will
															// contain the
															// energy sum for
															// each col
		// computing for each column what is the energy sum
		for (int i = 0; i < cols; i++) {// for each column
			for (int j = 0; j < rows; j++) {// for each row
				cell = (energy.cellMatrix)[j + 1][i + 1];
				arrayOfSums[i] += cell.energy;
			}
		}
		for (int i = 1; i < cols; i++) {// finding the minimum energy seam
			// ????? check the indexes
			if (arrayOfSums[i] < arrayOfSums[index])
				index = i;
		}
		for (int i = 0; i < rows; i++) {
			coors[i].row = i;
			coors[i].col = index;
		}
	}
}

class Coordinates {
	int row = -1;
	int col = -1;

	Coordinates(int m, int n) {
		this.row = m;
		this.col = n;
	}
}