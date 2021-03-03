package org.springframework.samples.petclinic.model;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.samples.petclinic.model.utils.ExpectedViolation;
import org.springframework.samples.petclinic.model.utils.GenerateValidatorTest;
import org.springframework.samples.petclinic.service.ExplotacionServiceTests;

public class ValidatorExplotacionTests {
	
	
	private static Stream<ExplotacionGanadera> positiveTestExplotacionData(){
		Stream<ExplotacionGanadera> res;
		Ganadero ganadero = new Ganadero();
		
		ExplotacionGanadera explotacionPos1 = ExplotacionServiceTests.generateExplotacion("53728343", "Granada", 
				false, "Moncayo", ganadero);
		ExplotacionGanadera explotacionPos2 = ExplotacionServiceTests.generateExplotacion("123456789", "Lebrija", 
				false, "Finca El gallo peleón", ganadero);
		ExplotacionGanadera explotacionPos3 = ExplotacionServiceTests.generateExplotacion("58728-012", "Carmona", 
				false, "123", ganadero);
		ExplotacionGanadera explotacionPos4 = ExplotacionServiceTests.generateExplotacion("18792837", "Écija", 
				false, "1234567890123456789012345678901234567890123456789", ganadero);
		ExplotacionGanadera explotacionPos5 = ExplotacionServiceTests.generateExplotacion("18792837", "Écija", 
				null, "1234567890123456789012345678901234567890123456789", ganadero);
		
		res = Stream.of(explotacionPos1,explotacionPos2,explotacionPos3, explotacionPos4, explotacionPos5);
		
		return res;
	}
	
	private static Stream<Arguments> negativeTestExplotacionData(){
		Stream<Arguments> res;
		Ganadero ganadero = new Ganadero();
		
		ExplotacionGanadera explotacionNeg1 = ExplotacionServiceTests.generateExplotacion("", "", 
				false, "El Moncayo", ganadero);
		Map<String,List<ExpectedViolation>> explotacionNeg1Violations = Stream.of(
				new ExpectedViolation("numeroRegistro", "must not be empty", "javax.validation.constraints.NotEmpty.message"),
				new ExpectedViolation("terminoMunicipal", "must not be empty", "javax.validation.constraints.NotEmpty.message"))
				.collect(Collectors.groupingBy(ExpectedViolation::getProperyPath));
		
		ExplotacionGanadera explotacionNeg2 = ExplotacionServiceTests.generateExplotacion("", "", 
				false, "", ganadero);
		Map<String,List<ExpectedViolation>> explotacionNeg2Violations = Stream.of(
				new ExpectedViolation("numeroRegistro", "must not be empty", "javax.validation.constraints.NotEmpty.message"),
				new ExpectedViolation("terminoMunicipal", "must not be empty", "javax.validation.constraints.NotEmpty.message"),
				new ExpectedViolation("name", "size must be between", "javax.validation.constraints.Size.message"))
				.collect(Collectors.groupingBy(ExpectedViolation::getProperyPath));
		
		ExplotacionGanadera explotacionNeg3 = ExplotacionServiceTests.generateExplotacion(null, null, 
				false, "El Moncayo", ganadero);
		Map<String,List<ExpectedViolation>> explotacionNeg3Violations = Stream.of(
				new ExpectedViolation("numeroRegistro", "must not be null", "javax.validation.constraints.NotNull.message"),
				new ExpectedViolation("terminoMunicipal", "must not be null", "javax.validation.constraints.NotNull.message"),
				new ExpectedViolation("numeroRegistro", "must not be empty", "javax.validation.constraints.NotEmpty.message"),
				new ExpectedViolation("terminoMunicipal", "must not be empty", "javax.validation.constraints.NotEmpty.message"))
				.collect(Collectors.groupingBy(ExpectedViolation::getProperyPath));
		
		ExplotacionGanadera explotacionNeg4 = ExplotacionServiceTests.generateExplotacion("53728343", "Carmona", 
				false, "EX", ganadero);
		Map<String,List<ExpectedViolation>> explotacionNeg4Violations = Stream.of(
				new ExpectedViolation("name", "size must be between", "javax.validation.constraints.Size.message"))
				.collect(Collectors.groupingBy(ExpectedViolation::getProperyPath));
		
		ExplotacionGanadera explotacionNeg5 = ExplotacionServiceTests.generateExplotacion("53728343", "Carmona", 
				false, "123456789012345678901234567890123456789012345678901", ganadero);
		Map<String,List<ExpectedViolation>> explotacionNeg5Violations = Stream.of(
				new ExpectedViolation("name", "size must be between", "javax.validation.constraints.Size.message"))
				.collect(Collectors.groupingBy(ExpectedViolation::getProperyPath));
		
		ExplotacionGanadera explotacionNeg6 = ExplotacionServiceTests.generateExplotacion("", "", 
				false, "", null);
		Map<String,List<ExpectedViolation>> explotacionNeg6Violations = Stream.of(
				new ExpectedViolation("numeroRegistro", "must not be empty", "javax.validation.constraints.NotEmpty.message"),
				new ExpectedViolation("terminoMunicipal", "must not be empty", "javax.validation.constraints.NotEmpty.message"),
				new ExpectedViolation("name", "size must be between", "javax.validation.constraints.Size.message"))
				.collect(Collectors.groupingBy(ExpectedViolation::getProperyPath));
		
		res = Stream.of(
				Arguments.of(explotacionNeg1, explotacionNeg1Violations),
				Arguments.of(explotacionNeg2, explotacionNeg2Violations),
				Arguments.of(explotacionNeg3, explotacionNeg3Violations),
				Arguments.of(explotacionNeg4, explotacionNeg4Violations),
				Arguments.of(explotacionNeg5, explotacionNeg5Violations),
				Arguments.of(explotacionNeg6, explotacionNeg6Violations));
		
		return res;
	}

	
	@ParameterizedTest()
	@MethodSource("positiveTestExplotacionData")
	void positiveTestsExplotacion(ExplotacionGanadera explotacion) {
		
		GenerateValidatorTest.validatorPositiveTests(explotacion);
	}
	
	@ParameterizedTest()
	@MethodSource("negativeTestExplotacionData")
	void negativeTestsExplotacion(ExplotacionGanadera explotacion, Map<String,
			List<ExpectedViolation>> expectedConstraintViolations) {
		
		GenerateValidatorTest.validatorNegativeTests(explotacion, expectedConstraintViolations);
	}

}
