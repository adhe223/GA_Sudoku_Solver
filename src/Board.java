//Alex Henry
//Midterm

import java.util.Vector;

public class Board implements Comparable<Board>{
	Vector<Row> vRows = new Vector<Row>();
	private int fitnessScore = -1;
	
	//Takes in a string with 81 characters that are the values of the board. 0 for blank places.
	Board(String inputBoard) {
		//Initialize the rows and build the board
		int index = 0;
		for (int row = 0; row < 9; row++) {
			int[] rowInts = new int[9];
			for (int col = 0; col < 9; col++) {
				rowInts[col] = inputBoard.charAt(index) - '0';	//Convert to int
				index++;
			}
			vRows.add(new Row(rowInts, row));
		}
	}
	
	/*Assign fitness function. We know the rows do not violate the laws of sudoku because we created them atomically.
	 * Iterate over each column and check if there are repeated digits. For each repeat, increment the fitness function (lower is better).
	 * Also iterate through each 3x3 block and for each repeat digits increment the fitness.*/
	public void setFitness() {
		int tempFitness = 0;
		
		
		//Keep a vector of ints and add numbers to it as we go, if we run into a repeat we increment fitness
		Vector<Integer> vInts = new Vector<Integer>();
		
		
		for (int col = 0; col < 9; col++) {
			for (int row = 0; row < 9; row++) {
				if (vInts.contains(vRows.get(row).getIntAtIndex(col))) {
					//Already in vector, repeat
					tempFitness++;
				} else {
					vInts.add(vRows.get(row).getIntAtIndex(col));
				}
			}
			
			vInts = new Vector<Integer>();
		}
		
		
		
		//That handles the columns, now iterate over the 3x3 blocks
		vInts = new Vector<Integer>();
		int rowIndex = 0;
		int colIndex = 0;
		for (int block = 0; block < 9; block++) {			
			for (int row = rowIndex; row < rowIndex + 3; row++) {
				for (int col = colIndex; col < colIndex + 3; col++) {
					if (vInts.contains(vRows.get(row).getIntAtIndex(col))) {
						//Already in vector
						tempFitness++;
					} else {
						vInts.add(vRows.get(row).getIntAtIndex(col));
					}
				}
			}
			
			colIndex = colIndex + 3;
			
			if ((block + 1) % 3 == 0) {
				rowIndex = rowIndex + 3;
				colIndex = 0;
			}
			
			vInts = new Vector<Integer>();
		}
		
		
		fitnessScore = tempFitness;
	}
	
	public int getFitness() {
		return fitnessScore;
	}
	
	//Go through each row and call its mutate function. Will mutate based on the mutate prob (0 to 100)
	public void mutate(int prob, String origBoard) {
		for (Row r : vRows) {
			r.mutate(prob, origBoard);
		}
	}
	
	public int compareTo(Board x) {
		if (this.getFitness() > x.getFitness()) {
			return 1;
		}
		if (this.getFitness() < x.getFitness()) {
			return -1;
		}
		return 0;
	}
	
	public String getBoardStringData() {
		StringBuilder toBuild = new StringBuilder();
		for (Row r : vRows) {
			toBuild.append(r.getRowDataAsString());
		}
		
		return toBuild.toString();
	}
	
	public String getRowStringData(int row) {
		return vRows.get(row).getRowDataAsString();
	}
	
	public Boolean isSolved() {
		if (fitnessScore == 0) {
			return true;
		}
		return false;
	}
	
	@Override public String toString() {
		StringBuilder toReturn = new StringBuilder();
		toReturn.append("Sudoku Board:\n");
		for (int i = 0; i < vRows.size(); i++) {
			toReturn.append(vRows.get(i).toString());
			toReturn.append("\n");
			
			if ((i + 1) % 3 == 0 && i != 8) {	//Add horiz lines
				toReturn.append("-----------------------\n");
			}
		}
		
		return toReturn.toString();
	}
	
}
