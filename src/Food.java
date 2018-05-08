
public abstract class Food extends Actor {

	@Override
	public void act(long now) {
		// TODO Auto-generated method stub
		
	}
	
	public void remove() {
		getWorld().remove(this);
	}

	public abstract void onEat();
}
