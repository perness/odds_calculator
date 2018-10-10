package com.spring.bet_calculator.dto

import io.swagger.annotations.ApiModelProperty
import java.lang.Deprecated

class OpponentDto constructor(
    @ApiModelProperty("Team name of the opponent")
    var name: String? = null,

    @ApiModelProperty("Ratio of win/loss")
    var ratio: Int? = null,

    @ApiModelProperty("The auto generated teamId of the football team")
    var opponentId: String? = null
) {
    @ApiModelProperty("Deprecated. Use teamId instead")
    @Deprecated
    var id: String? = null
}