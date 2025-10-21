package com.ufund.api.ufundapi;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.ufund.api.ufundapi.Controller.CupboardController;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class CupboardApiApplicationTests {
	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private CupboardController cupboardController;

	// NOTE: You have to manually add/delete the data your unit tests create/destroy after they run,
	// I'm not sure how to go about resetting each database file after each test
	void setUp() {
		// eventually, should setup and seed the database with data to reset it after each unit test
	}

	@Test
	void contextLoads() {
		assertTrue(cupboardController != null);
	}

	@Test
	void testGetCupboard() throws Exception {
		ResponseEntity<String> expected = this.restTemplate.getForEntity("http://localhost:" + 8080 + "/cupboard/needs",String.class);
		assertTrue((expected.getBody()).contains("Markers") == true);
		assertTrue(expected.getStatusCode() == HttpStatus.OK);
	}

	@Test
	void testRemoveNeedsFromCupboard() throws Exception {
		HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        String requestBody = "{\"id\":1,\"name\":\"Markers\",\"quantity\":100,\"fundingAmount\":50.0}"; 
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

		// Delete item from cupboard
		ResponseEntity<String> result = this.restTemplate.exchange("http://localhost:8080/cupboard/need", HttpMethod.DELETE, requestEntity, String.class);
		
		// Get inventory contents
		String inventory = this.restTemplate.getForObject("http://localhost:" + 8080 + "/inventory",
				String.class);

		// Get cupboard contents
		String cupboard = this.restTemplate.getForObject("http://localhost:" + 8080 + "/cupboard/needs",
				String.class);
		
		assertTrue(inventory.contains("Markers"));
		assertTrue(!cupboard.contains("Markers"));
		assertTrue(result.getStatusCode() == HttpStatus.OK);
	}

	@Test
	void testAddNeedToCupboard() throws Exception {
		HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        String requestBody = "{\"id\":2,\"name\":\"Notepads\",\"quantity\":50,\"fundingAmount\":20.0}"; 
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

		// Add item to cupboard
		ResponseEntity<String> re = this.restTemplate.exchange("http://localhost:8080/cupboard/need", HttpMethod.POST, requestEntity, String.class);

		// Get cupboard contents
		String cupboard = this.restTemplate.getForObject("http://localhost:" + 8080 + "/cupboard/needs",
				String.class);
		
		assertTrue(cupboard.contains("Notepads"));
		assertTrue(re.getStatusCode() == HttpStatus.CREATED);
	}
}
