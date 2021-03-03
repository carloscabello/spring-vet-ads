package org.springframework.samples.petclinic.web;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.blankOrNullString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.After;
import org.junit.Before;
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
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.model.Ganadero;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.service.GanaderoService;
import org.springframework.samples.petclinic.service.GanaderoServiceTests;
import org.springframework.samples.petclinic.service.UserService;
import org.springframework.samples.petclinic.web.utils.FieldErrorCount;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import org.springframework.context.annotation.FilterType;

@WebMvcTest(
	controllers=GanaderoController.class,
	excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class),
	excludeAutoConfiguration= SecurityConfiguration.class)
/*Controlador a instanciar para probarlo*/	
/*Excluir los componentes de seguridad al instanciar el controlador*/
/*PD: Si cargamos los componentes de seguridad, tal como esta implementado (Con roles y usuarios), 
 * tambien tendríamos que cargar el contexto de la aplicacion completo llegando hasta la capad de datos.
 * Si hacemos esto, estaríamos hablando mas bien de un test de integración, no de test unitarios*/
class GanaderoControllerTests {

	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private GanaderoService ganaderoService;
	
	@MockBean
	private UserService userService;
	
	
	@WithMockUser(value="spring")
	@Test
	void testCrearGanadoGET() throws Exception {
		mockMvc.perform(get("/ganadero/new"))
			/*1. Comprobamos que se recibe una respuesta HTTP correcta*/
			.andExpect(status().isOk())
			/*2. Comprobamos el nombre de la vista*/
			.andExpect(view().name("ganadero/editGanadero"))
			/*3. Comprobamos que recibimos un modelAttribute Ganadero*/
			.andExpect(model().attributeExists("ganadero"))
			/*4. Comprobamos que Ganadero tiene las propiedades esperadas*/
			.andExpect(model().attribute("ganadero", hasProperty("dni", blankOrNullString()) ) )
			.andExpect(model().attribute("ganadero", hasProperty("postalCode", blankOrNullString()) ) )
			.andExpect(model().attribute("ganadero", hasProperty("address", blankOrNullString()) ) )
			.andExpect(model().attribute("ganadero", hasProperty("firstName", blankOrNullString()) ) )
			.andExpect(model().attribute("ganadero", hasProperty("lastName", blankOrNullString()) ) )
			.andExpect(model().attribute("ganadero", hasProperty("telephone", blankOrNullString()) ) )
			.andExpect(model().attribute("ganadero", hasProperty("mail", blankOrNullString()) ) )
			.andExpect(model().attribute("ganadero", hasProperty("province", blankOrNullString()) ) )
			.andExpect(model().attribute("ganadero", hasProperty("city", blankOrNullString()) ) )
				/*No podemos comprobar los atributos de user porque user=null en la respuesta*/
			.andExpect(model().attribute("ganadero", hasProperty("user") ));
	}
	
	@WithMockUser(value="spring")
	@ParameterizedTest()
	@MethodSource("dataPositivoCrearGanaderoPOST")
	/* Ganadero ganadero: Parametros del Ganadero que seran incluidos en la peticion POST*/
	void testsPositivosCrearGanaderoPOST(Ganadero ganadero) throws Exception {
		
		
		/*1. Arrange
		 *	Preparamos la peticion POST con los parametros correspondientes*/
		MultiValueMap<String, String> ganaderoParams = ganaderoToMultiValueMap(ganadero);
		MockHttpServletRequestBuilder postRequest = post("/ganadero/new").with(csrf()).params(ganaderoParams);
				/** Parametros del formulario**/
		
		/*2. Act
		*	Enviamos la peticion POST*/
		ResultActions postResultActions = mockMvc.perform(postRequest)
				/*Comprobamos que se recibe una respuesta HTTP correcta*/
				.andExpect(status().isOk())
				.andExpect(view().name("welcome"));
		
		/*3.Assert
		 *	Comprobamos que no se devuelven errores*/
		postResultActions
		.andExpect(model().attributeHasNoErrors("ganadero"));
		verify(ganaderoService).saveGanadero(ganadero);
	}
	
