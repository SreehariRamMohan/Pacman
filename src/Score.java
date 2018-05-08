import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class Score extends Text {
	private int value;
	
	public Score() {
		value = 0;
		this.setFont(Font.font(24));
		updateDisplay();
		
	}
	
	public void updateDisplay() {
		this.setText(""+value);
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
		updateDisplay();
	}
	
	
	
	
}
