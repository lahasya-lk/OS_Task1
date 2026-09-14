import matrix.Matrix;
import matrix.MatrixGenerator;
import threading.MatrixMultiplier;
import verification.TensorFlowVerifier;

public class Main {

    public static void main(String[] args) {

        int aRows = 2;
        int aColumns = 2;
        int bRows = 2;
        int bColumns = 2;

        if (args.length == 4) {
            aRows = Integer.parseInt(args[0]);
            aColumns = Integer.parseInt(args[1]);
            bRows = Integer.parseInt(args[2]);
            bColumns = Integer.parseInt(args[3]);
        }

        Matrix a = MatrixGenerator.random(aRows, aColumns);
        Matrix b = MatrixGenerator.random(bRows, bColumns);

        MatrixMultiplier multiplier = new MatrixMultiplier(4);

        long start = System.nanoTime();
        Matrix result = multiplier.multiply(a, b);
        long end = System.nanoTime();

        double javaTimeMs = (end - start) / 1_000_000.0;

        System.out.println("A: " + aRows + " × " + aColumns);
        System.out.println("B: " + bRows + " × " + bColumns);
        System.out.println("C: " + result.getRows() + " × " + result.getColumns());

        System.out.printf(
                "Java threaded computation: %.3f ms%n",
                javaTimeMs
        );

        long verificationStart = System.nanoTime();
        boolean verified = TensorFlowVerifier.verify(a, b, result);
        long verificationEnd = System.nanoTime();

        double tensorflowTimeMs =
                (verificationEnd - verificationStart) / 1_000_000.0;

        System.out.printf(
                "TensorFlow verification: %.3f ms%n",
                tensorflowTimeMs
        );

        System.out.println(
                "Verification: " + (verified ? "PASSED" : "FAILED")
        );
    }
}
