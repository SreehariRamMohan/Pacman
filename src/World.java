
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javafx.animation.AnimationTimer;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;

public abstract class World extends Pane {
	
	private AnimationTimer timer;
	private HashSet<KeyCode> keySet = new HashSet();	

	public World() {
		
		timer = new AnimationTimer() {
			@Override
			public void handle(long now) {
				// TODO Auto-generated method stub
				act(now);
				
				List<Node> nodeList = getChildren();
				for(int i = 0; i < nodeList.size(); i++) {
					Node n = nodeList.get(i);
					
					if(n instanceof Actor) {
						((Actor) n).act(now);
					}
				}				
								
			}
		};
		
		start();

	}
	
	public abstract void act(long now);
	
	void add(Actor actor) {
		getChildren().add(actor);
	}
	
	public <A extends Actor> java.util.List<A> getObjects(java.lang.Class<A> cls) {
		ArrayList<A> list = new ArrayList<>();
		for(Node a : getChildren()) {
//			if(a.getClass() == cls) { //if this breaks use instanceof
//				list.add((A) a);
//			}
			
			if(cls.isInstance(a)) { //if this breaks use instanceof
				list.add((A) a);
			}
			
			
			
		}
		return list;
	}
	
	public void remove(Actor actor) {
		getChildren().remove(actor);
	}
	
	public void start() {
		timer.start();
	}
	
	public void stop() {
		timer.stop();
	}
	
	public void addKeyCode(KeyCode key) {
		keySet.add(key);
	}
	
	public void removeKey(KeyCode key) {
		keySet.remove(key);
	}
	
	public boolean isKeyDown(KeyCode key) {
		return keySet.contains(key);
	}
	
	public void pause() {
		stop();
	}
	
}
