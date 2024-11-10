package main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import main.task.BufferManager;
import main.task.MNISTask;

public class Main {

	
	public static void main(String[] args) throws IOException, InterruptedException {
		Buffer buffer = new Buffer(3);
		Worker bufferManager = new Worker(new BufferManager(buffer));
		
		int i = 1;
		while (i != 1000) {
			Image img1 = buffer.retrieveImage();
			Worker worker = new Worker(new MNISTask(img1, buffer));
			worker.start();
			i++;
		}
		
	
	}
	
}
