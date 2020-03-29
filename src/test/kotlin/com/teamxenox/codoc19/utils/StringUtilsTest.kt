package com.teamxenox.codoc19.utils

import com.winterbe.expekt.should
import org.junit.jupiter.api.Test


class StringUtilsTest {
    @Test
    fun testSuccess() {
        StringUtils.addComma(0).should.equal("0")
        StringUtils.addComma(1).should.equal("1")
        StringUtils.addComma(100).should.equal("100")
        StringUtils.addComma(1000).should.equal("1,000")
        StringUtils.addComma(10000).should.equal("10,000")
        StringUtils.addComma(100000).should.equal("1,00,000")
        StringUtils.addComma(1000000).should.equal("10,00,000")
        StringUtils.addComma(10000000).should.equal("1,00,00,000")
        StringUtils.addComma(100000000).should.equal("10,00,00,000")
        StringUtils.addComma(1000000000).should.equal("1000000000")
    }

    @Test
    fun testInsertSuccess() {
        "ThisTest".insert(4, "Is").should.equal("ThisIsTest")
    }
}