import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

public class Model {
	
	private int horizontalCells;
	private int verticalCells;
	
	PacManWorld world;
	
	private static final Actor EMPTY_CELL = null;
	
	//array to store walls, ghosts, and pacman
	private Actor[][] characters;
	
	//array to store food, and powerups
	private Actor[][] food;
	
	public Model(File f, PacManWorld world) {
		
		//read horizontal, and vertical cells from the .txt file.
		//read in pacman location, ghost location, wall locations		
		characters = new Actor[10][10];
		food = new Actor[10][10];
		this.world = world;
		loadMapFromFile(f);
		
		
	}

	/**
	 * This method loads the game map from the current file.
	 * 
	 * @param f the file location we want to load from
	 */
	public void loadMapFromFile(File f) {
		Scanner scanner = null;
		try {
			scanner = new Scanner(f);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String line = scanner.nextLine();
		String[] dims = line.split(" ");
		int width = Integer.parseInt(dims[0]);
		int height = Integer.parseInt(dims[1]);
		System.out.println("Width is " + width + " and height is " + height);
		
		int row = 0;
		while(scanner.hasNextLine()) {
			String rows = scanner.nextLine();
			String[] arr = rows.split("");
			System.out.println(Arrays.toString(arr));
			
			for(int col = 0; col < arr.length; col++) {
				if(arr[col] == null) {
					//cell is empty
				} else if(arr[col].equals("|") || arr[col].equals("-")) {
					//wall here
					characters[row][col] = new Wall();;
				} else if(arr[col].equals("P")) {
					characters[row][col] = new Pacman();
					world.setPacman(characters[row][col]);
					
				} else if(arr[col].equals("G")) {
					characters[row][col] = new Ghost();
					world.storeGhost(characters[row][col]);
				}
				
				if(characters[row][col] != null) {
					characters[row][col].setX(col*Controller.CHARACTER_DIMS);
					characters[row][col].setY(row*Controller.CHARACTER_DIMS);
					world.add(characters[row][col]);
				}
 			}
			
				
			row++;
		}
		
		
	}
	
	public void saveState() {
		
	}
}
