import java.util.ArrayList;
import java.util.List;

public class PacManWorld extends World {

	private Score score;
	private Actor pacman;
	private List<Actor> ghosts;
	private Model model;
	
	private int level = 1;
	
	public PacManWorld() {
		score = new Score();
		ghosts = new ArrayList<>();
		score.setX(40);
		score.setY(50);
		getChildren().add(score);
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
	
	public Score getScore() {
		return score;
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

}
