
package org.springframework.samples.petclinic.service;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
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
import org.springframework.samples.petclinic.model.Contrato;
import org.springframework.samples.petclinic.model.ExplotacionGanadera;
import org.springframework.samples.petclinic.model.Ganadero;
import org.springframework.samples.petclinic.model.TipoEstadoCita;
import org.springframework.samples.petclinic.model.TipoEstadoContrato;
import org.springframework.samples.petclinic.model.TiposGanado;
import org.springframework.samples.petclinic.model.Veterinario;
import org.springframework.samples.petclinic.repository.springdatajpa.CitaRepository;

@ExtendWith(MockitoExtension.class)
public class CitaServiceTests {

	@Mock
	private CitaRepository		citaRepository;

	@Mock
	protected ContratoService	contratoService;

	@Mock
	protected GanaderoService	ganaderoService;

	@Mock
	protected VetService		vetService;

	@InjectMocks
	protected CitaService		citaService;

	//1 ----- MÉTODO GUARDAR UNA CITA


	//1.1 -> Insertar cita correctamente
	private static Stream<Cita> shouldInsertCitaData() throws ParseException {
		Stream<Cita> res;

		//Generando los datos para el contrato para la cita pos 1 con el
		//método auxiliar contrato1ParaPruebas()
		Contrato contratoParaCitaPos1 = CitaServiceTests.contrato1ParaPruebas();

		Assert.assertTrue(contratoParaCitaPos1.getEstado().equals(TipoEstadoContrato.ACEPTADO));
		Cita citaPos1 = CitaServiceTests.generateCita(CitaNonSolitaryServiceTests.newDateFormatted("2020/06/01 10:00"), CitaNonSolitaryServiceTests.newDateFormatted("2020/06/01 11:00"), "Varias ovejas no están dando leche", null, TipoEstadoCita.PENDIENTE,
			contratoParaCitaPos1);

		res = Stream.of(citaPos1);
		return res;

	}

	@ParameterizedTest()
	@MethodSource("shouldInsertCitaData")
	void shouldInsertCita(final Cita cita) {
		this.citaService.saveCita(cita);
		Mockito.verify(this.citaRepository).save(cita);
	}

	//1.1 -> No se puede insertar cita correctamente
	private static Stream<Cita> shouldNotInsertCitaData() throws ParseException {
		Stream<Cita> res;

		//Generando los datos para el contrato para la cita pos 1 con el
		//método auxiliar contrato1ParaPruebas()
		Contrato contratoParaCitaNeg1 = CitaServiceTests.contrato1ParaPruebas();

		Cita citaNeg1 = CitaServiceTests.generateCita(null, CitaNonSolitaryServiceTests.newDateFormatted("2020/06/01 11:00"), "Varias vacas no están dando leche", null, TipoEstadoCita.PENDIENTE, contratoParaCitaNeg1);
		Cita citaNeg2 = CitaServiceTests.generateCita(CitaNonSolitaryServiceTests.newDateFormatted("2020/06/01 10:00"), CitaNonSolitaryServiceTests.newDateFormatted("2020/06/01 11:00"), "", null, TipoEstadoCita.PENDIENTE, contratoParaCitaNeg1);

		res = Stream.of(citaNeg1, citaNeg2);
		return res;

	}

	@ParameterizedTest()
	@MethodSource("shouldNotInsertCitaData")
	void shouldNotInsertCita(final Cita cita) throws Exception {
		this.citaService.saveCita(cita);
		Mockito.verify(this.citaRepository).save(cita);
	}

	//2 ----- MÉTODO ACEPTAR UNA CITA

	//2.1 -> Aceptar cita correctamente
	private static Stream<Cita> shouldAcceptOrRejectCitaData() throws ParseException {
		Stream<Cita> res;

		//Generando los datos para el contrato para la cita pos 1 con el
		//método auxiliar contrato1ParaPruebas()
		Contrato contratoParaCitaParaAceptar1 = CitaServiceTests.contrato1ParaPruebas();

		Assert.assertTrue(contratoParaCitaParaAceptar1.getEstado().equals(TipoEstadoContrato.ACEPTADO));
		Cita citaParaAceptar1 = CitaServiceTests.generateCita(CitaNonSolitaryServiceTests.newDateFormatted("2020/09/01 10:00"), CitaNonSolitaryServiceTests.newDateFormatted("2020/09/01 11:00"), "Varias ovejas no están dando leche", null,
			TipoEstadoCita.PENDIENTE, contratoParaCitaParaAceptar1);

		res = Stream.of(citaParaAceptar1);
		return res;
	}

