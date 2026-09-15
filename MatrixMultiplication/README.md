# Multithreaded Matrix Multiplication

## Overview

This project implements matrix multiplication using **Java multithreading**. The computation is divided among multiple worker threads using `ExecutorService`.

The project also uses **TensorFlow Java** to verify the correctness of the result. A simple web interface is included to enter or generate matrices, perform multiplication, display the result, and visualize the computation.

## Features

- Matrix multiplication using 4 worker threads
- Supports square and rectangular matrices
- Manual and random matrix generation
- TensorFlow-based result verification
- JUnit testing
- Web-based result display and animation

## Technologies Used

- Java 21
- Maven
- TensorFlow Java
- JUnit 5
- HTML, CSS, JavaScript

## Project Structure

```text
Matrix-Multiplication/
│
├── src/
│   ├── main/
│   │   └── java/
│   │       ├── Main.java
│   │       ├── MatrixServer.java
│   │       ├── TensorFlowTest.java
│   │       ├── matrix/
│   │       ├── threading/
│   │       └── verification/
│   │
│   └── test/
│       └── java/
│           └── MatrixMultiplierTest.java
│
├── web/
│   ├── index.html
│   ├── style.css
│   └── animation.js
│
├── pom.xml
└── README.md


How to Run
Prerequisites
Make sure the following are installed:
JDK 21
Maven 3.9+
VS Code
A web browser

Check Java: java -version
Check Maven: mvn -version
Make sure Maven is using Java 21.

1. Open the Project
Open the Matrix-Multiplication folder in VS Code.
Make sure pom.xml is present in the project folder.

2. Build the Project
Open the VS Code terminal and run:
mvn clean install
If the build is successful, you should see:
BUILD SUCCESS

3. Start the Java Server
Run: mvn exec:java "-Dexec.mainClass=MatrixServer"
The server will start at: http://localhost:8080
Keep this terminal running.

4. Open the Web Interface
Open the web folder in VS Code and open:
web/index.html
You can use the Live Server extension to run the webpage.
The page will usually open at something similar to:
http://127.0.0.1:5500/web/index.html

5. Perform Matrix Multiplication
Enter the matrix dimensions.
Enter the matrix values or generate random matrices.
Click MULTIPLY.
The result will be displayed on the webpage.
The result is also verified using TensorFlow.
The animation shows the matrix computation.

Example
For:
A = [1  2]       B = [5  6]
    [3  4]           [7  8]
The result is:
C = [19  22]
    [43  50]

Testing
To run the JUnit tests:
mvn test
The tests check matrix multiplication, rectangular matrices, invalid dimensions, and consistency between different numbers of worker threads.

Workflow
Input Matrices
      ↓
Dimension Validation
      ↓
Create Worker Tasks
      ↓
Divide Rows Among Workers
      ↓
4 Worker Threads Execute
      ↓
Matrix Multiplication
      ↓
TensorFlow Verification
      ↓
Display Result
      ↓
Show Animation

Conclusion
This project helped us understand how matrix multiplication can be performed using multiple threads in Java. The work is divided among different worker threads, and TensorFlow is used to verify the result. The web interface also makes it easier to perform the multiplication and visualize the process. Overall, the project provides practical experience with multithreading, matrix operations, testing, and result verification.
