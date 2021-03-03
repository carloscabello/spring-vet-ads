
package org.springframework.samples.petclinic.util;

import org.springframework.samples.petclinic.forms.RecetaForm;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class RecetaFormValidator implements Validator {

	@Override
	public boolean supports(final Class<?> clazz) {
		return RecetaForm.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(final Object target, final Errors errors) {
		RecetaForm recetaForm = (RecetaForm) target;

		//Restricción 1: Debe tener al menos la cantidad de 1 producto del veterinario
		if (recetaForm.getCantidad() != null) {
			if (recetaForm.getCantidad().stream().mapToInt(x -> x).sum() == 0) {
				errors.rejectValue("cantidad", "receta.cantidadTotal.igualA0", "La suma de las cantidades de los productos debe ser superior a 0");
			}
		}

	}

}
