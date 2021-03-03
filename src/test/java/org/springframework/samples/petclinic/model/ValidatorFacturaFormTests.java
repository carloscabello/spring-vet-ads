
package org.springframework.samples.petclinic.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.samples.petclinic.forms.FacturaForm;
import org.springframework.samples.petclinic.model.utils.GenerateValidatorTest;
import org.springframework.samples.petclinic.service.CitaNonSolitaryServiceTests;
import org.springframework.samples.petclinic.service.ContratoServiceTests;
import org.springframework.samples.petclinic.service.GanaderoServiceTests;
import org.springframework.samples.petclinic.service.ProductoServiceTests;
import org.springframework.samples.petclinic.service.RecetaServiceTests;
import org.springframework.samples.petclinic.service.VeterinarioServiceTests;

public class ValidatorFacturaFormTests {

	private static Stream<FacturaForm> positiveTestFacturaFormData() throws ParseException {
		Stream<FacturaForm> res;
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

		Contrato contrato = new Contrato();
		ExplotacionGanadera exp1 = ContratoServiceTests.generateExplotacionGanadera("2", "4", true, ganaderoPos1, "exp1");
		ExplotacionGanadera exp2 = ContratoServiceTests.generateExplotacionGanadera("4", "8", true, ganaderoPos2, "exp2");

		Contrato contratoPos1 = ContratoServiceTests.generateContrato(null, TipoEstadoContrato.PENDIENTE, ValidatorContratoTests.newLocalDateFormattedWithoutHour("2020/08/23"), ValidatorContratoTests.newLocalDateFormattedWithoutHour("2020/08/23"),
			ValidatorContratoTests.newLocalDateFormattedWithoutHour("2021/08/23"), exp1, vet1);

		List<Integer> cantidad = new ArrayList<>();
		cantidad.add(1);
		cantidad.add(1);
		cantidad.add(1);

		Producto prodPos1 = ProductoServiceTests.generateProducto("Ibrupofenomon", 5, 4.44, false);
		Producto prodPos2 = ProductoServiceTests.generateProducto("paracetamon", 123, 123.0, true);
		Producto prodPos3 = ProductoServiceTests.generateProducto("medicamenton", 12, 12.12, false);

		prodPos1.setVeterinario(vet1);
		prodPos2.setVeterinario(vet2);
		prodPos3.setVeterinario(vet2);

		List<Integer> prod = new ArrayList<>();
		prod.add(prodPos1.getId());
		prod.add(prodPos2.getId());
		prod.add(prodPos3.getId());

		Cita citaParaRecetaPos1 = RecetaServiceTests.cita1ParaPruebas();
		Receta recetaPos1 = RecetaServiceTests.generateReceta(CitaNonSolitaryServiceTests.newDateFormatted("2020/05/31 17:00"), "Para dar semanalmente durante una semana a las cabras", false, citaParaRecetaPos1, null);
		List<Receta> recetas = new ArrayList<>();
		recetas.add(recetaPos1);
		FacturaForm facturaFormPos1 = ValidatorFacturaFormTests.generateFacturaForm(cantidad, contratoPos1, false, ValidatorFacturaFormTests.newDateFormattedWithoutHour("2020/08/18"), prod, recetas);

		res = Stream.of(facturaFormPos1);
		return res;
	}

