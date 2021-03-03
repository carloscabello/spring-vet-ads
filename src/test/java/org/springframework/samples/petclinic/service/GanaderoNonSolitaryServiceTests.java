
package org.springframework.samples.petclinic.service;

import java.util.stream.Stream;

import javax.validation.ConstraintViolationException;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.Ganadero;
import org.springframework.samples.petclinic.repository.springdatajpa.GanaderoRepository;
import org.springframework.stereotype.Service;

/*
 * Los tests unitarios NO-SOLITARIOS deberían minimizarse.
 * Se usan para probar las queries personalizadas, no hay otra forma sino con este tipo de tests
 * También son necesarias para comprobar el comportamiento del respositorio
 */

@DisplayName("Ganadero Service Non-solitary tests")
@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class GanaderoNonSolitaryServiceTests {

	@Autowired
	protected GanaderoService ganaderoService;
	
	@Autowired
	protected GanaderoRepository ganaderoRepository;


	@DisplayName("Ganadero Service custom queries tests")
	@Nested
	class GanaderoCustomServiceQueries {

		//Esta prueba no es necesaria, se hace con propositos demostrativos
		@Test
		@Disabled
		public void findOneTest() throws Exception {
			Assertions.assertThat(GanaderoNonSolitaryServiceTests.this.ganaderoService.findGanaderoById(1)).isNotNull();
		}

		@Test
		@Disabled
		public void findOneTestNotFound() throws Exception {
			Assertions.assertThat(GanaderoNonSolitaryServiceTests.this.ganaderoService.findGanaderoById(9999)).isEmpty();
		}

	}


	private static Stream<Ganadero> shouldInsertGanaderoData() {

		Stream<Ganadero> res;

		Ganadero ganaderoPos1 = GanaderoServiceTests.generateGanadero("Antonio", "Banderas", "0123456789", "anton@mail.com", "Sevilla", "Dos Hermanas", "12345678A", "41345", "Calle Antonio Mairena, 8", "antonio1", "12345");
		Ganadero ganaderoPos2 = GanaderoServiceTests.generateGanadero("Lucas", "Contreras Domenech", "189892902", "l12222@mail.com", "Málaga", "Marbella", "12345678R", "10100", "Avenida de los ilustrísimos 81, portal A", "lucas1", "12345");

		res = Stream.of(ganaderoPos1, ganaderoPos2);
		return res;

	}

	@Disabled
	@ParameterizedTest()
	@MethodSource("shouldInsertGanaderoData")
	void shouldInsertGanadero(final Ganadero ganadero) {

		this.ganaderoService.saveGanadero(ganadero);
	}

	private static Stream<Arguments> shouldNotInsertGanaderoData() {

		Stream<Arguments> res;

		Ganadero ganaderoNeg1 = GanaderoServiceTests.generateGanadero("", "", "", "anton@mail.com", "Sevilla", "Dos Hermanas", "", "", "", "", "");
		Ganadero ganaderoNeg2 = GanaderoServiceTests.generateGanadero("Lucas", "Contreras Domenech", "189892902", "l12222@mail.com", "Málaga", "Marbella", "123456789I", "10100", "Avenida de los ilustrísimos 81, portal A", "", "12345");

		res = Stream.of(Arguments.of(ganaderoNeg1, ConstraintViolationException.class), Arguments.of(ganaderoNeg2, ConstraintViolationException.class));
		return res;

	}

	@Disabled
	@ParameterizedTest()
	@MethodSource("shouldNotInsertGanaderoData")
	void shouldNotInsertGanadero(final Ganadero ganadero, final Class<Exception> expectedException) throws Exception {
		Assertions.assertThatThrownBy(() -> {
			this.ganaderoService.saveGanadero(ganadero);
		}).isInstanceOf(expectedException);
	}

}
