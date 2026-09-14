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

Project Structure:

Matrix-Multiplication/
├── src/
│  ├── main/
│  │  └── java/
│  │    ├── Main.java
│  │    ├── MatrixServer.java
│  │    ├── TensorFlowTest.java
│  │    │
│  │    ├── matrix/
│  │    │  ├── Matrix.java
│  │    │  ├── MatrixGenerator.java
│  │    │  └── MatrixValidator.java
│  │    │
│  │    ├── threading/
│  │    │  ├── ComputationEvent.java
│  │    │  ├── MatrixMultiplier.java
│  │    │  ├── MatrixTask.java
│  │    │  └── WorkerManager.java
│  │    │
│  │    └── verification/
│  │      └── TensorFlowVerifier.java
│  │
│  └── test/
│    └── java/
│      └── MatrixMultiplierTest.java
│
├── web/
│  ├── index.html
│  ├── style.css
│  └── animation.js
│
├── pom.xml
├── classpath.txt
└── README.md
