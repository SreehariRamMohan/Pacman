import javafx.scene.paint.Color;

public abstract class Character extends Actor {


	
	private double ERROR = 1;
	
	private String direction = null;
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
	
}
