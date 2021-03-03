
package org.springframework.samples.petclinic.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.samples.petclinic.model.Contrato;
import org.springframework.samples.petclinic.model.ExplotacionGanadera;
import org.springframework.samples.petclinic.model.Ganadero;
import org.springframework.samples.petclinic.model.TipoEstadoContrato;
import org.springframework.samples.petclinic.model.TiposGanado;
import org.springframework.samples.petclinic.model.Veterinario;
import org.springframework.samples.petclinic.repository.springdatajpa.ContratoRepository;

@ExtendWith(MockitoExtension.class)
public class ContratoServiceTests {

	@Mock
	private ContratoRepository	contratoRepository;

	@Mock
	protected VetService		vetService;

	@InjectMocks
	protected ContratoService	contratoService;

	private static final int	TEST_VET_ID	= 1;


	private static Stream<Contrato> shouldInsertContratoData() {

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
	private static Contrato contratoParaTests1() {
		return ContratoServiceTests.shouldInsertContratoData().collect(Collectors.toList()).get(0);
	}
	private static Contrato contratoParaTests2() {
		return ContratoServiceTests.shouldInsertContratoData().collect(Collectors.toList()).get(1);
	}
	private static Veterinario vetFalsoLogged() {
		return ContratoServiceTests.shouldInsertContratoData().collect(Collectors.toList()).get(0).getVeterinario();
	}

	@ParameterizedTest()
	@MethodSource("shouldInsertContratoData")
	void shouldInsertContrato(final Contrato contrato) {
		this.contratoService.saveContrato(contrato);
		Mockito.verify(this.contratoRepository).save(contrato);
	}

	private static Stream<Contrato> shouldNotInsertContratoData() {

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

		Contrato contratoNeg1 = ContratoServiceTests.generateContrato(tiposGanado, null, LocalDate.of(2020, 2, 28), LocalDate.of(2020, 3, 1), LocalDate.of(2021, 3, 1), exp1, vet1);
		//Contrato contratoPos2 = ContratoServiceTests.generateContrato(tiposGanado2, TipoEstadoContrato.PENDIENTE, LocalDate.of(2020, 2, 28), LocalDate.of(2020, 3, 1), LocalDate.of(2021, 3, 1), exp2, vet2);

		res = Stream.of(contratoNeg1);
		return res;
	}
	@ParameterizedTest()
	@MethodSource("shouldNotInsertContratoData")
	void shouldNotInsertContrato(final Contrato contrato) throws Exception {
		this.contratoService.saveContrato(contrato);
		Mockito.verify(this.contratoRepository).save(contrato);
	}

	@ParameterizedTest
	@MethodSource("shouldInsertContratoData")
	void findAllContratoVigentesByLoguedVetTest(final Contrato contrato) throws Exception {
		Veterinario vet1 = contrato.getVeterinario();
		Iterable<Contrato> contI = Collections.singleton(contrato);
		BDDMockito.given(this.vetService.findVeterinarioByLogedUser()).willReturn(vet1);
		BDDMockito.given(this.contratoRepository.findAllContratosVigentesByVeterinarioId(vet1.getId())).willReturn(contI);
		this.contratoService.findAllContratoVigentesByLoguedVeterinario();
		Mockito.verify(this.vetService).findVeterinarioByLogedUser();
		Mockito.verify(this.contratoRepository).findAllContratosVigentesByVeterinarioId(vet1.getId());
	}
	@ParameterizedTest
	@MethodSource("shouldInsertContratoData")
	public void findAllContratoByExpGanaderaAndEstadoTest(final Contrato contrato) throws Exception {
		this.contratoService.findAllContratoByExpGanaderaAndEstado(contrato.getExplotacionGanadera().getId(), contrato.getEstado());
		Mockito.verify(this.contratoRepository).findAllContratosByExpGanaderaAndEstado(contrato.getExplotacionGanadera().getId(), contrato.getEstado());
	}

	@ParameterizedTest
	@MethodSource("shouldInsertContratoData")
	public void findContratoVigenteByExpGanaderaAndVetAndEstadoTest(final Contrato contrato) throws Exception {
		this.contratoService.findAllContratosVigentesByExpGanaderaAndVeterinarioAndEstado(contrato.getExplotacionGanadera().getId(), contrato.getVeterinario().getId(), contrato.getEstado());
		Mockito.verify(this.contratoRepository).findAllContratosVigentesByExpGanaderaAndVeterinarioAndEstado(contrato.getExplotacionGanadera().getId(), contrato.getVeterinario().getId(), contrato.getEstado());
	}

	@ParameterizedTest
	@MethodSource("shouldInsertContratoData")
	public void findAllContratoByExpGanaderaIdTest(final Contrato contrato) throws Exception {
		this.contratoService.findAllContratoByExpGanaderaId(contrato.getId());
		Mockito.verify(this.contratoRepository).findAllContratosByExpGanaderaId(contrato.getId());
	}

	public static ExplotacionGanadera generateExplotacionGanadera(final String numeroRegistro, final String terminoMunicipal, final Boolean esArchivado, final Ganadero ganadero, final String name) {

		ExplotacionGanadera exp = new ExplotacionGanadera();
		exp.setEsArchivado(esArchivado);
		exp.setGanadero(ganadero);
		exp.setNumeroRegistro(numeroRegistro);
		exp.setTerminoMunicipal(terminoMunicipal);
		exp.setName(name);

		return exp;
	}

	public static Contrato generateContrato(final List<TiposGanado> ganados, final TipoEstadoContrato estado, final LocalDate fechaPeticion, final LocalDate fechaInicial, final LocalDate fechaFinal, final ExplotacionGanadera explotacionGanadera,
		final Veterinario veterinario) {
		Contrato c = new Contrato();
		c.setGanados(ganados);
		c.setEstado(estado);
		c.setFechaPeticion(fechaPeticion);
		c.setFechaInicial(fechaInicial);
		c.setFechaFinal(fechaFinal);
		c.setExplotacionGanadera(explotacionGanadera);
		c.setVeterinario(veterinario);
		return c;
	}
}
