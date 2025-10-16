package com.ufund.api.ufundapi;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

import com.ufund.api.ufundapi.Controller.CupboardController;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class CupboardApiApplicationTests {
	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private CupboardController cupboardController;

	@Test
	void contextLoads() {
		assertThat(cupboardController).isNotNull();
	}

	@Test
	void testGetCupboard() throws Exception {
		String expected = this.restTemplate.getForObject("http://localhost:" + 8080 + "/cupboard/needs",
				String.class);
		assertThat(expected.contains("Markers"));
	}

	@Test
	void testRemoveNeedsFromCupboard() throws Exception {
		HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        String requestBody = "'{\"id\":1\",\"name\":\"Markers\",\"quantity\":100,\"fundingAmount\":50.0}'"; 
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

		// Delete item from cupboard
		this.restTemplate.exchange("http://localhost:8080/cupboard/need", HttpMethod.DELETE, requestEntity, String.class);
		
		// Get inventory contents
		String inventory = this.restTemplate.getForObject("http://localhost:" + 8080 + "/inventory",
				String.class);

		// Get cupboard contents
		String cupboard = this.restTemplate.getForObject("http://localhost:" + 8080 + "/cupboard/needs",
				String.class);
		
		assertThat(inventory.contains("Markers"));
		assertThat(!cupboard.contains("Markers"));
	}

	@Test
	void testAddNeedToCupboard() throws Exception {
		HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        String requestBody = "'{\"id\":2\",\"name\":\"Notepads\",\"quantity\":50,\"fundingAmount\":20.0}'"; 
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

		// Add item to cupboard
		this.restTemplate.exchange("http://localhost:8080/cupboard/need", HttpMethod.POST, requestEntity, String.class);

		// Get cupboard contents
		String cupboard = this.restTemplate.getForObject("http://localhost:" + 8080 + "/cupboard/needs",
				String.class);
		
		// assertThat(inventory.contains("Markers"));
		assertThat(cupboard.contains("Notepads"));
	}
}
