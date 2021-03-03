package org.springframework.samples.petclinic.web;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.blankOrNullString;
import static org.hamcrest.Matchers.hasProperty;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.BDDMockito;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.model.Animal;
import org.springframework.samples.petclinic.model.ExplotacionGanadera;
import org.springframework.samples.petclinic.model.Lote;
import org.springframework.samples.petclinic.model.TipoSexo;
import org.springframework.samples.petclinic.model.TiposGanado;
import org.springframework.samples.petclinic.repository.springdatajpa.TiposGanadoRepository;
import org.springframework.samples.petclinic.service.AnimalHistoricoService;
import org.springframework.samples.petclinic.service.AnimalService;
import org.springframework.samples.petclinic.service.AnimalServiceTests;
import org.springframework.samples.petclinic.service.ExplotacionGanaderaService;
import org.springframework.samples.petclinic.service.LoteService;
import org.springframework.samples.petclinic.service.TipoGanadoServiceTests;
import org.springframework.samples.petclinic.web.e2e.AnimalControllerE2ETests;
import org.springframework.samples.petclinic.web.utils.FieldErrorCount;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
@WebMvcTest(controllers = AnimalController.class, excludeFilters = 
@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = 
WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
/* Controlador a instanciar para probarlo */
/* Excluir los componentes de seguridad al instanciar el controlador */
/*
 * PD: Si cargamos los componentes de seguridad, tal como esta implementado (Con roles y usuarios),
 * tambien tendríamos que cargar el contexto de la aplicacion completo llegando hasta la capad de datos.
 * Si hacemos esto, estaríamos hablando mas bien de un test de integración, no de test unitarios
 */
public class AnimalControllerTests {

	@Autowired
	private MockMvc							mockMvc;

	@MockBean
	private AnimalService					animalService;

	@MockBean
	private ExplotacionGanaderaService		expGanaderaService;

	@MockBean
	private TiposGanadoRepository			tiposGanadoRepository;

	@MockBean
	private AnimalHistoricoService			animalHistoricoService;

	@MockBean
	private LoteService						loteService;

	final Integer							EXP_GANADERA_ID			= 1;
	final Integer							GANADO_ID				= 1;
	final Integer							ANIMAL_ID				= 16;
	
	private static final int				TEST_ANIMAL_ID			= 1;
	private static final int				TEST_GANADO_ID			= 2;
	private static final int				TEST_EXP_GANADERA_ID	= 1;
	private static final boolean			TEST_ES_ARCHIVADO		= false;

	/* Varible de utilidad para consultar los tipos de ganado */
	private static Map<String, TiposGanado>	tiposGanadoMap			= TipoGanadoServiceTests.defaultTiposGanado();


	@WithMockUser(value = "spring")
	@Test
	void testCrearAnimalGET() throws Exception {

		mockMvc.perform(get("/explotacion-ganadera/{expGanaderaId}/ganado/{ganadoId}/animal/new",this.EXP_GANADERA_ID,this.GANADO_ID))
		/*1. Comprobamos que se recibe una respuesta HTTP correcta*/
		.andExpect(status().isOk())
		/*2. Comprobamos el nombre de la vista*/
		.andExpect(view().name("animal/editAnimal"))
		/*3. Comprobamos que rescibimos un modelAttribute Veterinario*/
		.andExpect(model().attributeExists("animal"))
		/*4. Comprobamos que Veterinario tiene las propiedades esperadas*/
		.andExpect(model().attribute("animal", hasProperty("identificadorAnimal", blankOrNullString()) ))
		.andExpect(model().attribute("animal", hasProperty("fechaIdentificacion", blankOrNullString()) ) )
		.andExpect(model().attribute("animal", hasProperty("fechaNacimiento", blankOrNullString())) )
		.andExpect(model().attribute("animal", hasProperty("comprado", blankOrNullString())) )
		.andExpect(model().attribute("animal", hasProperty("procedencia", blankOrNullString())) )
		.andExpect(model().attribute("animal", hasProperty("fechaEntrada", blankOrNullString())) );		
	}

	private static Stream<Animal> testsPositivosCrearAnimalData() {
		Stream<Animal> res;

		ExplotacionGanadera explotacion1 = new ExplotacionGanadera();
		explotacion1.setId(1);
		Animal animalPos1 = AnimalServiceTests.generateAnimal("POR09809", "2020/02/01", "2020/02/20", null, TipoSexo.Macho, AnimalControllerTests.tiposGanadoMap.get("Porcino"), explotacion1, null);

		ExplotacionGanadera explotacion2 = new ExplotacionGanadera();
		explotacion2.setId(2);
		Animal animalPos2 = AnimalServiceTests.generateAnimal("C09809", "2020/01/01", "2020/01/15", null, TipoSexo.Hembra, AnimalControllerTests.tiposGanadoMap.get("Caprino"), explotacion2, null);

		ExplotacionGanadera explotacion3 = new ExplotacionGanadera();
		explotacion3.setId(3);
		Animal animalPos3 = AnimalServiceTests.generateAnimal("OV019238098", "2014/01/01", "2014/01/15", "2014/01/15", TipoSexo.Hembra, AnimalControllerTests.tiposGanadoMap.get("Ovino"), explotacion3, null);
		animalPos3.setProcedencia("La granjita de Clotilde");
		res = Stream.of(animalPos1, animalPos2, animalPos3);

		return res;
	}

	@WithMockUser(value = "spring")
	@ParameterizedTest()
	@MethodSource("testsPositivosCrearAnimalData")
	void testsPositivosCrearAnimalPOST(final Animal animal) throws Exception {
		/*
		 * 1. Arrange
		 * Preparamos la peticion POST con los parametros correspondientes
		 */
		Integer expGanaderaId = animal.getExplotacionGanadera().getId();
		Integer tipoGanadoId = animal.getTipoGanado().getId();
		MultiValueMap<String, String> animalParams = AnimalControllerTests.animalToMultiValueMap(animal);
		MockHttpServletRequestBuilder postRequest = MockMvcRequestBuilders.post("/explotacion-ganadera/{expGanaderaId}/ganado/{ganadoId}/animal/new", expGanaderaId, tipoGanadoId).with(csrf()).params(animalParams);
		/* Hacemos stubs necesarios */
		Optional<ExplotacionGanadera> explotacionOptional = Optional.of(animal.getExplotacionGanadera());
		Mockito.when(this.expGanaderaService.findExpGanaderaById(expGanaderaId)).thenReturn(explotacionOptional);

		Optional<TiposGanado> ganadoOptional = Optional.of(AnimalControllerTests.tiposGanadoMap.get(animal.getTipoGanado().getTipoGanado()));
		Mockito.when(this.tiposGanadoRepository.findById(tipoGanadoId)).thenReturn(ganadoOptional);
		/*
		 * 2. Act
		 * Enviamos la peticion POST
		 */
		ResultActions postResultActions = this.mockMvc.perform(postRequest)
			/*
			 * Comprobamos que se recibe una respuesta HTTP correcta
			 * y se devuelve a la vista correcta
			 */
			.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("animal/listaAnimales"));

		/*
		 * 3.Assert
		 * Comprobamos que no se devuelven errores
		 */
		ArgumentCaptor<Animal> argument = ArgumentCaptor.forClass(Animal.class);
		postResultActions.andExpect(MockMvcResultMatchers.model().attributeExists("message"));
		Mockito.verify(this.animalService).saveAnimal(argument.capture());
		Assertions.assertThat(argument.getValue().getExplotacionGanadera().getId()).isEqualTo(expGanaderaId);
		Assertions.assertThat(argument.getValue().getTipoGanado().getId()).isEqualTo(tipoGanadoId);
	}

	private static Stream<Arguments> testsNegativosCrearExplotacionPOSTData() {
		Stream<Arguments> res;

		MultiValueMap<String, String> animalNeg1Params = AnimalControllerTests.animalParams("", "2020/02/01", "2020/02/20", "Macho", null, null, null);
		List<FieldErrorCount> animalNeg1ErrorFields = Stream.of(new FieldErrorCount("identificadorAnimal", 1)).collect(Collectors.toList());

		MultiValueMap<String, String> animalNeg2Params = AnimalControllerTests.animalParams("C09809", null, null, "Macho", null, null, null);
		List<FieldErrorCount> animalNeg2ErrorFields = Stream.of(new FieldErrorCount("fechaNacimiento", 1), new FieldErrorCount("fechaIdentificacion", 1)).collect(Collectors.toList());

		MultiValueMap<String, String> animalNeg3Params = AnimalControllerTests.animalParams("VAC-1098309727", "01/01/2020", "15/01/2020", "Macho", null, null, null);
		List<FieldErrorCount> animalNeg3ErrorFields = Stream.of(new FieldErrorCount("fechaNacimiento", 1), new FieldErrorCount("fechaIdentificacion", 1)).collect(Collectors.toList());

		MultiValueMap<String, String> animalNeg4Params = AnimalControllerTests.animalParams("AS-LCNKOEJ", "2018/02/01", "2018/02/18", "Hembra", true, null, null);
		List<FieldErrorCount> animalNeg4ErrorFields = Stream.of(new FieldErrorCount("procedencia", 1), new FieldErrorCount("fechaEntrada", 1)).collect(Collectors.toList());

		MultiValueMap<String, String> animalNeg5Params = AnimalControllerTests.animalParams("AS-LCNKOEJ", "2018/02/01", "2018/02/18", "Hembra", true, null, "2018/03/01");
		List<FieldErrorCount> animalNeg5ErrorFields = Stream.of(new FieldErrorCount("procedencia", 1)).collect(Collectors.toList());

		MultiValueMap<String, String> animalNeg6Params = AnimalControllerTests.animalParams("AS-LCNKOEJ", "2018/02/01", "2018/02/18", "Hembra", true, "Carmona", null);
		List<FieldErrorCount> animalNeg6ErrorFields = Stream.of(new FieldErrorCount("fechaEntrada", 1)).collect(Collectors.toList());

		MultiValueMap<String, String> animalNeg7Params = AnimalControllerTests.animalParams(null, null, null, null, null, null, null);
		List<FieldErrorCount> animalNeg7ErrorFields = Stream.of(new FieldErrorCount("identificadorAnimal", 2), new FieldErrorCount("sexo", 1), new FieldErrorCount("fechaNacimiento", 1), new FieldErrorCount("fechaIdentificacion", 1))
			.collect(Collectors.toList());
		res = Stream.of(Arguments.of(animalNeg1Params, 1, AnimalControllerTests.tiposGanadoMap.get("Porcino"), animalNeg1ErrorFields), Arguments.of(animalNeg2Params, 2, AnimalControllerTests.tiposGanadoMap.get("Caprino"), animalNeg2ErrorFields),
			Arguments.of(animalNeg3Params, 3, AnimalControllerTests.tiposGanadoMap.get("Vacuno"), animalNeg3ErrorFields), Arguments.of(animalNeg4Params, 4, AnimalControllerTests.tiposGanadoMap.get("Asnal"), animalNeg4ErrorFields),
			Arguments.of(animalNeg5Params, 4, AnimalControllerTests.tiposGanadoMap.get("Asnal"), animalNeg5ErrorFields), Arguments.of(animalNeg6Params, 4, AnimalControllerTests.tiposGanadoMap.get("Asnal"), animalNeg6ErrorFields),
			Arguments.of(animalNeg7Params, 4, AnimalControllerTests.tiposGanadoMap.get("Asnal"), animalNeg7ErrorFields));
		return res;
	}

	@WithMockUser(value = "spring")
	@ParameterizedTest()
	@MethodSource("testsNegativosCrearExplotacionPOSTData")
	void testsNegativosCrearExplotacionPOST(final MultiValueMap<String, String> animalParams, final Integer expGanaderaId, final TiposGanado ganado, final List<FieldErrorCount> fieldsWithErrors) throws Exception {
		/*
		 * 1. Arrange
		 * Preparamos la peticion POST con los parametros correspondientes
		 */
		MockHttpServletRequestBuilder postRequest = MockMvcRequestBuilders.post("/explotacion-ganadera/{expGanaderaId}/ganado/{ganadoId}/animal/new", expGanaderaId, ganado.getId()).with(csrf()).params(animalParams);
		/* Hacemos stubs necesarios */
		ExplotacionGanadera explotacion = new ExplotacionGanadera();
		explotacion.setId(expGanaderaId);
		Optional<ExplotacionGanadera> explotacionOptional = Optional.of(explotacion);
		Mockito.when(this.expGanaderaService.findExpGanaderaById(expGanaderaId)).thenReturn(explotacionOptional);

		Optional<TiposGanado> ganadoOptional = Optional.of(AnimalControllerTests.tiposGanadoMap.get(ganado.getTipoGanado()));
		Mockito.when(this.tiposGanadoRepository.findById(ganado.getId())).thenReturn(ganadoOptional);

		/*
		 * 2. Act
		 * Enviamos la peticion POST
		 */
		ResultActions postResultActions = this.mockMvc.perform(postRequest)
			/*
			 * Comprobamos que se recibe una respuesta HTTP correcta
			 * y se devuelve a la vista correcta
			 */
			.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("animal/editAnimal")).andExpect(MockMvcResultMatchers.model().attributeExists("animal"));

		/*
		 * 3.Assert
		 * Comprobamos que se reciben todos los errores esperados
		 */
		Integer expectedErrorCount = FieldErrorCount.total(fieldsWithErrors);

		postResultActions.andExpect(MockMvcResultMatchers.model().attributeErrorCount("animal", expectedErrorCount));
		for (FieldErrorCount fieldName : fieldsWithErrors) {
			postResultActions.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("animal", fieldName.getFieldName()));
		}

		ArgumentCaptor<Animal> argument = ArgumentCaptor.forClass(Animal.class);
		Mockito.verify(this.animalService, Mockito.never()).saveAnimal(argument.capture());
	}

	@WithMockUser(value = "spring")
	@Test
	void testArchivarAnimalNoDeLoteGET() throws Exception {
		Animal animal = ArchivarControllerTests.animalForTestingNoDeLote();
		BDDMockito.given(this.animalService.findAnimalById(AnimalControllerTests.TEST_ANIMAL_ID)).willReturn(Optional.of(animal));
		this.mockMvc.perform(MockMvcRequestBuilders.get("/explotacion-ganadera/{expGanaderaId}/ganado/{ganadoId}/animal/{animalId}/delete", this.EXP_GANADERA_ID, AnimalControllerTests.TEST_GANADO_ID, AnimalControllerTests.TEST_ANIMAL_ID))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/explotacion-ganadera/1/ganado/2/animal/esArchivado/true/list"));

		Mockito.verify(this.animalService).findAnimalById(AnimalControllerTests.TEST_ANIMAL_ID);
		Optional<Animal> animalO = this.animalService.findAnimalById(AnimalControllerTests.TEST_ANIMAL_ID);
		Mockito.verify(this.animalService).deleteAnimal(animalO.get());
	}

	@WithMockUser(value = "spring")
	@Test
	void testArchivarAnimalDeLoteGET() throws Exception {
		Animal animal = ArchivarControllerTests.animalForTestingDeLote();
		BDDMockito.given(this.animalService.findAnimalById(AnimalControllerTests.TEST_ANIMAL_ID)).willReturn(Optional.of(animal));
		this.mockMvc.perform(MockMvcRequestBuilders.get("/explotacion-ganadera/{expGanaderaId}/ganado/{ganadoId}/animal/{animalId}/delete", this.EXP_GANADERA_ID, AnimalControllerTests.TEST_GANADO_ID, AnimalControllerTests.TEST_ANIMAL_ID))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/explotacion-ganadera/1/ganado/2/animal/lote/1/show"));

		Mockito.verify(this.animalService).findAnimalById(AnimalControllerTests.TEST_ANIMAL_ID);
		Optional<Animal> animalO = this.animalService.findAnimalById(AnimalControllerTests.TEST_ANIMAL_ID);
		Mockito.verify(this.animalService).deleteAnimal(animalO.get());
	}

	@WithMockUser(value = "spring")
	@Test
	void testListarAnimal() throws Exception {

		Iterable<Animal> animales = new ArrayList<>();
		Iterable<Lote> lotes = new ArrayList<>();
		TiposGanado ganados = TipoGanadoServiceTests.newTipoGanado(1, "Vacuno");
		BDDMockito.given(this.animalService.findAllAnimalNoLoteByExpIdYGanadoId(AnimalControllerTests.TEST_EXP_GANADERA_ID, AnimalControllerTests.TEST_GANADO_ID, AnimalControllerTests.TEST_ES_ARCHIVADO)).willReturn(animales);
		BDDMockito.given(this.loteService.findAllLoteFromAnimalByExpIdAndGanadoIdAndEsArchivado(AnimalControllerTests.TEST_EXP_GANADERA_ID, AnimalControllerTests.TEST_GANADO_ID, AnimalControllerTests.TEST_ES_ARCHIVADO)).willReturn(lotes);
		BDDMockito.given(this.tiposGanadoRepository.findById(AnimalControllerTests.TEST_GANADO_ID)).willReturn(Optional.of(ganados));

		mockMvc.perform(MockMvcRequestBuilders.get("/explotacion-ganadera/{expGanaderaId}/ganado/{ganadoId}/animal/esArchivado/{esArchivado}/list", AnimalControllerTests.TEST_EXP_GANADERA_ID, AnimalControllerTests.TEST_GANADO_ID,
			AnimalControllerTests.TEST_ES_ARCHIVADO)).andExpect(status().isOk())
		.andExpect(view().name("animal/listaAnimales"))
			.andExpect(model().attributeExists("lote"))
			.andExpect(model().attributeExists("animales"))
			.andExpect(model().attributeExists("expId"))
			.andExpect(model().attributeExists("tipoGanado"))
			.andExpect(model().attributeExists("esArchivado"));
		Mockito.verify(this.animalService).findAllAnimalNoLoteByExpIdYGanadoId( this.EXP_GANADERA_ID,  AnimalControllerTests.TEST_GANADO_ID, false);
		Mockito.verify(this.loteService).findAllLoteFromAnimalByExpIdAndGanadoIdAndEsArchivado( this.EXP_GANADERA_ID,  AnimalControllerTests.TEST_GANADO_ID, false);
		Mockito.verify(this.tiposGanadoRepository)
		.findById( AnimalControllerTests.TEST_GANADO_ID);
	}

	@WithMockUser(value = "spring")
	@Test
	void testMostrarAnimal() throws Exception {

		Animal animal = ArchivarControllerTests.animalForTestingNoDeLote();
		animal.setEsArchivado(false);
		BDDMockito.given(this.animalService.findAnimalById(AnimalControllerTests.TEST_ANIMAL_ID)).willReturn(Optional.of(animal));
		this.mockMvc.perform(MockMvcRequestBuilders.get("/explotacion-ganadera/{expGanaderaId}/ganado/{ganadoId}/animal/{animalId}/show", this.EXP_GANADERA_ID, this.GANADO_ID, AnimalControllerTests.TEST_ANIMAL_ID))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.view()
			.name("animal/mostrarAnimal"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("animal"));
	}
	private static MultiValueMap<String, String> animalParams(final String identificadorAnimal, final String fechaNacimiento, final String fechaIdentificacion, final String sexo, final Boolean comprado, final String procedencia,
		final String fechaEntrada) {
		MultiValueMap<String, String> res = new LinkedMultiValueMap<String, String>();

		res.add("identificadorAnimal", identificadorAnimal);
		res.add("fechaIdentificacion", fechaIdentificacion);
		res.add("fechaNacimiento", fechaNacimiento);
		res.add("sexo", sexo);
		if (comprado != null) {
			res.add("comprado", comprado.toString());
		}

		res.add("procedencia", procedencia);
		res.add("fechaEntrada", fechaEntrada);

		return res;
	}

	private static MultiValueMap<String, String> animalToMultiValueMap(final Animal animal) {
		MultiValueMap<String, String> res = new LinkedMultiValueMap<String, String>();

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
		formatter = formatter.withLocale(new Locale("es", "ES"));

		res.add("identificadorAnimal", animal.getIdentificadorAnimal());

		if (animal.getSexo() != null) {
			res.add("sexo", animal.getSexo().toString());
		}

		if (animal.getTipoGanado() != null && animal.getTipoGanado().getId() != null) {
			res.add("tipoGanado.id", animal.getTipoGanado().getId().toString());
		}

		if (animal.getExplotacionGanadera() != null && animal.getExplotacionGanadera().getId() != null) {
			res.add("explotacionGanadera.id", animal.getExplotacionGanadera().getId().toString());
		}
		/* Fechas */
		if (animal.getFechaNacimiento() != null) {
			res.add("fechaNacimiento", animal.getFechaNacimiento().format(formatter));
		}
		if (animal.getFechaIdentificacion() != null) {
			res.add("fechaIdentificacion", animal.getFechaIdentificacion().format(formatter));
		}
		if (animal.getFechaEntrada() != null) {
			res.add("fechaEntrada", animal.getFechaEntrada().format(formatter));
			res.add("comprado", "true");
		}
		if (animal.getProcedencia() != null) {
			res.add("procedencia", animal.getProcedencia());
		}

		return res;
	}

}
