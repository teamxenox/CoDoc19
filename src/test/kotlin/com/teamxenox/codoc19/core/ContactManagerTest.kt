package com.teamxenox.codoc19.core

import com.winterbe.expekt.should
import org.junit.jupiter.api.Test

class ContactManagerTest {
    @Test
    fun testStatesSuccess() {
        ContactManager.getStates().size.should.above(1)
    }
}