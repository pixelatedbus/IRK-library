package com.irk.test.model

import java.math.BigInteger
import kotlin.text.iterator

object Crypto {
    fun caesarEncrypt(text: String, shift: Int): Pair<String, List<CryptoStep>> {
        val steps = mutableListOf<CryptoStep>()
        val result = StringBuilder()

        text.forEach { char ->
            if (char.isLetter()) {
                val start = if (char.isUpperCase()) 'A' else 'a'
                val shiftedChar = start + (char - start + shift) % 26
                result.append(shiftedChar)
                steps.add(
                    CryptoStep(
                        "Encrypt '$char'",
                        "'$char' (${char - start}) + $shift => '$shiftedChar' (${(char - start + shift) % 26})"
                    )
                )
            } else {
                result.append(char)
            }
        }
        return Pair(result.toString(), steps)
    }

    fun generateRsaKeys(p: BigInteger, q: BigInteger): Pair<RSAKeys, List<CryptoStep>> {
        val steps = mutableListOf<CryptoStep>()

        val n = p * q
        steps.add(CryptoStep("1. Calculate Modulus (n)", "n = p * q = $p * $q = $n"))

        val phi = (p - BigInteger.ONE) * (q - BigInteger.ONE)
        steps.add(
            CryptoStep(
                "2. Calculate Euler's Totient (phi)",
                "phi = (p-1)*(q-1) = ${p - BigInteger.ONE}*${q - BigInteger.ONE} = $phi"
            )
        )

        var e = BigInteger("3")
        while (Math.gcd(phi, e) > BigInteger.ONE) {
            e = e.add(BigInteger("2"))
        }
        steps.add(CryptoStep("3. Find Public Key (e)", "Found e = $e, where gcd(e, phi) = 1"))

        val d = Math.modInverse(e, phi)
        steps.add(CryptoStep("4. Calculate Private Key (d)", "d = e⁻¹ mod phi = $d"))

        val keys = RSAKeys(n, e, d)
        return Pair(keys, steps)
    }

    fun rsaEncrypt(text: String, keys: RSAKeys): Pair<String, List<CryptoStep>> {
        val steps = mutableListOf<CryptoStep>()
        val nString = keys.n.toString()
        val blockSize = nString.length - 1

        val sanitizedText = text.replace("\r\n", " ").replace("\n", " ").filter { it.code >= 32 }

        val numericStringBuilder = StringBuilder()
        for (char in sanitizedText) {
            var asciiValue = char.code.toString()
            while (asciiValue.length < 3) {
                asciiValue = "0$asciiValue"
            }
            numericStringBuilder.append(asciiValue)
        }
        val numericString = numericStringBuilder.toString()
        steps.add(
            CryptoStep(
                "1. Sanitize and Convert to Padded ASCII",
                "'$sanitizedText' -> $numericString"
            )
        )

        val blocks = mutableListOf<String>()
        var index = 0
        while (index < numericString.length) {
            val endIndex = minOf(index + blockSize, numericString.length)
            blocks.add(numericString.substring(index, endIndex))
            index += blockSize
        }
        steps.add(CryptoStep("2. Split into Blocks of size $blockSize", blocks.joinToString(" ")))

        val encryptedBlocks = mutableListOf<String>()
        for (block in blocks) {
            val m = BigInteger(block)
            val c = Math.modPow(m, keys.e, keys.n)
            steps.add(
                CryptoStep(
                    "Encrypt block '$m'",
                    "C = M^e mod n = $m^${keys.e} mod ${keys.n} = $c"
                )
            )
            encryptedBlocks.add(c.toString())
        }

        return Pair(encryptedBlocks.joinToString(" "), steps)
    }

    fun rsaDecrypt(ciphertext: String, keys: RSAKeys): Pair<String, List<CryptoStep>> {
        val steps = mutableListOf<CryptoStep>()
        val encryptedBlocks = ciphertext.split(" ")

        val nString = keys.n.toString()
        val blockSize = nString.length - 1

        val decryptedNumericStrings = mutableListOf<String>()
        for (block in encryptedBlocks) {
            if (block.isBlank()) {
                continue
            }
            val c = BigInteger(block)
            val m = Math.modPow(c, keys.d, keys.n)
            steps.add(
                CryptoStep(
                    "Decrypt block '$c'",
                    "M = C^d mod n = $c^${keys.d} mod ${keys.n} = $m"
                )
            )

            var paddedM = m.toString()
            while (paddedM.length < blockSize) {
                paddedM = "0$paddedM"
            }
            decryptedNumericStrings.add(paddedM)
        }

        val numericString = decryptedNumericStrings.joinToString("")
        steps.add(CryptoStep("2. Reconstruct Numeric String", numericString))

        val textBuilder = StringBuilder()
        var i = 0
        while (i < numericString.length) {
            if (i + 3 <= numericString.length) {
                val chunk = numericString.substring(i, i + 3)
                textBuilder.append(chunk.toInt().toChar())
            }
            i += 3
        }
        val text = textBuilder.toString()
        steps.add(CryptoStep("3. Convert to Text", text))

        return Pair(text, steps)
    }
}
