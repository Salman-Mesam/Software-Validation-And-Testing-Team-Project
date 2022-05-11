package org.springframework.samples.petclinic.integration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OwnerTest {

	public Owner owner;

	public String ownerFirstName = "Gurdarshan";

	public String ownerLastName = "Singh";

	public String address = "123 University St.";

	public String city = "Montreal";

	public String telephone = "111-222-3333";

	public Integer ownerId = 789;

	@InjectMocks
	private Owner testOwner;

	@Mock
	Pet pet1;

	@Mock
	Pet pet2;

	@Mock
	Pet pet3;

	@Mock
	Pet pet;

	@BeforeEach
	void setup() {
		testOwner = new Owner();
		testOwner.setFirstName(this.ownerFirstName);
		testOwner.setLastName(this.ownerLastName);
		testOwner.setAddress(this.address);
		testOwner.setCity(this.city);
		testOwner.setTelephone(this.telephone);
		testOwner.setId(this.ownerId);

	}

	@AfterEach
	void resetOwner() {
		this.owner = null;
	}

	/**
	 * test get pets when owner does not have internal pets
	 */
	@Test
	void testGetPetsInternalIfPetsIsNull() {
		assertEquals(testOwner.getPetsInternal().size(), 0);
	}

	/**
	 * test get internal pets for the owner
	 */
	@Test
	void testGetPetsInternal() {
		Set<Pet> pets = new HashSet<>();
		pets.add(pet1);
		pets.add(pet2);
		testOwner.setPetsInternal(pets);
		assertEquals(testOwner.getPetsInternal().size(), 2);
	}

	/**
	 * test get internal pets when it is empty
	 */
	@Test
	void testGetPetsInternalNull() {
		assertEquals(testOwner.getPetsInternal(), new HashSet<Pet>());
	}

	/**
	 * test set internal pets in the correct case
	 */
	@Test
	void testSetPetsInternalValidPet() {
		Set<Pet> pets = new HashSet<>();
		pets.add(pet1);
		pets.add(pet2);
		testOwner.setPetsInternal(pets);
		assertEquals(testOwner.getPetsInternal(), pets);

	}

	/**
	 * test set null internal pets
	 */
	@Test
	void testSetPetsInternalNull() {
		testOwner.setPetsInternal(null);
		assertThat(testOwner.getPetsInternal().isEmpty());
	}

	/**
	 * test get internal pets in the correct case
	 */
	@Test
	void testGetPets() {
		Set<Pet> pets = new HashSet<>();
		when(pet1.getName()).thenReturn("test1");
		when(pet2.getName()).thenReturn("test2");
		when(pet3.getName()).thenReturn("test3");
		pets.add(pet1);
		pets.add(pet2);
		pets.add(pet3);
		testOwner.setPetsInternal(pets);
		List<Pet> result = new ArrayList<>();
		result.add(pet1);
		result.add(pet2);
		result.add(pet3);
		assertEquals(testOwner.getPets(), result);
	}

	/**
	 * test add internal pets for the owner in the correct case
	 */
	@Test
	void testAddPetValidPet() {
		when(pet.isNew()).thenReturn(true);
		doNothing().when(pet).setOwner(testOwner);
		testOwner.addPet(pet);
		assertTrue(testOwner.getPetsInternal().contains(pet));
	}

	/**
	 * test add null internal pets
	 */
	@Test
	void testAddPetNull() {
		assertThrows(Exception.class, () -> testOwner.addPet(null));
	}

	/**
	 * test get null internal pets
	 */
	@Test
	void testGetPetNull() {
		assertEquals(testOwner.getFirstName(), "Gurdarshan");
		assertThrows(NullPointerException.class, () -> testOwner.getPet(null));
	}

}
