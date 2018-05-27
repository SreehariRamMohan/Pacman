import java.util.ArrayList;
import java.util.Arrays;

import com.sun.scenario.effect.light.SpotLight;

import javafx.scene.image.Image;

public class Ghost extends Character {

	private boolean isEdible;
	private boolean isAutoPlay;
	private Pacman pac;

	private String[] dirChoices = {"UP", "DOWN", "LEFT", "RIGHT"};
	private int actCounter = 0; 
	private ArrayList<int[]> currentPath;
	int[] nextMove;
	
	private int startingRow;
	private int startingCol;
	
	private int numSquaresOfPacmanUntilReset;
	private int numberOfResets = 0; //holds the previous division value of squares/(squares until reset). We update this value to make sure we aren't recalculating the path multiple times if the pacman is stationary.
	
	
	
	
	public Ghost(int startingRow, int startingCol, int numSquaresOfPacmanUntilReset) {
		
		this.numSquaresOfPacmanUntilReset = numSquaresOfPacmanUntilReset;
		this.startingRow = startingRow;
		this.startingCol = startingCol;
		
		this.setImage(new Image("imgs/ghost.png")); 
		setSpeed(3);
		isAutoPlay = true;

	}
	
	/**
	 * Legacy method to choose a random direction which was used for random turning before we had DFS
	 * @return
	 */
	public int chooseRandomIndex() {
		return (int)(Math.random() * dirChoices.length);
	}

	/**
	 * Legacy method to help pick a direction to turn to
	 * @param chance
	 * @param num
	 * @return
	 */
	public boolean shouldTurn(int chance, int num) {
		int rand = 1 + (int) (Math.random() * (num)); // 1 - num
		if(rand <= chance) {
			return true;
		} else {
			return false;
		}
	}

	
	@Override
	public void act(long now) {
		//Act will move depending on auto play state.

		if(isAutoPlay) {
			autoPlayNextMove();
		}
		actCounter++;


	}

	/**
	 * The "heart" of our game. This method fetches a DFS path and uses that to guide the ghosts to the pacman.
	 */
	public void autoPlayNextMove() {
		
		edgeLoop();

		if(getWorld().getLevel() == 1) {

			int[] ghostPos = Character.getRowCol(this.getX() + this.getWidth()/2, this.getY() + this.getHeight()/2);
			
			forceRowColInBounds(ghostPos);
			
			int currRow = ghostPos[0];
			int currCol = ghostPos[1];


			int[] pacManPos = Character.getRowCol(this.getWorld().getPacman().getX() + this.getWorld().getPacman().getWidth()/2, this.getWorld().getPacman().getY() + this.getWorld().getPacman().getHeight()/2);
			//forceRowColInBounds(pacManPos);
			
			int pacmanRow = pacManPos[0];
			int pacmanCol = pacManPos[1];
			
			
			
			if(currRow == pacmanRow && currCol == pacmanCol) {
				// we are at the pacman, so we should stop and allow the pacman to detect us.
				return;
			}
			
			if(currentPath == null || currentPath.isEmpty() || needNewPathBasedGhostReset()) {
				currentPath = (ShortestPathUtils.getPaths(currRow, currCol, pacmanRow, pacmanCol, this.getWorld().getModel())); //use DFS to get the path to the ghost.
				//remove the first node in the list because it is where we are currently
				
				if(currentPath == null || currentPath.size() == 0) {										
					return;
				}

				currentPath.remove(0); // = [currRow, currCol] = starting position of the ghost
				
				String dir = getDirectionFromNode(currentPath.get(0)[0], currentPath.get(0)[1], currRow, currCol); //translates next move in the path (in form [row, col]) to a direction.
				
				this.setDirection(dir);
				
				this.centerCharacterInCell(); //centers in cell to avoid compounding errors to cause the ghost to "drift" off course.
			
			}

			ghostPos = Character.getRowCol(this.getX() + this.getWidth()/2, this.getY() + this.getHeight()/2);
			
			forceRowColInBounds(ghostPos);
			
			currRow = ghostPos[0];
			currCol = ghostPos[1];
			
			if(currRow == currentPath.get(0)[0] && 
					currCol == currentPath.get(0)[1]) {

				currentPath.remove(0);

				if(currentPath.isEmpty()) {
					
					currentPath = (ShortestPathUtils.getPaths(currRow, currCol, pacmanRow, pacmanCol, this.getWorld().getModel()));
					//remove the first node in the list because it is where we are currently
					currentPath.remove(0);
										
				}
				
				String nextDir = getDirectionFromNode(currentPath.get(0)[0], currentPath.get(0)[1], currRow, currCol);

				String oldDir = this.getDirection();

				if(!oldDir.equals(nextDir)) {
					this.centerCharacterInCell();
					this.setDirection(nextDir);
				} else {
					this.setDirection(nextDir);
				}	
			}

			this.safeMove(this.getDirection());

		}




	}

