import javafx.scene.image.Image;

public class RegFood extends Food {
	
	public RegFood() {
		this.setImage(new Image("imgs/regfood.png"));
	}

	@Override
	public void onEat() {
		getWorld().setScore(getWorld().getScore() + 10);
		getWorld().updateScore(getWorld().getScore());
		remove();
	}

}
