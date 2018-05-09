import javafx.scene.image.Image;

public class Ghost extends Character{
	
	private boolean isEdible;
	private boolean isAutoPlay;
	private Pacman pac;
	
	private String direction;
	
	private String[] dirChoices = {"UP", "DOWN", "LEFT", "RIGHT"};
	
	public Ghost() {
		this.setImage(new Image("imgs/ghost.png")); 
		setSpeed(3);
		isAutoPlay = true;
		
		direction = dirChoices[chooseRandomIndex()];
		
	}
	
	public int chooseRandomIndex() {
		return (int)(Math.random() * dirChoices.length);
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
			//level 1 is easy, the pac man move randomly
			if(this.isInCenter()) {
				direction = dirChoices[chooseRandomIndex()];
			} else {
				if(direction.equals("UP")) {
					this.move(0, -getSpeed());
				} else if(direction.equals("DOWN")) {
					this.move(0, getSpeed());
				} else if(direction.equals("RIGHT")) {
					this.move(getSpeed(), 0);
				} else {
					this.move(-getSpeed(), 0);
				}
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
