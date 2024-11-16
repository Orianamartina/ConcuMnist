package main;

import main.buffer.TaskBuffer;

public class ThreadPool {
	private int workerQ;
	private TaskBuffer taskBuffer;

	public ThreadPool(int workerQ, TaskBuffer taskBuffer) {
		this.workerQ = workerQ;
		this.taskBuffer = taskBuffer;
	}


	public void runWorkers() {
		int workerCount = 0;
		try {
			while(workerCount <= workerQ) {
				Worker worker = new Worker(taskBuffer);
				worker.start();
				workerCount++;
			}
		} catch (Exception e) {
		}
	}
}
