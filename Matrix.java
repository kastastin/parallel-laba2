import java.util.Arrays;

public class Matrix {
    public double[][] matrix;
    private final int xSize;
    private final int ySize;
    
    Matrix(int x, int y) {
        this.matrix = new double[x][y];
        this.xSize = x;
        this.ySize = y;
    }

    public int getXSize() { return this.xSize; }
    public int getYSize() { return this.ySize; }

    public double[] getRow(int index) {
         return this.matrix[index];
    }

    public double[] getColumn(int i) {
        return Arrays.stream(matrix).mapToDouble(d -> d[i]).toArray();
    }

    public void fillMatrixRandomly() {
        for (int x = 0; x < this.xSize; x++) {
            for (int y = 0; y < this.ySize; y++) {
                this.matrix[x][y] = Math.random() * 10;
            }
        }
    }

    public void displayMatrix() {
        System.out.println();
        for (int x = 0; x < this.xSize; x++) {
            for (int y = 0; y < this.ySize; y++) {
                System.out.printf("%10.1f", this.matrix[x][y]);
            }
            System.out.println();
        }
        System.out.println();
    }

    public void transpose() {
        double[][] transposedMatrix = new double[this.getYSize()][this.getXSize()];
        for (int x = 0; x < matrix[0].length; x++) {
            for (int y = 0; y < matrix.length; y++) {
                transposedMatrix[x][y] = this.matrix[x][y];
            }
        }
        this.matrix = transposedMatrix;
    }
}