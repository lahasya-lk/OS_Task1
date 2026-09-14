package threading;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class WorkerManager {

    private final int workerCount;

    public WorkerManager(int workerCount) {
        this.workerCount = workerCount;
    }

    public void execute(MatrixTask[] tasks) {
        ExecutorService executor =
                Executors.newFixedThreadPool(workerCount);

        try {
            Future<?>[] futures = new Future<?>[tasks.length];

            for (int i = 0; i < tasks.length; i++) {
                futures[i] = executor.submit(tasks[i]);
            }

            for (int i = 0; i < tasks.length; i++) {
                try {
                    futures[i].get();
                } catch (Exception e) {
                    System.out.println(
                            "Task " + i + " failed. Retrying..."
                    );

                    executor.submit(tasks[i]).get();
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(
                    "Task failed after retry.", e
            );
        } finally {
            executor.shutdown();
        }
    }
}