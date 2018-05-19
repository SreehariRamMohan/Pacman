
import javafx.scene.image.Image;

public class Pinky extends Ghost {
	
	public Pinky() {
		System.out.println("Pinky created");
		
		this.setImage(new Image("imgs/pinky.png"));
	}
	
	@Override
	public void act(long now) {
		super.act(now);
	}

}
