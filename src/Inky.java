
import javafx.scene.image.Image;

public class Inky extends Ghost {

	public Inky(int startingRow, int startingCol) {		
		super(startingRow, startingCol);
		System.out.println("Inky created");
		this.setImage(new Image("imgs/inky.png"));
	}
	
	@Override
	public void act(long now) {
		super.act(now);
	}

}