	@WithMockUser(value="spring")
	@ParameterizedTest()
	@MethodSource("dataNegativoCrearGanaderoPOST")
	/* Ganadero ganadero: Parametros del Ganadero que seran incluidos en la peticion POST
	 * List<String> fieldsWithErrors: Lista de atributos con errores esperados*/
	void testsNegativosCrearGanaderoPOST(Ganadero ganadero, List<FieldErrorCount> fieldsWithErrors) throws Exception {
		
		/*1. Arrange
		 *	Preparamos la peticion POST con los parametros correspondientes*/
		MultiValueMap<String, String> ganaderoParams = ganaderoToMultiValueMap(ganadero);
		MockHttpServletRequestBuilder postRequest = post("/ganadero/new").with(csrf()).params(ganaderoParams);
				/** Parametros del formulario**/
		
		/*2. Act
		*	Enviamos la peticion POST*/
		ResultActions postResultActions = mockMvc.perform(postRequest)
				/*Comprobamos que se recibe una respuesta HTTP correcta*/
				.andExpect(status().isOk())
				.andExpect(view().name("ganadero/editGanadero"))
				.andExpect(model().attributeExists("ganadero"))
				.andExpect(model().attributeHasErrors("ganadero"));
		
		/*3.Assert
		 *	Comprobamos que se reciben todos los errores esperados*/
		//System.out.println(postResultActions.andReturn().getModelAndView().getModel());
		Integer expectedErrorCount = FieldErrorCount.total(fieldsWithErrors);
		
		postResultActions.andExpect(model().attributeErrorCount("ganadero", expectedErrorCount));
		for(FieldErrorCount fieldName: fieldsWithErrors) {
			postResultActions.andExpect(model().attributeHasFieldErrors("ganadero", fieldName.getFieldName()));
		}
		
		verify(ganaderoService,never()).saveGanadero(ganadero);

		//res.getResolvedException().printStackTrace();
		//System.out.println(res.getResolvedException().getStackTrace().toString());
	}
	
	private static Stream<Ganadero> dataPositivoCrearGanaderoPOST(){
		
		Ganadero ganaderoPos1 = GanaderoServiceTests.generateGanadero("Antonio", "Banderas", "0123456789", 
				"anton@mail.com", "Sevilla", "Dos Hermanas", "12345123A", "41345", 
				"Calle Antonio Mairena, 8", "antonio1", "12345");
		Ganadero ganaderoPos2 = GanaderoServiceTests.generateGanadero("Niño", "Banderas Iglesias", "0123456789", 
				"ninon@domain", "Extremadura", "El gasco", "87654321B", "24674", 
				"Luis Manuel Andrade, 8. Piso 8-B", "nino1", "12345");
		Ganadero ganaderoPos3 = GanaderoServiceTests.generateGanadero("Laura", "Contreras Medina", "678256143", 
				"laura@mail.es", "Extremadura", "Cáceres", "12344321G", "67254", 
				"Aviación, 8", "laura", "12345");
		Ganadero ganaderoPos4 = GanaderoServiceTests.generateGanadero("Clara", "Sánchez Panza", "675245134", 
				"clarita83@mail.es", "Extremadura", "Plascencia", "12344321X", "67254", 
				"Reina Mercedes, 9", "clasanpan", "12345");
		Ganadero ganaderoPos5 = GanaderoServiceTests.generateGanadero("Marta", "Sánchez Panza", "675245134", 
				"marta.sanchez_16@mail.es", "Extremadura", "Plascencia", "12344321K", "67254", 
				"Reina Mercedes, 9", "martinina", "12345");
		
		Stream res = Stream.of(
				ganaderoPos1, ganaderoPos2, ganaderoPos3,
				ganaderoPos4, ganaderoPos5);
		return res;
	}
	
