
package org.springframework.samples.petclinic.util;

import org.springframework.samples.petclinic.model.Ganadero;
import org.springframework.samples.petclinic.service.GanaderoService;
import org.springframework.samples.petclinic.service.UserService;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class GanaderoValidator implements Validator {

	private GanaderoService	ganaderoService;

	private UserService		userService;


	public GanaderoValidator(final GanaderoService ganaderoService, final UserService userService) {
		this.ganaderoService = ganaderoService;
		this.userService = userService;
	}

	@Override
	public boolean supports(final Class<?> clazz) {
		return Ganadero.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(final Object target, final Errors errors) {
		Ganadero ganadero = (Ganadero) target;

		//Restricción 1: Dni veterinario repetido. No puede haber 2 veterinario o más con un mismo dni.
		if (this.ganaderoService.findGanaderoByDni(ganadero.getDni()).isPresent()) {
			errors.rejectValue("dni", "ganadero.dni.repetido");
		}

		//Restricción 2: Nombre de usuario repetido. No puede haber 2 cuentas o más con un  mismo nombre de usuario.
		if (ganadero.getUser() != null && userService.getUserByUsername(ganadero.getUser().getUsername()).isPresent()) {
			errors.rejectValue("user.username", "user.username.repetido");
		}

		if (ganadero.getUser() != null && ganadero.getUser().getUsername().contains(" ")) {
			errors.rejectValue("user.username", "user.username.contieneEspacios");
		}

		if (ganadero.getUser() != null && ganadero.getUser().getPassword().contains(" ")) {
			errors.rejectValue("user.password", "user.password.contieneEspacios");
		}

		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "user.username", "user.username.vacio");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "user.password", "user.password.vacia");

	}

}
