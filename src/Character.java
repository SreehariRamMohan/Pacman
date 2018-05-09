import java.util.Stack;

import javafx.scene.paint.Color;

public abstract class Character extends Actor {
	private double ERROR = 0.001;
	
	private String direction = null;
	private String previousDirection = null;
	
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
			
			System.out.println("Pacman is in the center");
			
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
		this.previousDirection = this.getDirection();
		this.direction = dir;
	}
	
	public void setUp() {
		this.previousDirection = this.getDirection();
		direction = UP;
	}
	
	public void setDown() {
		this.previousDirection = this.getDirection();
		direction = DOWN;
	}
	
	public void setLeft() {
		this.previousDirection = this.getDirection();
		direction = LEFT;
	}
	
	public void setRight() {
		this.previousDirection = this.getDirection();
		direction = RIGHT;
	}

	public void setCoordinate(double x, double y) {
		setX(x);
		setY(y);
	}

	public String getPreviousDirection() {
		return previousDirection;
	}

	public void setPreviousDirection(String previousDirection) {
		this.previousDirection = previousDirection;
	}
	
	
	
	
	
}
