package com.irk.test.model
import kotlin.math.abs
import kotlin.math.pow
fun createIdentityMatrix(size: Int): Matrix {
    val identityMatrix = Matrix(size, size)
    for (i in 0 until size) {
        identityMatrix[i, i] = 1.0
    }
    return identityMatrix
}

fun isIdentityMatrix(matrix: Matrix): Boolean {
    val rows = matrix.rows
    val cols = matrix.columns
    if (rows != cols) return false
    for (i in 0 until rows) {
        for (j in 0 until cols) {
            if (i == j && matrix[i, j] != 1.0) return false
            if (i != j && matrix[i, j] != 0.0) return false
        }
    }
    return true
}

fun gauss(inputMatrix: Matrix): Pair<Matrix, Int> {
    val matrix = inputMatrix.copy()
    val rows = matrix.rows
    val cols = matrix.columns
    var pivotRow = 0
    var swapCount = 0

    for (j in 0 until cols - 1) {
        if (pivotRow >= rows) break

        var i = pivotRow
        while (matrix[i, j] == 0.0) {
            i++
            if (i >= rows) {
                i = pivotRow
                break
            }
        }

        if (matrix[i, j] != 0.0) {
            if (i != pivotRow) {
                matrix.swapRow(i, pivotRow)
                swapCount++
            }

            for (k in pivotRow + 1 until rows) {
                val factor = matrix[k, j] / matrix[pivotRow, j]
                for (l in j until cols) {
                    matrix[k, l] -= factor * matrix[pivotRow, l]
                }
            }
            pivotRow++
        }
    }
    return Pair(matrix, swapCount)
}

fun upSubstitution(inputMatrix: Matrix): Matrix {
    val matrix = inputMatrix.copy()
    val rows = matrix.rows

    for (i in rows - 1 downTo 0) {
        val pivot = matrix[i, i]
        if (pivot == 0.0) {
            continue
        }
        val factor = 1 / pivot
        matrix.multiplyRow(i, factor)
        for (j in i - 1 downTo 0) {
            val factor = -matrix[j, i]
            matrix.addRowRow(j, i, factor)
        }
    }
    return matrix
}

fun gaussJordan(inputMatrix: Matrix): Pair<Matrix, List<Step>> {
    val steps = mutableListOf<Step>()
    val matrix = inputMatrix.copy()
    val rows = matrix.rows
    val cols = matrix.columns
    var pivotRow = 0
    var pivotCol = 0

    while (pivotRow < rows && pivotCol < cols) {
        var maxRow = pivotRow
        for (k in pivotRow + 1 until rows) {
            if (abs(matrix[k, pivotCol]) > abs(matrix[maxRow, pivotCol])) {
                maxRow = k
            }
        }

        if (maxRow != pivotRow) {
            val initial = matrix.copy()
            val description = "Swap R${pivotRow + 1} <-> R${maxRow + 1} for best pivot"
            matrix.swapRow(pivotRow, maxRow)
            steps.add(Step(initial, description, matrix.copy()))
        }

        if (matrix[pivotRow, pivotCol] == 0.0) {
            pivotCol++
            continue
        }

        val pivotValue = matrix[pivotRow, pivotCol]
        if (pivotValue != 1.0) {
            val initial = matrix.copy()
            val description = "R${pivotRow + 1} = R${pivotRow + 1} / ${"%.2f".format(pivotValue)}"
            matrix.multiplyRow(pivotRow, 1.0 / pivotValue)
            steps.add(Step(initial, description, matrix.copy()))
        }

        for (i in 0 until rows) {
            if (i != pivotRow) {
                val factor = matrix[i, pivotCol]
                if (factor != 0.0) {
                    val initial = matrix.copy()
                    val description = "R${i + 1} = R${i + 1} - ${"%.2f".format(factor)} * R${pivotRow + 1}"
                    matrix.addRowRow(i, pivotRow, -factor)
                    steps.add(Step(initial, description, matrix.copy()))
                }
            }
        }
        pivotRow++
        pivotCol++
    }

    return Pair(matrix, steps)
}

