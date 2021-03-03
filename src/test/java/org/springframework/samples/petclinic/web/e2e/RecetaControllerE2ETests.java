
package org.springframework.samples.petclinic.web.e2e;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
public class RecetaControllerE2ETests {

	private static final int	TEST_RECETA_ID		= 1;

	private static final int	TEST_RECETA_ID_2	= 999;

	@Autowired
	private MockMvc				mockMvc;


	// 1. --- Métodos crear una receta

	@WithMockUser(username = "veterinario1", authorities = {
		"veterinario"
	})
	@Test
	void testPositivoCrearRecetaGET() throws Exception {
		//Si este método no funciona será porque el veterinario no tiene citas para realizar una receta.
		//Ya que el data.sql tiene registrada una cita el dia 2020-05-26 y por esa cita, referente al
		//veterinario1 se puede hacer la petición. En cambio si hace 1 mes o más de la cita, no
		//se podrá realizar una receta. Esto ocurrirá cuando sea el dia 2020-06-27
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
	}

	@WithMockUser(username = "veterinario6", authorities = {
		"veterinario"
	})
	@Test
	void testCrearRecetaNoProductosConExistenciasForRecetaGET() throws Exception {
		//El veterinario6 no tiene productos creados en el data.sql, por eso este tests se da correctamente
		this.mockMvc.perform(MockMvcRequestBuilders.get("/veterinario/receta/new"))
		    .andExpect(MockMvcResultMatchers.status().isOk())
		    .andExpect(MockMvcResultMatchers.view().name("receta/listRecetaForm"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("recetas"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("message"));
	}

	@WithMockUser(username = "veterinario2", authorities = {
		"veterinario"
	})
	@Test
	void testCrearRecetaNoCitasDisponiblesParaLaRecetaGET() throws Exception {
		//El veterinario2 tiene productos, pero no tiene citas de hace menos de un mes. Por eso
		//este caso se da correctamente.
		this.mockMvc.perform(MockMvcRequestBuilders.get("/veterinario/receta/new"))
		    .andExpect(MockMvcResultMatchers.status().isOk())
		    .andExpect(MockMvcResultMatchers.view().name("receta/listRecetaForm"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("recetas"));
	}

	@WithMockUser(username = "veterinario1", authorities = {
		"veterinario"
	})
	@Test
	void testPositivoRecetaPOST() throws Exception {

		this.mockMvc.perform(MockMvcRequestBuilders.post("/veterinario/receta/new").with(SecurityMockMvcRequestPostProcessors.csrf())
			.param("descripcion", "Dar a las cabras durante 1 semana diariamente")
			.param("citaId", "9")
			.param("productoId", "1", "2")
			.param("cantidad", "3", "2"))
		    .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
		    .andExpect(MockMvcResultMatchers.view().name("redirect:/veterinario/receta/list"));

	}

	@WithMockUser(username = "veterinario1", authorities = {
		"veterinario"
	})
	@Test
	void testNegativoRecetaPOST() throws Exception {

		this.mockMvc.perform(MockMvcRequestBuilders.post("/veterinario/receta/new").with(SecurityMockMvcRequestPostProcessors.csrf())
			.param("descripcion", "")
			.param("citaId", "9")
			.param("productoId", "1", "2")
			.param("cantidad", "3", "2"))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("receta/createRecetaForm"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("recetaForm"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("productos"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("citas"));

	}

	//2 - Métodos listar recetas
	@WithMockUser(username = "veterinario1", authorities = {
		"veterinario"
	})
	@Test
	void testListRecetasVeterinarioGET() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/veterinario/receta/list"))
		    .andExpect(MockMvcResultMatchers.status().isOk())
		    .andExpect(MockMvcResultMatchers.view().name("receta/listRecetaForm"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("recetas"));
	}

	@WithMockUser(username = "ganadero1", authorities = {
		"ganadero"
	})
	@Test
	void testListRecetasGanaderoGET() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/ganadero/receta/list"))
		    .andExpect(MockMvcResultMatchers.status().isOk())
		    .andExpect(MockMvcResultMatchers.view().name("receta/listRecetaForm"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("recetas"));
	}

	//3 - Método mostrar receta

	@WithMockUser(username = "veterinario1", authorities = {
		"veterinario"
	})
	@Test
	void testShowRecetaWhenRecetaIsPresent() throws Exception {

		this.mockMvc.perform(MockMvcRequestBuilders.get("/receta/{recetaId}/show", RecetaControllerE2ETests.TEST_RECETA_ID))
		    .andExpect(MockMvcResultMatchers.status().isOk())
		    .andExpect(MockMvcResultMatchers.view().name("receta/showRecetaForm"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("receta"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("lineasReceta"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("precioTotalReceta"));
	}

	@WithMockUser(username = "veterinario1", authorities = {
		"veterinario"
	})
	@Test
	void testShowRecetaDontFindRecetaBeingVeterinario() throws Exception {

		this.mockMvc.perform(MockMvcRequestBuilders.get("/receta/{recetaId}/show", RecetaControllerE2ETests.TEST_RECETA_ID_2))
		    .andExpect(MockMvcResultMatchers.status().isOk())
		    .andExpect(MockMvcResultMatchers.view().name("receta/listRecetaForm"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("message"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("recetas"));
	}

	@WithMockUser(username = "ganadero1", authorities = {
		"ganadero"
	})
	@Test
	void testShowRecetaDontFindRecetaBeingGanadero() throws Exception {

		this.mockMvc.perform(MockMvcRequestBuilders.get("/receta/{recetaId}/show", RecetaControllerE2ETests.TEST_RECETA_ID_2))
		    .andExpect(MockMvcResultMatchers.status().isOk())
		    .andExpect(MockMvcResultMatchers.view().name("receta/listRecetaForm"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("message"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("recetas"));
	}

}
