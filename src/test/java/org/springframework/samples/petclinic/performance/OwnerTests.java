package org.springframework.samples.petclinic.performance;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.persistence.OwnerRepository;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.FileWriter;
import java.io.IOException;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OwnerTests {

	@Autowired
	OwnerRepository repo;

	@LocalServerPort
	int port;

	int numOwners = 0;

	/*
	 * Helper method for driver code. Gather data on execution speed for CREATE owner.
	 */
	public void ownerCreateTest(int numRequests, FileWriter file) throws IOException {
		String ownerCreateURL = "http://localhost:" + port + "/" + "owners/new";

		TestRestTemplate testRestTemplate = new TestRestTemplate();
		for (int i = 0; i < numRequests; i++) {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
			String body = "firstName=FirstName" + numOwners + "&lastName=LastName" + numOwners
					+ "&address=Address&city=City&telephone=9999999999";

			HttpEntity<String> ownerCreateRequest = new HttpEntity<>(body, headers);

			Long startTime = System.nanoTime();
			testRestTemplate.postForEntity(ownerCreateURL, ownerCreateRequest, String.class);
			Long endTime = System.nanoTime();

			long duration = endTime - startTime;

			file.write(numOwners + "," + duration + "\n");

			numOwners++;
		}

	}

	/*
	 * Helper method for driver code. Gather data on execution speed for EDIT owner.
	 */
	public void ownerEditTest(int numRequests, FileWriter file) throws IOException {
		if (numOwners < numRequests)
			return;

		String editOwnerURL = "http://localhost:" + port + "/owners/";

		TestRestTemplate testRestTemplate = new TestRestTemplate();
		for (int i = numOwners - numRequests; i < numOwners; i++) {
			int id = repo.findByLastName("LastName" + i).iterator().next().getId();

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
			String body = "firstName=FirstName" + numOwners + "&lastName=LastName" + numOwners
					+ "&address=Address&city=City&telephone=9999999999";

			HttpEntity<String> ownerUpdateRequest = new HttpEntity<>(body, headers);

			Long startTime = System.nanoTime();
			testRestTemplate.postForEntity(editOwnerURL + id + "/edit", ownerUpdateRequest, String.class);
			Long endTime = System.nanoTime();

			long duration = endTime - startTime;

			file.write(numOwners + "," + duration + "\n");
		}

	}

	/*
	 * Helper method for driver code. Populate db with owners for each test run.
	 */
	public void populateOwners(int numOwnersToAdd) {
		for (; numOwners < numOwnersToAdd; numOwners++) {
			Owner owner = new Owner();
			owner.setFirstName("FirstName" + numOwners);
			owner.setLastName("LastName" + numOwners);
			owner.setAddress("Address" + numOwners);
			owner.setCity("City" + numOwners);
			owner.setTelephone("9999999999");

			repo.save(owner);
		}
	}

	/*
	 * Driver code for Owner Performance Test NOTE ====> Add @Test annotation to run as
	 * unit test. It is removed so that it doesn't slow down the build so much.
	 */
	public void ownerPerformanceTestDriver() throws IOException {
		int numRequests = 2000;
		int[] thresholds = { 0, 5000, 10000, 100000, 500000, 1000000, 5000000 };

		FileWriter ownerCreateResultsCSV = new FileWriter("ownerCreateResults.csv");
		FileWriter ownerEditResultsCSV = new FileWriter("ownerEditResults.csv");

		for (int curThreshold : thresholds) {
			populateOwners(curThreshold);
			ownerCreateTest(numRequests, ownerCreateResultsCSV);
			ownerEditTest(numRequests, ownerEditResultsCSV);
		}

		ownerCreateResultsCSV.close();
		ownerEditResultsCSV.close();
	}

}