fun determinant(inputMatrix: Matrix): Pair<Double, List<Step>> {
    if (!inputMatrix.isSquare()) {
        throw Exception("Matrix must be square")
    }
    val steps = mutableListOf<Step>()
    var matrix = inputMatrix.copy()
    val n = matrix.rows
    var swapCount = 0

    steps.add(Step(inputMatrix, "Begin determinant calculation.", matrix.copy()))

    for (i in 0 until n) {
        // Find and swap pivot to current row
        var maxRow = i
        for (k in i + 1 until n) {
            if (abs(matrix[k, i]) > abs(matrix[maxRow, i])) {
                maxRow = k
            }
        }
        if (maxRow != i) {
            val initial = matrix.copy()
            matrix.swapRow(i, maxRow)
            swapCount++
            steps.add(Step(initial, "Swap R${i + 1} <-> R${maxRow + 1}", matrix.copy()))
        }

        if (matrix[i, i] == 0.0) {
            val finalDescription = "Pivot at [$i, $i] is 0. The determinant of the matrix is 0."
            steps.add(Step(matrix, finalDescription, matrix.copy()))
            return Pair(0.0, steps)
        }

        for (k in i + 1 until n) {
            val factor = matrix[k, i] / matrix[i, i]
            if (factor != 0.0) {
                val initial = matrix.copy()
                val description = "R${k + 1} = R${k + 1} - ${"%.2f".format(factor)} * R${i + 1}"
                matrix.addRowRow(k, i, -factor)
                steps.add(Step(initial, description, matrix.copy()))
            }
        }
    }

    val determinantValue = matrix.multiplyDiagonal() * (-1.0).pow(swapCount)
    val finalDescription = "Determinant = Product of diagonals * (-1)^swaps\n" +
            "Determinant = ${matrix.multiplyDiagonal()} * (-1)^$swapCount = $determinantValue"
    steps.add(Step(matrix, finalDescription, matrix.copy()))

    return Pair(determinantValue, steps)
}



fun inverse(inputMatrix: Matrix): Pair<Matrix, List<Step>> {
    if (!inputMatrix.isSquare()) {
        throw Exception("Matrix must be square")
    }
    val steps = mutableListOf<Step>()
    val identity = createIdentityMatrix(inputMatrix.rows)
    val augmentedMatrix = inputMatrix.copy().augment(identity)

    steps.add(
        Step(
            inputMatrix,
            "Augment the matrix with the Identity Matrix.",
            augmentedMatrix.copy()
        )
    )

    val (solvedMatrix, gjSteps) = gaussJordan(augmentedMatrix)
    steps.addAll(gjSteps)

    val (left, right) = solvedMatrix.split()
    if (!isIdentityMatrix(left)) {
        throw Exception("Matrix is not invertible")
    }
    steps.add(
        Step(
            solvedMatrix,
            "Inverse on the right side of the augmented matrix.",
            right.copy()
        )
    )

    return Pair(right, steps)
}

fun getUniqueSolution(inputMatrix: Matrix): Map<Int, Double> {
    val solution = mutableMapOf<Int, Double>()
    for (i in 0 until inputMatrix.rows) {
        solution[i] = inputMatrix[i, inputMatrix.columns - 1]
    }
    return solution
}

fun getParameterSolution(inputMatrix: Matrix): Map<Int, String> {
    val matrix = inputMatrix.copy()
    val rows = matrix.rows
    val cols = matrix.columns
    val solution = mutableMapOf<Int, String>()

    val pivotColumns = IntArray(rows) { -1 }
    val freeColumns = mutableListOf<Int>()

    for (i in 0 until rows) {
        for (j in 0 until cols - 1) {
            if (matrix[i, j] == 1.0) {
                pivotColumns[i] = j
                break
            }
        }
    }

    val allVarCols = 0 until cols - 1
    val pivotColsSet = pivotColumns.toSet()
    for (col in allVarCols) {
        if (col !in pivotColsSet) {
            freeColumns.add(col)
        }
    }

    val parameters = listOf("a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p")
    freeColumns.forEachIndexed { index, colIndex ->
        solution[colIndex] = parameters.getOrElse(index) { "p${index+1}" }
    }

    for (i in 0 until rows) {
        val pivotCol = pivotColumns[i]
        if (pivotCol == -1) continue

        var expression = "%.2f".format(matrix[i, cols - 1])

        freeColumns.forEachIndexed { index, freeColIndex ->
            val coefficient = matrix[i, freeColIndex]
            if (coefficient != 0.0) {
                val sign = if (-coefficient < 0) "-" else "+"
                val param = parameters.getOrElse(index) { "p${index+1}" }
                expression += " $sign ${"%.2f".format(abs(coefficient))}$param"
            }
        }
        solution[pivotCol] = expression
    }
    return solution
}

sealed class SolutionResult {
    data class UniqueSolution(val solutions: Map<Int, Double>) : SolutionResult()
    data class ParametricSolution(val solutions: Map<Int, String>) : SolutionResult()
    object NoSolution : SolutionResult()
}

fun cramer(coefficientMatrix: Matrix, constants: Matrix): Pair<Map<Int, Double>, List<Step>> {
    val steps = mutableListOf<Step>()
    val solution = mutableMapOf<Int, Double>()
    val n = coefficientMatrix.rows

    // Step 1: Calculate the main determinant
    val (mainDeterminant, detSteps) = determinant(coefficientMatrix)
    steps.addAll(detSteps)

    if (mainDeterminant == 0.0) {
        throw Exception("Cramer's rule cannot be used when the main determinant is zero.")
    }

    for (i in 0 until n) {
        val tempMatrix = coefficientMatrix.copy()

        for (j in 0 until n) {
            tempMatrix[j, i] = constants[j, 0]
        }
        steps.add(
            Step(
                coefficientMatrix,
                "replace column ${i + 1} with result column",
                tempMatrix.copy()
            )
        )

        val (varDeterminant, varDetSteps) = determinant(tempMatrix)
        steps.add(varDetSteps.last())

        solution[i] = varDeterminant / mainDeterminant
    }

    return Pair(solution, steps)
}

