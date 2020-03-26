package com.teamxenox.scholar

import com.teamxenox.scholar.subjects.corona.Corona
import com.winterbe.expekt.should
import org.junit.Test

class ScholarTest {
    @Test
    fun testCoronaSuccess() {
        val question = "How does it spread?"
        val answer = Scholar.getAnswer(Corona, question)
        answer!!.answer.should.contain("cough")
    }

    @Test
    fun testCoronaFailure() {
        val question = "Define gravity"
        val answer = Scholar.getAnswer(Corona, question)
        answer.should.equal(null)
    }
}