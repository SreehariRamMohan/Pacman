import javafx.scene.image.Image;

public class RegFood extends Food {
	
	public RegFood() {
		this.setImage(new Image("imgs/regfood.png"));
	}

	@Override
	public void onEat() {
		getWorld().updateScoreText(getWorld().getScore() + 10);
		remove();
	}

}
