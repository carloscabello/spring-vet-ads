package org.springframework.samples.petclinic.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.samples.petclinic.model.utils.ExpectedViolation;
import org.springframework.samples.petclinic.service.GanaderoServiceTests;
import org.springframework.util.Assert;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;


public class ValidatorGanaderoTests {

	private Validator createValidator() {
		LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
		localValidatorFactoryBean.afterPropertiesSet();
		return localValidatorFactoryBean;
	}
	
	private static Stream<Arguments> negativeTestGanaderoData(){
		
		Stream<Arguments> res;
		
		Ganadero ganaderoNeg1 = GanaderoServiceTests.generateGanadero("", "Banderas", "0123456789", 
				"anton@mail.com", "Sevilla", "Dos Hermanas", "12345678A", "41345", 
				"Calle Antonio Mairena, 8", "antonio1", "12345");
		Map<String,List<ExpectedViolation>> ganaderoNeg1Violations = Stream.of(
				new ExpectedViolation("firstName", "must not be empty", "javax.validation.constraints.NotEmpty.message"))
				.collect(Collectors.groupingBy(ExpectedViolation::getProperyPath));
		
		Ganadero ganaderoNeg2 = GanaderoServiceTests.generateGanadero(
				"Lucas", "", "0123456789", 
				"l12222@mail.com", "Málaga", "Marbella", "12345678J", "10100", 
				"Avenida de los ilustrísimos 81, portal A", "lucas", "12345");
		Map<String,List<ExpectedViolation>> ganaderoNeg2Violations= Stream.of(
				new ExpectedViolation("lastName", "must not be empty", "javax.validation.constraints.NotEmpty.message"))
				.collect(Collectors.groupingBy(ExpectedViolation::getProperyPath));
		
		Ganadero ganaderoNeg3 = GanaderoServiceTests.generateGanadero(
				"Lucas", "Sánchez", "", 
				"", "Málaga", "Marbella", "12345678J", "", 
				"", "lucas", "12345");
		Map<String,List<ExpectedViolation>> ganaderoNeg3Violations = Stream.of(
				new ExpectedViolation("postalCode", "must not be empty", "javax.validation.constraints.NotEmpty.message"),
				new ExpectedViolation("address", "must not be empty", "javax.validation.constraints.NotEmpty.message"),
				new ExpectedViolation("postalCode", "must match", "javax.validation.constraints.Pattern.message"),
				new ExpectedViolation("telephone", "digits", "javax.validation.constraints.Digits.message"),
				new ExpectedViolation("telephone", "must not be empty", "javax.validation.constraints.NotEmpty.message"),
				new ExpectedViolation("mail", "must not be empty", "javax.validation.constraints.NotEmpty.message"))
				.collect(Collectors.groupingBy(ExpectedViolation::getProperyPath));
		
		Ganadero ganaderoNeg4 = GanaderoServiceTests.generateGanadero(
				"Lucas", "Iglesias", "189892902", 
				"l12222@mail.com", "Málaga", "Marbella", "", "10100", 
				"Avenida de los ilustrísimos 81, portal A", "lucas", "12345");
		Map<String,List<ExpectedViolation>> ganaderoNeg4Violations = Stream.of(
				new ExpectedViolation("dni", "must not be empty", "javax.validation.constraints.NotEmpty.message"),
				new ExpectedViolation("dni", "must match", "javax.validation.constraints.Pattern.message"))
				.collect(Collectors.groupingBy(ExpectedViolation::getProperyPath));
		
		Ganadero ganaderoNeg5 = GanaderoServiceTests.generateGanadero(
				"", "", "", 
				"", "Málaga", "Marbella", "12345678J", "", 
				"", "lucas", "12345");
		Map<String,List<ExpectedViolation>> ganaderoNeg5Violations = Stream.of(
				new ExpectedViolation("firstName", "must not be empty", "javax.validation.constraints.NotEmpty.message"),
				new ExpectedViolation("lastName", "must not be empty", "javax.validation.constraints.NotEmpty.message"),
				new ExpectedViolation("postalCode", "must not be empty", "javax.validation.constraints.NotEmpty.message"),
				new ExpectedViolation("address", "must not be empty", "javax.validation.constraints.NotEmpty.message"),
				new ExpectedViolation("postalCode", "must match", "javax.validation.constraints.Pattern.message"),
				new ExpectedViolation("telephone", "digits", "javax.validation.constraints.Digits.message"),
				new ExpectedViolation("telephone", "must not be empty", "javax.validation.constraints.NotEmpty.message"),
				new ExpectedViolation("mail", "must not be empty", "javax.validation.constraints.NotEmpty.message"))
				.collect(Collectors.groupingBy(ExpectedViolation::getProperyPath));
		
		Ganadero ganaderoNeg6 = GanaderoServiceTests.generateGanadero(
				"Lucas", "Iglesias", "189892902", 
				"l12222@mail.com", "Málaga", "Marbella", "123456789", "10100", 
				"Avenida de los ilustrísimos 81, portal A", "lucas", "12345");
		Map<String,List<ExpectedViolation>> ganaderoNeg6Violations = Stream.of(
				new ExpectedViolation("dni", "must match", "javax.validation.constraints.Pattern.message"))
				.collect(Collectors.groupingBy(ExpectedViolation::getProperyPath));
		
		Ganadero ganaderoNeg7 = GanaderoServiceTests.generateGanadero("Karlos", "Arguiñano", "telefono1", 
				"mail.com", "Sevilla", "Dos Hermanas", "12345678A", "123456789", 
				"Calle Antonio Mairena, 8", "antonio1", "12345");
		Map<String,List<ExpectedViolation>> ganaderoNeg7Violations = Stream.of(
				new ExpectedViolation("postalCode", "must match", "javax.validation.constraints.Pattern.message"),
				new ExpectedViolation("telephone", "digits", "javax.validation.constraints.Digits.message"),
				new ExpectedViolation("mail", "must be a well-formed email address", "javax.validation.constraints.Email.message"))
				.collect(Collectors.groupingBy(ExpectedViolation::getProperyPath));
		
		Ganadero ganaderoNeg8 = GanaderoServiceTests.generateGanadero("Karlos", "Arguiñano", "1234567890123456789", 
				"mail@domanin", "Sevilla", "Dos Hermanas", "12345678A", "123", 
				"Calle Antonio Mairena, 8", "antonio1", "12345");
		Map<String,List<ExpectedViolation>> ganaderoNeg8Violations = Stream.of(
				new ExpectedViolation("postalCode", "must match", "javax.validation.constraints.Pattern.message"),
				new ExpectedViolation("telephone", "digits", "javax.validation.constraints.Digits.message"))
				.collect(Collectors.groupingBy(ExpectedViolation::getProperyPath));
		
		res = Stream.of(
				Arguments.of(ganaderoNeg1, ganaderoNeg1Violations), 
				Arguments.of(ganaderoNeg2, ganaderoNeg2Violations),
				Arguments.of(ganaderoNeg3, ganaderoNeg3Violations),
				Arguments.of(ganaderoNeg4, ganaderoNeg4Violations),
				Arguments.of(ganaderoNeg5, ganaderoNeg5Violations),
				Arguments.of(ganaderoNeg6, ganaderoNeg6Violations),
				Arguments.of(ganaderoNeg7, ganaderoNeg7Violations),
				Arguments.of(ganaderoNeg8, ganaderoNeg8Violations));
		
		return res;
	}
	
private static Stream<Ganadero> positiveTestGanaderoData(){
		
		Stream<Ganadero> res;
		
		Ganadero ganaderoPos1 = GanaderoServiceTests.generateGanadero("Julián", "Banderas", "0123456789", 
				"anton@mail.com", "Sevilla", "Dos Hermanas", "12345678A", "41345", 
				"Calle Antonio Mairena, 8", "antonio1", "12345");
		Ganadero ganaderoPos2 = GanaderoServiceTests.generateGanadero("Niño", "Banderas Iglesias", "0123456789", 
				"ninon@domain", "Extremadura", "El gasco", "87654321B", "24674", 
				"Luis Manuel Andrade, 8. Piso 8-B", "nino1", "12345");
		Ganadero ganaderoPos3 = GanaderoServiceTests.generateGanadero("Laura", "Contreras Medina", "678256143", 
				"laura@mail.es", "Extremadura", "Cáceres", "12344321G", "67254", 
				"Aviación, 8", "laura", "12345");
		Ganadero ganaderoPos4 = GanaderoServiceTests.generateGanadero("Clara", "Sánchez Panza", "675245134", 
				"clarita83@mail.es", "Extremadura", "Plascencia", "12344321X", "67254", 
				"Reina Mercedes, 9", "clasanpan", "12345");
		Ganadero ganaderoPos5 = GanaderoServiceTests.generateGanadero("Marta", "Sánchez Panza", "675245134", 
				"marta.sanchez_16@mail.es", "Extremadura", "Plascencia", "12344321K", "67254", 
				"Reina Mercedes, 9", "martinina", "12345");
		
		res = Stream.of(ganaderoPos1, ganaderoPos2, ganaderoPos3, ganaderoPos4, ganaderoPos5);
		
		return res;
	}

