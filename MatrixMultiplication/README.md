Multithreaded Matrix Multiplication using Java

Overview:
The project implements matrix multiplication using Java multithreading. The application goal is to demonstrate that a matrix multiplication algorithm can be decomposed into multiple tasks and parallelized using a number of worker threads.

TensorFlow Java is utilized to independently verify the correctness of the result produced by the Java multithreaded matrix multiplication implementation. The HTML, CSS, and JavaScript Web technologies are used in building an interface that allows entering and generating matrices, multiplying them, displaying the results, and visualizing the calculation.

The project allows to work with square and rectangular matrices, including validation of their dimensions.

Objectives:

Implement matrix multiplication using Java multithreading.
Decompose the matrix multiplication algorithm into multiple tasks.
Use the ExecutorService to launch worker tasks.
Support square and rectangular matrices.
Verify correctness of the Java result using TensorFlow Java.
Create a web interface to enter the matrices and launch multiplication.

Visualize matrix multiplication.
Test the application using junit testing framework.
Explore concurrency, task decomposition, synchronization, and verification concepts.

Technologies:
Java 21
Maven 3.9+
TensorFlow Java 1.1.0
JUnit 5
HTML
CSS
JavaScript
Java HTTP Server
VS Code

Conclusion:
This project helped us understand how matrix multiplication can be done using multiple threads in Java. By dividing the work between different worker threads, the calculations can be performed at the same time. We also used TensorFlow to check whether our result was correct and added a simple web interface to make the process easier to understand. Overall, the project gave us practical experience with multithreading, matrix operations, testing, and result verification.
