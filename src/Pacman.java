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
			
			/*
			 * Collision Logic ~ needs more improvements but it does the job for now
			 * ~Sreehari
			 */
			
			//check if the position the Pacman Will Move to is a wall.
			int[] pos = Character.getRowCol(this.getX() + Controller.CHARACTER_DIMS, this.getY());
			int row = pos[0];
			int col = pos[1];
			this.setRotate(0);
			if((getWorld().getModel().objectAt(row, col) instanceof Wall) && this.getIntersectingObjects(Wall.class).size() != 0) {
				//hit wall, uh oh :(. STOP, position pacman just outside the wall!!
				Wall w = (Wall) getWorld().getModel().objectAt(row, col);
				this.setCoordinate(w.getX() - Controller.CHARACTER_DIMS, w.getY());
			} else {
				//safe to move since we won't hit a wall.
				this.move(getSpeed(), 0);
			}
			
		} else if(this.getDirection().equals(Character.LEFT)) {
			/*
			 * Collision Logic ~ needs more improvements but it does the job for now
			 * ~Sreehari
			 */
			
			//check if the position the Pacman Will Move to is a wall.
			int[] pos = Character.getRowCol(this.getX() - Controller.CHARACTER_DIMS, this.getY());
			int row = pos[0];
			int col = pos[1];
			this.setRotate(180);
			if((getWorld().getModel().objectAt(row, col) instanceof Wall) && this.getIntersectingObjects(Wall.class).size() != 0) {
				//hit wall, uh oh :(. STOP, position pacman just outside the wall!!
				Wall w = (Wall) getWorld().getModel().objectAt(row, col);
				this.setCoordinate(w.getX() + Controller.CHARACTER_DIMS, w.getY());
			} else {
				//safe to move since we won't hit a wall.
				this.move(-getSpeed(), 0);
			}
		} else if(this.getDirection().equals(Character.DOWN)) {
			/*
			 * Collision Logic ~ needs more improvements but it does the job for now
			 * ~Sreehari
			 */
			
			//check if the position the Pacman Will Move to is a wall.
			int[] pos = Character.getRowCol(this.getX(), this.getY() + Controller.CHARACTER_DIMS);
			int row = pos[0];
			int col = pos[1];
			this.setRotate(90);
			if((getWorld().getModel().objectAt(row, col) instanceof Wall) && this.getIntersectingObjects(Wall.class).size() != 0) {
				//hit wall, uh oh :(. STOP, position pacman just outside the wall!!
				Wall w = (Wall) getWorld().getModel().objectAt(row, col);
				this.setCoordinate(w.getX(), w.getY() - Controller.CHARACTER_DIMS);
			} else {
				//safe to move since we won't hit a wall.
				this.move(0, getSpeed());
			}
		} else if(this.getDirection().equals(Character.UP)) {
			/*
			 * Collision Logic ~ needs more improvements but it does the job for now
			 * ~Sreehari
			 */
			
			//check if the position the Pacman Will Move to is a wall.
			int[] pos = Character.getRowCol(this.getX(), this.getY() - Controller.CHARACTER_DIMS);
			int row = pos[0];
			int col = pos[1];
			this.setRotate(270);
			if((getWorld().getModel().objectAt(row, col) instanceof Wall) && this.getIntersectingObjects(Wall.class).size() != 0) {
				//hit wall, uh oh :(. STOP, position pacman just outside the wall!!
				Wall w = (Wall) getWorld().getModel().objectAt(row, col);
				this.setCoordinate(w.getX(), w.getY() + Controller.CHARACTER_DIMS);
			} else {
				//safe to move since we won't hit a wall.
				this.move(0, -getSpeed());
			}
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
