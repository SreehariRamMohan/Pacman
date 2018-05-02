import javafx.scene.paint.Color;

public class PacMan extends Character implements Detectable {

	private int lives;
	private int foodEaten;
	
	
	public PacMan(int x, int y, Color color) {
		super(x, y, color);
	}

	@Override
	public void move(int newX, int newY) {
		
	}

	@Override
	public void die() {
		
	}

	
	
	//Detection Methods for PacMan to know when it was touched by another character
	
	@Override
	public void onDetect(Character c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDetect(Food f) {
		// TODO Auto-generated method stub
		
	}
}
