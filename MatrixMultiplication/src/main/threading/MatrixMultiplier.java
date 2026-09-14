package threading;

import matrix.Matrix;
import matrix.MatrixValidator;

import java.util.function.Consumer;

public class MatrixMultiplier {

    private final int workerCount;

    public MatrixMultiplier(int workerCount) {
        this.workerCount = workerCount;
    }

    public Matrix multiply(Matrix a, Matrix b) {

        return multiply(a, b, null);
    }

    public Matrix multiply(
            Matrix a,
            Matrix b,
            Consumer<ComputationEvent> eventListener) {

        MatrixValidator.validateForMultiplication(a, b);

        Matrix result =
                new Matrix(
                        a.getRows(),
                        b.getColumns()
                );

        int rows = a.getRows();

        int activeWorkers =
                Math.min(workerCount, rows);

        int rowsPerWorker =
                (int) Math.ceil(
                        (double) rows / activeWorkers
                );

        MatrixTask[] tasks =
                new MatrixTask[activeWorkers];


        for (int i = 0; i < activeWorkers; i++) {

            int startRow =
                    i * rowsPerWorker;

            int endRow =
                    Math.min(
                            startRow + rowsPerWorker,
                            rows
                    );


            tasks[i] =
                    new MatrixTask(
                            a,
                            b,
                            result,
                            startRow,
                            endRow,
                            eventListener
                    );
        }


        WorkerManager workerManager =
                new WorkerManager(activeWorkers);

        workerManager.execute(tasks);

        return result;
    }
}
