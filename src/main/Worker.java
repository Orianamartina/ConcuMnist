package main;

import main.buffer.TaskBuffer;
import main.task.StopWorkerException;
import main.task.Task;


public class Worker extends Thread {
    private final TaskBuffer taskBuffer;

    public Worker(TaskBuffer taskBuffer) {
        this.taskBuffer = taskBuffer;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Task task = taskBuffer.retrieveItem();
                task.run();
            }
        } 
        catch (StopWorkerException | InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
