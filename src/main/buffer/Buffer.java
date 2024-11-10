package main.buffer;

import java.util.ArrayList;
import java.util.List;

public class Buffer<T> {
	private List<T> items;
	private int bufferSize;

	public Buffer(int bufferSize) {
		this.items = new ArrayList<>();
		this.bufferSize = bufferSize;
	}

	public synchronized void addItem(T item) throws InterruptedException {
		while (items.size() == bufferSize) {
			System.out.println("Waiting to add item...");
			wait();
		}
		items.add(item);
		notifyAll();
	}

	public synchronized T retrieveItem() throws InterruptedException {
		while (items.size() == 0) {
			System.out.println("Waiting to retrieve item...");
			wait();
		}
		T item = items.remove(0);
		notifyAll();
		return item;
	}
}
