package org.springframework.samples.petclinic.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.samples.petclinic.model.*;
import org.springframework.samples.petclinic.persistence.OwnerRepository;
import org.springframework.samples.petclinic.persistence.PetRepository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
public class PetTest {

	public Owner owner;

	public String ownerFirstName = "Gurdarshan";

	public String ownerLastName = "Singh";

	public String address = "123 University St.";

	public String city = "Montreal";

	public String telephone = "111-222-3333";

	public Integer ownerId = 789;

	public Integer petId = 456;

	public String petTypeName = "dog";

	@Mock
	private PetRepository petRepository;

	@Mock
	private OwnerRepository ownerRepository;

	@InjectMocks
	private static Pet pet;

	@BeforeEach
	public void setUp() {

		lenient().when(petRepository.findById(anyInt())).thenAnswer((InvocationOnMock invocation) -> {
			if (invocation.getArgument(0).equals(this.petId)) {
				Pet p = new Pet();
				Owner o = new Owner();
				o.setId(this.ownerId);
				o.setAddress(this.address);
				o.setCity(this.city);
				o.setFirstName(this.ownerFirstName);
				o.setLastName(this.ownerLastName);
				o.setTelephone(this.telephone);
				Set<Pet> set = new HashSet<Pet>();
				set.add(p);
				o.setPetsInternal(set);
				p.setOwner(o);
				p.setId(this.petId);
				p.setName("testName");
				p.setBirthDate(LocalDate.of(2020, 1, 8));
				PetType pT = new PetType();
				pT.setId(1);
				pT.setName(this.petTypeName);
				p.setType(pT);
				pet = p;
				return p;
			}
			else {
				return null;
			}
		});

	}

	/**
	 * test get pet types
	 */
	@Test
	void testGetType() {
		Pet p = petRepository.findById(this.petId);
		assertEquals(pet.getType(), p.getType());
		assertEquals(pet.getType().getName(), p.getType().getName());
		assertEquals(pet.getType().getId(), p.getType().getId());

	}

	/**
	 * test set pet type in the correct case
	 */
	@Test
	void testSetPetTypeValid() {
		Pet p = petRepository.findById(this.petId);
		PetType petTypeValid = new PetType();
		petTypeValid.setId(10);
		petTypeValid.setName("test");
		p.setType(petTypeValid);
		assertEquals(p.getType().getId(), 10);
	}

	/**
	 * test set null pet types
	 */
	@Test
	void testSetTypeNull() {
		Pet p = petRepository.findById(this.petId);
		p.setType(null);
		assertNull(p.getType());
	}

	/**
	 * test get owner for the pet in the correct case
	 */
	@Test
	void testGetOwnerExistentOwner() {
		Pet p = petRepository.findById(this.petId);
		assertEquals(pet.getOwner(), p.getOwner());
	}

	/**
	 * test get null owner
	 */
	@Test
	void testGetOwnerNull() {
		assertNull(ownerRepository.findById(anyInt()));
	}

	/**
	 * test set owner in the correct case
	 */
	@Test
	void testSetOwner() {
		Pet p = petRepository.findById(this.petId);
		Owner owner = new Owner();
		owner.setId(111111);
		owner.setFirstName("firstName");
		owner.setLastName("lastName");
		owner.setAddress("address");
		owner.setCity("City");
		owner.setTelephone("123456789");
		p.setOwner(owner);
		assertEquals(p.getOwner().getId(), 111111);
	}

	/**
	 * test set visit for the pet in the correct case
	 */
	@Test
	void testSetVisitsInternal() {
		Pet p = petRepository.findById(this.petId);
		Collection<Visit> visits = new LinkedHashSet<>();
		assertEquals(visits.size(), 0);
		Visit visit1 = new Visit();
		visit1.setId(100);
		visit1.setDescription("des");
		visit1.setDate(LocalDate.of(2022, 03, 07));
		visits.add(visit1);
		Visit visit2 = new Visit();
		visit2.setId(200);
		visit2.setDescription("des2");
		visit2.setDate(LocalDate.of(2022, 03, 8));
		visits.add(visit2);
		p.setVisitsInternal(visits);
		assertEquals(visits.size(), 2);
	}

	/**
	 * test get visit for the pet in the correct case
	 */
	@Test
	void testGetVisitsInternalExistentVisits() {
		Pet p = petRepository.findById(this.petId);
		Collection<Visit> visits = new LinkedHashSet<>();
		Visit visit1 = new Visit();
		assertEquals(visits.size(), 0);
		visit1.setId(100);
		visit1.setDescription("des");
		visit1.setDate(LocalDate.of(2022, 03, 07));
		visits.add(visit1);
		Visit visit2 = new Visit();
		visit2.setId(200);
		visit2.setDescription("des2");
		visit2.setDate(LocalDate.of(2022, 03, 8));
		visits.add(visit2);
		p.setVisitsInternal(visits);
		assertEquals(p.getVisitsInternal().size(), 2);
	}

	/**
	 * test get null visit
	 */
	@Test
	void testGetVisitsInternalNull() {
		Pet p = petRepository.findById(this.petId);
		assertThrows(NullPointerException.class, () -> {
			p.setVisitsInternal(null);
		});
	}

	/**
	 * test get visit for the pet in the correct case
	 */
	@Test
	void testGetVisits() {
		Pet p = petRepository.findById(this.petId);
		Collection<Visit> visits = new LinkedHashSet<>();
		assertEquals(visits.size(), 0);
		Visit visit1 = new Visit();
		visit1.setId(100);
		visit1.setDescription("des");
		visit1.setDate(LocalDate.of(2022, 03, 07));
		visits.add(visit1);
		Visit visit2 = new Visit();
		visit2.setId(200);
		visit2.setDescription("des2");
		visit2.setDate(LocalDate.of(2022, 03, 8));
		visits.add(visit2);
		p.setVisitsInternal(visits);
		assertEquals(p.getVisits().size(), 2);
		assertEquals(p.getVisits().get(1).getPetId(), visit1.getPetId());
		assertEquals(p.getVisits().get(0).getPetId(), visit2.getPetId());
	}

	/**
	 * test add visit for the pet in the correct case
	 */
	@Test
	void testAddVisit() {
		Pet p = petRepository.findById(this.petId);
		Visit visit = new Visit();
		visit.setId(11111);
		visit.setDescription("another description1");
		visit.setDate(LocalDate.of(2022, 03, 07));
		p.addVisit(visit);
		assertEquals(p.getVisits().get(0).getPetId(), visit.getPetId());
	}

	/**
	 * test add null visit
	 */
	@Test
	void testAddVisitNull() {
		Pet p = petRepository.findById(this.petId);
		assertThrows(NullPointerException.class, () -> {
			p.addVisit(null);
		});
	}

	/**
	 * test add an already existing visit
	 */
	@Test
	void testAddVisitDuplicate() {
		Pet p = petRepository.findById(this.petId);
		Visit visit1 = new Visit();
		visit1.setId(100);
		visit1.setDescription("des");
		visit1.setDate(LocalDate.of(2022, 03, 07));
		p.addVisit(visit1);
		Visit visit2 = new Visit();
		visit2.setId(100);
		visit2.setDescription("des");
		visit2.setDate(LocalDate.of(2022, 03, 07));
		p.addVisit(visit2);

		assertEquals(p.getVisits().size(), 2);
		assertEquals(p.getVisits().get(1).getPetId(), visit1.getPetId());
		assertEquals(p.getVisits().get(0).getPetId(), visit2.getPetId());
	}

}
