import java.util.Stack;

import javafx.scene.paint.Color;

public abstract class Character extends Actor {
	private double ERROR = 0.001;
	
	private String direction = null;
	
	private boolean turnInQue = false;
	private String quedTurn;
	
	private int speed;
	
	public static final String UP = "UP";
	public static final String DOWN = "DOWN";
	public static final String LEFT = "LEFT";
	public static final String RIGHT = "RIGHT";
	
	@Override
	public void act(long now) {
		// TODO Auto-generated method stub
	}
	
	public void die() {
		getWorld().remove(this);
	}
	
	public int[] getRowCol() {
		int col = (int) (this.getX()/Controller.CHARACTER_DIMS);
		int row = (int) (this.getY()/Controller.CHARACTER_DIMS);
		return new int[] {row, col};
	}
	
	public static int[] getRowCol(double x, double y) {
		int col = (int) (x/Controller.CHARACTER_DIMS);
		int row = (int) (y/Controller.CHARACTER_DIMS);
		return new int[] {row, col};
	}
	
	public boolean isInCenter() {
		double x = this.getX();
		double y = this.getY();
		
		if( (((x + this.getWidth()/2)%Controller.CHARACTER_DIMS) - Controller.CHARACTER_DIMS/2) < ERROR && (((y + this.getHeight()/2)%Controller.CHARACTER_DIMS) - Controller.CHARACTER_DIMS/2) < ERROR ) {
			
			int col = (int) (this.getX()/Controller.CHARACTER_DIMS);
			int row = (int) (this.getY()/Controller.CHARACTER_DIMS);
			
			this.setCoordinate(col*Controller.CHARACTER_DIMS, row*Controller.CHARACTER_DIMS);
						
			return true;
		} else {
			return false;
		}
		
	}
	
	public int getSpeed() {
		return speed;
	}
	
	public void setSpeed(int speed) {
		this.speed = speed;
	}
	
	public String getDirection() {
		return direction;
	}
	
	public void setDirection(String dir) {
		this.direction = dir;
	}
	
	public void setUp() {
		direction = UP;
	}
	
	public void setDown() {
		direction = DOWN;
	}
	
	public void setLeft() {
		direction = LEFT;
	}
	
	public void setRight() {
		direction = RIGHT;
	}

	public void setCoordinate(double x, double y) {
		setX(x);
		setY(y);
	}

	
	
	public boolean safeMove(String direction, Character c) {
		int[] pos = null;
		if(direction.equals(Character.UP)) {
			pos = Character.getRowCol(this.getX(), this.getY() - this.getSpeed());
		} else if(direction.equals(Character.DOWN)) {
			pos = Character.getRowCol(this.getX(), this.getY() + Controller.CHARACTER_DIMS);
		} else if(direction.equals(Character.LEFT)) {
			pos = Character.getRowCol(this.getX() - getSpeed(), this.getY() );
		} else { //direction = RIGHT
			pos = Character.getRowCol(this.getX() + Controller.CHARACTER_DIMS, this.getY() );
		}
		
		int row = pos[0];
		int col = pos[1];
		
		if((getWorld().getModel().objectAt(row, col) instanceof Wall)) {
			//hit wall, uh oh :(. STOP, position pacman just outside the wall!!
			Wall w = (Wall) getWorld().getModel().objectAt(row, col);
			
			if(direction.equals(Character.UP)) {
				this.setCoordinate(w.getX(), w.getY() + Controller.CHARACTER_DIMS);
			} else if(direction.equals(Character.DOWN)) {
				this.setCoordinate(w.getX(), w.getY() - Controller.CHARACTER_DIMS);
			} else if(direction.equals(Character.LEFT)) {
				this.setCoordinate(w.getX() + Controller.CHARACTER_DIMS, w.getY());
			} else { //direction = RIGHT
				this.setCoordinate(w.getX() - Controller.CHARACTER_DIMS, w.getY());
			}
			
			return false;

			
		} else {
			//safe to move since we won't hit a wall.
			
			if(direction.equals(Character.UP)) {
				this.move(0, -getSpeed());
			} else if(direction.equals(Character.DOWN)) {
				this.move(0, getSpeed());
			} else if(direction.equals(Character.LEFT)) {
				this.move(-getSpeed(), 0);
			} else { //direction = RIGHT
				this.move(getSpeed(), 0);
			}	
			return true;
		}
	}
	
	public void queTurn(String dir) {
		this.turnInQue = true;
		this.quedTurn = dir;
	}
	
	public boolean hasQue() {
		return turnInQue;
	}
	
	public String getQuedDir() {
		return this.quedTurn;
	}
	
	public void removeQuedTurn() {
		this.turnInQue = false;
	}
	
	
}
