
package org.springframework.samples.petclinic.service;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.samples.petclinic.model.Cita;
import org.springframework.samples.petclinic.model.LineaReceta;
import org.springframework.samples.petclinic.model.Receta;
import org.springframework.samples.petclinic.model.TipoEstadoCita;
import org.springframework.samples.petclinic.repository.springdatajpa.RecetaRepository;

@ExtendWith(MockitoExtension.class)
public class RecetaServiceTests {

	@Mock
	private RecetaRepository	recetaRepository;

	@Mock
	private GanaderoService		ganaderoService;

	@Mock
	private CitaService			citaService;

	@Mock
	private VetService			vetService;

	@InjectMocks
	private RecetaService		recetaService;

	//1 ----- MÉTODO GUARDAR UNA RECETA


	//1.1 -> Insertar receta correctamente
	private static Stream<Receta> shouldInsertRecetaData() throws ParseException {
		Stream<Receta> res;

		//Generando los datos para la receta para la cita pos 1 con el
		//método auxiliar contrato1ParaPruebas()
		Cita citaParaRecetaPos1 = RecetaServiceTests.cita1ParaPruebas();

		//Generando una receta
		Receta recetaPos1 = RecetaServiceTests.generateReceta(CitaNonSolitaryServiceTests.newDateFormatted("2020/05/31 17:00"), "Para dar semanalmente durante una semana a las cabras", false, citaParaRecetaPos1, null);

		res = Stream.of(recetaPos1);
		return res;

	}

	@ParameterizedTest()
	@MethodSource("shouldInsertRecetaData")
	void shouldInsertReceta(final Receta receta) {
		this.recetaService.saveReceta(receta);
		Mockito.verify(this.recetaRepository).save(receta);
	}

	//1.2 ->  No se puede insertar receta correctamente

	private static Stream<Receta> shouldNotInsertRecetaData() throws ParseException {
		Stream<Receta> res;

		//Generando los datos para la receta para la cita neg 1 con el
		//método auxiliar contrato1ParaPruebas()
		Cita citaParaRecetaNeg1 = RecetaServiceTests.cita1ParaPruebas();

		//Generando una receta
		Receta recetaNeg1 = RecetaServiceTests.generateReceta(null, "Para dar semanalmente durante una semana a las cabras", false, citaParaRecetaNeg1, null);

		//Generando otra receta
		Receta recetaNeg2 = RecetaServiceTests.generateReceta(CitaNonSolitaryServiceTests.newDateFormatted("2020/05/31 17:00"), null, false, citaParaRecetaNeg1, null);

		//Generando otra receta
		Receta recetaNeg3 = RecetaServiceTests.generateReceta(CitaNonSolitaryServiceTests.newDateFormatted("2020/05/31 17:00"), "", false, citaParaRecetaNeg1, null);

		res = Stream.of(recetaNeg1, recetaNeg2, recetaNeg3);
		return res;

	}

	@ParameterizedTest()
	@MethodSource("shouldNotInsertRecetaData")
	void shouldNotInsertReceta(final Receta receta) {
		this.recetaService.saveReceta(receta);
		Mockito.verify(this.recetaRepository).save(receta);
	}

