import java.util.Stack;
import javafx.scene.paint.Color;

public abstract class Character extends Actor {
	private double ERROR = 3;
	
	private String direction = STATIONARY;
	
	//these fields store the next turn the player has pressed so we can use adaptive turning. 
	private boolean turnInQue = false;
	private String quedTurn = Character.STATIONARY;
	
	//number of pixels the character moves each act.
	private int speed;
	
	//constants for turning (used for consistency).
	public static final String UP = "UP";
	public static final String DOWN = "DOWN";
	public static final String LEFT = "LEFT";
	public static final String RIGHT = "RIGHT";
	public static final String STATIONARY = "STATIONARY";
	
	public static boolean isPaused = true;
	
	@Override
	public void act(long now) {
		
		if(isPaused) {
			return;
		}
	}
	
	public void die() {
		getWorld().remove(this);
	}
	
	
	/**
	 * Returns the row, and column of the character
	 * Precondition: x, y represent the CENTER of the character. 
	 * @param x the x coordinate of the character
	 * @param y the y coordinate of the character
	 * @return an integer array with the first index being the row, and the second index being the column of the character
	 */
	public static int[] getRowCol(double x, double y) {
		int col = (int) (x/Controller.CHARACTER_DIMS);
		int row = (int) (y/Controller.CHARACTER_DIMS);
		return new int[] {row, col};
	}
	
	/**
	 * Determines whether the character is in the center
	 * @return boolean determining whether this character is in the center
	 */
	public boolean isInCenter() {
		double x = this.getX();
		double y = this.getY();		
				
		
		if( Math.abs((((x + this.getWidth()/2)%Controller.CHARACTER_DIMS) - Controller.CHARACTER_DIMS/2)) <= ERROR && 
				Math.abs((((y + this.getHeight()/2)%Controller.CHARACTER_DIMS) - Controller.CHARACTER_DIMS/2)) <= ERROR ) {
				
			//we need to re-center accurately
			int[] pos = Character.getRowCol(this.getX() + this.getWidth()/2, this.getY() + this.getHeight()/2);
			
			int row = pos[0];
			int col = pos[1];
					
			//auto-set character to center
			this.setCoordinate(col*Controller.CHARACTER_DIMS, row*Controller.CHARACTER_DIMS);
						
			return true;
		} else {
			return false;
		}
		
	}
	

	/**
	 * A method which centers the given character in its current cell.
	 * Precondition: The character is Controller.CHARACTER_DIMS wide and tall.
	 */
	public void centerCharacterInCell() {
		int[] pos = Character.getRowCol(this.getX() + this.getWidth()/2, this.getY() + this.getHeight()/2);
		int row = pos[0];
		int col = pos[1];
		this.setCoordinate(col*Controller.CHARACTER_DIMS, row*Controller.CHARACTER_DIMS);
	}
	
	

