import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;

public class ShortestPathUtils {

	private static final int NUMBER_OF_PATHS_TO_FIND_IN_DFS = 500;
	private static int numPathsFound = 0;
	private static ArrayList<ArrayList<int[]>> allPaths = new ArrayList<>();

	/*
	 * DFS - Dept first algorithm for path-finding
	 */
	public static ArrayList<int[]> getPaths(int ghostCurrRow, int ghostCurrCol, int pacmanRow, int pacmanCol, Model m) {
		numPathsFound = 0;
		allPaths.clear();
		
		boolean[][] visitedList = new boolean[m.getNumRows()][m.getNumCols()];
		
		ArrayList<int[]> oneOfThePaths = new ArrayList<>();
		oneOfThePaths.add(new int[]{ghostCurrRow, ghostCurrCol});
				
		shortestPath(ghostCurrRow, ghostCurrCol, pacmanRow, pacmanCol, visitedList, m, oneOfThePaths);

	
//		System.out.println("Paths Found");
//		for(ArrayList<int[]> path: allPaths) {
//			for(int[] node : path) {
//				
////				System.out.print(Arrays.toString(node) + " -> ");
//			}
//			//System.out.println();
//
//		}
		
		ArrayList<int[]> optimalPath = findMostOptimal(allPaths);
		
//		System.out.println("Length of shortest path is " + optimalPath.size());
		
		
		return optimalPath;
	
//		System.out.println("Optimal Path");
//		for(int[] node : optimalPath) {
//			System.out.print(Arrays.toString(node) + " -> ");
//		}
//		System.out.println();
//		
//		int[] nextMove = null;
//		
//		if(optimalPath.size() <= 1) {
//			nextMove = new int[]{ghostCurrRow, ghostCurrCol};
//		} else {
//			nextMove = new int[]{optimalPath.get(1)[0], optimalPath.get(1)[1]};
//		}
//		
//		int dx = nextMove[1] - ghostCurrCol;
//		int dy = nextMove[0] - ghostCurrRow;
//		
//		if(dx < 0 && dy == 0) {
//			return "LEFT";
//		} else if(dx > 0 && dy == 0) {
//			return "RIGHT";
//		} else if(dx == 0 && dy < 0) {
//			return "UP";
//		} else {
//			return "DOWN";
//		}
	}
	
	public static ArrayList<int[]> findMostOptimal(ArrayList<ArrayList<int[]>> allPaths) {
		
		int random = (int)(Math.random() * (allPaths.size()));
		
		
		int smallest = Integer.MAX_VALUE;
		ArrayList<int[]> toPick = null; 
		for(ArrayList<int[]> candidate : allPaths) {
			if(candidate.size() < smallest) {
				toPick = candidate;
				smallest = candidate.size();
			}
		}
		
		return allPaths.get(random);
	}


	public static void shortestPath(int currentRow, int currentCol, int destinationRow, int destinationCol, boolean[][] visited, Model m, ArrayList<int[]> oneOfThePaths)  {
		
		if(numPathsFound >= NUMBER_OF_PATHS_TO_FIND_IN_DFS) {
			return;
		} 
				
		visited[currentRow][currentCol] = true;
		
		if(currentRow == destinationRow && currentCol == destinationCol) {
			//we're done creating the [][] of relative distances, so we can return and find the shortest path.
//			System.out.println("Reached an end");
			
//			for(int[] pair : oneOfThePaths) {
//				System.out.print(Arrays.toString(pair) + "_");
//			}
//			System.out.println();
			numPathsFound++;
			
			allPaths.add((ArrayList<int[]>) oneOfThePaths.clone());

		} else {
			
			if(isInBounds(currentRow, currentCol - 1, visited) && !visited[currentRow][currentCol - 1] && hasPath(currentRow, currentCol - 1, m)) { //left open
//				System.out.println("Trying left");
				
				int[] thisLocation = new int[] {currentRow, currentCol - 1};
				oneOfThePaths.add(thisLocation);
				shortestPath(currentRow, currentCol - 1, destinationRow, destinationCol, visited, m, oneOfThePaths);
				oneOfThePaths.remove(thisLocation);
			}
			if(isInBounds(currentRow, currentCol + 1, visited) && !visited[currentRow][currentCol + 1] && hasPath(currentRow, currentCol + 1, m)) { //right open
//				System.out.println("Trying right");

				int[] thisLocation = new int[] {currentRow, currentCol + 1};
				oneOfThePaths.add(thisLocation);
				shortestPath(currentRow, currentCol + 1, destinationRow, destinationCol, visited, m, oneOfThePaths);
				oneOfThePaths.remove(thisLocation);
			}
			if(isInBounds(currentRow - 1, currentCol, visited) && !visited[currentRow - 1][currentCol] && hasPath(currentRow - 1, currentCol, m)) { //top open
//				System.out.println("Trying top");

				int[] thisLocation = new int[] {currentRow - 1, currentCol};
				oneOfThePaths.add(thisLocation);
				shortestPath(currentRow - 1, currentCol, destinationRow, destinationCol, visited, m, oneOfThePaths);
				oneOfThePaths.remove(thisLocation);
			}
			if(isInBounds(currentRow + 1, currentCol, visited) && !visited[currentRow + 1][currentCol] && hasPath(currentRow + 1, currentCol, m)) { //bottom open
//				System.out.println("Trying bottom");

				int[] thisLocation = new int[] {currentRow + 1, currentCol};
				oneOfThePaths.add(thisLocation);
				shortestPath(currentRow + 1, currentCol, destinationRow, destinationCol, visited, m, oneOfThePaths);
				oneOfThePaths.remove(thisLocation);
			}			
		}
		visited[currentRow][currentCol] = false;
	}
	
	public static boolean isInBounds(int r, int col, boolean[][] visited) {
		return r >= 0 && r < visited.length && col >= 0 && col < visited[r].length;
	}

	public static boolean isInBounds(int r, int col, Model m) {
		return r >= 0 && r < m.getNumRows() && col >= 0 && col < m.getNumCols();
	}

	public static boolean hasPath(int row, int col, Model m) {
		return ((m.objectAt(row, col) == null) || 
				m.objectAt(row, col) instanceof Pacman ||
				m.objectAt(row, col) instanceof Ghost);
	}

}
