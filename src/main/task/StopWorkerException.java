package main.task;

public class StopWorkerException extends RuntimeException {
    public StopWorkerException(String message) {
        super(message);
    }
}