package main.task;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import main.Buffer;
import main.Image;

public class BufferManager extends Task {
	private Buffer buffer;

	public BufferManager(Buffer buffer) {
		this.buffer = buffer;
	}

	public static List<Image> parseImages() throws IOException {
		List<Image> images = new ArrayList<>();

		try (BufferedReader br = new BufferedReader(
				new FileReader("/home/oriana/Desktop/ConcuMinst/src/main/mnist_test.csv"))) {
			String line;
			while ((line = br.readLine()) != null) {
				String[] items = line.split(",");

				if (items.length > 0) {
					int firstItem = Integer.parseInt(items[0].trim());

					Integer[] restItems = new Integer[items.length - 1];
					for (int i = 1; i < items.length; i++) {
						restItems[i - 1] = Integer.parseInt(items[i].trim());
					}

					images.add(new Image(firstItem, restItems));
				}
			}
		}

		return images;
	}

	@Override
	public void run() {
	    try {
    	  List<Image> images = parseImages();
	  	    int i = 0;
	        while (i < images.size() && i <= 1000) {
	            buffer.addImage(images.get(i));
	            i++;
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}


}
