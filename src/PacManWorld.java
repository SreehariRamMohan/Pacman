import java.util.ArrayList;
import java.util.List;

import javafx.scene.text.Text;

public class PacManWorld extends World {

	private int score;
	private Actor pacman;
	private List<Actor> ghosts;
	private Model model;
	private int level = 1;
	
	private Text scoreText;
	private Text liveText;
	
	public PacManWorld() {
		ghosts = new ArrayList<>();
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
	
	public List<Actor> getGhosts() {
		return ghosts;
	}


	public int getLevel() {
		return level;
	}


	public void setLevel(int level) {
		this.level = level;
	}


	public void updateScore(int newScore) {
		String newText = "Score: " + newScore;
		scoreText.setText(newText);
	}


	public void setScoreText(Text scoreText) {
		this.scoreText = scoreText;
	}



	public Text getLiveText() {
		return liveText;
	}


	public void setLiveText(Text liveText) {
		this.liveText = liveText;
	}


	public int getScore() {
		return score;
	}


	public void setScore(int score) {
		this.score = score;
	}

}
