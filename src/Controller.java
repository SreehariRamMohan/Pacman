import java.io.File;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Controller extends Application {

	public int NUM_GHOSTS = 5;
	public static int SCREEN_HEIGHT = 300;
	public static int SCREEN_WIDTH = 300;
	public static int CHARACTER_DIMS = 30;
	private PacManWorld world;
	
	private Text scoreText;
	private Text liveText;
	
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
	
		BorderPane topBox = new BorderPane();
		scoreText = new Text("Score: 0");
		liveText = new Text("Lives: 3");
		world.setScoreText(scoreText);
		world.setLiveText(liveText);
		
		
		scoreText.setFont(Font.font(24));
		liveText.setFont(Font.font(24));
		
		topBox.setLeft(scoreText);
		topBox.setRight(liveText);
		
		root.setTop(topBox);
		
		setKeyboardEvent();
		
		Model m = new Model(new File("map3.txt"), world);
		world.setModel(m);
		
		world.start();
		
		stage.setScene(scene);
		stage.setMaxWidth(630);
		stage.setMaxHeight(680);
		stage.show();
	}
	
	public void setKeyboardEvent() {
		world.requestFocus();
		world.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
			
				if(event.getCode() == KeyCode.UP){
					((Pacman)world.getPacman()).queueTurn("UP");
				}
				else if(event.getCode() == KeyCode.DOWN) {
					((Pacman)world.getPacman()).queueTurn("DOWN");
				}
				else if(event.getCode() == KeyCode.LEFT) {
					((Pacman)world.getPacman()).queueTurn("LEFT");
				}
				else if(event.getCode() == KeyCode.RIGHT) {
					((Pacman)world.getPacman()).queueTurn("RIGHT");
				}
				
			}
			
		});
	}
	
	

}
