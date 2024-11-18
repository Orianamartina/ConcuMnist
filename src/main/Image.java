package main;

import java.util.Arrays;

public class Image {
    private int number;
    private Integer[] data;

    public Image(int number, Integer[] data) {
        this.number = number;
        this.data = data;
    }

    public int getDistanceTo(Image other) {
        int totalResult = 0;

        for (int i = 1; i < 784; i++) {
            int firstValue = this.data[i];
            int secondValue = other.getData()[i];
            int squaredDifference = (firstValue - secondValue) * (firstValue - secondValue);
            totalResult += squaredDifference;
        }
        return totalResult;
    }

    public Integer[] getData() {
        return this.data;
    }

    public int getNumber() {
        return this.number;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Image other = (Image) obj;
        return number == other.number && Arrays.equals(data, other.data);
    }

    @Override
    public int hashCode() {
        int result = Integer.hashCode(number);
        result = 31 * result + Arrays.hashCode(data);
        return result;
    }
}
