package org.springframework.samples.petclinic.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class OwnerTests {

	public Owner owner;

	public String ownerFirstName = "Gurdarshan";

	public String ownerLastName = "Singh";

	public String address = "123 University St.";

	public String city = "Montreal";

	public String telephone = "111-222-3333";

	public Integer ownerId = 789;

	public Pet pet;

	public String petName = "Salman";

	public Integer petId = 456;

	public Pet pet2;

	public String petName2 = "Dany";

	public Integer petId2 = 751;

	@BeforeEach
	void createOwner() {
		this.owner = new Owner();
		this.owner.setFirstName(this.ownerFirstName);
		this.owner.setLastName(this.ownerLastName);
		this.owner.setId(this.ownerId);
		this.owner.setAddress(this.address);
		this.owner.setCity(this.city);
		this.owner.setTelephone(this.telephone);
	}

	@AfterEach
	void resetOwner() {
		this.owner = null;
	}

	// Check that getters return the right attribute values
	@Test
	void testVerifyOwner() {
		assertThat(this.owner.getFirstName()).isEqualTo(this.ownerFirstName);
		assertThat(this.owner.getLastName()).isEqualTo(this.ownerLastName);
		assertThat(this.owner.getId()).isEqualTo(this.ownerId);
		assertThat(this.owner.getAddress()).isEqualTo(this.address);
		assertThat(this.owner.getCity()).isEqualTo(this.city);
		assertThat(this.owner.getTelephone()).isEqualTo(this.telephone);
	}

	// Check the size of the pet list of a newly created owner
	@Test
	void testNewEmptyPets() {
		List<Pet> pets = this.owner.getPets();
		assertThat(pets.size()).isEqualTo(0);
	}

	// Check the addPet functionality
	@Test
	void testAddNewPet() {
		this.pet = new Pet();
		this.pet.setName(this.petName);
		this.owner.addPet(this.pet);
		assertThat(this.owner.getPets().size()).isEqualTo(1);
	}

	// Check if an existing pet's owner gets changed correctly
	@Test
	void testAddNewExistingPet() {
		this.pet = new Pet();
		this.pet.setId(this.petId);
		this.pet.setName(this.petName);
		this.owner.addPet(this.pet);
		assertThat(this.pet.getOwner()).isEqualTo(this.owner);
	}

	// Verify that when getting the list of pets we get it in ascending order of name
	@Test
	void testPetsSorting() {

		this.pet = new Pet();
		this.pet.setId(this.petId);
		this.pet.setName(this.petName);

		this.pet2 = new Pet();
		this.pet2.setId(this.petId2);
		this.pet2.setName(this.petName2);

		Set<Pet> pets = new LinkedHashSet<>();
		pets.add(this.pet);
		pets.add(this.pet2);

		this.owner.setPetsInternal(pets);

		List<Pet> pt = this.owner.getPets();
		assertThat(pt.get(0).getName()).isEqualTo(this.petName2);
		assertThat(pt.get(1).getName()).isEqualTo(this.petName);
	}

	@Test
	void testGetNotNewPet() {
		this.pet = new Pet();
		this.pet.setName(this.petName);
		this.pet.setId(this.petId);
		this.owner.addPet(this.pet);
		assertThat(this.owner.getPet(this.petName)).isEqualTo(null);
	}

	// Verify the different branches of getPet based on if it's a new Pet or not
	@Test
	void testGetNewPet() {
		this.pet = new Pet();
		this.pet.setName(this.petName);
		this.pet2 = new Pet();
		this.pet2.setName(this.petName2);
		this.owner.addPet(this.pet);
		this.owner.addPet(this.pet2);
		assertThat(this.owner.getPet(this.petName)).isEqualTo(this.pet);
		assertThat(this.owner.getPet(this.petName2)).isEqualTo(this.pet2);
	}

	// Verify the functionality of ToString
	@Test
	void testVerifyToString() {
		assertThat(this.owner.toString().contains(String.valueOf(this.ownerId))).isEqualTo(true);
		assertThat(this.owner.toString().contains(this.ownerFirstName)).isEqualTo(true);
		assertThat(this.owner.toString().contains(this.ownerLastName)).isEqualTo(true);
		assertThat(this.owner.toString().contains(this.address)).isEqualTo(true);
		assertThat(this.owner.toString().contains(this.city)).isEqualTo(true);
		assertThat(this.owner.toString().contains(this.telephone)).isEqualTo(true);
	}

}
