package org.springframework.samples.petclinic.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.LinkedHashSet;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PetTests {

	public Pet pet;

	public String petName = "Salman";

	public PetType petType = new PetType();

	public String petTypeName = "dog";

	public LocalDate birthDate = LocalDate.of(2022, 1, 15);

	public Integer petId = 456;

	public Visit visit1 = new Visit();

	public LocalDate visitDate1 = LocalDate.of(2022, 1, 25);

	public String visitDescription1 = "Vaccination";

	public Visit visit2 = new Visit();

	public LocalDate visitDate2 = LocalDate.of(2022, 2, 5);

	public String visitDescription2 = "Initial Checkup";

	public Owner owner;

	public String ownerFirstName = "Gurdarshan";

	@BeforeEach
	void createPet() {
		this.pet = new Pet();
		this.pet.setName(this.petName);
		this.pet.setId(this.petId);
		this.pet.setType(this.petType);
		this.petType.setName(this.petTypeName);
		this.pet.setBirthDate(this.birthDate);
		this.owner = new Owner();
		this.owner.setFirstName(this.ownerFirstName);
		this.pet.setOwner(this.owner);
	}

	@AfterEach
	void resetPet() {
		this.pet = null;
		this.owner = null;
	}

	// Verify functionality of getters
	@Test
	void testVerifyPet() {
		assertThat(this.pet.getName()).isEqualTo(this.petName);
		assertThat(this.pet.getId()).isEqualTo(this.petId);
		assertThat(this.pet.getBirthDate()).isEqualTo(this.birthDate);
		assertThat(this.pet.getType()).isEqualTo(this.petType);
		assertThat(this.pet.getType().toString()).isEqualTo(this.petTypeName);
		assertThat(this.pet.getOwner()).isEqualTo(this.owner);
	}

	// Verify the list of visits for a newly created pet
	@Test
	void testNewEmptyVisits() {
		List<Visit> visits = this.pet.getVisits();
		assertThat(visits.size()).isEqualTo(0);
	}

	// Verify the functionality of addVisit
	@Test
	void testAddNewVisit() {
		this.visit1.setDate(this.visitDate1);
		this.visit1.setDescription(this.visitDescription1);
		this.visit1.setId(this.petId);
		this.pet.addVisit(this.visit1);
		assertThat(this.pet.getVisits().size()).isEqualTo(1);
	}

	// Verify that when getting the list of visits we get it in ascending order of date
	@Test
	void testVisitsSorting() {

		this.visit1.setDate(this.visitDate1);
		this.visit1.setDescription(this.visitDescription1);
		this.visit1.setPetId(this.petId);

		this.visit2.setDate(this.visitDate2);
		this.visit2.setDescription(this.visitDescription2);
		this.visit2.setPetId(this.petId);

		Set<Visit> visits = new LinkedHashSet<>();
		visits.add(this.visit1);
		visits.add(this.visit2);

		this.pet.setVisitsInternal(visits);

		List<Visit> vs = this.pet.getVisits();
		assertThat(vs.get(0).getDate()).isEqualTo(this.visitDate2);
		assertThat(vs.get(0).getDescription()).isEqualTo(this.visitDescription2);
		assertThat(vs.get(0).getPetId()).isEqualTo(this.petId);
		assertThat(vs.get(1).getDate()).isEqualTo(this.visitDate1);
		assertThat(vs.get(1).getDescription()).isEqualTo(this.visitDescription1);
		assertThat(vs.get(1).getPetId()).isEqualTo(this.petId);
	}

}
