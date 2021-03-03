
package org.springframework.samples.petclinic.web;

import java.util.ArrayList;
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
import org.springframework.samples.petclinic.model.Producto;
import org.springframework.samples.petclinic.model.TiposGanado;
import org.springframework.samples.petclinic.model.Veterinario;
import org.springframework.samples.petclinic.service.LineaFacturaService;
import org.springframework.samples.petclinic.service.LineaRecetaService;
import org.springframework.samples.petclinic.service.ProductoService;
import org.springframework.samples.petclinic.service.ProductoServiceTests;
import org.springframework.samples.petclinic.service.VetService;
import org.springframework.samples.petclinic.service.VeterinarioServiceTests;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@WebMvcTest(
	/* Controlador a instanciar para probarlo */
	controllers = ProductoController.class,
	/* Excluir los componentes de seguridad al instanciar el controlador */
	excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)

public class ProductoControllerTests {

	private static final int	TEST_VET_ID			= 1;

	private static final int	TEST_PRODUCTO_ID	= 1;

	@Autowired
	private MockMvc				mockMvc;

	@MockBean
	private ProductoService		productoService;

	@MockBean
	private VetService			vetService;

	@MockBean
	private LineaRecetaService	lineaRecetaService;

	@MockBean
	private LineaFacturaService	lineaFacturaService;


	@BeforeEach
	void setup() {
		List<TiposGanado> tiposGanado2 = new ArrayList<TiposGanado>();
		TiposGanado p = new TiposGanado();
		p.setTipoGanado("porcino");
		p.setTipoGanado("caprino");
		tiposGanado2.add(p);
		Veterinario vet2 = VeterinarioServiceTests.generateVeterinario("Jotaro", "kujo", "622222223", "jojo2@gmail.com", "Sevilla", "Los palacios", "12345678B", tiposGanado2, true, "hola2", "hola2");

		Producto prodPos2 = ProductoServiceTests.generateProducto("paracetamon", 123, 123.21, true);
		Producto prodPos3 = ProductoServiceTests.generateProducto("medicamenton", 12, 12.12, false);

		prodPos2.setVeterinario(vet2);
		prodPos3.setVeterinario(vet2);

		prodPos2.setId(1);

		BDDMockito.given(this.vetService.findVeterinarioByLogedUser()).willReturn(vet2);

		BDDMockito.given(this.productoService.findProductoById(1)).willReturn(Optional.of(prodPos2));

	}

