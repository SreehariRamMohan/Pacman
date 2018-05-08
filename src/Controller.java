import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Controller extends Application {

	private int NUM_GHOSTS = 5;
	private int SCREEN_HEIGHT = 480;
	private int SCREEN_WIDTH = 480;
	private int CHARACTER_DIMS = 15;
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		stage.setTitle("Pac Man");
		
		BorderPane root = new BorderPane();
		PacManWorld world = new PacManWorld();
		world.setPrefHeight(480);
		world.setPrefWidth(480);
		root.setCenter(world);
		
		Scene scene = new Scene(root);
		
		Pacman pacman = new Pacman();
		pacman.setX(200);
		pacman.setY(250);
		
		world.add(pacman);
		
		for(int i = 0; i < NUM_GHOSTS; i++) {
			Ghost g = new Ghost();
			g.setX(Math.random()*SCREEN_WIDTH);
			g.setY(Math.random()*SCREEN_HEIGHT);
			world.add(g);
		}
		
		world.start();
		
		stage.setScene(scene);
		stage.show();
		
		
		
		
		
		
	}
	
	

}
