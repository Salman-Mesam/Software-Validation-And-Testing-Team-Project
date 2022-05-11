package org.springframework.samples.petclinic.persistence;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.Collection;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class PetRepositoryTest {

	@Autowired
	private PetRepository petRepository;

	/**
	 * Test pet type names
	 */
	@Test
	void testFindPetTypes() {

		Collection<PetType> petTypes = petRepository.findPetTypes();
		for (PetType petType : petTypes) {
			switch (petType.getName()) {
			case "bird":
				assertThat(petType.getName()).isEqualTo("bird");
				break;
			case "cat":
				assertThat(petType.getName()).isEqualTo("cat");
				break;
			case "dog":
				assertThat(petType.getName()).isEqualTo("dog");
				break;
			case "hamster":
				assertThat(petType.getName()).isEqualTo("hamster");
				break;
			case "lizard":
				assertThat(petType.getName()).isEqualTo("lizard");
				break;
			case "snake":
				assertThat(petType.getName()).isEqualTo("snake");
				break;
			}
		}
	}

	/**
	 * Test find pet by id
	 */
	@Test
	void testFindByIdExistentId() {

		Pet pet1 = petRepository.findById(1);
		assertThat(pet1.getId()).isEqualTo(1);
		assertThat(pet1.getName()).startsWith("Leo");
		assertThat(pet1.getOwner().getFirstName()).isEqualTo("George");
		assertThat(pet1.getBirthDate()).isEqualTo("2010-09-07");

		Pet pet2 = petRepository.findById(2);
		assertThat(pet2.getId()).isEqualTo(2);
		assertThat(pet2.getName()).startsWith("Basil");
		assertThat(pet2.getOwner().getFirstName()).isEqualTo("Betty");
		assertThat(pet2.getBirthDate()).isEqualTo("2012-08-06");
	}

	/**
	 * Test find pet by id for non-existing pet
	 */
	@Test
	void testFindByIdNonexistentId() {
		Pet nonExist = petRepository.findById(123456);
		assertNull(nonExist);
	}

	/**
	 * Test find null pet
	 */
	@Test
	void testFindByIdNull() {
		assertThrows(Exception.class, () -> petRepository.findById(null));
	}

	/**
	 * Test save pet in the correct case
	 */
	@Test
	void testSaveValidPet() {
		Owner o = new Owner();
		o.setId(1);
		PetType p = new PetType();
		p.setId(1);
		p.setName("cat");
		Pet pet = new Pet();
		pet.setName("poutine");
		pet.setBirthDate(LocalDate.parse("2022-03-06"));
		pet.setOwner(o);
		pet.setType(p);
		pet.setId(1234);
		petRepository.save(pet);
		assertThat(pet.getId()).isEqualTo(1234);
	}

	/**
	 * Test save null pet
	 */
	@Test
	void testSaveNull() {
		try {
			petRepository.save(null);
		}
		catch (Exception e) {
			assertEquals(e.getMessage(),
					"Entity must not be null.; nested exception is java.lang.IllegalArgumentException: Entity must not be null.");
		}

	}

}
