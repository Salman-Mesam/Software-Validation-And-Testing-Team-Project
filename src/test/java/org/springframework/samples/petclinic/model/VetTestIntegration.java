package org.springframework.samples.petclinic.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * This class is the same as the test in the integration folder. It is left here for testing automation use.
 */
@ExtendWith(MockitoExtension.class)
public class VetTestIntegration {
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

	@Test
	void testSetSpecialtiesInternal() {
		Set<Specialty> spcs = new HashSet<>();
		spcs.add(spc1);
		spcs.add(spc2);
		testVet.setSpecialtiesInternal(spcs);
		assertEquals(spcs, testVet.getSpecialtiesInternal());
	}

	@Test
	void testGetSpecialties() {
		Set<Specialty> spcs = new HashSet<>();
		spcs.add(spc1);
		spcs.add(spc2);
		testVet.setSpecialtiesInternal(spcs);
		assertEquals(2, testVet.getSpecialtiesInternal().size());
	}

	@Test
	void testGetNumberOfSpecialties() {
		Set<Specialty> spcs = new HashSet<>();
		spcs.add(spc1);
		spcs.add(spc2);
		testVet.setSpecialtiesInternal(spcs);
		assertEquals(2, testVet.getSpecialtiesInternal().size());
	}

	@Test
	void testAddSpecialtyValid() {
		testVet.addSpecialty(spc1);
		assertThat(testVet.getNrOfSpecialties()).isEqualTo(1);
	}

	@Test
	void testAddSpecialtyDuplicate() {
		testVet.addSpecialty(spc1);
		testVet.addSpecialty(spc1);
		assertThat(testVet.getNrOfSpecialties()).isEqualTo(1);
	}
}