	@ParameterizedTest()
	@MethodSource("positiveTestFacturaFormData")
	void positiveTestsCita(final FacturaForm facturaForm) {

		GenerateValidatorTest.validatorPositiveTests(facturaForm);

	}
	/*
	 * private static Stream<Arguments> negativeTestFacturaFormData() throws ParseException {
	 * Stream<Arguments> res;
	 *
	 * List<TiposGanado> tiposGanado = new ArrayList<TiposGanado>();
	 * List<TiposGanado> tiposGanado2 = new ArrayList<TiposGanado>();
	 * TiposGanado p = new TiposGanado();
	 * p.setTipoGanado("porcino");
	 * TiposGanado c = new TiposGanado();
	 * p.setTipoGanado("caprino");
	 * tiposGanado.add(c);
	 * tiposGanado.add(p);
	 * tiposGanado2.add(p);
	 * Veterinario vet1 = VetServiceTests.generateVeterinario("Joseph", "Joestar", "622222222", "jojo@gmail.com", "Sevilla", "Los palacios", "12345678A", true, tiposGanado, "hola", "hola");
	 * Veterinario vet2 = VetServiceTests.generateVeterinario("Jotaro", "kujo", "622222223", "jojo2@gmail.com", "Sevilla", "Los palacios", "12345678B", true, tiposGanado2, "hola2", "hola2");
	 * Ganadero ganaderoPos1 = GanaderoServiceTests.generateGanadero("Antonio", "Banderas", "0123456789", "anton@mail.com", "Sevilla", "Dos Hermanas", "123456789A", "41345", "Calle Antonio Mairena, 8", "antonio1", "12345");
	 * Ganadero ganaderoPos2 = GanaderoServiceTests.generateGanadero("Lucas", "Contreras Domenech", "189892902", "l12222@mail.com", "Málaga", "Marbella", "123456789I", "10100", "Avenida de los ilustrísimos 81, portal A", "lucas1", "12345");
	 *
	 * Contrato contrato = new Contrato();
	 * ExplotacionGanadera exp1 = ContratoServiceTests.generateExplotacionGanadera("2", "4", true, ganaderoPos1, "exp1");
	 * ExplotacionGanadera exp2 = ContratoServiceTests.generateExplotacionGanadera("4", "8", true, ganaderoPos2, "exp2");
	 *
	 * Contrato contratoPos1 = ContratoServiceTests.generateContrato(null, TipoEstadoContrato.PENDIENTE, ValidatorContratoTests.newLocalDateFormattedWithoutHour("2020/08/23"), ValidatorContratoTests.newLocalDateFormattedWithoutHour("2020/08/23"),
	 * ValidatorContratoTests.newLocalDateFormattedWithoutHour("2021/08/23"), exp1, vet1);
	 *
	 * List<Integer> cantidad = new ArrayList<>();
	 * cantidad.add(1);
	 * cantidad.add(1);
	 * cantidad.add(1);
	 *
	 * Producto prodPos1 = ProductoServiceTests.generateProducto("Ibrupofenomon", 5, 4.44, false);
	 * Producto prodPos2 = ProductoServiceTests.generateProducto("paracetamon", 123, 123.0, true);
	 * Producto prodPos3 = ProductoServiceTests.generateProducto("medicamenton", 12, 12.12, false);
	 *
	 * prodPos1.setVeterinario(vet1);
	 * prodPos2.setVeterinario(vet2);
	 * prodPos3.setVeterinario(vet2);
	 *
	 * List<Integer> prod = new ArrayList<>();
	 * prod.add(prodPos1.getId());
	 * prod.add(prodPos2.getId());
	 * prod.add(prodPos3.getId());
	 *
	 * Cita citaParaRecetaPos1 = RecetaServiceTests.cita1ParaPruebas();
	 * Receta recetaPos1 = RecetaServiceTests.generateReceta(CitaNonSolitaryServiceTests.newDateFormatted("2020/05/31 17:00"), "Para dar semanalmente durante una semana a las cabras", false, citaParaRecetaPos1, null);
	 * List<Receta> recetas = new ArrayList<>();
	 * recetas.add(recetaPos1);
	 *
	 * FacturaForm facturaFormNeg1 = ValidatorFacturaFormTests.generateFacturaForm(cantidad, contratoPos1, false, ValidatorFacturaFormTests.newDateFormattedWithoutHour("2020/08/18"), null, null);
	 * FacturaForm facturaFormNeg2 = ValidatorFacturaFormTests.generateFacturaForm(null, contratoPos1, false, ValidatorFacturaFormTests.newDateFormattedWithoutHour("2020/08/18"), prod, null);
	 *
	 * Map<String, List<ExpectedViolation>> facturaFormNeg1Violations = Stream
	 * .of(new ExpectedViolation("producto", "must not be empty", "javax.validation.constraints.NotBlank.message"), new ExpectedViolation("recetas", "must not be empty", "javax.validation.constraints.NotBlank.message"))
	 * .collect(Collectors.groupingBy(ExpectedViolation::getProperyPath));
	 *
	 * Map<String, List<ExpectedViolation>> facturaFormNeg2Violations = Stream
	 * .of(new ExpectedViolation("cantidad", "and recetas must not be empty", "javax.validation.constraints.NotBlank.message"), new ExpectedViolation("recetas", "must not be empty", "javax.validation.constraints.NotBlank.message"))
	 * .collect(Collectors.groupingBy(ExpectedViolation::getProperyPath));
	 *
	 * res = Stream.of(Arguments.of(facturaFormNeg1, facturaFormNeg1Violations), Arguments.of(facturaFormNeg2, facturaFormNeg2Violations));
	 *
	 * return res;
	 * }
	 *
	 * @ParameterizedTest()
	 *
	 * @MethodSource("negativeTestFacturaFormData")
	 * void negativeTestsFactura(final FacturaForm facturaForm, final Map<String, List<ExpectedViolation>> expectedConstraintViolations) {
	 *
	 * GenerateValidatorTest.validatorNegativeTests(facturaForm, expectedConstraintViolations);
	 *
	 * }
	 */

	// ----- Métodos auxiliares

	// Método para generar una factura
	public static FacturaForm generateFacturaForm(final List<Integer> cantidad, final Contrato contrato, final Boolean esPagado, final Date fechaEmision, final List<Integer> productoId, final List<Receta> recetas) {
		FacturaForm facturaForm = new FacturaForm();

		//Asignaciones cita
		facturaForm.setCantidad(cantidad);
		facturaForm.setContrato(contrato);
		facturaForm.setEsPagado(esPagado);
		facturaForm.setFechaEmision(fechaEmision);
		facturaForm.setProductoId(productoId);
		facturaForm.setRecetas(recetas);

		return facturaForm;
	}

	public static Date newDateFormattedWithoutHour(final String fecha) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		return sdf.parse(fecha);
	}

}
