
package org.springframework.samples.petclinic.web.e2e;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
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
public class ProductoControllerE2ETests {

	private static final int	TEST_VET_ID			= 1;

	private static final int	TEST_PRODUCTO_ID	= 1;

	@Autowired
	private MockMvc				mockMvc;


	@WithMockUser(value = "veterinario1", authorities = {
		"veterinario"
	})
	@Test
	void testInitCreationForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/producto/new")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("producto/newProducto"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("producto"));
	}
	@WithMockUser(value = "veterinario1", authorities = {
		"veterinario"
	})
	@Test
	void testCreateProducto() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/producto/new")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("producto/newProducto"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("producto")).andExpect(MockMvcResultMatchers.model().attribute("producto", Matchers.hasProperty("name", Matchers.blankOrNullString())))
			.andExpect(MockMvcResultMatchers.model().attribute("producto", Matchers.hasProperty("cantidad", Matchers.blankOrNullString())))
			.andExpect(MockMvcResultMatchers.model().attribute("producto", Matchers.hasProperty("precio", Matchers.blankOrNullString())))
			.andExpect(MockMvcResultMatchers.model().attribute("producto", Matchers.hasProperty("necesitaReceta", Matchers.blankOrNullString())));
	}

	@WithMockUser(value = "veterinario1", authorities = {
		"veterinario"
	})
	@Test
	void testPositivoCrearProductoPOST() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/producto/new").with(SecurityMockMvcRequestPostProcessors.csrf()).param("name", "Medicamento bien perron").param("cantidad", "4").param("precio", "4.12").param("necesitaReceta", "1"))
			.andExpect(MockMvcResultMatchers.view().name("producto/listarProductos")).andExpect(MockMvcResultMatchers.status().isOk());
	}

	@WithMockUser(value = "veterinario1", authorities = {
		"veterinario"
	})
	@Test
	void testNegativoCrearProductoPOST() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/producto/new").with(SecurityMockMvcRequestPostProcessors.csrf()).param("name", "Medicamento bien perron").param("cantidad", "-4").param("precio", "4.12").param("necesitaReceta", "1"))
			.andExpect(MockMvcResultMatchers.view().name("producto/newProducto")).andExpect(MockMvcResultMatchers.status().isOk());
	}

	@WithMockUser(value = "veterinario1", authorities = {
		"veterinario"
	})
	@Test
	void testListarProducto() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/producto/list")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("producto/listarProductos"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("productos"));
	}

	@WithMockUser(value = "veterinario1", authorities = {
		"veterinario"
	})
	@Test
	void testEditProducto() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/producto/{productoId}/edit", ProductoControllerE2ETests.TEST_PRODUCTO_ID)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("producto/editProducto"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("producto"));
	}

	@WithMockUser(value = "veterinario1", authorities = {
		"veterinario"
	})
	@Test
	void testEditProductoPositivoPOST() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/producto/{productoId}/edit", ProductoControllerE2ETests.TEST_PRODUCTO_ID).with(SecurityMockMvcRequestPostProcessors.csrf()).param("name", "Medicamento bien perron").param("cantidad", "4")
			.param("precio", "4.12").param("necesitaReceta", "1")).andExpect(MockMvcResultMatchers.view().name("producto/listarProductos")).andExpect(MockMvcResultMatchers.status().isOk());
	}

	@WithMockUser(value = "veterinario1", authorities = {
		"veterinario"
	})
	@Test
	void testEditProductoNegativoPOST() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/producto/{productoId}/edit", ProductoControllerE2ETests.TEST_PRODUCTO_ID).with(SecurityMockMvcRequestPostProcessors.csrf()).param("name", "Medicamento bien perron").param("cantidad", "4")
			.param("precio", "-4.12").param("necesitaReceta", "1")).andExpect(MockMvcResultMatchers.view().name("producto/editProducto")).andExpect(MockMvcResultMatchers.status().isOk());
	}
	@WithMockUser(value = "veterinario2", authorities = {
		"veterinario"
	})
	@Test
	void testDeleteProductoPositivo() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/producto/{productoId}/delete", 6)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("producto/listarProductos"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("message")).andExpect(MockMvcResultMatchers.model().attribute("message", Matchers.equalTo("Producto borrado con éxito.")));
	}
	@WithMockUser(value = "veterinario1", authorities = {
		"veterinario"
	})
	@Test
	void testDeleteProductoNegativo() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/producto/{productoId}/delete", 2)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("producto/listarProductos"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("message"))
			.andExpect(MockMvcResultMatchers.model().attribute("message", Matchers.equalTo("El producto no puede ser borrado, porque existe una receta o una factura realizada con este producto")));
	}

}
