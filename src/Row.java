import java.util.Random;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Row {
	private final static int ROWSIZE = 9;
	private ArrayList<Integer> possNums = new ArrayList<Integer>(Arrays.asList(1,2,3,4,5,6,7,8,9));
	private int[] vData = new int[ROWSIZE];
	private static Random roller = new Random();
	
	private static Random random = new Random();
	
	public Row(int[] vIntsIn) {
		vData = vIntsIn.clone();
		
		for (int i = 0; i < ROWSIZE; i++) {
			int num = vIntsIn[i];
			if (num != 0) {
				//Remove this number from the possNums
				for (int j = 0; j < possNums.size(); j++) {
					if (possNums.get(j) == num) {
						possNums.remove(j);
					}
				}
			} 
		}
		
		//Now we have placed the set numbers, randomly place the rest
		for (int i = 0; i < ROWSIZE; i++) {
			if (vData[i] == 0) {
				int toInsert = random.nextInt(possNums.size());
				vData[i] = possNums.get(toInsert);
				possNums.remove(toInsert);
			}
		}
	}
	
	//prob is integer from 0 to 100 representing the mutation prob
	//Roll 3 times and decide whether to swap random indices
	public void mutate(int prob) {
		for (int i = 0; i < 3; i++) {
			if (roller.nextInt(100) < prob) {
				//mutate by swapping 2 ints in the row. This way we maintain the invariant of our rows being valid
				int swapIndex0 = roller.nextInt(ROWSIZE);
				int swapIndex1 = roller.nextInt(ROWSIZE);
				int swapInt0 = vData[swapIndex0];
				int swapInt1 = vData[swapIndex1];
				
				vData[swapIndex0] = swapInt1;
				vData[swapIndex1] = swapInt0;				
			}
		}
	}
	
	public int getIntAtIndex(int index) {
		return vData[index];
	}
	
	public String getRowDataAsString() {
		StringBuilder toBuild = new StringBuilder();
		for (int i = 0; i < ROWSIZE; i++) {
			toBuild.append(vData[i]);
		}
		
		return toBuild.toString();
	}
	
	@Override public String toString() {
		//String toReturn = "Row " + rowNum + ":   ";
		String toReturn = "";
		for (int i = 0; i < ROWSIZE; i++) {
			toReturn = toReturn + vData[i] + " ";
			if ((i + 1) % 3 == 0 && i != 8) {toReturn = toReturn + " | ";}
		}
		
		return toReturn;
	}
}
