package com.irk.test.model

import java.util.PriorityQueue

class Huffman(val text: String) {

    val root: Node
    val frequencyTable: Map<Char, Int>
    val codeTable: Map<Char, String>
    val huffmanSteps: List<HuffmanStep>

    data class Node(
        val character: Char?,
        val frequency: Int,
        val left: Node? = null,
        val right: Node? = null
    ) : Comparable<Node> {
        fun isLeaf(): Boolean = left == null && right == null

        override fun compareTo(other: Node): Int = frequency.compareTo(other.frequency)
    }

    init {
        if (text.isEmpty()) {
            throw IllegalArgumentException("Input text cannot be null or empty.")
        }
        frequencyTable = buildFrequencyTable()
        val steps = mutableListOf<HuffmanStep>()
        root = buildTree(steps)
        huffmanSteps = steps
        codeTable = generateCodes()
    }

    private fun buildFrequencyTable(): Map<Char, Int> {
        return text.groupingBy { it }.eachCount()
    }

    private fun buildTree(steps: MutableList<HuffmanStep>): Node {
        val priorityQueue = PriorityQueue<Node>()

        frequencyTable.forEach { (char, freq) ->
            priorityQueue.add(Node(char, freq))
        }

        if (priorityQueue.size == 1) {
            val singleNode = priorityQueue.poll()
            return Node(null, singleNode!!.frequency, singleNode, null)
        }

        while (priorityQueue.size > 1) {
            val left = priorityQueue.poll()
            val right = priorityQueue.poll()

            val parent = Node(null, left!!.frequency + right!!.frequency, left, right)

            steps.add(HuffmanStep(left, right, parent))

            priorityQueue.add(parent)
        }

        return priorityQueue.poll()
    }

    private fun generateCodes(): Map<Char, String> {
        val codes = mutableMapOf<Char, String>()

        fun traverse(node: Node?, code: String) {
            if (node == null) return

            if (node.isLeaf()) {
                node.character?.let { codes[it] = code }
                return
            }

            traverse(node.left, code + "0")
            traverse(node.right, code + "1")
        }

        traverse(root, "")
        return codes
    }

    fun encode(): String {
        return text.map { codeTable[it] }.joinToString("")
    }

    fun decode(encodedText: String): String {
        val decodedText = StringBuilder()
        var currentNode = root

        encodedText.forEach { bit ->

            currentNode = if (bit == '0') {
                currentNode.left!!
            } else {
                currentNode.right!!
            }

            if (currentNode.isLeaf()) {
                decodedText.append(currentNode.character!!)
                currentNode = root
            }
        }
        return decodedText.toString()
    }
}
