package main;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import main.Image;
import javax.imageio.ImageIO;

public class ImageLoader {

	public static Image loadImage(String imgPath) throws IOException {
		File file = new File(imgPath);
		BufferedImage bufferedImage = ImageIO.read(file);

		WritableRaster raster = bufferedImage.getRaster();
		byte[] datos = ((DataBufferByte) raster.getDataBuffer()).getData();

		Integer[] pixelData = new Integer[datos.length];
		for (int i = 0; i < datos.length; i++) {
			pixelData[i] = (int) (datos[i]);
			System.out.println((int) (datos[i]) & 0xFF);
		}
		System.out.println(pixelData.length);
		return new Image(5, pixelData);
	}

}