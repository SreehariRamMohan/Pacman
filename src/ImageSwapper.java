import javafx.animation.Transition;
import javafx.scene.image.Image;
import javafx.util.Duration;

/**
 * This class is used as a custom transition for the ghost death animation to allow us to swap an image of the pacman.
 * We can then put this transition in a timeline for easy use.
 */
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
