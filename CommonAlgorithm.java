public class CommonAlgorithm {
    Matrix A;
    Matrix B;

    CommonAlgorithm(Matrix A, Matrix B) {
        this.A = A;
        this.B = B;
    }

    public Matrix multiply() {
        Matrix multilpiedMatrix = new Matrix(A.getXSize(), B.getYSize());

        for (int x = 0; x < A.getXSize(); x++) {
            for (int y = 0; y < B.getYSize(); y++) {
                multilpiedMatrix.matrix[x][y] = 0;

                for (int z = 0; z < A.getYSize(); z++) {
                    multilpiedMatrix.matrix[x][y] += A.matrix[x][z] * B.matrix[z][y];
                }
            }
        }

        return multilpiedMatrix;
    }
}