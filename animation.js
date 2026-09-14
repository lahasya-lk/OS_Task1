const aRows = document.getElementById("aRows");
const aColumns = document.getElementById("aColumns");
const bRows = document.getElementById("bRows");
const bColumns = document.getElementById("bColumns");


function generateMatrices() {
    const rowsA = Number(aRows.value);
    const columnsA = Number(aColumns.value);
    const columnsB = Number(bColumns.value);

    if (rowsA < 1 || columnsA < 1 || columnsB < 1) {
        document.getElementById("status").textContent =
            "Rows and columns must be greater than 0.";
        return;
    }

    bRows.value = columnsA;

    createMatrix("matrixA", rowsA, columnsA);
    createMatrix("matrixB", columnsA, columnsB);
}


function createMatrix(id, rows, columns) {
    const container = document.getElementById(id);
    container.innerHTML = "";

    const table = document.createElement("table");

    for (let i = 0; i < rows; i++) {
        const row = document.createElement("tr");

        for (let j = 0; j < columns; j++) {
            const cell = document.createElement("td");
            const input = document.createElement("input");

            input.type = "number";
            input.value = "0";

            input.dataset.row = i;
            input.dataset.column = j;

            cell.appendChild(input);
            row.appendChild(cell);
        }

        table.appendChild(row);
    }

    container.appendChild(table);
}


function generateRandom() {
    generateMatrices();

    fillRandom("matrixA");
    fillRandom("matrixB");
}


function fillRandom(id) {
    document.querySelectorAll(`#${id} input`).forEach(input => {
        input.value = Math.floor(Math.random() * 10);
    });
}


function getMatrix(id, rows, columns) {
    const inputs =
        document.querySelectorAll(`#${id} input`);

    const matrix = [];
    let index = 0;

    for (let i = 0; i < rows; i++) {
        matrix[i] = [];

        for (let j = 0; j < columns; j++) {
            matrix[i][j] =
                Number(inputs[index].value);

            index++;
        }
    }

    return matrix;
}


async function multiplyMatrices() {

    const rowsA = Number(aRows.value);
    const columnsA = Number(aColumns.value);
    const columnsB = Number(bColumns.value);

    if (rowsA < 1 || columnsA < 1 || columnsB < 1) {
        document.getElementById("status").textContent =
            "Rows and columns must be greater than 0.";

        return;
    }

    const matrixA = getMatrix(
        "matrixA",
        rowsA,
        columnsA
    );

    const matrixB = getMatrix(
        "matrixB",
        columnsA,
        columnsB
    );

    clearHighlights();

    document.getElementById("status").textContent =
        "Calculating...";

    document.getElementById("animation").textContent =
        "Preparing animation...";

    try {

        const response = await fetch(
            "http://localhost:8080/multiply",
            {
                method: "POST",

                headers: {
                    "Content-Type": "application/json"
                },

                body: JSON.stringify({
                    a: matrixA,
                    b: matrixB
                })
            }
        );

        if (!response.ok) {
            throw new Error(
                await response.text()
            );
        }

        const data =
            await response.json();

        displayResult(data.result);

        document.getElementById("status").textContent =
            "TensorFlow Verification: " +
            (data.verified
                ? "PASSED"
                : "FAILED") +
            " | Threaded computation: " +
            data.computationTime.toFixed(3) +
            " ms";

        animateEvents(
            data.events,
            rowsA,
            columnsA,
            columnsB
        );

    } catch (error) {

        document.getElementById("status").textContent =
            "Error: " + error.message;

        console.error(error);
    }
}


function displayResult(matrix) {

    const container =
        document.getElementById("result");

    container.innerHTML = "";

    const table =
        document.createElement("table");

    matrix.forEach(rowData => {

        const row =
            document.createElement("tr");

        rowData.forEach(value => {

            const cell =
                document.createElement("td");

            cell.textContent = value;

            row.appendChild(cell);
        });

        table.appendChild(row);
    });

    container.appendChild(table);
}


/* ============================= */
/* CHOOSE ANIMATION */
/* ============================= */

function animateEvents(
    events,
    rows,
    columnsA,
    columnsB
) {

    const totalOperations =
        rows * columnsA * columnsB;

    /*
     * Small matrices:
     * Show every multiplication step.
     *
     * Large matrices:
     * Show worker/thread execution.
     */

    if (totalOperations <= 100) {

        detailedAnimation(
            events,
            rows,
            columnsA,
            columnsB
        );

    } else {

        showThreadAnimation(
            rows,
            columnsA,
            columnsB
        );
    }
}


/* ============================= */
/* SMALL MATRIX ANIMATION */
/* ============================= */

