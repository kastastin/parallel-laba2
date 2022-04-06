import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class App {
    public static void main(String[] args) {
        // displayCommonOpperations(1000, 1000, false);
        // sizeExperiment();
        threadsExperiment();
    }

    public static void displayCommonOpperations(int xSize, int ySize, boolean isDisplayMatrix) {
        Matrix A = new Matrix(xSize, ySize);
        Matrix B = new Matrix(xSize, ySize);
        A.fillMatrixRandomly();
        B.fillMatrixRandomly();

        // Алгоритм послідовного перемноження матриць
        CommonAlgorithm common = new CommonAlgorithm(A, B);
        long time = System.nanoTime();
        Matrix multipliedMatrix = common.multiply();
        time = System.nanoTime() - time;
        if (isDisplayMatrix) {
          System.out.println("\tCommon multiplied matrix:");
          multipliedMatrix.displayMatrix();
        }
        displayTime(time, "Common", xSize, ySize);


        // Стріковий алгоритм перемноження матриць
        StripedAlgorithm striped = new StripedAlgorithm(A, B, 10);
        time = System.nanoTime();
        multipliedMatrix = striped.multiply();
        time = System.nanoTime() - time;
        if (isDisplayMatrix) {
          System.out.println("\tStriped multiplied matrix:");
          multipliedMatrix.displayMatrix();
        }
        displayTime(time, "Striped", xSize, ySize);


        // Алгоритм Фокса перемноження матриць
        FoxAlgorithm fox = new FoxAlgorithm(A, B, 10);
        time = System.nanoTime();
        multipliedMatrix = fox.multiply();
        time = System.nanoTime() - time;
        if (isDisplayMatrix) {
          System.out.println("\tFox multiplied matrix:");
          multipliedMatrix.displayMatrix();
        }
        displayTime(time, "Fox", xSize, ySize);


    }

    public static void displayTime(long t, String matrixName, int x, int y) {
        System.out.printf("\t%s [%d:%d] multiply time: %d ms\n\n", matrixName, x, y, t / 1000000);
    }

    public static void sizeExperiment() {
      System.out.printf("\n\tSize experiment:\n\n");
      int numberOfExperiments = 5;
      int numberOfTreads = Runtime.getRuntime().availableProcessors();
      
      int[] matrixSizes = new int[] {5, 10, 50, 100, 250, 500, 750, 1000, 1500};
      Map<Integer, Long> stripedAlgorithmTime = new HashMap<>();
      Map<Integer, Long> foxAlgorithmTime = new HashMap<>();
    
      for (int size : matrixSizes) {
          Matrix A = new Matrix(size, size);
          Matrix B = new Matrix(size, size);
          A.fillMatrixRandomly();
          B.fillMatrixRandomly();
      
          // Стрічковий алгоритм
          StripedAlgorithm striped = new StripedAlgorithm(A, B, numberOfTreads);
          long allTime = 0;
          for (int i = 0; i < numberOfExperiments; i++) {
              long time = System.nanoTime();
              Matrix C = striped.multiply();
              allTime += System.nanoTime() - time;
          }
          allTime = allTime / numberOfExperiments;
          stripedAlgorithmTime.put(size, allTime / 1000000);
      
          // алгоритм Фокса
          FoxAlgorithm fox = new FoxAlgorithm(A, B, numberOfTreads);
          allTime = 0;
          for (int i = 0; i < numberOfExperiments; i++) {
              long time = System.nanoTime();
              Matrix C = fox.multiply();
              allTime += System.nanoTime() - time;
          }
          allTime /= numberOfExperiments;
          foxAlgorithmTime.put(size, allTime / 1000000);
      }
    
      List<Integer> keys = stripedAlgorithmTime.keySet().stream().sorted().collect(Collectors.toList());
      System.out.printf("\tMatrix size: ");
      for (int key : keys) System.out.printf("  %5d ", key);
      System.out.println();
      System.out.printf("\tStriped time:");
      for (int key : keys) System.out.printf("  %5d ", stripedAlgorithmTime.get(key));
      System.out.printf("\n\tFox time:    ");
      for (int key : keys) System.out.printf("  %5d ", foxAlgorithmTime.get(key));
      System.out.println();
    }

    public static void threadsExperiment() {
        System.out.println("\n\tThreads experiment:\n\n");
        int numberOfExperiment = 5;
        int matrixSize = 1200;
      
        int[] numberOfStripedThreads = new int[] {5, 10, 50, 100, 250, 500};
        int[] numberOfFoxThreads = new int[] {4, 5, 10, 25, 50};
        Map<Integer, Long> stripedAlgorithmTime = new HashMap<>();
        Map<Integer, Long> foxAlgorithmTime = new HashMap<>();
      
        Matrix A = new Matrix(matrixSize, matrixSize);
        Matrix B = new Matrix(matrixSize, matrixSize);
        A.fillMatrixRandomly();
        B.fillMatrixRandomly();
      
        for (int thread : numberOfStripedThreads) {
            StripedAlgorithm striped = new StripedAlgorithm(A, B, thread);
            long allTime = 0;
            for (int i = 0; i < numberOfExperiment; i++) {
                long time = System.nanoTime();
                Matrix C = striped.multiply();
                allTime += System.nanoTime() - time;
            }
            allTime /= numberOfExperiment;
            stripedAlgorithmTime.put(thread, allTime / 1000000);
        }
      
        for (int thread : numberOfFoxThreads) {
            FoxAlgorithm fox = new FoxAlgorithm(A, B, thread);
            long allTime = 0;
            for (int i = 0; i < numberOfExperiment; i++) {
                long time = System.nanoTime();
                Matrix C = fox.multiply();
                allTime += System.nanoTime() - time;
            }
            allTime = allTime / numberOfExperiment;
            foxAlgorithmTime.put(thread, allTime / 1000000);
        }
      
        List<Integer> stripedKeys = stripedAlgorithmTime.keySet().stream().sorted().collect(Collectors.toList());

        System.out.printf("\tStriped algorithm:\n\tThreads:");
        for (int key : stripedKeys) System.out.printf("  %5d ", key);
        System.out.printf("\n\tTime:   ");
        for (int key : stripedKeys) System.out.printf("  %5d ", stripedAlgorithmTime.get(key));
      
        List<Integer> keysFox = foxAlgorithmTime.keySet().stream().sorted().collect(Collectors.toList());
        System.out.printf("\n\n\tFox algorithm:\n\tThreads:");
        for (int key : keysFox) System.out.printf("  %5d ", key);
        System.out.printf("\n\tTime:   ");
        for (int key : keysFox) System.out.printf("  %5d ", foxAlgorithmTime.get(key));
        System.out.println();
    }
}