	@ParameterizedTest()
	@MethodSource("shouldAcceptOrRejectCitaData")
	void shouldAcceptCita(final Cita cita) {
		//Vamos a generar un falso veterinario, como si fuera el veterinario logeado.
		//Hemos creado un método auxiliar para ello
		Veterinario loggedVeterinario = CitaServiceTests.falsoVeterinarioLogeado();

		//Llamadas del método
		Mockito.when(this.vetService.findVeterinarioByLogedUser()).thenReturn(loggedVeterinario);
		this.citaService.acceptCita(cita);
		Assert.assertTrue(cita.getEstado().equals(TipoEstadoCita.ACEPTADA));
	}

	//2.2 ->No se puede aceptar cita correctamente
	private static Stream<Arguments> shouldNotAcceptCitaData() throws ParseException {
		Stream<Arguments> res;

		//Contrato1 generado con el método auxiliar contrato1ParaPruebas()
		Contrato contratoParaCitaParaNoAceptar1 = CitaServiceTests.contrato1ParaPruebas();

		//--------------------------------------------------------------------

		//Contrato2 generado con el método auxiliar contrato2ParaPruebas()
		Contrato contratoParaCitaParaNoAceptar2 = CitaServiceTests.contrato2ParaPruebas();

		//--------------------------------------------------------------------

		//Citas contrato1
		Assert.assertTrue(contratoParaCitaParaNoAceptar1.getEstado().equals(TipoEstadoContrato.ACEPTADO));

		// --> Cita ya aceptada, que se va a intentar aceptar de nuevo --> ERROR
		Cita citaParaNoAceptar1 = CitaServiceTests.generateCita(CitaNonSolitaryServiceTests.newDateFormatted("2020/09/01 10:00"), CitaNonSolitaryServiceTests.newDateFormatted("2020/09/01 11:00"), "Varias ovejas no están dando leche", null,
			TipoEstadoCita.ACEPTADA, contratoParaCitaParaNoAceptar1);
		// --> Error que debe salir, con su mensaje
		IllegalArgumentException citaParaNoAceptar1ErrorEsperado = new IllegalArgumentException("La cita que se pretende aceptar no está en estado PENDIENTE");

		// --> Cita pendiente que se intenta aceptar una vez ya ha pasado la fecha de inicio --> ERROR
		Cita citaParaNoAceptar2 = CitaServiceTests.generateCita(CitaNonSolitaryServiceTests.newDateFormatted("2020/04/01 10:00"), CitaNonSolitaryServiceTests.newDateFormatted("2020/04/01 11:00"), "Uno de los cerdos tiene sangre en una de las patas", null,
			TipoEstadoCita.PENDIENTE, contratoParaCitaParaNoAceptar1);
		// --> Error que debe salir, con su mensaje
		IllegalArgumentException citaParaNoAceptar2ErrorEsperado = new IllegalArgumentException("La cita que se quiere aceptar es de una fecha que ya ha pasado.");

		//Citas contrato2
		Assert.assertTrue(contratoParaCitaParaNoAceptar2.getEstado().equals(TipoEstadoContrato.ACEPTADO));

		// --> Cita que intenta aceptar un veterinario logueado que no es del contrato
		Cita citaParaNoAceptar3 = CitaServiceTests.generateCita(CitaNonSolitaryServiceTests.newDateFormatted("2020/10/01 10:00"), CitaNonSolitaryServiceTests.newDateFormatted("2020/10/01 11:00"), "Las ovejas están perdiendo pelo", null,
			TipoEstadoCita.PENDIENTE, contratoParaCitaParaNoAceptar2);
		// --> Error que debe salir, con su mensaje
		IllegalArgumentException citaParaNoAceptar3ErrorEsperado = new IllegalArgumentException("La cita que se quiere aceptar no corresponde a un contrato del veterinario logueado");

		res = Stream.of(Arguments.of(citaParaNoAceptar1, citaParaNoAceptar1ErrorEsperado), Arguments.of(citaParaNoAceptar2, citaParaNoAceptar2ErrorEsperado), Arguments.of(citaParaNoAceptar3, citaParaNoAceptar3ErrorEsperado));
		return res;
	}

