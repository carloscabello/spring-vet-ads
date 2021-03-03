
package org.springframework.samples.petclinic.model.utils;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.assertj.core.api.Assertions;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

public class GenerateValidatorTest {

	private static Validator createValidator() {
		LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
		localValidatorFactoryBean.afterPropertiesSet();
		return localValidatorFactoryBean;
	}

	public static <T> void validatorPositiveTests(final T object) {

		/*
		 * 1. Arrange
		 * Inicializamos el validador
		 */
		LocaleContextHolder.setLocale(Locale.ENGLISH);

		Validator validator = GenerateValidatorTest.createValidator();
		/*
		 * 2. Act
		 * Validamos el Ganadero que llega como parametro
		 */
		Set<ConstraintViolation<T>> constraintViolations = validator.validate(object);

		/*
		 * 3. Assert
		 * Comprobamos que no se reciben errores
		 */
		Assertions.assertThat(0).isEqualTo(constraintViolations.size());

	}

	public static <T> void validatorNegativeTests(final T object, final Map<String, List<ExpectedViolation>> expectedConstraintViolations) {

		/*
		 * 1. Arrange
		 * Inicializamos el validador
		 */
		LocaleContextHolder.setLocale(Locale.ENGLISH);

		Validator validator = GenerateValidatorTest.createValidator();
		/*
		 * 2. Act
		 * Validamos el Veterinario que llega como parametro
		 */
		Set<ConstraintViolation<T>> constraintViolations = validator.validate(object);

		/*
		 * 3. Assert
		 * Hacemos las comprobaciones pertinentes
		 */

		//--- 3.1 Comprobamos el numero de restricciones recibidas es el mismo que el esperado
		Integer expectedConstraintViolationCount = expectedConstraintViolations.values().stream().mapToInt(List<ExpectedViolation>::size).sum();

		if (expectedConstraintViolationCount != constraintViolations.size()) {
			System.out.println("==============Unexpected ConstraintViolations at " + object.getClass() + " ================");
			System.out.println(constraintViolations.toString());
		}

		Assertions.assertThat(expectedConstraintViolationCount).isEqualTo(constraintViolations.size());

		//--- 3.2 Para cada restriccion del validador, comprobamos que se esperaba
		for (ConstraintViolation<T> violation : constraintViolations) {

			//Seleccionamos la expectedViolation del Map con la que comparar la violation
			ExpectedViolation expectedViolation = ExpectedViolation.getCorrespondingExpectedViolation(expectedConstraintViolations, violation);

			//--- 3.2.1 Comprobamos que el property path es el esperado
			Assertions.assertThat(violation.getPropertyPath().toString()).isEqualTo(expectedViolation.getProperyPath());

			//--- 3.2.2 Comprobamos que el message template es el esperado
			if (!expectedViolation.getMessageTemplate().isEmpty()) {
				Assertions.assertThat(violation.getMessageTemplate()).isEqualTo("{" + expectedViolation.getMessageTemplate() + "}");
			}

			//--- 3.2.2 Comprobamos que el contenido del message es el esperado
			if (!expectedViolation.getMessage().isEmpty()) {
				Assertions.assertThat(violation.getMessage()).contains(expectedViolation.getMessage());
			}
		}

	}

}
