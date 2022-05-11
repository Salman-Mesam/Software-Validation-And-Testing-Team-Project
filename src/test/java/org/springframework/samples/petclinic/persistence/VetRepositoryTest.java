package org.springframework.samples.petclinic.persistence;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collection;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class VetRepositoryTest {

	@Autowired
	private VetRepository vetRepository;

	/**
	 * Test find all vets
	 */
	@Test
	void testFindAll() {
		Collection<Vet> veterinarians = vetRepository.findAll();
		assertThat(veterinarians.size()).isEqualTo(6);
	}

}
