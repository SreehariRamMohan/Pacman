import java.io.File;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class SoundUtils {
	
	
	public static void playPacmanDeath() {
		
		Media deathSound = new Media(new File("src/sounds/pacman_death.wav").toURI().toString());
		MediaPlayer deathPlayer = new MediaPlayer(deathSound);
		
		deathPlayer.play();
		deathPlayer.setOnEndOfMedia(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				deathPlayer.dispose();

			}
			
		});
		
	}
	
}