	@ParameterizedTest()
	@MethodSource("shouldNotAcceptCitaData")
	void shouldNotAcceptCita(final Cita cita, final IllegalArgumentException errorEsperado) throws Exception {
		//Vamos a generar un falso veterinario, como si fuera el veterinario logeado.
		//Hemos creado un método auxiliar para ello
		Veterinario loggedVeterinario = CitaServiceTests.falsoVeterinarioLogeado();

		//Llamadas del método
		Mockito.when(this.vetService.findVeterinarioByLogedUser()).thenReturn(loggedVeterinario);
		Exception errorAcceptCita = Assertions.assertThrows(IllegalArgumentException.class, () -> {
			this.citaService.acceptCita(cita);
		});
		Assert.assertEquals(errorAcceptCita.getMessage(), errorEsperado.getMessage());
	}

	//3 ----- MÉTODO RECHAZAR UNA CITA

	//3.1 -> Rechazar cita correctamente
	//Usaremos los mismos datos que usamos para aceptar una cita
	@ParameterizedTest()
	@MethodSource("shouldAcceptOrRejectCitaData")
	void shouldRejectCita(final Cita cita) {
		//Vamos a generar un falso veterinario, como si fuera el veterinario logeado.
		//Hemos creado un método auxiliar para ello
		Veterinario loggedVeterinario = CitaServiceTests.falsoVeterinarioLogeado();

		//Para rechazar una cita correctamente, necesitamos dar una justificación desde la vista
		//Vamos a simular una justificación
		cita.setRechazoJustificacion("No puedo atenderle ese día, pida una nueva cita para otro día");

		//Llamadas del método
		Mockito.when(this.vetService.findVeterinarioByLogedUser()).thenReturn(loggedVeterinario);
		this.citaService.declineCita(cita);
		Assert.assertTrue(cita.getEstado().equals(TipoEstadoCita.RECHAZADA));
	}

