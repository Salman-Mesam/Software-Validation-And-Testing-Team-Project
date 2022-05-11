package org.springframework.samples.petclinic.controller;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.persistence.OwnerRepository;
import org.springframework.samples.petclinic.persistence.PetRepository;
import org.springframework.samples.petclinic.persistence.VisitRepository;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(VisitController.class)
public class VisitControllerTests {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private OwnerRepository owners;

	@MockBean
	private VisitRepository visits;

	@MockBean
	private PetRepository pets;

	/**
	 * Before each test a mock owner is created together with a mock pet object for that
	 * owner.
	 */
	@BeforeEach
	void setup() {
		given(this.pets.findById(101)).willReturn(new Pet());
	}

	/**
	 * Tests the proper initialization of the new visit form at the "/owners//pets/{petId}/visits/new" endpoint
	 * specifically tests the positive branch of conducting a post on the endpoint mentioned.
	 * @result User taken to "pets/createOrUpdateVisitForm" view
	 * @author Mohammad Salman Mesam
	 * @throws Exception
	 */
	@Test
	void testInitNewVisitForm() throws Exception {
		mockMvc.perform(get("/owners/*/pets/{petId}/visits/new", 101)).andExpect(status().isOk())
				.andExpect(view().name("pets/createOrUpdateVisitForm")).andExpect(model().attributeExists("visit"));
	}

	/**
	 * Tests that a new visit form was processed successfully as it was valid
	 * @result User redirected to "/owners/{ownerId}"
	 * @author Mohammad Salman Mesam
	 * @throws Exception
	 */
	@Test
	void testNoErrorsProcessNewVisitForm() throws Exception {
		ResultActions result = mockMvc
				.perform(post("/owners/{ownerId}/pets/{petId}/visits/new", 5, 101).param("date", "2022-03-08")
						.param("description", "New visit").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isFound());

		result.andExpect(view().name("redirect:/owners/{ownerId}"));
	}

	/**
	 * Tests that a new visit form was NOT processed succesfully due to a missing
	 * description
	 * @result User redirected to "pets/createOrUpdateVisitForm" view
	 * @author Mohammad Salman Mesam
	 * @throws Exception
	 */
	@Test
	void testErrorsProcessNewVisitForm() throws Exception {
		ResultActions result = mockMvc.perform(post("/owners/{ownerId}/pets/{petId}/visits/new", 5, 101)
				.param("date", "2022-03-08").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

		result.andExpect(view().name("pets/createOrUpdateVisitForm"));
	}

}
