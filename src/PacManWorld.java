import java.util.ArrayList;
import java.util.List;

public class PacManWorld extends World {

	private Score score;
	private Actor pacman;
	
	private List<Actor> ghosts;

	
	public PacManWorld() {
		score = new Score();
		ghosts = new ArrayList<>();
		score.setX(50);
		score.setY(50);
		getChildren().add(score);
	}
	
	
	@Override
	public void act(long now) {
		// TODO Auto-generated method stub
		
	}
	
	public int getScore() {
		return score.getValue();
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

}
