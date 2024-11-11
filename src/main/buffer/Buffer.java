package main.buffer;

import java.util.ArrayList;
import java.util.List;

public class Buffer<T> {
	protected List<T> items;
	private int bufferSize;

	public Buffer(int bufferSize) {
		this.items = new ArrayList<>();
		this.bufferSize = bufferSize;
	}

	public synchronized void addItem(T item) throws InterruptedException {
		while (items.size() == bufferSize) {
			wait();
		}
		items.add(item);
		notifyAll();
	}

	public synchronized T retrieveItem() throws InterruptedException {
		while (items.size() == 0) {
			wait();
		}
		T item = items.remove(0);
		notifyAll();
		return item;
	}
}
