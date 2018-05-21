import java.util.ArrayList;
import java.util.List;


import javafx.geometry.Insets;
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
	
	public PacManWorld() {
		ghosts = new ArrayList<>();
		this.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
	}
	
	
	@Override
	public void act(long now) {
		// TODO Auto-generated method stub
		
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
	 */
	public void updateLives(int newLives) {
		while(newLives != getLifeDisplayBox().getChildren().size()) {
			getLifeDisplayBox().getChildren().remove(this.getLifeDisplayBox().getChildren().size() - 1);
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
