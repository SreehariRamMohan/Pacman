
import javafx.scene.image.Image;

public class Inky extends Ghost {

	public Inky(int startingRow, int startingCol) {		
		super(startingRow, startingCol, Controller.INKY_RESET);
		this.setImage(new Image("imgs/inky.png"));
	}
	
	@Override
	public void act(long now) {
		super.act(now);
	}

}
