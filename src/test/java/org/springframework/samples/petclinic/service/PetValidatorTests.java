package org.springframework.samples.petclinic.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

@ExtendWith(MockitoExtension.class)
public class PetValidatorTests {

	@Mock
	Pet petMock;

	private PetValidator petValidator = new PetValidator();

	// Verify if invalid pet name gives an error as expected
	@Test
	void testInvalidPetName() {
		Mockito.when(petMock.getName()).thenReturn("");
		Mockito.when(petMock.getBirthDate()).thenReturn(LocalDate.of(2022, 2, 15));
		Errors errors = new BeanPropertyBindingResult(petMock, "name");
		petValidator.validate(petMock, errors);

		assertThat(errors.hasErrors()).isTrue();
		assertThat(errors.getFieldError("name")).isNotNull();
	}

	// Verify if invalid pet type gives an error as expected
	@Test
	void testInvalidPetType() {
		Mockito.when(petMock.getName()).thenReturn("Salman");
		Mockito.when(petMock.getType()).thenReturn(null);
		Mockito.when(petMock.isNew()).thenReturn(true);
		Mockito.when(petMock.getBirthDate()).thenReturn(LocalDate.of(2022, 2, 15));
		Errors errors = new BeanPropertyBindingResult(petMock, "type");
		petValidator.validate(petMock, errors);

		assertThat(errors.hasErrors()).isTrue();
		assertThat(errors.getFieldError("type")).isNotNull();
	}

	// Verify if invalid pet date gives an error as expected
	@Test
	void testInvalidPetDate() {
		Mockito.when(petMock.getName()).thenReturn("Salman");
		Mockito.when(petMock.getBirthDate()).thenReturn(null);
		Errors errors = new BeanPropertyBindingResult(petMock, "birthDate");
		petValidator.validate(petMock, errors);

		assertThat(errors.hasErrors()).isTrue();
		assertThat(errors.getFieldError("birthDate")).isNotNull();
	}

	// Verify if valid pet gives no errors
	@Test
	void testNewValidPet() {
		Mockito.when(petMock.getName()).thenReturn("Salman");
		PetType pt = new PetType();
		Mockito.when(petMock.getType()).thenReturn(pt);
		Mockito.when(petMock.isNew()).thenReturn(true);
		Mockito.when(petMock.getBirthDate()).thenReturn(LocalDate.of(2022, 2, 15));
		Errors errors = null;
		petValidator.validate(petMock, errors);

		assertThat(errors).isNull();
	}

	// Verify valid supports() case
	@Test
	void testValidSupports() {
		Class<Pet> clazz = Pet.class;
		boolean supports = petValidator.supports(clazz);
		assertThat(supports).isTrue();
	}

	// Verify invalid supports() case
	@Test
	void testInvalidSupports() {
		Class<PetType> clazz = PetType.class;
		boolean supports = petValidator.supports(clazz);
		assertThat(supports).isFalse();
	}

}
