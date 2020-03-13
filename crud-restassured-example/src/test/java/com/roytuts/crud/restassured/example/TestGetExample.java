package com.roytuts.crud.restassured.example;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import com.github.fge.jsonschema.main.JsonSchemaFactory;

import io.restassured.RestAssured;
import io.restassured.config.JsonConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.http.ContentType;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.module.jsv.JsonSchemaValidatorSettings;
import io.restassured.path.json.JsonPath;
import io.restassured.path.json.config.JsonPathConfig.NumberReturnType;

public class TestGetExample {

	@Test
	public void testGetVerifySizeOfElements() {
		List<Object> list = RestAssured.given().when().get("https://jsonplaceholder.typicode.com/posts").then()
				.assertThat().statusCode(200).and().contentType(ContentType.JSON).extract().body().jsonPath()
				.getList("");

		assertEquals(list.size(), 100);

		String response = RestAssured.given().when().get("https://jsonplaceholder.typicode.com/posts").then()
				.assertThat().statusCode(200).and().contentType(ContentType.JSON).extract().response().asString();

		list = JsonPath.from(response).getList("");

		assertEquals(list.size(), 100);
	}

	@Test
	public void testGetVerifyUserIdEqualTo() {
		RestAssured.given().when().get("https://jsonplaceholder.typicode.com/posts").then().assertThat().statusCode(200)
				.and().contentType(ContentType.JSON).body("userId[99]", Matchers.equalTo(10));
	}

	@Test
	public void testGetVerifyUserIdHasItems() {
		RestAssured.given().when().get("https://jsonplaceholder.typicode.com/posts").then().assertThat().statusCode(200)
				.and().contentType(ContentType.JSON).body("userId", Matchers.hasItems(1, 2, 3, 4, 10));
	}

	@Test
	public void testGetIntegerAsBigInteger() {
		JsonConfig jsonConfig = new JsonConfig(NumberReturnType.BIG_INTEGER);
		RestAssured.given().config(RestAssuredConfig.config().jsonConfig(jsonConfig)).when()
				.get("https://jsonplaceholder.typicode.com/posts").then().assertThat().statusCode(200).and()
				.contentType(ContentType.JSON).body("id[99]", Matchers.is(Matchers.equalTo(new BigInteger("100"))));
	}

	@Test
	public void testGetSchemaValidation() {
		RestAssured.given().when().get("https://jsonplaceholder.typicode.com/posts").then().assertThat().statusCode(200)
				.and().contentType(ContentType.JSON)
				.body(JsonSchemaValidator.matchesJsonSchemaInClasspath("post.json"));
	}

	@Test
	public void testGetSchemaValidationFalse() {
		JsonSchemaValidatorSettings settings = JsonSchemaValidatorSettings.settings().with()
				.jsonSchemaFactory(JsonSchemaFactory.newBuilder().freeze()).checkedValidation(false);

		RestAssured.given().when().get("https://jsonplaceholder.typicode.com/posts").then().assertThat().statusCode(200)
				.and().contentType(ContentType.JSON)
				.body(JsonSchemaValidator.matchesJsonSchemaInClasspath("post.json").using(settings));
	}

	@Test
	public void testGetVerifyResourceBodyHasItems() {
		RestAssured.given().when().get("https://reqres.in/api/unknown").then().assertThat().statusCode(200).and()
				.contentType(ContentType.JSON)
				.body("data.findAll {d -> d.year < 2002}.name", Matchers.hasItems("cerulean", "fuchsia rose"));
	}

	@Test
	public void testGetVerifyResourceListHasItems() {
		String response = RestAssured.given().when().get("https://reqres.in/api/unknown").then().assertThat()
				.statusCode(200).and().contentType(ContentType.JSON).extract().response().asString();

		List<String> names = JsonPath.from(response).getList("data.findAll {d -> d.year < 2002}.name");

		assertEquals(names.size(), 2);
		assertTrue(names.equals(Arrays.asList("cerulean", "fuchsia rose")));
	}

	@Test
	public void testGetVerifyPathParam() {
		String resp = RestAssured.given().pathParam("id", 1).when()
				.get("https://jsonplaceholder.typicode.com/posts/{id}")//
				// get("https://jsonplaceholder.typicode.com/posts/1") //
				// get("https://jsonplaceholder.typicode.com/posts/{id}", 1)//
				.then().assertThat().statusCode(200).and().contentType(ContentType.JSON).extract().response()
				.asString();

		int id = JsonPath.from(resp).getInt("id");
		int userId = JsonPath.from(resp).getInt("userId");

		assertEquals(id, 1);
		assertEquals(userId, 1);
	}

	@Test
	public void testGetVerifyQueryParam() {
		String resp = RestAssured.given().queryParam("userId", 1)/* param("userId", 1) */.when()
				.get("https://jsonplaceholder.typicode.com/posts").then().assertThat().statusCode(200).and()
				.contentType(ContentType.JSON).extract().response().asString();

		List<Object> list = JsonPath.from(resp).getList("");

		assertEquals(list.size(), 10);
	}

}
