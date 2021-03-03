
package org.springframework.samples.petclinic.util;

import org.springframework.samples.petclinic.model.AnimalHistorico;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class AnimalHistoricoValidator implements Validator {

	public AnimalHistoricoValidator() {
	}

	@Override
	public boolean supports(final Class<?> clazz) {
		return AnimalHistorico.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(final Object target, final Errors errors) {
		AnimalHistorico animalHistorico = (AnimalHistorico) target;

		//Restricción 1: Una de las 2 fechas no puede ser nula.
		if (animalHistorico.getFechaFallecimiento() == null && animalHistorico.getFechaVenta() == null) {
			errors.rejectValue("fechaFallecimiento", "animalHistorico.fecha.nula");
			errors.rejectValue("fechaVenta", "animalHistorico.fecha.nula");
		}

	}

}
