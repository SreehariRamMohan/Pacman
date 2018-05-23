import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class Pacman extends Character{

	private int lives;

	private static int trackPoint = 0;

	private Image pacManClosed = new Image("imgs/pacMan.png");
	private Image pacManOpen = new Image("imgs/pacMan2.png");
	private int openCloseMouthCounter = 0;
	
	private String[] deathImageFilePaths = new String[11];
		
	boolean isPlaying = false;
	
	private int pacmanFoodParticlesEaten = 0;
	
	private boolean shouldAnimateMouth = true;
	
	public final static int GHOST_EDIBLE_SECONDS = 6;
	
	public void playEatSound() {
		
		//We don't want sounds to overlap each other
		if(isPlaying) {
			return;
		} else {
			isPlaying = true;
		}
		
		Media eatingSound = new Media(new File("src/sounds/pacman_chomp.wav").toURI().toString());
		MediaPlayer chompPlayer = new MediaPlayer(eatingSound);
		
		chompPlayer.play();
		chompPlayer.setOnEndOfMedia(new Runnable() {

			@Override
			public void run() {
				isPlaying = false;
			}
			
		});
	}
	
	
	
	public Pacman() {		
		this.setImage(pacManClosed);
		lives = 1;
		setSpeed(3);
		pacmanFoodParticlesEaten = 0;
		playLevelStartSound();
		loadDeathAnimationImagePaths();

	}


	public Pacman(int lives) {
		this.setImage(pacManClosed);
		this.lives = lives;
		setSpeed(lives);
		pacmanFoodParticlesEaten = 0;
		playLevelStartSound();
		loadDeathAnimationImagePaths();
	}
	
	private void loadDeathAnimationImagePaths() {
		for(int i = 0; i < 11; i++) {
			String path = "imgs/pacmanDeathSequence/pacdeath"+(i+1)+".png";
			deathImageFilePaths[i] = path;
		}
	}


	@Override
	public void act(long now) {
		hasPacmanWon();
		animateMouth();
		detectFood();
		detectGhosts();
		mainMovement();
		edgeLoop();

	}

	private void hasPacmanWon() {
		if(this.getPacmanFoodParticlesEaten() == this.getWorld().getModel().getNumFoodParticles()) {
			this.getWorld().pause();
			Alert winAlert = new Alert(AlertType.CONFIRMATION);
			winAlert.setTitle("You Win!");
			winAlert.setHeaderText("Ate all food!");
			winAlert.setContentText("Do you want to Play Again?");
			
			ButtonType yes = new ButtonType("Yes");
			ButtonType exit = new ButtonType("Exit");
			
			winAlert.getButtonTypes().setAll(yes, exit);
			
			winAlert.getDialogPane().setPrefSize(400, 100);
			
			Image winImage = new Image("imgs/winAlertImage.png");
			ImageView winImageView = new ImageView(winImage);
			winImageView.setPreserveRatio(true);
			winImageView.setFitWidth(100);
			winAlert.setGraphic(winImageView);
			
			winAlert.show();
					
			Button restart = (Button) winAlert.getDialogPane().lookupButton(yes);
			restart.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
					setPacmanFoodParticlesEaten(0);
					resetGame();
					resetFood();
					setLives(3);
					getWorld().updateLives(getLives());
				}
				
			});
			
			Button exitButton = (Button) winAlert.getDialogPane().lookupButton(exit);
			exitButton.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
					//stop music so scene can be garbage collected.
					getWorld().pause();
					getWorld().getController().swapScenes("title");
				}
				
			});
		} else {
			//System.out.println("food eaten -> " + ((Pacman)this.getWorld().getPacman()).getPacmanFoodParticlesEaten() + " total ->  " + this.getWorld().getModel().getNumFoodParticles());
			
		}
		
	}



	private void mainMovement() {

		orientCorrectly(this.getDirection());
		
		if( (this.hasQueue() && this.canMove(this.getQueuedDirection())) 
				&& this.isInCenter()) {
			
			this.setDirection(this.getQueuedDirection());
			this.removeQueuedDirection();
			this.safeMove(this.getDirection());
		} else if(!this.canMove(this.getDirection())) {
			this.setDirection(Character.STATIONARY);
		} else {
			this.safeMove(this.getDirection());
		}
	}
	
	private void orientCorrectly(String direction) {
				
		if(this.getDirection().equals(Character.RIGHT)) {	
			this.setRotate(0);
		} else if(this.getDirection().equals(Character.LEFT)) {
			this.setRotate(180);
		} else if(this.getDirection().equals(Character.DOWN)) {
			this.setRotate(90);
		} else if(this.getDirection().equals(Character.UP)) {
			this.setRotate(270);
		}
	}

	private void animateMouth() {
		
		if(!shouldAnimateMouth()) {
			return;
		}
		
		openCloseMouthCounter++;
		
		if(openCloseMouthCounter < 20) {
			//open mouth
			this.setImage(pacManOpen);
		} else {
			//close mouth
			this.setImage(pacManClosed);
		}
		
		if(openCloseMouthCounter > 40) {
			openCloseMouthCounter = 0;
		}
		
	}
	
	
	/**
	 * To ensure accuracy with ghost collisions, to ensure collision in our game we check for both graphical collision 
	 * and collision in our model (2D Array)
	 * In order for a collision to be detected, the ghosts must collide with pacman both graphically and internally with our data structure. 
	 */
	private void detectGhosts() {
		

		if(this.getIntersectingObjects(Ghost.class).size() != 0) { //Graphical Collision check
			for(Actor ghost : this.getWorld().getGhosts()) {				
				int[] pos = Character.getRowCol(ghost.getX() + ghost.getWidth()/2, ghost.getY() + ghost.getHeight()/2);
				int row = pos[0];
				int col = pos[1];

				int[] myPos = Character.getRowCol(this.getX() + this.getWidth()/2, this.getY() + this.getHeight()/2);
				int myRow = myPos[0];
				int myCol = myPos[1];

				if(row == myRow && col == myCol) { //internal collision

					if(((Ghost)ghost).isEdible()) { //we eat ghost
						
						Ghost g = (Ghost) ghost;
						
						int initialGhostRow = ((Ghost) ghost).getStartingRow();
						int initialGhostCol = ((Ghost) ghost).getStartingCol();
						
						int increment = (int) Math.pow(2, trackPoint);
						trackPoint++;
						int point = increment*200;
						
						if(point > 1600) {
							point = 1600;
						}
						
						getWorld().updateScoreText(getWorld().getScore() + point);
						
						int[] currGhostRowCol = Character.getRowCol(g.getX(), g.getY());
						
						this.getWorld().removeGhost(ghost);
						getWorld().remove(ghost);
						
						/**
						 * TODO: Make these values dependent upon the ghost eaten, for now hard-coded 
						 */
						addScoreAnimation(point, 500, row, col);
						
						playGhostDeathSound();
						
						/**
						 * Determine which kind of Ghost we ate
						 **/
						String whoDoTheseEyesBelongTo = "";
						if(g instanceof Blinky) {
							whoDoTheseEyesBelongTo = "Blinky";
						} else if(g instanceof Clyde) {
							whoDoTheseEyesBelongTo = "Clyde";
						} else if(g instanceof Inky) {
							whoDoTheseEyesBelongTo = "Inky";
						} else if(g instanceof Pinky) {
							whoDoTheseEyesBelongTo = "Pinky";
						}
						
						//spawn the eyes in this cell and make the eyes travel back to the start for re-spawning. 
						spawnEyesGoingBackToHome(row, col, initialGhostRow, initialGhostCol, whoDoTheseEyesBelongTo);

						return;
						
					} else {
						System.out.println("@ - Calling die()");
						die();
					}
				}
			}			
		}

	}

	private void spawnEyesGoingBackToHome(int row, int col, int initialGhostRow, int initialGhostCol, String whoDoTheseEyesBelongTo) {
		
		ArrayList<int[]> pathToHome = (ShortestPathUtils.getPaths(row, col, initialGhostRow, initialGhostCol, this.getWorld().getModel()));
	
		//print out the path generated
		for(int[] pair : pathToHome) {
			System.out.print(Arrays.toString(pair) + "_");
		}
		System.out.println();
		
		
		pathToHome.remove(0); //0 is our first index, where we are right now, so it can be removed
		
		Image eyeImage = new Image("imgs/eyesRight.png");
		InvisibleActor eyes = new RespawnEyes(eyeImage, pathToHome, row, col, initialGhostRow, initialGhostCol, whoDoTheseEyesBelongTo);
		//Note: Eyes are not added to the model because they should not be collided with. 
		this.getWorld().add(eyes);
		//make the eyes go back to the home before they re-spawn into a new ghost.
	}



	private void addScoreAnimation(int points, int fadeout, int row, int col) {
		//add the score to this row, and col
		Image i = new Image("imgs/point" + points + ".png");
		InvisibleActor score = new InvisibleActor(i);
		score.setX(col * Controller.CHARACTER_DIMS);
		score.setY(row * Controller.CHARACTER_DIMS);
		this.getWorld().getChildren().add(score);
		
		//fade the score label out slowly
		FadeTransition scoreFade = new FadeTransition(Duration.millis(fadeout), score);
		scoreFade.setFromValue(1.0);
		scoreFade.setToValue(0);
		scoreFade.play();
		
		Timeline scoreEndTimeline = new Timeline();
		scoreEndTimeline.getKeyFrames().add(new KeyFrame(Duration.millis(fadeout + 10)));
		scoreEndTimeline.setOnFinished(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				getWorld().getChildren().remove(score);								
			}
			
		});
		
		scoreEndTimeline.play();
	}



	private void playGhostDeathSound() {
		
		Media eatingSound = new Media(new File("src/sounds/pacman_eatghost.wav").toURI().toString());
		MediaPlayer ghostDeathPlayer = new MediaPlayer(eatingSound);
		ghostDeathPlayer.setOnEndOfMedia(new Runnable() {

			@Override
			public void run() {
				isPlaying = false;
			}
			
		});
		ghostDeathPlayer.play();		
	}
	
	public void playPowerUpSound() {
		
		Media powerUpSound = new Media(new File("src/sounds/pacman_powerup.wav").toURI().toString());
		MediaPlayer player = new MediaPlayer(powerUpSound);
		player.setOnEndOfMedia(new Runnable() {

			@Override
			public void run() {
				isPlaying = false;
			}
			
		});
		player.play();
		
		
	}
	
	private void playLevelStartSound() {
		if(isPlaying) {
			return;
		} else {
			isPlaying = true;
		}
		Media startSound = new Media(new File("src/sounds/pacman_beginning.wav").toURI().toString());
		MediaPlayer soundPlayer = new MediaPlayer(startSound);
		
		soundPlayer.play();
		soundPlayer.setOnEndOfMedia(new Runnable() {

			@Override
			public void run() {
				isPlaying = false;
			}
			
		});
		
	}



	private void detectFood() {
		if(this.getIntersectingObjects(RegFood.class).size() != 0) { //graphical collision check

			for(RegFood food : this.getIntersectingObjects(RegFood.class)) { //internal check with our database
				int[] pos = Character.getRowCol(food.getX() + food.getWidth()/2, food.getY() + food.getHeight()/2);
				int row = pos[0];
				int col = pos[1];

				int[] myPos = Character.getRowCol(this.getX() + this.getWidth()/2, this.getY() + this.getHeight()/2);
				int myRow = myPos[0];
				int myCol = myPos[1];

				if(row == myRow && col == myCol) {
					//pacman has now eaten this food.
					playEatSound();
					
					food.onEat();
					//can now safely break out since we are only allowing one eat() per act();
					break;
				} else {
					
				}
			}

		}
		if(this.getIntersectingObjects(EatGhostPowerUp.class).size() != 0) {
			//we need to make sure that the food we take is on our row, col position.
			for(EatGhostPowerUp food : this.getIntersectingObjects(EatGhostPowerUp.class)) {
				int[] pos = Character.getRowCol(food.getX() + food.getWidth()/2, food.getY() + food.getHeight()/2);
				int row = pos[0];
				int col = pos[1];

				int[] myPos = Character.getRowCol(this.getX() + this.getWidth()/2, this.getY() + this.getHeight()/2);
				int myRow = myPos[0];
				int myCol = myPos[1];

				if(row == myRow && col == myCol) {
					//pacman has now eaten this food.
					//playPowerUpSound();
					food.onEat();
					//can now safely break out since we are only allowing one eat() per act();
					break;
				} else {

				}
			}

		}
	}

	private void edgeLoop() {
		//allow the pacman to edge loop
		if(this.getX() > getWorld().getWidth()) {
			this.setCoordinate(0, this.getY());
		} else if((this.getX()) <= 0) {
			this.setCoordinate(this.getWorld().getWidth() - this.getWidth(), this.getY());
		}		
	}

	public static void setTrackPoint(int p) {
		trackPoint = p;
	}

	public void decrementLives() {
		lives--;

		//update the lives of pacman with the label on the screen.
		if(this.getLives() > 0) {
			getWorld().updateLives(this.getLives());
		} else {
			getWorld().updateLives(0);
		}
	}

	public int getLives() {
		return lives;
	}

	public void setLives(int lives) {
		this.lives = lives;
	}

	public void onEat() {
		//Does something when something is eaten
	}

	@Override
	public void die() {

		decrementLives();
		getWorld().pause();
		SoundUtils.playPacmanDeath();

		if(this.getLives() > 0) { //TODO Make the Pacman Death Animation
			
			for(Actor a : this.getWorld().getGhosts()) {
				Ghost g = (Ghost) a;
				g.clearPathList();
			}
			deathAnimation();

		} else {		
			deathAnimation();
			System.out.println("*****YOU LOSE*****");
			//show pacman death animation.
		}
		
		
		
		
	}
	

	private void deathAnimation() {
		
		this.setDirection(Character.STATIONARY);
		
		SequentialTransition seq = new SequentialTransition();
		
		for(String path : deathImageFilePaths) {			
			System.out.println("path -> " + path);
			
			
			ImageSwapper trans = new ImageSwapper(this, path);
			PauseTransition pt = new PauseTransition(Duration.millis(100));
			
			seq.getChildren().add(trans);
			seq.getChildren().add(pt);
		}
		
		
		seq.play();
		
		seq.setOnFinished(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				if(getLives() <= 0) {
					//show you lose alert dialog
					showPacmanDeathAlert();
				} else {
					resetGame();
				}
			}
			
		});
		
		
	}

	private void showPacmanDeathAlert() {	
		Alert deathAlert = new Alert(AlertType.CONFIRMATION);
		deathAlert.setTitle("You lose!");
		deathAlert.setHeaderText("Lost 3 lives.");
		deathAlert.setContentText("Do you want to Play Again?");
		
		ButtonType yes = new ButtonType("Yes");
		ButtonType exit = new ButtonType("Exit");
		
		deathAlert.getButtonTypes().setAll(yes, exit);
		
		deathAlert.getDialogPane().setPrefSize(400, 100);
		
		Image ghostImage = new Image("imgs/deathAlertImage.png");
		ImageView ghostImageView = new ImageView(ghostImage);
		ghostImageView.setPreserveRatio(true);
		ghostImageView.setFitWidth(100);
		deathAlert.setGraphic(ghostImageView);
		
		deathAlert.show();
				
		Button restart = (Button) deathAlert.getDialogPane().lookupButton(yes);
		restart.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				resetGame();
				setPacmanFoodParticlesEaten(0);
				resetFood();
				setLives(3);
				getWorld().updateLives(getLives());
				getWorld().updateScoreText(0);
			}
		});
		
		Button exitButton = (Button) deathAlert.getDialogPane().lookupButton(exit);
		exitButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				//stop music so scene can be garbage collected.
				getWorld().pause();
				getWorld().getController().swapScenes("title");
			}
			
		});
		
		
		
	}
	
	public void resetFood() {
		System.out.println("In resetFood()");
		
		//add back any food which isn't on the screen
		for(int r = 0; r < this.getWorld().getModel().getNumRows(); r++) {
			for(int c = 0; c < this.getWorld().getModel().getNumCols(); c++) {
				//only if the food is not in the world should we add it!
				if(this.getWorld().getModel().getFoodAt(r, c) != null && 
						!this.getWorld().getChildren().contains(this.getWorld().getModel().getFoodAt(r, c))) {

					Food foodToAdd = this.getWorld().getModel().getFoodAt(r, c);
					
					if(foodToAdd instanceof EatGhostPowerUp) { //allow power ups to be used again
						((EatGhostPowerUp)foodToAdd).setHasEaten(false);
					}
					
					foodToAdd.setX(c * Controller.CHARACTER_DIMS);
					foodToAdd.setY(r * Controller.CHARACTER_DIMS);
					
					//lets make it fade in with a transition
					FadeTransition foodFadeIn = new FadeTransition(Duration.millis(2000), foodToAdd);
					foodFadeIn.setFromValue(0);
					foodFadeIn.setToValue(1);
					foodFadeIn.setAutoReverse(false);
					
					this.getWorld().add(foodToAdd);

					foodFadeIn.play();
				} else {
					
				}

			}
		}


	}



	public void resetGame() {

		getWorld().pause();
		
		Timer timer = new Timer();

		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				getWorld().start();
			}

		}, 1000);

		this.setRotate(0);
		this.setImage(new Image("imgs/pacMan.png"));
		this.setCoordinate(this.getWorld().getModel().pacmanInitialPosition[1] * Controller.CHARACTER_DIMS, this.getWorld().getModel().pacmanInitialPosition[0] * Controller.CHARACTER_DIMS);

		for(int i = 0; i < this.getWorld().getGhosts().size(); i++) {
			Ghost g = (Ghost) this.getWorld().getGhosts().get(i);
			g.setCoordinate(this.getWorld().getModel().ghostInitialPositions.get(i)[1] * Controller.CHARACTER_DIMS, this.getWorld().getModel().ghostInitialPositions.get(i)[0]  * Controller.CHARACTER_DIMS);
			g.clearPathList();
		}

	}

	public int getPacmanFoodParticlesEaten() {
		return pacmanFoodParticlesEaten;
	}

	public void setPacmanFoodParticlesEaten(int pacmanFoodParticlesEaten) {
		this.pacmanFoodParticlesEaten = pacmanFoodParticlesEaten;
	}



	public boolean shouldAnimateMouth() {
		return shouldAnimateMouth;
	}

	public void setShouldAnimateMouth(boolean shouldAnimateMouth) {
		this.shouldAnimateMouth = shouldAnimateMouth;
	}	
}
