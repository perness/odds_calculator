package com.spring.bet_calculator.repositories

import com.spring.bet_calculator.models.TeamEntity
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager

@Repository
interface TeamRepository : CrudRepository<TeamEntity, Long>, TeamRepositoryCustom
{
    fun findAllByCountry(country: String) : Iterable<TeamEntity>

    fun findAllByCity(city: String) : Iterable<TeamEntity>

    fun findAllByCountryAndCity(country: String, city: String) : Iterable<TeamEntity>

    fun findByName(name: String) : TeamEntity
}
//--------------------------------------------------------
@Transactional
interface TeamRepositoryCustom
{
    fun createTeam(name: String, city: String, country: String): Long

    fun updateCity(teamId: Long, city: String): Boolean

    fun update(
            teamId: Long,
            name: String,
            city: String,
            country: String
    ) : Boolean
}
//--------------------------------------------------------
@Repository
@Transactional
class TeamRepositoryImpl : TeamRepositoryCustom
{
    @Autowired
    private lateinit var em: EntityManager

    override fun createTeam(name: String, city: String, country: String): Long
    {
        val entity = TeamEntity(name, city, country)
        em.persist(entity)

        return entity.id!!
    }

    override fun updateCity(teamId: Long, city: String): Boolean
    {
        val team = em.find(TeamEntity::class.java, teamId) ?: return false
        team.city = city

        return true
    }

    override fun update(teamId: Long, name: String, city: String, country: String): Boolean
    {
        val team = em.find(TeamEntity::class.java, teamId) ?: return false
        team.name = name
        team.city = city
        team.country = country
        return true
    }
}