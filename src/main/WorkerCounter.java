package main;

public class WorkerCounter {
    private int maxThreadCount;

    public WorkerCounter(int maxThreadCount) {
        this.maxThreadCount = maxThreadCount;
    }

    public synchronized void completeWorker() {
        maxThreadCount--;
        if (maxThreadCount == 0) {
            notifyAll();
        }
    }

    public synchronized void waitForCompletion() throws InterruptedException {
        while (maxThreadCount > 0) {
            wait();
        }
    }
}
