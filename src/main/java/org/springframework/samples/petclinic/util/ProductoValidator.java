
package org.springframework.samples.petclinic.util;

import org.springframework.samples.petclinic.model.Producto;
import org.springframework.samples.petclinic.service.ProductoService;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class ProductoValidator implements Validator {

	private ProductoService productoService;


	public ProductoValidator(final ProductoService productoService) {
		this.productoService = productoService;
	}

	@Override
	public boolean supports(final Class<?> clazz) {
		return Producto.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(final Object target, final Errors errors) {
		Producto producto = (Producto) target;

		//Restricción 1: No puede haber 2 productos con igual nombre para un mismo veterinario
		if (producto.getName() != null) {
			if (this.productoService.findProductoByNameYVetId(producto.getName()).isPresent()) {
				errors.rejectValue("name", "producto.name.repetido");
			}
		}

	}

}