	/**
	 * Determines whether a ghost needs to reset the path based on his/her personality.
	 * @return
	 */
	private boolean needNewPathBasedGhostReset() {
		if(this.numSquaresOfPacmanUntilReset == -1) { //Clyde doesn't reset path
			return false;
		}
		int squaresPacmanMoved = ((Pacman) this.getWorld().getPacman()).getNumSquaresMoved();
		int numberOfResets = squaresPacmanMoved/numSquaresOfPacmanUntilReset;
		
		if(numberOfResets != this.numberOfResets) {
			boolean needsReset = ((squaresPacmanMoved > 0) && ((squaresPacmanMoved%numSquaresOfPacmanUntilReset) == 0));
			if(needsReset) {
				this.numberOfResets = numberOfResets;
			} 
			return needsReset;
		} else {
			return false;
		}
	}

	/**
	 * Method to "force" row, cols in bounds while edge-looping to avoid errors.
	 * @param pos
	 */
	private void forceRowColInBounds(int[] pos) {		
		if(!ShortestPathUtils.isInBounds(pos[0], pos[1], this.getWorld().getModel())) {
			if(pos[1] >= this.getWorld().getModel().getNumCols()) {
				pos[1] = 1;
			} else {
				pos[1] = this.getWorld().getModel().getNumCols() - 1;
			}
		}
		
	}

	
	public void clearPathList() {
		if(this.currentPath != null) {
			this.currentPath.clear();
		}
	}

	private void edgeLoop() {
		//allow the ghost to edge loop
		if(this.getX() > getWorld().getWidth()) {
			this.setCoordinate(0, this.getY());
		} else if((this.getX()) <= 0) {
			this.setCoordinate(this.getWorld().getWidth() - this.getWidth(), this.getY());
		}		
	}
	
	public boolean isAutoPlay() {
		return isAutoPlay;
	}
	
	public void toggleAutoPlay() {
		isAutoPlay = !isAutoPlay;
	}
	
	/**
	 * Translates [row, col] next position in path to a specific direction such as UP, RIGHT, LEFT, or RIGHT.
	 * Precondition: the next [row, col] is exactly 1 square away from us either up, down, left or right. Not diagonal.
	 * @param nextRow
	 * @param nextCol
	 * @param currRow
	 * @param currCol
	 * @return
	 */
	public static String getDirectionFromNode(int nextRow, int nextCol, int currRow, int currCol)  {
	
		int dx = nextCol - currCol;
		int dy = nextRow - currRow;
	
		if(dx < 0) {
			return "LEFT";
		} else if(dx > 0) {
			return "RIGHT";
		} else if(dy < 0) {
			return "UP";
		} else if(dy > 0){
			return "DOWN";
		} else {			
			return null;
		}
	}

	public int getStartingRow() {
		return startingRow;
	}
	public int getStartingCol() {
		return startingCol;
	}
	
	public boolean isEdible() {
		return isEdible;
	}

	public void setEdible(boolean isEdible) {
		this.isEdible = isEdible;
	}

	public Pacman getPacman() {
		return pac;
	}
}