	//3.3 -> No se puede rechazar cita correctamente
	private static Stream<Arguments> shouldNotRejectCitaData() throws ParseException {
		Stream<Arguments> res;

		//Contrato1 generado con el método auxiliar contrato1ParaPruebas()
		Contrato contratoParaCitaParaNoRechazar1 = CitaServiceTests.contrato1ParaPruebas();

		//--------------------------------------------------------------------

		//Contrato2 generado con el método auxiliar contrato2ParaPruebas()
		Contrato contratoParaCitaParaNoRechazar2 = CitaServiceTests.contrato2ParaPruebas();

		//--------------------------------------------------------------------

		//Citas contrato1
		Assert.assertTrue(contratoParaCitaParaNoRechazar1.getEstado().equals(TipoEstadoContrato.ACEPTADO));

		// --> Cita ya rechazada, que se va a intentar rechazar de nuevo --> ERROR
		Cita citaParaNoRechazar1 = CitaServiceTests.generateCita(CitaNonSolitaryServiceTests.newDateFormatted("2020/09/01 10:00"), CitaNonSolitaryServiceTests.newDateFormatted("2020/09/01 11:00"), "Varias ovejas no están dando leche", null,
			TipoEstadoCita.RECHAZADA, contratoParaCitaParaNoRechazar1);
		// --> Error que debe salir, con su mensaje
		IllegalArgumentException citaParaNoRechazar1ErrorEsperado = new IllegalArgumentException("La cita que se pretende rechazar no estaba en estado PENDIENTE");

		// --> Cita pendiente que se intenta rechazar una vez ya ha pasado la fecha de inicio --> ERROR
		Cita citaParaNoRechazar2 = CitaServiceTests.generateCita(CitaNonSolitaryServiceTests.newDateFormatted("2020/04/01 10:00"), CitaNonSolitaryServiceTests.newDateFormatted("2020/04/01 11:00"), "Uno de los cerdos tiene sangre en una de las patas", null,
			TipoEstadoCita.PENDIENTE, contratoParaCitaParaNoRechazar1);
		// --> Error que debe salir, con su mensaje
		IllegalArgumentException citaParaNoRechazar2ErrorEsperado = new IllegalArgumentException("La cita que se quiere rechazar es de una fecha que ya ha pasado.");

		// --> Cita se intenta rechazar sin una justificación --> ERROR
		Cita citaParaNoRechazar3 = CitaServiceTests.generateCita(CitaNonSolitaryServiceTests.newDateFormatted("2020/09/01 10:00"), CitaNonSolitaryServiceTests.newDateFormatted("2020/04/01 11:00"), "Uno de los cerdos tiene sangre en una de las patas", null,
			TipoEstadoCita.PENDIENTE, contratoParaCitaParaNoRechazar1);
		IllegalArgumentException citaParaNoRechazar3ErrorEsperado = new IllegalArgumentException("La cita debe rechazarse con algún motivo");

		//Citas contrato2
		Assert.assertTrue(contratoParaCitaParaNoRechazar2.getEstado().equals(TipoEstadoContrato.ACEPTADO));

		// --> Cita que intenta rechazar un veterinario logueado que no es del contrato
		Cita citaParaNoRechazar4 = CitaServiceTests.generateCita(CitaNonSolitaryServiceTests.newDateFormatted("2020/10/01 10:00"), CitaNonSolitaryServiceTests.newDateFormatted("2020/10/01 11:00"), "Las ovejas están perdiendo pelo", null,
			TipoEstadoCita.PENDIENTE, contratoParaCitaParaNoRechazar2);
		// --> Error que debe salir, con su mensaje
		IllegalArgumentException citaParaNoRechazar4ErrorEsperado = new IllegalArgumentException("La cita que se quiere rechazar no corresponde a un contrato del veterinario logueado");

		res = Stream.of(Arguments.of(citaParaNoRechazar1, citaParaNoRechazar1ErrorEsperado), Arguments.of(citaParaNoRechazar2, citaParaNoRechazar2ErrorEsperado), Arguments.of(citaParaNoRechazar3, citaParaNoRechazar3ErrorEsperado),
			Arguments.of(citaParaNoRechazar4, citaParaNoRechazar4ErrorEsperado));
		return res;
	}

	@ParameterizedTest()
	@MethodSource("shouldNotRejectCitaData")
	void shouldNotRejectCita(final Cita cita, final IllegalArgumentException errorEsperado) throws Exception {
		//Vamos a generar un falso veterinario, como si fuera el veterinario logeado.
		//Hemos creado un método auxiliar para ello
		Veterinario loggedVeterinario = CitaServiceTests.falsoVeterinarioLogeado();

		//Llamadas del método
		Mockito.when(this.vetService.findVeterinarioByLogedUser()).thenReturn(loggedVeterinario);
		Exception errorAcceptCita = Assertions.assertThrows(IllegalArgumentException.class, () -> {
			this.citaService.declineCita(cita);
		});
		Assert.assertEquals(errorAcceptCita.getMessage(), errorEsperado.getMessage());
	}

	// ----- Métodos auxiliares

	// Método para generar una cita
	public static Cita generateCita(final Date fechaHoraInicio, final Date fechaHoraFin, final String motivo, final String rechazoJustificacion, final TipoEstadoCita estado, final Contrato contrato) {
		Cita cita = new Cita();

		//Asignaciones cita
		cita.setFechaHoraInicio(fechaHoraInicio);
		cita.setFechaHoraFin(fechaHoraFin);
		cita.setMotivo(motivo);
		cita.setRechazoJustificacion(rechazoJustificacion);
		cita.setEstado(estado);
		cita.setContrato(contrato);

		return cita;
	}

	//Método para generar un falso veterinario logeado
	public static Veterinario falsoVeterinarioLogeado() {
		//Generando la lista de ganados del veterinario
		List<TiposGanado> tiposGanadoVeterinario = new ArrayList<>();
		TiposGanado porcino = new TiposGanado();
		porcino.setTipoGanado("Porcino");
		TiposGanado caprino = new TiposGanado();
		caprino.setTipoGanado("Caprino");
		TiposGanado ovino = new TiposGanado();
		ovino.setTipoGanado("Ovino");

		tiposGanadoVeterinario.add(porcino);
		tiposGanadoVeterinario.add(caprino);
		tiposGanadoVeterinario.add(ovino);

		Veterinario loggedVeterinario = VeterinarioServiceTests.generateVeterinario("Joseph", "Joestar", "622222222", "jojo@gmail.com", "Sevilla", "Los palacios", "12345678A", tiposGanadoVeterinario, true, "hola", "hola");
		return loggedVeterinario;
	}

