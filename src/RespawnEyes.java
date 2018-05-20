import javafx.scene.image.Image;
import java.util.ArrayList;
import java.util.Arrays;

public class RespawnEyes extends InvisibleActor {

	private ArrayList<int[]> pathToHome; 
	
	private int goalRow; 
	private int goalCol;
	
	private String ghostToSpawn;
	
	
	public RespawnEyes(Image i, ArrayList<int[]> pathToHome, int currRow, int currCol, 
			int goalRow, int goalCol, String theseEyesBelongTo) {
		
		super(i);
		
		this.ghostToSpawn = theseEyesBelongTo;
		this.setX(currCol * Controller.CHARACTER_DIMS);
		this.setY(currRow * Controller.CHARACTER_DIMS);
		
		System.out.println("Eyes x = " + this.getX() + " y = " + this.getY());

		
		this.setSpeed(5);
		
		this.pathToHome = pathToHome;
		
		this.goalRow = goalRow;
		this.goalCol = goalCol;
		
		int[] initialPositions = Character.getRowCol(this.getX() + this.getWidth()/2, this.getY() + this.getHeight()/2);
		
		int currentRow = initialPositions[0];
		int currentCol = initialPositions[1];
		
		String nextDir = Ghost.getDirectionFromNode(pathToHome.get(0)[0], pathToHome.get(0)[1], currentRow, currentCol);
		
		System.out.println("Initial positions: " + Arrays.toString(initialPositions));
		
		System.out.println("Destination: " + Arrays.toString(pathToHome.get(0)));
		
		
		System.out.println("In constructor dir = " + nextDir);
		
		this.setDirection(nextDir);
		
		
	}
	
	@Override
	public void act(long now) {		

		
		int[] eyePos = Character.getRowCol(this.getX() + this.getWidth()/2, this.getY() + this.getHeight()/2);
		int currRow = eyePos[0];
		int currCol = eyePos[1];
		
		if((currRow == this.goalRow && currCol == this.goalCol) || pathToHome.size() == 0) {
			atHome();
			return;
		}
		if(currRow == pathToHome.get(0)[0] && 
				currCol == pathToHome.get(0)[1]) {
						
			pathToHome.remove(0);
			
			String nextDir = Ghost.getDirectionFromNode(pathToHome.get(0)[0], pathToHome.get(0)[1], currRow, currCol);
			System.out.println("act() - > Next dir = " + nextDir);
			
			this.setDirection(nextDir);
			this.centerCharacterInCell();
			
			
		}
		//this.move(0, -this.getSpeed());
		System.out.println("direction = " + this.getDirection());
		
		this.safeMove(this.getDirection(), false);

	}
	
	private void atHome() {
		//we reached the home so we can delete the eyes and re-spawn
		
		//animation to fade out
		
		
		//animation to fade in ghost
		String ts = this.getGhostToSpawn();
		Ghost spawnedGhost = null;
		
		System.out.println("Tring to spawn new ghost -> " + ts);
		
		if(ts.equals("Blinky")) {
			spawnedGhost = new Blinky(goalRow, goalCol);
			System.out.println("bliny created");
			
		} else if(ts.equals("Clyde")) {
			spawnedGhost = new Clyde(goalRow, goalCol);
			System.out.println("Clyde created");
			
		} else if(ts.equals("Inky")) {
			spawnedGhost = new Inky(goalRow, goalCol);
			System.out.println("Inky created");
			
		} else if(ts.equals("Pinky")) {
			spawnedGhost = new Pinky(goalRow, goalCol);
			System.out.println("Pinky created");
		}
		if(spawnedGhost == null) {
			return;
		}
				
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
