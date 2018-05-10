import javafx.scene.image.Image;

public class Pacman extends Character{
	
	private int lives;
	
	private static int trackPoint = 0;
	
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
			if(this.safeMove(RIGHT, this)) {
				this.setRotate(0);
			}
		} else if(this.getDirection().equals(Character.LEFT)) {
			if(this.safeMove(LEFT, this)) {
				this.setRotate(180);
			}
		} else if(this.getDirection().equals(Character.DOWN)) {
			if(this.safeMove(DOWN, this)) {
				this.setRotate(90);
			}
		} else if(this.getDirection().equals(Character.UP)) {
			if(this.safeMove(UP, this)) {
				this.setRotate(270);
			}
		}
		
		if(this.getIntersectingObjects(RegFood.class).size() != 0) {
			RegFood food = this.getOneIntersectingObject(RegFood.class);
			food.onEat();
		} else if(this.getIntersectingObjects(EatGhostPowerUp.class).size() != 0) {
			EatGhostPowerUp food = this.getOneIntersectingObject(EatGhostPowerUp.class);
			food.onEat();
		}
		 
		if(this.getIntersectingObjects(Ghost.class).size() != 0) {
			Ghost ghost = this.getOneIntersectingObject(Ghost.class);
			if(ghost.isEdible()) {
				int increment = (int) Math.pow(2, trackPoint);
				trackPoint++;
				getWorld().setScore(getWorld().getScore() + increment*200);
				getWorld().updateScore(getWorld().getScore());
				getWorld().remove(ghost);
			} else {
				System.out.println("GAME OVER");
				//getWorld().remove(this);
			}
		}
		
	}
	
	public static void setTrackPoint(int p) {
		trackPoint = p;
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
