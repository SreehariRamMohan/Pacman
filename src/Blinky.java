

import javafx.scene.image.Image;

public class Blinky extends Ghost {

	public Blinky() {
		this.setImage(new Image("imgs/blinky.png"));
	}

	@Override
	public void act(long now) {
		super.act(now);
	}


}
