
package org.springframework.samples.petclinic.web.e2e;

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
public class FacturaControllerE2ETests {

	private static final int	TEST_CONTRATO_ID	= 1;

	@Autowired
	private MockMvc				mockMvc;


	// 1. --- Métodos crear una factura

	@WithMockUser(username = "veterinario1", authorities = {
		"veterinario"
	})
	@Test
	void testPositivoCrearFacturaGET() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/contrato/{contratoId}/factura/new", FacturaControllerE2ETests.TEST_CONTRATO_ID)).andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("factura/createFactura")).andExpect(MockMvcResultMatchers.model().attributeExists("productos")).andExpect(MockMvcResultMatchers.model().attributeExists("recetas"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("factura"));
	}

	@WithMockUser(username = "veterinario6", authorities = {
		"veterinario"
	})
	@Test
	void testCrearFacturaNoProductosNiRecetasConExistenciasForFacturaGET() throws Exception {
		//El veterinario3 no tiene productos ni el contrato 6 recetas en el data.sql, por eso este tests se da correctamente
		this.mockMvc.perform(MockMvcRequestBuilders.get("/contrato/{contratoId}/factura/new", 6)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("factura/listarFacturas"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("facturas")).andExpect(MockMvcResultMatchers.model().attributeExists("contratoId")).andExpect(MockMvcResultMatchers.model().attributeExists("message"));
	}

	@WithMockUser(username = "veterinario1", authorities = {
		"veterinario"
	})
	@Test
	void testPositivoFacturaPOST() throws Exception {

		this.mockMvc
			.perform(MockMvcRequestBuilders.post("/contrato/{contratoId}/factura/new", FacturaControllerE2ETests.TEST_CONTRATO_ID).with(SecurityMockMvcRequestPostProcessors.csrf()).param("contrato.id", "1").param("esPagado", "false")
				.param("fechaEmision", "2020/10/01 10:00").param("recetas.id", "1").param("productoId", "1", "2").param("cantidad", "3", "2"))
			.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("factura/listarFacturas"));

	}
	/*
	 * @WithMockUser(username = "veterinario1", authorities = {
	 * "veterinario"
	 * })
	 *
	 * @Test
	 * void testNegativoFacturaPOST() throws Exception {
	 *
	 * this.mockMvc
	 * .perform(MockMvcRequestBuilders.post("/contrato/{contratoId}/factura/new", FacturaControllerE2ETests.TEST_CONTRATO_ID)
	 * .with(SecurityMockMvcRequestPostProcessors.csrf())
	 * .param("contrato.id", "1")
	 * .param("esPagado", "false")
	 * .param("fechaEmision", "2020/10/01 10:00")
	 * .param("recetas.id", "")
	 * .param("productoId", "").param("cantidad", ""))
	 * .andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("factura/createFactura")).andExpect(MockMvcResultMatchers.model().attributeExists("facturaForm"))
	 * .andExpect(MockMvcResultMatchers.model().attributeExists("productos")).andExpect(MockMvcResultMatchers.model().attributeExists("recetas"));
	 *
	 * }
	 **/

}
