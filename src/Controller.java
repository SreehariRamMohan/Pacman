
import java.awt.Graphics;
import java.io.File;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Controller extends Application {

	public int NUM_GHOSTS = 5;
	public static int SCREEN_HEIGHT = 680;
	public static int SCREEN_WIDTH = 630;
	public static int CHARACTER_DIMS = 30;
	private PacManWorld world;
	
	private Text scoreText;
	
	private Image pacmanImage = new Image("imgs/pacMan.png");
	//characters
	
	private enum STATE{
		MENU,
		GAME
	};
	
	private STATE state = STATE.GAME;
	
	public static void main(String[] args) {
		launch(args);
	}
	
	
	
	

	@Override
	public void start(Stage stage) throws Exception {
		
		stage.setTitle("Pac Man");
		BorderPane root = new BorderPane();
		world = new PacManWorld();
		
		//create model so all objects are ready for us.
		if(state== STATE.GAME) {
			Model m = new Model(new File("map3.txt"), world);
			world.setModel(m);
			
			world.setPrefHeight(630);
			world.setPrefWidth(680);
			root.setCenter(world);
			Scene scene = new Scene(root);
	
		/**
		 * Code to create the top HUD (top row of information about the game).
		 */
		
			BorderPane topBox = new BorderPane();
			scoreText = new Text("Score: 0");
			HBox lifeDisplayHBox = new HBox();
			for(int i = 0; i < 3; i++) {
				lifeDisplayHBox.getChildren().add(new ImageView(pacmanImage));
			}
			topBox.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
			//Pass a reference to the text & lives to the world so it can update
			world.setLiveBox(lifeDisplayHBox);
			world.setScoreText(scoreText);
		
			//set the font to a custom pacman font 
			Font pacmanFont = Font.loadFont(Controller.this.getClass().getResource("pacfont.ttf").toExternalForm(), 24);
			scoreText.setFont(pacmanFont);
			scoreText.setFill(Color.WHITE);
			topBox.setLeft(scoreText);
			topBox.setRight(lifeDisplayHBox);		
			root.setTop(topBox);
		
		
			setKeyboardEvent();
		
		
		
			world.start();
		
			stage.setScene(scene);
		} else if(state== STATE.MENU) {
			//show the menu screen and have the buttons turn the state back to state.GAME
			
			
		}
		
		stage.setMaxWidth(630);
		stage.setMaxHeight(680);
		stage.show();
	}
	
	public void setKeyboardEvent() {
		world.requestFocus();
		world.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				if(state==STATE.GAME) {
					if(event.getCode() == KeyCode.UP && !((Pacman)world.getPacman()).getDirection().equals("UP")){
						((Pacman)world.getPacman()).queueTurn("UP");
					}
					else if(event.getCode() == KeyCode.DOWN && !((Pacman)world.getPacman()).getDirection().equals("DOWN")){
						((Pacman)world.getPacman()).queueTurn("DOWN");
					}
					else if(event.getCode() == KeyCode.LEFT && !((Pacman)world.getPacman()).getDirection().equals("LEFT")){
						((Pacman)world.getPacman()).queueTurn("LEFT");
					}
					else if(event.getCode() == KeyCode.RIGHT && !((Pacman)world.getPacman()).getDirection().equals("RIGHT")){
						((Pacman)world.getPacman()).queueTurn("RIGHT");
					}
				}
				
				
				/**
				 * This code is a more robust way to turn so that you can queue in the direction you are moving
				 * LEAVE THIS COMMENTED FOR NOW, UNCOMMENT after TESTING A LOT
				 */
				//Code to make direction be queued based on current queued direction as opposed to the above where
				//direction queue is based on current direction
				/*
				if(event.getCode() == KeyCode.UP && !((Pacman)world.getPacman()).getQueuedDirection().equals("UP")) {
					((Pacman)world.getPacman()).queueTurn("UP");
				}
				else if(event.getCode() == KeyCode.DOWN && !((Pacman)world.getPacman()).getQueuedDirection().equals("DOWN")) {
					((Pacman)world.getPacman()).queueTurn("DOWN");
				}
				else if(event.getCode() == KeyCode.LEFT && !((Pacman)world.getPacman()).getQueuedDirection().equals("LEFT")) {
					((Pacman)world.getPacman()).queueTurn("LEFT");
				}
				else if(event.getCode() == KeyCode.RIGHT && !((Pacman)world.getPacman()).getQueuedDirection().equals("RIGHT")) {
					((Pacman)world.getPacman()).queueTurn("RIGHT");
				}*/
				
				
				System.out.println("Queue has " + ((Pacman) world.getPacman()).getQueuedDirection());
				
				
			}
			
		});
	}
}

