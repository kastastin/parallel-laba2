import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class FoxAlgorithm {
    private int numberOfThreads;
    Matrix A;
    Matrix B;

    public FoxAlgorithm(Matrix A, Matrix B, int n) {
        this.A = A;
        this.B = B;
        this.numberOfThreads = n;
    }

    public Matrix multiply() {
        Matrix multipliedMatrix = new Matrix(A.getXSize(), B.getYSize());

        if (!(A.getXSize() == B.getXSize() & A.getXSize() == A.getYSize() & B.getXSize() == B.getYSize())) {
            try {
                throw new Exception("Error!\nMatrix dimensions incorrect/");
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(-1);
            }
        }

        this.numberOfThreads = Math.min(this.numberOfThreads, A.getXSize());
        this.numberOfThreads = findNearestDivider(this.numberOfThreads, A.getXSize());
        int step = A.getXSize() / this.numberOfThreads;

        ExecutorService executor = Executors.newFixedThreadPool(this.numberOfThreads);
        ArrayList<Future> threads = new ArrayList<>();

        int[][] xSize = new int[numberOfThreads][numberOfThreads];
        int[][] ySize = new int[numberOfThreads][numberOfThreads];

        int xStep = 0;
        for (int x = 0; x < numberOfThreads; x++) {
            int yStep = 0;
            for (int y = 0; y < numberOfThreads; y++) {
                xSize[x][y] = xStep;
                ySize[x][y] = yStep;
                yStep += step;
            }
            xStep += step;
        }

        for (int z = 0; z < numberOfThreads; z++) {
            for (int x = 0; x < numberOfThreads; x++) {
                for (int y = 0; y < numberOfThreads; y++) {
                    int xStep1 = xSize[x][y];
                    int yStep1 = ySize[x][y];
                    int yStep2 = xSize[x][(x + z) % numberOfThreads];
                    int stepJ1 = ySize[x][(x + z) % numberOfThreads];
                    int xStep2 = xSize[(x + z) % numberOfThreads][y];
                    int stepJ2 = ySize[(x + z) % numberOfThreads][y];

                    ThreadFoxAlgorithm thread = new ThreadFoxAlgorithm(cloneBlock(A, yStep2, stepJ1, step), cloneBlock(B, xStep2, stepJ2, step), multipliedMatrix, xStep1, yStep1);
                    threads.add(executor.submit(thread));
                }
            }
        }

        for (Future map : threads) {
            try {
                map.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        executor.shutdown();
        return multipliedMatrix;
    }

    private int findNearestDivider(int i, int val) {
        int temp = i;
        while (temp > 1) {
            if (val % temp == 0) break;
            if (temp >= i) {
                temp++;
            } else {
                temp--;
            }

            if (temp > Math.sqrt(val)) {
                temp = Math.min(i, val / i) - 1;
            }
        }
        
        if (temp >= i) {
            return temp;
        } else {
            if (temp != 0) {
                return val / temp;
            } else {
                return val;
            }
        }
    }

    private Matrix cloneBlock(Matrix m, int x, int y, int size) {
        Matrix block = new Matrix(size, size);
        for (int z = 0; z < size; z++) {
            System.arraycopy(m.matrix[z + x], y, block.matrix[z], 0, size);
        }
        return block;
    }
}