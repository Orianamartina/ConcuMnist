package main;

public class IntTuple implements Comparable<IntTuple> {
	
	private int first;
	private int second;
	
	public IntTuple(int first, int second) {
		this.first = first;
		this.second = second;
	}
	
	public int first() {
		return this.first;
	}
	
	public int second() {
		return this.second;
	}

	@Override
	public int compareTo(IntTuple other) {
		return Integer.compare(this.second, other.second);
	}
}
