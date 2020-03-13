package com.roytuts.crud.restassured.example;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;

public class TestPutExample {

	@Test
	public void testUpdateUser() {
		String resp = RestAssured.given().request().accept(ContentType.JSON)
				.body("{\"name\": \"morpheus\", \"job\": \"zion resident\"}").when()
				.put("https://reqres.in/api/users/2").then().assertThat().statusCode(200).and()
				.contentType(ContentType.JSON).extract().response().asString();

		System.out.println(resp);

		String updatedAt = JsonPath.from(resp).getString("updatedAt");

		assertTrue(updatedAt != null);
	}

	@Test
	public void testUpdateUserPathParam() {
		String resp = RestAssured.given().pathParam("id", 2).request().accept(ContentType.JSON)
				.body("{\"name\": \"morpheus\", \"job\": \"zion resident\"}").when()
				.put("https://reqres.in/api/users/{id}").then().assertThat().statusCode(200).and()
				.contentType(ContentType.JSON).extract().response().asString();

		String updatedAt = JsonPath.from(resp).getString("updatedAt");

		assertTrue(updatedAt != null);
	}

}
