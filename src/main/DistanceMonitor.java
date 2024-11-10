package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DistanceMonitor {
	private List<IntTuple> tuples;
	private boolean writer;
	
	public DistanceMonitor() {
		this.tuples = new ArrayList<>();
	}
	public synchronized void addTupple(IntTuple tuple) throws InterruptedException {
		while(this.writer) {
			wait();
		}
		tuples.add(tuple);
		tuples.sort((t1, t2) -> Integer.compare(t1.second(), t2.second()));

		notifyAll();
	}
	
	 public int predictNumber(int k) {
	        if (k > tuples.size()) {
	            k = tuples.size();
	        }
	        System.out.println(tuples);
	        Map<Integer, Integer> countMap = new HashMap<>();

	        for (int i = 0; i < k; i++) {
	            int number = tuples.get(i).first();
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
	
}
