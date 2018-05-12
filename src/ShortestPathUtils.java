import java.util.Stack;

public class ShortestPathUtils {

	/*
	 * Lee's algorithm
	 * 
	 * ~ Created May 11 10:30 PM ~ Sreehari 
	 * Version 001
	 * 
	 * Psuedocode & Examples utilized for this code
	 * https://en.wikipedia.org/wiki/Lee_algorithm
	 * https://stackoverflow.com/questions/28889273/how-to-find-the-shortest-path-in-2d-array-with-multiple-endpoints
	 */
	public static String getNextOptimalTurn(int ghostCurrRow, int ghostCurrCol, int pacmanRow, int pacmanCol, Model m) {
		Integer[][] relativeDistancesFromStart = new Integer[m.getNumRows()][m.getNumCols()];
		shortestPath(ghostCurrRow, ghostCurrCol, pacmanRow, pacmanCol, relativeDistancesFromStart, m, 0);

		//back-track from end -> start going from bigger i's until we hit i = 0.

		int i = relativeDistancesFromStart[pacmanRow][pacmanCol];
		int currRow = pacmanRow;
		int currCol = ghostCurrCol;
		Stack<String> chronologicalMoves = new Stack();
		//because we are going in reverse order, the next move that the pacman should be will be the last item added to to the stack.

		while(i > 0) {


			//try all directions: left, right, top, bottom and see which one has descending relative cost.

			if(hasPath(currRow, currCol - 1, relativeDistancesFromStart) && relativeDistancesFromStart[currRow][currCol - 1] == (i - 1)) { //left
				currRow = currRow;
				currCol = currCol - 1;
				chronologicalMoves.push("RIGHT");
			}
			else if(hasPath(currRow, currCol + 1, relativeDistancesFromStart) && relativeDistancesFromStart[currRow][currCol - 1] == (i - 1)) { //right
				currRow = currRow;
				currCol = currCol + 1;
				chronologicalMoves.push("LEFT");
			}
			else if(hasPath(currRow - 1, currCol, relativeDistancesFromStart) && relativeDistancesFromStart[currRow][currCol - 1] == (i - 1)) { //top
				currRow = currRow - 1;
				currCol = currCol;
				chronologicalMoves.push("BOTTM");
			}
			else if(hasPath(currRow + 1, currCol, relativeDistancesFromStart) && relativeDistancesFromStart[currRow][currCol - 1] == (i - 1)) { //bottom
				currRow = currRow + 1;
				currCol = currCol;
				chronologicalMoves.push("TOP");
			}
			
			i--;
		}
		
		return chronologicalMoves.pop();

	}

	public static void shortestPath(int currentRow, int currentCol, int destinationRow, int destinationCol, Integer[][] relativeDistancesFromStart, Model m, int i)  {
		if(currentRow == destinationRow && currentCol == destinationCol) {
			//we're done creating the [][] of relative distances, so we can return and find the shortest path.
			relativeDistancesFromStart[currentRow][currentCol] = i;
			return;
		} else {
			relativeDistancesFromStart[currentRow][currentCol] = i;

			if(hasPath(currentRow, currentCol - 1, m)) { //left open
				shortestPath(currentRow, currentCol - 1, destinationRow, destinationCol, relativeDistancesFromStart, m, i + 1);
			}
			if(hasPath(currentRow, currentCol + 1, m)) { //right open
				shortestPath(currentRow, currentCol + 1, destinationRow, destinationCol, relativeDistancesFromStart, m, i + 1);
			} 
			if(hasPath(currentRow - 1, currentCol, m)) { //top open
				shortestPath(currentRow - 1, currentCol, destinationRow, destinationCol, relativeDistancesFromStart, m, i + 1);
			} 
			if(hasPath(currentRow + 1, currentCol, m)) { //bottom open
				shortestPath(currentRow + 1, currentCol, destinationRow, destinationCol, relativeDistancesFromStart, m, i + 1);	
			} 
		}
	}

	public static boolean hasPath(int r, int col, Integer[][] rel) {
		return r >= 0 && r < rel.length && col >= 0 && col < rel[0].length && rel[r][col] != null;
	}


	public static boolean hasPath(int row, int col, Model m) {
		return row >= 0 && row < m.getNumRows() && col >= 0 && col < m.getNumCols() && (m.objectAt(row, col) == null);
	}

}
