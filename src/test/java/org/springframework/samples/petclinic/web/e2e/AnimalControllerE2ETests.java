
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
public class AnimalControllerE2ETests {

	private static final boolean	TEST_ES_ARCHIVADO			= false;

	private static final int		TEST_ANIMAL_ID				= 8;

	private static final int		TEST_EXPLOTACIONGANADERA_ID	= 1;

	private static final int		TEST_GANADO_ID				= 1;

	private static final int		TEST_ANIMAL_NO_DE_LOTE_ID	= 21;

	@Autowired
	MockMvc							mockMvc;


	@WithMockUser(value = "veterinario1", authorities = {
		"veterinario"
	})
	@Test
	void testArchivarAnimalNoDeLoteGET() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/explotacion-ganadera/{expGanaderaId}/ganado/{ganadoId}/animal/{animalId}/delete", AnimalControllerE2ETests.TEST_EXPLOTACIONGANADERA_ID, AnimalControllerE2ETests.TEST_GANADO_ID,
			AnimalControllerE2ETests.TEST_ANIMAL_NO_DE_LOTE_ID)).andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/explotacion-ganadera/1/ganado/1/animal/esArchivado/true/list"));
	}

	@WithMockUser(value = "veterinario1", authorities = {
		"veterinario"
	})
	@Test
	void testArchivarAnimalDeLoteGET() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/explotacion-ganadera/{expGanaderaId}/ganado/{ganadoId}/animal/{animalId}/delete", AnimalControllerE2ETests.TEST_EXPLOTACIONGANADERA_ID, AnimalControllerE2ETests.TEST_GANADO_ID,
			AnimalControllerE2ETests.TEST_ANIMAL_ID)).andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/explotacion-ganadera/1/ganado/1/animal/lote/2/show"));

	}

	@WithMockUser(username = "ganadero1", authorities = {
		"ganadero"
	})
	@Test
	void testListarAnimalesGET() throws Exception {
		this.mockMvc
			.perform(MockMvcRequestBuilders.get("/explotacion-ganadera/{expGanaderaId}/ganado/{ganadoId}/animal/esArchivado/{esArchivado}/list", AnimalControllerE2ETests.TEST_EXPLOTACIONGANADERA_ID, AnimalControllerE2ETests.TEST_GANADO_ID,
				AnimalControllerE2ETests.TEST_ES_ARCHIVADO))
			.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("animal/listaAnimales")).andExpect(MockMvcResultMatchers.model().attributeExists("lote"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("animales")).andExpect(MockMvcResultMatchers.model().attributeExists("expId")).andExpect(MockMvcResultMatchers.model().attributeExists("tipoGanado"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("esArchivado"));

	}
}
