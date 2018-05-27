
import java.awt.Graphics;
import java.io.File;
import java.net.URISyntaxException;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Controller extends Application {

	//game CONSTANTS
	public int NUM_GHOSTS = 5;
	public static int SCREEN_HEIGHT = 680;
	public static int SCREEN_WIDTH = 630;
	public static int CHARACTER_DIMS = 30;
	private PacManWorld world;
	
	//Labels/Scores for the HUD
	private Text scoreText;
	private Image pacmanImage = new Image("imgs/pacMan.png");

	private Stage stage;
	
	/*
	 * Reset constants for ghosts. These variables indicate how often the ghosts will recalculate their paths. 
	 * (The smaller the numbers, the more accurate these ghosts will be). 
	 */
	public static final int PINKY_RESET = 10;
	public static final int BLINKY_RESET = 20;
	public static final int INKY_RESET = 30;
	public static final int CLYDE_RESET = -1; //-1 indicates that we only reset when the path is empty
	
	public static void main(String[] args) {
		launch(args);
	}
	

	@Override
	public void start(Stage stage) throws Exception {
		
		stage.setTitle("Pac Man");
		this.stage = stage;
		
		Scene titleScene = createTitleUI();
		
		stage.setScene(titleScene);
		stage.setMaxWidth(630);
		stage.setMaxHeight(680);
		stage.show();
	}
	
	/**
	 * Layout Manager method which can swap screens for us.
	 * @param sceneName the name of the scene to swap to. 
	 */
	public void swapScenes(String sceneName) {
		if(sceneName.equals("title")) {
			stage.setMaxWidth(630);
			stage.setMaxHeight(680);
			stage.setScene(createTitleUI());
		} else if(sceneName.equals("game")) {
			stage.setMaxWidth(630);
			stage.setMaxHeight(680);
			stage.setScene(createGameUI());
		}
	}
	
	
	
	private Scene createGameUI() {
		BorderPane root = new BorderPane();
		world = new PacManWorld();
		
		//create model so all objects are ready for us.
		Model m = new Model(new File("map3.txt"), world);
		world.setModel(m);
		world.setController(this);
		
		world.setPrefHeight(630);
		world.setPrefWidth(680);
		root.setCenter(world);
		Scene scene = new Scene(root);
	
		/*
		 * Code to create the top HUD (top row of information about the game).
		 */
		
		BorderPane topBox = new BorderPane();
		topBox.setMaxWidth(630);
		
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
		
		return scene;
	}


	public void setKeyboardEvent() {
		world.requestFocus();
		world.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
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
				
				/**
				 * UNSTABLE
				 * Code below is a more robust way to turn so that you can queue in the direction you are moving
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
			}
			
		});
	}
	
	private Scene createTitleUI() {
		Group root = new Group();
		Scene scene = new Scene(root,630,680);
	
		
		MediaPlayer player = new MediaPlayer(new Media(getClass().getResource("imgs/gameplay.mp4").toExternalForm()));
		MediaView mediaView = new MediaView(player);
		player.setAutoPlay(true);
		player.setOnEndOfMedia(new Runnable() {
			@Override
			public void run() {
				player.seek(Duration.ZERO);
			}
		});
		
		//Title
		String pac = null;
		try {
			pac = getClass().getResource("imgs/pacmanTitle.png").toURI().toString();
		} catch (URISyntaxException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Image title = new Image(pac, 500, 100, false, false);
		ImageView pacmanTitle = new ImageView(title);
		
		//Play Button
		String play = null;
		try {
			play = getClass().getResource("imgs/button_play.png").toURI().toString();
			
		} catch (URISyntaxException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Image button = new Image(play, 100, 50, false, false);
		ImageView play_button = new ImageView(button);
		
		//Instructions
		String inst = null;
		try {
			inst = getClass().getResource("imgs/button_instructions.png").toURI().toString();
		} catch (URISyntaxException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		Image ructions = new Image(inst,150,50,false,false);
		ImageView instructions = new ImageView(ructions);
		instructions.setOnMouseClicked(new EventHandler<MouseEvent>() {
		WebView webView = new WebView();
			@Override
			public void handle(MouseEvent event) {
				player.dispose();
				
				BorderPane newRoot = new BorderPane();				
				Scene webScene = new Scene(newRoot,630,680);
				Button backButton = null;
				WebEngine engine = webView.getEngine();
				try {
					engine.load(getClass().getResource("imgs/Instructions.html").toURI().toString());
					newRoot.setTop(webView);
					
					String back = null;
					try {
						back = getClass().getResource("imgs/back_button2.png").toURI().toString();
					} catch (URISyntaxException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					Image backButt = new Image(back);
					ImageView button = new ImageView(backButt);
					backButton = new Button("Back");
					
					button.setOnMouseClicked(new EventHandler<MouseEvent>() {

						@Override
						public void handle(MouseEvent event) {
							swapScenes("title");
						}
						
					});
					
					HBox bottom = new HBox();
					
					bottom.getChildren().add(button);
					
					newRoot.setLeft(button);
					
					stage.setScene(webScene);
				} catch (URISyntaxException e) {
					e.printStackTrace();
				}
			}
		});
		
		play_button.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				swapScenes("game");
				player.dispose();
			}
			
		});
		
		pacmanTitle.setX(scene.getWidth()/2 - title.getWidth()/2);
		pacmanTitle.setY(scene.getHeight()/6);
		
		play_button.setX(scene.getWidth()/2 - button.getWidth()/2);
		play_button.setY(2*scene.getHeight()/6 + 25);
		
		instructions.setX(scene.getWidth()/2 - ructions.getWidth()/2);
		instructions.setY(3*scene.getHeight()/6);
		
		root.getChildren().add(mediaView);
		root.getChildren().add(pacmanTitle);
		root.getChildren().add(play_button);
		root.getChildren().add(instructions);
		
		return scene;
	}
}

