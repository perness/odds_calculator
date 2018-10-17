package com.spring.bet_calculator

class Constants
{
    companion object
    {
        const val TEAM_ID_PARAM = "The numeric teamId of the team"
        const val OPPONENT_ID_PARAM = "The numeric opponentId of the opponent"
        const val BASE_JSON = "application/json;charset=UTF-8"
        const val TEAM_JSON = "application/vnd.bet_calculator.teams+json;charset=UTF-8;version=2"
        const val OPPONENT_JSON = "application/vnd.bet_calculator.opponents+json;charset=UTF-8;version=2"
        const val JSON_MERGE_PATCH = "application/merge-patch+json"
    }
}