package org.springframework.samples.petclinic.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.samples.petclinic.model.utils.ExpectedViolation;
import org.springframework.samples.petclinic.service.TipoGanadoServiceTests;
import org.springframework.samples.petclinic.service.VeterinarioServiceTests;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

public class ValidatorVeterinarioTests {
	
	private static Map<String, TiposGanado> tiposGanadoMap = TipoGanadoServiceTests.defaultTiposGanado();
	
	private Validator createValidator() {
		LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
		localValidatorFactoryBean.afterPropertiesSet();
		return localValidatorFactoryBean;
	}
	
	private static Stream<Arguments> negativeTestVeterinarioData(){
		Stream<Arguments> res;
		
		List<TiposGanado> especialidadesNeg1 = Stream.of(
				tiposGanadoMap.get("Porcino"),
				tiposGanadoMap.get("Ovino"))
				.collect(Collectors.toList());
		Veterinario veterinarioNeg1 = VeterinarioServiceTests.generateVeterinario("", "", "0123456789", 
				"anton@mail.com", "Sevilla", "Dos Hermanas", "12345678A", especialidadesNeg1, true, "julian", "12345");		
		Map<String,List<ExpectedViolation>> veterinarioNeg1Violations= Stream.of(
				new ExpectedViolation("firstName", "must not be empty", "javax.validation.constraints.NotEmpty.message"),
				new ExpectedViolation("lastName", "must not be empty", "javax.validation.constraints.NotEmpty.message"))
				.collect(Collectors.groupingBy(ExpectedViolation::getProperyPath));
		
		List<TiposGanado> especialidadesNeg2 = Stream.of(
				tiposGanadoMap.get("Vacuno"),
				tiposGanadoMap.get("Caprino"))
				.collect(Collectors.toList());
		Veterinario veterinarioNeg2 = VeterinarioServiceTests.generateVeterinario("Marco", "Aurelio Arkantos", "", 
				"", "Sevilla", "Dos Hermanas", "12345678A", especialidadesNeg2, true, "julian", "12345");		
		Map<String,List<ExpectedViolation>> veterinarioNeg2Violations= Stream.of(
				new ExpectedViolation("telephone", "digits", "javax.validation.constraints.Digits.message"),
				new ExpectedViolation("telephone", "must not be empty", "javax.validation.constraints.NotEmpty.message"),
				new ExpectedViolation("mail", "must not be empty", "javax.validation.constraints.NotEmpty.message"))
				.collect(Collectors.groupingBy(ExpectedViolation::getProperyPath));
		
		List<TiposGanado> especialidadesNeg3 = null;
		Veterinario veterinarioNeg3 = VeterinarioServiceTests.generateVeterinario("Marco", "Aurelio Arkantos", "0123456789123123", 
				"marco@mail", "Sevilla", "Dos Hermanas", "12345678A", especialidadesNeg3, true, "julian", "12345");		
		Map<String,List<ExpectedViolation>> veterinarioNeg3Violations= Stream.of(
				new ExpectedViolation("telephone", "numeric value out of bounds", "javax.validation.constraints.Digits.message"),
				new ExpectedViolation("tiposGanado", "must not be null", "javax.validation.constraints.NotNull.message"))
				.collect(Collectors.groupingBy(ExpectedViolation::getProperyPath));
		
		List<TiposGanado> especialidadesNeg4 = new ArrayList<TiposGanado>();
		Veterinario veterinarioNeg4 = VeterinarioServiceTests.generateVeterinario("Marco", "Aurelio Arkantos", "012", 
				"marco@mail", "", "", "12345678A", especialidadesNeg4, true, "julian", "12345");		
		Map<String,List<ExpectedViolation>> veterinarioNeg4Violations= Stream.of(
				new ExpectedViolation("province", "must not be empty", "javax.validation.constraints.NotEmpty.message"),
				new ExpectedViolation("city", "must not be empty", "javax.validation.constraints.NotEmpty.message"))
				.collect(Collectors.groupingBy(ExpectedViolation::getProperyPath));
		
		List<TiposGanado> especialidadesNeg5 = new ArrayList<TiposGanado>();
		Veterinario veterinarioNeg5 = VeterinarioServiceTests.generateVeterinario("Laura", "Contreras Medina", "678256143", 
				"laura@mail.es", "Sevilla", "Gines", "012345678", especialidadesNeg5, false, "carlo", "54321");
		Map<String,List<ExpectedViolation>> veterinarioNeg5Violations= Stream.of(
				new ExpectedViolation("dni", "must match", "javax.validation.constraints.Pattern.message"))
				.collect(Collectors.groupingBy(ExpectedViolation::getProperyPath));
		
		List<TiposGanado> especialidadesNeg6 = new ArrayList<TiposGanado>();
		Veterinario veterinarioNeg6 = VeterinarioServiceTests.generateVeterinario("Laura", "Contreras Medina", "ABCDEFHIJ", 
				"mail", "Sevilla", "Gines", "01234567E", especialidadesNeg5, false, "carlo", "54321");
		Map<String,List<ExpectedViolation>> veterinarioNeg6Violations= Stream.of(
				new ExpectedViolation("mail", "must be a well-formed email address", "javax.validation.constraints.Email.message"),
				new ExpectedViolation("telephone", "digits", "javax.validation.constraints.Digits.message"))
				.collect(Collectors.groupingBy(ExpectedViolation::getProperyPath));
		
		List<TiposGanado> especialidadesNeg7 = new ArrayList<TiposGanado>(tiposGanadoMap.values());
		Veterinario veterinarioNeg7 = VeterinarioServiceTests.generateVeterinario("", "", "", 
				"anton@mail.com", "Sevilla", "Dos Hermanas", "badDNI", especialidadesNeg7, true, "julian", "12345");
		Map<String,List<ExpectedViolation>> veterinarioNeg7Violations= Stream.of(
				new ExpectedViolation("firstName", "must not be empty", "javax.validation.constraints.NotEmpty.message"),
				new ExpectedViolation("lastName", "must not be empty", "javax.validation.constraints.NotEmpty.message"),
				new ExpectedViolation("telephone", "digits", "javax.validation.constraints.Digits.message"),
				new ExpectedViolation("telephone", "must not be empty", "javax.validation.constraints.NotEmpty.message"),
				new ExpectedViolation("dni", "must match", "javax.validation.constraints.Pattern.message"))
				.collect(Collectors.groupingBy(ExpectedViolation::getProperyPath));
		
		Veterinario veterinarioNeg8 = VeterinarioServiceTests.generateVeterinario("", "", "", 
				"", "", "", "", null, true, "julian", "12345");
		Map<String,List<ExpectedViolation>> veterinarioNeg8Violations= Stream.of(
				new ExpectedViolation("firstName", "must not be empty", "javax.validation.constraints.NotEmpty.message"),
				new ExpectedViolation("lastName", "must not be empty", "javax.validation.constraints.NotEmpty.message"),
				new ExpectedViolation("telephone", "must not be empty", "javax.validation.constraints.NotEmpty.message"),
				new ExpectedViolation("telephone", "digits", "javax.validation.constraints.Digits.message"),
				new ExpectedViolation("mail", "must not be empty", "javax.validation.constraints.NotEmpty.message"),
				new ExpectedViolation("province", "must not be empty", "javax.validation.constraints.NotEmpty.message"),
				new ExpectedViolation("city", "must not be empty", "javax.validation.constraints.NotEmpty.message"),
				new ExpectedViolation("dni", "must match", "javax.validation.constraints.Pattern.message"),
				new ExpectedViolation("dni", "must not be empty", "javax.validation.constraints.NotEmpty.message"),
				new ExpectedViolation("tiposGanado", "must not be null", "javax.validation.constraints.NotNull.message"))
				.collect(Collectors.groupingBy(ExpectedViolation::getProperyPath));
		
		
		res = Stream.of(
				Arguments.of(veterinarioNeg1, veterinarioNeg1Violations),
				Arguments.of(veterinarioNeg2, veterinarioNeg2Violations),
				Arguments.of(veterinarioNeg3, veterinarioNeg3Violations),
				Arguments.of(veterinarioNeg4, veterinarioNeg4Violations),
				Arguments.of(veterinarioNeg5, veterinarioNeg5Violations),
				Arguments.of(veterinarioNeg6, veterinarioNeg6Violations),
				Arguments.of(veterinarioNeg7, veterinarioNeg7Violations),
				Arguments.of(veterinarioNeg8, veterinarioNeg8Violations));
		return res;
	}
	
