package com.roytuts.crud.restassured.example;

import org.junit.jupiter.api.Test;

import io.restassured.RestAssured;

public class TestDeleteExample {

	@Test
	public void testDeleteUser() {
		RestAssured.given().when().delete("https://reqres.in/api/users/2").then().assertThat().statusCode(204);
	}

}
