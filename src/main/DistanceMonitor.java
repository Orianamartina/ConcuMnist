package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class DistanceMonitor {
    private PriorityQueue<IntTuple> tuples;
    private boolean writer;

    public DistanceMonitor() {
        this.tuples = new PriorityQueue<>((t1, t2) -> Integer.compare(t1.second(), t2.second()));
        this.writer = false;
    }

    public synchronized void addTuple(IntTuple tuple) throws InterruptedException {
        while (this.writer) {
            wait();
        }
        
        this.writer = true;
        tuples.add(tuple);
        this.writer = false;
        notifyAll();
    }


    public synchronized int predictNumber(int k) {
        k = Math.min(k, tuples.size());
        List<IntTuple> topKElements = new ArrayList<>();
        PriorityQueue<IntTuple> tempQueue = new PriorityQueue<>(tuples);

        for (int i = 0; i < k; i++) {
        	IntTuple iem = tempQueue.poll();
            topKElements.add(iem);
        }
      
        Map<Integer, Integer> countMap = new HashMap<>();
        for (IntTuple tuple : topKElements) {
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
//        System.out.println(tuples.size());
//        System.out.println("Amount of compared rows:should be 10000/60000");
        return mostFrequentNumber;
    }

}
