
package org.springframework.samples.petclinic.web.e2e;

import javax.transaction.Transactional;

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

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Transactional
//@TestPropertySource(locations = "classpath:application-mysql.properties")
public class ArchivarControllerE2ETests {

	private static final int	TEST_EXPLOTACIONGANADERA_ID	= 1;

	private static final int	TEST_GANADO_ID				= 1;

	private static final int	TEST_ANIMAL_ID				= 1;

	private static final int	TEST_ANIMAL_NO_DE_LOTE_ID	= 16;

	private static final int	TEST_LOTE_ID				= 1;

	@Autowired
	private MockMvc				mockMvc;


	@WithMockUser(value = "veterinario1", authorities = {
		"veterinario"
	})
	@Test
	void testArchivarAnimalGET() throws Exception {
		this.mockMvc
			.perform(MockMvcRequestBuilders.get("/explotacion-ganadera/{explotacionGanaderaId}/ganado/{ganadoId}/animal/{animalId}/animal-historico/new", ArchivarControllerE2ETests.TEST_EXPLOTACIONGANADERA_ID, ArchivarControllerE2ETests.TEST_GANADO_ID,
				ArchivarControllerE2ETests.TEST_ANIMAL_ID))
			.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("animal/archivarAnimal")).andExpect(MockMvcResultMatchers.model().attributeExists("animalHistorico"))
			.andExpect(MockMvcResultMatchers.model().attribute("animalHistorico", Matchers.hasProperty("fechaFallecimiento"))).andExpect(MockMvcResultMatchers.model().attribute("animalHistorico", Matchers.hasProperty("fechaVenta")))
			.andExpect(MockMvcResultMatchers.model().attribute("animalHistorico", Matchers.hasProperty("masInfo"))).andExpect(MockMvcResultMatchers.model().attribute("animalHistorico", Matchers.hasProperty("animal")));
	}

	@WithMockUser(value = "veterinario1", authorities = {
		"veterinario"
	})
	@Test
	void testPositivoArchivarAnimalPOST() throws Exception {
		this.mockMvc
			.perform(MockMvcRequestBuilders.post("/explotacion-ganadera/{explotacionGanaderaId}/ganado/{ganadoId}/animal/{animalId}/animal-historico/new", ArchivarControllerE2ETests.TEST_EXPLOTACIONGANADERA_ID, ArchivarControllerE2ETests.TEST_GANADO_ID,
				ArchivarControllerE2ETests.TEST_ANIMAL_NO_DE_LOTE_ID).with(SecurityMockMvcRequestPostProcessors.csrf()).param("fechaFallecimiento", "").param("fechaVenta", "2020/10/01").param("masInfo", "info"))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/explotacion-ganadera/1/ganado/1/animal/esArchivado/false/list"));
	}

	@WithMockUser(value = "veterinario1", authorities = {
		"veterinario"
	})
	@Test
	void testPositivoArchivarAnimalDeLotePOST() throws Exception {
		this.mockMvc
			.perform(MockMvcRequestBuilders.post("/explotacion-ganadera/{explotacionGanaderaId}/ganado/{ganadoId}/animal/{animalId}/animal-historico/new", ArchivarControllerE2ETests.TEST_EXPLOTACIONGANADERA_ID, ArchivarControllerE2ETests.TEST_GANADO_ID,
				ArchivarControllerE2ETests.TEST_ANIMAL_ID).with(SecurityMockMvcRequestPostProcessors.csrf()).param("fechaFallecimiento", "").param("fechaVenta", "2020/10/01").param("masInfo", "info"))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/explotacion-ganadera/1/ganado/1/animal/lote/1/show"));

	}

	@WithMockUser(value = "veterinario1", authorities = {
		"veterinario"
	})
	@Test
	void testNegativoSalvarAnimalHistoricoPOST() throws Exception {
		this.mockMvc
			.perform(MockMvcRequestBuilders.post("/explotacion-ganadera/{explotacionGanaderaId}/ganado/{ganadoId}/animal/{animalId}/animal-historico/new", ArchivarControllerE2ETests.TEST_EXPLOTACIONGANADERA_ID, ArchivarControllerE2ETests.TEST_GANADO_ID,
				ArchivarControllerE2ETests.TEST_ANIMAL_ID).with(SecurityMockMvcRequestPostProcessors.csrf()).param("fechaFallecimiento", "").param("fechaVenta", "").param("masInfo", "info"))
			.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("animalHistorico", "fechaFallecimiento", "fechaVenta")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("animal/archivarAnimal"));
	}

	@WithMockUser(value = "veterinario1", authorities = {
		"veterinario"
	})
	@Test
	void testNegativoSalvarAnimalHistoricoMalFechaFallecimientoPOST() throws Exception {
		this.mockMvc
			.perform(MockMvcRequestBuilders.post("/explotacion-ganadera/{explotacionGanaderaId}/ganado/{ganadoId}/animal/{animalId}/animal-historico/new", ArchivarControllerE2ETests.TEST_EXPLOTACIONGANADERA_ID, ArchivarControllerE2ETests.TEST_GANADO_ID,
				ArchivarControllerE2ETests.TEST_ANIMAL_ID).with(SecurityMockMvcRequestPostProcessors.csrf()).param("fechaFallecimiento", "090909090").param("fechaVenta", "").param("masInfo", "info"))
			.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("animalHistorico", "fechaFallecimiento")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("animal/archivarAnimal"));
	}

	@WithMockUser(value = "veterinario1", authorities = {
		"veterinario"
	})
	@Test
	void testNegativoSalvarAnimalHistoricoMalFechaVentaPOST() throws Exception {
		this.mockMvc
			.perform(MockMvcRequestBuilders.post("/explotacion-ganadera/{explotacionGanaderaId}/ganado/{ganadoId}/animal/{animalId}/animal-historico/new", ArchivarControllerE2ETests.TEST_EXPLOTACIONGANADERA_ID, ArchivarControllerE2ETests.TEST_GANADO_ID,
				ArchivarControllerE2ETests.TEST_ANIMAL_ID).with(SecurityMockMvcRequestPostProcessors.csrf()).param("fechaFallecimiento", "").param("fechaVenta", "090909090").param("masInfo", "info"))
			.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("animalHistorico", "fechaVenta")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("animal/archivarAnimal"));
	}

	@WithMockUser(value = "veterinario1", authorities = {
		"veterinario"
	})
	@Test
	void testArchivarLoteGET() throws Exception {
		this.mockMvc
			.perform(MockMvcRequestBuilders.get("/explotacion-ganadera/{explotacionGanaderaId}/ganado/{ganadoId}/lote/{loteId}/animal-historico/new", ArchivarControllerE2ETests.TEST_EXPLOTACIONGANADERA_ID, ArchivarControllerE2ETests.TEST_GANADO_ID,
				ArchivarControllerE2ETests.TEST_LOTE_ID))
			.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("animal/archivarAnimal")).andExpect(MockMvcResultMatchers.model().attributeExists("animalHistorico"))
			.andExpect(MockMvcResultMatchers.model().attribute("animalHistorico", Matchers.hasProperty("fechaFallecimiento"))).andExpect(MockMvcResultMatchers.model().attribute("animalHistorico", Matchers.hasProperty("fechaVenta")))
			.andExpect(MockMvcResultMatchers.model().attribute("animalHistorico", Matchers.hasProperty("masInfo"))).andExpect(MockMvcResultMatchers.model().attribute("animalHistorico", Matchers.hasProperty("animal")));
	}

	@WithMockUser(value = "veterinario1", authorities = {
		"veterinario"
	})
	@Test
	void testArchivarLotePOST() throws Exception {
		this.mockMvc
			.perform(MockMvcRequestBuilders.post("/explotacion-ganadera/{explotacionGanaderaId}/ganado/{ganadoId}/lote/{loteId}/animal-historico/new", ArchivarControllerE2ETests.TEST_EXPLOTACIONGANADERA_ID, ArchivarControllerE2ETests.TEST_GANADO_ID,
				ArchivarControllerE2ETests.TEST_LOTE_ID).with(SecurityMockMvcRequestPostProcessors.csrf()).param("fechaFallecimiento", "").param("fechaVenta", "2020/10/01").param("masInfo", "info"))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/explotacion-ganadera/1/ganado/1/animal/esArchivado/false/list"));
	}

	@WithMockUser(value = "veterinario1", authorities = {
		"veterinario"
	})
	@Test
	void testNegativoArchivarLotePOST() throws Exception {
		this.mockMvc
			.perform(MockMvcRequestBuilders.post("/explotacion-ganadera/{explotacionGanaderaId}/ganado/{ganadoId}/lote/{loteId}/animal-historico/new", ArchivarControllerE2ETests.TEST_EXPLOTACIONGANADERA_ID, ArchivarControllerE2ETests.TEST_GANADO_ID,
				ArchivarControllerE2ETests.TEST_LOTE_ID).with(SecurityMockMvcRequestPostProcessors.csrf()).param("fechaFallecimiento", "").param("fechaVenta", "").param("masInfo", "info"))
			.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("animalHistorico", "fechaFallecimiento", "fechaVenta")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("animal/archivarAnimal"));
	}

	@WithMockUser(value = "veterinario1", authorities = {
		"veterinario"
	})
	@Test
	void testNegativoArchivarLoteMalFechaVentaPOST() throws Exception {
		this.mockMvc
			.perform(MockMvcRequestBuilders.post("/explotacion-ganadera/{explotacionGanaderaId}/ganado/{ganadoId}/lote/{loteId}/animal-historico/new", ArchivarControllerE2ETests.TEST_EXPLOTACIONGANADERA_ID, ArchivarControllerE2ETests.TEST_GANADO_ID,
				ArchivarControllerE2ETests.TEST_LOTE_ID).with(SecurityMockMvcRequestPostProcessors.csrf()).param("fechaFallecimiento", "").param("fechaVenta", "090909090").param("masInfo", "info"))
			.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("animalHistorico", "fechaVenta")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("animal/archivarAnimal"));
	}

	@WithMockUser(value = "veterinario1", authorities = {
		"veterinario"
	})
	@Test
	void testNegativoArchivarLoteMalFechaFallecimientoPOST() throws Exception {
		this.mockMvc
			.perform(MockMvcRequestBuilders.post("/explotacion-ganadera/{explotacionGanaderaId}/ganado/{ganadoId}/lote/{loteId}/animal-historico/new", ArchivarControllerE2ETests.TEST_EXPLOTACIONGANADERA_ID, ArchivarControllerE2ETests.TEST_GANADO_ID,
				ArchivarControllerE2ETests.TEST_LOTE_ID).with(SecurityMockMvcRequestPostProcessors.csrf()).param("fechaFallecimiento", "090909090").param("fechaVenta", "").param("masInfo", "info"))
			.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("animalHistorico", "fechaFallecimiento")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("animal/archivarAnimal"));
	}

	@WithMockUser(value = "veterinario1", authorities = {
		"veterinario"
	})
	@Test
	void testArchivarGanadoGET() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/explotacion-ganadera/{explotacionGanaderaId}/ganado/{ganadoId}/animal/animal-historico/new", ArchivarControllerE2ETests.TEST_EXPLOTACIONGANADERA_ID, ArchivarControllerE2ETests.TEST_GANADO_ID))
			.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("animal/archivarAnimal")).andExpect(MockMvcResultMatchers.model().attributeExists("animalHistorico"))
			.andExpect(MockMvcResultMatchers.model().attribute("animalHistorico", Matchers.hasProperty("fechaFallecimiento"))).andExpect(MockMvcResultMatchers.model().attribute("animalHistorico", Matchers.hasProperty("fechaVenta")))
			.andExpect(MockMvcResultMatchers.model().attribute("animalHistorico", Matchers.hasProperty("masInfo"))).andExpect(MockMvcResultMatchers.model().attribute("animalHistorico", Matchers.hasProperty("animal")));
	}

	@WithMockUser(value = "veterinario1", authorities = {
		"veterinario"
	})
	@Test
	void testPositivoArchivarGanadoPOST() throws Exception {
		this.mockMvc
			.perform(MockMvcRequestBuilders.post("/explotacion-ganadera/{explotacionGanaderaId}/ganado/{ganadoId}/animal/animal-historico/new", ArchivarControllerE2ETests.TEST_EXPLOTACIONGANADERA_ID, ArchivarControllerE2ETests.TEST_GANADO_ID)
				.with(SecurityMockMvcRequestPostProcessors.csrf()).param("fechaFallecimiento", "").param("fechaVenta", "2020/10/01").param("masInfo", "info"))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/veterinario/explotacion-ganadera/list"));
	}

	@WithMockUser(value = "veterinario1", authorities = {
		"veterinario"
	})
	@Test
	void testNegativoArchivarGanadoPOST() throws Exception {
		this.mockMvc
			.perform(MockMvcRequestBuilders.post("/explotacion-ganadera/{explotacionGanaderaId}/ganado/{ganadoId}/animal/animal-historico/new", ArchivarControllerE2ETests.TEST_EXPLOTACIONGANADERA_ID, ArchivarControllerE2ETests.TEST_GANADO_ID)
				.with(SecurityMockMvcRequestPostProcessors.csrf()).param("fechaFallecimiento", "").param("fechaVenta", "").param("masInfo", "info"))
			.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("animalHistorico", "fechaFallecimiento", "fechaVenta")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("animal/archivarAnimal"));
	}

	@WithMockUser(value = "veterinario1", authorities = {
		"veterinario"
	})
	@Test
	void testNegativoArchivarGanadoMalFechaVentaPOST() throws Exception {
		this.mockMvc
			.perform(MockMvcRequestBuilders.post("/explotacion-ganadera/{explotacionGanaderaId}/ganado/{ganadoId}/animal/animal-historico/new", ArchivarControllerE2ETests.TEST_EXPLOTACIONGANADERA_ID, ArchivarControllerE2ETests.TEST_GANADO_ID)
				.with(SecurityMockMvcRequestPostProcessors.csrf()).param("fechaFallecimiento", "").param("fechaVenta", "090909090").param("masInfo", "info"))
			.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("animalHistorico", "fechaVenta")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("animal/archivarAnimal"));
	}

	@WithMockUser(value = "veterinario1", authorities = {
		"veterinario"
	})
	@Test
	void testNegativoArchivarGanadoMalFechaFallecimientoPOST() throws Exception {
		this.mockMvc
			.perform(MockMvcRequestBuilders.post("/explotacion-ganadera/{explotacionGanaderaId}/ganado/{ganadoId}/animal/animal-historico/new", ArchivarControllerE2ETests.TEST_EXPLOTACIONGANADERA_ID, ArchivarControllerE2ETests.TEST_GANADO_ID)
				.with(SecurityMockMvcRequestPostProcessors.csrf()).param("fechaFallecimiento", "090909090").param("fechaVenta", "").param("masInfo", "info"))
			.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("animalHistorico", "fechaFallecimiento")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("animal/archivarAnimal"));
	}
}
