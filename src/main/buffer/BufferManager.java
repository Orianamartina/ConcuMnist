package main.buffer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import main.Image;
import main.task.MNISTask;
import main.task.Task;

public class BufferManager extends Task {
	private TaskBuffer buffer;
	private List<List<Image>> subsets;
	private Image image;

	public BufferManager(TaskBuffer buffer, List<List<Image>> subsets, Image image) {
		this.buffer = buffer;
		this.subsets = subsets;
		this.image = image;
	}


	@Override
	public void run() {
	    try {
	    	for (List<Image> subset: subsets) {
	    		buffer.addItem(new MNISTask(image, subset));
	    	}
	  	  
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}


}
