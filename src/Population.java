import java.util.ArrayList;
import java.util.Collections;
import java.util.Vector;
import java.util.Random;

public class Population {
	private static int SELECTIVITY = 15;	//Int from 0 to 100 that decides what percentage of the population to keep each round
	private static int SAVIOR_PROB = 2;	//Probability (0 to 99) that a less fit solution will be spared in the pruning
	private static int MUTATE_PROB = 5;	//Int from 0 to 100 that represents the probability to mutate a row
	private int stuckCount = 0;
	private static int POP_SIZE;
	private ArrayList<Integer> pastFitness = new ArrayList<Integer>();
	private static final int resetSize = 10;
	
	private static Vector<Board> vPopBoards;
	private static String originalBoard;
	private static Random roller = new Random();
	
	public Population(int popSize, String origBoard) {
		originalBoard = origBoard;
		POP_SIZE = popSize;
		vPopBoards = new Vector<Board>(); 
		
		//Fill the population with new boards
		for (int i = 0; i < popSize; i++) {
			vPopBoards.add(new Board(originalBoard));
		}
		
		//Assign fitness score to population
		setFitnessScores();
	}
	
	//Iterates through the population and sets fitness scores
	public void setFitnessScores() {
		for (Board b : vPopBoards) {
			b.setFitness();
		}
		
		//Now sort the vector. Board implements the Comparable class, so this is trivial
		Collections.sort(vPopBoards);
	}
	
	//Prune the population based on fitness scores and the tweaks set in this classes static variable. Make sure pop vector is sorted before this method is called.
	public void selectBreedMutateSort() {
		int remIndex = vPopBoards.size() - 1;
		double prob = SELECTIVITY * (double).01;
		while (vPopBoards.size() > prob * POP_SIZE && remIndex >= 0) {
			//Keep some less fit options for genetic diversity
			if (roller.nextInt(100) > SAVIOR_PROB) {
				//Remove the board from the population
				vPopBoards.remove(remIndex);
			}
			
			remIndex--;
		}
		
		//Now we need to refill the population by crossing the fittest solutions
		while (vPopBoards.size() < POP_SIZE) {
			//Provide the cross method with 4 parent boards to create a child from
			int randIndex0 = roller.nextInt((int)(prob * POP_SIZE));
			int randIndex1 = roller.nextInt((int)(prob * POP_SIZE));
			int randIndex2 = roller.nextInt((int)(prob * POP_SIZE));
			int randIndex3 = roller.nextInt((int)(prob * POP_SIZE));
			
			Board parent0 = vPopBoards.get(randIndex0);
			Board parent1 = vPopBoards.get(randIndex1);
			Board parent2 = vPopBoards.get(randIndex2);
			Board parent3 = vPopBoards.get(randIndex3);
			
			//Build vector of parents to pass to cross function
			Vector<Board> parents = new Vector<Board>();
			parents.add(parent0);
			parents.add(parent1);
			parents.add(parent2);
			parents.add(parent3);
			
			Board child = cross(parents);
			vPopBoards.add(child);
		}
		
		//Now mutate the population
		for (Board b : vPopBoards) {
			b.mutate(MUTATE_PROB, originalBoard);
		}
		
		//Now reassess fitness numbers and sort
		setFitnessScores();
		
		//Finally add our new best solution to the pastFitness array list
		pastFitness.add(getBestSolution().getFitness());
	}
	
	//Reset if best fitness is unchanged for resetSize rounds.
	public void reset() {
		if (pastFitness.size() < resetSize) {
			return;
		}
		//Check if all the same
		boolean same = true;
		int firstFit = pastFitness.get(0);
		for (int i = 1; i < pastFitness.size(); i++) {
			if (pastFitness.get(i) != firstFit) {
				same = false;
				
				//Not stuck. Remove the first index and continue
				pastFitness.remove(0);
				return;
			}
		}
		
		//If we've made it here we are stuck. Reset pop, past fitness, print statement, and increment stuck count
		vPopBoards = new Vector<Board>(); 
		
		//Fill the population with new boards
		for (int i = 0; i < POP_SIZE; i++) {
			vPopBoards.add(new Board(originalBoard));
		}
		
		//Assign fitness score to population
		setFitnessScores();
		
		//Reset past fitness
		pastFitness = new ArrayList<Integer>();
		
		//Print message
		//System.out.println("\nStuck, resetting the population\n");
		
		//Increment stuck count
		stuckCount++;
	}
	
	public Board getBestSolution() {
		return vPopBoards.get(0);
	}
	
	public Board isSolved() {
		if (vPopBoards.get(0).isSolved()) {
			return vPopBoards.get(0);
		}
		
		return null;
	}
	
	public int getStuckCount() {
		return stuckCount;
	}
	
	public void setSelectivity(int inSelect) {
		SELECTIVITY = inSelect;
	}
	
	public void setSavior(int inSavior) {
		SAVIOR_PROB = inSavior;
	}
	
	public void setMutate(int inMutate) {
		MUTATE_PROB = inMutate;
	}
	
	public int getSelectivity() {
		return SELECTIVITY;
	}
	
	public int getSavior() {
		return SAVIOR_PROB;
	}
	
	public int getMutate() {
		return MUTATE_PROB;
	}
	
	private Board cross(Vector<Board> inParents) {
		int fromParent;
		StringBuilder buildBoard = new StringBuilder();
		
		//Iterate over all rows and build one by one, randomly selecting from a parent
		for (int i = 0; i < 9; i++) {
			fromParent = roller.nextInt(4);
			buildBoard.append(inParents.get(fromParent).getRowStringData(i));
		}
		
		return new Board(buildBoard.toString());
	}
}
