
package org.springframework.samples.petclinic.web;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.forms.CitaForm;
import org.springframework.samples.petclinic.model.Cita;
import org.springframework.samples.petclinic.model.TipoEstadoCita;
import org.springframework.samples.petclinic.model.ValidatorCitaFormTests;
import org.springframework.samples.petclinic.service.CitaNonSolitaryServiceTests;
import org.springframework.samples.petclinic.service.CitaService;
import org.springframework.samples.petclinic.service.CitaServiceTests;
import org.springframework.samples.petclinic.service.ContratoService;
import org.springframework.samples.petclinic.service.ExplotacionGanaderaService;
import org.springframework.samples.petclinic.service.GanaderoService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.hamcrest.Matchers.blankOrNullString;

@WebMvcTest(
	/* Controlador a instanciar para probarlo */
	controllers = CitaController.class,
	/* Excluir los componentes de seguridad al instanciar el controlador */
	excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
/*
 * PD: Si cargamos los componentes de seguridad, tal como esta implementado (Con roles y usuarios),
 * tambien tendríamos que cargar el contexto de la aplicacion completo llegando hasta la capad de datos.
 * Si hacemos esto, estaríamos hablando mas bien de un test de integración, no de test unitarios
 */
public class CitaControllerTests {

	private static final int			TEST_CONTRATO_ID	= 2;

	private static final int			TEST_CITA_ID		= 2;

	@Autowired
	private MockMvc						mockMvc;

	@MockBean
	private CitaService					citaService;

	@MockBean
	private ExplotacionGanaderaService	explotacionGanaderaService;

	@MockBean
	private ContratoService				contratoService;
 
    //Datos de prueba
	//Objeto CitaForm que se usa para las pruebas
	public CitaForm citaFormForTesting() throws ParseException {
		CitaForm citaForm = new CitaForm();
		citaForm.setFecha(CitaControllerTests.newDateFormattedWithoutHour("2020/10/01"));
		citaForm.setHoraInicio("10:00");
		citaForm.setDuracion(1);
		citaForm.setMotivo("Una de las vacas no da leche");
		citaForm.setContratoId(CitaControllerTests.TEST_CONTRATO_ID);
		return citaForm;
	}
		
	//Cita que se usa para las pruebas
	public Cita citaForTesting() throws ParseException {
		Cita cita = new Cita();
		cita.setFechaHoraInicio(CitaNonSolitaryServiceTests.newDateFormatted("2020/10/01 10:00"));
		// 1 hora más tarde que la fecha inicio, ya que la duración es de 1 hora como se determinó en el citaForm
		cita.setFechaHoraFin(CitaNonSolitaryServiceTests.newDateFormatted("2020/10/01 11:00"));
		cita.setMotivo("Una de las vacas no da leche"); //Mismo motivo que citaForm
		cita.setEstado(TipoEstadoCita.PENDIENTE); //Al crear la cita, el estado es pendiente
		/*
		* Debemos simular un contrato tmb al cual hace referencia la cita.
	    * En la clase de los test de servicio de citas, tenemos un método que crea
		* un contrato de prueba
		*/
		cita.setContrato(CitaServiceTests.contrato1ParaPruebas());
		return cita;
	}
	
	@BeforeEach
	void setup() throws ParseException {
		CitaForm citaForm = new CitaForm();
		BDDMockito.given(this.citaService.createCitaForm()).willReturn(citaForm);
		citaForm = citaFormForTesting();
		Cita cita = citaForTesting();
		BDDMockito.given(this.citaService.reconstruct(citaForm)).willReturn(cita);
		BDDMockito.given(this.citaService.findCitaById(CitaControllerTests.TEST_CITA_ID)).willReturn(Optional.of(cita));
		
		//Métodos usados en el validador: CitaFormValidator
		BDDMockito.given(contratoService.findContratoById(TEST_CONTRATO_ID)).willReturn(Optional.of(CitaServiceTests.contrato1ParaPruebas()));
		
		Date fechaInicioCita = citaForm.getFecha();
		String horaInicio = citaForm.getHoraInicio();
		Integer duracion = citaForm.getDuracion();
		Date fechaHoraInicio = CitaNonSolitaryServiceTests.newDateFormatted("2020/10/01 10:00");
		Date fechaHoraFin = CitaNonSolitaryServiceTests.newDateFormatted("2020/10/01 11:00");
		BDDMockito.given(this.citaService.reconstructFechaHoraInicioCita(fechaInicioCita, horaInicio)).willReturn(fechaHoraInicio);
		BDDMockito.given(this.citaService.reconstructFechaHoraFinCita(fechaHoraInicio, duracion)).willReturn(fechaHoraFin);
		List<Cita> listaCitasVacia = new ArrayList<>();
		BDDMockito.given(this.citaService.findCitasAceptadasYPendientesByGanaderoIdAndDateTimeRange(fechaHoraInicio,fechaHoraFin)).willReturn(listaCitasVacia);
	}

	// 1. --- Métodos crear una cita

	@WithMockUser(value = "spring")
	@Test
	void testCrearCitaGET() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/ganadero/contrato/{contratoId}/cita/new", CitaControllerTests.TEST_CONTRATO_ID))
		    .andExpect(MockMvcResultMatchers.status().isOk())
		    .andExpect(MockMvcResultMatchers.view().name("cita/createCitaForm"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("citaForm"))
			.andExpect(MockMvcResultMatchers.model().attribute("citaForm", Matchers.hasProperty("fecha")))
			.andExpect(MockMvcResultMatchers.model().attribute("citaForm", Matchers.hasProperty("horaInicio")))
			.andExpect(MockMvcResultMatchers.model().attribute("citaForm", Matchers.hasProperty("duracion")))
			.andExpect(MockMvcResultMatchers.model().attribute("citaForm", Matchers.hasProperty("motivo")))
			.andExpect(MockMvcResultMatchers.model().attribute("citaForm", Matchers.hasProperty("contratoId")));
		Mockito.verify(this.citaService).createCitaForm();
	}

	@WithMockUser(value = "spring")
	@Test
	void testPositivoCrearCitaPOST() throws Exception {
		  this.mockMvc.perform(MockMvcRequestBuilders.post("/ganadero/contrato/{contratoId}/cita/new", CitaControllerTests.TEST_CONTRATO_ID)
		    .with(SecurityMockMvcRequestPostProcessors.csrf())
		    .param("fecha","2020/10/01")
		    .param("horaInicio","10:00")
			.param("duracion","1")
			.param("motivo","Una de las vacas no da leche")
			.param("contratoId","2"))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
			.andExpect(MockMvcResultMatchers.view().name("redirect:/ganadero/cita/list"));
		//Creando un citaForm falso para poder pasarlo como parámetro
		CitaForm citaForm = citaFormForTesting();
		Mockito.verify(this.citaService).reconstruct(citaForm);
		Mockito.verify(this.citaService).saveCita(this.citaService.reconstruct(citaForm));
	}

	@WithMockUser(value = "spring")
	@Test
	void testNegativoCrearCitaPOST() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/ganadero/contrato/{contratoId}/cita/new", CitaControllerTests.TEST_CONTRATO_ID)
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

	@WithMockUser(value = "spring")
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
		Mockito.verify(this.citaService).findCitasFuturasByLoggedGanaderoAndEstado(TipoEstadoCita.ACEPTADA);
		Mockito.verify(this.citaService).findCitasFuturasByLoggedGanaderoAndEstado(TipoEstadoCita.PENDIENTE);
		Mockito.verify(this.citaService).findCitasByLoggedGanaderoAndFilters(null, null, null);
		Mockito.verify(this.explotacionGanaderaService).findExpGanaderaByGanaderoId(false);
		Mockito.verify(this.contratoService).findContratosVigentesAceptadosByLogguedGanadero();
	}

	@WithMockUser(value = "spring")
	@Test
	void testPositivoListarCitasGanaderoPOST() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/ganadero/cita/list")
			.with(SecurityMockMvcRequestPostProcessors.csrf())
			.param("explotacionGanadera.id", "1")
			.param("contrato.id", "2"))
		    .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
			.andExpect(MockMvcResultMatchers.view().name("redirect:/ganadero/contrato/" + CitaControllerTests.TEST_CONTRATO_ID + "/cita/new"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testNegativoListarCitasGanaderoPOST() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/ganadero/cita/list")
			.with(SecurityMockMvcRequestPostProcessors.csrf())
			.param("explotacionGanadera.id", "1"))
	    //Tiene errores en el atributo contrato, ya que no dispone de un contrato
		    .andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("selectCreateCitaForm","contrato"))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("/ganadero/cita/list"));
	}

	// 3. --- Métodos listar las citas de un veterinario

	@WithMockUser(value = "spring")
	@Test
	void testListarCitasVeterinario() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/veterinario/cita/list"))
		    .andExpect(MockMvcResultMatchers.status().isOk())
		    .andExpect(MockMvcResultMatchers.view().name("cita/listCitasForm"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("citasAceptadas"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("citasPendientes"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("citasTodas"));
		Mockito.verify(this.citaService).findCitasFuturasByLoggedVeterinarioAndEstado(TipoEstadoCita.ACEPTADA);
		Mockito.verify(this.citaService).findCitasFuturasByLoggedVeterinarioAndEstado(TipoEstadoCita.PENDIENTE);
		Mockito.verify(this.citaService).findCitasByLoggedVeterinarioAndFilters(null, null, null);
	}

	// 4. --- Métodos rechazar citas

	@WithMockUser(value = "spring")
	@Test
	void testRechazarCitasGET() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/veterinario/cita/{citaId}/decline", CitaControllerTests.TEST_CITA_ID))
		    .andExpect(MockMvcResultMatchers.status().isOk())
		    .andExpect(MockMvcResultMatchers.view().name("cita/citaDeclineForm"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("cita"))
			.andExpect(MockMvcResultMatchers.model().attribute("cita", Matchers.hasProperty("fechaHoraInicio")))
			.andExpect(MockMvcResultMatchers.model().attribute("cita", Matchers.hasProperty("fechaHoraFin")))
			.andExpect(MockMvcResultMatchers.model().attribute("cita", Matchers.hasProperty("motivo")))
			.andExpect(MockMvcResultMatchers.model().attribute("cita", Matchers.hasProperty("rechazoJustificacion", blankOrNullString())))
			.andExpect(MockMvcResultMatchers.model().attribute("cita", Matchers.hasProperty("estado")))
			.andExpect(MockMvcResultMatchers.model().attribute("cita", Matchers.hasProperty("contrato")));
		Mockito.verify(this.citaService).findCitaById(CitaControllerTests.TEST_CITA_ID);
	}

	@WithMockUser(value = "spring")
	@Test
	void testPositivoRechazarCitasPOST() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/veterinario/cita/{citaId}/decline", CitaControllerTests.TEST_CITA_ID)
			.with(SecurityMockMvcRequestPostProcessors.csrf())
			.param("rechazoJustificacion","Ese día no estoy disponible, me ha surgido un imprevisto. Pida una cita otro día.")
			.param("fechaHoraInicio","2020/10/01 10:00")
			.param("fechaHoraFin","2020/10/01 11:00")
			.param("motivo","Una de las vacas no da leche")
			.param("estado","PENDIENTE"))
		    .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
		    .andExpect(MockMvcResultMatchers.view().name("redirect:/veterinario/cita/list"));
		Mockito.verify(this.citaService).findCitaById(CitaControllerTests.TEST_CITA_ID);
		
		/*Simularemos una cita para el método decline cita. Esta sería la cita que se
		  recibiría en el metodo findCitaById(citaId)*/
		Cita cita = citaForTesting();
		cita.setRechazoJustificacion("Ese día no estoy disponible, me ha surgido un imprevisto. Pida una cita otro día.");
		Mockito.verify(this.citaService).declineCita(cita);
	}

	@WithMockUser(value = "spring")
	@Test
	void testNegativoRechazarCitasPOST() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/veterinario/cita/{citaId}/decline", CitaControllerTests.TEST_CITA_ID)
			//La justificación de rechazarla no puede estar vacía
			.with(SecurityMockMvcRequestPostProcessors.csrf())
			.param("rechazoJustificacion", "")
			.param("fechaHoraInicio","2020/10/01 10:00")
			.param("fechaHoraFin","2020/10/01 11:00")
			.param("motivo","Una de las vacas no da leche")
			.param("estado","PENDIENTE"))
		    .andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("cita", "rechazoJustificacion"))
		    .andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("cita/citaDeclineForm"));
	}

	// 5. --- Métodos aceptar citas

	@WithMockUser(value = "spring")
	@Test
	void testAceptarCitas() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/veterinario/cita/{citaId}/accept", CitaControllerTests.TEST_CITA_ID))
		    .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
			.andExpect(MockMvcResultMatchers.view().name("redirect:/veterinario/cita/list"));
		Mockito.verify(this.citaService).findCitaById(CitaControllerTests.TEST_CITA_ID);
		Mockito.verify(this.citaService).acceptCita(citaForTesting());
	}

	
	
	@DisplayName("Custom CitaFormValidator tests")
	@Nested
	class CitaFormCustomValidatorTests{
		/*Probando que el validador CitaFormValidator funciona correctamente */
		
		@BeforeEach
		void resetMockUps() {
			Mockito.reset(citaService, explotacionGanaderaService, contratoService);
		}
		
		@WithMockUser(value = "spring")
		@Test
		public void shouldRejectDateCitaFormBeforeTomorrow() throws Exception {
		    /* Restricción: La cita que se intenta pedir debe ser como mínimo para el día de mañana */
			Mockito.when(contratoService.findContratoById(TEST_CONTRATO_ID)).thenReturn(Optional.of(CitaServiceTests.contrato1ParaPruebas()));
			
			Date fechaHoraInicio = new Date();
			Date fechaHoraFin = new Date();
			List<Cita> citasSolapadas = new ArrayList<>();
			CitaForm citaForm = citaFormForTesting();
			Mockito.when(citaService.reconstructFechaHoraInicioCita(citaForm.getFecha(), citaForm.getHoraInicio())).thenReturn(fechaHoraInicio);
			Mockito.when(citaService.reconstructFechaHoraFinCita(fechaHoraInicio, citaForm.getDuracion())).thenReturn(fechaHoraFin);
			Mockito.when(citaService.findCitasAceptadasYPendientesByGanaderoIdAndDateTimeRange(fechaHoraInicio, fechaHoraFin)).thenReturn(citasSolapadas);
			
			mockMvc.perform(post("/ganadero/contrato/{contratoId}/cita/new", CitaControllerTests.TEST_CONTRATO_ID).with(csrf())
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
		
		@WithMockUser(value = "spring")
		@ParameterizedTest()
		@ValueSource(strings = {"2020/03/09", "2021/06/11", "2021/09/25"})
		public void shouldRejectDateCitaFormNotInDateRangeOfHisContrato(String date) throws Exception {
			/* Restricción: La fecha de la cita que se quiere pedir debe estar entre la fecha inicial y final de su contrato. */
			/* El contrato usado para esta comprobación es el contrato1ParaPruebas() de la clase CitaServiceTests. Este
			   contrato empieza en la fecha 2020/03/10 (yyyy/MM/dd) y finaliza en la fecha 2021/06/10 (yyyy/MM/dd) */
			Mockito.when(contratoService.findContratoById(TEST_CONTRATO_ID)).thenReturn(Optional.of(CitaServiceTests.contrato1ParaPruebas()));
			
			Date fechaHoraInicio = new Date();
			Date fechaHoraFin = new Date();
			List<Cita> citasSolapadas = new ArrayList<>();
			CitaForm citaForm = citaFormForTesting();
			Mockito.when(citaService.reconstructFechaHoraInicioCita(citaForm.getFecha(), citaForm.getHoraInicio())).thenReturn(fechaHoraInicio);
			Mockito.when(citaService.reconstructFechaHoraFinCita(fechaHoraInicio, citaForm.getDuracion())).thenReturn(fechaHoraFin);
			Mockito.when(citaService.findCitasAceptadasYPendientesByGanaderoIdAndDateTimeRange(fechaHoraInicio, fechaHoraFin)).thenReturn(citasSolapadas);
			
			mockMvc.perform(post("/ganadero/contrato/{contratoId}/cita/new", CitaControllerTests.TEST_CONTRATO_ID).with(csrf())
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
		
		@WithMockUser(value = "spring")
		@Test
		public void shouldRejectDateCitaFormBeingGanaderoBecauseYouHaveAnotherCitaInThisRange() throws Exception {
			/*
			 * Restricción: No se puede pedir una cita que solape a otra, es decir, si tienes una pendiente o aceptada
			 * para ese rango de tiempo, no puedes pedir otra.
			 * Veo innecesario hacerlo parametrizado, ya que esté metodo únicamente verifica si existe una cita
			 * pendiente o aceptada para la fecha de la nueva cita que se intenta pedir. Al hacer mock de esta 
			 * llamada, definimos que existe una cita, por lo que este test siempre saltará la excepción. Así
			 * que se hace para comprobar que si salta esta excepción, nos devuelve la correcta.
			 */
			
			/* Mockeamos los métodos del servicio necesarios, usados en el validador */
			Mockito.when(contratoService.findContratoById(TEST_CONTRATO_ID)).thenReturn(Optional.of(CitaServiceTests.contrato1ParaPruebas()));
			
			Date fechaHoraInicio = new Date();
			Date fechaHoraFin = new Date();
			List<Cita> citasSolapadas = new ArrayList<>();
			citasSolapadas.add(new Cita());
			CitaForm citaForm = citaFormForTesting();
			Mockito.when(citaService.reconstructFechaHoraInicioCita(citaForm.getFecha(), citaForm.getHoraInicio())).thenReturn(fechaHoraInicio);
			Mockito.when(citaService.reconstructFechaHoraFinCita(fechaHoraInicio, citaForm.getDuracion())).thenReturn(fechaHoraFin);
			Mockito.when(citaService.findCitasAceptadasYPendientesByGanaderoIdAndDateTimeRange(fechaHoraInicio, fechaHoraFin)).thenReturn(citasSolapadas);
			
			mockMvc.perform(post("/ganadero/contrato/{contratoId}/cita/new", CitaControllerTests.TEST_CONTRATO_ID).with(csrf())
				   .param("fecha","2020/10/01")
			       .param("horaInicio","10:00")
				   .param("duracion","1")
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
		
		@BeforeEach
		void resetMockUps() {
			Mockito.reset(citaService, explotacionGanaderaService, contratoService);
		}
		
		@WithMockUser(value = "spring")
		@Test
		public void shouldNotRejectCitaRechazoJustificacionEmptyTest() throws Exception{
			
			mockMvc.perform(MockMvcRequestBuilders.post("/veterinario/cita/{citaId}/decline", CitaControllerTests.TEST_CITA_ID)
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

	//Métodos auxiliares
	public static Date newDateFormattedWithoutHour(final String fecha) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		return sdf.parse(fecha);
	}
	
}
