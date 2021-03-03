
package org.springframework.samples.petclinic.web;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
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
import org.springframework.samples.petclinic.forms.FacturaForm;
import org.springframework.samples.petclinic.model.Contrato;
import org.springframework.samples.petclinic.model.Factura;
import org.springframework.samples.petclinic.model.LineaFactura;
import org.springframework.samples.petclinic.model.Producto;
import org.springframework.samples.petclinic.model.Receta;
import org.springframework.samples.petclinic.service.CitaNonSolitaryServiceTests;
import org.springframework.samples.petclinic.service.CitaServiceTests;
import org.springframework.samples.petclinic.service.ContratoService;
import org.springframework.samples.petclinic.service.FacturaService;
import org.springframework.samples.petclinic.service.FacturaServiceTests;
import org.springframework.samples.petclinic.service.GanaderoService;
import org.springframework.samples.petclinic.service.LineaFacturaService;
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
	controllers = FacturaController.class,
	/* Excluir los componentes de seguridad al instanciar el controlador */
	excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
/*
 * PD: Si cargamos los componentes de seguridad, tal como esta implementado (Con roles y usuarios),
 * tambien tendríamos que cargar el contexto de la aplicacion completo llegando hasta la capad de datos.
 * Si hacemos esto, estaríamos hablando mas bien de un test de integración, no de test unitarios
 */
public class FacturaControllerTests {

	private static final int	TEST_CONTRATO_ID	= 1;
	@Autowired
	private MockMvc				mockMvc;

	@MockBean
	private FacturaService		facturaService;

	@MockBean
	private LineaFacturaService	lineaFacturaService;

	@MockBean
	private ContratoService		contratoService;

	@MockBean
	private ProductoService		productoService;

	@MockBean
	private RecetaService		recetaService;

	@MockBean
	private VetService			vetService;

	@MockBean
	private GanaderoService		ganaderoService;


	//Datos de prueba
	//Objeto Contrato que se usa para las pruebas
	public Contrato contratoForTesting() throws ParseException {
		Contrato contrato = CitaServiceTests.contrato1ParaPruebas();
		contrato.setId(1);
		contrato.getExplotacionGanadera().getGanadero().setId(1);
		return contrato;
	}

	//Objeto que se usa para las pruebas
	public FacturaForm facturaFormForTesting() throws ParseException {
		FacturaForm facturaForm = new FacturaForm();
		List<Integer> lista = new ArrayList<>();
		lista.add(1);
		facturaForm.setEsPagado(false);
		facturaForm.setFechaEmision(CitaNonSolitaryServiceTests.newDateFormatted("2020/06/01 10:00"));
		facturaForm.setContrato(this.contratoForTesting());
		facturaForm.setProductoId(lista);
		facturaForm.setCantidad(lista);
		List<Receta> recetas = new ArrayList<Receta>();
		recetas.add(FacturaServiceTests.receta1ParaPruebas());
		recetas.add(FacturaServiceTests.receta2ParaPruebas());
		facturaForm.setRecetas(recetas);
		return facturaForm;
	}

	//Objeto que se usa para las pruebas
	public Factura facturaForTesting() throws ParseException {
		Factura factura = new Factura();
		factura.setEsPagado(false);
		factura.setFechaEmision(CitaNonSolitaryServiceTests.newDateFormatted("2020/06/01 10:00"));
		factura.setContrato(this.contratoForTesting());
		List<Receta> recetas = this.recetasForTesting();
		factura.setRecetas(recetas);
		return factura;
	}

	//Objeto que se usa para las pruebas
	public List<Receta> recetasForTesting() throws ParseException {
		List<Receta> recetas = new ArrayList<Receta>();
		recetas.add(FacturaServiceTests.receta1ParaPruebas());
		recetas.add(FacturaServiceTests.receta2ParaPruebas());
		return recetas;
	}

	//Objeto LineaFactura que se usa para las pruebas
	public LineaFactura lineaFacturaForTesting() throws ParseException {
		LineaFactura lineaFactura = new LineaFactura();
		lineaFactura.setCantidad(4);
		lineaFactura.setPrecioVenta(4.5);
		lineaFactura.setProducto(new Producto());
		lineaFactura.setFactura(this.facturaForTesting());
		return lineaFactura;
	}

	@BeforeEach
	void setup() throws ParseException {

		Producto producto = new Producto();
		Iterable<Producto> productos = Collections.singleton(producto);
		BDDMockito.given(this.productoService.findAllProductosByLoguedVeterinario()).willReturn(productos);

		BDDMockito.given(this.facturaService.reconstruct(this.facturaFormForTesting(), this.contratoForTesting().getId())).willReturn(this.facturaForTesting());

		List<LineaFactura> lineasFactura = new ArrayList<>();
		lineasFactura.add(this.lineaFacturaForTesting());
		BDDMockito.given(this.lineaFacturaService.reconstruct(this.facturaFormForTesting(), this.facturaForTesting())).willReturn(lineasFactura);

		List<Factura> facturas = new ArrayList<>();
		facturas.add(this.facturaForTesting());
		BDDMockito.given(this.facturaService.findFacturasByContratoId(this.contratoForTesting().getId())).willReturn(facturas);
		BDDMockito.given(this.facturaService.findFacturasByGanaderoIdBeingGanadero(this.contratoForTesting().getExplotacionGanadera().getGanadero().getId())).willReturn(facturas);

	}