	@ParameterizedTest()
	@MethodSource("negativeTestGanaderoData")
	void negativeTestsGanadero(Ganadero ganadero, 
			Map<String,List<ExpectedViolation>> expectedConstraintViolations) {
		
		/*1. Arrange
		 *	Inicializamos el validador*/
		LocaleContextHolder.setLocale(Locale.ENGLISH);

		Validator validator = createValidator();
		/*2. Act
		 *	Validamos el Ganadero que llega como parametro*/
		Set<ConstraintViolation<Ganadero>> constraintViolations = validator.validate(ganadero);
		
		/*3. Assert
		 *	Hacemos las comprobaciones pertinentes*/
		
		//--- 3.1 Comprobamos el numero de restricciones recibidas es el mismo que el esperado
		Integer expectedConstraintViolationCount = expectedConstraintViolations.values().stream()
				.mapToInt(List<ExpectedViolation>::size).sum();
		
		if(expectedConstraintViolationCount!=constraintViolations.size()) {
			System.out.println("==============Unexpected ConstraintViolations at "+this.getClass()+" ================");
			System.out.println(constraintViolations.toString());
		}
		
		assertThat(expectedConstraintViolationCount).isEqualTo(constraintViolations.size());
		
		//--- 3.2 Para cada restriccion del validador, comprobamos que se esperaba
		for(ConstraintViolation<Ganadero> violation: constraintViolations) {
			
			//Seleccionamos la expectedViolation del Map con la que comparar la violation
			ExpectedViolation expectedViolation = ExpectedViolation.getCorrespondingExpectedViolation(expectedConstraintViolations, violation);
			
			//--- 3.2.1 Comprobamos que el property path es el esperado
			assertThat(violation.getPropertyPath().toString())
			.isEqualTo(expectedViolation.getProperyPath());
			
			//--- 3.2.2 Comprobamos que el message template es el esperado
			if (!expectedViolation.getMessageTemplate().isEmpty()) {
				assertThat(violation.getMessageTemplate())
				.isEqualTo("{"+expectedViolation.getMessageTemplate()+"}");
			}
			
			//--- 3.2.2 Comprobamos que el contenido del message es el esperado
			if (!expectedViolation.getMessage().isEmpty()) {
				assertThat(violation.getMessage())
				.contains(expectedViolation.getMessage());
			}
		}

	}
	
	@ParameterizedTest()
	@MethodSource("positiveTestGanaderoData")
	void positiveTestsGanadero(Ganadero ganadero) {
		
		/*1. Arrange
		 *	Inicializamos el validador*/
		LocaleContextHolder.setLocale(Locale.ENGLISH);

		Validator validator = createValidator();
		/*2. Act
		 *	Validamos el Ganadero que llega como parametro*/
		Set<ConstraintViolation<Ganadero>> constraintViolations = validator.validate(ganadero);
		
		/*3. Assert
		 *	Comprobamos que no se reciben errores*/
		assertThat(0).isEqualTo(constraintViolations.size());
	}
}
