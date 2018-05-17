import java.util.ArrayList;
import java.util.Arrays;

import javafx.scene.image.Image;

public class Ghost extends Character {

	private boolean isEdible;
	private boolean isAutoPlay;
	private Pacman pac;

	private String[] dirChoices = {"UP", "DOWN", "LEFT", "RIGHT"};
	private int actCounter = 0; 
	private ArrayList<int[]> currentPath;
	int[] nextMove;
	String dir;
	
	public Ghost() {
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

	public void toggleEdible() {
		isEdible = !isEdible;
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
			
			int[] ghostPos = Character.getRowCol(this.getX(), this.getY());
			
			forceRowColInBounds(ghostPos);
			
			int currRow = ghostPos[0];
			int currCol = ghostPos[1];

			int[] pacManPos = Character.getRowCol(this.getWorld().getPacman().getX(), this.getWorld().getPacman().getY());

			int goalRow = pacManPos[0];
			int goalCol = pacManPos[1];
			


			if(currentPath == null || currentPath.isEmpty()) {
				currentPath = (ShortestPathUtils.getPaths(currRow, currCol, goalRow, goalCol, this.getWorld().getModel()));
				//remove the first node in the list because it is where we are currently
				currentPath.remove(0);
				
				System.out.println("Ghost positon: " + Arrays.toString(Character.getRowCol(this.getX(), this.getY())));
				System.out.println("Pacman positon: " + Arrays.toString(Character.getRowCol(this.getWorld().getPacman().getX(), this.getWorld().getPacman().getY())));		
				for(int[] node : currentPath) {
					System.out.print(Arrays.toString(node) + " *-> ");
					
					if(getWorld().getModel().objectAt(node[0], node[1]) instanceof Wall) {
						System.out.println("*************PATH NOT CLEAR");
						
					} 
										
				}
				System.out.println("PATH CLEAR");
				
				
			}

			//			System.out.println("Ghost should move " + this.getDirection());


			//			this.safeMove(this.getDirection());

			if((actCounter%(Controller.CHARACTER_DIMS/this.getSpeed())) == 0) {
				nextMove = currentPath.remove(0);
				int nextRow = nextMove[0];
				int nextCol = nextMove[1];
				
				//figure out why the ghosts are still being blocked by walls despite using path-finding
				int rowDelta = Math.abs(nextRow - Character.getRowCol(this.getX(), this.getY())[0]);
				int colDelta = Math.abs(nextCol - Character.getRowCol(this.getX(), this.getY())[1]);
				
				//if this is true it is BADDD because it means that the ghost position and his next move are out of sync.
				if(rowDelta > 1 || colDelta > 1) {
					System.out.println("OUT OF SYNC WITH PATH");
					
				}
				
				
				/**
				 * trying to fix the issue where the pacman gets blocked despite following the DFS path
				 */
				//while(rowDelta > 1 || colDelta > 1) { //while out of sync

					/*
					 * If this is true it means that the ghosts position and it's next move are out of sync. 
					 * One way to fix this is to simply reset the path
					 */
					
					
					/**
					 * OPTION !
					 */
//					currentPath = (ShortestPathUtils.getPaths(currRow, currCol, goalRow, goalCol, this.getWorld().getModel()));
//					//remove the first node in the list because it is where we are currently
//					currentPath.remove(0);
//					
//					nextMove = currentPath.remove(0);
//					nextRow = nextMove[0];
//					nextCol = nextMove[1];	
//					
//					rowDelta = Math.abs(nextRow - Character.getRowCol(this.getX(), this.getY())[0]);
//					colDelta = Math.abs(nextCol - Character.getRowCol(this.getX(), this.getY())[1]);
					
					
					/**
					 * OPTION 2
					 */
					
//					if(currentPath.size() == 0) {
//						currentPath = currentPath = (ShortestPathUtils.getPaths(currRow, currCol, goalRow, goalCol, this.getWorld().getModel()));
//						currentPath.remove(0);
//					} 
//					
//					nextMove = currentPath.remove(0);
//					nextRow = nextMove[0];
//					nextCol = nextMove[1];	
//					
//					rowDelta = Math.abs(nextRow - Character.getRowCol(this.getX(), this.getY())[0]);
//					colDelta = Math.abs(nextCol - Character.getRowCol(this.getX(), this.getY())[1]);
//					
				//}
				
				
				
				
				
				//prevent ourselves from moving to the same spot.
				while(Character.getRowCol(this.getX(), this.getY())[0] == nextRow && 
					Character.getRowCol(this.getX(), this.getY())[1] == nextCol) {
						nextMove = currentPath.remove(0);
						nextRow = nextMove[0];
						nextCol = nextMove[1];
				}
				
				dir = getDirectionFromNode(nextRow, nextCol, currRow, currCol);
				this.setDirection(dir);	

				this.centerGhostInCell();
			}  
			if(this.canMoveForGhosts(dir)) {
				this.safeMove(dir);
				
			} else {
				System.out.println("Blocked trying to go -> " + this.getDirection() + "current position = " + Arrays.toString(this.getRowCol(this.getX(), this.getY())) + " future position " + Arrays.toString(this.getFutureRowColFromDirection(this.getDirection())));
				System.exit(0);
			}



		}




	}

	private void forceRowColInBounds(int[] ghostPos) {
		if(!ShortestPathUtils.isInBounds(ghostPos[0], ghostPos[1], this.getWorld().getModel())) {
			if(ghostPos[1] > this.getWorld().getModel().getNumCols()) {
				ghostPos[0] = 0;
			} else {
				ghostPos[0] = this.getWorld().getModel().getNumCols() - 1;
			}
		}
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
	
	public String getDirectionFromNode(int nextRow, int nextCol, int currRow, int currCol)  {
	
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
			
			System.out.println(getWorld().getModel().objectAt(nextRow, nextCol).getClass().getName());
			
			
			 
			
			
			return null;
		}
	}


}
