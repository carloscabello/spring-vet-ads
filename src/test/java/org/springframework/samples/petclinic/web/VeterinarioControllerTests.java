package org.springframework.samples.petclinic.web;

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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.model.Ganadero;
import org.springframework.samples.petclinic.model.TiposGanado;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.model.Veterinario;
import org.springframework.samples.petclinic.model.utils.ExpectedViolation;
import org.springframework.samples.petclinic.repository.springdatajpa.TiposGanadoRepository;
import org.springframework.samples.petclinic.service.GanaderoServiceTests;
import org.springframework.samples.petclinic.service.UserService;
import org.springframework.samples.petclinic.service.VetService;
import org.springframework.samples.petclinic.service.VeterinarioServiceTests;
import org.springframework.samples.petclinic.web.utils.FieldErrorCount;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@WebMvcTest(
		controllers=VetController.class,
		excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class),
		excludeAutoConfiguration= SecurityConfiguration.class)
	/*Controlador a instanciar para probarlo*/	
	/*Excluir los componentes de seguridad al instanciar el controlador*/
	/*PD: Si cargamos los componentes de seguridad, tal como esta implementado (Con roles y usuarios), 
	 * tambien tendríamos que cargar el contexto de la aplicacion completo llegando hasta la capad de datos.
	 * Si hacemos esto, estaríamos hablando mas bien de un test de integración, no de test unitarios*/
public class VeterinarioControllerTests {
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private VetService vetService;
	
	@MockBean
	private UserService userService;
	
	@MockBean
	private TiposGanadoRepository tiposGanadoRepository;
	
	@WithMockUser(value="spring")
	@Test
	void testCrearVeterinarioGET() throws Exception{
		mockMvc.perform(get("/veterinario/new"))
		/*1. Comprobamos que se recibe una respuesta HTTP correcta*/
		.andExpect(status().isOk())
		/*2. Comprobamos el nombre de la vista*/
		.andExpect(view().name("veterinario/editVeterinario"))
		/*3. Comprobamos que recibimos un modelAttribute Veterinario*/
		.andExpect(model().attributeExists("veterinario"))
		.andExpect(model().attributeExists("todosTiposG"))
		/*4. Comprobamos que Veterinario tiene las propiedades esperadas*/
		.andExpect(model().attribute("veterinario", hasProperty("dni", blankOrNullString()) ) )
		.andExpect(model().attribute("veterinario", hasProperty("tiposGanado", blankOrNullString()) ) )
		.andExpect(model().attribute("veterinario", hasProperty("firstName", blankOrNullString()) ) )
		.andExpect(model().attribute("veterinario", hasProperty("lastName", blankOrNullString()) ) )
		.andExpect(model().attribute("veterinario", hasProperty("telephone", blankOrNullString()) ) )
		.andExpect(model().attribute("veterinario", hasProperty("mail", blankOrNullString()) ) )
		.andExpect(model().attribute("veterinario", hasProperty("province", blankOrNullString()) ) )
		.andExpect(model().attribute("veterinario", hasProperty("city", blankOrNullString()) ) )
			/*No podemos comprobar los atributos de user porque user=null en la respuesta*/
		.andExpect(model().attribute("veterinario", hasProperty("user") ));
	}
	
	@WithMockUser(value="spring")
	@ParameterizedTest()
	@MethodSource("dataPositivoCrearVeterinarioPOST")
	/* Veterinario veterinario: Parametros del Veterinario que seran incluidos en la peticion POST*/
	void testsPositivosCrearVeterinarioPOST(Veterinario veterinario) throws Exception {
		
		/*1. Arrange
		 *	Preparamos la peticion POST con los parametros correspondientes*/
		MultiValueMap<String, String> veterinarioParams = veterinarioToMultiValueMap(veterinario);
		MockHttpServletRequestBuilder postRequest = post("/veterinario/new").with(csrf()).params(veterinarioParams);
		
		/*2. Act
		*	Enviamos la peticion POST*/
		ResultActions postResultActions = mockMvc.perform(postRequest)
				/*Comprobamos que se recibe una respuesta HTTP correcta*/
				.andExpect(status().isOk())
				.andExpect(view().name("welcome"));
		
		/*3.Assert
		 *	Comprobamos que no se devuelven errores*/
		postResultActions
		.andExpect(model().attributeHasNoErrors("veterinario"));
		/*No tiene sentido comprobar referencias a otras entidades, ya que el binder no las puede
		 * inicializar. El servicio siempre se llama con una lista vacia de tiposGanado.*/
		//Omitimos tipoGanado de la comprobacion (Matchers esta deprecated, pero lo usamos igual porque es lo mas practico)
		//verify(vetService).saveVet(Matchers.refEq(veterinario,"tiposGanado")); 
		verify(vetService).saveVet(veterinario);
	}
	
