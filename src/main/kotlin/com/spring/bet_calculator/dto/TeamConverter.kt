package com.spring.bet_calculator.dto

import com.spring.bet_calculator.models.TeamEntity

class TeamConverter
{
    companion object
    {
        fun transform(entity: TeamEntity) : TeamDto
        {
            return TeamDto(
                    name = entity.name,
                    city = entity.city,
                    country = entity.country,
                    teams = entity.teams,
                    teamId = entity.id?.toString()
            ).apply { id = entity.id?.toString() }
        }

        fun transform(entities: Iterable<TeamEntity>) : List<TeamDto>
        {
            return entities.map { transform(it) }
        }
    }
}