import java.io.File;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Controller extends Application {

	public int NUM_GHOSTS = 5;
	public static int SCREEN_HEIGHT = 300;
	public static int SCREEN_WIDTH = 300;
	public static int CHARACTER_DIMS = 30;

	private PacManWorld world;
	
	//characters
	
	
	
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		stage.setTitle("Pac Man");
		
		BorderPane root = new BorderPane();
		world = new PacManWorld();
		world.setPrefHeight(300);
		world.setPrefWidth(300);
		root.setCenter(world);
		
		Scene scene = new Scene(root);
	
		
		setKeyboardEvent();
		
		Model m = new Model(new File("map.txt"), world);
		world.setModel(m);
		
		world.start();
		
		stage.setScene(scene);
		stage.setMaxWidth(300);
		stage.setMaxHeight(350);
		stage.show();
	}
	
	public void setKeyboardEvent() {
		world.requestFocus();
		world.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				
				/*if(!((Pacman)world.getPacman()).isInCenter()) {
					return;
				}
				if(event.getCode() == KeyCode.RIGHT) {
					//move pacman right
					((Pacman) world.getPacman()).setRight();
				} else if(event.getCode() == KeyCode.LEFT) {
					((Pacman) world.getPacman()).setLeft();

				} else if(event.getCode() == KeyCode.DOWN) {
					((Pacman) world.getPacman()).setDown();

				} else if(event.getCode() == KeyCode.UP) {
					((Pacman) world.getPacman()).setUp();

				}*/
				
				//System.out.println(((Pacman)world.getPacman()).getDirection());
				
				if(event.getCode() == KeyCode.RIGHT) {
					world.addKeyCode(KeyCode.RIGHT);
					world.removeKey(KeyCode.LEFT);
					world.removeKey(KeyCode.UP);
					world.removeKey(KeyCode.DOWN);
				} else if(event.getCode() == KeyCode.LEFT) {
					world.addKeyCode(KeyCode.LEFT);
					world.removeKey(KeyCode.RIGHT);
					world.removeKey(KeyCode.UP);
					world.removeKey(KeyCode.DOWN);
				} else if(event.getCode() == KeyCode.UP) {
					world.addKeyCode(KeyCode.UP);
					world.removeKey(KeyCode.LEFT);
					world.removeKey(KeyCode.RIGHT);
					world.removeKey(KeyCode.DOWN);
				} else if(event.getCode() == KeyCode.DOWN) {
					world.addKeyCode(KeyCode.DOWN);
					world.removeKey(KeyCode.LEFT);
					world.removeKey(KeyCode.UP);
					world.removeKey(KeyCode.RIGHT);
				}
				
//				if(((Pacman)world.getPacman()).getDirection() == "LEFT" ||
//						((Pacman)world.getPacman()).getDirection() == "RIGHT"){
//					if(world.isKeyDown(KeyCode.LEFT)){
//						((Pacman)world.getPacman()).setLeft();
//					}
//					if(world.isKeyDown(KeyCode.RIGHT)) {
//						((Pacman)world.getPacman()).setRight();
//					}
//				}
//				
//				else if(((Pacman)world.getPacman()).getDirection() == "UP" || 
//						((Pacman)world.getPacman()).getDirection() == "DOWN"){
//					if(world.isKeyDown(KeyCode.UP)) {
//						((Pacman)world.getPacman()).setUp();
//					}
//					if(world.isKeyDown(KeyCode.DOWN)) {
//						((Pacman)world.getPacman()).setDown();
//					}
//				}
				
				if(world.isKeyDown(KeyCode.UP)){
					if(((Pacman)world.getPacman()).isInCenter()) {
						((Pacman)world.getPacman()).setUp();
					}
				}
				else if(world.isKeyDown(KeyCode.DOWN)) {
					if(((Pacman)world.getPacman()).isInCenter()) {
						((Pacman)world.getPacman()).setDown();
					}
				}
				else if(world.isKeyDown(KeyCode.LEFT)) {
					if(((Pacman)world.getPacman()).isInCenter()) {
						((Pacman)world.getPacman()).setLeft();
					}
				}
				else if(world.isKeyDown(KeyCode.RIGHT)) {
					if(((Pacman)world.getPacman()).isInCenter()) {
						((Pacman)world.getPacman()).setRight();
					}
				}
				
			}
			
		});
	}
	
	

}
