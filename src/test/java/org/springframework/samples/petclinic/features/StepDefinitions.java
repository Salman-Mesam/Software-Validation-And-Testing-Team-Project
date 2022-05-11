package org.springframework.samples.petclinic.features;

import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.path.xml.XmlPath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.persistence.OwnerRepository;

import java.util.Collection;
import java.util.Iterator;

import static io.restassured.RestAssured.*;
import static org.junit.jupiter.api.Assertions.*;

public class StepDefinitions {

	@Autowired
	OwnerRepository ownerRepository;

	public static Owner owner;

	public static XmlPath htmlPath;

	public static String editPetUrl, addVisitUrl;

	public static String petName;

	public static String petBirthDate;

	public static String petType;

	public static int petIndex;

	@Before
	public void resetVariables() {
		owner = null;
		htmlPath = null;
		editPetUrl = null;
		addVisitUrl = null;
		petName = null;
		petBirthDate = null;
		petType = null;
		petIndex = -1;
	}

	// Edit Owner Feature
	// //////////////////////////////////////////////////////////////////

	// Normal Flow
	@When("the user searches for an owner with first name {string} and last name {string}")
	public void the_user_searches_for_an_owner_with_lastname(String firstName, String lastName) {
		// Get HTML page of owner
		htmlPath = given().queryParam("lastName", lastName).when().get("/owners").htmlPath();

		// Verify that the Name is right
		assertEquals(firstName + " " + lastName, htmlPath.getString("html.body.div.div.table[0].tr[0].td.b"));

		// Get the owner object
		owner = ownerRepository.findByLastName(lastName).iterator().next();
	}

	@When("the user searches for an owner with first name {string} and a common last name {string}")
	public void the_user_searches_for_an_owner_with_a_common_lastname(String firstName, String lastName) {
		// Get HTML page of owner
		htmlPath = given().queryParam("lastName", lastName).when().get("/owners").htmlPath();

		// Get all owners with the same last name
		Collection<Owner> results = ownerRepository.findByLastName(lastName);
		Iterator<Owner> iterator = results.iterator();
		boolean ownerFound = false;
		// Iterate over all the owners with the common last name
		for (int i = 0; i < results.size(); i++) {
			Owner current = iterator.next();

			// Find the owner with the same firstname
			if (htmlPath.getString("html.body.div.div.table.tbody.tr[" + i + "].td[0].a")
					.equals(firstName + " " + lastName) && current.getFirstName().equals(firstName)) {
				ownerFound = true;
				owner = current;

				// Set htmlPath to the owner's page
				htmlPath = get("/owners/" + owner.getId()).htmlPath();
				break;
			}
		}
		// If the owner wasn't found, it means that the owner doesn't exist
		assertTrue(ownerFound);
	}

	@When("the user searches for an owner called {string} {string} with no input")
	public void the_user_searches_for_an_owner_with_no_input(String firstName, String lastName) {
		// Get HTML page of owner
		htmlPath = given().queryParam("lastName", "").when().get("/owners").htmlPath();

		// Same thing as common last name except we iterate over all owners
		Collection<Owner> results = ownerRepository.findByLastName("");
		Iterator<Owner> iterator = results.iterator();
		boolean ownerFound = false;
		for (int i = 0; i < results.size(); i++) {
			Owner current = iterator.next();
			if (htmlPath.getString("html.body.div.div.table.tbody.tr[" + i + "].td[0].a")
					.equals(firstName + " " + lastName) && current.getFirstName().equals(firstName)
					&& current.getLastName().equals(lastName)) {
				ownerFound = true;
				owner = current;
				htmlPath = get("/owners/" + owner.getId()).htmlPath();
				break;
			}
		}
		assertTrue(ownerFound);
	}

	@And("the user edits the information with {string}, {string}, {string}, {string} and {string}")
	public void the_user_edits_the_information_with(String newFirstName, String newLastName, String address,
			String city, String telephone) {
		// POST request to edit the owner
		htmlPath = given().queryParam("firstName", newFirstName).queryParam("lastName", newLastName)
				.queryParam("address", address).queryParam("city", city).queryParam("telephone", telephone).when()
				.post("/owners/" + owner.getId() + "/edit").htmlPath();
	}

	@Then("the owner's information should be updated accordingly with {string}, {string}, {string}, {string} and {string}")
	public void the_owner_information_should_be_updated_accordingly(String newFirstName, String newLastName,
			String address, String city, String telephone) {
		// Get the owner's page
		htmlPath = given().when().get("/owners/" + owner.getId()).htmlPath();

		// Check that each attribute matches on the page
		assertEquals(newFirstName + " " + newLastName, htmlPath.getString("html.body.div.div.table[0].tr[0].td.b"));
		assertEquals(address, htmlPath.getString("html.body.div.div.table[0].tr[1].td"));
		assertEquals(city, htmlPath.getString("html.body.div.div.table[0].tr[2].td"));
		assertEquals(telephone, htmlPath.getString("html.body.div.div.table[0].tr[3].td"));
	}

	@And("reset the owner's information back to default")
	public void reset_the_owners_information_back_to_default() {
		// Edit the owner's information back to default as cleanup
		given().queryParam("firstName", owner.getFirstName()).queryParam("lastName", owner.getLastName())
				.queryParam("address", owner.getAddress()).queryParam("city", owner.getCity())
				.queryParam("telephone", owner.getTelephone()).when().post("/owners/" + owner.getId() + "/edit");
	}

