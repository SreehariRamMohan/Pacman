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
	
	public Ghost(int startingRow, int startingCol) {
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
			/**
			 * Level 1 is simple, the pac-man simply moves randomly around the board
			 */
			
			int[] ghostPos = Character.getRowCol(this.getX() + this.getWidth()/2, this.getY() + this.getHeight()/2);
			
			forceRowColInBounds(ghostPos);
			
			int currRow = ghostPos[0];
			int currCol = ghostPos[1];

			int[] pacManPos = Character.getRowCol(this.getWorld().getPacman().getX(), this.getWorld().getPacman().getY());

			int pacmanRow = pacManPos[0];
			int pacmanCol = pacManPos[1];
			
			if(currRow == pacmanRow && currCol == pacmanCol) {
				//we are at a pacman so the game should end, unless the pacman is edible
				//System.out.println("Curr row = " + currRow + " currCol = " + currCol + " pacmanRow = " + pacmanRow + " pacmanCol = " + pacmanCol);
				
				return;
			}
			
			if(currentPath == null || currentPath.isEmpty()) {
				currentPath = (ShortestPathUtils.getPaths(currRow, currCol, pacmanRow, pacmanCol, this.getWorld().getModel()));
				//remove the first node in the list because it is where we are currently
				
				if(currentPath == null || currentPath.size() == 0) {
					return;
				}
				
				System.out.println("Current path size = " + currentPath.size());
				System.out.println("curr row = " + currRow + " currCol = " + currCol);
				System.out.println("Pacman row = " + pacmanRow + " pacman col = " + pacmanCol);
								
				
				
				currentPath.remove(0); // = [currRow, currCol] = starting position of the ghost
				
				String dir = getDirectionFromNode(currentPath.get(0)[0], currentPath.get(0)[1], currRow, currCol);
				
				this.setDirection(dir);
				
				this.centerCharacterInCell();
//				System.out.println("Ghost positon: " + Arrays.toString(Character.getRowCol(this.getX(), this.getY())));
//				System.out.println("Pacman positon: " + Arrays.toString(Character.getRowCol(this.getWorld().getPacman().getX(), this.getWorld().getPacman().getY())));		
				for(int[] node : currentPath) {
					System.out.print(Arrays.toString(node) + " *-> ");
				}				
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

	private void forceRowColInBounds(int[] ghostPos) {
//		System.out.println("Before " + Arrays.toString(ghostPos));
		
		if(!ShortestPathUtils.isInBounds(ghostPos[0], ghostPos[1], this.getWorld().getModel())) {
			if(ghostPos[1] >= this.getWorld().getModel().getNumCols()) {
				ghostPos[0] = 0;
			} else {
				ghostPos[0] = this.getWorld().getModel().getNumCols() - 1;
			}
		}
//		System.out.println("After " + Arrays.toString(ghostPos));
		
	}
	
	public void clearPathList() {
		this.currentPath.clear();
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

//		System.out.println("Inside getDirectionFromNode() nextRow = " + nextRow + " nextCol = " + nextCol + ""
//				+ " currRow = " + currRow + " currCol = " + currCol);
		
	
		if(dx < 0) {
			return "LEFT";
		} else if(dx > 0) {
			return "RIGHT";
		} else if(dy < 0) {
			return "UP";
		} else if(dy > 0){
			return "DOWN";
		} else {
			System.out.println("THis should never happen! dx = " + dx + " dy = " + dy);
			System.out.println();
			
//			int[] ghostPos = Character.getRowCol(this.getX(), this.getY());
//			int currRow1 = ghostPos[0];
//			int currCol1 = ghostPos[1];
//			int[] pacManPos = Character.getRowCol(this.getWorld().getPacman().getX(), this.getWorld().getPacman().getY());
//			int goalRow1 = pacManPos[0];
//			int goalCol1 = pacManPos[1];
//			
//			System.out.println("Ghost row = " + currRow1 + " ghost col " + currCol1);
//			System.out.println("Pacman row = " + goalRow1 + " Pacman col " + goalCol1);

			System.out.println("nextRow = " + nextRow);
			System.out.println("Next col = " + nextCol);
			
			//System.out.println(getWorld().getModel().objectAt(nextRow, nextCol).getClass().getName());
			
			
			 
			
			
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