	@WithMockUser(value = "spring")
	@Test
	void testInitCreationForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/producto/new")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("producto/newProducto"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("producto"));
	}
	@WithMockUser(value = "spring")
	@Test
	void testCreateProducto() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/producto/new")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("producto/newProducto"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("producto")).andExpect(MockMvcResultMatchers.model().attribute("producto", Matchers.hasProperty("name", Matchers.blankOrNullString())))
			.andExpect(MockMvcResultMatchers.model().attribute("producto", Matchers.hasProperty("cantidad", Matchers.blankOrNullString())))
			.andExpect(MockMvcResultMatchers.model().attribute("producto", Matchers.hasProperty("precio", Matchers.blankOrNullString())))
			.andExpect(MockMvcResultMatchers.model().attribute("producto", Matchers.hasProperty("necesitaReceta", Matchers.blankOrNullString())));
	}

	@WithMockUser(value = "spring")
	@Test
	void testPositivoCrearProductoPOST() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/producto/new").with(SecurityMockMvcRequestPostProcessors.csrf()).param("name", "Medicamento bien perron").param("cantidad", "4").param("precio", "4.12").param("necesitaReceta", "1"))
			.andExpect(MockMvcResultMatchers.view().name("producto/listarProductos")).andExpect(MockMvcResultMatchers.status().isOk());
	}

	@WithMockUser(value = "spring")
	@Test
	void testNegativoCrearProductoPOST() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/producto/new").with(SecurityMockMvcRequestPostProcessors.csrf()).param("name", "Medicamento bien perron").param("cantidad", "-4").param("precio", "4.12").param("necesitaReceta", "1"))
			.andExpect(MockMvcResultMatchers.view().name("producto/newProducto")).andExpect(MockMvcResultMatchers.status().isOk());
	}

	@WithMockUser(value = "spring")
	@Test
	void testListarProducto() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/producto/list")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("producto/listarProductos"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("productos"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testEditProducto() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/producto/{productoId}/edit", ProductoControllerTests.TEST_PRODUCTO_ID)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("producto/editProducto"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("producto"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testEditProductoPositivoPOST() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/producto/{productoId}/edit", ProductoControllerTests.TEST_PRODUCTO_ID).with(SecurityMockMvcRequestPostProcessors.csrf()).param("name", "Medicamento bien perron").param("cantidad", "4")
			.param("precio", "4.12").param("necesitaReceta", "1")).andExpect(MockMvcResultMatchers.view().name("producto/listarProductos")).andExpect(MockMvcResultMatchers.status().isOk());
	}

	@WithMockUser(value = "spring")
	@Test
	void testEditProductoNegativoPOST() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/producto/{productoId}/edit", ProductoControllerTests.TEST_PRODUCTO_ID).with(SecurityMockMvcRequestPostProcessors.csrf()).param("name", "Medicamento bien perron").param("cantidad", "4")
			.param("precio", "-4.12").param("necesitaReceta", "1")).andExpect(MockMvcResultMatchers.view().name("producto/editProducto")).andExpect(MockMvcResultMatchers.status().isOk());
	}

	@WithMockUser(value = "spring")
	@Test
	void testDeleteProducto() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/producto/{productoId}/delete", ProductoControllerTests.TEST_PRODUCTO_ID)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("producto/listarProductos"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("message"));
	}


	@DisplayName("Custom ProductoValidator Tests")
	@Nested
	class ProductoCustomValidatorTests {

		@BeforeEach
		void resetMockUps() {
			Mockito.reset(ProductoControllerTests.this.productoService, ProductoControllerTests.this.vetService);
		}

		@WithMockUser(value = "spring")
		@Test
		public void shouldRejectDuplicateNameTest() throws Exception {
			List<TiposGanado> tiposGanado2 = new ArrayList<TiposGanado>();
			TiposGanado p = new TiposGanado();
			p.setTipoGanado("porcino");
			p.setTipoGanado("caprino");
			tiposGanado2.add(p);
			Veterinario vet2 = VeterinarioServiceTests.generateVeterinario("Jotaro", "kujo", "622222223", "jojo2@gmail.com", "Sevilla", "Los palacios", "12345678B", tiposGanado2, true, "hola2", "hola2");

			String duplicateName = "paracetamon";
			Producto prodPos1 = ProductoServiceTests.generateProducto(duplicateName, 123, 123.21, true);

			prodPos1.setVeterinario(vet2);

			Optional<Producto> productoOpt1 = Optional.of(prodPos1);

			Mockito.when(ProductoControllerTests.this.productoService.findProductoByNameYVetId(duplicateName)).thenReturn(productoOpt1);

			MultiValueMap<String, String> productoParams = this.productoToMultiValueMap(prodPos1);

			ProductoControllerTests.this.mockMvc.perform(MockMvcRequestBuilders.post("/producto/new").with(SecurityMockMvcRequestPostProcessors.csrf()).params(productoParams)).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attributeHasErrors("producto")).andExpect(MockMvcResultMatchers.model().attributeErrorCount("producto", 1))
				.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrorCode("producto", "name", "producto.name.repetido"));

		}
		public MultiValueMap<String, String> productoToMultiValueMap(final Producto producto) {
			MultiValueMap<String, String> res = new LinkedMultiValueMap<String, String>();
			res.add("name", producto.getName());
			res.add("cantidad", producto.getCantidad().toString());
			res.add("precio", producto.getPrecio().toString());
			res.add("necesitaReceta", producto.getNecesitaReceta().toString());
			return res;

		}
	}
}
