//Alex Henry
//Midterm

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class SudokuDriver {
	private static final int POP_SIZE = 3000;
	private static final String IN_BOARD = "790000300000006900800030076000005002005418700400700000610090008002300000009000054";
	private static final String EASY6 = "000090004410003000807604210001007002060040090200500700048306907000400021600010000";
	private static final String STANDARD6 = "070000018490600005000007400000301609060000040205906000004700000900004082680000090";
	private static final String EXTREME6 = "000002700000015032400080160020004000750020049000500080043090006590240000008100000";
	private static final String SOLVED_TEST = "796854321243176985851239476137965842925418763468723519614597238582341697379682154";
	private static final String ONE_OFF_TEST = "796854321243176985851239476137965842925418763468723519614597238582341657379682154";
	private static final int NUM_TEST = 10;	//Number of tests for data collection
	
	
	private static Population myPop;
	
	public SudokuDriver() {
		//Manually input the board here. An 81 character string of ints 1-9 where 0 are the blank spots
		myPop = new Population(POP_SIZE, STANDARD6);
	}
	
	public static void main(String[] args) {
		SudokuDriver driver = new SudokuDriver();
		
		//for (int mutation = 25; mutation <= 25; mutation+=5) {		
			try {
				File file = new File("Results.txt");
				
				// if file doesnt exists, then create it
				if (!file.exists()) {
					file.createNewFile();
				}
				
				FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
				BufferedWriter bw = new BufferedWriter(fw);
				StringBuilder output = new StringBuilder();
				output.append("\nSelectivity: " + myPop.getSelectivity() + "   Savior: " + myPop.getSavior() + "   Mutate: " + myPop.getMutate() + "\n");
				
				bw.write(output.toString());
				bw.flush();
				bw.close();
			} catch (Exception e) {}
			
			
			//Run number of tests
			for (int i = 0; i < NUM_TEST; i++) {
				driver = new SudokuDriver();
				
				final long startTime = System.nanoTime();
				
				int round = 1;
				Board solution = null;		
				
				while (solution == null) {
					//Debug Statements
					
					System.out.println("Beginning Round " + round + " with a best solution of fitness " + myPop.getBestSolution().getFitness());
					System.out.println(myPop.getBestSolution());
					
					
					myPop.selectBreedMutateSort();
					solution = myPop.isSolved();
					if (solution != null) {
						long endTime = System.nanoTime();
						long timeElapsed = endTime - startTime;
						double timeInSeconds = (double)timeElapsed / 1000000000L;
						
						
						System.out.println("Congratulations, the genetic solver has found a solution in round " + round + "!");
						System.out.print("The Solver got stuck: " + myPop.getStuckCount() + " times and took ");
						System.out.printf("%.2f", timeInSeconds);
						System.out.print("s to solve the puzzle.\n");
						System.out.println(myPop.getBestSolution());
						
						
						resultsToFile(round, timeInSeconds, myPop.getStuckCount());
					}
					
					myPop.reset();
					
					round++;
					
					if (round == 12000) {
						solution = new Board(IN_BOARD);
						try {
							File file = new File("Results.txt");
							
							// if file doesnt exists, then create it
							if (!file.exists()) {
								file.createNewFile();
							}
							
							FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
							BufferedWriter bw = new BufferedWriter(fw);
							StringBuilder output = new StringBuilder();
							output.append("\nCould not solve.\n");
							
							bw.write(output.toString());
							bw.flush();
							bw.close();
						} catch (Exception e) {}
					}
				}
			}
		//}
		
		System.out.println("Testing Finished");
	}
	
	public static void resultsToFile(int round, double timeInSeconds, int stuckCount) {
		try {
			File file = new File("Results.txt");
			
			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}
			
			FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
			BufferedWriter bw = new BufferedWriter(fw);
			StringBuilder output = new StringBuilder();
			
			output.append("Round: " + round + "   ");
			output.append("Stuck: " + myPop.getStuckCount() + "   ");
			output.append("Time Elapsed: " + timeInSeconds + "\n");
			
			bw.write(output.toString());
			bw.flush();
			bw.close();
		} catch(IOException e) {
			
		}
	}
}
