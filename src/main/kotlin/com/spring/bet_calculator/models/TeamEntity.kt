package com.spring.bet_calculator.models

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
data class TeamEntity constructor(
        var name: String,
        var city: String,
        var country: String,
        @Id @GeneratedValue var id: Long? = null
)