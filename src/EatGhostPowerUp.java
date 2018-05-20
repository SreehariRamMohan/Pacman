import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javafx.scene.image.Image;

public class EatGhostPowerUp extends Food {

	public EatGhostPowerUp() {
		this.setImage(new Image("imgs/powerup.png"));
	}
	
	@Override
	public void onEat() {
		List<Actor> list = getWorld().getGhosts();
		int size = list.size();
		this.getWorld().remove(this);
		
		
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				for(int i=0; i<size; i++) {
					
					Ghost ghost = (Ghost) list.get(i);
					if(ghost instanceof Blinky) {
						ghost.setImage(new Image("imgs/blinky.png"));
					} else if(ghost instanceof Inky) {
						ghost.setImage(new Image("imgs/inky.png"));
					} else if(ghost instanceof Pinky) {
						ghost.setImage(new Image("imgs/pinky.png"));
					} else if(ghost instanceof Clyde) {
						ghost.setImage(new Image("imgs/clyde.png"));
					}
					
					ghost.toggleEdible();
					Pacman.setTrackPoint(0);
				}
			}
			
		}, 6000);
		
		
		for(int i=0; i<size; i++) {
			Ghost ghost = (Ghost) list.get(i);
			ghost.toggleEdible();
			ghost.setImage(new Image("imgs/edibleghost.png"));
		}
		
		 
		
	}
	
}
