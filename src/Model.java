import java.io.File;

public class Model {
	
	private int horizontalCells;
	private int verticalCells;
	
	private static final Actor EMPTY_CELL = null;
	
	//array to store walls, ghosts, and pacman
	private Actor[][] characters;
	
	//array to store food, and powerups
	private Actor[][] food;
	
	public Model(File f) {
		
		//read horizontal, and vertical cells from the .txt file.
		//read in pacman location, ghost location, wall locations
		loadMapFromFile(f);
		
		
	}

	/**
	 * This method loads the game map from the current file.
	 * 
	 * @param f the file location we want to load from
	 */
	public void loadMapFromFile(File f) {
		
	}
	
	public void saveState() {
		
	}
}