	@WithMockUser(value = "spring")
	@Test
	void testPositivoCrearFacturaGET() throws Exception {
		//Mocks para el test en concreto

		List<Contrato> contratos = new ArrayList<>();
		contratos.add(this.contratoForTesting());

		List<Receta> recetas = this.recetasForTesting();

		List<Producto> productos = new ArrayList<>();
		productos.add(new Producto());

		BDDMockito.given(this.recetaService.findRecetasByContratoIdAndNoEsFacturado(FacturaControllerTests.TEST_CONTRATO_ID)).willReturn(recetas);
		BDDMockito.given(this.productoService.findAllProductosConExistenciasByVeterinarioIdNoNecesitanReceta()).willReturn(productos);

		this.mockMvc.perform(
			MockMvcRequestBuilders.get("/contrato/{contratoId}/factura/new", FacturaControllerTests.TEST_CONTRATO_ID))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("factura/createFactura"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("productos"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("recetas"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("factura"));
		Mockito.verify(this.recetaService).findRecetasByContratoIdAndNoEsFacturado(ArgumentMatchers.eq(FacturaControllerTests.TEST_CONTRATO_ID));
		Mockito.verify(this.productoService).findAllProductosConExistenciasByVeterinarioIdNoNecesitanReceta();

	}

	@WithMockUser(value = "spring")
	@Test
	void testPositivoCrearFacturaPOST() throws Exception {
		//Mocks para el test en concreto

		List<Contrato> contratos = new ArrayList<>();
		contratos.add(this.contratoForTesting());

		List<Producto> productos = new ArrayList<>();
		productos.add(new Producto());

		BDDMockito.given(this.productoService.findAllProductosConExistenciasByVeterinarioIdNoNecesitanReceta()).willReturn(productos);

		this.mockMvc
			.perform(MockMvcRequestBuilders.post("/contrato/{contratoId}/factura/new", FacturaControllerTests.TEST_CONTRATO_ID).with(SecurityMockMvcRequestPostProcessors.csrf()).param("cantidad", this.facturaFormForTesting().getCantidad().toString())
				.param("contrato", this.facturaFormForTesting().getContrato().toString()).param("esPagado", this.facturaFormForTesting().getEsPagado().toString()).param("productoId", this.facturaFormForTesting().getProductoId().toString())
				.param("fechaEmision", this.facturaFormForTesting().getFechaEmision().toString()).param("recetas", this.facturaFormForTesting().getRecetas().toString()))
			.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("message")).andExpect(MockMvcResultMatchers.view().name("factura/createFactura"));

		Mockito.verify(this.recetaService).findRecetasByContratoIdAndNoEsFacturado(ArgumentMatchers.eq(FacturaControllerTests.TEST_CONTRATO_ID));
		Mockito.verify(this.productoService).findAllProductosConExistenciasByVeterinarioIdNoNecesitanReceta();

	}

	@WithMockUser(value = "spring")
	@Test
	void testCrearFacturaNoProductosNiRecetaForFacturaGET() throws Exception {

		BDDMockito.given(this.recetaService.findRecetasByContratoIdAndNoEsFacturado(FacturaControllerTests.TEST_CONTRATO_ID)).willReturn(new ArrayList<>());
		BDDMockito.given(this.productoService.findAllProductosConExistenciasByVeterinarioIdNoNecesitanReceta()).willReturn(new ArrayList<>());

		this.mockMvc
			.perform(MockMvcRequestBuilders.post("/contrato/{contratoId}/factura/new", FacturaControllerTests.TEST_CONTRATO_ID).with(SecurityMockMvcRequestPostProcessors.csrf()).param("cantidad", this.facturaFormForTesting().getCantidad().toString())
				.param("contrato", this.facturaFormForTesting().getContrato().toString()).param("esPagado", this.facturaFormForTesting().getEsPagado().toString()).param("productoId", this.facturaFormForTesting().getProductoId().toString())
				.param("fechaEmision", this.facturaFormForTesting().getFechaEmision().toString()).param("recetas", this.facturaFormForTesting().getRecetas().toString()))
			.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("message")).andExpect(MockMvcResultMatchers.view().name("factura/createFactura"));
		Mockito.verify(this.recetaService).findRecetasByContratoIdAndNoEsFacturado(ArgumentMatchers.eq(FacturaControllerTests.TEST_CONTRATO_ID));
		Mockito.verify(this.productoService).findAllProductosConExistenciasByVeterinarioIdNoNecesitanReceta();

	}

}
