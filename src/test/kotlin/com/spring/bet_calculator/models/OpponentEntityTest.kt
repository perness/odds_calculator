package com.spring.bet_calculator.models

import com.spring.bet_calculator.repositories.OpponentRepository
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.BeforeClass
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
class OpponentEntityTest
{
    @Autowired
    private lateinit var crud: OpponentRepository

    @Before
    fun cleanDatabase()
    {
        crud.deleteAll()
    }

    @Test
    fun testInit()
    {
        Assert.assertNotNull(crud)
    }

    @Test
    fun testCreate()
    {
        val name = "AS Roma"
        val ratio = 5
        val entity = crud.save(OpponentEntity(name, ratio))

        assertEquals(1, crud.count())
        assertEquals(name, entity.name)
        assertEquals(5, entity.ratio)
    }

    @Test
    fun testUpdateRatio()
    {
        val name = "AS Roma"
        val ratio = 5
        val entity = crud.save(OpponentEntity(name, ratio))

        assertEquals(5, entity.ratio)
        assertTrue(crud.updateRatio(entity.id!!, 1))

        val newEntity = crud.findById(entity.id!!)
        assertEquals(1, newEntity.get().ratio)
        assertEquals(entity.name, newEntity.get().name)
    }
}




























