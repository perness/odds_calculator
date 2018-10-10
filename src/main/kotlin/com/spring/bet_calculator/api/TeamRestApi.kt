package com.spring.bet_calculator.api

import com.google.common.base.Throwables
import com.spring.bet_calculator.Constants
import com.spring.bet_calculator.dto.TeamConverter
import com.spring.bet_calculator.dto.TeamDto
import com.spring.bet_calculator.models.TeamEntity
import com.spring.bet_calculator.repositories.TeamRepository
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import io.swagger.annotations.ApiResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.ConstraintViolationException

/*const val ID_PARAM = "The numeric teamId of the team"
const val BASE_JSON = "application/json;charset=UTF-8"
const val TEAM_JSON = "application/vnd.bet_calculator.teams+json;charset=UTF-8;version=2"*/

@Api(
        value = "/teams",
        description = "Handling of creating and retrieving teams"
)
@RequestMapping(
        path = ["/teams"], // when the url is "<base>/teams", then this class will be used to handle it
        produces = [Constants.TEAM_JSON, Constants.BASE_JSON]
)
@RestController
class TeamRestApi
{
    @Autowired
    private lateinit var teamCrud: TeamRepository

    @Value("\${server.servlet.context-path}")
    private lateinit var contextPath: String

    /*
        request URL parameters are in the form

        ?<name>=<value>&<name>=<value>&...

        for example

        /teams?country=Norway&teamId=3

        So here we ll have a single endpoint for getting "teams", where
        optional filtering on "country" and "city" will be based on
        URL parameters, and not different endpoints
     */
    @ApiOperation("Get all teams")
    @GetMapping
    fun get(@ApiParam("The country name")
            @RequestParam("country", required = false)
            country: String?,

            @ApiParam("The city of the teams")
            @RequestParam("city", required = false)
            city: String?
            ) : ResponseEntity<List<TeamDto>>
    {
        val list = if (country.isNullOrBlank() && city.isNullOrBlank())
        {
            teamCrud.findAll()
        }
        else if (!country.isNullOrBlank() && !city.isNullOrBlank())
        {
            teamCrud.findAllByCountryAndCity(country!!, city!!)
        }
        else if (!country.isNullOrBlank())
        {
            teamCrud.findAllByCountry(country!!)
        }
        else
        {
            teamCrud.findAllByCity(city!!)
        }

        return ResponseEntity.ok(TeamConverter.transform(list))
    }

    @ApiOperation("Create team")
    @PostMapping(consumes = [Constants.TEAM_JSON, Constants.BASE_JSON])
    @ApiResponse(code = 201, message = "The teamId of the newly created team")
    fun createTeam(
            @ApiParam("Name of team, name of city, name of country and ID(null)")
            @RequestBody
            teamDto: TeamDto
    ) : ResponseEntity<Long>
    {
        if (!(teamDto.id.isNullOrEmpty() && teamDto.teamId.isNullOrEmpty()))
        {
            return ResponseEntity.status(400).build()
        }
        if (teamDto.name == null || teamDto.city == null || teamDto.country == null)
        {
            return ResponseEntity.status(400).build()
        }

        val id: Long?

        try
        {
            id = teamCrud.createTeam(teamDto.name!!, teamDto.city!!, teamDto.country!!)
        }
        catch (e: Exception)
        {
            if (Throwables.getRootCause(e) is ConstraintViolationException)
            {
                return ResponseEntity.status(400).build()
            }
            throw e
        }
        return ResponseEntity.status(201).body(id)
    }

    @ApiOperation("Delete a team with given ID")
    @DeleteMapping(path = ["/{id}"])
    fun delete(
            @ApiParam(Constants.TEAM_ID_PARAM)
            @PathVariable("id")
            pathId: String?
    ) : ResponseEntity<Any> {

        val id: Long?
        try
        {
            id = pathId!!.toLong()
        }
        catch (e: Exception)
        {
            return ResponseEntity.status(400).build()
        }
        if (!teamCrud.existsById(id))
        {
            return ResponseEntity.status(404).build()
        }
        teamCrud.deleteById(id)
        return ResponseEntity.status(204).build()
    }

    @ApiOperation("Get a single team specified by id")
    @GetMapping(path = ["/{id}"])
    fun getTeam(
            @ApiParam(Constants.TEAM_ID_PARAM)
            @PathVariable("id")
            pathId: String?) : ResponseEntity<TeamDto>
    {
        val id: Long
        try
        {
            id = pathId!!.toLong()
        }
        catch (e: Exception)
        {
            return ResponseEntity.status(404).build()
        }
        val dto = teamCrud.findById(id).orElse(null) ?: return ResponseEntity.status(404).build()
        return ResponseEntity.ok(TeamConverter.transform(dto))
    }

    @ApiOperation("Update existing team")
    @PutMapping(path = ["/{id}"], consumes = [Constants.TEAM_JSON, Constants.BASE_JSON])
    fun update(
            @ApiParam(Constants.TEAM_ID_PARAM)
            @PathVariable("id")
            pathId: String?,
            @ApiParam("The team that will replace the old one. Cannot change ID")
            @RequestBody
            dto: TeamDto) : ResponseEntity<Any>
    {
        val dtoId : Long
        try
        {
            dtoId = getTeamId(dto)!!.toLong()
        }
        catch (e: Exception)
        {
            return ResponseEntity.status(404).build()
        }
        if (getTeamId(dto) != pathId)
        {
            // Not allowed to change the id of the resource (because set by the DB).
            // In this case, 409 (Conflict) sounds more appropriate than the generic 400
            return ResponseEntity.status(409).build()
        }
        if (!teamCrud.existsById(dtoId))
        {
            return ResponseEntity.status(404).build()
        }
        if (dto.name == null || dto.city == null || dto.country == null)
        {
            return ResponseEntity.status(400).build()
        }
        try
        {
            teamCrud.update(dtoId, dto.name!!, dto.city!!, dto.country!!)
        }
        catch (e: Exception)
        {
            if(Throwables.getRootCause(e) is ConstraintViolationException)
            {
                return ResponseEntity.status(400).build()
            }
            throw e
        }
        return ResponseEntity.status(204).build()
    }

    private fun getTeamId(dto: TeamDto): String? {

        return if (dto.teamId != null) {
            dto.teamId
        } else {
            dto.id
        }
    }

    private fun increaseOrDeacreaseRatio(win: Boolean, team: TeamEntity, oponent: String) : Int
    {
        if (win)
        {

        }
        return 0
    }
}






