	private static Stream<Arguments> dataNegativoCrearGanaderoPOST(){
		
		Ganadero ganaderoNeg1 = GanaderoServiceTests.generateGanadero("", "Banderas", "0123456789", 
				"anton@mail.com", "Sevilla", "Dos Hermanas", "12345678A", "41345", 
				"Calle Antonio Mairena, 8", "antonio1", "12345");
		List<FieldErrorCount> ganaderoNeg1ErrorFields = Stream.of(
				new FieldErrorCount("firstName",1))
				.collect(Collectors.toList());
		
		Ganadero ganaderoNeg2 = GanaderoServiceTests.generateGanadero("", "", "0123456789", 
				"anton@mail.com", "Sevilla", "Dos Hermanas", "12345678A", "41345", 
				"Calle Antonio Mairena, 8", "antonio1", "12345");
		List<FieldErrorCount> ganaderoNeg2ErrorFields = Stream.of(
				new FieldErrorCount("firstName",1), 
				new FieldErrorCount("lastName",1))
				.collect(Collectors.toList());
		
		Ganadero ganaderoNeg3 = GanaderoServiceTests.generateGanadero("Antonio", "Banderas", "", 
				"", "Sevilla", "Dos Hermanas", "12345678A", "41345", 
				"Calle Antonio Mairena, 8", "antonio1", "12345");

		List<FieldErrorCount> ganaderoNeg3ErrorFields = Stream.of(
				new FieldErrorCount("telephone",2),
				new FieldErrorCount("mail",1))
				.collect(Collectors.toList());
		
		Ganadero ganaderoNeg4 = GanaderoServiceTests.generateGanadero("Antonio", "Banderas", "0123456789", 
				"anton@mail.com", "", "", "12345678A", "41345", 
				"Calle Antonio Mairena, 8", "antonio1", "12345");
		List<FieldErrorCount> ganaderoNeg4ErrorFields = Stream.of(
				new FieldErrorCount("province",1),
				new FieldErrorCount("city",1))
				.collect(Collectors.toList());
		
		Ganadero ganaderoNeg5 = GanaderoServiceTests.generateGanadero("Antonio", "Banderas", "0123456789", 
				"anton@mail.com", "Sevilla", "Dos Hermanas", "12345678A", "", 
				"", "antonio1", "12345");
		List<FieldErrorCount> ganaderoNeg5ErrorFields = Stream.of(
				new FieldErrorCount("postalCode",2), 
				new FieldErrorCount("address",1))
				.collect(Collectors.toList());
		
		Ganadero ganaderoNeg6 = GanaderoServiceTests.generateGanadero("", "", "", "", "", "", "", "", "", "", "");
		List<FieldErrorCount> ganaderoNeg6ErrorFields = Stream.of(
				new FieldErrorCount("firstName",1), 
				new FieldErrorCount("lastName",1), 
				new FieldErrorCount("telephone",2), 
				new FieldErrorCount("mail",1), 
				new FieldErrorCount("province",1),
				new FieldErrorCount("city",1), 
				new FieldErrorCount("dni",2), 
				new FieldErrorCount("postalCode",2), 
				new FieldErrorCount("address",1), 
				new FieldErrorCount("user.username",1), 
				new FieldErrorCount("user.password",1))
				.collect(Collectors.toList());
		
		Stream res = Stream.of(
				Arguments.of(ganaderoNeg1,ganaderoNeg1ErrorFields),
				Arguments.of(ganaderoNeg2,ganaderoNeg2ErrorFields),
				Arguments.of(ganaderoNeg3,ganaderoNeg3ErrorFields),
				Arguments.of(ganaderoNeg4,ganaderoNeg4ErrorFields),
				Arguments.of(ganaderoNeg5,ganaderoNeg5ErrorFields),
				Arguments.of(ganaderoNeg6,ganaderoNeg6ErrorFields)
				);
		return res;
	}
	
	protected static MultiValueMap<String, String> ganaderoToMultiValueMap(final Ganadero ganadero) {
		
		MultiValueMap<String, String> res = new LinkedMultiValueMap<String, String>();
		res.add("firstName", ganadero.getFirstName());
		res.add("lastName", ganadero.getLastName());
		res.add("telephone", ganadero.getTelephone());
		res.add("mail", ganadero.getMail());
		res.add("province", ganadero.getProvince());
		res.add("city", ganadero.getCity());
		res.add("dni", ganadero.getDni());
		res.add("postalCode", ganadero.getPostalCode());
		res.add("address", ganadero.getAddress());
		res.add("user.username", ganadero.getUser().getUsername());
		res.add("user.password", ganadero.getUser().getPassword());
		
		return res;

	}
	
	private static MultiValueMap<String, String> generateGanadero(final String firstName, final String lastName, final String telephone, final String mail, final String province, final String city, final String dni, final String postalCode, final String address,
	final String username, final String password) {
		
		MultiValueMap<String, String> res = new LinkedMultiValueMap<String, String>();
		res.add("firstName", firstName);
		res.add("lastName", lastName);
		res.add("telephone", telephone);
		res.add("mail", mail);
		res.add("province", province);
		res.add("city", city);
		res.add("dni", dni);
		res.add("postalCode", postalCode);
		res.add("address", address);
		res.add("user.username", username);
		res.add("user.password", password);
		
		return res;

	}
	
	@DisplayName("Custom GanaderoValidator tests")
	@Nested
	class ValidatorCustomGanaderoValidatorTests {
		/* Estos son tests para el GanaderoValidator ya que no hay otra forma de probarlos, pues es invocado desde
		 * el Controlador con initBinder*/
		
		@BeforeEach
		/*This is necessary because all calls are registered and preserved for all the tests*/
		void resetMockUps() {
			Mockito.reset(ganaderoService, userService);
		}

