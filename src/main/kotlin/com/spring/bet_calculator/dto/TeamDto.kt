package com.spring.bet_calculator.dto

import io.swagger.annotations.ApiModelProperty
import java.lang.Deprecated

data class TeamDto constructor(
    @ApiModelProperty("The name of the football team")
    var name: String? = null,

    @ApiModelProperty("The city the football team")
    var city: String? = null,

    @ApiModelProperty("The country of the football team")
    var country: String? = null,

    @ApiModelProperty("The auto generated teamId of the football team")
    var teamId: String? = null
) {
    @ApiModelProperty("Deprecated. Use teamId instead")
    @Deprecated
    var id: String? = null
}