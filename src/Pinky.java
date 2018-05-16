import javafx.scene.image.Image;

public class Pinky extends Ghost {
	
	public Pinky() {
		this.setImage(new Image("imgs/pinky.png"));
	}
	
	@Override
	public void act(long now) {
		if(isAutoPlay) {
			autoPlayNextMove();
		}
	}

}
