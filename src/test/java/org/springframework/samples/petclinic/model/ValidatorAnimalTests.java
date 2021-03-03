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
import org.springframework.samples.petclinic.service.AnimalServiceTests;
import org.springframework.samples.petclinic.service.TipoGanadoServiceTests;

public class ValidatorAnimalTests {
	
	private static Map<String, TiposGanado> tiposGanadoMap = TipoGanadoServiceTests.defaultTiposGanado();
	
	private static Stream<Animal> positiveTestsAnimalesSinLoteData(){
		Stream<Animal> res;
		ExplotacionGanadera explotacion = new ExplotacionGanadera();
		
		Animal animalPos1 = AnimalServiceTests.generateAnimal("POR09809", "2020/02/01", "2020/02/20", null, 
				TipoSexo.Macho, tiposGanadoMap.get("Porcino"), explotacion, null);
		Animal animalPos2 = AnimalServiceTests.generateAnimal("C09809", "2020/01/01", "2020/01/15", null, 
				TipoSexo.Hembra, tiposGanadoMap.get("Caprino"), explotacion, null);
		Animal animalPos3 = AnimalServiceTests.generateAnimal("1", "2014/01/01", "2014/01/15", "2014/01/15", 
				TipoSexo.Hembra, tiposGanadoMap.get("Ovino"), explotacion, null);
		Animal animalPos4 = AnimalServiceTests.generateAnimal("VAC-1098309727", "2016/08/01", "2016/08/20", "2017/01/20", 
				TipoSexo.Hembra, tiposGanadoMap.get("Vacuno"), explotacion, null);
		Animal animalPos5 = AnimalServiceTests.generateAnimal("AS-LCNKOEJ", "2018/11/29", "2018/12/01", "2020/01/20", 
				TipoSexo.Hembra, tiposGanadoMap.get("Asnal"), explotacion, null);
		
		res = Stream.of(animalPos1, animalPos2, animalPos3, animalPos4, animalPos5);
		return res;
	}
	
	private static Stream<Animal> positiveTestsAnimalesConLoteData(){
		Stream<Animal> res;
		ExplotacionGanadera explotacion = new ExplotacionGanadera();
		Lote lote = new Lote();
		
		Animal animalPos1 = AnimalServiceTests.generateAnimal("POR09809", "2020/02/01", "2020/02/20", null, 
				TipoSexo.Macho, tiposGanadoMap.get("Porcino"), explotacion, lote);
		Animal animalPos2 = AnimalServiceTests.generateAnimal("C09809", "2020/01/01", "2020/01/15", null, 
				TipoSexo.Hembra, tiposGanadoMap.get("Caprino"), explotacion, lote);
		Animal animalPos3 = AnimalServiceTests.generateAnimal("1", "2020/01/01", "2014/01/15", "2014/01/15", 
				TipoSexo.Hembra, tiposGanadoMap.get("Ovino"), explotacion, lote);
		
		res = Stream.of(animalPos1, animalPos2, animalPos3);
		return res;
	}
	
	private static Stream<Arguments> negativeTestAnimalesSinLoteData(){
		Stream<Arguments> res;
		ExplotacionGanadera explotacion = new ExplotacionGanadera();
		
		Animal animalNeg1 = AnimalServiceTests.generateAnimal("", "2020/02/01", "2020/02/20", null, 
				TipoSexo.Macho, tiposGanadoMap.get("Avicola"), explotacion, null);
		Map<String,List<ExpectedViolation>> animalNeg1Violations= Stream.of(
				new ExpectedViolation("identificadorAnimal", "must not be empty", "javax.validation.constraints.NotEmpty.message"))
				.collect(Collectors.groupingBy(ExpectedViolation::getProperyPath));
		
		Animal animalNeg2 = AnimalServiceTests.generateAnimal("EQ01029872", null, null, "2020/02/01", 
				TipoSexo.Hembra, tiposGanadoMap.get("Equino"), explotacion, null);
		Map<String,List<ExpectedViolation>> animalNeg2Violations= Stream.of(
				new ExpectedViolation("fechaNacimiento", "must not be null", "javax.validation.constraints.NotNull.message"),
				new ExpectedViolation("fechaIdentificacion", "must not be null", "javax.validation.constraints.NotNull.message"))
				.collect(Collectors.groupingBy(ExpectedViolation::getProperyPath));
				
		Animal animalNeg3 = AnimalServiceTests.generateAnimal("EQ01029872", "2020/02/01", "2020/02/20", "2020/02/01", 
				null, null, explotacion, null);
		Map<String,List<ExpectedViolation>> animalNeg3Violations= Stream.of(
				new ExpectedViolation("sexo", "must not be null", "javax.validation.constraints.NotNull.message"))
				.collect(Collectors.groupingBy(ExpectedViolation::getProperyPath));
		
		Animal animalNeg4 = AnimalServiceTests.generateAnimal("EQ01029872", "2022/02/01", "2022/02/20", "2022/02/01", 
				TipoSexo.Hembra, tiposGanadoMap.get("Equino"), explotacion, null);
		animalNeg4.setComprado(true);
		animalNeg4.setProcedencia("Carmona");
		Map<String,List<ExpectedViolation>> animalNeg4Violations= Stream.of(
				new ExpectedViolation("fechaNacimiento", "must be a past date", "javax.validation.constraints.Past.message"),
				new ExpectedViolation("fechaIdentificacion", "must be a past date", "javax.validation.constraints.Past.message"),
				new ExpectedViolation("fechaEntrada", "must be a past date", "javax.validation.constraints.Past.message"))
				.collect(Collectors.groupingBy(ExpectedViolation::getProperyPath));
		
		Animal animalNeg5 = AnimalServiceTests.generateAnimal("", null, null, null, 
				null, null, null, null);
		Map<String,List<ExpectedViolation>> animalNeg5Violations= Stream.of(
				new ExpectedViolation("identificadorAnimal", "must not be empty", "javax.validation.constraints.NotEmpty.message"),
				new ExpectedViolation("fechaNacimiento", "must not be null", "javax.validation.constraints.NotNull.message"),
				new ExpectedViolation("fechaIdentificacion", "must not be null", "javax.validation.constraints.NotNull.message"),
				new ExpectedViolation("sexo", "must not be null", "javax.validation.constraints.NotNull.message"),
				new ExpectedViolation("explotacionGanadera", "must not be null", "javax.validation.constraints.NotNull.message"))
				.collect(Collectors.groupingBy(ExpectedViolation::getProperyPath));

		
		res = Stream.of(
				Arguments.of(animalNeg1,animalNeg1Violations),
				Arguments.of(animalNeg2,animalNeg2Violations),
				Arguments.of(animalNeg3,animalNeg3Violations),
				Arguments.of(animalNeg4,animalNeg4Violations));
		
		return res;
	}
	
	@ParameterizedTest()
	@MethodSource("positiveTestsAnimalesSinLoteData")
	void positiveTestsAnimalesSinLote(Animal animal) {
		
		GenerateValidatorTest.validatorPositiveTests(animal);
	}
	
	@ParameterizedTest()
	@MethodSource("positiveTestsAnimalesConLoteData")
	void positiveTestsAnimalesConLote(Animal animal) {
		
		GenerateValidatorTest.validatorPositiveTests(animal);
	}
	
	@ParameterizedTest()
	@MethodSource("negativeTestAnimalesSinLoteData")
	void negativeTestsAnimalesSinLote(Animal animal, Map<String,
			List<ExpectedViolation>> expectedConstraintViolations) {
		
		GenerateValidatorTest.validatorNegativeTests(animal, expectedConstraintViolations);
	}	

}
