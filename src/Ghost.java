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
				
				System.out.println("Ghost positon: " + Arrays.toString(Character.getRowCol(this.getX(), this.getY())));
				System.out.println("Pacman positon: " + Arrays.toString(Character.getRowCol(this.getWorld().getPacman().getX(), this.getWorld().getPacman().getY())));

				
				for(int[] node : currentPath) {
					System.out.print(Arrays.toString(node) + " -> ");
				}
				System.out.println();
				
				System.exit(0);
				
			}

			//			System.out.println("Ghost should move " + this.getDirection());


			//			this.safeMove(this.getDirection());

			if((actCounter%10) == 0) {
				nextMove = currentPath.remove(0);
				int nextRow = nextMove[0];
				int nextCol = nextMove[1];
				
				dir = getDirectionFromNode(nextRow, nextCol, currRow, currCol);
								
//				dir = dirChoices[chooseRandomIndex()];
//				while(!this.canMove(dir)) {
//					dir = dirChoices[chooseRandomIndex()];
//				}
				this.centerGhostInCell();
			}  
			if(this.canMove(dir)) {
				
				//this.safeMove(dir);
				this.safeMove(dir);
				
//				moveInDirectionBy(dir, 15);
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
	
	public String getDirectionFromNode(int nextRow, int nextCol, int currRow, int currCol) {
	
		int dx = nextCol - currCol;
		int dy = nextRow - currRow;
	
		if(dx < 0 && dy == 0) {
			return "LEFT";
		} else if(dx > 0 && dy == 0) {
			return "RIGHT";
		} else if(dx == 0 && dy < 0) {
			return "UP";
		} else {
			return "DOWN";
		}
	}


}
