import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
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
	
	//variables to store food particles and determine if the game is over
	int numFoodParticles;
	
	/**
	 * Fields to store the initial positions of Pacman & ghosts
	 */
	int[] pacmanInitialPosition = new int[2];
	ArrayList<int[]> ghostInitialPositions = new ArrayList<int[]>();
	
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
			e.printStackTrace();
		}
		String line = scanner.nextLine();
		String[] dims = line.split(" ");
		int width = Integer.parseInt(dims[0]);
		int height = Integer.parseInt(dims[1]);
		
		characters = new Actor[width/Controller.CHARACTER_DIMS][height/Controller.CHARACTER_DIMS];
		food = new Actor[width/Controller.CHARACTER_DIMS][height/Controller.CHARACTER_DIMS];
		
		this.world.setPrefWidth(width);
		this.world.setPrefHeight(height);
		
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
					pacmanInitialPosition[0] = row;
					pacmanInitialPosition[1] = col;
				} else if(arr[col].equals("G")) {
					characters[row][col] = new Ghost(row, col);
					world.storeGhost((Ghost) characters[row][col]);
					this.ghostInitialPositions.add(new int[] {row, col});
				} else if(arr[col].equals("B")) {
					characters[row][col] = new Blinky(row, col);
					world.storeGhost((Ghost) characters[row][col]);
					this.ghostInitialPositions.add(new int[] {row, col});
				} else if(arr[col].equals("I")) {
					characters[row][col] = new Inky(row, col);
					world.storeGhost((Ghost) characters[row][col]);
					this.ghostInitialPositions.add(new int[] {row, col});
				} 
				else if(arr[col].equals("C")) {
					characters[row][col] = new Clyde(row, col);
					world.storeGhost((Ghost) characters[row][col]);
					this.ghostInitialPositions.add(new int[] {row, col});
				} 
				else if(arr[col].equals("Y")) {
					characters[row][col] = new Pinky(row, col);
					world.storeGhost((Ghost) characters[row][col]);
					this.ghostInitialPositions.add(new int[] {row, col});
				} 
				else if(arr[col].equals("R")) {
					food[row][col] = new RegFood();
				} else if(arr[col].equals("U")) {
					food[row][col] = new EatGhostPowerUp();
				}
				
				if(characters[row][col] != null) {
					characters[row][col].setX(col*Controller.CHARACTER_DIMS);
					characters[row][col].setY(row*Controller.CHARACTER_DIMS);
					world.add(characters[row][col]);
				}
				
				if(food[row][col] != null) {
					numFoodParticles++;
					food[row][col].setX(col*Controller.CHARACTER_DIMS);
					food[row][col].setY(row*Controller.CHARACTER_DIMS);
					world.add(food[row][col]);
				}
 			}
			
				
			row++;
		}
		
		
	}
	
	public Actor objectAt(int row, int col) {
		if(row >= 0 && row < characters.length && col >= 0 && col < characters[row].length) {
			return characters[row][col];
		} else {
			return null ;	
		}
	}
	
	public void setCharacterAt(int row, int col, Character c) {
		if(row >= 0 && row < characters.length && col >= 0 && col < characters[row].length) {
			characters[row][col] = c;
		}
	}
	
	public void printModel() {
		
		System.out.println("----------------------------");

		
		for(int i = 0; i < this.characters.length; i++) {
			for(int j = 0; j < this.characters[0].length; j++) {
				if(characters[i][j] instanceof Wall) {
					System.out.print("W ");
				} else if(characters[i][j] instanceof Pacman) {
					System.out.print("P ");
				} else if(characters[i][j] instanceof Ghost) {
					System.out.print("G ");
					
				} else {
					System.out.print("  ");
				}
			}
			System.out.println();
		}
		
		System.out.println("----------------------------");
		
	}
	
	public void printFood() {

		System.out.println("----------------------------");


		for(int i = 0; i < this.food.length; i++) {
			for(int j = 0; j < this.food[0].length; j++) {
				if(food[i][j] instanceof RegFood) {
					System.out.print("R ");
				} else if(food[i][j] instanceof EatGhostPowerUp) {
					System.out.print("S ");
				} else {
					System.out.print("  ");
				}
			}
			System.out.println();
		}

		System.out.println("----------------------------");

	}
	
	public Food getFoodAt(int r, int c) {
		return (Food) food[r][c];
	}
	
	
	
	public int getNumRows() {
		return this.characters.length;
	}
	
	public int getNumCols() {
		return this.characters[0].length;
	}
	
	public void saveState() {
		
	}
	
	public int getNumFoodParticles() {
		return numFoodParticles;
	}
	
}
