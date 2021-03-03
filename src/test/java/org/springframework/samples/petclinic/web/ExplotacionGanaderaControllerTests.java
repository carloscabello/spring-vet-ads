
package org.springframework.samples.petclinic.web;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.assertj.core.api.Assertions;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.model.Animal;
import org.springframework.samples.petclinic.model.Contrato;
import org.springframework.samples.petclinic.model.ExplotacionGanadera;
import org.springframework.samples.petclinic.model.Ganadero;
import org.springframework.samples.petclinic.model.TipoEstadoContrato;
import org.springframework.samples.petclinic.model.TiposGanado;
import org.springframework.samples.petclinic.model.Veterinario;
import org.springframework.samples.petclinic.repository.springdatajpa.TiposGanadoRepository;
import org.springframework.samples.petclinic.service.AnimalService;
import org.springframework.samples.petclinic.service.ContratoService;
import org.springframework.samples.petclinic.service.ContratoServiceTests;
import org.springframework.samples.petclinic.service.ExplotacionGanaderaService;
import org.springframework.samples.petclinic.service.GanaderoServiceTests;
import org.springframework.samples.petclinic.service.VeterinarioServiceTests;
import org.springframework.samples.petclinic.web.utils.FieldErrorCount;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@WebMvcTest(controllers = ExplotacionGanaderaController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
/* Controlador a instanciar para probarlo */
/* Excluir los componentes de seguridad al instanciar el controlador */
/*
 * PD: Si cargamos los componentes de seguridad, tal como esta implementado (Con roles y usuarios),
 * tambien tendríamos que cargar el contexto de la aplicacion completo llegando hasta la capad de datos.
 * Si hacemos esto, estaríamos hablando mas bien de un test de integración, no de test unitarios
 */
public class ExplotacionGanaderaControllerTests {

	@Autowired
	private MockMvc						mockMvc;

	@MockBean
	private ExplotacionGanaderaService	expService;

	@MockBean
	private AnimalService				animalService;

	@MockBean
	private ContratoService				contratoService;

	@MockBean
	private TiposGanadoRepository		tiposGanadoRepository;

	private static final int			TEST_EXPLOTACIONGANADERA_ID	= 1;


	@BeforeEach
	void setup() {
		Ganadero ganadero1 = GanaderoServiceTests.generateGanadero("Sora", "Riku", "622222222", "kingdom@gmail.com", "Badajoz", "Hinojosa del Valle", "45556839L", "06226", "Calle Islas del Destino", "soriku", "kairi4e");
		ExplotacionGanadera expGanadera1 = ContratoServiceTests.generateExplotacionGanadera("1", "Los Antruejos", false, ganadero1, "Villaricos");
		expGanadera1.setId(ExplotacionGanaderaControllerTests.TEST_EXPLOTACIONGANADERA_ID);
		Optional<ExplotacionGanadera> expGanaderaO = Optional.of(expGanadera1);
		BDDMockito.given(this.expService.findExpGanaderaById(ExplotacionGanaderaControllerTests.TEST_EXPLOTACIONGANADERA_ID)).willReturn(expGanaderaO);

		TiposGanado tipoGanado1 = new TiposGanado();
		tipoGanado1.setTipoGanado("Ovino");
		tipoGanado1.setId(2);
		List<TiposGanado> tiposGanado = new ArrayList<>();
		tiposGanado.add(tipoGanado1);
		Veterinario vet1 = VeterinarioServiceTests.generateVeterinario("Joseph", "Joestar", "622222222", "jojo@gmail.com", "Sevilla", "Los palacios", "12345678A", tiposGanado, true, "hola", "hola");
		List<Contrato> contratos = new ArrayList<>();
		Contrato contratoP = ContratoServiceTests.generateContrato(tiposGanado, TipoEstadoContrato.PENDIENTE, LocalDate.of(2020, 2, 28), LocalDate.of(2020, 3, 1), LocalDate.of(2021, 3, 1), expGanadera1, vet1);
		contratos.add(contratoP);
		BDDMockito.given(this.contratoService.findAllContratoByExpGanaderaAndEstado(ExplotacionGanaderaControllerTests.TEST_EXPLOTACIONGANADERA_ID, TipoEstadoContrato.PENDIENTE)).willReturn(contratos);

	}

	@WithMockUser(value = "spring")
	@Test
	void testPositivosalvarExplotacionGanaderaArchivadaGET() throws Exception {
		BDDMockito.given(this.animalService.findAllAnimalByExpId(ExplotacionGanaderaControllerTests.TEST_EXPLOTACIONGANADERA_ID, false)).willReturn(new ArrayList<>());
		this.mockMvc.perform(MockMvcRequestBuilders.get("/ganadero/explotacion-ganadera/{expId}/archivarExpGanadera", ExplotacionGanaderaControllerTests.TEST_EXPLOTACIONGANADERA_ID)).andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("explotacionGanadera/listaExplotacionesGanadero"));
		Mockito.verify(this.animalService).findAllAnimalByExpId(ExplotacionGanaderaControllerTests.TEST_EXPLOTACIONGANADERA_ID, false);

		Mockito.verify(this.expService).findExpGanaderaById(ExplotacionGanaderaControllerTests.TEST_EXPLOTACIONGANADERA_ID);
		Optional<ExplotacionGanadera> expGanadera = this.expService.findExpGanaderaById(ExplotacionGanaderaControllerTests.TEST_EXPLOTACIONGANADERA_ID);
		Mockito.verify(this.expService).archivarExpGanadera(expGanadera.get());

		Mockito.verify(this.contratoService).findAllContratoByExpGanaderaAndEstado(ExplotacionGanaderaControllerTests.TEST_EXPLOTACIONGANADERA_ID, TipoEstadoContrato.ACEPTADO);

		Mockito.verify(this.contratoService).findAllContratoByExpGanaderaAndEstado(ExplotacionGanaderaControllerTests.TEST_EXPLOTACIONGANADERA_ID, TipoEstadoContrato.PENDIENTE);
		List<Contrato> contratosPendientes = (List<Contrato>) this.contratoService.findAllContratoByExpGanaderaAndEstado(ExplotacionGanaderaControllerTests.TEST_EXPLOTACIONGANADERA_ID, TipoEstadoContrato.PENDIENTE);
		for (Contrato c : contratosPendientes) {
			Mockito.verify(this.contratoService).deleteContrato(c);
		}
	}

	@WithMockUser(value = "spring")
	@Test
	void testNegativosalvarExplotacionGanaderaArchivadaGET() throws Exception {
		Animal animal = new Animal();
		List<Animal> animales = new ArrayList<>();
		animales.add(animal);
		BDDMockito.given(this.animalService.findAllAnimalByExpId(ExplotacionGanaderaControllerTests.TEST_EXPLOTACIONGANADERA_ID, false)).willReturn(animales);

		this.mockMvc.perform(MockMvcRequestBuilders.get("/ganadero/explotacion-ganadera/{expId}/archivarExpGanadera", ExplotacionGanaderaControllerTests.TEST_EXPLOTACIONGANADERA_ID)).andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("explotacionGanadera/listaExplotacionesGanadero"));

		Mockito.verify(this.animalService).findAllAnimalByExpId(ExplotacionGanaderaControllerTests.TEST_EXPLOTACIONGANADERA_ID, false);
	}

	@WithMockUser(value = "spring")
	@Test
	void testCrearExplotacionGET() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/ganadero/explotacion-ganadera/new"))
			/* 1. Comprobamos que se recibe una respuesta HTTP correcta */
			.andExpect(MockMvcResultMatchers.status().isOk())
			/* 2. Comprobamos el nombre de la vista */
			.andExpect(MockMvcResultMatchers.view().name("explotacionGanadera/editExpGanadera"))
			/* 3. Comprobamos que recibimos un modelAttribute Veterinario */
			.andExpect(MockMvcResultMatchers.model().attributeExists("explotacionGanadera"))
			/* 4. Comprobamos que Veterinario tiene las propiedades esperadas */
			.andExpect(MockMvcResultMatchers.model().attribute("explotacionGanadera", Matchers.hasProperty("name", Matchers.blankOrNullString())))
			.andExpect(MockMvcResultMatchers.model().attribute("explotacionGanadera", Matchers.hasProperty("numeroRegistro", Matchers.blankOrNullString())))
			.andExpect(MockMvcResultMatchers.model().attribute("explotacionGanadera", Matchers.hasProperty("terminoMunicipal", Matchers.blankOrNullString())));
	}

	@WithMockUser(value = "spring")
	@ParameterizedTest()
	@CsvSource({
		"El Moncayo, 53728343, Granada", "Finca El gallo peleón, 123456789, Lebrija", "Finca Santa Luisa, 18792837, Écija"
	})
	void testsPositivosCrearExplotacionPOST(final String name, final String numeroRegistro, final String terminoMunicipal) throws Exception {

		/*
		 * 1. Arrange
		 * Preparamos la peticion POST con los parametros correspondientes
		 */
		MultiValueMap<String, String> veterinarioParams = ExplotacionGanaderaControllerTests.simpleExplotacionGanaderaParams(name, numeroRegistro, terminoMunicipal);
		MockHttpServletRequestBuilder postRequest = MockMvcRequestBuilders.post("/ganadero/explotacion-ganadera/new").with(SecurityMockMvcRequestPostProcessors.csrf()).params(veterinarioParams);

		/*
		 * 2. Act
		 * Enviamos la peticion POST
		 */
		ResultActions postResultActions = this.mockMvc.perform(postRequest)
			/*
			 * Comprobamos que se recibe una respuesta HTTP correcta
			 * y se devuelve a la vista correcta
			 */
			.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("explotacionGanadera/listaExplotacionesGanadero"));

		/*
		 * 3.Assert
		 * Comprobamos que no se devuelven errores
		 */
		ArgumentCaptor<ExplotacionGanadera> argument = ArgumentCaptor.forClass(ExplotacionGanadera.class);
		postResultActions.andExpect(MockMvcResultMatchers.model().attributeExists("message"));
		Mockito.verify(this.expService).saveExpGanadera(argument.capture());
		Assertions.assertThat(argument.getValue().getName()).isEqualTo(name);
		Assertions.assertThat(argument.getValue().getNumeroRegistro()).isEqualTo(numeroRegistro);
		Assertions.assertThat(argument.getValue().getTerminoMunicipal()).isEqualTo(terminoMunicipal);
	}

	private static Stream<Arguments> testsNegativosCrearExplotacionData() {
		Stream<Arguments> res;

		MultiValueMap<String, String> explotacionNeg1 = ExplotacionGanaderaControllerTests.simpleExplotacionGanaderaParams("", "", "");
		List<FieldErrorCount> explotacionNeg1ErrorFields = Stream.of(new FieldErrorCount("name", 1), new FieldErrorCount("numeroRegistro", 1), new FieldErrorCount("terminoMunicipal", 1)).collect(Collectors.toList());

		MultiValueMap<String, String> explotacionNeg2 = ExplotacionGanaderaControllerTests.simpleExplotacionGanaderaParams(null, null, null);
		List<FieldErrorCount> explotacionNeg2ErrorFields = Stream.of(new FieldErrorCount("numeroRegistro", 2), new FieldErrorCount("terminoMunicipal", 2)).collect(Collectors.toList());

		MultiValueMap<String, String> explotacionNeg3 = ExplotacionGanaderaControllerTests.simpleExplotacionGanaderaParams("A", "28472345", "Lebrija");
		List<FieldErrorCount> explotacionNeg3ErrorFields = Stream.of(new FieldErrorCount("name", 1)).collect(Collectors.toList());

		MultiValueMap<String, String> explotacionNeg4 = ExplotacionGanaderaControllerTests.simpleExplotacionGanaderaParams("123456789012345678901234567890123456789012345678901", "28472345", "Lebrija");
		List<FieldErrorCount> explotacionNeg4ErrorFields = Stream.of(new FieldErrorCount("name", 1)).collect(Collectors.toList());

		MultiValueMap<String, String> explotacionNeg5 = ExplotacionGanaderaControllerTests.simpleExplotacionGanaderaParams("", "28472345", "Lebrija");
		List<FieldErrorCount> explotacionNeg5ErrorFields = Stream.of(new FieldErrorCount("name", 1)).collect(Collectors.toList());

		MultiValueMap<String, String> explotacionNeg6 = ExplotacionGanaderaControllerTests.simpleExplotacionGanaderaParams("Montehermoso SA", "", "");
		List<FieldErrorCount> explotacionNeg6ErrorFields = Stream.of(new FieldErrorCount("numeroRegistro", 1), new FieldErrorCount("terminoMunicipal", 1)).collect(Collectors.toList());

		res = Stream.of(Arguments.of(explotacionNeg1, explotacionNeg1ErrorFields), Arguments.of(explotacionNeg2, explotacionNeg2ErrorFields), Arguments.of(explotacionNeg3, explotacionNeg3ErrorFields),
			Arguments.of(explotacionNeg4, explotacionNeg4ErrorFields), Arguments.of(explotacionNeg5, explotacionNeg5ErrorFields), Arguments.of(explotacionNeg6, explotacionNeg6ErrorFields));
		return res;
	}

	@WithMockUser(value = "spring")
	@ParameterizedTest()
	@MethodSource("testsNegativosCrearExplotacionData")
	void testsNegativosCrearExplotacionPOST(final MultiValueMap<String, String> explotacionParams, final List<FieldErrorCount> fieldsWithErrors) throws Exception {
		/*
		 * 1. Arrange
		 * Preparamos la peticion POST con los parametros correspondientes
		 */
		MockHttpServletRequestBuilder postRequest = MockMvcRequestBuilders.post("/ganadero/explotacion-ganadera/new").with(SecurityMockMvcRequestPostProcessors.csrf()).params(explotacionParams);

		/*
		 * 2. Act
		 * Enviamos la peticion POST
		 */
		ResultActions postResultActions = this.mockMvc.perform(postRequest)
			/* Comprobamos que se recibe una respuesta HTTP correcta */
			.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("explotacionGanadera/editExpGanadera")).andExpect(MockMvcResultMatchers.model().attributeExists("explotacionGanadera"))
			.andExpect(MockMvcResultMatchers.model().attributeHasErrors("explotacionGanadera"));

		/*
		 * 3.Assert
		 * Comprobamos que se reciben todos los errores esperados
		 */
		//System.out.println(postResultActions.andReturn().getModelAndView().getModel());
		Integer expectedErrorCount = FieldErrorCount.total(fieldsWithErrors);

		postResultActions.andExpect(MockMvcResultMatchers.model().attributeErrorCount("explotacionGanadera", expectedErrorCount));
		for (FieldErrorCount fieldName : fieldsWithErrors) {
			postResultActions.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("explotacionGanadera", fieldName.getFieldName()));
		}
		ArgumentCaptor<ExplotacionGanadera> argument = ArgumentCaptor.forClass(ExplotacionGanadera.class);
		Mockito.verify(this.expService, Mockito.never()).saveExpGanadera(argument.capture());
	}

	@WithMockUser(value = "spring")
	@Test
	void testListarExplotacionGanadero() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/ganadero/explotacion-ganadera/list")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("explotacionGanadera/listaExplotacionesGanadero"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("expGanaderas")).andExpect(MockMvcResultMatchers.model().attributeExists("todosTiposG"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testListarExplotacionVeterinario() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/veterinario/explotacion-ganadera/list")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("explotacionGanadera/listaExplotacionesVeterinario"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("contratos")).andExpect(MockMvcResultMatchers.model().attributeExists("todosTiposG"));
	}

	public static MultiValueMap<String, String> simpleExplotacionGanaderaParams(final String name, final String numeroRegistro, final String terminoMunicipal) {
		MultiValueMap<String, String> res = new LinkedMultiValueMap<String, String>();
		res.add("name", name);
		res.add("numeroRegistro", numeroRegistro);
		res.add("terminoMunicipal", terminoMunicipal);
		return res;
	}

}
