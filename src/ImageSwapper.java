import javafx.animation.Transition;
import javafx.scene.image.Image;

public class ImageSwapper extends Transition {

	private Character toAnimate;
	private String imageName;
	
	public ImageSwapper(Pacman c, String imageName) {
		this.toAnimate = c;
		this.imageName = imageName;
	}
	
	@Override
	protected void interpolate(double frac) {
		System.out.println("changed image to " + imageName);
		
		this.toAnimate.setImage(new Image(imageName));
	}

}