	@WithMockUser(value="spring")
	@ParameterizedTest()
	@MethodSource("dataNegativoCrearVeterinarioPOST")
	/* Ganadero ganadero: Parametros del Ganadero que seran incluidos en la peticion POST
	 * List<String> fieldsWithErrors: Lista de atributos con errores esperados*/
	void testsNegativosCrearVeterinarioPOST(Veterinario veterinario, List<FieldErrorCount> fieldsWithErrors) throws Exception {
		
		/*1. Arrange
		 *	Preparamos la peticion POST con los parametros correspondientes*/
		MultiValueMap<String, String> veterinarioParams = veterinarioToMultiValueMap(veterinario);
		MockHttpServletRequestBuilder postRequest = post("/veterinario/new").with(csrf()).params(veterinarioParams);
		
		/*2. Act
		*	Enviamos la peticion POST*/
		ResultActions postResultActions = mockMvc.perform(postRequest)
				/*Comprobamos que se recibe una respuesta HTTP correcta*/
				.andExpect(status().isOk())
				.andExpect(view().name("veterinario/editVeterinario"))
				.andExpect(model().attributeExists("veterinario"))
				.andExpect(model().attributeHasErrors("veterinario"));
		
		/*3.Assert
		 *	Comprobamos que se reciben todos los errores esperados*/
		//System.out.println(postResultActions.andReturn().getModelAndView().getModel());
		Integer expectedErrorCount = FieldErrorCount.total(fieldsWithErrors);
		
		postResultActions.andExpect(model().attributeErrorCount("veterinario", expectedErrorCount));
		for(FieldErrorCount fieldName: fieldsWithErrors) {
			postResultActions.andExpect(model().attributeHasFieldErrors("veterinario", fieldName.getFieldName()));
		}
		
		verify(vetService,never()).saveVet(veterinario);

		//res.getResolvedException().printStackTrace();
		//System.out.println(res.getResolvedException().getStackTrace().toString());
	}	
	
	
	private static Stream<Veterinario> dataPositivoCrearVeterinarioPOST(){
		Stream<Veterinario> res;
		
		/*Dado que el binder no puede recuperar los TiposGanado en base a las ID, omitimos esta propiedad e 
		 * inicializamos la lista vacia*/
		Veterinario veterinarioPos1 = VeterinarioServiceTests.generateVeterinario("Julián", "Banderas", "0123456789", 
				"anton@mail.com", "Sevilla", "Dos Hermanas", "12345123A", new ArrayList<TiposGanado>(), true, "julian", "12345");
		
		Veterinario veterinarioPos2 = VeterinarioServiceTests.generateVeterinario("Carlo", "Magno Alexandrio", "987654321", 
				"carlo@mail.com", "Extremadura", "El Gasco", "12344321G", new ArrayList<TiposGanado>(), false, "carlo", "54321");
		
		Veterinario veterinarioPos3 = VeterinarioServiceTests.generateVeterinario("Tulio", "Iglesias Romero", "987654321", 
				"iglesias@mail", "Extremadura", "El Gasco", "12344321X", new ArrayList<TiposGanado>(), true, "carlo", "54321");
		
		List<TiposGanado> especialidadesPos4 = new ArrayList<TiposGanado>();
		Veterinario veterinarioPos4 = VeterinarioServiceTests.generateVeterinario("Laura", "Contreras Medina", "678256143", 
				"marta.sanchez_16@mail.es", "Sevilla", "Gines", "12344321K", especialidadesPos4, false, "carlo", "54321");
		
		Veterinario veterinarioPos5 = VeterinarioServiceTests.generateVeterinario("Clara", "Sánchez Panza", "0123456789", 
				"marta.sanchez_16@mail.es", "Sevilla", "Gines", "12344321K", new ArrayList<TiposGanado>(), true, "carlo", "54321");
		
		res = Stream.of(veterinarioPos1, veterinarioPos2, veterinarioPos3, veterinarioPos4, veterinarioPos5);
		
		return res;		
	}
	
