
public class PacManWorld extends World {

	private Score score;
	
	public PacManWorld() {
		score = new Score();
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

}
