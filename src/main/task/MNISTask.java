package main.task;

import main.Buffer;
import main.Image;

public class MNISTask extends Task {
	private Image img1;
	private Image img2;
	private Buffer buffer;

	public MNISTask(Image img1, Buffer buffer) {
		this.img1 = img1;
		this.buffer = buffer;
	}

	@Override
    public void run() {
		try {
			Image img2 = buffer.retrieveImage();
	        System.out.println(img1.getDistanceTo(img2));
		} catch (Exception e) {
	        e.printStackTrace();
	    }
	
    }
}
