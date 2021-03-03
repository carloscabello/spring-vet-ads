
package org.springframework.samples.petclinic.util;

import org.springframework.samples.petclinic.model.ExplotacionGanadera;
import org.springframework.samples.petclinic.service.ExplotacionGanaderaService;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class ExplotacionGanaderaValidator implements Validator {

	private ExplotacionGanaderaService expGanService;


	public ExplotacionGanaderaValidator(final ExplotacionGanaderaService expGanService) {
		this.expGanService = expGanService;
	}

	@Override
	public boolean supports(final Class<?> clazz) {
		return ExplotacionGanadera.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(final Object target, final Errors errors) {
		ExplotacionGanadera expGanadera = (ExplotacionGanadera) target;

		//Restricción 1: Número de registro repetido. No puede haber 2 o más explotaciones ganaderas con el mismo numero de registro
		if (this.expGanService.findExpGanaderaByNumeroRegistro(expGanadera.getNumeroRegistro()).isPresent()) {
			errors.rejectValue("numeroRegistro", "expGanadera.numRegistro.repetido");
		}

	}

}
