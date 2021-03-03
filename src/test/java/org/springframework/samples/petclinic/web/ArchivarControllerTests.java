
package org.springframework.samples.petclinic.web;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.util.Pair;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.model.Animal;
import org.springframework.samples.petclinic.model.AnimalHistorico;
import org.springframework.samples.petclinic.model.ExplotacionGanadera;
import org.springframework.samples.petclinic.model.Ganadero;
import org.springframework.samples.petclinic.model.Lote;
import org.springframework.samples.petclinic.model.TipoSexo;
import org.springframework.samples.petclinic.model.TiposGanado;
import org.springframework.samples.petclinic.service.AnimalHistoricoService;
import org.springframework.samples.petclinic.service.AnimalService;
import org.springframework.samples.petclinic.service.ArchivarServiceTests;
import org.springframework.samples.petclinic.service.ContratoServiceTests;
import org.springframework.samples.petclinic.service.ExplotacionGanaderaService;
import org.springframework.samples.petclinic.service.GanaderoService;
import org.springframework.samples.petclinic.service.GanaderoServiceTests;
import org.springframework.samples.petclinic.service.LoteService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(controllers = AnimalHistoricoController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
public class ArchivarControllerTests {

	private static final int	TEST_EXPLOTACIONGANADERA_ID	= 1;

	private static final int	TEST_GANADO_ID				= 2;

	private static final int	TEST_ANIMAL_ID				= 1;

	private static final int	TEST_LOTE_ID				= 1;

	@Autowired
	MockMvc						mockMvc;

	@MockBean
	AnimalHistoricoService		animalHistoricoService;

	@MockBean
	AnimalService				animalService;

	@MockBean
	LoteService					loteService;

	@MockBean
	ExplotacionGanaderaService	expGanaderaService;

	@MockBean
	GanaderoService				ganaderoService;


	public static Pair<Lote, List<Animal>> loteForTesting() {
		Ganadero ganadero1 = GanaderoServiceTests.generateGanadero("Sora", "Riku", "622222222", "kingdom@gmail.com", "Badajoz", "Hinojosa del Valle", "45556839L", "06226", "Calle Islas del Destino", "soriku", "kairi4e");
		ExplotacionGanadera expGanadera1 = ContratoServiceTests.generateExplotacionGanadera("1", "Los Antruejos", false, ganadero1, "Villaricos");
		expGanadera1.setId(ArchivarControllerTests.TEST_EXPLOTACIONGANADERA_ID);
		TiposGanado tipoGanado1 = new TiposGanado();
		tipoGanado1.setTipoGanado("Ovino");
		tipoGanado1.setId(2);
		Lote lote = ArchivarServiceTests.generateLote(false, expGanadera1, "LO12345", tipoGanado1);
		List<Animal> animales = new ArrayList<>();
		Animal animal1 = ArchivarServiceTests.generateAnimal(false, true, expGanadera1, null, LocalDate.of(2020, 1, 15), LocalDate.of(2020, 1, 10), "OV1", null, null, TipoSexo.Macho, tipoGanado1);
		animales.add(animal1);
		Animal animal2 = ArchivarServiceTests.generateAnimal(false, true, expGanadera1, null, LocalDate.of(2020, 1, 15), LocalDate.of(2020, 1, 10), "OV2", null, null, TipoSexo.Macho, tipoGanado1);
		animales.add(animal2);
		Animal animal3 = ArchivarServiceTests.generateAnimal(false, true, expGanadera1, null, LocalDate.of(2020, 1, 15), LocalDate.of(2020, 1, 10), "OV3", null, null, TipoSexo.Macho, tipoGanado1);
		animales.add(animal3);
		for (Animal a : animales) {
			a.setLote(lote);
		}
		Pair<Lote, List<Animal>> tupla = Pair.of(lote, animales);
		return tupla;
	}

	public static Animal animalForTestingNoDeLote() throws ParseException {
		Ganadero ganadero1 = GanaderoServiceTests.generateGanadero("Sora", "Riku", "622222222", "kingdom@gmail.com", "Badajoz", "Hinojosa del Valle", "45556839L", "06226", "Calle Islas del Destino", "soriku", "kairi4e");
		ExplotacionGanadera expGanadera1 = ContratoServiceTests.generateExplotacionGanadera("1", "Los Antruejos", false, ganadero1, "Villaricos");
		expGanadera1.setId(ArchivarControllerTests.TEST_EXPLOTACIONGANADERA_ID);
		TiposGanado tipoGanado1 = new TiposGanado();
		tipoGanado1.setTipoGanado("Ovino");
		tipoGanado1.setId(2);
		Animal animal = ArchivarServiceTests.generateAnimal(false, true, expGanadera1, null, LocalDate.of(2020, 1, 15), LocalDate.of(2020, 1, 10), "OV12345", null, null, TipoSexo.Macho, tipoGanado1);
		return animal;
	}

	public static Animal animalForTestingDeLote() throws ParseException {
		Ganadero ganadero1 = GanaderoServiceTests.generateGanadero("Sora", "Riku", "622222222", "kingdom@gmail.com", "Badajoz", "Hinojosa del Valle", "45556839L", "06226", "Calle Islas del Destino", "soriku", "kairi4e");
		ExplotacionGanadera expGanadera1 = ContratoServiceTests.generateExplotacionGanadera("1", "Los Antruejos", false, ganadero1, "Villaricos");
		expGanadera1.setId(ArchivarControllerTests.TEST_EXPLOTACIONGANADERA_ID);
		TiposGanado tipoGanado1 = new TiposGanado();
		tipoGanado1.setTipoGanado("Ovino");
		tipoGanado1.setId(2);
		Lote lote = ArchivarControllerTests.loteForTesting().getFirst();
		lote.setId(ArchivarControllerTests.TEST_LOTE_ID);
		Animal animal = ArchivarServiceTests.generateAnimal(false, true, expGanadera1, null, LocalDate.of(2020, 1, 15), LocalDate.of(2020, 1, 10), "OV12345", lote, null, TipoSexo.Macho, tipoGanado1);
		return animal;
	}

	public AnimalHistorico animalHistoricoForTesting() throws ParseException {
		Animal animal = ArchivarControllerTests.animalForTestingNoDeLote();
		AnimalHistorico animalHistorico = ArchivarServiceTests.generateAnimalHistorico(animal, null, LocalDate.of(2020, 10, 1), "info");
		return animalHistorico;
	}

	@BeforeEach
	void setup() {
		Ganadero ganadero1 = GanaderoServiceTests.generateGanadero("Sora", "Riku", "622222222", "kingdom@gmail.com", "Badajoz", "Hinojosa del Valle", "45556839L", "06226", "Calle Islas del Destino", "soriku", "kairi4e");
		Optional<Ganadero> ganaderoO = Optional.of(ganadero1);
		BDDMockito.given(this.ganaderoService.findGanaderoById(ArchivarControllerTests.TEST_GANADO_ID)).willReturn(ganaderoO);
		ExplotacionGanadera expGanadera1 = ContratoServiceTests.generateExplotacionGanadera("1", "Los Antruejos", false, ganadero1, "Villaricos");
		expGanadera1.setId(ArchivarControllerTests.TEST_EXPLOTACIONGANADERA_ID);
		Optional<ExplotacionGanadera> expGanaderaO = Optional.of(expGanadera1);
		BDDMockito.given(this.expGanaderaService.findExpGanaderaById(ArchivarControllerTests.TEST_EXPLOTACIONGANADERA_ID)).willReturn(expGanaderaO);
		TiposGanado tipoGanado1 = new TiposGanado();
		tipoGanado1.setTipoGanado("Ovino");
		tipoGanado1.setId(2);
		Lote lote = ArchivarControllerTests.loteForTesting().getFirst();
		lote.setId(ArchivarControllerTests.TEST_LOTE_ID);
		Optional<Lote> loteO = Optional.of(lote);
		BDDMockito.given(this.loteService.findLoteById(1)).willReturn(loteO);
	}

	@WithMockUser(value = "spring")
	@Test
	void testArchivarAnimalGET() throws Exception {
		this.mockMvc
			.perform(MockMvcRequestBuilders.get("/explotacion-ganadera/{explotacionGanaderaId}/ganado/{ganadoId}/animal/{animalId}/animal-historico/new", ArchivarControllerTests.TEST_EXPLOTACIONGANADERA_ID, ArchivarControllerTests.TEST_GANADO_ID,
				ArchivarControllerTests.TEST_ANIMAL_ID))
			.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("animal/archivarAnimal")).andExpect(MockMvcResultMatchers.model().attributeExists("animalHistorico"))
			.andExpect(MockMvcResultMatchers.model().attribute("animalHistorico", Matchers.hasProperty("fechaFallecimiento"))).andExpect(MockMvcResultMatchers.model().attribute("animalHistorico", Matchers.hasProperty("fechaVenta")))
			.andExpect(MockMvcResultMatchers.model().attribute("animalHistorico", Matchers.hasProperty("masInfo"))).andExpect(MockMvcResultMatchers.model().attribute("animalHistorico", Matchers.hasProperty("animal")));
	}

	@WithMockUser(value = "spring")
	@Test
	void testPositivoArchivarAnimalPOST() throws Exception {
		Optional<Animal> animalO = Optional.of(ArchivarControllerTests.animalForTestingNoDeLote());
		BDDMockito.given(this.animalService.findAnimalById(ArchivarControllerTests.TEST_ANIMAL_ID)).willReturn(animalO);

		this.mockMvc.perform(MockMvcRequestBuilders
			.post("/explotacion-ganadera/{explotacionGanaderaId}/ganado/{ganadoId}/animal/{animalId}/animal-historico/new", ArchivarControllerTests.TEST_EXPLOTACIONGANADERA_ID, ArchivarControllerTests.TEST_GANADO_ID, ArchivarControllerTests.TEST_ANIMAL_ID)
			.with(SecurityMockMvcRequestPostProcessors.csrf()).param("fechaFallecimiento", "").param("fechaVenta", "2020/10/01").param("masInfo", "info")).andExpect(MockMvcResultMatchers.status().is3xxRedirection())
			.andExpect(MockMvcResultMatchers.view().name("redirect:/explotacion-ganadera/1/ganado/2/animal/esArchivado/false/list"));
		Mockito.verify(this.animalService).findAnimalById(ArchivarControllerTests.TEST_ANIMAL_ID);
		Mockito.verify(this.animalHistoricoService).saveAnimalHistorico(this.animalHistoricoForTesting());
		Mockito.verify(this.animalService).archivarAnimal(ArchivarControllerTests.animalForTestingNoDeLote());
	}

	@WithMockUser(value = "spring")
	@Test
	void testPositivoArchivarAnimalDeLotePOST() throws Exception {
		Optional<Animal> animalO = Optional.of(ArchivarControllerTests.animalForTestingDeLote());
		BDDMockito.given(this.animalService.findAnimalById(ArchivarControllerTests.TEST_ANIMAL_ID)).willReturn(animalO);
		List<Animal> animalesLote = ArchivarControllerTests.loteForTesting().getSecond();
		BDDMockito.given(this.animalService.findAllByLoteId(ArchivarControllerTests.TEST_LOTE_ID, false)).willReturn(animalesLote);

		this.mockMvc.perform(MockMvcRequestBuilders
			.post("/explotacion-ganadera/{explotacionGanaderaId}/ganado/{ganadoId}/animal/{animalId}/animal-historico/new", ArchivarControllerTests.TEST_EXPLOTACIONGANADERA_ID, ArchivarControllerTests.TEST_GANADO_ID, ArchivarControllerTests.TEST_ANIMAL_ID)
			.with(SecurityMockMvcRequestPostProcessors.csrf()).param("fechaFallecimiento", "").param("fechaVenta", "2020/10/01").param("masInfo", "info")).andExpect(MockMvcResultMatchers.status().is3xxRedirection())
			.andExpect(MockMvcResultMatchers.view().name("redirect:/explotacion-ganadera/1/ganado/2/animal/lote/1/show"));
		Mockito.verify(this.animalService).findAnimalById(ArchivarControllerTests.TEST_ANIMAL_ID);
		AnimalHistorico animalHdeLote = this.animalHistoricoForTesting();
		animalHdeLote.getAnimal().setLote(ArchivarControllerTests.loteForTesting().getFirst());
		Mockito.verify(this.animalHistoricoService).saveAnimalHistorico(animalHdeLote);
		Mockito.verify(this.animalService).archivarAnimal(ArchivarControllerTests.animalForTestingDeLote());

	}

	@WithMockUser(value = "spring")
	@Test
	void testNegativoSalvarAnimalHistoricoPOST() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders
			.post("/explotacion-ganadera/{explotacionGanaderaId}/ganado/{ganadoId}/animal/{animalId}/animal-historico/new", ArchivarControllerTests.TEST_EXPLOTACIONGANADERA_ID, ArchivarControllerTests.TEST_GANADO_ID, ArchivarControllerTests.TEST_ANIMAL_ID)
			.with(SecurityMockMvcRequestPostProcessors.csrf()).param("fechaFallecimiento", "").param("fechaVenta", "").param("masInfo", "info"))
			.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("animalHistorico", "fechaFallecimiento", "fechaVenta")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("animal/archivarAnimal"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testNegativoSalvarAnimalHistoricoMalFechaFallecimientoPOST() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders
			.post("/explotacion-ganadera/{explotacionGanaderaId}/ganado/{ganadoId}/animal/{animalId}/animal-historico/new", ArchivarControllerTests.TEST_EXPLOTACIONGANADERA_ID, ArchivarControllerTests.TEST_GANADO_ID, ArchivarControllerTests.TEST_ANIMAL_ID)
			.with(SecurityMockMvcRequestPostProcessors.csrf()).param("fechaFallecimiento", "090909090").param("fechaVenta", "").param("masInfo", "info"))
			.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("animalHistorico", "fechaFallecimiento")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("animal/archivarAnimal"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testNegativoSalvarAnimalHistoricoMalFechaVentaPOST() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders
			.post("/explotacion-ganadera/{explotacionGanaderaId}/ganado/{ganadoId}/animal/{animalId}/animal-historico/new", ArchivarControllerTests.TEST_EXPLOTACIONGANADERA_ID, ArchivarControllerTests.TEST_GANADO_ID, ArchivarControllerTests.TEST_ANIMAL_ID)
			.with(SecurityMockMvcRequestPostProcessors.csrf()).param("fechaFallecimiento", "").param("fechaVenta", "090909090").param("masInfo", "info")).andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("animalHistorico", "fechaVenta"))
			.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("animal/archivarAnimal"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testArchivarLoteGET() throws Exception {
		this.mockMvc
			.perform(MockMvcRequestBuilders.get("/explotacion-ganadera/{explotacionGanaderaId}/ganado/{ganadoId}/lote/{loteId}/animal-historico/new", ArchivarControllerTests.TEST_EXPLOTACIONGANADERA_ID, ArchivarControllerTests.TEST_GANADO_ID,
				ArchivarControllerTests.TEST_LOTE_ID))
			.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("animal/archivarAnimal")).andExpect(MockMvcResultMatchers.model().attributeExists("animalHistorico"))
			.andExpect(MockMvcResultMatchers.model().attribute("animalHistorico", Matchers.hasProperty("fechaFallecimiento"))).andExpect(MockMvcResultMatchers.model().attribute("animalHistorico", Matchers.hasProperty("fechaVenta")))
			.andExpect(MockMvcResultMatchers.model().attribute("animalHistorico", Matchers.hasProperty("masInfo"))).andExpect(MockMvcResultMatchers.model().attribute("animalHistorico", Matchers.hasProperty("animal")));
	}

	@WithMockUser(value = "spring")
	@Test
	void testArchivarLotePOST() throws Exception {
		List<Animal> animales = ArchivarControllerTests.loteForTesting().getSecond();
		BDDMockito.given(this.animalService.findAllByLoteId(ArchivarControllerTests.TEST_LOTE_ID, false)).willReturn(animales);
		this.mockMvc
			.perform(MockMvcRequestBuilders
				.post("/explotacion-ganadera/{explotacionGanaderaId}/ganado/{ganadoId}/lote/{loteId}/animal-historico/new", ArchivarControllerTests.TEST_EXPLOTACIONGANADERA_ID, ArchivarControllerTests.TEST_GANADO_ID, ArchivarControllerTests.TEST_LOTE_ID)
				.with(SecurityMockMvcRequestPostProcessors.csrf()).param("fechaFallecimiento", "").param("fechaVenta", "2020/10/01").param("masInfo", "info"))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/explotacion-ganadera/1/ganado/2/animal/esArchivado/false/list"));
		Mockito.verify(this.loteService).findLoteById(ArchivarControllerTests.TEST_LOTE_ID);
		Mockito.verify(this.loteService).archivarLote(ArchivarControllerTests.loteForTesting().getFirst());
		Mockito.verify(this.animalService).findAllByLoteId(ArchivarControllerTests.TEST_LOTE_ID, false);
		for (Animal a : animales) {
			AnimalHistorico ah = new AnimalHistorico();
			ah.setFechaVenta(LocalDate.of(2020, 10, 01));
			ah.setAnimal(a);
			ah.setMasInfo("info");
			Mockito.verify(this.animalHistoricoService).saveAnimalHistorico(ah);
			Mockito.verify(this.animalService).archivarAnimal(a);
		}
	}

	@WithMockUser(value = "spring")
	@Test
	void testNegativoArchivarLotePOST() throws Exception {
		this.mockMvc
			.perform(MockMvcRequestBuilders
				.post("/explotacion-ganadera/{explotacionGanaderaId}/ganado/{ganadoId}/lote/{loteId}/animal-historico/new", ArchivarControllerTests.TEST_EXPLOTACIONGANADERA_ID, ArchivarControllerTests.TEST_GANADO_ID, ArchivarControllerTests.TEST_LOTE_ID)
				.with(SecurityMockMvcRequestPostProcessors.csrf()).param("fechaFallecimiento", "").param("fechaVenta", "").param("masInfo", "info"))
			.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("animalHistorico", "fechaFallecimiento", "fechaVenta")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("animal/archivarAnimal"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testNegativoArchivarLoteMalFechaVentaPOST() throws Exception {
		this.mockMvc
			.perform(MockMvcRequestBuilders
				.post("/explotacion-ganadera/{explotacionGanaderaId}/ganado/{ganadoId}/lote/{loteId}/animal-historico/new", ArchivarControllerTests.TEST_EXPLOTACIONGANADERA_ID, ArchivarControllerTests.TEST_GANADO_ID, ArchivarControllerTests.TEST_LOTE_ID)
				.with(SecurityMockMvcRequestPostProcessors.csrf()).param("fechaFallecimiento", "").param("fechaVenta", "090909090").param("masInfo", "info"))
			.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("animalHistorico", "fechaVenta")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("animal/archivarAnimal"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testNegativoArchivarLoteMalFechaFallecimientoPOST() throws Exception {
		this.mockMvc
			.perform(MockMvcRequestBuilders
				.post("/explotacion-ganadera/{explotacionGanaderaId}/ganado/{ganadoId}/lote/{loteId}/animal-historico/new", ArchivarControllerTests.TEST_EXPLOTACIONGANADERA_ID, ArchivarControllerTests.TEST_GANADO_ID, ArchivarControllerTests.TEST_LOTE_ID)
				.with(SecurityMockMvcRequestPostProcessors.csrf()).param("fechaFallecimiento", "090909090").param("fechaVenta", "").param("masInfo", "info"))
			.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("animalHistorico", "fechaFallecimiento")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("animal/archivarAnimal"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testArchivarGanadoGET() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/explotacion-ganadera/{explotacionGanaderaId}/ganado/{ganadoId}/animal/animal-historico/new", ArchivarControllerTests.TEST_EXPLOTACIONGANADERA_ID, ArchivarControllerTests.TEST_GANADO_ID))
			.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("animal/archivarAnimal")).andExpect(MockMvcResultMatchers.model().attributeExists("animalHistorico"))
			.andExpect(MockMvcResultMatchers.model().attribute("animalHistorico", Matchers.hasProperty("fechaFallecimiento"))).andExpect(MockMvcResultMatchers.model().attribute("animalHistorico", Matchers.hasProperty("fechaVenta")))
			.andExpect(MockMvcResultMatchers.model().attribute("animalHistorico", Matchers.hasProperty("masInfo"))).andExpect(MockMvcResultMatchers.model().attribute("animalHistorico", Matchers.hasProperty("animal")));
	}

	@WithMockUser(value = "spring")
	@Test
	void testPositivoArchivarGanadoPOST() throws Exception {
		List<Animal> animales = ArchivarControllerTests.loteForTesting().getSecond();
		//BDDMockito.given(this.animalService.findAllByLoteId(ArchivarControllerTests.TEST_LOTE_ID, false)).willReturn(animales);
		BDDMockito.given(this.animalService.findAllAnimalByExpIdYGanadoIYEsArchivado(ArchivarControllerTests.TEST_EXPLOTACIONGANADERA_ID, ArchivarControllerTests.TEST_GANADO_ID, false)).willReturn(animales);
		Lote lote = ArchivarControllerTests.loteForTesting().getFirst();
		List<Lote> lotes = new ArrayList<>();
		lotes.add(lote);
		BDDMockito.given(this.loteService.findAllLoteByExpIdYGanadoId(ArchivarControllerTests.TEST_EXPLOTACIONGANADERA_ID, ArchivarControllerTests.TEST_GANADO_ID, false)).willReturn(lotes);
		this.mockMvc
			.perform(MockMvcRequestBuilders.post("/explotacion-ganadera/{explotacionGanaderaId}/ganado/{ganadoId}/animal/animal-historico/new", ArchivarControllerTests.TEST_EXPLOTACIONGANADERA_ID, ArchivarControllerTests.TEST_GANADO_ID)
				.with(SecurityMockMvcRequestPostProcessors.csrf()).param("fechaFallecimiento", "").param("fechaVenta", "2020/10/01").param("masInfo", "info"))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/veterinario/explotacion-ganadera/list"));
		Mockito.verify(this.animalService).findAllAnimalByExpIdYGanadoIYEsArchivado(ArchivarControllerTests.TEST_EXPLOTACIONGANADERA_ID, ArchivarControllerTests.TEST_GANADO_ID, false);
		for (Animal a : animales) {
			AnimalHistorico ah = new AnimalHistorico();
			ah.setFechaVenta(LocalDate.of(2020, 10, 01));
			ah.setAnimal(a);
			ah.setMasInfo("info");
			Mockito.verify(this.animalHistoricoService).saveAnimalHistorico(ah);
			Mockito.verify(this.animalService).archivarAnimal(a);
		}
		Mockito.verify(this.loteService).findAllLoteByExpIdYGanadoId(ArchivarControllerTests.TEST_EXPLOTACIONGANADERA_ID, ArchivarControllerTests.TEST_GANADO_ID, false);
		for (Lote l : lotes) {
			Mockito.verify(this.loteService).archivarLote(l);
		}
	}

	@WithMockUser(value = "spring")
	@Test
	void testNegativoArchivarGanadoPOST() throws Exception {
		this.mockMvc
			.perform(MockMvcRequestBuilders.post("/explotacion-ganadera/{explotacionGanaderaId}/ganado/{ganadoId}/animal/animal-historico/new", ArchivarControllerTests.TEST_EXPLOTACIONGANADERA_ID, ArchivarControllerTests.TEST_GANADO_ID)
				.with(SecurityMockMvcRequestPostProcessors.csrf()).param("fechaFallecimiento", "").param("fechaVenta", "").param("masInfo", "info"))
			.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("animalHistorico", "fechaFallecimiento", "fechaVenta")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("animal/archivarAnimal"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testNegativoArchivarGanadoMalFechaVentaPOST() throws Exception {
		this.mockMvc
			.perform(MockMvcRequestBuilders.post("/explotacion-ganadera/{explotacionGanaderaId}/ganado/{ganadoId}/animal/animal-historico/new", ArchivarControllerTests.TEST_EXPLOTACIONGANADERA_ID, ArchivarControllerTests.TEST_GANADO_ID)
				.with(SecurityMockMvcRequestPostProcessors.csrf()).param("fechaFallecimiento", "").param("fechaVenta", "090909090").param("masInfo", "info"))
			.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("animalHistorico", "fechaVenta")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("animal/archivarAnimal"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testNegativoArchivarGanadoMalFechaFallecimientoPOST() throws Exception {
		this.mockMvc
			.perform(MockMvcRequestBuilders.post("/explotacion-ganadera/{explotacionGanaderaId}/ganado/{ganadoId}/animal/animal-historico/new", ArchivarControllerTests.TEST_EXPLOTACIONGANADERA_ID, ArchivarControllerTests.TEST_GANADO_ID)
				.with(SecurityMockMvcRequestPostProcessors.csrf()).param("fechaFallecimiento", "090909090").param("fechaVenta", "").param("masInfo", "info"))
			.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("animalHistorico", "fechaFallecimiento")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("animal/archivarAnimal"));
	}
}