function detailedAnimation(
    events,
    rows,
    columnsA,
    columnsB
) {

    const container =
        document.getElementById("animation");

    container.innerHTML = "";

    const title =
        document.createElement("h3");

    title.textContent =
        "Step-by-Step Multiplication";

    container.appendChild(title);

    const area =
        document.createElement("div");

    area.className =
        "animation-matrices";

    area.innerHTML = `
        <div class="animation-matrix">
            <h3>Matrix A</h3>

            ${createAnimationMatrix(
                "A",
                rows,
                columnsA
            )}
        </div>

        <div class="matrix-symbol">×</div>

        <div class="animation-matrix">
            <h3>Matrix B</h3>

            ${createAnimationMatrix(
                "B",
                columnsA,
                columnsB
            )}
        </div>

        <div class="matrix-symbol">=</div>

        <div class="animation-matrix">
            <h3>Matrix C</h3>

            ${createAnimationMatrix(
                "C",
                rows,
                columnsB
            )}
        </div>
    `;

    container.appendChild(area);

    const info =
        document.createElement("div");

    info.className =
        "animation-info";

    container.appendChild(info);

    const result =
        Array.from(
            { length: rows },
            () => Array(columnsB).fill(0)
        );

    let index = 0;


    function next() {

        if (index >= events.length) {

            clearHighlights();

            info.innerHTML =
                "<strong>✓ Animation completed</strong>";

            return;
        }

        const event =
            events[index];

        clearHighlights();


        const aCell =
            document.getElementById(
                `animation-A-${event.row}-${event.k}`
            );

        const bCell =
            document.getElementById(
                `animation-B-${event.k}-${event.column}`
            );

        const cCell =
            document.getElementById(
                `animation-C-${event.row}-${event.column}`
            );


        if (aCell) {
            aCell.classList.add(
                "active-cell"
            );
        }

        if (bCell) {
            bCell.classList.add(
                "active-cell"
            );
        }


        result[event.row][event.column] +=
            event.product;


        if (cCell) {

            cCell.textContent =
                result[event.row][event.column];

            cCell.classList.add(
                "updated-cell"
            );
        }


        info.innerHTML = `
            <div class="animation-operation">

                A[${event.row}][${event.k}]
                ×
                B[${event.k}][${event.column}]
                =

                <strong>
                    ${event.product}
                </strong>

            </div>

            <div>

                C[${event.row}][${event.column}]
                =

                <strong>
                    ${result[event.row][event.column]}
                </strong>

            </div>
        `;


        index++;

        setTimeout(
            next,
            500
        );
    }


    next();
}


/* ============================= */
/* LARGE MATRIX THREAD ANIMATION */
/* ============================= */

function showThreadAnimation(
    rows,
    columnsA,
    columnsB
) {

    const animation =
        document.getElementById("animation");


    /*
     * Java currently uses 4 workers.
     *
     * If there are fewer than 4 rows,
     * only that many workers are needed.
     */

    const workerCount =
        Math.min(4, rows);


    /*
     * Same row distribution used
     * by MatrixMultiplier.java.
     */

    const rowsPerWorker =
        Math.ceil(
            rows / workerCount
        );


    animation.innerHTML = `
        <div class="thread-progress-area">

            <h3>
                Parallel Thread Execution
            </h3>

            <p>
                The matrix rows are divided
                among worker threads.
            </p>

            <div id="threadProgress"></div>

        </div>
    `;


    const container =
        document.getElementById(
            "threadProgress"
        );


    /*
     * Create one display for each worker.
     */

    for (
        let i = 0;
        i < workerCount;
        i++
    ) {

        const startRow =
            i * rowsPerWorker;


        const endRow =
            Math.min(
                startRow +
                rowsPerWorker -
                1,
                rows - 1
            );


        const worker =
            document.createElement(
                "div"
            );


        worker.className =
            "thread-progress";


        worker.innerHTML = `

            <div class="thread-name">
                Worker ${i + 1}
            </div>

            <div class="thread-row">
                Rows ${startRow} - ${endRow}
            </div>

            <div class="progress-bar">

                <div
                    class="progress-fill"
                    id="progress-${i}">
                </div>

            </div>

            <div
                class="thread-status"
                id="status-${i}">

                Processing...

            </div>
        `;


        container.appendChild(
            worker
        );
    }


    /*
     * Animate each worker.
     */

    for (
        let i = 0;
        i < workerCount;
        i++
    ) {

        setTimeout(() => {

            const progress =
                document.getElementById(
                    `progress-${i}`
                );


            const status =
                document.getElementById(
                    `status-${i}`
                );


            if (progress) {
                progress.style.width =
                    "100%";
            }


            if (status) {
                status.textContent =
                    "✓ Completed";
            }


        }, 500 + i * 300);
    }
}


/* ============================= */
/* CREATE ANIMATION MATRIX */
/* ============================= */

function createAnimationMatrix(
    label,
    rows,
    columns
) {

    let html =
        "<table>";


    for (
        let i = 0;
        i < rows;
        i++
    ) {

        html +=
            "<tr>";


        for (
            let j = 0;
            j < columns;
            j++
        ) {

            let value = 0;


            if (label === "A") {

                const input =
                    document.querySelector(
                        `#matrixA input[data-row="${i}"][data-column="${j}"]`
                    );


                if (input) {
                    value =
                        input.value;
                }
            }


            if (label === "B") {

                const input =
                    document.querySelector(
                        `#matrixB input[data-row="${i}"][data-column="${j}"]`
                    );


                if (input) {
                    value =
                        input.value;
                }
            }


            html += `

                <td
                    id="animation-${label}-${i}-${j}"
                >
                    ${value}
                </td>

            `;
        }


        html +=
            "</tr>";
    }


    html +=
        "</table>";


    return html;
}


/* ============================= */
/* CLEAR HIGHLIGHTS */
/* ============================= */

function clearHighlights() {

    document
        .querySelectorAll(
            ".active-cell"
        )
        .forEach(cell => {

            cell.classList.remove(
                "active-cell"
            );
        });


    document
        .querySelectorAll(
            ".updated-cell"
        )
        .forEach(cell => {

            cell.classList.remove(
                "updated-cell"
            );
        });
}


/* ============================= */
/* B ROWS = A COLUMNS */
/* ============================= */

aColumns.addEventListener(
    "input",
    () => {

        bRows.value =
            aColumns.value;
    }
);


/* ============================= */
/* INITIAL MATRICES */
/* ============================= */

generateMatrices();