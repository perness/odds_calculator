package com.spring.bet_calculator.api

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.common.base.Throwables
import com.spring.bet_calculator.Constants
import com.spring.bet_calculator.dto.OpponentConverter
import com.spring.bet_calculator.dto.OpponentDto
import com.spring.bet_calculator.models.OpponentEntity
import com.spring.bet_calculator.repositories.OpponentRepository
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import io.swagger.annotations.ApiResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.ConstraintViolationException

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

    @ApiOperation("Get all opponents")
    @GetMapping
    fun get() : ResponseEntity<List<OpponentDto>>
    {
        val list = crud.findAll()
        return ResponseEntity.ok(OpponentConverter.transform(list))
    }

    @ApiOperation("Get opponent by ID")
    @GetMapping(path = ["/{id}"], produces = [(Constants.OPPONENT_JSON)])
    fun getById(@ApiParam(Constants.OPPONENT_ID_PARAM)
                @PathVariable("id")
                id: Long?) : ResponseEntity<OpponentDto>
    {
        val entity = crud.findById(id!!)

        return ResponseEntity.ok(OpponentConverter.transform(entity.get()))
    }

    @ApiOperation("Delete opponent with given ID")
    @DeleteMapping(path = ["/{id}"])
    fun delete(
            @ApiParam(Constants.OPPONENT_ID_PARAM)
            @PathVariable("id")
            pathId: String?) : ResponseEntity<Any>
    {
        val id: Long?

        try
        {
            id = pathId!!.toLong()
        }
        catch (e: Exception)
        {
            return ResponseEntity.status(400).build()
        }

        if (!crud.existsById(id))
        {
            return ResponseEntity.status(404).build()
        }

        crud.deleteById(id)
        return ResponseEntity.status(204).build()
    }

    @ApiOperation("Create opponent")
    @PostMapping(consumes = [Constants.OPPONENT_JSON, Constants.BASE_JSON])
    @ApiResponse(code = 201, message = "The id of the newly created opponent")
    fun create(
            @ApiParam("Name of the opponent, start ratio and id(null)")
            @RequestBody
            dto: OpponentDto) : ResponseEntity<Long>
    {
        if (!(dto.id.isNullOrEmpty() && dto.opponentId.isNullOrEmpty()))
        {
            return ResponseEntity.status(400).build()
        }

        if (dto.name == null || dto.ratio == null)
        {
            return ResponseEntity.status(400).build()
        }

        val entity: OpponentEntity?

        try
        {
            entity = crud.save(OpponentEntity(dto.name!!, dto.ratio!!))
        }
        catch (e: Exception)
        {
            if (Throwables.getRootCause(e) is ConstraintViolationException)
            {
                return ResponseEntity.status(400).build()
            }
            throw e
        }

        return ResponseEntity.status(201).body(entity.id)
    }

    @ApiOperation("Add win to ratio using JSON Merge patch")
    @PatchMapping(path = ["/{id}"],
            consumes = ["application/merge-patch+json"])
    fun mergePatch(
            @ApiParam(Constants.OPPONENT_ID_PARAM)
            @PathVariable("id")
            id: Long?,
            @ApiParam("The partial patch")
            @RequestBody
            jsonPatch : String) : ResponseEntity<Void>
    {
        val dto = crud.findById(id!!).get()
        val jackson = ObjectMapper()
        val jsonNode: JsonNode

        try
        {
            jsonNode = jackson.readValue(jsonPatch, JsonNode::class.java)
        }
        catch (e: Exception)
        {
            return ResponseEntity.status(400).build()
        }

        if (jsonNode.has("id"))
        {
            return ResponseEntity.status(409).build()
        }

        var newName = dto.name
        var newRatio = dto.ratio

        if (jsonNode.has("name"))
        {
            val nameNode = jsonNode.get("name")
            if (nameNode.isNull)
            {
                newName = null.toString()
            }
            else if (nameNode.isTextual)
            {
                newName = nameNode.asText()
            }
            else
            {
                return ResponseEntity.status(400).build()
            }
        }

        if (jsonNode.has("ratio"))
        {
            val ratioNode = jsonNode.get("ratio")
            if (ratioNode.isNull)
            {
                newRatio = 0
            }
            else if (ratioNode.isNumber)
            {
                newRatio = ratioNode.intValue()
            }
            else
            {
                return ResponseEntity.status(400).build()
            }
        }
        crud.update(id, newName, newRatio)

        return ResponseEntity.status(204).build()
    }
}

























