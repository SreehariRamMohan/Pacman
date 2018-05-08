import javafx.scene.paint.Color;

public abstract class Character extends Actor {

	private int x;
	private int y;
	private String direction;
	private int speed;
	
	public final String up = "UP";
	public final String down = "DOWN";
	public final String left = "LEFT";
	public final String right = "RIGHT";
	
	@Override
	public void act(long now) {
		// TODO Auto-generated method stub
	}
	
	public void die() {
		getWorld().remove(this);
	}
	
	public void moveTo(int x, int y) {
		this.x = x;
		this.y = y;
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
		direction = up;
	}
	
	public void setDown() {
		direction = down;
	}
	
	public void setLeft() {
		direction = left;
	}
	
	public void setRight() {
		direction = right;
	}
	
}
