package com.spring.bet_calculator.models

import com.spring.bet_calculator.constraint.Country
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

@Entity
data class TeamEntity constructor(
        @get:NotBlank @get:Size(max = 64) var name: String,
        @get:NotBlank @get:Size(max = 64) var city: String,
        @get:Country var country: String,
        var teams: LinkedHashMap<String, OpponentEntity>? = null,
        @get:Id @get:GeneratedValue var id: Long? = null
)