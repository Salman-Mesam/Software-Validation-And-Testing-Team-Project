package org.springframework.samples.petclinic.persistence;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class OwnerRepositoryTest {

	@Autowired
	private OwnerRepository ownerRepository;

	/**
	 * Test find owner by last name
	 */
	@Test
	void testFindByLastNameExist() {
		String lastName = "existLastName";
		Owner o = new Owner();
		o.setAddress("testAddress");
		o.setCity("Montreal");
		o.setFirstName("Xiaohong");
		o.setLastName(lastName);
		o.setTelephone("5141234567");
		ownerRepository.save(o);

		// find
		Collection<Owner> list = ownerRepository.findByLastName(lastName);

		o = list.iterator().next();

		assertNotNull(o);
		assertEquals("Xiaohong", o.getFirstName());
		assertEquals("Montreal", o.getCity());
		assertEquals("testAddress", o.getAddress());
		assertEquals("5141234567", o.getTelephone());
		assertEquals(lastName, o.getLastName());
	}

	/**
	 * Test find owner by last name when the name is not existed in database
	 */
	@Test
	void testFindByLastNameNotExist() {
		String lastName = "notexistLastName";
		Owner o = new Owner();
		o.setAddress("testAddress");
		o.setCity("Montreal");
		o.setFirstName("Xiaohong");
		o.setLastName("Zhang");
		o.setTelephone("5141234567");
		ownerRepository.save(o);

		Collection<Owner> list = ownerRepository.findByLastName(lastName);

		assertEquals(list.size(), 0);

	}

	/**
	 * Test find owner by last name when name is null
	 */
	@Test
	void testFindByLastNameNull() {

		Owner o = new Owner();
		o.setAddress("testAddress");
		o.setTelephone("5141234567");
		o.setCity("Montreal");
		o.setFirstName("Xiaohong");
		o.setLastName(null);
		assertThrows(ConstraintViolationException.class, () -> {
			ownerRepository.save(o);
		});
	}

	/**
	 * Test find owner by id in the right case
	 */
	@Test
	void testFindByIdExistent() {

		int id = 2;
		Owner o = new Owner();
		o.setAddress("testAddress");
		o.setTelephone("5141234567");
		o.setCity("Montreal");
		o.setFirstName("Xiaohong");
		o.setLastName("Zhang");
		o.setId(id);

		ownerRepository.save(o);

		o = ownerRepository.findById(id);

		assertNotNull(o);
		assertEquals("Xiaohong", o.getFirstName());
		assertEquals("Montreal", o.getCity());
		assertEquals("testAddress", o.getAddress());
		assertEquals("5141234567", o.getTelephone());
		assertEquals("Zhang", o.getLastName());

	}

	/**
	 * Test find owner by id for non-existing owner
	 */
	@Test
	void testFindByIdNonExistent() {
		Owner owner = ownerRepository.findById(10000);
		assertNull(owner);
	}

	/**
	 * Test find owner by id when id is null
	 */
	@Test
	void testFindByIdNull() {
		Owner owner = ownerRepository.findById(null);
		assertNull(owner);
	}

	/**
	 * Test save owner
	 */
	@Test
	void testSaveValidOwner() {
		Owner o = new Owner();
		o.setAddress("testAddress");
		o.setTelephone("5141234567");
		o.setCity("Montreal");
		o.setFirstName("Xiaohong");
		o.setLastName("Zhang");
		o.setId(1234);
		ownerRepository.save(o);
		assertThat(o.getId()).isEqualTo(1234);
	}

	/**
	 * Test save null owner
	 */
	@Test
	void testSaveNull() {
		try {
			ownerRepository.save(null);
		}
		catch (Exception e) {
			assertEquals(e.getMessage(),
					"Entity must not be null.; nested exception is java.lang.IllegalArgumentException: Entity must not be null.");
		}

	}

}
