
package org.springframework.samples.petclinic.web;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.hasProperty;

import java.text.ParseException;
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
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.forms.RecetaForm;
import org.springframework.samples.petclinic.model.Cita;
import org.springframework.samples.petclinic.model.LineaReceta;
import org.springframework.samples.petclinic.model.Producto;
import org.springframework.samples.petclinic.model.Receta;
import org.springframework.samples.petclinic.model.TipoEstadoCita;
import org.springframework.samples.petclinic.model.Veterinario;
import org.springframework.samples.petclinic.service.CitaNonSolitaryServiceTests;
import org.springframework.samples.petclinic.service.CitaService;
import org.springframework.samples.petclinic.service.CitaServiceTests;
import org.springframework.samples.petclinic.service.LineaRecetaService;
import org.springframework.samples.petclinic.service.ProductoService;
import org.springframework.samples.petclinic.service.RecetaService;
import org.springframework.samples.petclinic.service.VetService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(
	/* Controlador a instanciar para probarlo */
	controllers = RecetaController.class,
	/* Excluir los componentes de seguridad al instanciar el controlador */
	excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
/*
 * PD: Si cargamos los componentes de seguridad, tal como esta implementado (Con roles y usuarios),
 * tambien tendríamos que cargar el contexto de la aplicacion completo llegando hasta la capad de datos.
 * Si hacemos esto, estaríamos hablando mas bien de un test de integración, no de test unitarios
 */
public class RecetaControllerTests {

	private static final int			TEST_RECETA_ID		= 1;
	
	@Autowired
	private MockMvc				mockMvc;

	@MockBean
	private RecetaService		recetaService;

	@MockBean
	private LineaRecetaService	lineaRecetaService;

	@MockBean
	private CitaService			citaService;

	@MockBean
	private ProductoService		productoService;

	@MockBean
	private VetService			vetService;

	//Datos de prueba
	//Objeto Cita que se usa para las pruebas
	public Cita citaForTesting() throws ParseException {
		Cita cita = new Cita();
		cita.setFechaHoraInicio(CitaNonSolitaryServiceTests.newDateFormatted("2020/05/31 10:00"));
		cita.setFechaHoraFin(CitaNonSolitaryServiceTests.newDateFormatted("2020/05/31 11:00"));
		cita.setMotivo("Tengo un par de cabras resfriadas");
		cita.setRechazoJustificacion(null);
		cita.setEstado(TipoEstadoCita.ACEPTADA);
		cita.setContrato(CitaServiceTests.contrato1ParaPruebas());
		return cita;
	}
	
	//Objeto RecetaForm que se usa para las pruebas
	public RecetaForm recetaFormForTesting() {
		RecetaForm recetaForm = new RecetaForm();
		recetaForm.setDescripcion("Dar diariamente a las cabras durante 1 semana");
		recetaForm.setCitaId(1);
		List<Integer> lista = new ArrayList<>();
		lista.add(1);
		recetaForm.setProductoId(lista);
		recetaForm.setCantidad(lista);
		return recetaForm;
	}
	
	//Objeto Receta que se usa para las pruebas
	public Receta recetaForTesting() throws ParseException {
		Receta receta = new Receta();
		receta.setId(1);
		receta.setFechaRealizacion(CitaNonSolitaryServiceTests.newDateFormatted("2020/06/01 10:00"));
		receta.setDescripcion("Dar diariamente a las cabras durante 1 semana");
		receta.setEsFacturado(false);
		receta.setCita(citaForTesting());
		return receta;
	}
	
	//Objeto LineaReceta que se usa para las pruebas
	public LineaReceta lineaRecetaForTesting() throws ParseException {
		LineaReceta lineaReceta = new LineaReceta();
		lineaReceta.setCantidad(4);
		lineaReceta.setPrecioVenta(4.5);
		lineaReceta.setProducto(new Producto());
		lineaReceta.setReceta(recetaForTesting());
		return lineaReceta;
	}
	
	@BeforeEach
	void setup() throws ParseException {

		Producto producto = new Producto();
		Iterable<Producto> productos = Collections.singleton(producto);
		BDDMockito.given(this.productoService.findAllProductosByLoguedVeterinario()).willReturn(productos);

		BDDMockito.given(this.recetaService.reconstruct(recetaFormForTesting())).willReturn(recetaForTesting());
		
		//Mocks para listar receta
		Receta receta = new Receta();
		Iterable<Receta> recetas = Collections.singleton(receta);
		BDDMockito.given(this.recetaService.findRecetasByLoggedVeterinario()).willReturn(recetas);
		BDDMockito.given(this.recetaService.findRecetasByLoggedGanadero()).willReturn(recetas);

		//Mocks para mostrar receta
		LineaReceta lr = new LineaReceta();
		Iterable<LineaReceta> lineasReceta = Collections.singleton(lr);
		BDDMockito.given(this.lineaRecetaService.findLineasRecetaByRecetaId(TEST_RECETA_ID)).willReturn(lineasReceta);
		BDDMockito.given(this.lineaRecetaService.getPrecioTotalRecetaByRecetaId(TEST_RECETA_ID)).willReturn(50.0);
		
	}

	// 1. --- Métodos crear una receta

	@WithMockUser(value = "spring")
	@Test
	void testPositivoCrearRecetaGET() throws Exception {
		//Mocks para el test en concreto
		
		List<Cita> citas = new ArrayList<>();
		citas.add(citaForTesting());
		/*
		 * Hago este mock con los valores en ArgumentMatchers.any() en los 2 últimos valores de la query porque
		 *ambos son una fecha que se recoge de la fecha actual. Al mockear esta fecha, si pongo una fecha diferente
		 *o incluso igual que la que se usa en el método del controlador, saltara el error de que estos parámetros
		 *no son iguales, ya que las fechas se recogen en instantes distintos. El tiempo cambia por milésimas de
		 *segundo. 
		 */
		BDDMockito.given(this.citaService.findCitasByLoggedVeterinarioAndFilters(ArgumentMatchers.eq(TipoEstadoCita.ACEPTADA),ArgumentMatchers.any(),ArgumentMatchers.any())).willReturn(citas);
		
		//Ponemos que este método devuelva 2 para que así se haga correctamente.
		BDDMockito.given(this.productoService.numeroProductosConExistenciasByVeterinarioId()).willReturn(2);
		
		this.mockMvc.perform(MockMvcRequestBuilders.get("/veterinario/receta/new"))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("receta/createRecetaForm"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("recetaForm"))
			.andExpect(MockMvcResultMatchers.model().attribute("recetaForm", Matchers.hasProperty("descripcion")))
			.andExpect(MockMvcResultMatchers.model().attribute("recetaForm", Matchers.hasProperty("citaId")))
			.andExpect(MockMvcResultMatchers.model().attribute("recetaForm", Matchers.hasProperty("productoId")))
			.andExpect(MockMvcResultMatchers.model().attribute("recetaForm", Matchers.hasProperty("cantidad")))
			.andExpect(MockMvcResultMatchers.model().attributeExists("productos"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("citas"));
		Mockito.verify(this.citaService).findCitasByLoggedVeterinarioAndFilters(ArgumentMatchers.eq(TipoEstadoCita.ACEPTADA), ArgumentMatchers.any(), ArgumentMatchers.any());
		Mockito.verify(this.productoService).numeroProductosConExistenciasByVeterinarioId();
		Mockito.verify(this.productoService).findAllProductosByLoguedVeterinario();
	}

	@WithMockUser(value = "spring")
	@Test
	void testCrearRecetaNoProductosConExistenciasForRecetaGET() throws Exception {
		//Mocks para el test en concreto
		//Ponemos que este método devuelva 0 para que así haga lo que necesitamos.
		BDDMockito.given(this.productoService.numeroProductosConExistenciasByVeterinarioId()).willReturn(0);

		Cita cita = new Cita();
		List<Cita> citas = new ArrayList<>();
		citas.add(cita);
		/*
		 * Hago este mock con los valores en ArgumentMatchers.any() en los 2 últimos valores de la query porque
		 *ambos son una fecha que se recoge de la fecha actual. Al mockear esta fecha, si pongo una fecha diferente
		 *o incluso igual que la que se usa en el método del controlador, saltara el error de que estos parámetros
		 *no son iguales, ya que las fechas se recogen en instantes distintos. El tiempo cambia por milésimas de
		 *segundo. 
		 */
		BDDMockito.given(this.citaService.findCitasByLoggedVeterinarioAndFilters(ArgumentMatchers.eq(TipoEstadoCita.ACEPTADA),ArgumentMatchers.any(),ArgumentMatchers.any())).willReturn(citas);

		this.mockMvc.perform(MockMvcRequestBuilders.get("/veterinario/receta/new"))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("receta/listRecetaForm"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("message"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("recetas"));
		Mockito.verify(this.productoService).numeroProductosConExistenciasByVeterinarioId();
		Mockito.verify(this.citaService).findCitasByLoggedVeterinarioAndFilters(ArgumentMatchers.eq(TipoEstadoCita.ACEPTADA), ArgumentMatchers.any(), ArgumentMatchers.any());
		Mockito.verify(this.recetaService).findRecetasByLoggedVeterinario();
	}

	@WithMockUser(value = "spring")
	@Test
	void testCrearRecetaNoCitasDisponiblesParaLaRecetaGET() throws Exception {
		//Mocks para el test en concreto
		//Ponemos que este método devuelva 2 para que así haga lo que necesitamos.
		BDDMockito.given(this.productoService.numeroProductosConExistenciasByVeterinarioId()).willReturn(2);

		//Ponemos que la lista de citas sea empty para que así el método salga el camino correspondiente
		Iterable<Cita> citas = Collections.emptyList();
		/*
		 * Hago este mock con los valores en ArgumentMatchers.any() en los 2 últimos valores de la query porque
		 *ambos son una fecha que se recoge de la fecha actual. Al mockear esta fecha, si pongo una fecha diferente
		 *o incluso igual que la que se usa en el método del controlador, saltara el error de que estos parámetros
		 *no son iguales, ya que las fechas se recogen en instantes distintos. El tiempo cambia por milésimas de
		 *segundo. 
		 */
		BDDMockito.given(this.citaService.findCitasByLoggedVeterinarioAndFilters(ArgumentMatchers.eq(TipoEstadoCita.ACEPTADA),ArgumentMatchers.any(),ArgumentMatchers.any())).willReturn(citas);

		this.mockMvc.perform(MockMvcRequestBuilders.get("/veterinario/receta/new"))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("receta/listRecetaForm"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("recetas"));
		Mockito.verify(this.productoService).numeroProductosConExistenciasByVeterinarioId();
		Mockito.verify(this.citaService).findCitasByLoggedVeterinarioAndFilters(ArgumentMatchers.eq(TipoEstadoCita.ACEPTADA), ArgumentMatchers.any(), ArgumentMatchers.any());
		Mockito.verify(this.recetaService).findRecetasByLoggedVeterinario();
	}

	@WithMockUser(value = "spring")
	@Test
	void testPositivoCrearRecetaPOST() throws Exception {
		
		this.mockMvc.perform(MockMvcRequestBuilders.post("/veterinario/receta/new")
			.with(SecurityMockMvcRequestPostProcessors.csrf())
			.param("descripcion", "Dar diariamente a las cabras durante 1 semana")
			.param("citaId", "1")
			.param("productoId","1")
			.param("cantidad","1"))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
			.andExpect(MockMvcResultMatchers.view().name("redirect:/veterinario/receta/list"));
		Mockito.verify(this.recetaService).reconstruct(recetaFormForTesting());
		Mockito.verify(this.recetaService).saveReceta(recetaForTesting());
		Mockito.verify(this.lineaRecetaService).reconstructAndSaveLineasReceta(recetaFormForTesting(), recetaForTesting());
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testNegativoCrearRecetaPOST() throws Exception {
		
		List<Cita> citas = new ArrayList<>();
		citas.add(citaForTesting());
		/*
		 * Hago este mock con los valores en ArgumentMatchers.any() en los 2 últimos valores de la query porque
		 *ambos son una fecha que se recoge de la fecha actual. Al mockear esta fecha, si pongo una fecha diferente
		 *o incluso igual que la que se usa en el método del controlador, saltara el error de que estos parámetros
		 *no son iguales, ya que las fechas se recogen en instantes distintos. El tiempo cambia por milésimas de
		 *segundo. 
		 */
		BDDMockito.given(this.citaService.findCitasByLoggedVeterinarioAndFilters(ArgumentMatchers.eq(TipoEstadoCita.ACEPTADA),ArgumentMatchers.any(),ArgumentMatchers.any())).willReturn(citas);
		
		this.mockMvc.perform(MockMvcRequestBuilders.post("/veterinario/receta/new")
	        .with(SecurityMockMvcRequestPostProcessors.csrf())
	        .param("descripcion", "")
	        .param("citaId", "9")
	        .param("productoId", "1","2")
	        .param("cantidad", "3","2"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.view().name("receta/createRecetaForm"))
            .andExpect(MockMvcResultMatchers.model().attributeExists("recetaForm"))
            .andExpect(MockMvcResultMatchers.model().attributeExists("productos"))
            .andExpect(MockMvcResultMatchers.model().attributeExists("citas"));
		Mockito.verify(this.productoService).findAllProductosByLoguedVeterinario();
		Mockito.verify(this.citaService).findCitasByLoggedVeterinarioAndFilters(ArgumentMatchers.eq(TipoEstadoCita.ACEPTADA), ArgumentMatchers.any(), ArgumentMatchers.any());
	}

	//2 - Métodos listar recetas
	@WithMockUser(value = "spring")
	@Test
	void testListRecetasVeterinarioGET() throws Exception {

		this.mockMvc.perform(MockMvcRequestBuilders.get("/veterinario/receta/list"))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("receta/listRecetaForm"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("recetas"));
		Mockito.verify(this.recetaService).findRecetasByLoggedVeterinario();
	}

	@WithMockUser(value = "spring")
	@Test
	void testListRecetasGanaderoGET() throws Exception {

		this.mockMvc.perform(MockMvcRequestBuilders.get("/ganadero/receta/list"))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("receta/listRecetaForm"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("recetas"));
		Mockito.verify(this.recetaService).findRecetasByLoggedGanadero();
	}

	//3 - Método mostrar receta
	
	@WithMockUser(value = "spring")
	@Test
	void testShowRecetaWhenRecetaIsPresent() throws Exception{
		
		//Mockeamos que encuentre la receta
		Optional<Receta> recetaOptional = Optional.of(recetaForTesting());
		BDDMockito.given(this.recetaService.findRecetaById(TEST_RECETA_ID)).willReturn(recetaOptional);
		
		this.mockMvc.perform(MockMvcRequestBuilders.get("/receta/{recetaId}/show",TEST_RECETA_ID))
		    .andExpect(MockMvcResultMatchers.status().isOk())
		    .andExpect(MockMvcResultMatchers.view().name("receta/showRecetaForm"))
		    .andExpect(MockMvcResultMatchers.model().attributeExists("receta"))
		    .andExpect(MockMvcResultMatchers.model().attributeExists("lineasReceta"))
		    .andExpect(MockMvcResultMatchers.model().attributeExists("precioTotalReceta"));
	    Mockito.verify(this.recetaService).findRecetaById(TEST_RECETA_ID);
	    Mockito.verify(this.lineaRecetaService).findLineasRecetaByRecetaId(TEST_RECETA_ID);
	    Mockito.verify(this.lineaRecetaService).getPrecioTotalRecetaByRecetaId(TEST_RECETA_ID);
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testShowRecetaDontFindRecetaBeingVeterinario() throws Exception {
		
		//Mockeamos para que no encuentre la receta
		Optional<Receta> recetaOptional = Optional.empty();
		BDDMockito.given(this.recetaService.findRecetaById(TEST_RECETA_ID)).willReturn(recetaOptional);
		
		//Mockeamos para que exista un veterinario logeado
		BDDMockito.given(this.vetService.findVeterinarioByLogedUser()).willReturn(new Veterinario());
		
		this.mockMvc.perform(MockMvcRequestBuilders.get("/receta/{recetaId}/show",TEST_RECETA_ID))
	        .andExpect(MockMvcResultMatchers.status().isOk())
	        .andExpect(MockMvcResultMatchers.view().name("receta/listRecetaForm"))
	        .andExpect(MockMvcResultMatchers.model().attributeExists("message"))
	        .andExpect(MockMvcResultMatchers.model().attributeExists("recetas"));
        Mockito.verify(this.recetaService).findRecetaById(TEST_RECETA_ID);
        Mockito.verify(this.vetService).findVeterinarioByLogedUser();
		Mockito.verify(this.recetaService).findRecetasByLoggedVeterinario();
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testShowRecetaDontFindRecetaBeingGanadero() throws Exception {
		
		//Mockeamos para que no encuentre la receta
		Optional<Receta> recetaOptional = Optional.empty();
		BDDMockito.given(this.recetaService.findRecetaById(TEST_RECETA_ID)).willReturn(recetaOptional);
		
		//Mockeamos para que no exista un veterinario logeado, por lo que debe existir un ganadero
		BDDMockito.given(this.vetService.findVeterinarioByLogedUser()).willReturn(null);
		
		this.mockMvc.perform(MockMvcRequestBuilders.get("/receta/{recetaId}/show",TEST_RECETA_ID))
	        .andExpect(MockMvcResultMatchers.status().isOk())
	        .andExpect(MockMvcResultMatchers.view().name("receta/listRecetaForm"))
	        .andExpect(MockMvcResultMatchers.model().attributeExists("message"))
	        .andExpect(MockMvcResultMatchers.model().attributeExists("recetas"));
        Mockito.verify(this.recetaService).findRecetaById(TEST_RECETA_ID);
        Mockito.verify(this.vetService).findVeterinarioByLogedUser();
		Mockito.verify(this.recetaService).findRecetasByLoggedGanadero();
	}
	
	
	@DisplayName("Custom RecetaFormValidator tests")
	@Nested
	class RecetaFormCustomValidatorTests{
		
		@BeforeEach
		void resetMockUps() {
			Mockito.reset(recetaService, lineaRecetaService, citaService, productoService, vetService);
		}
		
		/*
		 * Restricción: La suma de las cantidades de la receta debe ser mayor que 0;
		 */
		
		@WithMockUser(value = "spring")
		@Test
		public void shouldNotCreateRecetaWhenSumaListaCantidadIgualA0Test() throws Exception{
			
			mockMvc.perform(MockMvcRequestBuilders.post("/veterinario/receta/new")
		        .with(SecurityMockMvcRequestPostProcessors.csrf())
		        .param("descripcion", "Dar a las cabras durante 1 semana diariamente")
		        .param("citaId", "9")
		        .param("productoId", "")
		        .param("cantidad", ""))
	            .andExpect(MockMvcResultMatchers.status().isOk())
	            .andExpect(model().attributeHasErrors("recetaForm"))
			    .andExpect(model().attributeErrorCount("recetaForm", 1))
			    .andExpect(model().attributeHasFieldErrorCode("recetaForm","cantidad","receta.cantidadTotal.igualA0"));
		}
	}
}
