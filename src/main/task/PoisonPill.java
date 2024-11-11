package main.task;

import main.WorkerCounter;

public class PoisonPill extends Task {
	private WorkerCounter workerCounter;

	public PoisonPill(WorkerCounter workerCounter) {
		this.workerCounter = workerCounter;
	}

	@Override
	public void run() {
		workerCounter.completeWorker();

		throw new StopWorkerException("Worker stopped due to Poison Pill.");

	}
}
