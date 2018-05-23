import javafx.animation.Transition;
import javafx.scene.image.Image;
import javafx.util.Duration;

public class ImageSwapper extends Transition {

	private Character toAnimate;
	private String imageName;

	public ImageSwapper(Pacman c, String imageName) {
		this.toAnimate = c;
		this.imageName = imageName;

		this.setCycleDuration(Duration.millis(100));
	}

	@Override
	protected void interpolate(double frac) {
		this.toAnimate.setRotate(0);
		this.toAnimate.setImage(new Image(imageName));
	}

}
