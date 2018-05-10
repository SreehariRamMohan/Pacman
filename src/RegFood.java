import javafx.scene.image.Image;

public class RegFood extends Food {
	
	public RegFood() {
		this.setImage(new Image("imgs/regfood.png"));
	}

	@Override
	public void onEat() {
		getWorld().getScore().setValue(getWorld().getScore().getValue()+10);
		remove();
	}

}
