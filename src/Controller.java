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
				if(event.getCode() == KeyCode.RIGHT) {
					//move pacman right
					((Pacman) world.getPacman()).setRight();
				} else if(event.getCode() == KeyCode.LEFT) {
					((Pacman) world.getPacman()).setLeft();
				} else if(event.getCode() == KeyCode.DOWN) {
					((Pacman) world.getPacman()).setDown();
				} else if(event.getCode() == KeyCode.UP) {
					((Pacman) world.getPacman()).setUp();
				}
			}
			
		});
	}
	
	

}
