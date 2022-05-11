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
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.persistence.OwnerRepository;
import org.springframework.samples.petclinic.persistence.PetRepository;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PetTests {

	@Autowired
	PetRepository petRepo;

	@Autowired
	OwnerRepository ownerRepo;

	@LocalServerPort
	int port;

	Owner owner;

	String ownerLastName = "LastName";

	int ownerID;

	int numPets = 0;

	/*
	 * Helper method for driver code. Gather data on execution speed for CREATE pet.
	 */
	public void petCreateTest(int numRequests, FileWriter file) throws IOException {
		String petCreateURL = "http://localhost:" + port + "/" + "owners/" + ownerID + "/pets/new";

		TestRestTemplate testRestTemplate = new TestRestTemplate();
		for (int i = 0; i < numRequests; i++) {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
			String body = "id=&name=PetName" + numPets + "&birthDate=2000-01-01&type=bird";

			HttpEntity<String> petCreateRequest = new HttpEntity<>(body, headers);

			Long startTime = System.nanoTime();
			testRestTemplate.postForEntity(petCreateURL, petCreateRequest, String.class);
			Long endTime = System.nanoTime();

			long duration = endTime - startTime;

			file.write(numPets + "," + duration + "\n");

			numPets++;
		}

	}

	/*
	 * Helper method for driver code. Gather data on execution speed for EDIT pet.
	 */
	public void petEditTest(int numRequests, FileWriter file) throws IOException {
		if (numPets < numRequests)
			return;

		String petEditURL = "http://localhost:" + port + "/owners/" + ownerID + "/pets/";

		TestRestTemplate testRestTemplate = new TestRestTemplate();
		for (int i = numPets - numRequests; i < numPets; i++) {
			Owner owner = ownerRepo.findByLastName(ownerLastName).iterator().next();
			int id = owner.getPet("PetName" + i).getId();

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
			String body = "name=Name" + numPets + "&birthDate=2000-01-01&type=bird";

			HttpEntity<String> petEditRequest = new HttpEntity<>(body, headers);

			Long startTime = System.nanoTime();
			testRestTemplate.postForEntity(petEditURL + id + "/edit", petEditRequest, String.class);
			Long endTime = System.nanoTime();

			long duration = endTime - startTime;
			file.write(numPets + "," + duration + "\n");
		}

	}

	/*
	 * Helper method for driver code. Populate db with pets for each test run.
	 */
	public void populatePets(int numPetsToAdd) {
		for (; numPets < numPetsToAdd; numPets++) {
			Pet pet = new Pet();
			pet.setName("PetName" + numPets);
			pet.setBirthDate(LocalDate.now());
			pet.setOwner(owner);
			pet.setType(petRepo.findPetTypes().get(0));

			owner.addPet(pet);

			petRepo.save(pet);
			ownerRepo.save(owner);
		}
	}

	/*
	 * Helper method for driver code. Init same owner to be used for each test.
	 */
	public void initOwnerDetails() {
		owner = new Owner();
		owner.setFirstName("FirstName");
		owner.setLastName(ownerLastName);
		owner.setAddress("Address");
		owner.setCity("City");
		owner.setTelephone("9999999999");

		ownerRepo.save(owner);
		ownerID = ownerRepo.findByLastName(ownerLastName).iterator().next().getId();
	}

	/*
	 * Driver code for Pet Performance Test. NOTE ====> Add @Test annotation to run as
	 * unit test. It is removed so that it doesn't slow down the build so much.
	 */
	public void petPerformanceTestDriver() throws IOException {
		int numRequests = 500;
		int[] thresholds = { 0, 1000, 2000, 5000, 7000, 10000 };

		FileWriter petCreateResultsCSV = new FileWriter("petCreateResults.csv");
		FileWriter petEditResultsCSV = new FileWriter("petEditResults.csv");

		initOwnerDetails();
		for (int curThreshold : thresholds) {
			populatePets(curThreshold);
			petCreateTest(numRequests, petCreateResultsCSV);
			petEditTest(numRequests, petEditResultsCSV);
		}

		petCreateResultsCSV.close();
		petEditResultsCSV.close();
	}

}
