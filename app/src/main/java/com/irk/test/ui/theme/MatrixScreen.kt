package com.irk.test.ui.theme

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.irk.test.viewmodel.MatrixOperation
import com.irk.test.viewmodel.MatrixUiState
import com.irk.test.viewmodel.MatrixViewModel
import com.irk.test.viewmodel.MatrixStepUiModel

@Composable
fun MatrixScreen(viewModel: MatrixViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        item {
            OperationSelector(
                selectedOperation = uiState.selectedOperation,
                onOperationSelected = viewModel::onOperationSelected
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            MatrixInputSection(uiState = uiState, viewModel = viewModel)
            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            Button(
                onClick = { viewModel.calculate() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Calculate")
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            ResultsSection(uiState = uiState)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OperationSelector(selectedOperation: MatrixOperation, onOperationSelected: (MatrixOperation) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = selectedOperation.name,
            onValueChange = {},
            readOnly = true,
            label = { Text("Operation") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            MatrixOperation.values().forEach { operation ->
                DropdownMenuItem(
                    text = { Text(operation.name) },
                    onClick = {
                        onOperationSelected(operation)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun MatrixInputSection(uiState: MatrixUiState, viewModel: MatrixViewModel) {
    // Matrix A Input
    Text("Matrix A", style = MaterialTheme.typography.titleMedium)
    DimensionInputs(
        rows = uiState.rowsA,
        cols = uiState.colsA,
        onRowsChange = { viewModel.onDimensionsChanged(rowsA = it) },
        onColsChange = { viewModel.onDimensionsChanged(colsA = it) }
    )
    MatrixGrid(
        matrix = uiState.matrixA,
        onValueChanged = { row, col, value -> viewModel.onMatrixAValueChanged(row, col, value) }
    )

    // Matrix B Input (conditional)
    if (uiState.selectedOperation in listOf(MatrixOperation.ADDITION, MatrixOperation.SUBTRACTION, MatrixOperation.MULTIPLICATION)) {
        Spacer(modifier = Modifier.height(16.dp))
        Text("Matrix B", style = MaterialTheme.typography.titleMedium)
        DimensionInputs(
            rows = uiState.rowsB,
            cols = uiState.colsB,
            onRowsChange = { viewModel.onDimensionsChanged(rowsB = it) },
            onColsChange = { viewModel.onDimensionsChanged(colsB = it) }
        )
        MatrixGrid(
            matrix = uiState.matrixB,
            onValueChanged = { row, col, value -> viewModel.onMatrixBValueChanged(row, col, value) }
        )
    }
}

@Composable
fun DimensionInputs(rows: String, cols: String, onRowsChange: (String) -> Unit, onColsChange: (String) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        OutlinedTextField(
            value = rows,
            onValueChange = onRowsChange,
            label = { Text("Rows") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.weight(1f)
        )
        OutlinedTextField(
            value = cols,
            onValueChange = onColsChange,
            label = { Text("Cols") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.weight(1f)
        )
    }
    Spacer(modifier = Modifier.height(8.dp))
}

@Composable
fun MatrixGrid(matrix: List<List<String>>, onValueChanged: (row: Int, col: Int, value: String) -> Unit) {
    val cols = matrix.firstOrNull()?.size ?: 0
    if (cols == 0) return

    LazyVerticalGrid(
        columns = GridCells.Fixed(cols),
        modifier = Modifier
            .fillMaxWidth()
            .height((matrix.size * 60).dp), // Approximate height
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items(matrix.size * cols) { index ->
            val row = index / cols
            val col = index % cols
            OutlinedTextField(
                value = matrix[row][col],
                onValueChange = { onValueChanged(row, col, it) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center)
            )
        }
    }
}

@Composable
fun ResultsSection(uiState: MatrixUiState) {
    if (uiState.error != null) {
        Text(uiState.error, color = MaterialTheme.colorScheme.error)
    }

    uiState.resultMatrix?.let {
        Text("Result Matrix", style = MaterialTheme.typography.titleMedium)
        MatrixDisplay(matrix = it)
    }

    uiState.resultScalar?.let {
        Text("Result: $it", style = MaterialTheme.typography.titleMedium)
    }

    uiState.resultSolution?.let {
        Text("Solution:", style = MaterialTheme.typography.titleMedium)
        Text(it)
    }

    if (uiState.steps.isNotEmpty()) {
        Spacer(modifier = Modifier.height(16.dp))
        Text("Steps", style = MaterialTheme.typography.titleLarge)
        uiState.steps.forEach { step ->
            StepItem(step = step)
        }
    }
}

@Composable
fun MatrixDisplay(matrix: List<List<String>>) {
    Column(
        modifier = Modifier
            .border(1.dp, Color.Gray)
            .padding(4.dp)
    ) {
        matrix.forEach { row ->
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                row.forEach { value ->
                    Text(value, modifier = Modifier.width(50.dp), textAlign = TextAlign.Center)
                }
            }
        }
    }
}

@Composable
fun StepItem(step: MatrixStepUiModel) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            MatrixDisplay(matrix = step.initialMatrix)
            Spacer(modifier = Modifier.height(8.dp))
            Text(step.title, style = MaterialTheme.typography.bodyMedium, textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.height(8.dp))
            MatrixDisplay(matrix = step.finalMatrix)
        }
    }
}
