package threading;

public class ComputationEvent {
    private final String workerName;
    private final int row;
    private final int column;
    private final int k;
    private final double aValue;
    private final double bValue;
    private final double product;

    public ComputationEvent(
            String workerName,
            int row,
            int column,
            int k,
            double aValue,
            double bValue) {

        this.workerName = workerName;
        this.row = row;
        this.column = column;
        this.k = k;
        this.aValue = aValue;
        this.bValue = bValue;
        this.product = aValue * bValue;
    }

    public String getWorkerName() {
        return workerName;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public int getK() {
        return k;
    }

    public double getAValue() {
        return aValue;
    }

    public double getBValue() {
        return bValue;
    }

    public double getProduct() {
        return product;
    }
}
