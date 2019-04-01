
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
	EnergyFunctions energyObj;

	seamCalculate(EnergyFunctions energy, seamShape shape) {
		this.shape = shape;
		this.coors = new Coordinates[energy.rows];
		for (int i = 0; i < energy.rows; i++) {
			coors[i] = new Coordinates(-1, -1);
		}
		this.energyObj = energy;
	}

	void updateSeam() {
		if (this.shape == seamShape.straight)
			straightCalculation();
		else {
			generalCalculation();
		}
	}

	private void generalCalculation() {

		int rows = this.energyObj.rows;
		int cols = this.energyObj.cols;
		Cell currCell = null, upLeftCell = null, upCell = null, upRightCell = null;

		for (int x = 0; x < cols; x++) {// the first row "M"'s value will be
										// initialize to its energy value
			currCell = energyObj.cellMatrix[x + 1][1];
			currCell.M = currCell.energy;
		}
		// the dynamical programming
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
		int row_index = rows - 1, min_index = 0, x, y, coors_index = 0;
		Cell minCell = energyObj.cellMatrix[1][row_index + 1];
		System.out.println(minCell.M);
		for (x = 0; x < cols; x++) {
			currCell = energyObj.cellMatrix[x + 1][row_index + 1];
			System.out.println(currCell.M);
			if (currCell.M < minCell.M) {
				minCell = currCell;
				min_index = x;
			}
		} // found the sim's start (from the bottom)
		coors[coors_index].col = min_index;
		coors[coors_index].row = rows - 1;
		//System.out.println("current="+currCell+", x="+min_index+", y="+row_index);
		coors_index++;
		row_index--;

		while (row_index >= 0) {
			x = coors[coors_index - 1].col;
			y = coors[coors_index - 1].row;
			currCell = energyObj.cellMatrix[x + 1][y + 1];
			upLeftCell = energyObj.cellMatrix[x][y];
			upCell = energyObj.cellMatrix[x + 1][y];
			upRightCell = energyObj.cellMatrix[x + 2][y];
			System.out.println("current="+currCell+", x="+x+", y="+y);
			
			coors[coors_index].row = row_index;
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
		
	}

	private double min(double m1, double m2) {
		if (m1 > m2)
			return m2;
		return m1;
	}

	private void straightCalculation() {// **i ignored that we must start with
										// the second row**

		int rows = this.energyObj.rows;
		int cols = this.energyObj.cols;
		Cell cell = null;
		int index = 0;
		double[] arrayOfSums = new double[this.energyObj.cols];// this array will
																// contain the
																// energy sum for
																// each col
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
}

class Coordinates {
	int row = -1;
	int col = -1;

	Coordinates(int m, int n) {
		this.row = n;
		this.col = m;
	}

	public String toString() {
		return "[x=" + col + " ,y=" + row+"]";

	}
}