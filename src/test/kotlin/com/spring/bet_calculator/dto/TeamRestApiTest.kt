package com.spring.bet_calculator.dto

import com.spring.bet_calculator.api.TEAM_JSON
import io.restassured.RestAssured.*
import org.hamcrest.CoreMatchers.equalTo
import org.junit.Test

class TeamRestApiTest : TRTestBase()
{
    @Test
    fun testCreateAndGetWithNewFormat()
    {
        val name = "AS Roma"
        val city = "Rome"
        val country = "Italy"
        val dto = TeamDto(name, city, country, null)

        given().accept(TEAM_JSON)
                .get()
                .then()
                .statusCode(200)
                .body("size()", equalTo(0))

        val id = given().contentType(TEAM_JSON)
                .body(dto)
                .post()
                .then()
                .statusCode(201)
                .extract().asString()

        given().accept(TEAM_JSON)
                .get()
                .then()
                .statusCode(200)
                .body("size()", equalTo(1))

        given().accept(TEAM_JSON)
                .pathParam("id", id)
                .get("/{id}")
                .then()
                .statusCode(200)
                .body("teamId", equalTo(id))
                .body("name", equalTo(name))
                .body("city", equalTo(city))
                .body("country", equalTo(country))
    }

    @Test
    fun testDoubleDelete()
    {
        val name = "AS Roma"
        val city = "Rome"
        val country = "Italy"
        val dto = TeamDto(name, city, country, null)

        val id = given().contentType(TEAM_JSON)
                .body(dto)
                .post()
                .then()
                .statusCode(201)
                .extract().asString()

        delete("/$id").then().statusCode(204)
        delete("/$id").then().statusCode(404)
    }

    @Test
    fun testUpdate()
    {
        val name = "AS Roma"
        val city = "Rome"
        val country = "Italy"
        val dto = TeamDto(name, city, country, null)

        val id = given().contentType(TEAM_JSON)
                .body(dto)
                .post()
                .then()
                .statusCode(201)
                .extract().asString()

        val newName = "Napoli"
        val newCity = "Naples"

        get("/$id").then().body("name", equalTo(name))

        given().contentType(TEAM_JSON)
                .pathParam("id", id)
                .body(TeamDto(newName, newCity, country, null, id))
                .put("/{id}")
                .then()
                .statusCode(204)

        get("/$id").then().body("name", equalTo(newName))
    }

    @Test
    fun testMissingForUpdate()
    {
        given().contentType(TEAM_JSON)
                .body("{\"id\":\"-333\"}")
                .pathParam("id", "-333")
                .put("/{id}")
                .then()
                .statusCode(404)
    }

    @Test
    fun getAllByCity()
    {
        val name = "AS Roma"
        val city = "Rome"
        val country = "Italy"
        val dto = TeamDto(name, city, country)
        val name2 = "Frosinone"
        val dto2 = TeamDto(name2, city, country)

        given().contentType(TEAM_JSON)
                .body(dto)
                .post()
                .then()
                .statusCode(201)
                .extract().asString()

        given().contentType(TEAM_JSON)
                .body(dto2)
                .post()
                .then()
                .statusCode(201)
                .extract().asString()

        given().accept(TEAM_JSON)
                .queryParam("city", city)
                .get()
                .then()
                .statusCode(200)
                .body("size()", equalTo(2))

        /*given().accept(TEAM_JSON)
                .queryParam("city", city)
                .get()
                .then()
                .statusCode(200)
                .body("city", equalTo(JSONString { "Rome" }.toJSONString()))*/
    }

    @Test
    fun testGetAllByCountry()
    {
        val name = "AS Roma"
        val city = "Rome"
        val country = "Italy"
        val dto = TeamDto(name, city, country)
        val name2 = "Frosinone"
        val dto2 = TeamDto(name2, city, country)

        given().contentType(TEAM_JSON)
                .body(dto)
                .post()
                .then()
                .statusCode(201)
                .extract().asString()

        given().contentType(TEAM_JSON)
                .body(dto2)
                .post()
                .then()
                .statusCode(201)
                .extract().asString()

        given().accept(TEAM_JSON)
                .queryParam("country", country)
                .get()
                .then()
                .statusCode(200)
                .body("size()", equalTo(2))
    }
}





























