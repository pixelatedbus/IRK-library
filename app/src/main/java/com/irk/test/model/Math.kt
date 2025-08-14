package com.irk.test.model

import java.math.BigInteger
object Math {

    fun modPow(base: BigInteger, exponent: BigInteger, modulus: BigInteger): BigInteger {
        var res = BigInteger.ONE
        var b = base.mod(modulus)
        var exp = exponent

        while (exp > BigInteger.ZERO) {
            if (exp.testBit(0)) {
                res = (res * b).mod(modulus)
            }
            exp = exp.shiftRight(1)
            b = (b * b).mod(modulus)
        }
        return res
    }

    fun gcd(a: BigInteger, b: BigInteger): BigInteger {
        var tempA = a
        var tempB = b
        while (tempB != BigInteger.ZERO) {
            val temp = tempB
            tempB = tempA.mod(tempB)
            tempA = temp
        }
        return tempA
    }

    fun modInverse(a: BigInteger, m: BigInteger): BigInteger {
        var m0 = m
        var a0 = a
        var x0 = BigInteger.ZERO
        var x1 = BigInteger.ONE

        if (m0 == BigInteger.ONE) return BigInteger.ZERO

        while (a0 > BigInteger.ONE) {
            val q = a0 / m0
            var t = m0

            m0 = a0.mod(m0)
            a0 = t
            t = x0

            x0 = x1 - q * x0
            x1 = t
        }

        if (x1 < BigInteger.ZERO) {
            x1 = x1.add(m)
        }
        return x1
    }
}