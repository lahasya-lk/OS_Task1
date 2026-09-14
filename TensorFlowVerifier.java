package verification;

import matrix.Matrix;
import org.tensorflow.EagerSession;
import org.tensorflow.Tensor;
import org.tensorflow.ndarray.Shape;
import org.tensorflow.op.Ops;
import org.tensorflow.types.TFloat64;

public class TensorFlowVerifier {

    public static boolean verify(Matrix a, Matrix b, Matrix result) {

        try (EagerSession session = EagerSession.create()) {

            Ops tf = Ops.create(session);

            TFloat64 tensorA = TFloat64.tensorOf(
                    Shape.of(a.getRows(), a.getColumns()));

            TFloat64 tensorB = TFloat64.tensorOf(
                    Shape.of(b.getRows(), b.getColumns()));

            for (int i = 0; i < a.getRows(); i++) {
                for (int j = 0; j < a.getColumns(); j++) {
                    tensorA.setDouble(a.get(i, j), i, j);
                }
            }

            for (int i = 0; i < b.getRows(); i++) {
                for (int j = 0; j < b.getColumns(); j++) {
                    tensorB.setDouble(b.get(i, j), i, j);
                }
            }

            var tensorflowResult = tf.linalg.matMul(
                    tf.constant(tensorA),
                    tf.constant(tensorB)
            );

            try (Tensor output = tensorflowResult.asTensor();
                 TFloat64 tensorResult = (TFloat64) output) {

                for (int i = 0; i < result.getRows(); i++) {
                    for (int j = 0; j < result.getColumns(); j++) {
                        double expected = tensorResult.getDouble(i, j);

                        if (Math.abs(expected - result.get(i, j)) > 0.000001) {
                            return false;
                        }
                    }
                }
            }

            return true;
        }
    }
}