
package org.springframework.samples.petclinic.web.e2e;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Transactional
//@TestPropertySource(locations = "classpath:application-mysql.properties")
public class CitaControllerE2ETests {

	private static final int	TEST_CONTRATO_ID	= 2;

	private static final int	TEST_CITA_ID		= 2;

	private static final int	TEST_CITA_ID_2		= 4;

	@Autowired
	private MockMvc				mockMvc;


	// 1. --- Métodos crear una cita

	@WithMockUser(username = "ganadero1", authorities = {
		"ganadero"
	})
	@Test
	void testCrearCitaGET() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/ganadero/contrato/{contratoId}/cita/new", CitaControllerE2ETests.TEST_CONTRATO_ID))
		    .andExpect(MockMvcResultMatchers.status().isOk())
		    .andExpect(MockMvcResultMatchers.view().name("cita/createCitaForm"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("citaForm"))
			.andExpect(MockMvcResultMatchers.model().attribute("citaForm", Matchers.hasProperty("fecha")))
			.andExpect(MockMvcResultMatchers.model().attribute("citaForm", Matchers.hasProperty("horaInicio")))
			.andExpect(MockMvcResultMatchers.model().attribute("citaForm", Matchers.hasProperty("duracion")))
			.andExpect(MockMvcResultMatchers.model().attribute("citaForm", Matchers.hasProperty("motivo")))
			.andExpect(MockMvcResultMatchers.model().attribute("citaForm", Matchers.hasProperty("contratoId")));
	}

