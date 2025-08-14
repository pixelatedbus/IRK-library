package com.irk.test.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.irk.test.model.Matrix
import com.irk.test.model.SolutionResult
import com.irk.test.model.Step
import com.irk.test.model.determinant
import com.irk.test.model.gaussJordan
import com.irk.test.model.inverse
import com.irk.test.model.multiplyMatrixBruteForce
import com.irk.test.model.solveSPL
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class MatrixOperation {
    ADDITION,
    SUBTRACTION,
    MULTIPLICATION,
    DETERMINANT,
    INVERSE,
    GAUSS_JORDAN,
    SOLVE_SPL
}

data class MatrixUiState(
    val rowsA: String = "2",
    val colsA: String = "2",
    val rowsB: String = "2",
    val colsB: String = "2",
    val matrixA: List<List<String>> = List(2) { List(2) { "" } },
    val matrixB: List<List<String>> = List(2) { List(2) { "" } },
    val selectedOperation: MatrixOperation = MatrixOperation.ADDITION,
    val resultMatrix: List<List<String>>? = null,
    val resultScalar: String? = null,
    val resultSolution: String? = null,
    val steps: List<MatrixStepUiModel> = emptyList(),
    val error: String? = null
)

data class MatrixStepUiModel(
    val title: String,
    val initialMatrix: List<List<String>>,
    val finalMatrix: List<List<String>>
)

class MatrixViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(MatrixUiState())
    val uiState: StateFlow<MatrixUiState> = _uiState.asStateFlow()

    fun onDimensionsChanged(rowsA: String? = null, colsA: String? = null, rowsB: String? = null, colsB: String? = null) {
        _uiState.update { currentState ->
            val newRowsA = rowsA?.toIntOrNull() ?: currentState.rowsA.toInt()
            val newColsA = colsA?.toIntOrNull() ?: currentState.colsA.toInt()
            val newRowsB = rowsB?.toIntOrNull() ?: currentState.rowsB.toInt()
            val newColsB = colsB?.toIntOrNull() ?: currentState.colsB.toInt()
            currentState.copy(
                rowsA = newRowsA.toString(),
                colsA = newColsA.toString(),
                rowsB = newRowsB.toString(),
                colsB = newColsB.toString(),
                matrixA = List(newRowsA) { List(newColsA) { "" } },
                matrixB = List(newRowsB) { List(newColsB) { "" } }
            )
        }
    }

    fun onMatrixAValueChanged(row: Int, col: Int, value: String) {
        _uiState.update { currentState ->
            val newMatrix = currentState.matrixA.map { it.toMutableList() }.toMutableList()
            newMatrix[row][col] = value
            currentState.copy(matrixA = newMatrix)
        }
    }

    fun onMatrixBValueChanged(row: Int, col: Int, value: String) {
        _uiState.update { currentState ->
            val newMatrix = currentState.matrixB.map { it.toMutableList() }.toMutableList()
            newMatrix[row][col] = value
            currentState.copy(matrixB = newMatrix)
        }
    }

    fun onOperationSelected(operation: MatrixOperation) {
        _uiState.update { it.copy(selectedOperation = operation, resultMatrix = null, resultScalar = null, steps = emptyList(), error = null) }
    }

    fun calculate() {
        viewModelScope.launch {
            val state = _uiState.value
            try {
                val matrixA = state.matrixA.toModel()
                val matrixB = if (state.selectedOperation in listOf(MatrixOperation.ADDITION, MatrixOperation.SUBTRACTION, MatrixOperation.MULTIPLICATION)) state.matrixB.toModel() else null

                when (state.selectedOperation) {
                    MatrixOperation.ADDITION -> {
                        val result = matrixA + matrixB!!
                        _uiState.update { it.copy(resultMatrix = result.toUiModel(), error = null) }
                    }
                    MatrixOperation.SUBTRACTION -> {
                        val result = matrixA - matrixB!!
                        _uiState.update { it.copy(resultMatrix = result.toUiModel(), error = null) }
                    }
                    MatrixOperation.MULTIPLICATION -> {
                        val result = multiplyMatrixBruteForce(matrixA, matrixB!!)
                        _uiState.update { it.copy(resultMatrix = result.toUiModel(), error = null) }
                    }
                    MatrixOperation.DETERMINANT -> {
                        val (result, steps) = determinant(matrixA)
                        _uiState.update { it.copy(resultScalar = result.toString(), steps = steps.toUiModel(), error = null) }
                    }
                    MatrixOperation.INVERSE -> {
                        val (result, steps) = inverse(matrixA)
                        _uiState.update { it.copy(resultMatrix = result.toUiModel(), steps = steps.toUiModel(), error = null) }
                    }
                    MatrixOperation.GAUSS_JORDAN -> {
                        val (result, steps) = gaussJordan(matrixA)
                        _uiState.update { it.copy(resultMatrix = result.toUiModel(), steps = steps.toUiModel(), error = null) }
                    }
                    MatrixOperation.SOLVE_SPL -> {
                        val (result, steps) = solveSPL(matrixA)
                        val solutionText = when (result) {
                            is SolutionResult.UniqueSolution -> result.solutions.entries.joinToString("\n") { (index, value) -> "x${index + 1} = $value" }
                            is SolutionResult.ParametricSolution -> result.solutions.entries.joinToString("\n") { (index, value) -> "x${index + 1} = $value" }
                            is SolutionResult.NoSolution -> "No solution exists."
                        }
                        _uiState.update { it.copy(resultSolution = solutionText, steps = steps.toUiModel(), error = null) }
                    }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message ?: "An error occurred.") }
            }
        }
    }

    // --- Mapper Functions ---
    private fun List<List<String>>.toModel(): Matrix {
        val rows = this.size
        val cols = this.firstOrNull()?.size ?: 0
        val matrix = Matrix(rows, cols)
        this.forEachIndexed { i, row ->
            row.forEachIndexed { j, value ->
                matrix[i, j] = value.toDoubleOrNull() ?: 0.0
            }
        }
        return matrix
    }

    private fun Matrix.toUiModel(): List<List<String>> {
        return List(this.rows) { i ->
            List(this.columns) { j ->
                "%.2f".format(this[i, j])
            }
        }
    }

    private fun List<Step>.toUiModel(): List<MatrixStepUiModel> {
        return this.map {
            MatrixStepUiModel(
                title = it.stepDescription,
                initialMatrix = it.initialMatrix.toUiModel(),
                finalMatrix = it.finalMatrix.toUiModel()
            )
        }
    }
}
