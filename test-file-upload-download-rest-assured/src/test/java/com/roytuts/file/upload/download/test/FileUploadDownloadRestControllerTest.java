package com.roytuts.file.upload.download.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;

import org.junit.jupiter.api.Test;

import io.restassured.RestAssured;

public class FileUploadDownloadRestControllerTest {

	@Test
	public void testUploadFile() {
		String resp = RestAssured.given().multiPart("file", new File("src/test/resources/info.xlsx")).when()
				.post("http://localhost:8080/upload").then().assertThat().statusCode(200).and().extract().body().asString();

		assertEquals(resp, "info.xlsx");
	}

	@Test
	public void testDownloadFile() {
		String contentType = RestAssured.given().when().get("http://localhost:8080/download").then().assertThat()
				.statusCode(200).and().extract().contentType();

		assertEquals(contentType, "text/json");
	}
}
