import java.util.ArrayList;
import java.util.List;


import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class PacManWorld extends World {

	private int score;
	
	private Actor pacman;
	private List<Actor> ghosts;
	private Model model;
	private int level = 1;
	
	private Text scoreText;
	private HBox lifeDisplayBox;
	private HBox hbox;
	private Controller controller;
	
	public PacManWorld() {
		ghosts = new ArrayList<>();
		this.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
	}
	
	
	@Override
	public void act(long now) {
		// TODO Auto-generated method stub
		
	}
	
	public Controller getController() {
		return controller;
	}
	
	public void setController(Controller c) {
		this.controller = c;
	}
	
	public void setModel(Model m) {
		this.model = m;
	}
	
	public Model getModel() {
		return model;
	}
	
	public void setPacman(Actor pac) {
		this.pacman = pac;
	}
	
	public Actor getPacman() {
		return pacman;
	}
	
	public void storeGhost(Actor ghost) {
		ghosts.add(ghost);
	}
	
	public void removeGhost(Actor ghost) {
		ghosts.remove(ghost);
	}
	
	public List<Actor> getGhosts() {
		return ghosts;
	}


	public int getLevel() {
		return level;
	}


	public void setLevel(int level) {
		this.level = level;
	}

	
	/**
	 * Methods for updating the lives
	 * 
	 * Precondition: Assumes that the you are calling this method properly and that the number of new lives is 
	 * LESS than the value you called it with last time. Unless it is the first time in which it will add numLives.
	 * 
	 * Postcondition: newLives pacman lives will be shown on the upper right corner. If you didn't follow the preconditions then an 
	 * IndexOutOfBoundsException will be thrown
	 */
	public void updateLives(int newLives) {
		
		getLifeDisplayBox().getChildren().clear();
		
		for(int i = 0; i < newLives; i++) {
			getLifeDisplayBox().getChildren().add(new ImageView(new Image("imgs/pacMan.png")));
		}
		
	}

	public HBox getLifeDisplayBox() {
		return lifeDisplayBox;
	}
	public void setLiveBox(HBox lifeDisplayBox) {
		this.lifeDisplayBox = lifeDisplayBox;
	}
	
	
	/**
	 * Methods for updating the score that a player has
	 */
	public void setScoreText(Text scoreText) {
		this.scoreText = scoreText;
	}
	
	public int getScore() {
		return score;
	}
	
	public void updateScoreText(int newScore) {
		this.score  = newScore;
		this.scoreText.setText("Score: " + this.getScore());
	}
	
}
