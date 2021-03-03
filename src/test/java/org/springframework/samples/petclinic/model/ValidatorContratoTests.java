
package org.springframework.samples.petclinic.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.samples.petclinic.model.utils.ExpectedViolation;
import org.springframework.samples.petclinic.model.utils.GenerateValidatorTest;
import org.springframework.samples.petclinic.service.ContratoServiceTests;
import org.springframework.samples.petclinic.service.GanaderoServiceTests;
import org.springframework.samples.petclinic.service.VeterinarioServiceTests;

public class ValidatorContratoTests {

	private static Stream<Contrato> positiveContratoData() throws ParseException {

		Stream<Contrato> res;

		List<TiposGanado> tiposGanado = new ArrayList<TiposGanado>();
		List<TiposGanado> tiposGanado2 = new ArrayList<TiposGanado>();
		TiposGanado p = new TiposGanado();
		p.setTipoGanado("porcino");
		TiposGanado c = new TiposGanado();
		p.setTipoGanado("caprino");
		tiposGanado.add(c);
		tiposGanado.add(p);
		tiposGanado2.add(p);
		Veterinario vet1 = VeterinarioServiceTests.generateVeterinario("Joseph", "Joestar", "622222222", "jojo@gmail.com", "Sevilla", "Los palacios", "12345678A", tiposGanado, true, "hola", "hola");
		Veterinario vet2 = VeterinarioServiceTests.generateVeterinario("Jotaro", "kujo", "622222223", "jojo2@gmail.com", "Sevilla", "Los palacios", "12345678B", tiposGanado2, true, "hola2", "hola2");
		Ganadero ganaderoPos1 = GanaderoServiceTests.generateGanadero("Antonio", "Banderas", "0123456789", "anton@mail.com", "Sevilla", "Dos Hermanas", "123456789A", "41345", "Calle Antonio Mairena, 8", "antonio1", "12345");
		Ganadero ganaderoPos2 = GanaderoServiceTests.generateGanadero("Lucas", "Contreras Domenech", "189892902", "l12222@mail.com", "Málaga", "Marbella", "123456789I", "10100", "Avenida de los ilustrísimos 81, portal A", "lucas1", "12345");

		ExplotacionGanadera exp1 = ContratoServiceTests.generateExplotacionGanadera("2", "4", true, ganaderoPos1, "exp1");
		ExplotacionGanadera exp2 = ContratoServiceTests.generateExplotacionGanadera("4", "8", true, ganaderoPos2, "exp2");

		Contrato contratoPos1 = ContratoServiceTests.generateContrato(tiposGanado, TipoEstadoContrato.PENDIENTE, LocalDate.of(2020, 2, 28), LocalDate.of(2020, 3, 1), LocalDate.of(2021, 3, 1), exp1, vet1);
		Contrato contratoPos2 = ContratoServiceTests.generateContrato(tiposGanado2, TipoEstadoContrato.PENDIENTE, LocalDate.of(2020, 2, 28), LocalDate.of(2020, 3, 1), LocalDate.of(2021, 3, 1), exp2, vet2);

		res = Stream.of(contratoPos1, contratoPos2);
		return res;
	}

	@ParameterizedTest()
	@MethodSource("positiveContratoData")
	void positiveTestsContrato(final Contrato contrato) {
		GenerateValidatorTest.validatorPositiveTests(contrato);
	}

	private static Stream<Arguments> negativeContratoData() throws ParseException {

		Stream<Arguments> res;

		List<TiposGanado> tiposGanado = new ArrayList<TiposGanado>();
		List<TiposGanado> tiposGanado2 = new ArrayList<TiposGanado>();
		TiposGanado p = new TiposGanado();
		p.setTipoGanado("porcino");
		TiposGanado c = new TiposGanado();
		p.setTipoGanado("caprino");
		tiposGanado.add(c);
		tiposGanado.add(p);
		tiposGanado2.add(p);
		Veterinario vet1 = VeterinarioServiceTests.generateVeterinario("Joseph", "Joestar", "622222222", "jojo@gmail.com", "Sevilla", "Los palacios", "12345678A", tiposGanado, true, "hola", "hola");
		Veterinario vet2 = VeterinarioServiceTests.generateVeterinario("Jotaro", "kujo", "622222223", "jojo2@gmail.com", "Sevilla", "Los palacios", "12345678B", tiposGanado2, true, "hola2", "hola2");
		Ganadero ganaderoPos1 = GanaderoServiceTests.generateGanadero("Antonio", "Banderas", "0123456789", "anton@mail.com", "Sevilla", "Dos Hermanas", "123456789A", "41345", "Calle Antonio Mairena, 8", "antonio1", "12345");
		Ganadero ganaderoPos2 = GanaderoServiceTests.generateGanadero("Lucas", "Contreras Domenech", "189892902", "l12222@mail.com", "Málaga", "Marbella", "123456789I", "10100", "Avenida de los ilustrísimos 81, portal A", "lucas1", "12345");

		ExplotacionGanadera exp1 = ContratoServiceTests.generateExplotacionGanadera("2", "4", true, ganaderoPos1, "exp1");
		ExplotacionGanadera exp2 = ContratoServiceTests.generateExplotacionGanadera("4", "8", true, ganaderoPos2, "exp2");

		Contrato contratoPos1 = ContratoServiceTests.generateContrato(null, TipoEstadoContrato.PENDIENTE, ValidatorContratoTests.newLocalDateFormattedWithoutHour("2020/08/23"), ValidatorContratoTests.newLocalDateFormattedWithoutHour("2020/08/23"),
			ValidatorContratoTests.newLocalDateFormattedWithoutHour("2021/08/23"), exp1, vet1);
		Map<String, List<ExpectedViolation>> contratoNeg1Violations = Stream.of(new ExpectedViolation("ganados", "must not be null", "javax.validation.constraints.NotNull.message")).collect(Collectors.groupingBy(ExpectedViolation::getProperyPath));
		Contrato contratoPos2 = ContratoServiceTests.generateContrato(tiposGanado2, TipoEstadoContrato.PENDIENTE, ValidatorContratoTests.newLocalDateFormattedWithoutHour("2020/08/23"), null, null, exp2, vet2);
		Map<String, List<ExpectedViolation>> contratoNeg2Violations = Stream
			.of(new ExpectedViolation("fechaInicial", "must not be null", "javax.validation.constraints.NotNull.message"), new ExpectedViolation("fechaFinal", "must not be null", "javax.validation.constraints.NotNull.message"))
			.collect(Collectors.groupingBy(ExpectedViolation::getProperyPath));
		res = Stream.of(Arguments.of(contratoPos1, contratoNeg1Violations), Arguments.of(contratoPos2, contratoNeg2Violations));
		return res;
	}

	@ParameterizedTest
	@MethodSource("negativeContratoData")
	void negativeTestsContrato(final Contrato contrato, final Map<String, List<ExpectedViolation>> expectedConstraintViolations) {
		GenerateValidatorTest.validatorNegativeTests(contrato, expectedConstraintViolations);
	}

	public static LocalDate newLocalDateFormattedWithoutHour(final String fecha) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		return sdf.parse(fecha).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	}

}
