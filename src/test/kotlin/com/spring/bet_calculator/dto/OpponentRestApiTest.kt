package com.spring.bet_calculator.dto

import com.spring.bet_calculator.Constants
import io.restassured.RestAssured.given
import org.hamcrest.CoreMatchers.equalTo
import org.junit.Assert.assertEquals
import org.junit.Test

class OpponentRestApiTest : ORTestBase()
{
    @Test
    fun testCreate()
    {
        val name = "AS Roma"
        val ratio = 0

        createOpponent(name, ratio)

        given().accept(Constants.OPPONENT_JSON)
                .get()
                .then()
                .statusCode(200)
                .body("size()", equalTo(1))
    }

    @Test
    fun testPatchMergeJsonUpdateRatio()
    {
        val name = "AS Roma"
        val ratio = 0

        val id = createOpponent(name, ratio)

        val newRatio = 5

        given().contentType(Constants.JSON_MERGE_PATCH)
                .body("{\"ratio\":$newRatio}")
                .patch(id)
                .then()
                .statusCode(204)

        val updatedDto = given().accept(Constants.OPPONENT_JSON)
                .get(id)
                .then()
                .statusCode(200)
                .extract()
                .`as`(OpponentDto::class.java)

        assertEquals(newRatio, updatedDto.ratio)
        assertEquals(name, updatedDto.name)
    }

    @Test
    fun testPatchMergeJsonUpdateName()
    {
        val name = "AS Roma"
        val ratio = 0
        val newName = "Napoli"

        val id = createOpponent(name, ratio)

        given().contentType(Constants.JSON_MERGE_PATCH)
                .body("{\"name\":\"$newName\"}")
                .patch(id)
                .then()
                .statusCode(204)

        val updatedDto = given().accept(Constants.OPPONENT_JSON)
                .get(id)
                .then()
                .statusCode(200)
                .extract()
                .`as`(OpponentDto::class.java)

        assertEquals(ratio, updatedDto.ratio)
        assertEquals(newName, updatedDto.name)
    }

    @Test
    fun testJsonMergePatchUpdateAll()
    {
        val name = "AS Roma"
        val ratio = 0
        val newName = "Napoli"
        val newRatio = 5

        val id = createOpponent(name, ratio)

        given().contentType(Constants.JSON_MERGE_PATCH)
                .body("{\"name\":\"$newName\", \"ratio\":$newRatio}")
                .patch(id)
                .then()
                .statusCode(204)

        val updatedDto = given().accept(Constants.OPPONENT_JSON)
                .get(id)
                .then()
                .statusCode(200)
                .extract()
                .`as`(OpponentDto::class.java)

        assertEquals(newRatio, updatedDto.ratio)
        assertEquals(newName, updatedDto.name)
    }

    fun createOpponent(name: String, ratio: Int) : String
    {
        val dto = OpponentDto(name, ratio)
        return given().contentType(Constants.OPPONENT_JSON)
                .body(dto)
                .post()
                .then()
                .statusCode(201)
                .extract().asString()
    }
}

