	private static Stream<Arguments> dataNegativoCrearVeterinarioPOST(){
		Stream<Arguments> res;
			
			Veterinario veterinarioNeg1 = VeterinarioServiceTests.generateVeterinario("", "", "0123456789", 
					"anton@mail.com", "Sevilla", "Dos Hermanas", "12345678A", new ArrayList<TiposGanado>(), true, "julian", "12345");		
			List<FieldErrorCount> veterinarioNeg1ErrorFields = Stream.of(
					new FieldErrorCount("firstName",1),
					new FieldErrorCount("lastName",1))
					.collect(Collectors.toList());
			
			Veterinario veterinarioNeg2 = VeterinarioServiceTests.generateVeterinario("Marco", "Aurelio Arkantos", "", 
					"", "Sevilla", "Dos Hermanas", "12345678A", new ArrayList<TiposGanado>(), true, "julian", "12345");		
			List<FieldErrorCount> veterinarioNeg2ErrorFields = Stream.of(
					new FieldErrorCount("telephone",2),
					new FieldErrorCount("mail",1))
					.collect(Collectors.toList());			

			Veterinario veterinarioNeg3 = VeterinarioServiceTests.generateVeterinario("Marco", "Aurelio Arkantos", "0123456789123123", 
					"marco@mail", "Sevilla", "Dos Hermanas", "12345678A", null, true, "julian", "12345");	
			List<FieldErrorCount> veterinarioNeg3ErrorFields = Stream.of(
					new FieldErrorCount("telephone",1),
					new FieldErrorCount("tiposGanado",1))
					.collect(Collectors.toList());			
			
			Veterinario veterinarioNeg4 = VeterinarioServiceTests.generateVeterinario("Marco", "Aurelio Arkantos", "012", 
					"marco@mail", "", "", "12345678A", new ArrayList<TiposGanado>(), true, "julian", "12345");		
			List<FieldErrorCount> veterinarioNeg4ErrorFields = Stream.of(
					new FieldErrorCount("province",1),
					new FieldErrorCount("city",1))
					.collect(Collectors.toList());	
			
			Veterinario veterinarioNeg5 = VeterinarioServiceTests.generateVeterinario("Laura", "Contreras Medina", "678256143", 
					"laura@mail.es", "Sevilla", "Gines", "012345678", new ArrayList<TiposGanado>(), false, "carlo", "54321");
			List<FieldErrorCount> veterinarioNeg5ErrorFields = Stream.of(
					new FieldErrorCount("dni",1))
					.collect(Collectors.toList());	
			
			Veterinario veterinarioNeg6 = VeterinarioServiceTests.generateVeterinario("Laura", "Contreras Medina", "ABCDEFHIJ", 
					"mail", "Sevilla", "Gines", "01234567E", new ArrayList<TiposGanado>(), false, "carlo", "54321");
			List<FieldErrorCount> veterinarioNeg6ErrorFields = Stream.of(
					new FieldErrorCount("mail",1),
					new FieldErrorCount("telephone",1))
					.collect(Collectors.toList());	

			
			Veterinario veterinarioNeg7 = VeterinarioServiceTests.generateVeterinario("", "", "", 
					"anton@mail.com", "Sevilla", "Dos Hermanas", "badDNI", new ArrayList<TiposGanado>(), true, "julian", "12345");
			List<FieldErrorCount> veterinarioNeg7ErrorFields = Stream.of(
					new FieldErrorCount("firstName",1),
					new FieldErrorCount("lastName",1),
					new FieldErrorCount("telephone",2),
					new FieldErrorCount("dni",1))
					.collect(Collectors.toList());	
			
			Veterinario veterinarioNeg8 = VeterinarioServiceTests.generateVeterinario("", "", "", 
					"", "", "", "", null, true, "julian", "12345");
			List<FieldErrorCount> veterinarioNeg8ErrorFields = Stream.of(
					new FieldErrorCount("firstName",1),
					new FieldErrorCount("lastName",1),
					new FieldErrorCount("telephone",2),
					new FieldErrorCount("mail",1),
					new FieldErrorCount("province",1),
					new FieldErrorCount("city",1),
					new FieldErrorCount("dni",2),
					new FieldErrorCount("tiposGanado",1))
					.collect(Collectors.toList());

			Veterinario veterinarioNeg9 = VeterinarioServiceTests.generateVeterinario("Nombre", "Apellidos Apellidos", "0123456789", 
					"anton@mail.com", "Sevilla", "Dos Hermanas", "12345678A", new ArrayList<TiposGanado>(), true, "", "");		
			List<FieldErrorCount> veterinarioNeg9ErrorFields = Stream.of(
					new FieldErrorCount("user.username",1),
					new FieldErrorCount("user.password",1))
					.collect(Collectors.toList());
			
			res = Stream.of(
					Arguments.of(veterinarioNeg1, veterinarioNeg1ErrorFields),
					Arguments.of(veterinarioNeg2, veterinarioNeg2ErrorFields),
					Arguments.of(veterinarioNeg3, veterinarioNeg3ErrorFields),
					Arguments.of(veterinarioNeg4, veterinarioNeg4ErrorFields),
					Arguments.of(veterinarioNeg5, veterinarioNeg5ErrorFields),
					Arguments.of(veterinarioNeg6, veterinarioNeg6ErrorFields),
					Arguments.of(veterinarioNeg7, veterinarioNeg7ErrorFields),
					Arguments.of(veterinarioNeg8, veterinarioNeg8ErrorFields),
					Arguments.of(veterinarioNeg9, veterinarioNeg9ErrorFields));	
		
		return res;		
	}
	
