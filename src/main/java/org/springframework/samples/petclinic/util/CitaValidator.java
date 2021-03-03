package org.springframework.samples.petclinic.util;

import org.springframework.samples.petclinic.model.Cita;
import org.springframework.samples.petclinic.model.TipoEstadoCita;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class CitaValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return Cita.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		Cita cita = (Cita) target;
		
		//Restricción 1: Si el estado de la cita es rechazado, debe haber justificación
		if(cita.getRechazoJustificacion()==null || cita.getRechazoJustificacion().isEmpty()) {
			errors.rejectValue("rechazoJustificacion", "citas.rechazoJustificacion.vacio");
		}
	}

}
