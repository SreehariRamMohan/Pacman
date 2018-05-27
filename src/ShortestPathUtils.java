import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;

public class ShortestPathUtils {

	private static final int NUMBER_OF_PATHS_TO_FIND_IN_DFS = 50; //cut off our DFS after 50 paths are found.
	private static int numPathsFound = 0;
	private static ArrayList<ArrayList<int[]>> allPaths = new ArrayList<>(); //stores all possible paths found by DFS.

	/*
	 * DFS - Dept first algorithm for path-finding
	 */
	
	/**
	 * Our core algorithm for making the ghost AI. This method returns an array list of [row, col] movements which the ghost can then use to move itself around the board and find the pacman
	 * @param ghostCurrRow your current row
	 * @param ghostCurrCol your current column
	 * @param pacmanRow the row you want to get to
	 * @param pacmanCol the column you want to go to
	 * @param m the model
	 * @return
	 */
	public static ArrayList<int[]> getPaths(int ghostCurrRow, int ghostCurrCol, int pacmanRow, int pacmanCol, Model m) {
		numPathsFound = 0;
		allPaths.clear();
		
		//create a visited list to prevent ourselves from retracing paths we have already tried. 
		boolean[][] visitedList = new boolean[m.getNumRows()][m.getNumCols()];
		
		ArrayList<int[]> oneOfThePaths = new ArrayList<>(); // the path being built up recursively.
		oneOfThePaths.add(new int[]{ghostCurrRow, ghostCurrCol});
		
		shortestPath(ghostCurrRow, ghostCurrCol, pacmanRow, pacmanCol, visitedList, m, oneOfThePaths);
		
		ArrayList<int[]> optimalPath = findMostOptimal(allPaths);

		return optimalPath;
	}
	
	/**
	 * Method to find the best path from allPaths. We define the best path as the path with the shortest length.
	 * @param allPaths a list containing all possible paths returned from the DFS algorithm.
	 * @return
	 */
	public static ArrayList<int[]> findMostOptimal(ArrayList<ArrayList<int[]>> allPaths) {				
		//use selection sort to find the shortest (most optimal) path and return it. 
		
		int smallest = Integer.MAX_VALUE;
		ArrayList<int[]> toPick = null; 
		for(ArrayList<int[]> candidate : allPaths) {
			if(candidate.size() < smallest) {
				toPick = candidate;
				smallest = candidate.size();
			}
		}
		return toPick;
	}

	/**
	 * Recursive method to build up a path
	 * @param currentRow
	 * @param currentCol
	 * @param destinationRow
	 * @param destinationCol
	 * @param visited the visited array
	 * @param m the model
	 * @param oneOfThePaths the path being built up.
	 */
	public static void shortestPath(int currentRow, int currentCol, int destinationRow, int destinationCol, boolean[][] visited, Model m, ArrayList<int[]> oneOfThePaths)  {
		
		if(numPathsFound >= NUMBER_OF_PATHS_TO_FIND_IN_DFS) {
			return;
		} 
				
		visited[currentRow][currentCol] = true;
		
		if(currentRow == destinationRow && currentCol == destinationCol) {
			//we're done creating the [][] of relative distances, so we can return and find the shortest path.
			numPathsFound++;
			allPaths.add((ArrayList<int[]>) oneOfThePaths.clone());
		} else {
			
			if(isInBounds(currentRow, currentCol - 1, visited) && !visited[currentRow][currentCol - 1] && hasPath(currentRow, currentCol - 1, m)) { //left open				
				int[] thisLocation = new int[] {currentRow, currentCol - 1};
				oneOfThePaths.add(thisLocation);
				shortestPath(currentRow, currentCol - 1, destinationRow, destinationCol, visited, m, oneOfThePaths);
				oneOfThePaths.remove(thisLocation);
			}
			if(isInBounds(currentRow, currentCol + 1, visited) && !visited[currentRow][currentCol + 1] && hasPath(currentRow, currentCol + 1, m)) { //right open
				int[] thisLocation = new int[] {currentRow, currentCol + 1};
				oneOfThePaths.add(thisLocation);
				shortestPath(currentRow, currentCol + 1, destinationRow, destinationCol, visited, m, oneOfThePaths);
				oneOfThePaths.remove(thisLocation);
			}
			if(isInBounds(currentRow - 1, currentCol, visited) && !visited[currentRow - 1][currentCol] && hasPath(currentRow - 1, currentCol, m)) { //top open
				int[] thisLocation = new int[] {currentRow - 1, currentCol};
				oneOfThePaths.add(thisLocation);
				shortestPath(currentRow - 1, currentCol, destinationRow, destinationCol, visited, m, oneOfThePaths);
				oneOfThePaths.remove(thisLocation);
			}
			if(isInBounds(currentRow + 1, currentCol, visited) && !visited[currentRow + 1][currentCol] && hasPath(currentRow + 1, currentCol, m)) { //bottom open
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

	/**
	 * Determines whether this spot in the model can be safely moved on by the ghost.
	 * @param row
	 * @param col
	 * @param m
	 * @return
	 */
	public static boolean hasPath(int row, int col, Model m) {
		return ((m.objectAt(row, col) == null) || 
				m.objectAt(row, col) instanceof Pacman ||
				m.objectAt(row, col) instanceof Ghost);
	}

}
