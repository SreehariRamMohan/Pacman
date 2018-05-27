import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Arrays;

public class RespawnEyes extends InvisibleActor {

	//stores the path to home.
	private ArrayList<int[]> pathToHome; 
	
	private int goalRow; 
	private int goalCol;
	
	//the type of ghost to spawn when the pacman finally gets home. 
	private String ghostToSpawn;
	
	
	public RespawnEyes(Image i, ArrayList<int[]> pathToHome, int currRow, int currCol, 
			int goalRow, int goalCol, String theseEyesBelongTo) {
		
		super(i);
		
		this.ghostToSpawn = theseEyesBelongTo;
		this.setX(currCol * Controller.CHARACTER_DIMS);
		this.setY(currRow * Controller.CHARACTER_DIMS);
		
		this.setSpeed(5);
		
		this.pathToHome = pathToHome;
		
		this.goalRow = goalRow;
		this.goalCol = goalCol;
		
		int[] initialPositions = Character.getRowCol(this.getX() + this.getWidth()/2, this.getY() + this.getHeight()/2);
		
		int currentRow = initialPositions[0];
		int currentCol = initialPositions[1];
		
		String nextDir = Ghost.getDirectionFromNode(pathToHome.get(0)[0], pathToHome.get(0)[1], currentRow, currentCol);		
		this.setDirection(nextDir);
	}
	
	@Override
	public void act(long now) {		
		int[] eyePos = Character.getRowCol(this.getX() + this.getWidth()/2, this.getY() + this.getHeight()/2);
		int currRow = eyePos[0];
		int currCol = eyePos[1];
		
		if((currRow == this.goalRow && currCol == this.goalCol) || pathToHome.size() == 0) { // we are at our destination (home)
			atHome();
			return;
		}
		if(currRow == pathToHome.get(0)[0] && 
				currCol == pathToHome.get(0)[1]) { //advance eyes one "step" and make them move.
						
			pathToHome.remove(0);
			
			String nextDir = Ghost.getDirectionFromNode(pathToHome.get(0)[0], pathToHome.get(0)[1], currRow, currCol);
			
			this.setDirection(nextDir);
			this.centerCharacterInCell();
			
			
		}
		this.safeMove(this.getDirection(), false);
	}
	
	private void atHome() {
		//we reached the home so we can delete the eyes and re-spawn

		//animation to fade in ghost
		String ts = this.getGhostToSpawn();
		Ghost spawnedGhost = null;
		
		//Create the type of ghost which died
		if(ts.equals("Blinky")) {
			spawnedGhost = new Blinky(goalRow, goalCol);			
		} else if(ts.equals("Clyde")) {
			spawnedGhost = new Clyde(goalRow, goalCol);			
		} else if(ts.equals("Inky")) {
			spawnedGhost = new Inky(goalRow, goalCol);			
		} else if(ts.equals("Pinky")) {
			spawnedGhost = new Pinky(goalRow, goalCol);
		}
		if(spawnedGhost == null) { //This should never happen if the eye object is used properly
			return;
		}
		
		//make the ghosts fade in.
		FadeTransition ghostFadeIn = new FadeTransition(Duration.millis(1000), spawnedGhost);
		ghostFadeIn.setFromValue(0);
		ghostFadeIn.setToValue(1);
		ghostFadeIn.play();
				
		spawnedGhost.setCoordinate(goalCol*Controller.CHARACTER_DIMS, goalRow*Controller.CHARACTER_DIMS);
		this.getWorld().getModel().setCharacterAt(goalRow, goalCol, spawnedGhost);	
		
		this.getWorld().storeGhost(spawnedGhost);
		this.getWorld().add(spawnedGhost);

		getWorld().remove(this);
		
	}

	public ArrayList<int[]> getPathHome() {
		return pathToHome;
	}

	public String getGhostToSpawn() {
		return ghostToSpawn;
	}

	public void setGhostToSpawn(String ghostToSpawn) {
		this.ghostToSpawn = ghostToSpawn;
	}
	
}