	private static Stream<Arguments> shouldNotInsertRecetaDataAssertExceptions() throws ParseException {
		Stream<Arguments> res;

		// ----------------------- Generando la receta que debería dar fallo

		//Generando los datos para la receta para la cita neg 1 con el
		//método auxiliar contrato2ParaPruebas()
		//Esta cita tiene de estado Rechazada, por lo que al no ser una cita aceptada, al crear una receta de esa
		//cita debería saltar una excepción
		Cita citaParaRecetaNeg1 = RecetaServiceTests.cita2ParaPruebas();

		Receta recetaNeg1 = RecetaServiceTests.generateReceta(CitaNonSolitaryServiceTests.newDateFormatted("2020/05/31 17:00"), "Para dar semanalmente durante una semana a las cabras", false, citaParaRecetaNeg1, null);
		
		// --> Error que debe salir, con su mensaje
		IllegalArgumentException recetaNeg1ErrorEsperado = new IllegalArgumentException("La cita elegida para la receta debe estar en estado aceptada");

		// ------------------------ Generando otra receta que debería dar fallo

		//Generando los datos para la receta para la cita neg 2 con el
		//método auxiliar contrato3ParaPruebas()
		//Esta cita tiene como fechas 2020/01/01 10:00 - 2020/01/01 11:00 de fecha inicio y fecha final, respectivamente
		//Por lo que al crear una receta en una fecha que no tenga de diferencia como máximo un mes más tarde que la
		//cita saltará una excepción
		Cita citaParaRecetaNeg2 = RecetaServiceTests.cita3ParaPruebas();

		Receta recetaNeg2 = RecetaServiceTests.generateReceta(CitaNonSolitaryServiceTests.newDateFormatted("2020/09/31 17:00"), "Para dar semanalmente durante una semana a las cabras", false, citaParaRecetaNeg2, null);
		
		// --> Error que debe salir, con su mensaje
		IllegalArgumentException recetaNeg2ErrorEsperado = new IllegalArgumentException("La cita elegida para la receta debe ser una cita que se haya tenido entre los tiempos del momento actual y un mes hacia atrás");

		res = Stream.of(Arguments.of(recetaNeg1, recetaNeg1ErrorEsperado), Arguments.of(recetaNeg2, recetaNeg2ErrorEsperado));
		return res;
	}

	@ParameterizedTest()
	@MethodSource("shouldNotInsertRecetaDataAssertExceptions")
	void shouldNotInsertRecetaDataAssertExceptions(final Receta receta, final IllegalArgumentException errorEsperado) throws Exception {

		Exception errorInsertReceta = Assertions.assertThrows(IllegalArgumentException.class, () -> {
			this.recetaService.saveReceta(receta);
		});
		Assert.assertEquals(errorInsertReceta.getMessage(), errorEsperado.getMessage());
	}

	@ParameterizedTest()
	@MethodSource("shouldInsertRecetaData")
	void shouldEsFacturadoEqualTrueWhenTheRecetaIsFacturada(final Receta receta) {
		//Usamos los datos de shouldInsertRecetaData porque también nos vale para este test.
		//Vamos a comprobar si cuando se pasa una receta por el método para marcarla como facturada, su atributo
		//esFacturado es igual a true.
		this.recetaService.marcarRecetasComoFacturadas(receta);
		Mockito.verify(this.recetaRepository).save(receta);
		Assert.assertEquals(true, receta.getEsFacturado());
	}

	//Métodos auxiliares

	// Método para generar una receta
	public static Receta generateReceta(final Date fechaRealizacion, final String descripcion, final Boolean esFacturado, final Cita cita, final List<LineaReceta> lineaReceta) {
		Receta receta = new Receta();

		//Asignaciones receta
		receta.setFechaRealizacion(fechaRealizacion);
		receta.setDescripcion(descripcion);
		receta.setEsFacturado(esFacturado);
		receta.setCita(cita);

		return receta;
	}

	//DATOS DE PRUEBA

	// --- Cita1 ---
	public static Cita cita1ParaPruebas() throws ParseException {

		Cita cita = CitaServiceTests.generateCita(CitaNonSolitaryServiceTests.newDateFormatted("2020/05/31 10:00"), CitaNonSolitaryServiceTests.newDateFormatted("2020/05/31 11:00"), "Varias ovejas no están dando leche", null, TipoEstadoCita.ACEPTADA,
			CitaServiceTests.contrato1ParaPruebas());
		return cita;
	}

	// --- Cita2 ---
	public static Cita cita2ParaPruebas() throws ParseException {
		Cita cita = CitaServiceTests.generateCita(CitaNonSolitaryServiceTests.newDateFormatted("2020/05/31 10:00"), CitaNonSolitaryServiceTests.newDateFormatted("2020/05/31 11:00"), "Varias ovejas no están dando leche", null, TipoEstadoCita.RECHAZADA,
			CitaServiceTests.contrato1ParaPruebas());
		return cita;
	}

	// --- Cita3 ---
	public static Cita cita3ParaPruebas() throws ParseException {
		Cita cita = CitaServiceTests.generateCita(CitaNonSolitaryServiceTests.newDateFormatted("2020/01/01 10:00"), CitaNonSolitaryServiceTests.newDateFormatted("2020/01/01 11:00"), "Varias ovejas no están dando leche", null, TipoEstadoCita.ACEPTADA,
			CitaServiceTests.contrato1ParaPruebas());
		return cita;
	}

}
