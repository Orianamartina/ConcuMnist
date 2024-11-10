package main;

import java.io.IOException;

import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import main.task.MNISTask;
import main.task.PoisonPill;
import main.Image;
import main.buffer.TaskBuffer;

public class Main {

	
	public static Image loadImage(String path) throws IOException {
		return ImageLoader.loadImage(path);
	}

    public static BufferedReader loadCsv(String path) throws IOException {
        return new BufferedReader(new FileReader(path));
    }

    public static List<List<Image>> parseImageSubsets(int n) throws IOException {
        List<List<Image>> imageSubsets = new ArrayList<>();
        List<Image> currentSubset = new ArrayList<>();

        try (BufferedReader br = loadCsv("/home/oriana/Desktop/concuMinst/src/main/mnist_test.csv")) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] items = line.split(",");

                if (items.length > 0) {
                    int firstItem = Integer.parseInt(items[0].trim());

                    Integer[] restItems = new Integer[items.length - 1];
                    for (int i = 1; i < items.length; i++) {
                        restItems[i - 1] = Integer.parseInt(items[i].trim());
                    }

                    currentSubset.add(new Image(firstItem, restItems));

                    if (currentSubset.size() == n) {
                        imageSubsets.add(new ArrayList<>(currentSubset));
                        currentSubset.clear();
                    }
                }
            }

            if (!currentSubset.isEmpty()) {
                imageSubsets.add(new ArrayList<>(currentSubset));
            }
        }

        return imageSubsets;
    }

	public static void main(String[] args) throws IOException, InterruptedException {
		String modo = "test";
		int k = 13;
		int cant_threads = 10;
		int tamaño_buffer = 20;

		
		TaskBuffer taskBuffer = new TaskBuffer(tamaño_buffer);
		List<List<Image>> imageSubsets = parseImageSubsets((1000));
		Image image = imageSubsets.get(0).get(0);
		DistanceMonitor distances = new DistanceMonitor();
		
		ThreadPool threadPool = new ThreadPool(cant_threads, taskBuffer);
		
		threadPool.runWorkers();
		
		for (List<Image> subset: imageSubsets) {
    		taskBuffer.addItem(new MNISTask(image, subset, distances));
    	}
  	  	for (int i = 0; i <=cant_threads;  i++) {
  	  		taskBuffer.addItem(new PoisonPill());
  	  	}
  	  	
  	  	
  	  	System.out.println(distances.predictNumber(k));
		
		
	}

}
