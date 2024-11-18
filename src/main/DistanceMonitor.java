package main;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class DistanceMonitor {
    private Map<Image, PriorityQueue<IntTuple>> imageTuplesMap;
    private boolean writer;
    private int k;

    public DistanceMonitor(int k) {
        this.imageTuplesMap = new HashMap<>();
        this.writer = false;
        this.k = k;
    }

    public synchronized void addTuple(Image image, IntTuple tuple) throws InterruptedException {
        while (this.writer) {
            wait();
        }

        this.writer = true;
        
        PriorityQueue<IntTuple> queue = imageTuplesMap.computeIfAbsent(image, k -> 
            new PriorityQueue<>((t1, t2) -> Integer.compare(t2.second(), t1.second()))
        );
        
        queue.add(tuple);

        if (queue.size() > k) {
            queue.poll();
        }

        this.writer = false;
        notifyAll();
    }

    public synchronized int predictNumberForOneImage() throws IllegalStateException {
        if (imageTuplesMap.size() != 1) {
            throw new IllegalStateException("Error: predictNumberForOneImage requires exactly one image in the map.");
        }

        Image image = imageTuplesMap.keySet().iterator().next();
        PriorityQueue<IntTuple> tuples = new PriorityQueue<>(imageTuplesMap.get(image));

        Map<Integer, Integer> countMap = new HashMap<>();
        while (!tuples.isEmpty()) {
            IntTuple tuple = tuples.poll();
            int number = tuple.first();
            countMap.put(number, countMap.getOrDefault(number, 0) + 1);
        }

        int mostFrequentNumber = -1;
        int maxCount = 0;
        for (Map.Entry<Integer, Integer> entry : countMap.entrySet()) {
            if (entry.getValue() > maxCount) {
                mostFrequentNumber = entry.getKey();
                maxCount = entry.getValue();
            }
        }
        
        return mostFrequentNumber;
    }

    public synchronized double calculateSuccessRatio() {
        int totalImages = imageTuplesMap.size();
        int correctPredictions = 0;

        for (Map.Entry<Image, PriorityQueue<IntTuple>> entry : imageTuplesMap.entrySet()) {

            Image image = entry.getKey();
            PriorityQueue<IntTuple> tuples = new PriorityQueue<>(entry.getValue());

            Map<Integer, Integer> countMap = new HashMap<>();
            while (!tuples.isEmpty()) {
                IntTuple tuple = tuples.poll();
                int number = tuple.first();
                countMap.put(number, countMap.getOrDefault(number, 0) + 1);
            }

            int mostFrequentNumber = -1;
            int maxCount = 0;
            for (Map.Entry<Integer, Integer> countEntry : countMap.entrySet()) {
                if (countEntry.getValue() > maxCount) {
                    mostFrequentNumber = countEntry.getKey();
                    maxCount = countEntry.getValue();
                }
            }

            if (mostFrequentNumber == image.getNumber()) {
                correctPredictions++;
            }
        }

        return totalImages == 0 ? 0.0 : ((double) correctPredictions / totalImages) * 100;
    }
}