	private static Stream<Veterinario> positiveTestVeterinarioData(){
		
		Stream<Veterinario> res;
		
		List<TiposGanado> especialidadesPos1 = Stream.of(
				tiposGanadoMap.get("Porcino"),
				tiposGanadoMap.get("Ovino"))
				.collect(Collectors.toList());
		Veterinario veterinarioPos1 = VeterinarioServiceTests.generateVeterinario("Julián", "Banderas", "0123456789", 
				"anton@mail.com", "Sevilla", "Dos Hermanas", "12345678A", especialidadesPos1, true, "julian", "12345");
		
		List<TiposGanado> especialidadesPos2 = Stream.of(
				tiposGanadoMap.get("Vacuno"),
				tiposGanadoMap.get("Caprino"))
				.collect(Collectors.toList());
		Veterinario veterinarioPos2 = VeterinarioServiceTests.generateVeterinario("Carlo", "Magno Alexandrio", "987654321", 
				"carlo@mail.com", "Extremadura", "El Gasco", "12344321G", especialidadesPos2, false, "carlo", "54321");
		
		List<TiposGanado> especialidadesPos3 = Stream.of(
				tiposGanadoMap.get("Equino"),
				tiposGanadoMap.get("Asnal"),
				tiposGanadoMap.get("Avicola"))
				.collect(Collectors.toList());
		Veterinario veterinarioPos3 = VeterinarioServiceTests.generateVeterinario("Tulio", "Iglesias Romero", "987654321", 
				"iglesias@mail", "Extremadura", "El Gasco", "12344321X", especialidadesPos3, true, "carlo", "54321");
		
		List<TiposGanado> especialidadesPos4 = new ArrayList<TiposGanado>();
		Veterinario veterinarioPos4 = VeterinarioServiceTests.generateVeterinario("Laura", "Contreras Medina", "678256143", 
				"marta.sanchez_16@mail.es", "Sevilla", "Gines", "12344321K", especialidadesPos4, false, "carlo", "54321");
		
		List<TiposGanado> especialidadesPos5 = new ArrayList<TiposGanado>(tiposGanadoMap.values());
		Veterinario veterinarioPos5 = VeterinarioServiceTests.generateVeterinario("Clara", "Sánchez Panza", "0123456789", 
				"marta.sanchez_16@mail.es", "Sevilla", "Gines", "12344321K", especialidadesPos5, true, "carlo", "54321");
		
		res = Stream.of(veterinarioPos1, veterinarioPos2, veterinarioPos3, veterinarioPos4, veterinarioPos5);
		
		return res;
	}
	
