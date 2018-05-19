

import javafx.scene.image.Image;

public class Clyde extends Ghost {

	public Clyde(int startingRow, int startingCol) {
		super(startingRow, startingCol);
		this.setImage(new Image("imgs/clyde.png"));
	}
	
	@Override
	public void act(long now) {
		super.act(now);
	}

}