	// Error Flow
	@When("the user searches for an owner with invalid last name {string}")
	public void the_user_searches_for_an_owner_with_invalid_lastname(String lastName) {
		// Get HTML response of invalid last name search
		htmlPath = given().queryParam("lastName", lastName).when().get("/owners").htmlPath();
	}

	@Then("the message \"has not been found\" should be displayed")
	public void the_message_has_not_been_found_should_be_displayed() {
		assertEquals("has not been found", htmlPath.getString("html.body.div.div.form.div[0].div.div.div.p"));
	}

	// Add Pet Feature //////////////////////////////////////////////////////////////////

	// Normal Flow
	@And("the user adds the pet's information {string}, {string} and {string}")
	public void the_user_adds_the_pets_information(String name, String birthDate, String type) {
		// Get HTML Page of POST request
		htmlPath = given().queryParam("id", "").queryParam("name", name).queryParam("birthDate", birthDate)
				.queryParam("type", type).when().post("/owners/" + owner.getId() + "/pets/new").htmlPath();
	}

	@Then("the pet should appear in the list of pets with the right information {string}, {string} and {string}")
	public void the_pet_should_appear_in_the_list_of_pets(String name, String birthDate, String type) {
		// Get owner's page
		htmlPath = given().when().get("/owners/" + owner.getId()).htmlPath();

		boolean petValidated = false;
		// Go over all the pets of the owner and find the newly added one
		// Save the information from the Pet
		for (int i = 0; i < owner.getPets().size() + 1; i++) {
			if (htmlPath.getString("html.body.div.div.table[1].tr[" + i + "].td[0].dl.dd[0]").equals(name)
					&& htmlPath.getString("html.body.div.div.table[1].tr[" + i + "].td[0].dl.dd[1]").equals(birthDate)
					&& htmlPath.getString("html.body.div.div.table[1].tr[" + i + "].td[0].dl.dd[2]").equals(type)) {
				petValidated = true;
				editPetUrl = htmlPath
						.getString("html.body.div.div.table[1].tr[" + i + "].td[1].table.tr.td[0].a.@href");
				petName = name;
				petBirthDate = birthDate;
				petType = type;
			}
		}

		if (!petValidated)
			fail();
	}

	@And("edit added pet's name to not collide with future pets")
	public void edit_added_pets_name_to_not_collide_with_future_pets() {
		// Get the pet id from the href
		int petId = Integer.parseInt(editPetUrl.split("/pets/")[1].split("/edit")[0]);

		// Set the name with a prefix of "zzz-test-" to make sure the pets stays at the
		// bottom of
		// the list and doesn't interfere with future added pets
		given().queryParam("id", petId).queryParam("name", "zzz-test-" + petName).queryParam("birthDate", petBirthDate)
				.queryParam("type", petType).when().post("/owners/" + editPetUrl);
	}

	// Add Visit Feature
	// //////////////////////////////////////////////////////////////////

	// Normal Flow
	@And("the user adds a visit for the pet {string} the information with {string} and {string}")
	public void the_user_adds_a_visit(String petName, String date, String description) {
		// Go through all the pets and find the one with the specified pet name
		for (int i = 0; i < owner.getPets().size(); i++) {
			if (htmlPath.getString("html.body.div.div.table[1].tr[" + i + "].td[0].dl.dd[0]").equals(petName)) {
				addVisitUrl = htmlPath
						.getString("html.body.div.div.table[1].tr[" + i + "].td[1].table.tr.last().td[1].a.@href");
				petIndex = i;
				break;
			}
		}

		if (addVisitUrl == null)
			fail();

		// Get HTML response of POST request
		htmlPath = given().queryParam("date", date).queryParam("description", description).when()
				.post("/owners/" + addVisitUrl).htmlPath();

	}

	@Then("the previous visits table should have the {string} and {string} of the newly added visit")
	public void the_previous_visits_table_should_be_updated(String date, String description) {
		// Get HTML page of owner
		htmlPath = given().when().get("/owners/" + owner.getId()).htmlPath();

		// Iterate over the all the visits
		// Fail if the visit isn't found
		int i = 0;
		while (!htmlPath.getString("html.body.div.div.table[1].tr[" + petIndex + "].td[1].table.tr[" + i + "].td[0]")
				.equals(date)
				&& !htmlPath
						.getString("html.body.div.div.table[1].tr[" + petIndex + "].td[1].table.tr[" + i + "].td[1]")
						.equals(description)) {
			if (htmlPath.getString("html.body.div.div.table[1].tr[" + petIndex + "].td[1].table.tr[" + i + "].td[0]")
					.equals("")) {
				fail();
			}
			else {
				i++;
			}
		}
	}

	// Error Flow
	@Then("it should display {string} under the {string}th input")
	public void it_should_display_error_message_under_the_ith_input(String errorMessage,
			String inputIndex) {
		// Check the error message of the specified input

		assertEquals(errorMessage,
				htmlPath.getString("html.body.div.div.form.div[0].div[" + inputIndex + "].div.span[1].text()"));
	}

}
