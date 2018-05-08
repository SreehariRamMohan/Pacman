import javafx.scene.image.Image;

public class Pacman extends Character{
	
	private int lives;
	
	public Pacman() {
		this.setImage(new Image("imgs/pacMan.png"));
		lives = 3;
		setSpeed(3);
	}
	
	public Pacman(int lives) {
		//this.setImage() Add image later
		this.lives = lives;
		setSpeed(3);
	}
	
	@Override
	public void act(long now) {
		//move in current direction
		if(this.getDirection() == null) {
			//start of game do nothing until player moves.
		} else if(this.getDirection().equals(Character.RIGHT)) {
			this.move(getSpeed(), 0);
			this.setRotate(0);
		} else if(this.getDirection().equals(Character.LEFT)) {
			this.move(-getSpeed(), 0);
			this.setRotate(180);
		} else if(this.getDirection().equals(Character.DOWN)) {
			this.move(0, getSpeed());
			this.setRotate(90);
		} else if(this.getDirection().equals(Character.UP)) {
			this.move(0, -getSpeed());
			this.setRotate(270);
		}
	}
	
	public void decrementLives() {
		lives--;
	}
	
	public int getLives() {
		return lives;
	}
	
	public void setLives(int lives) {
		this.lives = lives;
	}
	
	public void onEat() {
		//Does something when something is eaten
	}
}
