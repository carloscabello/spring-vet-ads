
package org.springframework.samples.petclinic.model;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.samples.petclinic.forms.RecetaForm;
import org.springframework.samples.petclinic.model.utils.ExpectedViolation;
import org.springframework.samples.petclinic.model.utils.GenerateValidatorTest;

public class ValidatorRecetaFormTests {

	private static Stream<RecetaForm> positiveTestRecetaFormData() throws ParseException {
		Stream<RecetaForm> res;

		List<Integer> productoId1 = new ArrayList<>();
		productoId1.add(1);
		List<Integer> cantidad1 = new ArrayList<>();
		cantidad1.add(1);
		RecetaForm recetaFormPos1 = ValidatorRecetaFormTests.generateRecetaForm("Para dar a las cabras todos los días durante una semana", 10, productoId1, cantidad1);

		List<Integer> productoId2 = new ArrayList<>();
		productoId2.add(1);
		productoId2.add(4);
		List<Integer> cantidad2 = new ArrayList<>();
		cantidad2.add(2);
		cantidad2.add(3);
		RecetaForm recetaFormPos2 = ValidatorRecetaFormTests.generateRecetaForm("Medicamentos para dar a las ovejas. El primero cada 2 días y el segundo cada día. Durante 2 semanas", 10, productoId2, cantidad2);

		List<Integer> productoId3 = new ArrayList<>();
		productoId3.add(3);
		List<Integer> cantidad3 = new ArrayList<>();
		cantidad3.add(1);
		RecetaForm recetaFormPos3 = ValidatorRecetaFormTests.generateRecetaForm("Dar a los cerdos durante 2 días, 1 vez por día", 10, productoId3, cantidad3);

		res = Stream.of(recetaFormPos1, recetaFormPos2, recetaFormPos3);
		return res;
	}

	@ParameterizedTest()
	@MethodSource("positiveTestRecetaFormData")
	void positiveTestsCita(final RecetaForm recetaForm) {

		GenerateValidatorTest.validatorPositiveTests(recetaForm);

	}

	private static Stream<Arguments> negativeTestRecetaFormData() throws ParseException {
		Stream<Arguments> res;

		List<Integer> productoId1 = new ArrayList<>();
		productoId1.add(1);
		List<Integer> cantidad1 = new ArrayList<>();
		cantidad1.add(1);
		RecetaForm recetaFormNeg1 = ValidatorRecetaFormTests.generateRecetaForm( null, 10, productoId1,cantidad1);
		Map<String, List<ExpectedViolation>> recetaFormNeg1Violations = Stream
			.of(new ExpectedViolation("descripcion", "must not be blank", "javax.validation.constraints.NotBlank.message"), new ExpectedViolation("descripcion", "must not be null", "javax.validation.constraints.NotNull.message"))
			.collect(Collectors.groupingBy(ExpectedViolation::getProperyPath));

		RecetaForm recetaFormNeg2 = ValidatorRecetaFormTests.generateRecetaForm("", 10, productoId1,cantidad1);
		Map<String, List<ExpectedViolation>> recetaFormNeg2Violations = Stream
			.of(new ExpectedViolation("descripcion", "must not be blank", "javax.validation.constraints.NotBlank.message"))
			.collect(Collectors.groupingBy(ExpectedViolation::getProperyPath));

		res = Stream.of(
			Arguments.of(recetaFormNeg1, recetaFormNeg1Violations),
			Arguments.of(recetaFormNeg2, recetaFormNeg2Violations));

		return res;
	}
	
	@ParameterizedTest()
	@MethodSource("negativeTestRecetaFormData")
	void negativeTestsCita(final RecetaForm recetaForm, final Map<String, List<ExpectedViolation>> expectedConstraintViolations) {

		GenerateValidatorTest.validatorNegativeTests(recetaForm, expectedConstraintViolations);

	}

	// ----- Métodos auxiliares

	// Método para generar una cita
	public static RecetaForm generateRecetaForm(final String descripcion, final Integer citaId, final List<Integer> productoId, final List<Integer> cantidad) {
		RecetaForm recetaForm = new RecetaForm();

		//Asignaciones cita
		recetaForm.setDescripcion(descripcion);
		recetaForm.setCitaId(citaId);
		recetaForm.setProductoId(productoId);
		recetaForm.setCantidad(cantidad);

		return recetaForm;
	}

}
