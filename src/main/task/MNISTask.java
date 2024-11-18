package main.task;

import java.util.List;
import java.util.PriorityQueue;

import main.DistanceMonitor;
import main.Image;
import main.IntTuple;

public class MNISTask extends Task {
    private Image image;
    private List<Image> images;
    private DistanceMonitor distances;
    private int k;

    public MNISTask(Image image, List<Image> images, DistanceMonitor distances, int k) {
        this.image = image;
        this.images = images;
        this.distances = distances;
        this.k = k;
    }

    @Override
    public void run() {
        PriorityQueue<IntTuple> closestDistances = new PriorityQueue<>(
            (t1, t2) -> Integer.compare(t2.second(), t1.second())
        );
        for (Image other : images) {
            int distance = this.image.getDistanceTo(other);
            IntTuple tuple = new IntTuple(other.getNumber(), distance);

            closestDistances.add(tuple);
            if (closestDistances.size() > k) {
                closestDistances.poll();
            }
        }
        try {
            while (!closestDistances.isEmpty()) {
                distances.addTuple(this.image, closestDistances.poll());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
