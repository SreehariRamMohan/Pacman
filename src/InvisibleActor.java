import javafx.scene.image.Image;

/*
 * This class is meant for actors which need to display information
 * such as a score/ or ghost eyes which move back to the start but we
 * don't want them to be solid. In other words, other objects should be able to go through them
 */

public class InvisibleActor extends Character {
	
	public InvisibleActor(Image i) {
		this.setImage(i);
	}
	
	@Override
	public void act(long now) {
		// TODO Auto-generated method stub
		
	}

}
