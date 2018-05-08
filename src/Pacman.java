import javafx.scene.image.Image;

public class Pacman extends Character{
	private int lives;
	
	public Pacman() {
		this.setImage(new Image("imgs/pacMan.png"));
		lives = 3;
		setSpeed(10);
	}
	
	public Pacman(int lives) {
		//this.setImage() Add image later
		this.lives = lives;
		setSpeed(10);
	}
	
	@Override
	public void act(long now) {
		// TODO Auto-generated method stub
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
