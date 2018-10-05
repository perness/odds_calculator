package com.spring.bet_calculator.models

import com.spring.bet_calculator.repositories.TeamRepository
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

@RunWith(SpringRunner::class)
@DataJpaTest
@Transactional(propagation = Propagation.NEVER)
class TeamEntityTest
{
    @Autowired
    private lateinit var crud: TeamRepository

    @Before
    fun cleanDatabase()
    {
        crud.deleteAll()
    }

    @Test
    fun testInit()
    {
        assertNotNull(crud)
    }

    @Test
    fun testCreate()
    {
        assertEquals(0, crud.count())

        val teamEntity = crud.save(TeamEntity("AS Roma", "Rome", "Italy"))

        assertEquals(1, crud.count())
        assertEquals(teamEntity, crud.findById(teamEntity.id!!).get())
    }

    @Test
    fun testDelete()
    {
        assertEquals(0, crud.count())

        val teamEntity = crud.save(TeamEntity("AS Roma", "Rome", "Italy"))

        assertTrue(crud.existsById(teamEntity.id!!))
        assertTrue(crud.findAll().any { n -> n.id == teamEntity.id })
        assertEquals(1, crud.count())

        crud.deleteById(teamEntity.id!!)

        assertFalse(crud.existsById(teamEntity.id!!))
        assertFalse(crud.findAll().any { n -> n.id == teamEntity.id })
        assertEquals(0, crud.count())
    }

    @Test
    fun testGet()
    {
        val name = "AS Roma"
        val description = "Rome"
        val country = "Italy"

        val teamEntity = crud.save(TeamEntity(name, description, country))

        assertEquals(name, teamEntity.name)
        assertEquals(description, teamEntity.city)
        assertEquals(country, teamEntity.country)
    }

    @Test
    fun testUpdate()
    {
        val name = "AS Roma"
        val description = "Rome"
        val country = "Italy"

        val teamEntity = crud.save(TeamEntity(name, description, country))
        assertEquals(description, teamEntity.city)

        val updatedDescription = "new city"

        crud.updateCity(teamEntity.id!!, updatedDescription)
        assertEquals(updatedDescription, crud.findById(teamEntity.id!!).get().city)
    }

    private fun createSomeTeams()
    {
        crud.save(TeamEntity("AS Roma", "Rome", "Italy"))
        crud.save(TeamEntity("AC Milan", "Milano", "Italy"))
        crud.save(TeamEntity("FC Inter", "Milano", "Italy"))
        crud.save(TeamEntity("Juventus", "Turin", "Italy"))
        crud.save(TeamEntity("Napoli", "Naples", "Italy"))
        crud.save(TeamEntity("Manchester United", "Manchester", "England"))
        crud.save(TeamEntity("Manchester City", "Manchester", "England"))
    }

    @Test
    fun testFindAll()
    {
        assertEquals(0, crud.findAll().count())
        createSomeTeams()
        assertEquals(7, crud.findAll().count())
    }

    @Test
    fun testGetAllByCountry()
    {
        assertEquals(0, crud.findAll().count())
        createSomeTeams()

        assertEquals(5, crud.findAllByCountry("Italy").count())
        assertEquals(2, crud.findAllByCountry("England").count())
    }

    @Test
    fun testGetAllByCity()
    {
        assertEquals(0, crud.findAll().count())
        createSomeTeams()

        assertEquals(2, crud.findAllByCity("Milano").count())
        assertEquals(2, crud.findAllByCity("Manchester").count())
    }

    @Test
    fun testGetAllByCountryAndCity()
    {
        assertEquals(0, crud.findAll().count())
        createSomeTeams()

        assertEquals(2, crud.findAllByCountryAndCity("Italy", "Milano").count())
        assertEquals(2, crud.findAllByCountryAndCity("England", "Manchester").count())
    }
}















