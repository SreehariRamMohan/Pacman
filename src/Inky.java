import javafx.scene.image.Image;

public class Inky extends Ghost {

	public Inky() {
		this.setImage(new Image("imgs/inky.png"));
	}
	
	@Override
	public void act(long now) {
		if(isAutoPlay) {
			autoPlayNextMove();
		}
	}

}
