package com.irk.test.viewmodel

import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.irk.test.model.Huffman
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class HuffmanNodeUiModel(
    val id: Int,
    val character: Char?,
    val frequency: Int,
    val code: String,
    val position: Offset,
    val leftId: Int?,
    val rightId: Int?
)

data class HuffmanStepUiModel(
    val leftNodeId: Int,
    val rightNodeId: Int,
    val parentNodeId: Int
)

data class HuffmanUiState(
    val inputText: String = "",
    val encodedText: String = "",
    val decodedText: String = "",
    val frequencyTable: Map<Char, Int> = emptyMap(),
    val codeTable: Map<Char, String> = emptyMap(),
    val treeNodes: List<HuffmanNodeUiModel> = emptyList(),
    val animationSteps: List<HuffmanStepUiModel> = emptyList(),
    val currentAnimationStep: Int = -1,
    val error: String? = null
)

class HuffmanViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(HuffmanUiState())
    val uiState: StateFlow<HuffmanUiState> = _uiState.asStateFlow()

    fun onInputTextChanged(text: String) {
        _uiState.update { it.copy(inputText = text, error = null) }
    }

    fun process() {
        viewModelScope.launch {
            val currentState = _uiState.value
            if (currentState.inputText.isEmpty()) {
                _uiState.update { it.copy(error = "Input text cannot be empty.") }
                return@launch
            }

            try {
                val huffman = Huffman(currentState.inputText)
                val nodeMap = mutableMapOf<Huffman.Node, Int>()
                val treeNodes = buildTreeUiModel(huffman.root, nodeMap)
                val animationSteps = huffman.huffmanSteps.map { step ->
                    HuffmanStepUiModel(
                        leftNodeId = nodeMap[step.left]!!,
                        rightNodeId = nodeMap[step.right]!!,
                        parentNodeId = nodeMap[step.parent]!!
                    )
                }

                _uiState.update {
                    it.copy(
                        encodedText = huffman.encode(),
                        decodedText = huffman.decode(huffman.encode()),
                        frequencyTable = huffman.frequencyTable,
                        codeTable = huffman.codeTable,
                        treeNodes = treeNodes,
                        animationSteps = animationSteps,
                        currentAnimationStep = -1,
                        error = null
                    )
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message ?: "An error occurred.") }
            }
        }
    }

    fun nextAnimationStep() {
        _uiState.update {
            val maxSteps = it.animationSteps.size - 1
            if (it.currentAnimationStep < maxSteps) {
                it.copy(currentAnimationStep = it.currentAnimationStep + 1)
            } else {
                it
            }
        }
    }

    fun previousAnimationStep() {
        _uiState.update {
            if (it.currentAnimationStep > -1) {
                it.copy(currentAnimationStep = it.currentAnimationStep - 1)
            } else {
                it
            }
        }
    }


    private fun buildTreeUiModel(root: Huffman.Node?, nodeMap: MutableMap<Huffman.Node, Int>): List<HuffmanNodeUiModel> {
        if (root == null) return emptyList()

        val nodes = mutableListOf<HuffmanNodeUiModel>()
        val codeMap = mutableMapOf<Huffman.Node, String>()
        var nodeIdCounter = 0

        fun assignCodes(node: Huffman.Node, code: String) {
            codeMap[node] = code
            if (node.left != null) assignCodes(node.left, code + "0")
            if (node.right != null) assignCodes(node.right, code + "1")
        }
        assignCodes(root, "")

        val ySpacing = 150f
        val xSpacing = 100f

        var leafCounter = 0
        fun traverseAndBuild(node: Huffman.Node, depth: Int): Int {
            val currentId = nodeIdCounter++
            nodeMap[node] = currentId
            val code = codeMap[node] ?: ""

            val xPos: Float
            val yPos = depth * ySpacing

            if (node.isLeaf()) {
                xPos = leafCounter * xSpacing
                leafCounter++
            } else {
                val leftChildId = if (node.left != null) traverseAndBuild(node.left, depth + 1) else null
                val rightChildId = if (node.right != null) traverseAndBuild(node.right, depth + 1) else null

                val leftChildPos = nodes.find { it.id == leftChildId }?.position
                val rightChildPos = nodes.find { it.id == rightChildId }?.position

                xPos = if (leftChildPos != null && rightChildPos != null) {
                    (leftChildPos.x + rightChildPos.x) / 2
                } else {
                    leftChildPos?.x ?: rightChildPos?.x ?: 0f
                }

                nodes.add(
                    HuffmanNodeUiModel(
                        id = currentId,
                        character = null,
                        frequency = node.frequency,
                        code = code,
                        position = Offset(xPos, yPos),
                        leftId = leftChildId,
                        rightId = rightChildId
                    )
                )
                return currentId
            }

            nodes.add(
                HuffmanNodeUiModel(
                    id = currentId,
                    character = node.character,
                    frequency = node.frequency,
                    code = code,
                    position = Offset(xPos, yPos),
                    leftId = null,
                    rightId = null
                )
            )
            return currentId
        }

        traverseAndBuild(root, 0)
        return nodes
    }
}