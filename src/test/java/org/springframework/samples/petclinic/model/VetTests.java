/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.util.SerializationUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Dave Syer
 */
class VetTests {

	public Vet vet;

	public String vetFirstName = "Zaphod";

	public String vetLastName = "Beeblebrox";

	public Specialty specialty1 = new Specialty();

	public String specialtyName1 = "kids";

	public Specialty specialty2 = new Specialty();

	public String specialtyName2 = "dentist";

	public Integer vetId = 123;

	@BeforeEach
	void createVet() {
		this.vet = new Vet();
		this.vet.setFirstName(this.vetFirstName);
		this.vet.setLastName(this.vetLastName);
		this.vet.setId(this.vetId);
	}

	@AfterEach
	void resetVet() {
		this.vet = null;
	}

	@Test
	void testSerialization() {
		Vet other = (Vet) SerializationUtils.deserialize(SerializationUtils.serialize(this.vet));
		assertThat(other.getFirstName()).isEqualTo(this.vet.getFirstName());
		assertThat(other.getLastName()).isEqualTo(this.vet.getLastName());
		assertThat(other.getId()).isEqualTo(this.vet.getId());
	}

	// Verify the list of specialties for a newly created vet
	@Test
	void testNewEmptySpecialties() {
		List<Specialty> sp = this.vet.getSpecialties();
		assertThat(sp.size()).isEqualTo(0);
		assertThat(this.vet.getNrOfSpecialties()).isEqualTo(0);
	}

	// Verify the functionality of addSpecialty
	@Test
	void testAddNewSpecialties() {
		this.specialty1.setName(this.specialtyName1);
		this.vet.addSpecialty(this.specialty1);
		assertThat(this.vet.getNrOfSpecialties()).isEqualTo(1);
	}

	// Verify functionality of getSpecialtiesInternal
	@Test
	void testGetSpecialtiesInternalNull() {
		Vet vet = new Vet();
		assertThat(vet.getSpecialtiesInternal().isEmpty());
	}

	// Verify that when getting the list of specialties we get it in ascending order of
	// name
	@Test
	void testSpecialtySorting() {

		this.specialty1.setName(this.specialtyName1);
		this.specialty2.setName(this.specialtyName2);

		this.vet.addSpecialty(this.specialty1);
		this.vet.addSpecialty(this.specialty2);

		List<Specialty> sp = this.vet.getSpecialties();
		assertThat(sp.get(0).getName()).isEqualTo(this.specialtyName2);
		assertThat(sp.get(1).getName()).isEqualTo(this.specialtyName1);
	}

	// Verify functionality of setSpecialtiesInternal
	@Test
	void testSetSpecialtiesInternal() {
		Vet testVet = new Vet();
		Set<Specialty> spcs = new HashSet<>();
		spcs.add(this.specialty1);
		spcs.add(this.specialty2);
		testVet.setSpecialtiesInternal(spcs);
		assertEquals(spcs, testVet.getSpecialtiesInternal());
	}

	// Verify the list of vets for a newly created Vets object
	@Test
	void testGetVetList() {
		Vets vets = new Vets();
		List<Vet> vlist = vets.getVetList();
		assertThat(vlist.size()).isEqualTo(0);
	}

}
