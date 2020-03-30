package com.teamxenox.codoc19.data.entities

import java.util.*
import javax.persistence.*

@Entity(name = "charts")
class Chart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
    var userId: Int? = null
    lateinit var tgFileId: String

    @Enumerated(EnumType.STRING)
    lateinit var chartType: Type

    lateinit var country: String
    lateinit var createdAt: String

    enum class Type {
        DEATH, CASE
    }
}