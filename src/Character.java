import java.util.Stack;
import javafx.scene.paint.Color;

public abstract class Character extends Actor {
	private double ERROR = 3;
	
	private String direction = STATIONARY;
	
	private boolean turnInQue = false;
	private String quedTurn = Character.STATIONARY;
	
	private int speed;
	
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
	
	
	public static int[] getRowCol(double x, double y) {
		int col = (int) (x/Controller.CHARACTER_DIMS);
		int row = (int) (y/Controller.CHARACTER_DIMS);
		return new int[] {row, col};
	}
	
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
	
	public void centerGhostInCell() {
		int[] pos = Character.getRowCol(this.getX() + this.getWidth()/2, this.getY() + this.getHeight()/2);
		int row = pos[0];
		int col = pos[1];
		this.setCoordinate(col*Controller.CHARACTER_DIMS, row*Controller.CHARACTER_DIMS);
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
	
	public boolean isStationary() {
		return this.getDirection().equals(Character.STATIONARY);
	}

	
	//checks for walls and other stuff to make sure the character can make a move in the direction
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
	
	
	public void moveInDirectionBy(String direction, int by) {

		if(direction.equals(Character.UP)) {
			this.setCoordinate(this.getX(), this.getY() - by);
		} else if(direction.equals(Character.DOWN)) {
			this.setCoordinate(this.getX(), this.getY() + by);
		} else if(direction.equals(Character.LEFT)) {
			this.setCoordinate(this.getX() - by, this.getY());
		} else if(direction.equals(Character.RIGHT)){ //direction = RIGHT
			this.setCoordinate(this.getX() + by, this.getY());
		}  else if(direction.equals(Character.STATIONARY)) {
			this.setCoordinate(this.getX(), this.getY());
		}
	}
	
	/**
	 * Precondition: canMove() was called prior to safeMove() so the path in direction is clear.
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
	
	public void queueTurn(String dir) {
		this.turnInQue = true;
		this.quedTurn = dir;
	}
	
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
	
	public boolean isPacManOutOfBounds() {
		Pacman p = (Pacman) this.getWorld().getPacman();
		return p.getX() >= getWorld().getWidth() || (p.getX()) <= 0;
	}
	
	public boolean isGhostOutOfBounds(Ghost g) {
		return g.getX() >= getWorld().getWidth() || (g.getX()) <= 0;
	}
}
