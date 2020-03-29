package com.teamxenox.covid19api.utils

object ArrayUtils {

    /**
     * To find sum of each column in a matrix
     */
    fun mergeColumn(matrix: List<List<Int>>): Array<Int> {
        val result = mutableListOf<Int>()
        val numOfCols = matrix.first().size
        var sumCol: Int
        for (i in 0 until numOfCols) {
            sumCol = 0
            for (j in matrix.indices) {
                sumCol += matrix[j][i]
            }
            result.add(sumCol)
        }
        return result.toTypedArray()
    }
}