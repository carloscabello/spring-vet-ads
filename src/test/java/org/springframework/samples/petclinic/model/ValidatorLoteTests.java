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
import org.springframework.samples.petclinic.service.LoteServiceTests;
import org.springframework.samples.petclinic.service.TipoGanadoServiceTests;

public class ValidatorLoteTests {
	
	private static Map<String, TiposGanado> tiposGanadoMap = TipoGanadoServiceTests.defaultTiposGanado();
	
	private static Stream<Lote> positiveTestsLotesData(){
		Stream<Lote> res;
		ExplotacionGanadera explotacion = new ExplotacionGanadera();
		
		
		Lote lotePos1 = LoteServiceTests.generateLote("L012374", false, explotacion, tiposGanadoMap.get("Porcino"));
		Lote lotePos2 = LoteServiceTests.generateLote("12983102", true, explotacion, tiposGanadoMap.get("Ovino"));
		Lote lotePos3 = LoteServiceTests.generateLote("ABC-123", null, explotacion, tiposGanadoMap.get("Caprino"));
		
		res = Stream.of(lotePos1, lotePos2, lotePos3);
		return res;
	}
	
	private static Stream<Arguments> negativeTestsLotesData(){
		Stream<Arguments> res;
		ExplotacionGanadera explotacion = new ExplotacionGanadera();
		
		Lote loteNeg1 = LoteServiceTests.generateLote("", false, explotacion, tiposGanadoMap.get("Porcino"));
		Map<String,List<ExpectedViolation>> loteNeg1Violations= Stream.of(
				new ExpectedViolation("identificadorLote", "must not be empty", "javax.validation.constraints.NotEmpty.message"))
				.collect(Collectors.groupingBy(ExpectedViolation::getProperyPath));
		
		Lote loteNeg2 = LoteServiceTests.generateLote(null, true, explotacion, tiposGanadoMap.get("Porcino"));
		Map<String,List<ExpectedViolation>> loteNeg2Violations= Stream.of(
				new ExpectedViolation("identificadorLote", "must not be null", "javax.validation.constraints.NotNull.message"))
				.collect(Collectors.groupingBy(ExpectedViolation::getProperyPath));
		
		Lote loteNeg3 = LoteServiceTests.generateLote("L012374", true, null, null);
		Map<String,List<ExpectedViolation>> loteNeg3Violations= Stream.of(
				new ExpectedViolation("explotacionGanadera", "must not be null", "javax.validation.constraints.NotNull.message"),
				new ExpectedViolation("tipoGanado", "must not be null", "javax.validation.constraints.NotNull.message"))
				.collect(Collectors.groupingBy(ExpectedViolation::getProperyPath));
		
		Lote loteNeg4 = LoteServiceTests.generateLote("L012374", true, null, null);
		Map<String,List<ExpectedViolation>> loteNeg4Violations= Stream.of(
				new ExpectedViolation("tipoGanado", "must not be null", "javax.validation.constraints.NotNull.message"))
				.collect(Collectors.groupingBy(ExpectedViolation::getProperyPath));
		
		Lote loteNeg5 = LoteServiceTests.generateLote(null, true, null, null);
		Map<String,List<ExpectedViolation>> loteNeg5Violations= Stream.of(
				new ExpectedViolation("identificadorLote", "must not be null", "javax.validation.constraints.NotNull.message"),
				new ExpectedViolation("tipoGanado", "must not be null", "javax.validation.constraints.NotNull.message"))
				.collect(Collectors.groupingBy(ExpectedViolation::getProperyPath));
		
		res = Stream.of(
				Arguments.of(loteNeg1,loteNeg1Violations));
		return res;
	}

	@ParameterizedTest()
	@MethodSource("positiveTestsLotesData")
	void positiveTestsLotes(Lote lote) {
		
		GenerateValidatorTest.validatorPositiveTests(lote);
	}
	
	@ParameterizedTest()
	@MethodSource("negativeTestsLotesData")
	void negativeTestsLotes(Lote lote, Map<String,
			List<ExpectedViolation>> expectedConstraintViolations) {
		
		GenerateValidatorTest.validatorNegativeTests(lote, expectedConstraintViolations);
	}	
}
