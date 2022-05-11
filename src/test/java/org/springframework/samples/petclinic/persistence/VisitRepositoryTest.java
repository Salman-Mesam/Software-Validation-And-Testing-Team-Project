package org.springframework.samples.petclinic.persistence;

import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.samples.petclinic.model.Visit;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class VisitRepositoryTest {

	@Autowired
	private VisitRepository visitRepository;

	/**
	 * Test save visits in the correct case
	 */
	@Test
	void testSaveVisitValid() {
		Visit visit = new Visit();
		visit.setDate(LocalDate.of(2022, 03, 06));
		visit.setDescription("testDescription");
		visit.setPetId(1);
		visit.setId(1234);
		visitRepository.save(visit);
		assertThat(visit.getId()).isEqualTo(1234);
	}

	/**
	 * Test save null visit
	 */
	@Test
	void testSaveVisitNull() {
		try {
			visitRepository.save(null);
		}
		catch (Exception e) {
			assertEquals(e.getMessage(),
					"Entity must not be null.; nested exception is java.lang.IllegalArgumentException: Entity must not be null.");
		}
	}

	/**
	 * Test find pet by id in the correct case
	 */
	@Test
	void testFindPetByExistentId() {

		List<Visit> pet1 = visitRepository.findByPetId(7);
		assertThat(pet1.get(0).getId()).isEqualTo(1);
		assertThat(pet1.get(0).getPetId()).isEqualTo(7);
		assertThat(pet1.get(0).getDate()).isEqualTo("2013-01-01");
		assertThat(pet1.get(0).getDescription()).isEqualTo("rabies shot");

		assertThat(pet1.get(1).getId()).isEqualTo(4);
		assertThat(pet1.get(1).getPetId()).isEqualTo(7);
		assertThat(pet1.get(1).getDate()).isEqualTo("2013-01-04");
		assertThat(pet1.get(1).getDescription()).isEqualTo("spayed");

	}

	/**
	 * Test find pet by id for non-existing pet
	 */
	@Test
	void testFindPetByNonexistentId() {
		List<Visit> nonExist = visitRepository.findByPetId(12345678);
		assertTrue(nonExist.isEmpty());
	}

	/**
	 * Test find null pet
	 */
	@Test
	void testFindPetNull() {
		List<Visit> nullVisit = visitRepository.findByPetId(null);
		assertTrue(nullVisit.isEmpty());
	}

}
