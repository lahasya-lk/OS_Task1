package matrix;

import java.util.Random;

public class MatrixGenerator {

    public static Matrix random(int rows, int columns) {
        Matrix matrix = new Matrix(rows, columns);
        Random random = new Random();

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                matrix.set(i, j, random.nextInt(10));
            }
        }

        return matrix;
    }
}