
package org.springframework.samples.petclinic.web.e2e;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Transactional
//@TestPropertySource(locations = "classpath:application-mysql.properties")
public class ExplotacionGanaderaControllerE2ETests {

	private static final int	TEST_EXPLOTACIONGANADERA_ID_POS	= 8;

	private static final int	TEST_EXPLOTACIONGANADERA_ID_NEG	= 1;

	@Autowired
	private MockMvc				mockMvc;


	@WithMockUser(value = "ganadero5", authorities = {
		"ganadero"
	})
	@Test
	void testPositivosalvarExplotacionGanaderaArchivadaGET() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/ganadero/explotacion-ganadera/{expId}/archivarExpGanadera", ExplotacionGanaderaControllerE2ETests.TEST_EXPLOTACIONGANADERA_ID_POS)).andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("explotacionGanadera/listaExplotacionesGanadero"));
	}

	@WithMockUser(value = "ganadero1", authorities = {
		"ganadero"
	})
	@Test
	void testNegativosalvarExplotacionGanaderaArchivadaGET() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/ganadero/explotacion-ganadera/{expId}/archivarExpGanadera", ExplotacionGanaderaControllerE2ETests.TEST_EXPLOTACIONGANADERA_ID_NEG)).andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("explotacionGanadera/listaExplotacionesGanadero"));
	}

	// 2. --- Métodos listar las explotaciones de un ganadero

	@WithMockUser(username = "ganadero1", authorities = {
		"ganadero"
	})
	@Test
	void testListarExplotacionesGanaderoGET() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/ganadero/explotacion-ganadera/list")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("explotacionGanadera/listaExplotacionesGanadero"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("expGanaderas")).andExpect(MockMvcResultMatchers.model().attributeExists("todosTiposG"));
		;
	}

	// 3. --- Métodos listar las explotaciones de un veterinario

	@WithMockUser(username = "veterinario1", authorities = {
		"veterinario"
	})
	@Test
	void testListarCitasVeterinario() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/veterinario/explotacion-ganadera/list")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("explotacionGanadera/listaExplotacionesVeterinario"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("contratos")).andExpect(MockMvcResultMatchers.model().attributeExists("todosTiposG"));
		;
	}
}
