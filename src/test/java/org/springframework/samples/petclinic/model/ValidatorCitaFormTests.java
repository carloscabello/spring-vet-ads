
package org.springframework.samples.petclinic.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.validation.Validator;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.samples.petclinic.forms.CitaForm;
import org.springframework.samples.petclinic.model.utils.ExpectedViolation;
import org.springframework.samples.petclinic.model.utils.GenerateValidatorTest;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

public class ValidatorCitaFormTests {

	private static Stream<CitaForm> positiveTestCitaFormData() throws ParseException {
		Stream<CitaForm> res;

		CitaForm citaFormPos1 = ValidatorCitaFormTests.generateCitaForm(ValidatorCitaFormTests.newDateFormattedWithoutHour("2020/08/18"), "17:00", 2, "A unas cuantas de oveja se les cae el pelo solo", 2);

		CitaForm citaFormPos2 = ValidatorCitaFormTests.generateCitaForm(ValidatorCitaFormTests.newDateFormattedWithoutHour("2021/02/05"), "10:00", 1, "Mis cerdos necesitan una revisión. Los noto raros", 2);

		CitaForm citaFormPos3 = ValidatorCitaFormTests.generateCitaForm(ValidatorCitaFormTests.newDateFormattedWithoutHour("2020/01/01"), "08:00", 2, "Mis cabras no dan leche", 3);

		res = Stream.of(citaFormPos1, citaFormPos2, citaFormPos3);
		return res;
	}

	@ParameterizedTest()
	@MethodSource("positiveTestCitaFormData")
	void positiveTestsCita(final CitaForm citaForm) {

		GenerateValidatorTest.validatorPositiveTests(citaForm);

	}

