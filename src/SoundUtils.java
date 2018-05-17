import java.io.File;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class SoundUtils {
	
	private static Media deathSound = new Media(new File("src/sounds/pacman_death.wav").toURI().toString());
	private static MediaPlayer deathPlayer = new MediaPlayer(deathSound);
	
	public static void playPacmanDeath() {
		
		deathPlayer.play();
		
		
	}
	
}
