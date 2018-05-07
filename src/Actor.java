import java.util.ArrayList;
import java.util.List;

import javafx.scene.image.ImageView;

public abstract class Actor extends ImageView {
	
	public Actor() {
		
	}
	
	public double getHeight() {
		return getBoundsInParent().getHeight();
	}

	public double getWidth() {
		return getBoundsInParent().getWidth();
	}

	public <A extends Actor> List<A> getIntersectingObjects(Class<A> cls) {
				
		
		ArrayList<A> list = new ArrayList<>();
		
		
		for(A n : getWorld().getObjects(cls)) {
			if(n != this && n.intersects(this.getBoundsInParent())) {
				list.add(n);
			}
		}
		
		return list;
	}

	public World getWorld() {
		return (World)(getParent());
	}
	
	public <A extends Actor> A getOneIntersectingObject(java.lang.Class<A> cls) {
		return (getIntersectingObjects(cls).size() > 0) ? getIntersectingObjects(cls).get(0) : null;
	}

	public void move(double dx, double dy) {
		this.setX(this.getX() + dx);
		this.setY(this.getY() + dy);
	}

	public abstract void act(long now);
}
