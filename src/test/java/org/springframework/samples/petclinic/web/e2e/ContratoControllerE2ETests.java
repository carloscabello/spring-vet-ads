
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
public class ContratoControllerE2ETests {

	@Autowired
	private MockMvc				mockMvc;

	private static final int	TEST_CONTRATO_ID		= 1;

	private static final int	TEST_EXP_GANADERA_ID	= 1;


	@WithMockUser(value = "ganadero1", authorities = {
		"ganadero"
	})
	@Test
	void testInitCreationForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/veterinario/{vetId}/contrato", 5)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("contrato/createContratoForm"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("contrato"));
	}

	@WithMockUser(value = "ganadero2", authorities = {
		"ganadero"
	})
	@Test
	void testCrearContratoGET() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/veterinario/{vetId}/contrato", 5))
			/* 1. Comprobamos que se recibe una respuesta HTTP correcta */
			.andExpect(MockMvcResultMatchers.status().isOk())
			/* 2. Comprobamos el nombre de la vista */
			.andExpect(MockMvcResultMatchers.view().name("contrato/createContratoForm"))
			/* 3. Comprobamos que recibimos un modelAttribute Ganadero */
			.andExpect(MockMvcResultMatchers.model().attributeExists("contrato"))
			/* 4. Comprobamos que Ganadero tiene las propiedades esperadas */
			.andExpect(MockMvcResultMatchers.model().attribute("contrato", Matchers.hasProperty("fechaPeticion", Matchers.notNullValue())))
			.andExpect(MockMvcResultMatchers.model().attribute("contrato", Matchers.hasProperty("fechaInicial", Matchers.blankOrNullString())))
			.andExpect(MockMvcResultMatchers.model().attribute("contrato", Matchers.hasProperty("fechaFinal", Matchers.blankOrNullString())))
			.andExpect(MockMvcResultMatchers.model().attribute("contrato", Matchers.hasProperty("ganados", Matchers.blankOrNullString())))
			.andExpect(MockMvcResultMatchers.model().attribute("contrato", Matchers.hasProperty("explotacionGanadera", Matchers.blankOrNullString())))
			.andExpect(MockMvcResultMatchers.model().attribute("contrato", Matchers.hasProperty("veterinario", Matchers.notNullValue())));

	}

	@WithMockUser(value = "ganadero2", authorities = {
		"ganadero"
	})
	@Test
	void testPositivoCrearContratoPOST() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/veterinario/{vetId}/contrato", 4).with(SecurityMockMvcRequestPostProcessors.csrf()).param("fechaInicial", "2020/12/01").param("fechaFinal", "2021/12/01").param("ganados.id", "2")
			.param("explotacionGanadera.id", "4").param("veterinario.id", "4")).andExpect(MockMvcResultMatchers.view().name("redirect:/ganadero/explotacion-ganadera/4/contrato/list")).andExpect(MockMvcResultMatchers.status().is3xxRedirection());

	}

	@WithMockUser(value = "ganadero1", authorities = {
		"ganadero"
	})
	@Test
	void testNegativoCrearContratoPOST() throws Exception {
		this.mockMvc
			.perform(
				MockMvcRequestBuilders.post("/veterinario/{vetId}/contrato", 5).with(SecurityMockMvcRequestPostProcessors.csrf()).param("fechaFinal", "2021/12/01").param("veterinario.id", "5").param("ganados.id", "1").param("explotacionGanadera.id", "1"))
			.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("contrato", "fechaInicial")).andExpect(MockMvcResultMatchers.view().name("contrato/createContratoForm")).andExpect(MockMvcResultMatchers.status().isOk());

	}

	@WithMockUser(value = "veterinario1", authorities = {
		"veterinario"
	})
	@Test
	void testListContratos() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/contrato/list")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("contrato/listarContratos"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("contratos"));
	}

	@WithMockUser(value = "ganadero1", authorities = {
		"ganadero"
	})
	@Test
	void testShowContrato() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/contrato/{contratoId}/show", ContratoControllerE2ETests.TEST_CONTRATO_ID)).andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.model().attributeExists("contratoMostrado")).andExpect(MockMvcResultMatchers.view().name("contrato/showContrato"));
	}

	@WithMockUser(value = "veterinario1", authorities = {
		"veterinario"
	})
	@Test
	void testAcceptContrato() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/contrato/{contratoId}/accept", 4)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("contrato/listarContratos"));

	}

	@WithMockUser(value = "veterinario4", authorities = {
		"veterinario"
	})
	@Test
	void testRefuseContrato() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/contrato/{contratoId}/refuse", 8)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("contrato/listarContratos"));

	}

	@WithMockUser(value = "veterinario4", authorities = {
		"veterinario"
	})
	@Test
	void testConcludeContrato() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/contrato/{contratoId}/conclude", ContratoControllerE2ETests.TEST_CONTRATO_ID)).andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("contrato/listarContratos"));

	}

	@WithMockUser(value = "ganadero1", authorities = {
		"ganadero"
	})
	@Test
	void testlistarContratosExplotacionGanadera() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/ganadero/explotacion-ganadera/{expGanaderaId}/contrato/list", ContratoControllerE2ETests.TEST_EXP_GANADERA_ID)).andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("contrato/listarContratos")).andExpect(MockMvcResultMatchers.model().attributeExists("contratos"));
	}
}
