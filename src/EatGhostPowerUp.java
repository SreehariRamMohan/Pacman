import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javafx.animation.Animation;
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
		((Pacman)this.getWorld().getPacman()).playPowerUpSound();
		
		List<Actor> list = getWorld().getGhosts();
		int size = list.size();
		
		for(int i=0; i<size; i++) {
			Ghost ghost = (Ghost) list.get(i);
			ghost.setEdible(true);
			ghost.setImage(new Image("imgs/edibleghost.png"));
		}
		
		this.setOpacity(0);
		
		EatGhostPowerUp myself = this;


		
		/*
		 * For first 3/4 of the time the ghosts are blue, then for the remaining 1/4 of the time they flash indicating that
		 * they will turn back to normal soon
		 */
		Timeline indicator = new Timeline();
		indicator.getKeyFrames().add(new KeyFrame(Duration.millis(0.75 * Pacman.GHOST_EDIBLE_SECONDS * 1000.0)));
		indicator.setOnFinished(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
	
				/*
				 * Start flashing the ghost
				 */				
				flashGhost();

				indicator.stop();				
			}
			
		});
		
		indicator.play();
		
		/*
		 * Indicates ghost will turn back to normal soon
		 */
		
		
		Timeline timeline = new Timeline();
		timeline.getKeyFrames().add(new KeyFrame(Duration.millis(Pacman.GHOST_EDIBLE_SECONDS * 1000)));
		timeline.setOnFinished(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
	
				//turn back to normal.
				
				System.out.println("Back to normal");
				
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
					ghost.setOpacity(1);
					Pacman.setTrackPoint(0);

				}

				timeline.stop();
				

				getWorld().remove(myself);
				
			}
			
		});
		
		timeline.play();
		
		
	}
	
	public void setHasEaten(boolean hasEaten) {
		this.hasEaten = hasEaten;
	}
	
	public void flashGhost() {
		List<Actor> list = getWorld().getGhosts();
		int size = list.size();
		for(int i=0; i<size; i++) {
			Ghost ghost = (Ghost) list.get(i);
			ghost.setImage(new Image("imgs/almostNormal.png"));
			
			int blinkOnTime = 100;
			int blinkOffTime = 50;
			
			KeyFrame blinkOn = new KeyFrame(Duration.millis(blinkOnTime), new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
					ghost.setOpacity(1);					
				}
				
			});
			
			KeyFrame blinkOff = new KeyFrame(Duration.millis(blinkOffTime), new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
					ghost.setOpacity(0.4);
				}
				
			});
			
			int oneCycleTime = blinkOnTime + blinkOffTime;
			
			double numCycle = (0.25 * Pacman.GHOST_EDIBLE_SECONDS * 1000)/oneCycleTime;
			
			Timeline ghostBlinkTimeline = new Timeline(blinkOn, blinkOff);
			ghostBlinkTimeline.setCycleCount((int) Math.floor(numCycle)); //better to underestimate cycles than underestimate, so we ensure
																		//that the ghosts stop blinking and turn back to normal correctly
			ghostBlinkTimeline.play();
		}
	}
	
	
}
