
import javafx.scene.image.Image;

public class Pinky extends Ghost {
	
	public Pinky(int startingRow, int startingCol) {
		super(startingRow, startingCol, Controller.PINKY_RESET);		
		this.setImage(new Image("imgs/pinky.png"));
	}
	
	@Override
	public void act(long now) {
		super.act(now);
	}

}
