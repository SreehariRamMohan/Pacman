import javafx.scene.paint.Color;

public abstract class Food {
	int x;
	int y;
	int points;
	Color c;
	
	public abstract void remove();
	
	public abstract void move(int newX, int newY);
	
}
