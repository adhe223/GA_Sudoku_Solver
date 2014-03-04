
public class SudokuDriver {
	private static final int POP_SIZE = 3000;
	private static final String IN_BOARD = "790000300000006900800030076000005002005418700400700000610090008002300000009000054";
	private static final String SOLVED_TEST = "796854321243176985851239476137965842925418763468723519614597238582341697379682154";
	
	private static Population myPop;
	
	public SudokuDriver() {
		//Manually input the board here. An 81 character string of ints 1-9 where 0 are the blank spots
		myPop = new Population(POP_SIZE, IN_BOARD);
	}
	
	public static void main(String[] args) {
		SudokuDriver driver = new SudokuDriver();
		int round = 1;
		Board solution = null;
		
		while (solution == null) {
			System.out.println("Beginning Round " + round + " with a best solution of fitness " + myPop.getBestSolution().getFitness());
			System.out.println(myPop.getBestSolution());
			
			myPop.selectBreedMutateSort();
			solution = myPop.isSolved();
			if (solution != null) {
				System.out.println("Congratulations, the genetic solver has found a solution!");
				System.out.println(myPop.getBestSolution());
			}
		}
	}
}
