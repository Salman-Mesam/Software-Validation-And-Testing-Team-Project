package org.springframework.samples.petclinic.controller;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.persistence.OwnerRepository;
import org.springframework.samples.petclinic.persistence.PetRepository;
import org.springframework.samples.petclinic.persistence.VisitRepository;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(OwnerController.class)
public class OwnerControllerTests {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private OwnerRepository owners;

	@MockBean
	private VisitRepository visits;

	@MockBean
	private PetRepository pets;

	/**
	 * Our setup method includes three owners for our tests, as well as mocking of the
	 * findByLastName and findById methods for the mentioned mock objects.
	 */
	@BeforeEach
	void setup() {
		Owner firstOwner = new Owner();
		firstOwner.setFirstName("Salman");
		firstOwner.setLastName("Mesam");
		firstOwner.setId(33);
		ArrayList<Owner> list = new ArrayList<Owner>();
		list.add(firstOwner);
		given(this.owners.findByLastName("Mesam")).willReturn(list);
		given(this.owners.findById(33)).willReturn(firstOwner);

		Owner secondOwner = new Owner();
		secondOwner.setFirstName("Gurdarshan");
		secondOwner.setLastName("Singh");
		secondOwner.setId(55);
		given(this.owners.findByLastName("Singh")).willReturn(Lists.newArrayList(firstOwner, secondOwner));
		given(this.owners.findById(55)).willReturn(secondOwner);

		Pet fish = new Pet();
		fish.setId(105);
		fish.setName("Tim");
		fish.setBirthDate(LocalDate.of(2022, 3, 8));
		fish.setOwner(secondOwner);
		Set<Pet> pets = new HashSet<Pet>();
		pets.add(fish);
		secondOwner.setPetsInternal(pets);
	}

	/**
	 * tests that intialization on the /owners/new endpoint is successful.
	 * @result user taken to "owners/createOrUpdateOwnerForm" view
	 * @author Mohammad Salman Mesam
	 * @throws Exception
	 */
	@Test
	void testValidNewOwnerForm() throws Exception {
		mockMvc.perform(get("/owners/new")).andExpect(status().isOk())
				.andExpect(view().name("owners/createOrUpdateOwnerForm")).andExpect(model().attributeExists("owner"));
	}

	/**
	 * Tests the positive branch of the POST on /owners/new endpoint. Verifies that the
	 * owner form creation is succesfull when provided with valid input body i,e the
	 * mandatory fields first name, last name, city, address and telephone number in the
	 * request body.
	 * @result user taken to "/owners/{ownerId}" page/view -- in this test case owner id
	 * is NULL.
	 * @author Mohammad Salman Mesam
	 * @throws Exception
	 */
	@Test
	void testValidOwnerFormCreation() throws Exception {
		ResultActions actions = mockMvc
				.perform(post("/owners/new").param("firstName", "Salman").param("lastName", "Mesam")
						.param("city", "Montreal").param("address", "123 University St")
						.param("telephone", "1112223333").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isFound());

		actions.andExpect(view().name("redirect:/owners/" + null));

	}

