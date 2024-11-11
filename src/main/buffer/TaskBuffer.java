package main.buffer;

import main.task.Task;

public class TaskBuffer extends Buffer<Task> {

	public TaskBuffer(int bufferSize) {
		super(bufferSize);
		
	}
	
	public int tasks() {
		return this.items.size();
	}
}
