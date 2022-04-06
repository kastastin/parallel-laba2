import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class StripedAlgorithm {
    private final int numberOfThreads;
    Matrix A;
    Matrix B;

    StripedAlgorithm(Matrix A, Matrix B, int n) {
        this.A = A;
        this.B = B;
        this.numberOfThreads = n;
    }

    public Matrix multiply() {
        Matrix multipliedMatrix = new Matrix(A.getXSize(), B.getYSize());
        ExecutorService executor = Executors.newFixedThreadPool(this.numberOfThreads);
        List<Future<double[]>> list = new ArrayList<>();
    
        for (int x = 0; x < A.getXSize(); x++) {
            Callable<double[]> worker = new CallableStripedAlgorithm(B, A.getRow(x), x);
            Future<double[]> submit = executor.submit(worker);
            list.add(submit);
        }
    
        for (int x = 0; x < list.size(); x++) {
            try {
                multipliedMatrix.matrix[x] = list.get(x).get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
    
        executor.shutdown();
        return multipliedMatrix;
    }
}