	/**
	 * Tests the negative branch of the POST on "/owners/new" endpoint. verifies that the
	 * owner form creation is unsuccesfull when provided with invalid input body i,e
	 * missing city, address and telephone field in request body(incomplete owner info).
	 * @results user taken to "owners/createOrUpdateOwnerForm"
	 * @author Mohammad Salman Mesam * @throws Exception
	 */
	@Test
	void testInvalidOwnerFormCreation() throws Exception {
		ResultActions actions = mockMvc.perform(post("/owners/new").param("firstName", "Salman")
				.param("lastName", "Mesam").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

		actions.andExpect(view().name("owners/createOrUpdateOwnerForm"));

	}

	/**
	 * tests that intialization on the /owners/find endpoint is successful
	 * @result user taken to "owners/findOwners" page
	 * @author Mohammad Salman Mesam
	 * @throws Exception
	 */
	@Test
	void testFormFindInit() throws Exception {
		mockMvc.perform(get("/owners/find")).andExpect(status().isOk()).andExpect(view().name("owners/findOwners"))
				.andExpect(model().attributeExists("owner"));
	}

	/**
	 * tests the branch of the parameterless (No input) GET request on the /owners
	 * endpoint where no owners are found by lastName being Null as a search parameter.
	 * @result "owners/findOwners" page.
	 * @author Mohammad Salman Mesam
	 * @throws Exception
	 */
	@Test
	void testNoOwnerFormFind() throws Exception {
		mockMvc.perform(get("/owners")).andExpect(status().isOk()).andExpect(view().name("owners/findOwners"))
				.andExpect(model().attributeExists("owner"));
	}

	/**
	 * tests the branch of the GET request on the /owners endpoint where exactly one owner
	 * found by lastName entered as a search parameter
	 * @result user redirected to "/owners/{ownerId}" view
	 * @author Mohammad Salman Mesam
	 * @throws Exception
	 */
	@Test
	void testExactlyOneOwnerFormFind() throws Exception {
		mockMvc.perform(get("/owners").param("firstName", "Salman").param("lastName", "Mesam").param("city", "Montreal")
				.param("address", "123 University St").param("telephone", "1112223333"))
				.andExpect(view().name("redirect:/owners/" + 33)).andExpect(status().isFound());
	}

	/**
	 * tests the processFindForm controller method specifically the branch of the GET
	 * request on the /owners endpoint where more than one owners are found by lastName
	 * entered as a search parameter.
	 * @result user taken to "owners/ownersList" view
	 * @author Mohammad Salman Mesam
	 * @throws Exception
	 */
	@Test
	void testMoreThanOneOwnerFormFind() throws Exception {
		mockMvc.perform(get("/owners").param("firstName", "Gurdarshan").param("lastName", "Singh")
				.param("city", "Montreal").param("address", "123 University St").param("telephone", "1112223333"))
				.andExpect(view().name("owners/ownersList")).andExpect(status().isOk());
	}

	/**
	 * tests that intialization on the /owners/{ownerId}/edit endpoint is successful
	 * @result user taken to "owners/createOrUpdateOwnerForm" page
	 * @author Mohammad Salman Mesam
	 * @throws Exception
	 */
	@Test
	void testUpdateFormInit() throws Exception {
		mockMvc.perform(get("/owners/{ownerId}/edit", 55).param("firstName", "Gurdarshan")).andExpect(status().isOk())
				.andExpect(view().name("owners/createOrUpdateOwnerForm")).andExpect(model().attributeExists("owner"));
	}

	/**
	 * Tests the positive branch of POST on the /owners/{ownerId}/edit endpoint . Checks
	 * that the form update is successfull when provided with valid input request body i,e
	 * input form body includes a first name, last name, city, address and telephone
	 * number.
	 * @result User redirected to "/owners/{ownerId}" page corresponding to the ownerId in
	 * the path of the url.
	 * @author Mohammad Salman Mesam
	 * @throws Exception
	 */
	@Test
	void testValidUpdateOwnerForm() throws Exception {
		ResultActions actions = mockMvc.perform(post("/owners/{ownerId}/edit", 55).param("firstName", "Gurdarshan")
				.param("lastName", "Singh").param("city", "Montreal").param("address", "123 University St")
				.param("telephone", "111222333").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isFound());
		System.out.println(actions);
		actions.andExpect(view().name("redirect:/owners/{ownerId}"));

	}

	/**
	 * Tests the negative branch of POST on the /owners/{ownerId}/edit endpoint . Checks
	 * that the form update is unsuccesfull when provided with invalid input request body
	 * i,e input form body does not contain City, address and telephone
	 * @result User taken to "owners/createOrUpdateOwnerForm" view
	 * @author Mohammad Salman Mesam
	 * @throws Exception
	 */
	@Test
	void testInvalidUpdateOwnerForm() throws Exception {
		ResultActions actions = mockMvc.perform(post("/owners/{ownerId}/edit", 55).param("firstName", "Gurdarshan")
				.param("lastName", "Singh").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

		actions.andExpect(view().name("owners/createOrUpdateOwnerForm"));

	}

	/**
	 * Tests specifically performing a GET on /owners/{ownerId}.It tests that the we are
	 * able to retrieve owner with pets, the owner being assosciated to the ownerId
	 * provided a path variable (the input).
	 * @result user taken to "owners/ownerDetails" page that contains details for the
	 * ownerId provided
	 * @author Mohammad Salman Mesam
	 * @throws Exception
	 */
	@Test
	void testRetrieveOwnerForm() throws Exception {
		mockMvc.perform(get("/owners/{ownerId}", 55)).andExpect(status().isOk())
				.andExpect(view().name("owners/ownerDetails")).andExpect(model().attributeExists("owner"));
	}

}
