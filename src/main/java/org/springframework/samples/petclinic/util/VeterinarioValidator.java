
package org.springframework.samples.petclinic.util;

import org.springframework.samples.petclinic.model.Veterinario;
import org.springframework.samples.petclinic.service.UserService;
import org.springframework.samples.petclinic.service.VetService;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class VeterinarioValidator implements Validator {

	private VetService	veterinarioService;

	private UserService	userService;


	public VeterinarioValidator(final VetService veterinarioService, final UserService userService) {
		this.veterinarioService = veterinarioService;
		this.userService = userService;
	}

	@Override
	public boolean supports(final Class<?> clazz) {
		return Veterinario.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(final Object target, final Errors errors) {
		Veterinario vet = (Veterinario) target;

		//Restricción 1: Dni veterinario repetido. No puede haber 2 veterinario o más con un mismo dni.
		if (this.veterinarioService.findVeterinarioByDni(vet.getDni()).isPresent()) {
			errors.rejectValue("dni", "veterinario.dni.repetido");
		}

		//Restricción 2: Nombre de usuario repetido. No puede haber 2 cuentas o más con un  mismo nombre de usuario.
		if (this.userService.getUserByUsername(vet.getUser().getUsername()).isPresent()) {
			errors.rejectValue("user.username", "user.username.repetido");
		}

		//Restricción 3: Nombre de usuario con espacios. El nombre de usuario no puede contener espacios
		if (vet.getUser().getUsername().contains(" ")) {
			errors.rejectValue("user.username", "user.username.contieneEspacios");
		}
		//Restricción 4: Contraseña con espacios. La contraseña no puede contener espacios
		if (vet.getUser().getPassword().contains(" ")) {
			errors.rejectValue("user.password", "user.password.contieneEspacios");
		}

		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "user.username", "user.username.vacio");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "user.password", "user.password.vacia");

	}

}
