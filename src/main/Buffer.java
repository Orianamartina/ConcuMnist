package main;

import java.util.ArrayList;
import java.util.List;

//
//Una clase Buffer (implementada como un monitor utilizando m ́etodos synchroni-
//zed) que act ́ua como una cola FIFO concurrente de capacidad acotada. Es decir,
//
//bloquea a un lector intentando sacar un elemento cuando est ́a vac ́ıa y bloquea a
//un productor intentando agregar un elemento cuando est ́a llena. La capacidad del
//Buffer es la que corresponde al par ́ametro tamano_buffer.

public class Buffer {
	private List<Image> images;
	private int bufferSize;
	
	public Buffer(int bufferSize) {
		this.images = new ArrayList<>();	
		this.bufferSize = bufferSize;
		}
	
	public synchronized void addImage(Image image) throws InterruptedException {
		while (images.size() == bufferSize) {
			System.out.println("Estoy esperando para agregar");
			wait();
		}
		images.add(image);
	}
	
	public synchronized Image retrieveImage() throws InterruptedException {
		while (images.size() == 0) {
			System.out.println("Estoy esperando para sacar");
			wait();
		}
		return images.removeLast();
	}
}
