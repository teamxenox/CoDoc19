package com.teamxenox.covid19api.utils

object ArrayUtils {

    /**
     * To find sum of each column in a matrix
     */
    fun mergeColumn(matrix: List<List<Int>>): List<Int> {
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
        return result
    }


    /**
     * To get difference without negative values
     */
    fun getDiffNoNegative(data: List<Int>): List<Int> {

        val diff = mutableListOf<Int>()
        var x = 0
        for ((i, d) in data.withIndex()) {
            if (i == 0) {
                x = d
                diff.add(d)
                continue
            }

            var xDiff = d - x
            if (xDiff < 0) {
                // damn fix!! -_-
                xDiff = 0
            }
            diff.add(xDiff)
            x = d
        }
        return diff
    }

    fun trimStartNonDeaths(data: List<Int>): List<Int> {
        val newData = mutableListOf<Int>()
        for (d in data) {
            if (d == 0 && newData.isEmpty()) {
                continue
            }

            newData.add(d)
        }
        return newData
    }

}