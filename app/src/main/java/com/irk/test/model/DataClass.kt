package com.irk.test.model

import com.irk.test.model.Huffman.Node
import java.math.BigInteger

data class Step (val initialMatrix: Matrix, val stepDescription: String, val finalMatrix: Matrix)
data class CryptoStep(val title: String, val description: String)
data class RSAKeys(val n: BigInteger, val e: BigInteger, val d: BigInteger)
data class HuffmanStep(val left: Node, val right: Node, val parent: Node)
data class ReferenceItem(val title: String, val url: String, val material: Material)
enum class Material { ALGEO, MATDIS, STIMA}