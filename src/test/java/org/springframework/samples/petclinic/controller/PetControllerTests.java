package org.springframework.samples.petclinic.controller;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.service.PetTypeFormatter;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.persistence.OwnerRepository;
import org.springframework.samples.petclinic.persistence.PetRepository;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(value = PetController.class,
		includeFilters = @ComponentScan.Filter(value = PetTypeFormatter.class, type = FilterType.ASSIGNABLE_TYPE))
public class PetControllerTests {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private OwnerRepository owners;

	@MockBean
	private PetRepository pets;

	/**
	 * A setup method creates a pretend owner object and a mock pet object connected with
	 * that owner before each test.
	 */
	@BeforeEach
	void setup() {

		Owner owner = new Owner();
		owner.setFirstName("Salman");
		owner.setLastName("Mesam");
		owner.setAddress("123 University St");
		owner.setCity("Montreal");
		owner.setTelephone("111-222-3333");
		owner.setId(5);

		PetType fish = new PetType();
		fish.setId(105);
		fish.setName("fish");
		ArrayList<PetType> list = new ArrayList<PetType>();
		list.add(fish);
		given(this.pets.findPetTypes()).willReturn(list);
		given(this.owners.findById(5)).willReturn(owner);
		given(this.pets.findById(101)).willReturn(new Pet());
	}

	/**
	 * Tests that intialization on the /pets/new endpoint is successful.
	 * @result User taken to "pets/createOrUpdatePetForm" view
	 * @throws Exception
	 * @author Mohammad Salman Mesam
	 */
	@Test
	void testCreatingNewPetForm() throws Exception {
		mockMvc.perform(get("/owners/{ownerId}/pets/new", 5)).andExpect(status().isOk())
				.andExpect(view().name("pets/createOrUpdatePetForm")).andExpect(model().attributeExists("pet"));
	}

	/**
	 * Tests the positive branch of the POST on /pets/new endpoint. Checks that the pet
	 * form creation is succesfull when provided with valid input body i,e input form body
	 * includes the pet name , type and birth date of the pet
	 * @result User redirected to "/owners/{ownerId}" view
	 * @author Mohammad Salman Mesam
	 * @throws Exception
	 */
	@Test
	void testValidFormCreation() throws Exception {
		mockMvc.perform(post("/owners/{ownerId}/pets/new", 5).param("name", "Tim").param("type", "fish")
				.param("birthDate", "2022-03-08")).andExpect(status().isFound())
				.andExpect(view().name("redirect:/owners/{ownerId}"));
	}

	/**
	 * Tests the negative branch of the POST on /pets/new endpoint. Checks that pet form
	 * creation is unsuccesfull when provided with invalid input body i,e input form body
	 * does not include the pet type and the birth date
	 * @result User taken to "pets/createOrUpdatePetForm" page/view
	 * @author Mohammad Salman Mesam
	 * @throws Exception
	 */
	@Test
	void testInvalidFormCreation() throws Exception {
		mockMvc.perform(post("/owners/{ownerId}/pets/new", 5)).andExpect(model().attributeHasNoErrors("owner"))
				.andExpect(model().attributeHasErrors("pet")).andExpect(model().attributeHasFieldErrors("pet", "type"))
				.andExpect(model().attributeHasFieldErrorCode("pet", "type", "required"))
				.andExpect(model().attributeHasFieldErrors("pet", "birthDate"))
				.andExpect(model().attributeHasFieldErrorCode("pet", "birthDate", "required"))
				.andExpect(model().attributeHasFieldErrors("pet", "name"))
				.andExpect(model().attributeHasFieldErrorCode("pet", "name", "required")).andExpect(status().isOk())
				.andExpect(view().name("pets/createOrUpdatePetForm"));
	}

	/**
	 * Tests that intialization on the /pets/{petId}/edit endpoint is successful
	 * @result User taken to "pets/createOrUpdatePetForm" view
	 * @author Mohammad Salman Mesam
	 * @throws Exception
	 */
	@Test
	void testUpdatingForm() throws Exception {
		mockMvc.perform(get("/owners/{ownerId}/pets/{petId}/edit", 5, 101)).andExpect(status().isOk())
				.andExpect(model().attributeExists("pet")).andExpect(view().name("pets/createOrUpdatePetForm"));
	}

	/**
	 * tests the positive branch of the POST on "/pets/{petId}/edit" endpoint. checks that
	 * the pet form update is successfull when provided with valid input body i,e input
	 * form body includes the pet name , type and birth date of the pet.
	 * @result user taken to "/owners/{ownerId}" view
	 * @author Mohammad Salman Mesam
	 * @throws Exception
	 */
	@Test
	void testValidFormUpdate() throws Exception {
		mockMvc.perform(post("/owners/{ownerId}/pets/{petId}/edit", 5, 101).param("name", "Tim").param("type", "fish")
				.param("birthDate", "2022-03-08")).andExpect(status().isFound())
				.andExpect(view().name("redirect:/owners/{ownerId}"));
	}

	/**
	 * tests the negative branch of the POST on "/pets/{petId}/edit" endpoint. Checks that
	 * the pet form update is unsuccesfull when provided with invalid input body i,e input
	 * form body does not include the pet name, type and the birth date.
	 * @result user taken to "pets/createOrUpdatePetForm" view
	 * @author Mohammad Salman Mesam
	 * @throws Exception
	 */
	@Test
	void testInvalidFormUpdate() throws Exception {
		mockMvc.perform(post("/owners/{ownerId}/pets/{petId}/edit", 5, 101))
				.andExpect(model().attributeHasNoErrors("owner")).andExpect(model().attributeHasErrors("pet"))
				.andExpect(status().isOk()).andExpect(view().name("pets/createOrUpdatePetForm"));
	}

}