	@DisplayName("Custom VeterinarioValidator tests")
	@Nested
	class VeterinarioCustomValidatorTests{
		/* Estos son tests para el VeterinarioValidator ya que no hay otra forma de probarlos, ya que es invocado desde
		 * el Controlador con initBinder*/
		
		@BeforeEach
		/*This is necessary because all calls are registered and preserved for all the tests*/
		void resetMockUps() {
			Mockito.reset(vetService, userService, tiposGanadoRepository);
		}
		
		@WithMockUser(value="spring")
		@Test
		public void shouldRejectDuplicateDNITest() throws Exception{
			/*1. Arrange */
			String duplicateDNI = "12345678A";
			Veterinario veterinario = VeterinarioServiceTests.generateVeterinario("Julián", "Banderas", "0123456789", 
					"anton@mail.com", "Sevilla", "Dos Hermanas", duplicateDNI, new ArrayList<TiposGanado>(), true, "julian", "12345");
			Veterinario veterinarioDuplicate = VeterinarioServiceTests.generateVeterinario("Carlo", "Magno Alexandrio", "987654321", 
					"carlo@mail.com", "Extremadura", "El Gasco", duplicateDNI, new ArrayList<TiposGanado>(), false, "carlo", "54321");
			Optional<Veterinario> vetDuplicateOptional = Optional.of(veterinarioDuplicate);
			when(vetService.findVeterinarioByDni(duplicateDNI)).thenReturn(vetDuplicateOptional);
					
			MultiValueMap<String, String> veterinarioParams = veterinarioToMultiValueMap(veterinario);
			/*2. Act */
			mockMvc.perform(post("/veterinario/new").with(csrf()).params(veterinarioParams))
			/*3.Assert */
			.andExpect(status().isOk())
			.andExpect(model().attributeHasErrors("veterinario"))
			.andExpect(model().attributeErrorCount("veterinario", 1))
			.andExpect(model().attributeHasFieldErrorCode("veterinario", "dni", "veterinario.dni.repetido"));
			
			verify(vetService).findVeterinarioByDni(duplicateDNI);
		}
		
