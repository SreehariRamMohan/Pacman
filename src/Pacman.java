import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class Pacman extends Character{

	private int lives;

	private static int trackPoint = 0;

	private Image pacManClosed = new Image("imgs/pacMan.png");
	private Image pacManOpen = new Image("imgs/pacMan2.png");
	private int openCloseMouthCounter = 0;
	
	//sounds
	
	boolean isPlaying = false;
	
	public void playEatSound() {
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
		lives = 3;
		setSpeed(3);
		playLevelStartSound();
	}

	public Pacman(int lives) {
		//this.setImage() Add image later
		this.lives = 3;
		setSpeed(3);
		playLevelStartSound();
	}

	@Override
	public void act(long now) {
		animateMouth();
		detectFood();
		detectGhosts();
		mainMovement();
		edgeLoop();

	}

	private void mainMovement() {
		
//		System.out.println("locX = " + this.getX() + " locy = " + this.getY());
		
		
		
		orientCorrectly(this.getDirection());

//		System.out.println("Can I turn " + this.getQueuedDirection() + " the answer is " + this.canMove(this.getQueuedDirection()));
//		System.out.println("Is in center ? -> " + this.isInCenter() + " x = " + this.getX() + " y = " + this.getY());
				
		if( (this.hasQueue() && this.canMove(this.getQueuedDirection())) 
				&& this.isInCenter()) {
			
			System.out.println("Dequeue " + this.getQueuedDirection());
			System.out.println("X = " + this.getX());
			
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

	private void detectGhosts() {
		//collision with ghosts
		if(this.getIntersectingObjects(Blinky.class).size() != 0) {

			//we need to make sure that the ghost we take is on our row, col position.

			for(Blinky ghost : this.getIntersectingObjects(Blinky.class)) {
				int[] pos = Character.getRowCol(ghost.getX() + ghost.getWidth()/2, ghost.getY() + ghost.getHeight()/2);
				int row = pos[0];
				int col = pos[1];

				int[] myPos = Character.getRowCol(this.getX() + this.getWidth()/2, this.getY() + this.getHeight()/2);
				int myRow = myPos[0];
				int myCol = myPos[1];

				//the food only counts iff we are at it's row, col position.

				if(row == myRow && col == myCol) {
					//pacman has now eaten this food.
					if(ghost.isEdible()) {
						int increment = (int) Math.pow(2, trackPoint);
						trackPoint++;
						getWorld().updateScoreText(getWorld().getScore() + increment*200);
						getWorld().remove(ghost);
						
						/**
						 * TODO: Make these values dependent upon the ghost eaten, for now hard-coded 
						 */
						addScoreAnimation(200, 500, row, col);
						
						//play the ghost death sound. 
						playGhostDeathSound();

						
					} else {
						//System.out.println("GAME OVER");
						//getWorld().remove(this);
						System.out.println("@ - Calling die()");

						die();
					}
				}
			}			
		}

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
	
	private void playPowerUpSound() {
		
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
		if(this.getIntersectingObjects(RegFood.class).size() != 0) {

			//we need to make sure that the food we take is on our row, col position.
			
			
			for(RegFood food : this.getIntersectingObjects(RegFood.class)) {
				int[] pos = Character.getRowCol(food.getX() + food.getWidth()/2, food.getY() + food.getHeight()/2);
				int row = pos[0];
				int col = pos[1];

				int[] myPos = Character.getRowCol(this.getX() + this.getWidth()/2, this.getY() + this.getHeight()/2);
				int myRow = myPos[0];
				int myCol = myPos[1];

				
				
				//the food only counts iff we are at it's row, col position.

				if(row == myRow && col == myCol) {
					//pacman has now eaten this food.
					playEatSound();
					food.onEat();
					//can now safely break out since we are only allowing one eat() per act();
					break;
				} else {
					System.out.println("Row of food = " + row + " col of food = " + col);
					System.out.println("My row is " + myRow + " my col is " + myCol);
					
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

				//the food only counts iff we are at it's row, col position.

				if(row == myRow && col == myCol) {
					//pacman has now eaten this food.
					playPowerUpSound();
					food.onEat();
					
					//can now safely break out since we are only allowing one eat() per act();
					break;
				} else {
					System.out.println("Row of food = " + row + " col of food = " + col);
					System.out.println("My row is " + myRow + " my col is " + myCol);
					
					
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
		getWorld().updateLives(this.getLives());
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

		if(true) { //TODO Make the Pacman Death Animation and replace this with num-lives
			
			for(Actor a : this.getWorld().getGhosts()) {
				Ghost g = (Ghost) a;
				g.clearPathList();
			}
			SoundUtils.playPacmanDeath();
			
			resetGame();
		} else {
			System.out.println("*****YOU LOSE*****");
			//show pacman death animation.
		}
	}

	public void resetGame() {

		getWorld().pause();

		Timer timer = new Timer();

		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				getWorld().start();
			}

		}, 1000);

		this.setCoordinate(this.getWorld().getModel().pacmanInitialPosition[1] * Controller.CHARACTER_DIMS, this.getWorld().getModel().pacmanInitialPosition[0] * Controller.CHARACTER_DIMS);

		for(int i = 0; i < this.getWorld().getGhosts().size(); i++) {
			Ghost g = (Ghost) this.getWorld().getGhosts().get(i);
			g.setCoordinate(this.getWorld().getModel().ghostInitialPositions.get(i)[1] * Controller.CHARACTER_DIMS, this.getWorld().getModel().ghostInitialPositions.get(i)[0]  * Controller.CHARACTER_DIMS);
		}

	}

}
