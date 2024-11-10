package main;

import main.task.Task;

public class Worker extends Thread {
    private final Task task;

    public Worker(Task task) {
        this.task = task;
    }

    @Override
    public void run() {
        task.run();
    }
}