		@WithMockUser(value="spring")
		@Test
		public void shouldRejectDuplicateUsernameTest() throws Exception{
			/*1. Arrange */
			String duplicateUsername = "vetUsername";
			Veterinario veterinario = VeterinarioServiceTests.generateVeterinario("Julián", "Banderas", "0123456789", 
					"anton@mail.com", "Sevilla", "Dos Hermanas", "12345678A", new ArrayList<TiposGanado>(), true, duplicateUsername, "12345");
			Veterinario veterinarioDuplicate = VeterinarioServiceTests.generateVeterinario("Carlo", "Magno Alexandrio", "987654321", 
					"carlo@mail.com", "Extremadura", "El Gasco", "12312378A", new ArrayList<TiposGanado>(), false, duplicateUsername, "54321");
			Optional<User> vetDuplicateOptional = Optional.of(veterinarioDuplicate.getUser());
			when(userService.getUserByUsername(duplicateUsername)).thenReturn(vetDuplicateOptional);
					
			MultiValueMap<String, String> veterinarioParams = veterinarioToMultiValueMap(veterinario);
			/*2. Act */
			mockMvc.perform(post("/veterinario/new").with(csrf()).params(veterinarioParams))
			/*3.Assert */
			.andExpect(status().isOk())
			.andExpect(model().attributeHasErrors("veterinario"))
			.andExpect(model().attributeErrorCount("veterinario", 1))
			.andExpect(model().attributeHasFieldErrorCode("veterinario", "user.username", "user.username.repetido"));
			
			verify(userService).getUserByUsername(duplicateUsername);
		}
		
		@WithMockUser(value="spring")
		@ParameterizedTest()
		@ValueSource(strings = {"username Veterinario", " usernameVeterinario", "usernameVeterinario "})
		public void shouldRejectUsernameTest(String username) throws Exception{
			/*1. Arrange */
			
			Veterinario veterinario = VeterinarioServiceTests.generateVeterinario("Julián", "Banderas", "0123456789", 
					"anton@mail.com", "Sevilla", "Dos Hermanas", "12345678A", new ArrayList<TiposGanado>(), true, username, "12345");
			MultiValueMap<String, String> veterinarioParams = veterinarioToMultiValueMap(veterinario);
			/*2. Act */
			mockMvc.perform(post("/veterinario/new").with(csrf()).params(veterinarioParams))
			/*3.Assert */
			.andExpect(status().isOk())
			.andExpect(model().attributeHasErrors("veterinario"))
			.andExpect(model().attributeErrorCount("veterinario", 1))
			.andExpect(model().attributeHasFieldErrorCode("veterinario", "user.username", "user.username.contieneEspacios"));
		}
		
		@WithMockUser(value="spring")
		@ParameterizedTest()
		@ValueSource(strings = {"password Veterinario", " passwordVeterinario", "passwordVeterinario "})
		public void shouldRejectPasswordTest(String password) throws Exception{
			/*1. Arrange */
			
			Veterinario veterinario = VeterinarioServiceTests.generateVeterinario("Julián", "Banderas", "0123456789", 
					"anton@mail.com", "Sevilla", "Dos Hermanas", "12345678A", new ArrayList<TiposGanado>(), true, "vetUsername", password);
			MultiValueMap<String, String> veterinarioParams = veterinarioToMultiValueMap(veterinario);
			/*2. Act */
			mockMvc.perform(post("/veterinario/new").with(csrf()).params(veterinarioParams))
			/*3.Assert */
			.andExpect(status().isOk())
			.andExpect(model().attributeHasErrors("veterinario"))
			.andExpect(model().attributeErrorCount("veterinario", 1))
			.andExpect(model().attributeHasFieldErrorCode("veterinario", "user.password", "user.password.contieneEspacios"));
		}
		
	}
	
	public static MultiValueMap<String, String> veterinarioToMultiValueMap(final Veterinario veterinario) {
		
		MultiValueMap<String, String> res = new LinkedMultiValueMap<String, String>();
		res.add("firstName", veterinario.getFirstName());
		res.add("lastName", veterinario.getLastName());
		res.add("telephone", veterinario.getTelephone());
		res.add("mail", veterinario.getMail());
		res.add("province", veterinario.getProvince());
		res.add("city", veterinario.getCity());
		res.add("dni", veterinario.getDni());
		if(veterinario.getTiposGanado()!=null) {
			/*tiposGanado.id se pone como 0 para que se respete la anotacion @NotNull, pero no podemos asociar
			 * TiposGanado reales. Esto es porque como todo es un mock, el binder no relaciona los 
			 * TiposGanado.id con ninguna entidad real. Al crear el objeto Veterinario, tiposGanado=[]*/
			res.add("tiposGanado.id", "0");
		}
		res.add("esDisponible", new Boolean(veterinario.isEsDisponible()).toString());
		res.add("user.username", veterinario.getUser().getUsername());
		res.add("user.password", veterinario.getUser().getPassword());
		return res;

	}
	


}
