
public class Ghost extends Character{
	
	private boolean isEdible;
	private boolean isAutoPlay;
	private Pacman pac;
	
	public Ghost() {
		//this.setImage(); Make the Ghost have an image. Later make it depend on color
		setSpeed(10);
	}
	
	@Override
	public void act(long now) {
		//Act will move depending on auto play state.
	}
	
	public boolean isEdible() {
		return isEdible;
	}
	
	public void toggleEdible() {
		isEdible = !isEdible;
	}
	
	public Pacman getPacman() {
		return pac;
	}
	
	public void autoPlayNextMove() {
		
	}
	
	public boolean isAutoPlay() {
		return isAutoPlay;
	}
	
	public void toggleAutoPlay() {
		isAutoPlay = !isAutoPlay;
	}
}
