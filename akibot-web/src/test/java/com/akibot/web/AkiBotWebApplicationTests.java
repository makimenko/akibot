package com.akibot.web;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("fake")
public class AkiBotWebApplicationTests {

	private TestRestTemplate restTemplate = new TestRestTemplate();
	private String BASE_URL = "http://localhost:8080/";

	@Test
	public void test() {
		SpringApplication.run(AkiBotWebApplication.class);
		ResponseEntity<String> response = restTemplate.getForEntity(BASE_URL + "scene/index.html", String.class);
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

}
