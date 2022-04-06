import java.util.concurrent.Callable;

public class CallableStripedAlgorithm implements Callable<double[]> {
    private Matrix B;
    private final double[] row;
    private final int rowI;
    private double[] result;

    public CallableStripedAlgorithm(Matrix B, double[] r, int ri) {
        this.B = B;
        this.row = r;
        this.rowI = ri;
        this.result = new double[B.getYSize()];
    }

    public int getRowIndex() { return rowI; }

    @Override
    public double[] call() {
        for (int y = 0; y < B.getYSize(); y++) {
            for (int x = 0; x < row.length; x++) {
                result[y] += row[x] * B.matrix[x][y];
            }
        }

        return this.result;
    }
}