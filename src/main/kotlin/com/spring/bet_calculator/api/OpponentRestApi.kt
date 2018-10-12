package com.spring.bet_calculator.api

import com.spring.bet_calculator.Constants
import com.spring.bet_calculator.repositories.OpponentRepository
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@Api(
        value = "/opponents",
        description = "Handling of creating opponents and changing ratio"
)
@RequestMapping(
        path = ["/opponents"],
        produces = [Constants.OPPONENT_JSON, Constants.BASE_JSON]
)
@RestController
class OpponentRestApi
{
    @Autowired
    lateinit var crud: OpponentRepository

    @Value("\${server.servlet.context-path}")
    private lateinit var contextPath: String

    @ApiOperation("Add win to ratio")
    @PatchMapping(path = ["/{id}/win"], consumes = [(MediaType.TEXT_PLAIN_VALUE)])
    fun addWin(
            @ApiParam("The id of the opponent")
            @PathVariable("id")
            id: Long) : ResponseEntity<Void>
    {
        val entity = crud.findById(id)
        val ratio = entity.get().ratio + 1

        if (crud.updateRatio(id, ratio))
        {
            return ResponseEntity.status(204).build()
        }

        return ResponseEntity.status(400).build()
    }

}

