	//Método para generar un contrato para los casos de prueba
	//Usaremos 2 contratos diferentes para los casos de prueba

	// --- Contrato1 ---
	//Para un contrato necesitamos un veterinario. Para este contrato en concreto usaremos el
	//veterinario creado con el método falsoVeterinarioLogeado
	public static Contrato contrato1ParaPruebas() {

		//Generando un ganadero para la explotacion ganadera
		Ganadero gan1 = GanaderoServiceTests.generateGanadero("Antonio", "Banderas", "0123456789", "anton@mail.com", "Sevilla", "Dos Hermanas", "123456789A", "41345", "Calle Antonio Mairena, 8", "antonio1", "12345");

		//Generando una explotacion ganadera para el contrato
		ExplotacionGanadera expGan1 = ContratoServiceTests.generateExplotacionGanadera("361481843", "Los palacios y Villafranca", false, gan1, "Finca Trujillo");

		//Generando la lista de ganados del contrato
		TiposGanado porcino = new TiposGanado();
		porcino.setTipoGanado("Porcino");
		TiposGanado ovino = new TiposGanado();
		ovino.setTipoGanado("Ovino");
		List<TiposGanado> tiposGanadoContrato = new ArrayList<>();
		tiposGanadoContrato.add(porcino);
		tiposGanadoContrato.add(ovino);

		//Veterinario
		Veterinario vet1 = CitaServiceTests.falsoVeterinarioLogeado();

		//Contrato1
		Contrato contrato1 = ContratoServiceTests.generateContrato(tiposGanadoContrato, TipoEstadoContrato.ACEPTADO, LocalDate.of(2020, 2, 28), LocalDate.of(2020, 3, 10), LocalDate.of(2021, 6, 10), expGan1, vet1);
		return contrato1;
	}

	// --- Contrato2 ---
	//Para este contrato usaremos otro veterinario distinto
	public static Contrato contrato2ParaPruebas() {

		//Generando la lista de ganados del veterinario
		List<TiposGanado> tiposGanadoVeterinario = new ArrayList<>();
		TiposGanado porcino = new TiposGanado();
		porcino.setTipoGanado("Porcino");
		TiposGanado caprino = new TiposGanado();
		caprino.setTipoGanado("Caprino");
		TiposGanado ovino = new TiposGanado();
		ovino.setTipoGanado("Ovino");

		tiposGanadoVeterinario.add(porcino);
		tiposGanadoVeterinario.add(caprino);
		tiposGanadoVeterinario.add(ovino);

		//Generando un ganadero para la explotacion ganadera
		Ganadero gan2 = GanaderoServiceTests.generateGanadero("Antonio", "Banderas", "0123456789", "anton@mail.com", "Sevilla", "Dos Hermanas", "123456789A", "41345", "Calle Antonio Mairena, 8", "antonio1", "12345");

		//Generando una explotacion ganadera para el contrato
		ExplotacionGanadera expGan2 = ContratoServiceTests.generateExplotacionGanadera("361481843", "Los palacios y Villafranca", false, gan2, "Finca Trujillo");

		//Generando la lista de ganados del contrato
		List<TiposGanado> tiposGanadoContrato = new ArrayList<>();
		tiposGanadoContrato.add(porcino);
		tiposGanadoContrato.add(ovino);

		//Veterinario
		Veterinario vet2 = VeterinarioServiceTests.generateVeterinario("Mike", "Robinson", "633475823", "mikeRobin@gmail.com", "Sevilla", "Utrera", "58329932B", tiposGanadoVeterinario, true, "mike", "robinson");

		//Contrato1
		Contrato contrato2 = ContratoServiceTests.generateContrato(tiposGanadoContrato, TipoEstadoContrato.ACEPTADO, LocalDate.of(2020, 4, 03), LocalDate.of(2020, 5, 10), LocalDate.of(2021, 2, 10), expGan2, vet2);
		return contrato2;
	}

}