	@WithMockUser(username = "ganadero1", authorities = {
		"ganadero"
	})
	@Test
	void testPositivoCrearCitaPOST() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/ganadero/contrato/{contratoId}/cita/new", CitaControllerE2ETests.TEST_CONTRATO_ID)
			.with(SecurityMockMvcRequestPostProcessors.csrf())
			.param("fecha", "2020/10/01")
			.param("horaInicio", "10:00")
			.param("duracion", "1")
			.param("motivo", "Una de las vacas no da leche")
			.param("contratoId", "2"))
		    .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
		    .andExpect(MockMvcResultMatchers.view().name("redirect:/ganadero/cita/list"));
	}

	@WithMockUser(username = "ganadero1", authorities = {
		"ganadero"
	})
	@Test
	void testNegativoCrearCitaPOST() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/ganadero/contrato/{contratoId}/cita/new", CitaControllerE2ETests.TEST_CONTRATO_ID)
			.with(SecurityMockMvcRequestPostProcessors.csrf())
			.param("fecha", "2020/10/01")
			//Patrón horaInicio --> "^(0|1)[0-9]:00$". No sigue este patrón --> ERROR
			.param("horaInicio", "125250:00")
			.param("duracion", "1")
			.param("motivo", "Una de las vacas no da leche")
			.param("contratoId", "2"))
		    .andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("citaForm", "horaInicio"))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("cita/createCitaForm"));
	}

	// 2. --- Métodos listar las citas de un ganadero

	/*
	 * Este método te muestra la lista de citas y además podrás seleccionar una
	 * explotación ganadera, de la cual, una vez seleccionada, podrás elegir un contrato
	 * que tengas con algún veterinario sobre esa explotación ganadera para realizar una
	 * petición de una nueva cita sobre ese contrato pinchando en un botón. Por ello
	 * en este caso hay un método de GET y otro de POST. Si rellenas correctamente
	 * los datos, en realidad el método de POST te llevará al método de crear una
	 * nueva cita, con el id del contrato seleccionado.
	 */

	@WithMockUser(username = "ganadero1", authorities = {
		"ganadero"
	})
	@Test
	void testListarCitasGanaderoGET() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/ganadero/cita/list"))
		    .andExpect(MockMvcResultMatchers.status().isOk())
		    .andExpect(MockMvcResultMatchers.view().name("cita/listCitasForm"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("citasAceptadas"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("citasPendientes"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("citasTodas"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("explotacionesGanadero"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("contratos"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("selectCreateCitaForm"))
			.andExpect(MockMvcResultMatchers.model().attribute("selectCreateCitaForm", Matchers.hasProperty("explotacionGanadera")))
			.andExpect(MockMvcResultMatchers.model().attribute("selectCreateCitaForm", Matchers.hasProperty("contrato")));
	}

	@WithMockUser(username = "ganadero1", authorities = {
		"ganadero"
	})
	@Test
	void testPositivoListarCitasGanaderoPOST() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/ganadero/cita/list")
			.with(SecurityMockMvcRequestPostProcessors.csrf())
			.param("explotacionGanadera.id", "1")
			.param("contrato.id", "2"))
		    .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
			.andExpect(MockMvcResultMatchers.view().name("redirect:/ganadero/contrato/" + CitaControllerE2ETests.TEST_CONTRATO_ID + "/cita/new"));
	}

	@WithMockUser(username = "ganadero1", authorities = {
		"ganadero"
	})
	@Test
	void testNegativoListarCitasGanaderoPOST() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/ganadero/cita/list")
			.with(SecurityMockMvcRequestPostProcessors.csrf())
			.param("explotacionGanadera.id", "1"))
			//Tiene errores en el atributo contrato, ya que no dispone de un contrato
			.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("selectCreateCitaForm", "contrato"))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("/ganadero/cita/list"));
	}

	// 3. --- Métodos listar las citas de un veterinario

	@WithMockUser(username = "veterinario1", authorities = {
		"veterinario"
	})
	@Test
	void testListarCitasVeterinario() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/veterinario/cita/list"))
		    .andExpect(MockMvcResultMatchers.status().isOk())
		    .andExpect(MockMvcResultMatchers.view().name("cita/listCitasForm"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("citasAceptadas"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("citasPendientes"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("citasTodas"));
	}

	// 4. --- Métodos rechazar citas

	@WithMockUser(username = "veterinario1", authorities = {
		"veterinario"
	})
	@Test
	void testRechazarCitasGET() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/veterinario/cita/{citaId}/decline", CitaControllerE2ETests.TEST_CITA_ID))
		    .andExpect(MockMvcResultMatchers.status().isOk())
		    .andExpect(MockMvcResultMatchers.view().name("cita/citaDeclineForm"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("cita"))
			.andExpect(MockMvcResultMatchers.model().attribute("cita", Matchers.hasProperty("fechaHoraInicio")))
			.andExpect(MockMvcResultMatchers.model().attribute("cita", Matchers.hasProperty("fechaHoraFin")))
			.andExpect(MockMvcResultMatchers.model().attribute("cita", Matchers.hasProperty("motivo")))
			.andExpect(MockMvcResultMatchers.model().attribute("cita", Matchers.hasProperty("rechazoJustificacion", Matchers.blankOrNullString())))
			.andExpect(MockMvcResultMatchers.model().attribute("cita", Matchers.hasProperty("estado")))
			.andExpect(MockMvcResultMatchers.model().attribute("cita", Matchers.hasProperty("contrato")));
	}

	@WithMockUser(username = "veterinario1", authorities = {
		"veterinario"
	})
	@Test
	void testPositivoRechazarCitasPOST() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/veterinario/cita/{citaId}/decline", CitaControllerE2ETests.TEST_CITA_ID)
			.with(SecurityMockMvcRequestPostProcessors.csrf())
			.param("rechazoJustificacion", "Ese día no estoy disponible, me ha surgido un imprevisto. Pida una cita otro día.")
			.param("fechaHoraInicio", "2020/10/01 10:00")
			.param("fechaHoraFin", "2020/10/01 11:00")
			.param("motivo", "Una de las vacas no da leche")
			.param("estado", "PENDIENTE"))
		    .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
		    .andExpect(MockMvcResultMatchers.view().name("redirect:/veterinario/cita/list"));
	}

	@WithMockUser(username = "veterinario1", authorities = {
		"veterinario"
	})
	@Test
	void testNegativoRechazarCitasPOST() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/veterinario/cita/{citaId}/decline", CitaControllerE2ETests.TEST_CITA_ID)
			//La justificación de rechazarla no puede estar vacía
			.with(SecurityMockMvcRequestPostProcessors.csrf())
			.param("rechazoJustificacion", "")
			.param("fechaHoraInicio", "2020/10/01 10:00")
			.param("fechaHoraFin", "2020/10/01 11:00")
			.param("motivo", "Una de las vacas no da leche")
			.param("estado", "PENDIENTE"))
		    .andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("cita", "rechazoJustificacion"))
		    .andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("cita/citaDeclineForm"));
	}

	// 5. --- Métodos aceptar citas

	@WithMockUser(username = "veterinario1", authorities = {
		"veterinario"
	})
	@Test
	void testAceptarCitas() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/veterinario/cita/{citaId}/accept", CitaControllerE2ETests.TEST_CITA_ID_2))
		    .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
			.andExpect(MockMvcResultMatchers.view().name("redirect:/veterinario/cita/list"));
	}

	@DisplayName("Custom CitaFormValidator tests")
	@Nested
	class CitaFormCustomValidatorTests{
		/*Probando que el validador CitaFormValidator funciona correctamente */
		
		@WithMockUser(username = "ganadero1", authorities = {
			"ganadero"
		})
		@Test
		public void shouldRejectDateCitaFormBeforeTomorrow() throws Exception {
		   
			mockMvc.perform(post("/ganadero/contrato/{contratoId}/cita/new", CitaControllerE2ETests.TEST_CONTRATO_ID).with(csrf())
				   //La fecha es anterior a la actual, por lo que debería saltar el error correspondiente del custom validator
				   .param("fecha","2020/05/01")
			       .param("horaInicio","10:00")
				   .param("duracion","1")
				   .param("motivo","Una de las vacas no da leche")
				   .param("contratoId","2"))
			       .andExpect(status().isOk())
			       .andExpect(view().name("cita/createCitaForm"))
			       .andExpect(model().attributeHasErrors("citaForm"))
			       .andExpect(model().attributeErrorCount("citaForm", 1))
			       .andExpect(model().attributeHasFieldErrorCode("citaForm","fecha", "citaForm.fecha.fechaDeLaCitaNoDespuesDeLaFechaActual"));
		}
		
		@WithMockUser(username = "ganadero1", authorities = {
			"ganadero"
		})
		@ParameterizedTest()
		@ValueSource(strings = {"2020/03/09", "2021/06/11", "2021/09/25"})
		public void shouldRejectDateCitaFormNotInDateRangeOfHisContrato(String date) throws Exception {
			/* Restricción: La fecha de la cita que se quiere pedir debe estar entre la fecha inicial y final de su contrato. */
			//El contrato con id 2 finaliza en 2022, por lo que esto debería dar fallo
			
			mockMvc.perform(post("/ganadero/contrato/{contratoId}/cita/new", CitaControllerE2ETests.TEST_CONTRATO_ID).with(csrf())
				//La fecha no está dentro de las fechas del controlador, por lo que debería saltar el error correspondiente del custom validator
				   .param("fecha","2030/05/01")
			       .param("horaInicio","10:00")
				   .param("duracion","1")
				   .param("motivo","Una de las vacas no da leche")
				   .param("contratoId","2"))
			       .andExpect(status().isOk())
			       .andExpect(model().attributeHasErrors("citaForm"))
			       .andExpect(model().attributeErrorCount("citaForm", 1))
			       .andExpect(model().attributeHasFieldErrorCode("citaForm","fecha","citaForm.fecha.fueraDelRangoDeFechasDelContrato"));
		}
		
		@WithMockUser(username = "ganadero1", authorities = {
			"ganadero"
		})
		@Test
		public void shouldRejectDateCitaFormBeingGanaderoBecauseYouHaveAnotherCitaInThisRange() throws Exception {
			/*
			 * Restricción: No se puede pedir una cita que solape a otra, es decir, si tienes una pendiente o aceptada
			 * para ese rango de tiempo, no puedes pedir otra.
			 */
			//Este ganadero ya tiene una cita para ese momento. Tiene una cita de 2020-10-01 11:00 a 2020-10-01 12:00
			mockMvc.perform(post("/ganadero/contrato/{contratoId}/cita/new", CitaControllerE2ETests.TEST_CONTRATO_ID).with(csrf())
				   .param("fecha","2020/10/01")
			       .param("horaInicio","11:00")
				   .param("duracion","2")
				   .param("motivo","Una de las vacas no da leche")
				   .param("contratoId","2"))
			       .andExpect(status().isOk())
			       .andExpect(model().attributeHasErrors("citaForm"))
			       .andExpect(model().attributeErrorCount("citaForm", 1))
			       .andExpect(model().attributeHasFieldErrorCode("citaForm","fecha","citaForm.fecha.existeCitaSolapada"));
			
		}
		
	}

	
	@DisplayName("Custom CitaValidator tests")
	@Nested
	class CitaCustomValidatorTests{
		/*
		 * Este validador se usa cuando se va a rechazar una cita. Este tiene una única restricción, que cuando
		 * se rechaza la cita, la propiedad rechazoJustificacion de la cita debe rellenarse
		 */
		
		@WithMockUser(username = "veterinario1", authorities = {
			"veterinario"
		})
		@Test
		public void shouldNotRejectCitaRechazoJustificacionEmptyTest() throws Exception{
			
			mockMvc.perform(MockMvcRequestBuilders.post("/veterinario/cita/{citaId}/decline", CitaControllerE2ETests.TEST_CITA_ID)
				//La justificación de rechazarla no puede estar vacía
				.with(SecurityMockMvcRequestPostProcessors.csrf())
				.param("rechazoJustificacion", "")
				.param("fechaHoraInicio","2020/10/01 10:00")
				.param("fechaHoraFin","2020/10/01 11:00")
				.param("motivo","Una de las vacas no da leche")
				.param("estado","PENDIENTE"))
			    .andExpect(status().isOk())
			    .andExpect(model().attributeHasErrors("cita"))
			    .andExpect(model().attributeErrorCount("cita", 1))
			    .andExpect(model().attributeHasFieldErrorCode("cita","rechazoJustificacion","citas.rechazoJustificacion.vacio"));
		}
	}
	
}
