package com.teamxenox.covid19api.utils

import com.winterbe.expekt.should
import org.junit.Test
import java.io.File

class ArrayUtilsTest {

    @Test
    fun testLinearMergeSuccess() {
        val data = listOf(
                listOf(1, 2, 3, 4),
                listOf(4, 5, 6, 5),
                listOf(3, 6, 9, 6)
        )

        val merged = ArrayUtils.mergeColumn(data)
        merged[0].should.equal(1 + 4 + 3)
        merged[1].should.equal(2 + 5 + 6)
        merged[2].should.equal(3 + 6 + 9)
        merged[3].should.equal(4 + 5 + 6)

        println(File("charts/").absolutePath)
    }

    @Test
    fun testDiffSuccess() {
        val data = listOf(1, 2, 3, 3, 8)
        val diffArr = ArrayUtils.getDiffNoNegative(data)
        diffArr.toString().should.equal("[1, 1, 1, 0, 5]")
    }

    @Test
    fun testTrimNonDeaths() {
        val deaths = listOf<Int>(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 2, 2, 2, 3, 4, 5, 0, 0)
        val result = ArrayUtils.trimStartNonDeaths(deaths)
        result.toString().should.equal("[1, 2, 2, 2, 3, 4, 5, 0, 0]")
    }
}