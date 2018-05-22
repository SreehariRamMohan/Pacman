import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.util.Duration;

public class EatGhostPowerUp extends Food {

	private boolean hasEaten = false;
	
	public EatGhostPowerUp() {
		this.setImage(new Image("imgs/powerup.png"));
	}
	
	@Override
	public void onEat() {
		
		
		if(hasEaten) {
			return;
		} else {
			hasEaten = true;
		}
		
		//update number of food particles pacman has eaten
		((Pacman)getWorld().getPacman()).setPacmanFoodParticlesEaten(((Pacman)getWorld().getPacman()).getPacmanFoodParticlesEaten() + 1);
		
		
		List<Actor> list = getWorld().getGhosts();
		int size = list.size();
		
		for(int i=0; i<size; i++) {
			Ghost ghost = (Ghost) list.get(i);
			ghost.setEdible(true);
			ghost.setImage(new Image("imgs/edibleghost.png"));
		}
		
		this.setOpacity(0);
//		this.getWorld().remove(this);
		

		Timeline timeline = new Timeline();
		timeline.getKeyFrames().add(new KeyFrame(Duration.millis(5000)));
		
		EatGhostPowerUp myself = this;
		
		timeline.setOnFinished(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
	
				List<Actor> list = getWorld().getGhosts();
				int size = list.size();
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
					
					ghost.setEdible(false);
					Pacman.setTrackPoint(0);

				}

				timeline.stop();

				getWorld().remove(myself);
				
			}
			
		});
		
		timeline.play();
		
		
	}
}
