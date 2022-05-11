package org.springframework.samples.petclinic.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.samples.petclinic.model.Specialty;
import org.springframework.samples.petclinic.model.Vet;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class VetTest {

	public String vetFirstName = "Zaphod";

	public String vetLastName = "Beeblebrox";

	@InjectMocks
	public Vet testVet;

	@Mock
	Specialty spc1;

	@Mock
	Specialty spc2;

	@BeforeEach
	void setup() {
		testVet = new Vet();
		testVet.setFirstName(this.vetFirstName);
		testVet.setLastName(this.vetLastName);
	}

	/**
	 * test set speciality for the vet
	 */
	@Test
	void testSetSpecialtiesInternal() {
		Set<Specialty> spcs = new HashSet<>();
		spcs.add(spc1);
		spcs.add(spc2);
		testVet.setSpecialtiesInternal(spcs);
		assertEquals(spcs, testVet.getSpecialtiesInternal());
	}

	/**
	 * test get specialities for the vet in the correct case
	 */
	@Test
	void testGetSpecialties() {
		Set<Specialty> spcs = new HashSet<>();
		spcs.add(spc1);
		spcs.add(spc2);
		testVet.setSpecialtiesInternal(spcs);
		assertEquals(2, testVet.getSpecialtiesInternal().size());
	}

	/**
	 * test get the number of specialities in the correct case
	 */
	@Test
	void testGetNumberOfSpecialties() {
		Set<Specialty> spcs = new HashSet<>();
		spcs.add(spc1);
		spcs.add(spc2);
		testVet.setSpecialtiesInternal(spcs);
		assertEquals(2, testVet.getSpecialtiesInternal().size());
	}

	/**
	 * test add speciality for the vet in the correct case
	 */
	@Test
	void testAddSpecialtyValid() {
		testVet.addSpecialty(spc1);
		assertThat(testVet.getNrOfSpecialties()).isEqualTo(1);
	}

	/**
	 * test add a duplicated speciality
	 */
	@Test
	void testAddSpecialtyDuplicate() {
		testVet.addSpecialty(spc1);
		testVet.addSpecialty(spc1);
		assertThat(testVet.getNrOfSpecialties()).isEqualTo(1);
	}

}