	private static Stream<Arguments> negativeTestCitaFormData() throws ParseException {
		Stream<Arguments> res;

		CitaForm citaFormNeg1 = ValidatorCitaFormTests.generateCitaForm(null, null, 2, "A unas cuantas de oveja se les cae el pelo solo", 2);
		Map<String, List<ExpectedViolation>> citaFormNeg1Violations = Stream
			.of(new ExpectedViolation("fecha", "must not be null", "javax.validation.constraints.NotNull.message"), new ExpectedViolation("horaInicio", "must not be null", "javax.validation.constraints.NotNull.message"),
				new ExpectedViolation("horaInicio", "must not be blank", "javax.validation.constraints.NotBlank.message"))
			.collect(Collectors.groupingBy(ExpectedViolation::getProperyPath));

		CitaForm citaFormNeg2 = ValidatorCitaFormTests.generateCitaForm(ValidatorCitaFormTests.newDateFormattedWithoutHour("2020/10/03"), "", 2, "A unas cuantas de oveja se les cae el pelo solo", 2);
		Map<String, List<ExpectedViolation>> citaFormNeg2Violations = Stream
			.of(new ExpectedViolation("horaInicio", "must not be blank", "javax.validation.constraints.NotBlank.message"),new ExpectedViolation("horaInicio", "must match", "javax.validation.constraints.Pattern.message"))
			.collect(Collectors.groupingBy(ExpectedViolation::getProperyPath));

		CitaForm citaFormNeg3 = ValidatorCitaFormTests.generateCitaForm(ValidatorCitaFormTests.newDateFormattedWithoutHour("2020/11/03"), "1031299:00", null, null, 2);
		Map<String, List<ExpectedViolation>> citaFormNeg3Violations = Stream
			.of(new ExpectedViolation("horaInicio", "must match \"^(0|1)[0-9]:00$", "javax.validation.constraints.Pattern.message"), new ExpectedViolation("duracion", "must not be null", "javax.validation.constraints.NotNull.message"),
				new ExpectedViolation("motivo", "must not be null", "javax.validation.constraints.NotNull.message"), new ExpectedViolation("motivo", "must not be blank", "javax.validation.constraints.NotBlank.message"))
			.collect(Collectors.groupingBy(ExpectedViolation::getProperyPath));

		CitaForm citaFormNeg4 = ValidatorCitaFormTests.generateCitaForm(ValidatorCitaFormTests.newDateFormattedWithoutHour("2020/11/03"), "10:00", 0, "A unas cuantas de oveja se les cae el pelo solo", 2);
		Map<String, List<ExpectedViolation>> citaFormNeg4Violations = Stream
			.of(new ExpectedViolation("duracion", "must be between 1 and 6", "org.hibernate.validator.constraints.Range.message"))
			.collect(Collectors.groupingBy(ExpectedViolation::getProperyPath));

		CitaForm citaFormNeg5 = ValidatorCitaFormTests.generateCitaForm(ValidatorCitaFormTests.newDateFormattedWithoutHour("2020/11/03"), "10:00", 1, "", 2);
		Map<String, List<ExpectedViolation>> citaFormNeg5Violations = Stream
			.of(new ExpectedViolation("motivo", "must not be blank", "javax.validation.constraints.NotBlank.message"))
			.collect(Collectors.groupingBy(ExpectedViolation::getProperyPath));

		CitaForm citaFormNeg6 = ValidatorCitaFormTests.generateCitaForm(ValidatorCitaFormTests.newDateFormattedWithoutHour("2020/11/03"), "68291", 6, "", 2);
		Map<String, List<ExpectedViolation>> citaFormNeg6Violations = Stream
			.of(new ExpectedViolation("horaInicio", "must match \"^(0|1)[0-9]:00$", "javax.validation.constraints.Pattern.message"),new ExpectedViolation("motivo", "must not be blank", "javax.validation.constraints.NotBlank.message"))
			.collect(Collectors.groupingBy(ExpectedViolation::getProperyPath));

		CitaForm citaFormNeg7 = ValidatorCitaFormTests.generateCitaForm(ValidatorCitaFormTests.newDateFormattedWithoutHour("2020/11/03"), "68:21", 7, "", 2);
		Map<String, List<ExpectedViolation>> citaFormNeg7Violations = Stream
			.of(new ExpectedViolation("horaInicio", "must match \"^(0|1)[0-9]:00$", "javax.validation.constraints.Pattern.message"),new ExpectedViolation("duracion", "must be between 1 and 6", "org.hibernate.validator.constraints.Range.message"),
				new ExpectedViolation("motivo", "must not be blank", "javax.validation.constraints.NotBlank.message"))
			.collect(Collectors.groupingBy(ExpectedViolation::getProperyPath));

		res = Stream.of(
			Arguments.of(citaFormNeg1, citaFormNeg1Violations),
			Arguments.of(citaFormNeg2, citaFormNeg2Violations),
			Arguments.of(citaFormNeg3, citaFormNeg3Violations),
			Arguments.of(citaFormNeg4, citaFormNeg4Violations),
			Arguments.of(citaFormNeg5, citaFormNeg5Violations),
			Arguments.of(citaFormNeg6, citaFormNeg6Violations),
			Arguments.of(citaFormNeg7, citaFormNeg7Violations));

		return res;
	}

	@ParameterizedTest()
	@MethodSource("negativeTestCitaFormData")
	void negativeTestsCita(final CitaForm citaForm, final Map<String, List<ExpectedViolation>> expectedConstraintViolations) {

		GenerateValidatorTest.validatorNegativeTests(citaForm, expectedConstraintViolations);

	}

	// ----- Métodos auxiliares

	// Método para generar una cita
	public static CitaForm generateCitaForm(final Date fecha, final String horaInicio, final Integer duracion, final String motivo, final Integer contratoId) {
		CitaForm citaForm = new CitaForm();

		//Asignaciones cita
		citaForm.setFecha(fecha);
		citaForm.setHoraInicio(horaInicio);
		citaForm.setDuracion(duracion);
		citaForm.setMotivo(motivo);
		citaForm.setContratoId(contratoId);

		return citaForm;
	}

	public static Date newDateFormattedWithoutHour(final String fecha) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		return sdf.parse(fecha);
	}

}
