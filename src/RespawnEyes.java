import javafx.scene.image.Image;
import java.util.ArrayList;
import java.util.Arrays;

public class RespawnEyes extends InvisibleActor {

	private ArrayList<int[]> pathToHome; 
	
	private int goalRow; 
	private int goalCol;
	
	public RespawnEyes(Image i, ArrayList<int[]> pathToHome, int currRow, int currCol, int goalRow, int goalCol) {
		super(i);
		
		
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
		
		if(currRow == this.goalRow && currCol == this.goalCol) {
			//we reached the home so we can delete the eyes and re-spawn
			this.getWorld().remove(this);
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
	
	public ArrayList<int[]> getPathHome() {
		return pathToHome;
	}

}
