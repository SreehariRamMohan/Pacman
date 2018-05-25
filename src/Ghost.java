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
	
	public int chooseRandomIndex() {
		return (int)(Math.random() * dirChoices.length);
	}

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

	public boolean isEdible() {
		return isEdible;
	}

	public void setEdible(boolean isEdible) {
		this.isEdible = isEdible;
	}

	public Pacman getPacman() {
		return pac;
	}

	public void autoPlayNextMove() {
		
		edgeLoop();

		
		
		if(getWorld().getLevel() == 1) {
			
			
			
			int[] ghostPos = Character.getRowCol(this.getX() + this.getWidth()/2, this.getY() + this.getHeight()/2);
			
			forceRowColInBounds(ghostPos);
			
			int currRow = ghostPos[0];
			int currCol = ghostPos[1];

			int[] pacManPos = Character.getRowCol(this.getWorld().getPacman().getX() + this.getWorld().getPacman().getWidth()/2, this.getWorld().getPacman().getY() + this.getWorld().getPacman().getHeight()/2);

			int pacmanRow = pacManPos[0];
			int pacmanCol = pacManPos[1];
			
			if(currRow == pacmanRow && currCol == pacmanCol) {
				// we are at the pacman, so we should stop and allow the pacman to detect us.
				return;
			}
			
			if(currentPath == null || currentPath.isEmpty() || needNewPathBasedGhostReset()) {
				currentPath = (ShortestPathUtils.getPaths(currRow, currCol, pacmanRow, pacmanCol, this.getWorld().getModel()));
				//remove the first node in the list because it is where we are currently
				
				if(currentPath == null || currentPath.size() == 0) {					
					return;
				}

				currentPath.remove(0); // = [currRow, currCol] = starting position of the ghost
				
				String dir = getDirectionFromNode(currentPath.get(0)[0], currentPath.get(0)[1], currRow, currCol);
				
				this.setDirection(dir);
				
				this.centerCharacterInCell();
			
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

	private boolean needNewPathBasedGhostReset() {
		if(this.numSquaresOfPacmanUntilReset == -1) { //clyde doesn't reset path
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

	private void forceRowColInBounds(int[] ghostPos) {		
		if(!ShortestPathUtils.isInBounds(ghostPos[0], ghostPos[1], this.getWorld().getModel())) {
			if(ghostPos[1] >= this.getWorld().getModel().getNumCols()) {
				ghostPos[0] = 0;
			} else {
				ghostPos[0] = this.getWorld().getModel().getNumCols() - 1;
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
}