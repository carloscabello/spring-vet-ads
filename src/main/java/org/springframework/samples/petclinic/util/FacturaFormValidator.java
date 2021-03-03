
package org.springframework.samples.petclinic.util;

import org.springframework.samples.petclinic.forms.FacturaForm;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class FacturaFormValidator implements Validator {

	@Override
	public boolean supports(final Class<?> clazz) {
		return FacturaForm.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(final Object target, final Errors errors) {
		FacturaForm facturaForm = (FacturaForm) target;

		if (facturaForm.getRecetas() == null && (facturaForm.getCantidad() == null || facturaForm.getProductoId() == null)) {
			errors.rejectValue("recetas", "facturaForm.facturaVacia", "No puede crear una factura sin recetas y sin productos no recetables");
		}

	}

}