	/**
	 * Convenience method to set x and y
	 * @param x
	 * @param y
	 */
	public void setCoordinate(double x, double y) {
		setX(x);
		setY(y);
	}

	
	/**
	 * Determines whether the character can move in the direction specified going its speed.
	 * Checks for walls, and other objects within this characters speed.
	 * @param direction the direction the character wants to go next
	 * @return whether the path ahead is clear within speed pixels.
	 */
	public boolean canMove(String direction) {
		
		if(direction.equals(STATIONARY)) {
			return false;
		}
		
		int[] pos = getFutureRowColFromDirection(direction);
		int row = pos[0];
		int col = pos[1];
		//wall is blocking our path
		if((getWorld().getModel().objectAt(row, col) instanceof Wall)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns the [row, col] array of the pacman's position after it has moved speed pixels.
	 * @param direction
	 * @return
	 */
	public int[] getFutureRowColFromDirection(String direction) {		
		int[] pos = null;
	
		if(direction.equals(Character.UP)) {
			pos = Character.getRowCol(this.getX() + this.getWidth()/2, this.getY() - this.getSpeed());
		} else if(direction.equals(Character.DOWN)) {
			pos = Character.getRowCol(this.getX() + this.getWidth()/2, this.getY() + Controller.CHARACTER_DIMS + this.getSpeed());
		} else if(direction.equals(Character.LEFT)) {
			pos = Character.getRowCol(this.getX() - getSpeed(), this.getY() + this.getHeight()/2);
		} else if(direction.equals(Character.RIGHT)){ //direction = RIGHT
			pos = Character.getRowCol(this.getX() + Controller.CHARACTER_DIMS + this.getSpeed(), this.getY() + this.getHeight()/2 );
		}  else if(direction.equals(Character.STATIONARY)) {
			pos = Character.getRowCol(this.getX(), this.getY());
		}
		
		return pos;
	}
	
	/**
	 * Determines whether the character can move in the direction specified going its speed.
	 * Checks for walls, and other objects within this characters speed.
	 * @param direction the direction the character wants to go next
	 * @return whether the path ahead is clear within speed pixels.
	 */
	public boolean canMoveForGhosts(String direction) {
		
		if(direction.equals(STATIONARY)) {
			return false;
		}
		
		int[] pos = getFutureRowColFromDirectionForGhosts(direction);
		int row = pos[0];
		int col = pos[1];
		//wall is blocking our path
		if((getWorld().getModel().objectAt(row, col) instanceof Wall)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns the [row, col] array of the pacman's position after it has moved speed pixels.
	 * @param direction
	 * @return
	 */
	public int[] getFutureRowColFromDirectionForGhosts(String direction) {		
		int[] pos = null;
	
		if(direction.equals(Character.UP)) {
			pos = Character.getRowCol(this.getX(), this.getY() - this.getSpeed());
		} else if(direction.equals(Character.DOWN)) {
			pos = Character.getRowCol(this.getX(), this.getY() + Controller.CHARACTER_DIMS + this.getSpeed());
		} else if(direction.equals(Character.LEFT)) {
			pos = Character.getRowCol(this.getX() - getSpeed(), this.getY());
		} else if(direction.equals(Character.RIGHT)){ //direction = RIGHT
			pos = Character.getRowCol(this.getX() + Controller.CHARACTER_DIMS + this.getSpeed(), this.getY());
		}  else if(direction.equals(Character.STATIONARY)) {
			pos = Character.getRowCol(this.getX(), this.getY());
		}
		
		return pos;
	}
	
	/**
	 * The main movement method for all characters (ghosts & Pacman)
	 * Essentially this method moves the character on screen & also updates it's position in the model if necessary. 
	 * 
	 * Precondition: canMove() was called prior to safeMove() so the path in direction is clear.
	 * Poscondition: character would have been moved in direction by it's speed.
	 * @param direction
	 */
	public void safeMove(String direction) {
		
		
		int[] oldPos = Character.getRowCol(this.getX(), this.getY());
		int oldRow = oldPos[0];
		int oldCol = oldPos[1];
		
		int[] pos = getFutureRowColFromDirection(direction);
		int row = pos[0];
		int col = pos[1];
		
		if(direction.equals(Character.UP)) {
			this.move(0, -getSpeed());
		} else if(direction.equals(Character.DOWN)) {
			this.move(0, getSpeed());
		} else if(direction.equals(Character.LEFT)) {
			this.move(-getSpeed(), 0);
		} else if(direction.equals(Character.RIGHT)) { 
			this.move(getSpeed(), 0);
		} 
		
		//update the sprite position in the 2D array
		if(row != oldRow || col != oldCol) {
			getWorld().getModel().setCharacterAt(row, col, this);
			getWorld().getModel().setCharacterAt(oldRow, oldCol, null);
		}		
	}
	
	/**
	 * Similar to safeMove(String direction) however this method has the option of not updating, this is useful for objects we don't 
	 * want in model such as the Eyes.
	 * 
	 * Precondition: canMove() was called prior to safeMove() so the path in direction is clear.
	 * Poscondition: character would have been moved in direction by it's speed.
	 * @param direction
	 */
	public void safeMove(String direction, boolean updateInModel) {
		if(updateInModel) {
			safeMove(direction);
			return;
		}
		
		if(direction.equals(Character.UP)) {
			this.move(0, -getSpeed());
		} else if(direction.equals(Character.DOWN)) {
			this.move(0, getSpeed());
		} else if(direction.equals(Character.LEFT)) {
			this.move(-getSpeed(), 0);
		} else if(direction.equals(Character.RIGHT)) { 
			this.move(getSpeed(), 0);
		} 
	}
	
	/**
	 * Put a turn in the queue to be used when a spot opens up.
	 * @param dir
	 */
	public void queueTurn(String dir) {
		this.turnInQue = true;
		this.quedTurn = dir;
	}
	
	/**
	 * Determines if a turn is in the queue.
	 * @return
	 */
	public boolean hasQueue() {
		return turnInQue;
	}
	
	/**
	 * Precondition: a call to hasQueue() returns true;
	 */
	public String getQueuedDirection() {
		return this.quedTurn;
	}
	
	public void removeQueuedDirection() {
		this.turnInQue = false;
	}
	
	/**
	 * LEGACY METHOD. 
	 * Determines whether pacman is out of bounds.
	 * @return
	 */
	public boolean isPacManOutOfBounds() {
		Pacman p = (Pacman) this.getWorld().getPacman();
		return p.getX() >= getWorld().getWidth() || (p.getX()) <= 0;
	}
	
	public boolean isGhostOutOfBounds(Ghost g) {
		return g.getX() >= getWorld().getWidth() || (g.getX()) <= 0;
	}
	
	public boolean isStationary() {
		return this.getDirection().equals(Character.STATIONARY);
	}
	
	/*
	 * Getters/Setter below.
	 */
	
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

}
