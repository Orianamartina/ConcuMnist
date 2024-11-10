package main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Image {
	private int number;
	private Integer[] data;

	public Image(int number, Integer[] data) {
		this.number = number;
		this.data = data;
				
	}
	
	public int getDistanceTo(Image other) {
		
		int totalResult = 0;

		for (int i = 1; i < 728; i++) {
			int firstValue = this.data[i];
			int secondValue = other.getData()[i];
			int squaredDifference = (firstValue - secondValue) * (firstValue - secondValue);
			totalResult += squaredDifference;
		}
		return totalResult;

	}
	
	public Integer[] getData(){
		return this.data;
	}
	public int getNumber() {
		return this.number;
	}
}
