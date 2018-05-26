import javafx.scene.image.Image;

public class Cherry extends Food{

	public Cherry() {
		this.setImage(new Image("imgs/cherry.png"));
	}
	
	@Override
	public void onEat() {		
		if(((Pacman)this.getWorld().getPacman()).hasCherryBeenSpawned()) {
			this.setOpacity(0);
			int[] rowcol = Character.getRowCol(this.getX() + this.getWidth()/2, this.getY() + this.getHeight()/2);
			((Pacman)this.getWorld().getPacman()).addScoreAnimation(100, 1000, rowcol[0], rowcol[1]);
			this.getWorld().updateScoreText(this.getWorld().getScore() + 100);
			this.getWorld().getModel().setFoodAt(rowcol[0], rowcol[1], null);
			((Pacman)this.getWorld().getPacman()).setHasCherryBeenSpawned(false);
			this.getWorld().remove(this);
		}
	}
}
