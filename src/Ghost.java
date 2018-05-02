import javafx.scene.paint.Color;

public class Ghost extends Character{

	private boolean isEdible;
		
	public Ghost(int x, int y, Color color) {
		super(x, y, color);
		
	}

	@Override
	public void move(int newX, int newY) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void die() {
		// TODO Auto-generated method stub
		
	}
	
	public void autoPlayNextMove() {
		//use AI to make the ghost's next move
	}
	
	public void changeState(boolean isEdible) {
		this.isEdible = isEdible;
		
		//change color of ghost as well here
		
	}
}
