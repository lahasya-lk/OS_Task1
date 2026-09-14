package threading;

import matrix.Matrix;

import java.util.function.Consumer;

public class MatrixTask implements Runnable {

    private final Matrix a;
    private final Matrix b;
    private final Matrix result;
    private final int startRow;
    private final int endRow;
    private final Consumer<ComputationEvent> eventListener;

    public MatrixTask(
            Matrix a,
            Matrix b,
            Matrix result,
            int startRow,
            int endRow) {

        this(
                a,
                b,
                result,
                startRow,
                endRow,
                null
        );
    }

    public MatrixTask(
            Matrix a,
            Matrix b,
            Matrix result,
            int startRow,
            int endRow,
            Consumer<ComputationEvent> eventListener) {

        this.a = a;
        this.b = b;
        this.result = result;
        this.startRow = startRow;
        this.endRow = endRow;
        this.eventListener = eventListener;
    }

    @Override
    public void run() {

        String workerName =
                Thread.currentThread().getName();

        for (int i = startRow; i < endRow; i++) {

            for (int j = 0; j < b.getColumns(); j++) {

                double sum = 0;

                for (int k = 0; k < a.getColumns(); k++) {

                    double aValue =
                            a.get(i, k);

                    double bValue =
                            b.get(k, j);

                    double product =
                            aValue * bValue;

                    sum += product;

                    if (eventListener != null) {

                        eventListener.accept(
                                new ComputationEvent(
                                        workerName,
                                        i,
                                        j,
                                        k,
                                        aValue,
                                        bValue
                                )
                        );
                    }
                }

                result.set(i, j, sum);
            }
        }

        System.out.println(
                workerName +
                " completed rows " +
                startRow +
                " to " +
                (endRow - 1)
        );
    }
}
