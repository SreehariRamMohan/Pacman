import java.util.List;

import javafx.scene.image.Image;

public class EatGhostPowerUp extends Food {

	public EatGhostPowerUp() {
		this.setImage(new Image("imgs/powerup.png"));
	}
	
	@Override
	public void onEat() {
		List<Actor> list = getWorld().getGhosts();
		int size = list.size();
		System.out.println(size);
		remove();
		for(int i=0; i<size; i++) {
			Ghost ghost = (Ghost) list.get(i);
			ghost.toggleEdible();
			ghost.setImage(new Image("imgs/edibleghost.png"));
		}
		//add a buffer for around 10 seconds and then the ghosts revert back
		
		/**
		 * for(int i=0; i<getWorld().getGhosts().size(); i++) {
			Ghost ghost = (Ghost) list.get(i);
			ghost.toggleEdible();
			ghost.setImage(new Image("imgs/ghost.png"));
		}
		
		 */
		
	}
	
}
