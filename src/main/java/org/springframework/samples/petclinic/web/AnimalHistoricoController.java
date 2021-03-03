
package org.springframework.samples.petclinic.web;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Animal;
import org.springframework.samples.petclinic.model.AnimalHistorico;
import org.springframework.samples.petclinic.model.Lote;
import org.springframework.samples.petclinic.service.AnimalHistoricoService;
import org.springframework.samples.petclinic.service.AnimalService;
import org.springframework.samples.petclinic.service.LoteService;
import org.springframework.samples.petclinic.util.AnimalHistoricoValidator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("explotacion-ganadera/{expGanaderaId}/ganado/{ganadoId}")
public class AnimalHistoricoController {

	@Autowired
	private final AnimalHistoricoService	animalHistoricoService;

	@Autowired
	private AnimalService					animalService;

	@Autowired
	private LoteService						loteService;


	public AnimalHistoricoController(final AnimalHistoricoService animalHistoricoService, final AnimalService animalService, final LoteService loteService) {
		this.animalHistoricoService = animalHistoricoService;
		this.animalService = animalService;
		this.loteService = loteService;
	}

	@InitBinder
	public void setAllowedFields(final WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

	@InitBinder("animalHistorico")
	public void initBinder(final WebDataBinder dataBinder) {
		dataBinder.addValidators(new AnimalHistoricoValidator());
	}

	@GetMapping(value = "/animal/{animalId}/animal-historico/new")
	public String crearAnimalHistorico(final ModelMap modelMap) {
		String view = "animal/archivarAnimal";
		AnimalHistorico animalHistorico = new AnimalHistorico();
		modelMap.addAttribute("animalHistorico", animalHistorico);
		return view;
	}

	@PostMapping(value = "/animal/{animalId}/animal-historico/new")
	public String salvarAnimalHistorico(@Valid final AnimalHistorico animalHistorico, final BindingResult result, final ModelMap modelMap, @PathVariable("animalId") final int animalId) {
		String view = "animal/listAnimales";
		if (result.hasErrors()) {
			modelMap.addAttribute("animalHistorico", animalHistorico);
			return "animal/archivarAnimal";
		} else {
			Animal animal = this.animalService.findAnimalById(animalId).get();
			animalHistorico.setAnimal(animal);
			this.animalHistoricoService.saveAnimalHistorico(animalHistorico);
			this.animalService.archivarAnimal(animal);
			if (animal.getLote() != null) {
				List<Animal> animalesLote = (List<Animal>) this.animalService.findAllByLoteId(animal.getLote().getId(), false);
				if (animalesLote.isEmpty()) {
					this.loteService.archivarLote(animal.getLote());
					view = "redirect:/explotacion-ganadera/" + animal.getExplotacionGanadera().getId() + "/ganado/" + animal.getTipoGanado().getId() + "/animal/esArchivado/false/list";
				} else {
					view = "redirect:/explotacion-ganadera/" + animal.getExplotacionGanadera().getId() + "/ganado/" + animal.getTipoGanado().getId() + "/animal/lote/" + animal.getLote().getId() + "/show";
				}
			} else {
				view = "redirect:/explotacion-ganadera/" + animal.getExplotacionGanadera().getId() + "/ganado/" + animal.getTipoGanado().getId() + "/animal/esArchivado/false/list";
			}
			modelMap.addAttribute("message", "El animal ha sido archivado con éxito");
		}
		return view;
	}

	@GetMapping(value = "/lote/{loteId}/animal-historico/new")
	public String crearLoteAnimalHistorico(final ModelMap modelMap) {
		String view = "animal/archivarAnimal";
		AnimalHistorico animalHistorico = new AnimalHistorico();
		modelMap.addAttribute("animalHistorico", animalHistorico);
		return view;
	}

	@PostMapping(value = "/lote/{loteId}/animal-historico/new")
	public String salvarLoteAnimalHistorico(@Valid final AnimalHistorico animalHistorico, final BindingResult result, final ModelMap modelMap, @PathVariable("loteId") final int loteId) {
		String view = "animal/listAnimales";
		if (result.hasErrors()) {
			modelMap.addAttribute("animalHistorico", animalHistorico);
			return "animal/archivarAnimal";
		} else {
			Lote lote = this.loteService.findLoteById(loteId).get();
			this.loteService.archivarLote(lote);
			Iterable<Animal> animales = this.animalService.findAllByLoteId(lote.getId(), false);
			for (Animal a : animales) {
				AnimalHistorico nuevoAnimalHistorico = this.createAnimalHistorico(animalHistorico, a);
				this.animalHistoricoService.saveAnimalHistorico(nuevoAnimalHistorico);
				this.animalService.archivarAnimal(a);
			}
			view = "redirect:/explotacion-ganadera/" + lote.getExplotacionGanadera().getId() + "/ganado/" + lote.getTipoGanado().getId() + "/animal/esArchivado/false/list";
			modelMap.addAttribute("message", "El lote ha sido archivado con éxito");
		}
		return view;
	}

	@GetMapping(value = "/animal/animal-historico/new")
	public String crearGanadoHistorico(final ModelMap modelMap) {
		String view = "animal/archivarAnimal";
		AnimalHistorico animalHistorico = new AnimalHistorico();
		modelMap.addAttribute("animalHistorico", animalHistorico);
		return view;
	}

	@PostMapping(value = "/animal/animal-historico/new")
	public String salvarGanadoHistorico(@Valid final AnimalHistorico animalHistorico, final BindingResult result, final ModelMap modelMap, @PathVariable("expGanaderaId") final int expGanaderaId, @PathVariable("ganadoId") final int ganadoId) {
		String view = "explotacionGanadera/listaExplotaciones";
		if (result.hasErrors()) {
			modelMap.addAttribute("animalHistorico", animalHistorico);
			return "animal/archivarAnimal";
		} else {
			//Archivamos todos los animales no archivados, sean de lote o no.
			Iterable<Animal> animales = this.animalService.findAllAnimalByExpIdYGanadoIYEsArchivado(expGanaderaId, ganadoId, false);
			for (Animal a : animales) {
				AnimalHistorico nuevoAnimalHistorico = this.createAnimalHistorico(animalHistorico, a);
				this.animalHistoricoService.saveAnimalHistorico(nuevoAnimalHistorico);
				this.animalService.archivarAnimal(a);
			}

			//Archivamos todos los lotes que existen
			Iterable<Lote> lotes = this.loteService.findAllLoteByExpIdYGanadoId(expGanaderaId, ganadoId, false);
			for (Lote l : lotes) {
				this.loteService.archivarLote(l);
			}
			view = "redirect:/veterinario/explotacion-ganadera/list";
			modelMap.addAttribute("message", "El ganado ha sido archivado con éxito.");
		}
		return view;
	}

	// ---- Métodos auxiliares ----
	public AnimalHistorico createAnimalHistorico(final AnimalHistorico animalHistorico, final Animal animal) {
		AnimalHistorico nuevoAnimalHistorico = new AnimalHistorico();
		nuevoAnimalHistorico.setFechaFallecimiento(animalHistorico.getFechaFallecimiento());
		nuevoAnimalHistorico.setFechaVenta(animalHistorico.getFechaVenta());
		nuevoAnimalHistorico.setMasInfo(animalHistorico.getMasInfo());
		nuevoAnimalHistorico.setAnimal(animal);
		return nuevoAnimalHistorico;
	}
}
