import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Controller extends Application {

	private int NUM_GHOSTS = 5;
	private int SCREEN_HEIGHT = 300;
	private int SCREEN_WIDTH = 300;
	private int CHARACTER_DIMS = 30;

	private PacManWorld world;
	
	//characters
	Pacman pacman;
	
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		stage.setTitle("Pac Man");
		
		BorderPane root = new BorderPane();
		world = new PacManWorld();
		world.setPrefHeight(480);
		world.setPrefWidth(480);
		root.setCenter(world);
		
		Scene scene = new Scene(root);
		
		this.pacman = new Pacman();
		pacman.setX(200);
		pacman.setY(250);
		
		world.add(pacman);
		
		for(int i = 0; i < NUM_GHOSTS; i++) {
			Ghost g = new Ghost();
			g.setX(Math.random()*SCREEN_WIDTH);
			g.setY(Math.random()*SCREEN_HEIGHT);
			world.add(g);
		}
		
		setKeyboardEvent();
		
		
		
		
		world.start();
		
		stage.setScene(scene);
		stage.setMaxWidth(300);
		stage.setMaxHeight(300);
		stage.show();
	}
	
	public void setKeyboardEvent() {
		world.requestFocus();
		world.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				if(event.getCode() == KeyCode.RIGHT) {
					//move pacman right
					pacman.setRight();
				} else if(event.getCode() == KeyCode.LEFT) {
					pacman.setLeft();
				} else if(event.getCode() == KeyCode.DOWN) {
					pacman.setDown();
				} else if(event.getCode() == KeyCode.UP) {
					pacman.setUp();
				}
			}
			
		});
	}
	
	

}