		@WithMockUser(value="spring")
		@Test
		public void shouldRejectDuplicateDNITest() throws Exception {
			/*1. Arrange */
			String duplicateDNI = "12345678A";
			Ganadero ganadero = GanaderoServiceTests.generateGanadero("Antonio", "Banderas", "0123456789", 
					"anton@mail.com", "Sevilla", "Dos Hermanas", duplicateDNI, "41345", 
					"Calle Antonio Mairena, 8", "antonio1", "12345");
			Ganadero ganaderoDuplicate = GanaderoServiceTests.generateGanadero("Antonio", "Banderas", "0123456789", 
					"anton@mail.com", "Sevilla", "Dos Hermanas", duplicateDNI, "41345", 
					"Calle Antonio Mairena, 8", "antonio1", "12345");
			Optional<Ganadero> ganaderoDuplicateOptinal = Optional.of(ganaderoDuplicate);
			when(ganaderoService.findGanaderoByDni(duplicateDNI)).thenReturn(ganaderoDuplicateOptinal);
			
			MultiValueMap<String, String> ganaderoParams = ganaderoToMultiValueMap(ganadero);
			/*2. Act */
			mockMvc.perform(post("/ganadero/new").with(csrf()).params(ganaderoParams))
			/*3.Assert */
			.andExpect(status().isOk())
			.andExpect(model().attributeHasErrors("ganadero"))
			.andExpect(model().attributeErrorCount("ganadero", 1))
			.andExpect(model().attributeHasFieldErrorCode("ganadero", "dni", "ganadero.dni.repetido"));
			
			verify(ganaderoService).findGanaderoByDni(duplicateDNI);
			
		}

		@WithMockUser(value="spring")
		@Test
		public void shouldRejectDuplicateUsernameTest() throws Exception {
			/*1. Arrange */
			String duplicateUsername = "ganaderoUsername";
			Ganadero ganadero = GanaderoServiceTests.generateGanadero("Antonio", "Banderas", "0123456789", 
					"anton@mail.com", "Sevilla", "Dos Hermanas", "12345678A", "41345", 
					"Calle Antonio Mairena, 8", duplicateUsername, "12345");
			Ganadero ganaderoDuplicate = GanaderoServiceTests.generateGanadero("Antonio", "Banderas", "0123456789", 
					"anton@mail.com", "Sevilla", "Dos Hermanas", "12345678A", "41345", 
					"Calle Antonio Mairena, 8", duplicateUsername, "12345");
			Optional<User> userDuplicateOptinal = Optional.of(ganaderoDuplicate.getUser());
			when(userService.getUserByUsername(duplicateUsername)).thenReturn(userDuplicateOptinal);
			
			MultiValueMap<String, String> ganaderoParams = ganaderoToMultiValueMap(ganadero);
			/*2. Act */
			mockMvc.perform(post("/ganadero/new").with(csrf()).params(ganaderoParams))
			/*3.Assert */
			.andExpect(status().isOk())
			.andExpect(model().attributeHasErrors("ganadero"))
			.andExpect(model().attributeErrorCount("ganadero", 1))
			.andExpect(model().attributeHasFieldErrorCode("ganadero", "user.username", "user.username.repetido"));
			
			verify(userService).getUserByUsername(duplicateUsername);
			
		}
		
		@WithMockUser(value="spring")
		@ParameterizedTest()
		@ValueSource(strings = {"username Ganadero", " usernameGanadero", "usernameGanadero "})
		public void shouldRejectUsernameTest(String username) throws Exception {
			/*1. Arrange */
			Ganadero ganadero = GanaderoServiceTests.generateGanadero("Antonio", "Banderas", "0123456789", 
					"anton@mail.com", "Sevilla", "Dos Hermanas", "12345678A", "41345", 
					"Calle Antonio Mairena, 8", username, "12345");
			
			MultiValueMap<String, String> ganaderoParams = ganaderoToMultiValueMap(ganadero);
			/*2. Act */
			mockMvc.perform(post("/ganadero/new").with(csrf()).params(ganaderoParams))
			/*3.Assert */
			.andExpect(status().isOk())
			.andExpect(model().attributeHasErrors("ganadero"))
			.andExpect(model().attributeErrorCount("ganadero", 1))
			.andExpect(model().attributeHasFieldErrorCode("ganadero", "user.username", "user.username.contieneEspacios"));
			
		}
		
		@WithMockUser(value="spring")
		@ParameterizedTest()
		@ValueSource(strings = {"username Ganadero", " usernameGanadero", "usernameGanadero "})
		public void shouldRejectPasswordTest(String password) throws Exception {
			/*1. Arrange */
			Ganadero ganadero = GanaderoServiceTests.generateGanadero("Antonio", "Banderas", "0123456789", 
					"anton@mail.com", "Sevilla", "Dos Hermanas", "12345678A", "41345", 
					"Calle Antonio Mairena, 8", "ganaderoUsername", password);
			
			MultiValueMap<String, String> ganaderoParams = ganaderoToMultiValueMap(ganadero);
			/*2. Act */
			mockMvc.perform(post("/ganadero/new").with(csrf()).params(ganaderoParams))
			/*3.Assert */
			.andExpect(status().isOk())
			.andExpect(model().attributeHasErrors("ganadero"))
			.andExpect(model().attributeErrorCount("ganadero", 1))
			.andExpect(model().attributeHasFieldErrorCode("ganadero", "user.password", "user.password.contieneEspacios"));
			
		}

	}
	
	
}
