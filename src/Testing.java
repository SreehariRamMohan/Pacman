
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
		return nums+1;
	}
	
	public int addTwo() {
		count+=2;
		return count;
	}
}
