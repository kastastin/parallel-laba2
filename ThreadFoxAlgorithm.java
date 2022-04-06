public class ThreadFoxAlgorithm extends Thread {
    private final int xStep;
    private final int yStep;
    private final Matrix A;
    private final Matrix B;
    private final Matrix multipliedMatrix;

    public ThreadFoxAlgorithm(Matrix A, Matrix B, Matrix C, int stepI, int stepJ) {
        this.A = A;
        this.B = B;
        this.multipliedMatrix = C;
        this.xStep = stepI;
        this.yStep = stepJ;
    }

    public Matrix multiplyBlocks() {
        Matrix multipliedBlock = new Matrix(A.getYSize(), B.getXSize());
        for (int x = 0; x < A.getXSize(); x++) {
            for (int y = 0; y < B.getYSize(); y++) {
                for (int z = 0; z < A.getYSize(); z++) {
                    multipliedBlock.matrix[x][y] += A.matrix[x][z] * B.matrix[z][y];
                }
            }
        }
        return multipliedBlock;
    }

    @Override
    public void run() {
        Matrix block = multiplyBlocks();

        for (int x = 0; x < block.getXSize(); x++) {
            for (int y = 0; y < block.getYSize(); y++) {
                multipliedMatrix.matrix[x + xStep][y + yStep] += block.matrix[x][y];
            }
        }
    }
}