package com.irk.test.ui.theme

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.irk.test.viewmodel.HuffmanNodeUiModel
import com.irk.test.viewmodel.HuffmanStepUiModel
import com.irk.test.viewmodel.HuffmanUiState
import com.irk.test.viewmodel.HuffmanViewModel

@Composable
fun HuffmanScreen(
    modifier: Modifier = Modifier,
    viewModel: HuffmanViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        ControlsSection(
            inputText = uiState.inputText,
            onInputChange = viewModel::onInputTextChanged,
            onProcessClick = viewModel::process
        )

        Divider(modifier = Modifier.padding(vertical = 16.dp))

        ResultsSection(
            uiState = uiState,
            onNextStep = viewModel::nextAnimationStep,
            onPrevStep = viewModel::previousAnimationStep
        )
    }
}

@Composable
private fun ControlsSection(
    inputText: String,
    onInputChange: (String) -> Unit,
    onProcessClick: () -> Unit
) {
    OutlinedTextField(
        value = inputText,
        onValueChange = onInputChange,
        label = { Text("Enter Text") },
        modifier = Modifier.fillMaxWidth()
    )
    Spacer(modifier = Modifier.height(8.dp))
    Button(
        onClick = onProcessClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text("Visualize Huffman Tree")
    }
}

@Composable
private fun ColumnScope.ResultsSection(
    uiState: HuffmanUiState,
    onNextStep: () -> Unit,
    onPrevStep: () -> Unit
) {
    if (uiState.error != null) {
        Text(text = uiState.error, color = MaterialTheme.colorScheme.error)
    }

    if (uiState.treeNodes.isNotEmpty()) {
        AnimationControls(
            onPrevClick = onPrevStep,
            onNextClick = onNextStep,
            step = uiState.currentAnimationStep,
            maxSteps = uiState.animationSteps.size - 1
        )
        Spacer(modifier = Modifier.height(8.dp))
    }

    LazyColumn(modifier = Modifier.weight(1f)) {
        item {
            if (uiState.treeNodes.isNotEmpty()) {
                TreeVisualization(
                    nodes = uiState.treeNodes,
                    animationSteps = uiState.animationSteps,
                    currentStep = uiState.currentAnimationStep
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
        item {
            if (uiState.frequencyTable.isNotEmpty()) {
                TableDisplay("Frequency Table", uiState.frequencyTable.mapValues { it.value.toString() })
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
        item {
            if (uiState.codeTable.isNotEmpty()) {
                TableDisplay("Code Table", uiState.codeTable)
            }
        }
    }
}

@Composable
fun AnimationControls(
    onPrevClick: () -> Unit,
    onNextClick: () -> Unit,
    step: Int,
    maxSteps: Int
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(onClick = onPrevClick, enabled = step > -1) {
            Text("Previous")
        }
        Text(
            text = "Step: ${step + 1}/${maxSteps + 1}",
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Button(onClick = onNextClick, enabled = step < maxSteps) {
            Text("Next")
        }
    }
}


@Composable
fun TreeVisualization(
    nodes: List<HuffmanNodeUiModel>,
    animationSteps: List<HuffmanStepUiModel>,
    currentStep: Int
) {
    val textMeasurer = rememberTextMeasurer()

    // Determine which nodes should be visible based on the current animation step
    val visibleNodeIds = remember(nodes, animationSteps, currentStep) {
        if (nodes.isEmpty()) return@remember emptySet()

        // Start with all the leaf nodes
        val visibleIds = nodes.filter { it.character != null }.map { it.id }.toMutableSet()

        // Add parent nodes up to the current step
        if (currentStep >= 0) {
            for (i in 0..currentStep) {
                animationSteps.getOrNull(i)?.let { step ->
                    visibleIds.add(step.parentNodeId)
                }
            }
        }
        visibleIds
    }

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(400.dp)
    ) {
        val minX = nodes.minOfOrNull { it.position.x } ?: 0f
        val maxX = nodes.maxOfOrNull { it.position.x } ?: 0f
        val treeWidth = maxX - minX
        val offsetX = (size.width - treeWidth) / 2 - minX
        val offsetY = 60f // Vertical offset to push the tree down

        val nodeMap = nodes.associateBy { it.id }
        val highlightedStep = animationSteps.getOrNull(currentStep)

        // Draw lines for visible nodes
        nodes.forEach { node ->
            if (node.id in visibleNodeIds) {
                val startPos = node.position.copy(x = node.position.x + offsetX, y = node.position.y + offsetY)
                node.leftId?.let { leftId ->
                    if (leftId in visibleNodeIds) {
                        nodeMap[leftId]?.let { leftChild ->
                            val endPos = leftChild.position.copy(x = leftChild.position.x + offsetX, y = leftChild.position.y + offsetY)
                            drawLine(Color.Gray, start = startPos, end = endPos, strokeWidth = 2f)
                        }
                    }
                }
                node.rightId?.let { rightId ->
                    if (rightId in visibleNodeIds) {
                        nodeMap[rightId]?.let { rightChild ->
                            val endPos = rightChild.position.copy(x = rightChild.position.x + offsetX, y = rightChild.position.y + offsetY)
                            drawLine(Color.Gray, start = startPos, end = endPos, strokeWidth = 2f)
                        }
                    }
                }
            }
        }

        // Draw visible nodes and text
        nodes.forEach { node ->
            if (node.id in visibleNodeIds) {
                val center = node.position.copy(x = node.position.x + offsetX, y = node.position.y + offsetY)
                val radius = 40f

                val (nodeColor, strokeWidth) = when (node.id) {
                    highlightedStep?.leftNodeId -> Pair(Color.Green, 6f)
                    highlightedStep?.rightNodeId -> Pair(Color.Green, 6f)
                    highlightedStep?.parentNodeId -> Pair(Color.Blue, 6f)
                    else -> Pair(if (node.character != null) Color.Cyan else Color.LightGray, 3f)
                }

                drawCircle(
                    color = nodeColor,
                    radius = radius,
                    center = center,
                    style = Stroke(width = strokeWidth)
                )

                val textToShow = node.character?.let { "'$it'" } ?: node.frequency.toString()
                val textLayoutResult = textMeasurer.measure(
                    text = textToShow,
                    style = TextStyle(fontSize = 14.sp, textAlign = TextAlign.Center)
                )
                drawText(
                    textLayoutResult = textLayoutResult,
                    topLeft = Offset(
                        x = center.x - textLayoutResult.size.width / 2,
                        y = center.y - textLayoutResult.size.height / 2
                    )
                )
            }
        }
    }
}

@Composable
fun TableDisplay(title: String, data: Map<Char, String>) {
    Text(text = title, style = MaterialTheme.typography.titleLarge)
    Spacer(modifier = Modifier.height(8.dp))
    Card {
        Column(modifier = Modifier.padding(16.dp)) {
            data.forEach { (char, value) ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("'$char'")
                    Text(value)
                }
                Divider(modifier = Modifier.padding(vertical = 4.dp))
            }
        }
    }
}
