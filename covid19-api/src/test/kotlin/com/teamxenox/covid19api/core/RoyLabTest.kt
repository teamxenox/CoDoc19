package com.teamxenox.covid19api.core

import com.winterbe.expekt.should
import org.junit.Assert.*
import org.junit.Test

class RoyLabTest {
    @Test
    fun testGetDataSuccess() {
        val data = RoyLab.getData()
        data.size.should.above(100)
        println(data)
    }
}