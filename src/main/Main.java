package main;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.File;
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

	public static List<List<Image>> parseImageSubsets(int n) throws IOException {
		List<List<Image>> imageSubsets = new ArrayList<>();
		List<Image> currentSubset = new ArrayList<>();

		try (BufferedReader br = loadCsv("mnist_test.csv")) {
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
		if (mode.equals("imagen")) {
			System.out.print("Ingrese la ruta de la imagen (.png): ");
			filePath = scanner.nextLine().trim();
			if (!filePath.endsWith(".png") || !new File(filePath).exists()) {
				System.out.println("Archivo de imagen no encontrado o inválido.");
	
			}
		} else {
			System.out.print("Ingrese la ruta del archivo CSV: ");
			filePath = "";
//			filePath = scanner.nextLine().trim();
//			if (!filePath.endsWith(".csv") || !new File(filePath).exists()) {
//				System.out.println("Archivo CSV no encontrado o inválido.");
//				
//			}
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

		TaskBuffer taskBuffer = new TaskBuffer(tamaño_buffer);
		List<List<Image>> imageSubsets = parseImageSubsets((1000));
		DistanceMonitor distanceMonitor = new DistanceMonitor();
		ThreadPool threadPool = new ThreadPool(cant_threads, taskBuffer);
		
        System.out.println("\nModo seleccionado: " + mode);
        System.out.println("Ruta del archivo: " + filePath);
        System.out.println("Cantidad de vecinos más cercanos (k): " + k);
        System.out.println("Cantidad de threads: " + cant_threads);
        System.out.println("Tamaño del buffer: " + tamaño_buffer);
        
        if (mode.equals("test")) {
			Image image = imageSubsets.get(0).get(0);

			threadPool.runWorkers();

			for (List<Image> subset : imageSubsets) {
				taskBuffer.addItem(new MNISTask(image, subset, distanceMonitor));
			}
			for (int i = 0; i <= cant_threads; i++) {
				taskBuffer.addItem(new PoisonPill());
			}

			System.out.println(distanceMonitor.predictNumber(k));
		} else {

			Image image = ImageLoader.loadImage(filePath);

			threadPool.runWorkers();

			for (List<Image> subset : imageSubsets) {
				taskBuffer.addItem(new MNISTask(image, subset, distanceMonitor));
			}
			for (int i = 0; i <= cant_threads; i++) {
				taskBuffer.addItem(new PoisonPill());
			}

			System.out.println(distanceMonitor.predictNumber(k));
		}

	}

}
