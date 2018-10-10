package com.spring.bet_calculator.dto

import com.spring.bet_calculator.models.OpponentEntity

class OpponentConverter
{
    companion object
    {
        fun transform(entity: OpponentEntity) : OpponentDto
        {
            return OpponentDto(
                    name = entity.name,
                    ratio = entity.ratio,
                    opponentId = entity.id?.toString())
                    .apply { id = entity.id?.toString() }
        }

        fun transform(entity: Iterable<OpponentEntity>) : List<OpponentDto>
        {
            return entity.map { transform(it) }
        }
    }
}