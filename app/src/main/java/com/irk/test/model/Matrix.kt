package com.irk.test.model

import kotlin.math.abs

class Matrix(val rows: Int, val columns: Int) {
    private val data: Array<DoubleArray> = Array(rows) { DoubleArray(columns) }

    operator fun get(row: Int, col: Int): Double {
        if (row >= rows || col >= columns || row < 0 || col < 0) {
            throw IndexOutOfBoundsException("Matrix index out of bounds.")
        }
        return data[row][col]
    }

    operator fun set(row: Int, col: Int, value: Double) {
        if (row >= rows || col >= columns || row < 0 || col < 0) {
            throw IndexOutOfBoundsException("Matrix index out of bounds.")
        }
        data[row][col] = value
    }

    operator fun plus(other: Matrix): Matrix {
        if (rows != other.rows || columns != other.columns) {
            throw IllegalArgumentException("Matrix dimensions must match.")
        }
        val result = Matrix(rows, columns)
        for (i in 0 until rows) {
            for (j in 0 until columns) {
                result[i, j] = this[i, j] + other[i, j]
            }
        }
        return result
    }

    operator fun minus(other: Matrix): Matrix {
        if (rows != other.rows || columns != other.columns) {
            throw IllegalArgumentException("Matrix dimensions must match.")
        }
        val result = Matrix(rows, columns)
        for (i in 0 until rows) {
            for (j in 0 until columns) {
                result[i, j] = this[i, j] - other[i, j]
            }
        }
        return result
    }

    override fun toString(): String {
        val sb = StringBuilder()
        for (i in 0 until rows) {
            for (j in 0 until columns) {
                sb.append(data[i][j])
                sb.append(" ")
            }
            sb.append("\n")
        }
        return sb.toString()
    }

    fun copy(): Matrix {
        val result = Matrix(rows, columns)
        for (i in 0 until rows) {
            for (j in 0 until columns) {
                result[i, j] = this[i, j]
            }
        }
        return result
    }

    fun isSquare(): Boolean {
        return rows == columns
    }

    fun transpose(): Matrix {
        val result = Matrix(columns, rows)
        for (i in 0 until rows) {
            for (j in 0 until columns) {
                result[j, i] = this[i, j]
            }
        }
        return result
    }

    fun augment(other: Matrix): Matrix {
        if (rows != other.rows) {
            throw IllegalArgumentException("Matrix dimensions must match.")
        }
        val result = Matrix(rows, columns + other.columns)
        for (i in 0 until rows) {
            for (j in 0 until columns) {
                result[i, j] = this[i, j]
                result[i, j + columns] = other[i, j]
            }
        }
        return result
    }

    fun multiplyDiagonal(): Double {
        var product = 1.0
        for (i in 0 until rows) {
            product *= this[i, i]
        }
        return product
    }

    fun split(): Pair<Matrix, Matrix> {
        val left = Matrix(rows, columns / 2)
        val right = Matrix(rows, columns / 2)
        for (i in 0 until rows) {
            for (j in 0 until columns / 2) {
                left[i, j] = this[i, j]
                right[i, j] = this[i, j + columns / 2]
            }
        }
        return Pair(left, right)
    }

    fun splitConstants(): Pair<Matrix, Matrix> {
        val left = Matrix(rows, columns - 1)
        val right = Matrix(rows, 1)
        for (i in 0 until rows) {
            for (j in 0 until columns - 1) {
                left[i, j] = this[i, j]
            }
            right[i, 0] = this[i, columns - 1]
        }
        return Pair(left, right)
    }

    fun multiplyRow(row: Int, factor: Double) {
        for (i in 0 until columns) {
            this[row, i] *= factor
        }
    }

    fun multiplyConstant(factor: Double) {
        for (i in 0 until rows) {
            for (j in 0 until columns) {
                this[i, j] *= factor
            }
        }
    }

    fun addRow(row: Int, number: Double){
        for (i in 0 until columns) {
            this[row, i] += number
        }
    }

    fun addRowRow(row1: Int, row2: Int, factor: Double) {
        for (i in 0 until columns) {
            this[row1, i] += factor * this[row2, i]
        }
    }

    fun swapRow(row1: Int, row2: Int) {
        val temp = DoubleArray(columns)
        System.arraycopy(data[row1], 0, temp, 0, columns)
        System.arraycopy(data[row2], 0, data[row1], 0, columns)
        System.arraycopy(temp, 0, data[row2], 0, columns)
    }

    fun replaceCol(col: Int, values: DoubleArray) {
        for (i in 0 until rows) {
            this[i, col] = values[i]
        }
    }

    fun hasNoSolution(): Boolean {
        for (i in 0 until rows) {
            var coefficientSum = 0.0
            for (j in 0 until columns - 1) {
                coefficientSum += abs(this[i, j])
            }

            if (coefficientSum == 0.0 && this[i, columns - 1] != 0.0) {
                return true
            }
        }
        return false
    }

    fun hasInfiniteSolutions(): Boolean {
        for (i in 0 until rows) {
            var isAllZero = true
            for (j in 0 until columns) {
                if (this[i, j] != 0.0) {
                    isAllZero = false
                    break
                }
            }
            if (isAllZero) {
                return true
            }
        }
        return false
    }
    fun subMatrix(rowStart: Int, rowEnd: Int, colStart: Int, colEnd: Int): Matrix {
        val newRows = rowEnd - rowStart
        val newCols = colEnd - colStart
        val result = Matrix(newRows, newCols)
        for (i in 0 until newRows) {
            for (j in 0 until newCols) {
                result[i, j] = this[rowStart + i, colStart + j]
            }
        }
        return result
    }
    fun join(subMatrix: Matrix, rowStart: Int, colStart: Int) {
        for (i in 0 until subMatrix.rows) {
            for (j in 0 until subMatrix.columns) {
                this[rowStart + i, colStart + j] = subMatrix[i, j]
            }
        }
    }
}