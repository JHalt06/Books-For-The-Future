package com.ufund.api.ufundapi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
import org.springframework.http.ResponseEntity;

import com.ufund.api.ufundapi.Controller.CupboardController;
import com.ufund.api.ufundapi.DAO.CupboardDAO;
import com.ufund.api.ufundapi.DAO.InventoryDAO;
import com.ufund.api.ufundapi.Model.Need;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class CupboardApiApplicationTests {
	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private CupboardController cupboardController;

	@Autowired
	private InventoryDAO inventoryDAO;

	@Autowired
	private CupboardDAO cupboardDAO;

	@BeforeEach
	public void setUp() throws IOException {
		List<Need> inventoryNeeds = new ArrayList<>(inventoryDAO.getInventory().getInventory());
		for (Need need : inventoryNeeds) {
			inventoryDAO.deleteNeed(need.getId());
		}
		List<Need> cupboardNeeds = new ArrayList<>(cupboardDAO.getCupboard().getCupboard());
		for (Need need : cupboardNeeds) {
			cupboardDAO.deleteNeed(need.getId());
		}

		// add predictable data for tests
		inventoryDAO.addNeed(new Need(1L, "Markers", 100, 50.0));
		cupboardDAO.addNeed(new Need(2L, "Notepads", 50, 20.0));
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
			assertEquals(HttpStatus.OK, response.getStatusCode());
			assertTrue(response.getBody().contains("Notepads"));
			assertFalse(response.getBody().contains("Markers"));
	}

	@Test
	void testRemoveNeedsFromCupboard() throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/json");

		// removing "Notepads" (ID 2) which is in the cupboard from the setup
		String requestBody = "{\"id\":2,\"name\":\"Notepads\",\"quantity\":50,\"fundingAmount\":20.0}";
		HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

		// DELETE request to remove the item from the cupboard
		ResponseEntity<String> result = this.restTemplate.exchange(
				getBaseURL() + "/cupboard/need",
				HttpMethod.DELETE,
				requestEntity,
				String.class
		);

		// check if request was successful
		assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());

		// verify state
		String inventory = this.restTemplate.getForObject(getBaseURL() + "/inventory", String.class);
		String cupboard = this.restTemplate.getForObject(getBaseURL() + "/cupboard/needs", String.class);

		assertTrue(inventory.contains("Notepads"));
		assertFalse(cupboard.contains("Notepads"));
	}

	@Test
	void testAddNeedToCupboard() throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/json");

		// create a new need to add to the cupboard
		String requestBody = "{\"name\":\"Pencils\",\"quantity\":150,\"fundingAmount\":25.0}";
		HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

		// POST request to add the new item
		ResponseEntity<String> response = this.restTemplate.exchange(
				getBaseURL() + "/cupboard/need",
				HttpMethod.POST,
				requestEntity,
				String.class
		);

		// verify the created successfully
		assertEquals(HttpStatus.CREATED, response.getStatusCode());

		// verift state
		String cupboard = this.restTemplate.getForObject(getBaseURL() + "/cupboard/needs", String.class);

		assertTrue(cupboard.contains("Pencils"));
	}
}

	// private Long extractIdFromJSON(String json){
	// 	Pattern pattern = Pattern.compile("\"id\"\\s*:\\s*(\\d+)");
	// 	Matcher matcher =pattern.matcher(json);
	// 	if(matcher.find()){
	// 		return Long.parseLong(matcher.group(1));
	// 	}
	// 	return null;
	// }