fun solveSPL(inputMatrix: Matrix): Pair<SolutionResult, List<Step>> {
    val steps = mutableListOf<Step>()

    if (inputMatrix.rows != inputMatrix.columns - 1) {
        val (matrix, gjSteps) = gaussJordan(inputMatrix)
        steps.addAll(gjSteps)

        if (matrix.hasNoSolution()) {
            return Pair(SolutionResult.NoSolution, steps)
        }
        val solution = getParameterSolution(matrix)
        return Pair(SolutionResult.ParametricSolution(solution), steps)
    }

    val (coefficientMatrix, constants) = inputMatrix.splitConstants()
    val (mainDeterminant, detSteps) = determinant(coefficientMatrix)
    steps.addAll(detSteps)
    if (mainDeterminant != 0.0) {
        val (solution, cramerSteps) = cramer(coefficientMatrix, constants)
        steps.addAll(cramerSteps)
        return Pair(SolutionResult.UniqueSolution(solution), steps)
    }
    else {
        val (matrix, gjSteps) = gaussJordan(inputMatrix)
        steps.addAll(gjSteps)

        if (matrix.hasNoSolution()) {
            return Pair(SolutionResult.NoSolution, steps)
        } else {
            val solution = getParameterSolution(matrix)
            return Pair(SolutionResult.ParametricSolution(solution), steps)
        }
    }
}

private fun findNextPowerOf2(n: Int): Int {
    var p = 1
    while (p < n) {
        p *= 2
    }
    return p
}

fun multiplyMatrixBruteForce(a: Matrix, b: Matrix): Matrix {
    if (a.columns != b.rows) {
        throw Exception("Matrix dimensions are not valid for multiplication.")
    }
    val result = Matrix(a.rows, b.columns)
    for (i in 0 until a.rows) {
        for (j in 0 until b.columns) {
            var sum = 0.0
            for (k in 0 until a.columns) {
                sum += a[i, k] * b[k, j]
            }
            result[i, j] = sum
        }
    }
    return result
}

fun multiplyMatrixStrassen(a: Matrix, b: Matrix): Matrix {
    if (a.columns != b.rows) {
        throw Exception("Matrix dimensions are not valid for multiplication.")
    }

    // Find the next power of 2 for padding
    val n = maxOf(a.rows, a.columns, b.rows, b.columns)
    val m = findNextPowerOf2(n)

    // Pad matrices to the new size
    val paddedA = Matrix(m, m)
    val paddedB = Matrix(m, m)
    for (i in 0 until a.rows) {
        for (j in 0 until a.columns) {
            paddedA[i, j] = a[i, j]
        }
    }
    for (i in 0 until b.rows) {
        for (j in 0 until b.columns) {
            paddedB[i, j] = b[i, j]
        }
    }

    // Perform Strassen's multiplication
    val paddedC = strassenRecursive(paddedA, paddedB)

    // Extract the result from the padded matrix
    val result = Matrix(a.rows, b.columns)
    for (i in 0 until a.rows) {
        for (j in 0 until b.columns) {
            result[i, j] = paddedC[i, j]
        }
    }
    return result
}

private fun strassenRecursive(a: Matrix, b: Matrix): Matrix {
    val n = a.rows
    // Base case: if the matrix is 1x1
    if (n == 1) {
        val result = Matrix(1, 1)
        result[0, 0] = a[0, 0] * b[0, 0]
        return result
    }

    val half = n / 2
    val a11 = a.subMatrix(0, half, 0, half)
    val a12 = a.subMatrix(0, half, half, n)
    val a21 = a.subMatrix(half, n, 0, half)
    val a22 = a.subMatrix(half, n, half, n)

    val b11 = b.subMatrix(0, half, 0, half)
    val b12 = b.subMatrix(0, half, half, n)
    val b21 = b.subMatrix(half, n, 0, half)
    val b22 = b.subMatrix(half, n, half, n)

    val p1 = strassenRecursive(a11, b12 - b22)
    val p2 = strassenRecursive(a11 + a12, b22)
    val p3 = strassenRecursive(a21 + a22, b11)
    val p4 = strassenRecursive(a22, b21 - b11)
    val p5 = strassenRecursive(a11 + a22, b11 + b22)
    val p6 = strassenRecursive(a12 - a22, b21 + b22)
    val p7 = strassenRecursive(a11 - a21, b11 + b12)

    val c11 = p5 + p4 - p2 + p6
    val c12 = p1 + p2
    val c21 = p3 + p4
    val c22 = p5 + p1 - p3 - p7

    val result = Matrix(n, n)
    result.join(c11, 0, 0)
    result.join(c12, 0, half)
    result.join(c21, half, 0)
    result.join(c22, half, half)

    return result
}

