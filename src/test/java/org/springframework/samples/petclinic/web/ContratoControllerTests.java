
package org.springframework.samples.petclinic.web;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.model.Contrato;
import org.springframework.samples.petclinic.model.ExplotacionGanadera;
import org.springframework.samples.petclinic.model.Ganadero;
import org.springframework.samples.petclinic.model.TipoEstadoContrato;
import org.springframework.samples.petclinic.model.TiposGanado;
import org.springframework.samples.petclinic.model.Veterinario;
import org.springframework.samples.petclinic.repository.springdatajpa.ExplotacionGanaderaRepository;
import org.springframework.samples.petclinic.service.ContratoService;
import org.springframework.samples.petclinic.service.ContratoServiceTests;
import org.springframework.samples.petclinic.service.ExplotacionGanaderaService;
import org.springframework.samples.petclinic.service.GanaderoServiceTests;
import org.springframework.samples.petclinic.service.VetService;
import org.springframework.samples.petclinic.service.VeterinarioServiceTests;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(
	/* Controlador a instanciar para probarlo */
	controllers = ContratoController.class,
	/* Excluir los componentes de seguridad al instanciar el controlador */
	excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
/*
 * PD: Si cargamos los componentes de seguridad, tal como esta implementado (Con roles y usuarios),
 * tambien tendríamos que cargar el contexto de la aplicacion completo llegando hasta la capad de datos.
 * Si hacemos esto, estaríamos hablando mas bien de un test de integración, no de test unitarios
 */
public class ContratoControllerTests {

	@Autowired
	private MockMvc							mockMvc;

	@MockBean
	protected ContratoService				contratoService;

	@MockBean
	protected VetService					vetService;

	@MockBean
	protected ExplotacionGanaderaService	expGanaderaService;

	@MockBean
	protected ExplotacionGanaderaRepository	explotacGanaderaRepository;

	private static final int				TEST_VET_ID				= 1;

	private static final int				TEST_CONTRATO_ID		= 1;

	private static final int				TEST_EXP_GANADERA_ID	= 1;


	@BeforeEach
	void setup() {
		List<TiposGanado> tiposGanado = new ArrayList<TiposGanado>();
		TiposGanado p = new TiposGanado();
		p.setTipoGanado("porcino");
		TiposGanado c = new TiposGanado();

		p.setTipoGanado("caprino");
		tiposGanado.add(c);
		tiposGanado.add(p);
		Veterinario vet1 = VeterinarioServiceTests.generateVeterinario("Joseph", "Joestar", "622222222", "jojo@gmail.com", "Sevilla", "Los palacios", "12345678A", tiposGanado, true, "veterinario1", "veterinario1");
		Optional<Veterinario> ovet1 = Optional.of(vet1);
		Ganadero ganaderoPos1 = GanaderoServiceTests.generateGanadero("Antonio", "Banderas", "0123456789", "anton@mail.com", "Sevilla", "Dos Hermanas", "123456789A", "41345", "Calle Antonio Mairena, 8", "ganadero1", "ganadero1");

		ExplotacionGanadera exp1 = ContratoServiceTests.generateExplotacionGanadera("2", "4", true, ganaderoPos1, "exp1");

		exp1.setId(1);

		Contrato contratoPos1 = ContratoServiceTests.generateContrato(tiposGanado, TipoEstadoContrato.PENDIENTE, LocalDate.of(2020, 2, 28), LocalDate.of(2020, 3, 1), LocalDate.of(2021, 3, 1), exp1, vet1);

		contratoPos1.setId(1);

		Iterable<ExplotacionGanadera> expo = Collections.singleton(exp1);

		Iterable<Contrato> contratito = Collections.singleton(contratoPos1);

		BDDMockito.given(this.vetService.encontrarPorID(ContratoControllerTests.TEST_VET_ID)).willReturn(ovet1);

		BDDMockito.given(this.vetService.findVeterinarioByLogedUser()).willReturn(vet1);

		BDDMockito.given(this.expGanaderaService.findExpGanaderaByGanaderoId(false)).willReturn(expo);

		Optional<Contrato> opt = Optional.empty();

		BDDMockito.given(this.contratoService.findAllContratosVigentesByExpGanaderaAndVeterinarioAndEstado(1, 1, TipoEstadoContrato.ACEPTADO)).willReturn(opt);

		BDDMockito.given(this.contratoService.findAllContratosVigentesByExpGanaderaAndVeterinarioAndEstado(1, 1, TipoEstadoContrato.PENDIENTE)).willReturn(opt);

		BDDMockito.given(this.contratoService.findContratoById(1)).willReturn(Optional.of(contratoPos1));

		BDDMockito.given(this.contratoService.findAllContratoByExpGanaderaId(1)).willReturn(contratito);
	}

	public static Contrato ContratoAceptado() {
		List<TiposGanado> tiposGanado = new ArrayList<TiposGanado>();
		TiposGanado p = new TiposGanado();
		p.setTipoGanado("porcino");
		TiposGanado c = new TiposGanado();
		p.setTipoGanado("caprino");
		tiposGanado.add(c);
		tiposGanado.add(p);
		Veterinario vet1 = VeterinarioServiceTests.generateVeterinario("Joseph", "Joestar", "622222222", "jojo@gmail.com", "Sevilla", "Los palacios", "12345678A", tiposGanado, true, "veterinario1", "veterinario1");
		Ganadero ganaderoPos1 = GanaderoServiceTests.generateGanadero("Antonio", "Banderas", "0123456789", "anton@mail.com", "Sevilla", "Dos Hermanas", "123456789A", "41345", "Calle Antonio Mairena, 8", "ganadero1", "ganadero1");
		ExplotacionGanadera exp1 = ContratoServiceTests.generateExplotacionGanadera("2", "4", true, ganaderoPos1, "exp1");
		Contrato contratoPos1 = ContratoServiceTests.generateContrato(tiposGanado, TipoEstadoContrato.ACEPTADO, LocalDate.of(2020, 2, 28), LocalDate.of(2020, 3, 1), LocalDate.of(2021, 3, 1), exp1, vet1);
		contratoPos1.setId(1);
		return contratoPos1;
	}

	@WithMockUser(value = "spring")
	@Test
	void testInitCreationForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/veterinario/{vetId}/contrato", ContratoControllerTests.TEST_VET_ID)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("contrato/createContratoForm"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("contrato"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testCrearContratoGET() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/veterinario/{vetId}/contrato", ContratoControllerTests.TEST_VET_ID))
			/* 1. Comprobamos que se recibe una respuesta HTTP correcta */
			.andExpect(MockMvcResultMatchers.status().isOk())
			/* 2. Comprobamos el nombre de la vista */
			.andExpect(MockMvcResultMatchers.view().name("contrato/createContratoForm"))
			/* 3. Comprobamos que recibimos un modelAttribute Ganadero */
			.andExpect(MockMvcResultMatchers.model().attributeExists("contrato"))
			/* 4. Comprobamos que Ganadero tiene las propiedades esperadas */
			.andExpect(MockMvcResultMatchers.model().attribute("contrato", Matchers.hasProperty("fechaPeticion", Matchers.notNullValue())))
			.andExpect(MockMvcResultMatchers.model().attribute("contrato", Matchers.hasProperty("fechaInicial", Matchers.blankOrNullString())))
			.andExpect(MockMvcResultMatchers.model().attribute("contrato", Matchers.hasProperty("fechaFinal", Matchers.blankOrNullString())))
			.andExpect(MockMvcResultMatchers.model().attribute("contrato", Matchers.hasProperty("ganados", Matchers.blankOrNullString())))
			.andExpect(MockMvcResultMatchers.model().attribute("contrato", Matchers.hasProperty("explotacionGanadera", Matchers.blankOrNullString())))
			.andExpect(MockMvcResultMatchers.model().attribute("contrato", Matchers.hasProperty("veterinario", Matchers.notNullValue())));

	}

	@WithMockUser(value = "spring")
	@Test
	void testPositivoCrearGanadoPOST() throws Exception {
		this.mockMvc
			.perform(MockMvcRequestBuilders.post("/veterinario/{vetId}/contrato", ContratoControllerTests.TEST_VET_ID).with(SecurityMockMvcRequestPostProcessors.csrf()).param("fechaInicial", "2020/12/01").param("fechaFinal", "2021/12/01")
				.param("ganados.id", "1").param("explotacionGanadera.id", "1").param("veterinario.id", "1"))
			.andExpect(MockMvcResultMatchers.view().name("redirect:/ganadero/explotacion-ganadera/1/contrato/list")).andExpect(MockMvcResultMatchers.status().is3xxRedirection());
	}

	@WithMockUser(value = "spring")
	@Test
	void testNegativoCrearGanadoPOST() throws Exception {
		this.mockMvc
			.perform(MockMvcRequestBuilders.post("/veterinario/{vetId}/contrato", ContratoControllerTests.TEST_VET_ID).with(SecurityMockMvcRequestPostProcessors.csrf()).param("fechaFinal", "2021/12/01").param("ganados.id", "1")
				.param("explotacionGanadera.id", "1").param("veterinario.id", "1"))
			.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("contrato", "fechaInicial")).andExpect(MockMvcResultMatchers.view().name("contrato/createContratoForm")).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

	}

	@WithMockUser(value = "spring")
	@Test
	void testListContratos() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/contrato/list")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("contrato/listarContratos"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("contratos"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testShowContrato() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/contrato/{contratoId}/show", ContratoControllerTests.TEST_CONTRATO_ID)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("contratoMostrado"))
			.andExpect(MockMvcResultMatchers.view().name("contrato/showContrato"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testAcceptContrato() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/contrato/{contratoId}/accept", ContratoControllerTests.TEST_CONTRATO_ID)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("contrato/listarContratos"));

	}

	@WithMockUser(value = "spring")
	@Test
	void testRefuseContrato() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/contrato/{contratoId}/refuse", ContratoControllerTests.TEST_CONTRATO_ID)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("contrato/listarContratos"));

	}

	@WithMockUser(value = "spring")
	@Test
	void testConcludeContrato() throws Exception {
		BDDMockito.given(this.contratoService.findContratoById(1)).willReturn(Optional.of(ContratoControllerTests.ContratoAceptado()));
		this.mockMvc.perform(MockMvcRequestBuilders.get("/contrato/{contratoId}/conclude", ContratoControllerTests.TEST_CONTRATO_ID)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("contrato/listarContratos"));

	}

	@WithMockUser(value = "spring")
	@Test
	void testlistarContratosExplotacionGanadera() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/ganadero/explotacion-ganadera/{expGanaderaId}/contrato/list", ContratoControllerTests.TEST_EXP_GANADERA_ID)).andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("contrato/listarContratos")).andExpect(MockMvcResultMatchers.model().attributeExists("contratos"));
	}


	@DisplayName("Custom ContratoValidator tests")
	@Nested
	class ValidatorCustomValidatorTests {

		@BeforeEach
		void reserMockups() {
			Mockito.reset(ContratoControllerTests.this.contratoService, ContratoControllerTests.this.vetService, ContratoControllerTests.this.expGanaderaService, ContratoControllerTests.this.explotacGanaderaRepository);
		}

		@WithMockUser(value = "spring")
		@Test
		public void shouldRejectPresentContratoAceptado() throws Exception {

			List<TiposGanado> tiposGanado = new ArrayList<TiposGanado>();
			List<TiposGanado> tiposGanado2 = new ArrayList<TiposGanado>();
			TiposGanado p = new TiposGanado();
			p.setTipoGanado("porcino");
			p.setId(1);
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
			exp1.setId(1);
			ExplotacionGanadera exp2 = ContratoServiceTests.generateExplotacionGanadera("4", "8", true, ganaderoPos2, "exp2");

			Contrato contratoPos1 = ContratoServiceTests.generateContrato(tiposGanado, TipoEstadoContrato.ACEPTADO, LocalDate.of(2020, 2, 28), LocalDate.of(2020, 3, 1), LocalDate.of(2021, 3, 1), exp1, vet1);
			Optional<Contrato> conOp1 = Optional.of(contratoPos1);
			Contrato contratoPos2 = ContratoServiceTests.generateContrato(tiposGanado2, TipoEstadoContrato.PENDIENTE, LocalDate.of(2020, 2, 28), LocalDate.of(2020, 3, 1), LocalDate.of(2021, 3, 1), exp2, vet2);
			Optional<Contrato> conOp2 = Optional.of(contratoPos2);

			Mockito.when(ContratoControllerTests.this.contratoService.findAllContratosVigentesByExpGanaderaAndVeterinarioAndEstado(1, 1, TipoEstadoContrato.ACEPTADO)).thenReturn(conOp1);
			Mockito.when(ContratoControllerTests.this.contratoService.findAllContratosVigentesByExpGanaderaAndVeterinarioAndEstado(1, 1, TipoEstadoContrato.PENDIENTE)).thenReturn(Optional.empty());
			BDDMockito.given(ContratoControllerTests.this.vetService.encontrarPorID(ContratoControllerTests.TEST_VET_ID)).willReturn(Optional.of(vet1));

			ContratoControllerTests.this.mockMvc
				.perform(MockMvcRequestBuilders.post("/veterinario/{vetId}/contrato", ContratoControllerTests.TEST_VET_ID).with(SecurityMockMvcRequestPostProcessors.csrf()).param("fechaInicial", "2020/12/01").param("fechaFinal", "2021/12/01")
					.param("ganados.id", "1").param("explotacionGanadera.id", "1").param("veterinario.id", "1"))
				.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("contrato/createContratoForm")).andExpect(MockMvcResultMatchers.model().attributeHasErrors("contrato"))
				.andExpect(MockMvcResultMatchers.model().attributeErrorCount("contrato", 1)).andExpect(MockMvcResultMatchers.model().attributeHasFieldErrorCode("contrato", "fechaFinal", "contrato.existeUnContratoAceptado"));

		}

		@WithMockUser(value = "spring")
		@Test
		public void shouldRejectPresentContratoPendiente() throws Exception {

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
			Optional<Contrato> conOp1 = Optional.of(contratoPos1);
			Contrato contratoPos2 = ContratoServiceTests.generateContrato(tiposGanado2, TipoEstadoContrato.PENDIENTE, LocalDate.of(2020, 2, 28), LocalDate.of(2020, 3, 1), LocalDate.of(2021, 3, 1), exp2, vet2);
			Optional<Contrato> conOp2 = Optional.of(contratoPos2);

			Mockito.when(ContratoControllerTests.this.contratoService.findAllContratosVigentesByExpGanaderaAndVeterinarioAndEstado(contratoPos1.getExplotacionGanadera().getId(), contratoPos1.getVeterinario().getId(), TipoEstadoContrato.ACEPTADO))
				.thenReturn(Optional.empty());
			Mockito.when(ContratoControllerTests.this.contratoService.findAllContratosVigentesByExpGanaderaAndVeterinarioAndEstado(1, 1, TipoEstadoContrato.PENDIENTE)).thenReturn(conOp1);
			BDDMockito.given(ContratoControllerTests.this.vetService.encontrarPorID(ContratoControllerTests.TEST_VET_ID)).willReturn(Optional.of(vet1));
			ContratoControllerTests.this.mockMvc
				.perform(MockMvcRequestBuilders.post("/veterinario/{vetId}/contrato", ContratoControllerTests.TEST_VET_ID).with(SecurityMockMvcRequestPostProcessors.csrf()).param("fechaInicial", "2020/12/01").param("fechaFinal", "2021/12/01")
					.param("ganados.id", "1").param("explotacionGanadera.id", "1").param("veterinario.id", "1"))
				.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeHasErrors("contrato")).andExpect(MockMvcResultMatchers.model().attributeErrorCount("contrato", 1))
				.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrorCode("contrato", "fechaFinal", "contrato.existeUnContratoPendiente"));

		}

		@WithMockUser(value = "spring")
		@Test
		public void shouldRejectFinalDateBeforeInitialContrato() throws Exception {

			List<TiposGanado> tiposGanado = new ArrayList<TiposGanado>();
			List<TiposGanado> tiposGanado2 = new ArrayList<TiposGanado>();
			TiposGanado p = new TiposGanado();
			p.setId(1);
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
			exp1.setId(1);
			ExplotacionGanadera exp2 = ContratoServiceTests.generateExplotacionGanadera("4", "8", true, ganaderoPos2, "exp2");

			Contrato contratoPos1 = ContratoServiceTests.generateContrato(tiposGanado, TipoEstadoContrato.PENDIENTE, LocalDate.of(2020, 2, 28), LocalDate.of(2020, 3, 1), LocalDate.of(2021, 3, 1), exp1, vet1);
			Optional<Contrato> conOp1 = Optional.of(contratoPos1);
			Contrato contratoPos2 = ContratoServiceTests.generateContrato(tiposGanado2, TipoEstadoContrato.PENDIENTE, LocalDate.of(2020, 2, 28), LocalDate.of(2020, 3, 1), LocalDate.of(2021, 3, 1), exp2, vet2);
			Optional<Contrato> conOp2 = Optional.of(contratoPos2);

			Mockito.when(ContratoControllerTests.this.contratoService.findAllContratosVigentesByExpGanaderaAndVeterinarioAndEstado(contratoPos1.getExplotacionGanadera().getId(), contratoPos1.getVeterinario().getId(), TipoEstadoContrato.ACEPTADO))
				.thenReturn(Optional.empty());
			Mockito.when(ContratoControllerTests.this.contratoService.findAllContratosVigentesByExpGanaderaAndVeterinarioAndEstado(1, 1, TipoEstadoContrato.PENDIENTE)).thenReturn(Optional.empty());
			BDDMockito.given(ContratoControllerTests.this.vetService.encontrarPorID(ContratoControllerTests.TEST_VET_ID)).willReturn(Optional.of(vet1));
			ContratoControllerTests.this.mockMvc
				.perform(MockMvcRequestBuilders.post("/veterinario/{vetId}/contrato", ContratoControllerTests.TEST_VET_ID).with(SecurityMockMvcRequestPostProcessors.csrf()).param("fechaInicial", "2021/12/01").param("fechaFinal", "2020/12/01")
					.param("ganados.id", "1").param("explotacionGanadera.id", "1").param("veterinario.id", "1"))
				.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeHasErrors("contrato")).andExpect(MockMvcResultMatchers.model().attributeErrorCount("contrato", 1))
				.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrorCode("contrato", "fechaFinal", "contrato.fechaFinal.isBeforeFechaInic"));

		}

		@WithMockUser(value = "spring")
		@Test
		public void shouldRejectFinalDateBeforePeticionContrato() throws Exception {

			List<TiposGanado> tiposGanado = new ArrayList<TiposGanado>();
			List<TiposGanado> tiposGanado2 = new ArrayList<TiposGanado>();
			TiposGanado p = new TiposGanado();
			p.setTipoGanado("porcino");
			p.setId(1);
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
			exp1.setId(1);
			ExplotacionGanadera exp2 = ContratoServiceTests.generateExplotacionGanadera("4", "8", true, ganaderoPos2, "exp2");

			Contrato contratoPos1 = ContratoServiceTests.generateContrato(tiposGanado, TipoEstadoContrato.PENDIENTE, LocalDate.of(2020, 2, 28), LocalDate.of(2020, 3, 1), LocalDate.of(2021, 3, 1), exp1, vet1);
			Optional<Contrato> conOp1 = Optional.of(contratoPos1);
			Contrato contratoPos2 = ContratoServiceTests.generateContrato(tiposGanado2, TipoEstadoContrato.PENDIENTE, LocalDate.of(2020, 2, 28), LocalDate.of(2020, 3, 1), LocalDate.of(2021, 3, 1), exp2, vet2);
			Optional<Contrato> conOp2 = Optional.of(contratoPos2);

			Mockito.when(ContratoControllerTests.this.contratoService.findAllContratosVigentesByExpGanaderaAndVeterinarioAndEstado(contratoPos1.getExplotacionGanadera().getId(), contratoPos1.getVeterinario().getId(), TipoEstadoContrato.ACEPTADO))
				.thenReturn(Optional.empty());
			Mockito.when(ContratoControllerTests.this.contratoService.findAllContratosVigentesByExpGanaderaAndVeterinarioAndEstado(1, 1, TipoEstadoContrato.PENDIENTE)).thenReturn(Optional.empty());
			BDDMockito.given(ContratoControllerTests.this.vetService.encontrarPorID(ContratoControllerTests.TEST_VET_ID)).willReturn(Optional.of(vet1));
			ContratoControllerTests.this.mockMvc
				.perform(MockMvcRequestBuilders.post("/veterinario/{vetId}/contrato", ContratoControllerTests.TEST_VET_ID).with(SecurityMockMvcRequestPostProcessors.csrf()).param("fechaInicial", "2002/12/01").param("fechaFinal", "2021/12/01")
					.param("ganados.id", "1").param("explotacionGanadera.id", "1").param("veterinario.id", "1"))
				.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeHasErrors("contrato")).andExpect(MockMvcResultMatchers.model().attributeErrorCount("contrato", 1))
				.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrorCode("contrato", "fechaInicial", "contrato.fechaInicial.isBeforeFechaPet"));

		}
	}
}
