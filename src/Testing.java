
public class Testing {
	private int nums;
	private String names;
	private int count;
	
	public Testing() {
		nums = 0;
		names = "Neil";
		count = 0;
	}
	
	public Testing(int num, String nam, int cnt) {
		nums = num;
		names = nam;
		count = cnt;
	}
	
	public int addOne() {
		return (int) (nums*2 + Math.pow(2, 10));
	}
	
	public int addTwo() {
		return count+2;
	}
}
