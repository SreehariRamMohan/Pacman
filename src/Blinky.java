

import javafx.scene.image.Image;

public class Blinky extends Ghost {

	public Blinky(int startingRow, int startingCol) {
		super(startingRow, startingCol);
		this.setImage(new Image("imgs/blinky.png"));
	}

	@Override
	public void act(long now) {
		super.act(now);
	}


}
