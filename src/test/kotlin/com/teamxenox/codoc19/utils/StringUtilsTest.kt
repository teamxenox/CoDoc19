package com.teamxenox.codoc19.utils

import com.winterbe.expekt.should
import org.junit.jupiter.api.Test


class StringUtilsTest {
    @Test
    fun testSuccess() {
        StringUtils.addCommaIndia(0).should.equal("0")
        StringUtils.addCommaIndia(1).should.equal("1")
        StringUtils.addCommaIndia(100).should.equal("100")
        StringUtils.addCommaIndia(1000).should.equal("1,000")
        StringUtils.addCommaIndia(10000).should.equal("10,000")
        StringUtils.addCommaIndia(100000).should.equal("1,00,000")
        StringUtils.addCommaIndia(1000000).should.equal("10,00,000")
        StringUtils.addCommaIndia(10000000).should.equal("1,00,00,000")
        StringUtils.addCommaIndia(100000000).should.equal("10,00,00,000")
        StringUtils.addCommaIndia(1000000000).should.equal("1000000000")
    }

    @Test
    fun testInsertSuccess() {
        "ThisTest".insert(4, "Is").should.equal("ThisIsTest")
    }
}