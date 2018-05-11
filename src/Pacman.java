import java.util.Timer;
import java.util.TimerTask;

import javafx.scene.image.Image;

public class Pacman extends Character{

	private int lives;

	private static int trackPoint = 0;

	private Image pacManClosed = new Image("imgs/pacMan.png");
	private Image pacManOpen = new Image("imgs/pacMan2.png");
	private int openCloseMouthCounter = 0;


	public Pacman() {
		this.setImage(pacManClosed);
		lives = 3;
		setSpeed(3);
	}

	public Pacman(int lives) {
		//this.setImage() Add image later
		this.lives = 3;
		setSpeed(3);
	}

	@Override
	public void act(long now) {

		animateMouth();
		
		

		edgeLoop();

		if(this.hasQueue() && this.isInCenter() && this.canMove(this.getQueuedDirection(), this)) {
			this.setDirection(this.getQueuedDirection());
			this.removeQueuedDirection();
		} 
			
		if(this.getDirection() == null) {
			//start of game do nothing until player moves.
		} else if(this.getDirection().equals(Character.RIGHT) && this.canMove(RIGHT, this)) {	
			if(this.safeMove(RIGHT, this)) {
				this.setRotate(0);
			}
		} else if(this.getDirection().equals(Character.LEFT) && this.canMove(LEFT, this)) {
			if(this.safeMove(LEFT, this)) {
				this.setRotate(180);
			}
		} else if(this.getDirection().equals(Character.DOWN) && this.canMove(DOWN, this)) {
			if(this.safeMove(DOWN, this)) {
				this.setRotate(90);
			}
		} else if(this.getDirection().equals(Character.UP) && this.canMove(UP, this)) {
			if(this.safeMove(UP, this)) {
				this.setRotate(270);
			}
		}


		detectFood();
		detectGhosts();
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
		if(this.getIntersectingObjects(Ghost.class).size() != 0) {

			//we need to make sure that the ghost we take is on our row, col position.

			for(Ghost ghost : this.getIntersectingObjects(Ghost.class)) {
				int[] pos = Character.getRowCol(ghost.getX(), ghost.getY());
				int row = pos[0];
				int col = pos[1];

				int[] myPos = Character.getRowCol(this.getX(), this.getY());
				int myRow = myPos[0];
				int myCol = myPos[1];

				//the food only counts iff we are at it's row, col position.

				if(row == myRow && col == myCol) {
					//pacman has now eaten this food.
					if(ghost.isEdible()) {
						int increment = (int) Math.pow(2, trackPoint);
						trackPoint++;
						getWorld().setScore(getWorld().getScore() + increment*200);
						getWorld().updateScore(getWorld().getScore());
						getWorld().remove(ghost);
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

	private void detectFood() {
		if(this.getIntersectingObjects(RegFood.class).size() != 0) {

			//we need to make sure that the food we take is on our row, col position.

			for(RegFood food : this.getIntersectingObjects(RegFood.class)) {
				int[] pos = Character.getRowCol(food.getX(), food.getY());
				int row = pos[0];
				int col = pos[1];

				int[] myPos = Character.getRowCol(this.getX(), this.getY());
				int myRow = myPos[0];
				int myCol = myPos[1];

				//the food only counts iff we are at it's row, col position.

				if(row == myRow && col == myCol) {
					//pacman has now eaten this food.
					food.onEat();
					//can now safely break out since we are only allowing one eat() per act();
					break;
				}
			}

		} else if(this.getIntersectingObjects(EatGhostPowerUp.class).size() != 0) {
			//we need to make sure that the food we take is on our row, col position.

			for(EatGhostPowerUp food : this.getIntersectingObjects(EatGhostPowerUp.class)) {
				int[] pos = Character.getRowCol(food.getX(), food.getY());
				int row = pos[0];
				int col = pos[1];

				int[] myPos = Character.getRowCol(this.getX(), this.getY());
				int myRow = myPos[0];
				int myCol = myPos[1];

				//the food only counts iff we are at it's row, col position.

				if(row == myRow && col == myCol) {
					//pacman has now eaten this food.
					food.onEat();
					//can now safely break out since we are only allowing one eat() per act();
					break;
				}
			}

		}
	}

	private void edgeLoop() {
		//allow the pacman to edge loop
		if(this.getX() > getWorld().getWidth()) {
			this.setCoordinate(0, this.getY());
		} else if((this.getX() + this.getWidth()) < 0) {
			this.setCoordinate(this.getWorld().getWidth(), this.getY());
		}		
	}

	public static void setTrackPoint(int p) {
		trackPoint = p;
	}

	public void decrementLives() {
		lives--;

		//update the lives of pacman with the label on the screen.
		getWorld().getLivesText().setText("Lives: " + this.getLives());
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

		if(lives >= 0) {
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
