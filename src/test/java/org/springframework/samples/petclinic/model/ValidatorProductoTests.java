
package org.springframework.samples.petclinic.model;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.validation.Validator;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.samples.petclinic.model.utils.ExpectedViolation;
import org.springframework.samples.petclinic.model.utils.GenerateValidatorTest;
import org.springframework.samples.petclinic.service.ProductoServiceTests;
import org.springframework.samples.petclinic.service.VeterinarioServiceTests;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

public class ValidatorProductoTests {

	private static Validator createValidator() {
		LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
		localValidatorFactoryBean.afterPropertiesSet();
		return localValidatorFactoryBean;
	}

	public static Stream<Producto> positiveTestProductoData() throws ParseException {
		Stream<Producto> res;
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

		vet2.setId(4);

		Producto prodPos1 = ProductoServiceTests.generateProducto("Ibrupofenomon", 5, 4.44, false);
		Producto prodPos2 = ProductoServiceTests.generateProducto("paracetamon", 123, 123.0, true);
		Producto prodPos3 = ProductoServiceTests.generateProducto("medicamenton", 12, 12.12, false);

		prodPos1.setVeterinario(vet1);
		prodPos2.setVeterinario(vet2);
		prodPos3.setVeterinario(vet2);

		res = Stream.of(prodPos1, prodPos2, prodPos3);
		return res;
	}

	@ParameterizedTest
	@MethodSource("positiveTestProductoData")
	void positiveTestsProducto(final Producto producto) {
		GenerateValidatorTest.validatorPositiveTests(producto);
	}

	public static Stream<Arguments> negativeTestProductoData() throws ParseException {
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

		vet2.setId(4);

		Producto prodPos1 = ProductoServiceTests.generateProducto("Ibrupofenomon", -5, 4.44, false);
		Producto prodPos2 = ProductoServiceTests.generateProducto("paracetamon", 123, -123.0, true);
		Producto prodPos3 = ProductoServiceTests.generateProducto("medicamenton", -12, -12.12, false);
		Producto prodPos4 = ProductoServiceTests.generateProducto("", 12, 12.12, false);

		prodPos1.setVeterinario(vet1);
		Map<String, List<ExpectedViolation>> ProductoFormNeg1Violations = Stream.of(new ExpectedViolation("cantidad", "must be between 0 and 9223372036854775807", "org.hibernate.validator.constraints.Range.message"))
			.collect(Collectors.groupingBy(ExpectedViolation::getProperyPath));
		prodPos2.setVeterinario(vet2);
		Map<String, List<ExpectedViolation>> ProductoFormNeg2Violations = Stream.of(new ExpectedViolation("precio", "must be between 0 and 9223372036854775807", "org.hibernate.validator.constraints.Range.message"))
			.collect(Collectors.groupingBy(ExpectedViolation::getProperyPath));
		prodPos3.setVeterinario(vet2);
		Map<String, List<ExpectedViolation>> ProductoFormNeg3Violations = Stream.of(new ExpectedViolation("cantidad", "must be between 0 and 9223372036854775807", "org.hibernate.validator.constraints.Range.message"),
			new ExpectedViolation("precio", "must be between 0 and 9223372036854775807", "org.hibernate.validator.constraints.Range.message")).collect(Collectors.groupingBy(ExpectedViolation::getProperyPath));
		prodPos4.setVeterinario(vet1);
		Map<String, List<ExpectedViolation>> ProductoFormNeg4Violations = Stream.of(new ExpectedViolation("name", "size must be between 3 and 50", "javax.validation.constraints.Size.message"))
			.collect(Collectors.groupingBy(ExpectedViolation::getProperyPath));

		res = Stream.of(Arguments.of(prodPos1, ProductoFormNeg1Violations), Arguments.of(prodPos2, ProductoFormNeg2Violations), Arguments.of(prodPos3, ProductoFormNeg3Violations), Arguments.of(prodPos4, ProductoFormNeg4Violations));
		return res;
	}
	@ParameterizedTest
	@MethodSource("negativeTestProductoData")
	void negativeTestsProducto(final Producto producto, final Map<String, List<ExpectedViolation>> expV) {
		GenerateValidatorTest.validatorNegativeTests(producto, expV);
	}
}
