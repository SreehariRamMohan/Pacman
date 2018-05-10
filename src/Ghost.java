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
			
		}
	}
	
	public boolean isAutoPlay() {
		return isAutoPlay;
	}
	
	public void toggleAutoPlay() {
		isAutoPlay = !isAutoPlay;
	}
	

}
