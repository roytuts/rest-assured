package com.roytuts.crud.restassured.example;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;

public class TestPostExample {

	@Test
	public void testCreateNewUserJsonBody() {
		String resp = RestAssured.given().request().accept(ContentType.JSON)
				.body("{\"name\": \"morpheus\", \"job\": \"leader\"}").when().post("https://reqres.in/api/users").then()
				.assertThat().statusCode(201).and().contentType(ContentType.JSON).extract().response().asString();

		int id = JsonPath.from(resp).getInt("id");

		assertTrue(id > 0);
	}

	@Test
	public void testCreateNewUserObjectBody() {
		User user = new User();
		user.setName("morpheus");
		user.setJob("leader");

		String resp = RestAssured.given().request().accept(ContentType.JSON).body(user).when()
				.post("https://reqres.in/api/users").then().assertThat().statusCode(201).and()
				.contentType(ContentType.JSON).extract().response().asString();

		int id = JsonPath.from(resp).getInt("id");

		assertTrue(id > 0);
	}

}
