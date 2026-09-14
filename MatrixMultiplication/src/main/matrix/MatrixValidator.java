package matrix;

public class MatrixValidator {

    public static void validateForMultiplication(Matrix a, Matrix b) {
        if (a.getColumns() != b.getRows()) {
            throw new IllegalArgumentException(
                "Cannot multiply: A columns must equal B rows."
            );
        }
    }
}
