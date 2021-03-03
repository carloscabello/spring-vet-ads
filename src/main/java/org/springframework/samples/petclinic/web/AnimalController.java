
package org.springframework.samples.petclinic.web;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Animal;
import org.springframework.samples.petclinic.model.AnimalHistorico;
import org.springframework.samples.petclinic.model.ExplotacionGanadera;
import org.springframework.samples.petclinic.model.Lote;
import org.springframework.samples.petclinic.model.TiposGanado;
import org.springframework.samples.petclinic.repository.springdatajpa.TiposGanadoRepository;
import org.springframework.samples.petclinic.service.AnimalHistoricoService;
import org.springframework.samples.petclinic.service.AnimalService;
import org.springframework.samples.petclinic.service.ExplotacionGanaderaService;
import org.springframework.samples.petclinic.service.LoteService;
import org.springframework.samples.petclinic.util.AnimalValidator;
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
public class AnimalController {

	@Autowired
	private final AnimalService			animalService;

	@Autowired
	private ExplotacionGanaderaService	expGanaderaService;

	@Autowired
	private TiposGanadoRepository		tiposGanadoRepository;

	@Autowired
	private AnimalHistoricoService		animalHistoricoService;

	@Autowired
	private LoteService					loteService;

	public AnimalController(AnimalService animalService, ExplotacionGanaderaService expGanaderaService,
			TiposGanadoRepository tiposGanadoRepository, AnimalHistoricoService animalHistoricoService,
			LoteService loteService) {
		this.animalService = animalService;
		this.expGanaderaService = expGanaderaService;
		this.tiposGanadoRepository = tiposGanadoRepository;
		this.animalHistoricoService = animalHistoricoService;
		this.loteService = loteService;
	}

	@InitBinder
	public void setAllowedFields(final WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

	@InitBinder("animal")
	public void initBinder(final WebDataBinder dataBinder, @PathVariable("expGanaderaId") final int expGanaderaId) {
		dataBinder.addValidators(new AnimalValidator(this.animalService, expGanaderaId));
	}

	@GetMapping(value = "/animal/new")
	public String crearAnimal(final ModelMap modelMap, @PathVariable("ganadoId") final int ganadoId) {
		String view = "animal/editAnimal";
		Animal animal = new Animal();
		modelMap.addAttribute("animal", animal);
		return view;
	}

	@PostMapping(value = "/animal/new")
	public String salvarAnimal(@Valid final Animal animal, final BindingResult result, final ModelMap modelMap, @PathVariable("expGanaderaId") final int expGanaderaId, @PathVariable("ganadoId") final int ganadoId) {
		String view = "animal/listaAnimales";
		if (result.hasErrors()) {
			modelMap.addAttribute("animal", animal);
			return "animal/editAnimal";
		} else {
			ExplotacionGanadera expGanadera = this.expGanaderaService.findExpGanaderaById(expGanaderaId).get();
			animal.setExplotacionGanadera(expGanadera);
			animal.setTipoGanado(this.tiposGanadoRepository.findById(ganadoId).get());
			this.animalService.saveAnimal(animal);
			modelMap.addAttribute("message", "!Tu animal ha sido registrado con éxito!");
			view = this.listadoAnimales(modelMap, animal.getExplotacionGanadera().getId(), animal.getTipoGanado().getId(), false);
		}
		return view;
	}

	//Método que devuelve la lista de animales junto a la de lotes
	@GetMapping(value = "/animal/esArchivado/{esArchivado}/list")
	public String listadoAnimales(final ModelMap modelMap, @PathVariable("expGanaderaId") final int expGanaderaId, @PathVariable("ganadoId") final int ganadoId, @PathVariable("esArchivado") final boolean esArchivado) {
		String view = "animal/listaAnimales";
		Iterable<Animal> animales = this.animalService.findAllAnimalNoLoteByExpIdYGanadoId(expGanaderaId, ganadoId, esArchivado);
		Iterable<Lote> lote = this.loteService.findAllLoteFromAnimalByExpIdAndGanadoIdAndEsArchivado(expGanaderaId, ganadoId, esArchivado);
		TiposGanado tipoGanado = this.tiposGanadoRepository.findById(ganadoId).get();
		modelMap.addAttribute("lote", lote);
		modelMap.addAttribute("animales", animales);
		modelMap.addAttribute("expId", expGanaderaId);
		modelMap.addAttribute("tipoGanado", tipoGanado);
		modelMap.addAttribute("esArchivado", esArchivado);
		return view;
	}

	@GetMapping(value = "/animal/{animalId}/show")
	public String showAnimal(final ModelMap modelMap, @PathVariable("animalId") final int animalId) {
		String view = "animal/mostrarAnimal";
		Animal animal = this.animalService.findAnimalById(animalId).get();
		modelMap.addAttribute("animal", animal);
		if (animal.getEsArchivado() == true) {
			AnimalHistorico animalHistorico = this.animalHistoricoService.findAnimalHistoricoByAnimalId(animalId).get();
			modelMap.addAttribute("animalHistorico", animalHistorico);
		}
		return view;
	}

	@GetMapping(value = "/animal/{animalId}/delete")
	public String deleteAnimal(final ModelMap modelMap, @PathVariable("animalId") final int animalId) {
		Animal animal = this.animalService.findAnimalById(animalId).get();
		String res = "redirect:/explotacion-ganadera/" + animal.getExplotacionGanadera().getId() + "/ganado/" + animal.getTipoGanado().getId();

		/* Si el animal pertence a un lote, vuelvo a la vista de lote */
		if (animal.getLote() != null) {
			res += "/animal/lote/" + animal.getLote().getId() + "/show";

			/* Si el animal no pertenece al lote, vuelvo a la vista de la explotacion */
		} else {
			res += "/animal/esArchivado/true/list";
		}
		this.animalService.deleteAnimal(animal);
		return res;
	}
}
