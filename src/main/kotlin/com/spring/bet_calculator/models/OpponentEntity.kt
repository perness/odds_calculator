package com.spring.bet_calculator.models

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
data class OpponentEntity constructor(
        var name: String,
        var ratio: Int,
        @get:Id @get:GeneratedValue var id: Long? = null
)