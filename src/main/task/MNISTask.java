package main.task;

import java.util.ArrayList;
import java.util.List;

import main.DistanceMonitor;
import main.Image;
import main.IntTuple;
import main.buffer.Buffer;

public class MNISTask extends Task {
	private Image image;
	private List<Image> images;
	private  DistanceMonitor distances;

	public MNISTask(Image image, List<Image> images, DistanceMonitor distances) {
		this.image = image;
		this.images = images;
		this.distances = distances;
	}

	@Override
    public void run() {
		for(Image other: images) {
			try {
				distances.addTuple(new IntTuple(other.getNumber(),  this.image.getDistanceTo(other)));
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		
    }
}
