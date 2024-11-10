package main;

import main.buffer.TaskBuffer;
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
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Worker was interrupted and is stopping.");
        }
    }
}
