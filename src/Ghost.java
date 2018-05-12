import java.util.Arrays;

import javafx.scene.image.Image;

public class Ghost extends Character {
	
	private boolean isEdible;
	private boolean isAutoPlay;
	private Pacman pac;
	
	private String direction;
	
	private String[] dirChoices = {"UP", "DOWN", "LEFT", "RIGHT"};
	
	private int actCounter = 0; 
	
	
	public Ghost() {
		this.setImage(new Image("imgs/ghost.png")); 
		setSpeed(3);
		isAutoPlay = true;
		
		direction = dirChoices[chooseRandomIndex()];
		
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
		if(getWorld().getLevel() == 1) {
			/**
			 * Level 1 is simple, the pac-man simply moves randomly around the board
			 */
			boolean wasItSafe = this.safeMove(direction, this);
			
			if(!wasItSafe) {
				//change the directio
				direction = dirChoices[chooseRandomIndex()];
			}
			
			
			/**
			 * DON'T Delete! This is the Ghost AI for path-finding
			 * 
			 * Please Help fix!
			 * 
			 * ~ Sreehari
			 */
			
			System.out.println("------------------------------GHOST AI-------------------------");
			System.out.println("Next best move for ghost at " + Arrays.toString(Character.getRowCol(this.getX(), this.getY())));
			
			int[] ghostPos = Character.getRowCol(this.getX(), this.getY());
			int currRow = ghostPos[0];
			int currCol = ghostPos[1];
			
			int[] pacManPos = Character.getRowCol(this.getWorld().getPacman().getX(), this.getWorld().getPacman().getY());
			
			int goalRow = pacManPos[0];
			int goalCol = pacManPos[1];
			
			
			System.out.println(ShortestPathUtils.getNextOptimalTurn(currRow, currCol, goalRow, goalCol, this.getWorld().getModel()));
			System.out.println("------------------------------GHOST AI-------------------------");

			
		}
	}
	
	public boolean isAutoPlay() {
		return isAutoPlay;
	}
	
	public void toggleAutoPlay() {
		isAutoPlay = !isAutoPlay;
	}
	

}
