package com.ufund.api.ufundapi;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.jupiter.api.BeforeEach;
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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.ufund.api.ufundapi.Controller.CupboardController;
import com.ufund.api.ufundapi.Model.Need;

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

	

	private String getBaseURL(){
		return "http://localhost:" + port;
	}

	@Test
	void contextLoads() {
		assertTrue(cupboardController != null);
	}

	// @Test
	// void testGetCupboard() throws Exception {
	// 	ResponseEntity<String> expected = this.restTemplate.getForEntity("http://localhost:" + 8080 + "/cupboard/needs",String.class);
	// 	assertTrue((expected.getBody()).contains("Markers") == true);
	// 	assertTrue(expected.getStatusCode() == HttpStatus.OK);
	// }
	@Test
	void testGetCupboard() throws Exception {
		String url = getBaseURL() + "/cupboard/needs";
			ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
			assertTrue(response.getBody().contains("Markers"));
			assertEquals(HttpStatus.OK, response.getStatusCode());
	}

	// @Test
	// void testRemoveNeedsFromCupboard() throws Exception {
	// 	HttpHeaders headers = new HttpHeaders();
    //     headers.set("Content-Type", "application/json");

    //     String requestBody = "{\"id\":1,\"name\":\"Markers\",\"quantity\":100,\"fundingAmount\":50.0}"; 
    //     HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

	// 	// Delete item from cupboard
	// 	ResponseEntity<String> result = this.restTemplate.exchange("http://localhost:8080/cupboard/need", HttpMethod.DELETE, requestEntity, String.class);
		
	// 	// Get inventory contents
	// 	String inventory = this.restTemplate.getForObject("http://localhost:" + 8080 + "/inventory",
	// 			String.class);

	// 	// Get cupboard contents
	// 	String cupboard = this.restTemplate.getForObject("http://localhost:" + 8080 + "/cupboard/needs",
	// 			String.class);
		
	// 	assertTrue(inventory.contains("Markers"));
	// 	assertTrue(!cupboard.contains("Markers"));
	// 	assertTrue(result.getStatusCode() == HttpStatus.OK);
	// }

	@Test
	void testRemoveNeedsFromCupboard() throws Exception{
		String uniqueName = "MarkerTest" + System.currentTimeMillis();
		Need need = new Need(null,uniqueName,100,50.0);
		ResponseEntity<Need> addedResponse = restTemplate.postForEntity(getBaseURL() + 
		"/cupboard/need", need, Need.class);
		
		assertEquals(HttpStatus.CREATED, addedResponse.getStatusCode() );
		assertNotNull(addedResponse.getBody());


		Need addedNeed = addedResponse.getBody();
		assertNotNull(addedNeed);
		Long assignedId = addedNeed.getId();
		
		
		String url = getBaseURL() + "/cupboard/need";
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		
		String requestBody = String.format("{\"id\":%d,\"name\":\"%s\",\"quantity\":100,\"fundingAmount\":50.0}", 
		assignedId, uniqueName);
		HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

		ResponseEntity<String> deleteResponse = restTemplate.exchange(url, HttpMethod.DELETE,requestEntity,String.class);
		assertEquals(HttpStatus.NO_CONTENT, deleteResponse.getStatusCode());

		String inventory = restTemplate.getForObject(getBaseURL() + "/inventory", String.class);
		String cupboard = restTemplate.getForObject(getBaseURL() + "/cupboard/needs", String.class);

		
		assertTrue(inventory.contains(uniqueName));
		assertFalse(cupboard.contains(uniqueName));
		

	}
	






	// @Test
	// void testAddNeedToCupboard() throws Exception {
	// 	HttpHeaders headers = new HttpHeaders();
    //     headers.set("Content-Type", "application/json");

    //     String requestBody = "{\"id\":2,\"name\":\"Notepads\",\"quantity\":50,\"fundingAmount\":20.0}"; 
    //     HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

	// 	// Add item to cupboard
	// 	ResponseEntity<String> re = this.restTemplate.exchange("http://localhost:8080/cupboard/need", HttpMethod.POST, requestEntity, String.class);

	// 	// Get cupboard contents
	// 	String cupboard = this.restTemplate.getForObject("http://localhost:" + 8080 + "/cupboard/needs",
	// 			String.class);
		
	// 	assertTrue(cupboard.contains("Notepads"));
	// 	assertTrue(re.getStatusCode() == HttpStatus.CREATED);
	// }

	// @Test
	// void testAddNeedToCupboard() throws Exception{
	// 	String url = getBaseURL() + "/cupboard/needs";
	// 	HttpHeaders headers = new HttpHeaders();
	// 	headers.setContentType(MediaType.APPLICATION_JSON);
	// 	String requestBody = "{\"id\":2,\"name\":\"Notepads\",\"quantity\":50,\"fundingAmount\":20.0}"; 
	// 	HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

	// 	ResponseEntity<String> postResponse = restTemplate.exchange(url, HttpMethod.POST,requestEntity,String.class);
	// 	String cupboard = restTemplate.getForObject(getBaseURL() + "/cupboard/needs", String.class);

	// 	assertTrue(cupboard.contains("Notepads"));
	// 	assertEquals(HttpStatus.CREATED, postResponse.getStatusCode());

	// }
}
