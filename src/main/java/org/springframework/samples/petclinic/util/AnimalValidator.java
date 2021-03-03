
package org.springframework.samples.petclinic.util;

import org.springframework.samples.petclinic.model.Animal;
import org.springframework.samples.petclinic.service.AnimalService;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class AnimalValidator implements Validator {

	private AnimalService	animalService;

	private Integer			expGanaderaId;


	public AnimalValidator(final AnimalService animalService, final Integer expGanaderaId) {
		this.animalService = animalService;
		this.expGanaderaId = expGanaderaId;
	}

	@Override
	public boolean supports(final Class<?> clazz) {
		return Animal.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(final Object target, final Errors errors) {
		Animal animal = (Animal) target;

		//Restricción 1: Identificador animal repetido. Un animal no archivado ya existe en el sistema con el mismo identificador en esa explotación ganadera.
		if (this.animalService.findAnimalByIdentificadorAnimalAndExpIdAndEsArchivado(animal.getIdentificadorAnimal(), this.expGanaderaId, false).isPresent()) {
			errors.rejectValue("identificadorAnimal", "animal.identificadorAnimal.repetido");
		}

		//Restricción 2: Fecha identificación anterior a fecha nacimiento. La fecha de identificación debe ser posterior a la fecha de nacimiento.
		if (animal.getFechaNacimiento() != null && animal.getFechaIdentificacion() != null) {
			if (animal.getFechaNacimiento().isAfter(animal.getFechaIdentificacion())) {
				errors.rejectValue("fechaIdentificacion", "animal.fechaIdentificacion.anteriorAFechaNac");
			}
		}

		if (animal.getComprado() != null) {

			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "procedencia", "animal.procedencia.vacia");
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "fechaEntrada", "animal.fechaEntrada.vacia");

			if (animal.getFechaEntrada() != null) {
				//Restricción 3: Fecha entrada anterior a fecha de nacimiento. La fecha de entrada debe ser posterior a la fecha de nacimiento
				if (animal.getFechaNacimiento() != null) {
					if (animal.getFechaEntrada().isBefore(animal.getFechaNacimiento())) {
						errors.rejectValue("fechaEntrada", "animal.fechaEntrada.anteriorAFechaNac");
					}
				}

				//Restricción 4: Fecha entrada anterior a fecha identificación. La fecha de entrada debe ser posterior a la fecha de identificación
				if (animal.getFechaIdentificacion() != null) {
					if (animal.getFechaEntrada().isBefore(animal.getFechaIdentificacion())) {
						errors.rejectValue("fechaEntrada", "animal.fechaEntrada.anteriorAFechaIden");
					}
				}
			}

		}

	}

}
