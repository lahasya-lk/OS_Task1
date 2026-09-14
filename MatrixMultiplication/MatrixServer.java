import com.sun.net.httpserver.HttpServer;
import matrix.Matrix;
import threading.ComputationEvent;
import threading.MatrixMultiplier;
import verification.TensorFlowVerifier;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MatrixServer {

    public static void main(String[] args) throws IOException {

        HttpServer server =
                HttpServer.create(
                        new InetSocketAddress(8080),
                        0
                );

        server.createContext("/multiply", exchange -> {

            exchange.getResponseHeaders().set(
                    "Access-Control-Allow-Origin",
                    "*"
            );

            exchange.getResponseHeaders().set(
                    "Access-Control-Allow-Methods",
                    "POST, OPTIONS"
            );

            exchange.getResponseHeaders().set(
                    "Access-Control-Allow-Headers",
                    "Content-Type"
            );


            if (exchange.getRequestMethod()
                    .equalsIgnoreCase("OPTIONS")) {

                exchange.sendResponseHeaders(204, -1);
                exchange.close();
                return;
            }


            if (!exchange.getRequestMethod()
                    .equalsIgnoreCase("POST")) {

                sendResponse(
                        exchange,
                        405,
                        "POST required"
                );

                return;
            }


            try {

                String request =
                        readRequest(
                                exchange.getRequestBody()
                        );


                Matrix a =
                        parseMatrix(request, "a");

                Matrix b =
                        parseMatrix(request, "b");


                /*
                 * Store computation events for animation.
                 *
                 * The list is thread-safe because multiple
                 * worker threads can create events at the
                 * same time.
                 */
                List<ComputationEvent> events =
                        Collections.synchronizedList(
                                new ArrayList<>()
                        );


                /*
                 * For small matrices we can show every
                 * multiplication.
                 *
                 * For large matrices we only keep the first
                 * 200 events for browser animation.
                 */
                int maximumEvents = 200;


                MatrixMultiplier multiplier =
                        new MatrixMultiplier(4);


                long start =
                        System.nanoTime();


                Matrix result =
                        multiplier.multiply(
                                a,
                                b,
                                event -> {

                                    if (events.size()
                                            < maximumEvents) {

                                        events.add(event);
                                    }
                                }
                        );


                long end =
                        System.nanoTime();


                double computationTime =
                        (end - start)
                        / 1_000_000.0;


                boolean verified =
                        TensorFlowVerifier.verify(
                                a,
                                b,
                                result
                        );


                String response =
                        createResponse(
                                result,
                                verified,
                                computationTime,
                                events
                        );


                sendResponse(
                        exchange,
                        200,
                        response
                );


            } catch (Exception e) {

                sendResponse(
                        exchange,
                        400,
                        "Error: " + e.getMessage()
                );
            }
        });


        server.start();


        System.out.println(
                "Server running at http://localhost:8080"
        );
    }


    private static Matrix parseMatrix(
            String request,
            String name) {

        String key =
                "\"" + name + "\":[";

        int start =
                request.indexOf(key);


        if (start == -1) {

            throw new IllegalArgumentException(
                    "Matrix " + name + " not found."
            );
        }


        start += key.length();


        int end =
                request.indexOf("]]", start);


        if (end == -1) {

            throw new IllegalArgumentException(
                    "Invalid matrix format."
            );
        }


        String content =
                request.substring(
                        start,
                        end + 1
                );


        content =
                content.replace("[", "");


        String[] rows =
                content.split("\\],");


        int rowCount =
                rows.length;


        String[] firstRow =
                rows[0]
                        .replace("]", "")
                        .split(",");


        int columnCount =
                firstRow.length;


        Matrix matrix =
                new Matrix(
                        rowCount,
                        columnCount
                );


        for (int i = 0; i < rowCount; i++) {

            String row =
                    rows[i]
                            .replace("]", "");


            String[] values =
                    row.split(",");


            if (values.length != columnCount) {

                throw new IllegalArgumentException(
                        "Matrix must be rectangular."
                );
            }


            for (int j = 0;
                    j < columnCount;
                    j++) {

                matrix.set(
                        i,
                        j,
                        Double.parseDouble(
                                values[j]
                        )
                );
            }
        }


        return matrix;
    }


    private static String createResponse(
            Matrix result,
            boolean verified,
            double computationTime,
            List<ComputationEvent> events) {

        StringBuilder json =
                new StringBuilder();


        json.append("{");


        json.append("\"rows\":")
                .append(result.getRows())
                .append(",");


        json.append("\"columns\":")
                .append(result.getColumns())
                .append(",");


        json.append("\"computationTime\":")
                .append(computationTime)
                .append(",");


        json.append("\"verified\":")
                .append(verified)
                .append(",");


        json.append("\"result\":[");


        for (int i = 0;
                i < result.getRows();
                i++) {

            json.append("[");


            for (int j = 0;
                    j < result.getColumns();
                    j++) {

                json.append(
                        result.get(i, j)
                );


                if (j < result.getColumns() - 1) {
                    json.append(",");
                }
            }


            json.append("]");


            if (i < result.getRows() - 1) {
                json.append(",");
            }
        }


        json.append("],");


        /*
         * Computation events
         */
        json.append("\"events\":[");


        synchronized (events) {

            for (int i = 0;
                    i < events.size();
                    i++) {

                ComputationEvent event =
                        events.get(i);


                json.append("{");


                json.append("\"worker\":\"")
                        .append(event.getWorkerName())
                        .append("\",");


                json.append("\"row\":")
                        .append(event.getRow())
                        .append(",");


                json.append("\"column\":")
                        .append(event.getColumn())
                        .append(",");


                json.append("\"k\":")
                        .append(event.getK())
                        .append(",");


                json.append("\"aValue\":")
                        .append(event.getAValue())
                        .append(",");


                json.append("\"bValue\":")
                        .append(event.getBValue())
                        .append(",");


                json.append("\"product\":")
                        .append(event.getProduct());


                json.append("}");


                if (i < events.size() - 1) {
                    json.append(",");
                }
            }
        }


        json.append("]");


        json.append("}");


        return json.toString();
    }


    private static String readRequest(
            InputStream input)
            throws IOException {

        return new String(
                input.readAllBytes(),
                StandardCharsets.UTF_8
        );
    }


    private static void sendResponse(
            com.sun.net.httpserver.HttpExchange exchange,
            int status,
            String response)
            throws IOException {

        byte[] data =
                response.getBytes(
                        StandardCharsets.UTF_8
                );


        exchange.getResponseHeaders().set(
                "Content-Type",
                "application/json"
        );


        exchange.sendResponseHeaders(
                status,
                data.length
        );


        exchange.getResponseBody()
                .write(data);

        exchange.close();
    }
}