	@ParameterizedTest()
	@MethodSource("positiveTestVeterinarioData")
	void positiveTestsGanadero(Veterinario veterinario) {
		
		/*1. Arrange
		 *	Inicializamos el validador*/
		LocaleContextHolder.setLocale(Locale.ENGLISH);

		Validator validator = createValidator();
		/*2. Act
		 *	Validamos el Ganadero que llega como parametro*/
		Set<ConstraintViolation<Veterinario>> constraintViolations = validator.validate(veterinario);
		
		/*3. Assert
		 *	Comprobamos que no se reciben errores*/
		assertThat(0).isEqualTo(constraintViolations.size());
	}
	
	@ParameterizedTest()
	@MethodSource("negativeTestVeterinarioData")
	void negativeTestsVeterinario(Veterinario veterinario, 
			Map<String,List<ExpectedViolation>> expectedConstraintViolations) {
		
		/*1. Arrange
		 *	Inicializamos el validador*/
		LocaleContextHolder.setLocale(Locale.ENGLISH);

		Validator validator = createValidator();
		/*2. Act
		 *	Validamos el Veterinario que llega como parametro*/
		Set<ConstraintViolation<Veterinario>> constraintViolations = validator.validate(veterinario);
		
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
		for(ConstraintViolation<Veterinario> violation: constraintViolations) {
			
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

}
