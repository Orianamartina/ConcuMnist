package main.task;

public class PoisonPill extends Task {
	@Override
	public void run() {

		throw new StopWorkerException("Worker stopped due to Poison Pill.");

	}
}
