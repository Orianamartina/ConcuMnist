package main;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
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
		InputStream inputStream = Main.class.getClassLoader().getResourceAsStream(path);

		return new BufferedReader(new InputStreamReader(inputStream));
	}

	public static BufferedReader loadTestCsv(String path) throws IOException {
		FileInputStream fileInputStream = new FileInputStream(path);
		return new BufferedReader(new InputStreamReader(fileInputStream));
	}

	public static List<List<Image>> parseImageSubsets(int n) throws IOException {
		List<List<Image>> imageSubsets = new ArrayList<>();
		List<Image> currentSubset = new ArrayList<>();

		try (BufferedReader br = loadCsv("mnist_train.csv")) {
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

	public static List<Image> parseImageList(String path) throws IOException {
		List<Image> currentList = new ArrayList<>();
		try (BufferedReader br = loadTestCsv(path)) {
			String line;

			while ((line = br.readLine()) != null) {

				line = br.readLine();
				String[] items = line.split(",");

				if (items.length > 0) {
					int firstItem = Integer.parseInt(items[0].trim());

					Integer[] restItems = new Integer[items.length - 1];
					for (int i = 1; i < items.length; i++) {
						restItems[i - 1] = Integer.parseInt(items[i].trim());
					}
					currentList.add(new Image(firstItem, restItems));
				}

			}
		}
		return currentList;
	}

	public static void main(String[] args) throws IOException, InterruptedException {

		Scanner scanner = new Scanner(System.in);

		// Step 1: Seleccionar el modo
		String mode = "";
		while (true) {
			System.out.print("Seleccione modo: test o imagen: ");
			mode = scanner.nextLine().trim().toLowerCase();

			if (mode.equals("test") || mode.equals("imagen")) {
				break;
			} else {
				System.out.println("Modo inválido. Por favor ingrese 'test' o 'imagen'.");
			}
		}
		// Step 2: Cargar el archivo
		String filePath = "";
		while (true) {
			if (mode.equals("imagen")) {
				System.out.print("Ingrese la ruta de la imagen (.png): ");
				filePath = scanner.nextLine().trim();
				if (!filePath.endsWith(".png") || !new File(filePath).exists()) {
					System.out.println("Archivo de imagen no encontrado o inválido.");

				} else {
					break;
				}
			} else {
				System.out.print("Ingrese la ruta del archivo CSV: ");
				filePath = "";
				filePath = scanner.nextLine().trim();
				if (!filePath.endsWith(".csv") || !new File(filePath).exists()) {
					System.out.println("Archivo CSV no encontrado o inválido.");

				} else {
					break;
				}
			}
		}
		// step 3: obtener k
		int k = 0;
		while (true) {
			System.out.print("Seleccione la cantidad de vecinos más cercanos: ");
			try {
				k = Integer.parseInt(scanner.nextLine().trim());
				if (k > 0) {
					break;
				} else {
					System.out.println("Por favor, ingrese un número mayor que 0.");
				}
			} catch (NumberFormatException e) {
				System.out.println("Entrada inválida. Por favor, ingrese un número entero.");
			}
		}
		// Step 4: obtener cant_threads
		int cant_threads = 0;
		while (true) {
			System.out.print("Ingrese la cantidad de threads: ");
			try {
				cant_threads = Integer.parseInt(scanner.nextLine().trim());
				if (cant_threads > 0) {
					break;
				} else {
					System.out.println("Por favor, ingrese un número mayor que 0.");
				}
			} catch (NumberFormatException e) {
				System.out.println("Entrada inválida. Por favor, ingrese un número entero.");
			}
		}

		// Step 5: obtener tamaño buffer
		int tamaño_buffer = 0;
		while (true) {
			System.out.print("Ingrese el tamaño del buffer: ");
			try {
				tamaño_buffer = Integer.parseInt(scanner.nextLine().trim());
				if (tamaño_buffer > 0) {
					break;
				} else {
					System.out.println("Por favor, ingrese un número mayor que 0.");
				}
			} catch (NumberFormatException e) {
				System.out.println("Entrada inválida. Por favor, ingrese un número entero.");
			}
		}

		scanner.close();

		System.out.println("\nModo seleccionado: " + mode);
		System.out.println("Ruta del archivo: " + filePath);
		System.out.println("Cantidad de vecinos más cercanos (k): " + k);
		System.out.println("Cantidad de threads: " + cant_threads);
		System.out.println("Tamaño del buffer: " + tamaño_buffer);

		TaskBuffer taskBuffer = new TaskBuffer(tamaño_buffer);
		List<List<Image>> imageSubsets = parseImageSubsets(1000);
		DistanceMonitor distanceMonitor = new DistanceMonitor(k);
		ThreadPool threadPool = new ThreadPool(cant_threads, taskBuffer);
		WorkerCounter workerCounter = new WorkerCounter(cant_threads);

		Long startTime;
		
		System.out.println("Comenzando analisis...");
		System.out.println(" ");
		if (mode.equals("test")) {
			List<Image> images = parseImageList(filePath);
			startTime = System.currentTimeMillis();

			threadPool.runWorkers();

			for (Image image : images) {
				for (List<Image> subset : imageSubsets) {
					taskBuffer.addItem(new MNISTask(image, subset, distanceMonitor, k));
				}
			}

			for (int i = 0; i <= cant_threads; i++) {
				taskBuffer.addItem(new PoisonPill(workerCounter));
			}

			workerCounter.waitForCompletion();
			System.out.println("Ratio de éxito: " + distanceMonitor.calculateSuccessRatio() + "%");
		} else {

			Image image = ImageLoader.loadImage(filePath);
			startTime = System.currentTimeMillis();

			threadPool.runWorkers();

			for (List<Image> subset : imageSubsets) {
				taskBuffer.addItem(new MNISTask(image, subset, distanceMonitor, k));
			}

			int i = 0;
			while (i <= cant_threads) {
				taskBuffer.addItem(new PoisonPill(workerCounter));
				i++;
			}

			workerCounter.waitForCompletion();
			System.out.println("Resultado: " + distanceMonitor.predictNumberForOneImage());
		}
		Long endTime = System.currentTimeMillis();
		long elapsedTimeMillis = endTime - startTime;

		long elapsedTimeSeconds = elapsedTimeMillis / 1000;

		long minutes = elapsedTimeSeconds / 60;
		long seconds = elapsedTimeSeconds % 60;

		System.out.println("Tiempo de finalización: " + minutes + " minutos y " + seconds + " segundos");


	}

}
