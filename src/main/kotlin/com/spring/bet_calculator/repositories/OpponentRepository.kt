package com.spring.bet_calculator.repositories

import com.spring.bet_calculator.models.OpponentEntity
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager

@Repository
interface OpponentRepository : CrudRepository<OpponentEntity, Long>, OpponentRepositoryCustom
{

}
//--------------------------------------------------------
@Transactional
interface OpponentRepositoryCustom
{
    fun updateRatio(opponentId: Long, ratio: Int) : Boolean
}
//--------------------------------------------------------
@Repository
@Transactional
class OpponentRepositoryImpl : OpponentRepositoryCustom
{
    @Autowired
    private lateinit var em: EntityManager

    override fun updateRatio(opponentId: Long, ratio: Int) : Boolean
    {
        val entity = em.find(OpponentEntity::class.java, opponentId)
        entity.ratio = ratio

        return true
    }
}