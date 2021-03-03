
package org.springframework.samples.petclinic.web;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.forms.LoteForm;
import org.springframework.samples.petclinic.model.Animal;
import org.springframework.samples.petclinic.model.ExplotacionGanadera;
import org.springframework.samples.petclinic.model.Lote;
import org.springframework.samples.petclinic.model.TiposGanado;
import org.springframework.samples.petclinic.repository.springdatajpa.TiposGanadoRepository;
import org.springframework.samples.petclinic.service.AnimalService;
import org.springframework.samples.petclinic.service.ExplotacionGanaderaService;
import org.springframework.samples.petclinic.service.LoteService;
import org.springframework.samples.petclinic.util.LoteFormValidator;
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
@RequestMapping("explotacion-ganadera/{expGanaderaId}/ganado/{ganadoId}/animal")
public class LoteController {

	@Autowired
	private final LoteService			loteService;

	@Autowired
	private AnimalService				animalService;

	@Autowired
	private ExplotacionGanaderaService	expGanaderaService;

	@Autowired
	private TiposGanadoRepository		tiposGanadoRepository;


	public LoteController(final LoteService loteService) {
		this.loteService = loteService;
	}

	@InitBinder
	public void setAllowedFields(final WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

	@InitBinder("loteForm")
	public void initBinder(final WebDataBinder dataBinder, @PathVariable("expGanaderaId") final int expGanaderaId) {
		dataBinder.addValidators(new LoteFormValidator(this.loteService, expGanaderaId));
	}

	@GetMapping(value = "/lote/new")
	public String crearLote(final ModelMap modelMap) {
		String view = "lote/editLote";
		LoteForm loteForm = new LoteForm();
		modelMap.addAttribute("loteForm", loteForm);
		return view;
	}

	@PostMapping(value = "/lote/new")
	public String salvarLote(@Valid final LoteForm loteForm, final BindingResult result, final ModelMap modelMap, @PathVariable("expGanaderaId") final int expGanaderaId, @PathVariable("ganadoId") final int ganadoId) {
		String view = "animal/listaAnimales";
		if (result.hasErrors()) {
			modelMap.addAttribute("loteForm", loteForm);
			return "lote/editLote";
		} else {
			ExplotacionGanadera expGanadera = this.expGanaderaService.findExpGanaderaById(expGanaderaId).get();
			TiposGanado tipGan = this.tiposGanadoRepository.findById(ganadoId).get();
			Lote lote = new Lote();
			lote.setTipoGanado(tipGan);
			lote.setIdentificadorLote(loteForm.getIdentificadorLote());
			lote.setExplotacionGanadera(expGanadera);
			this.loteService.saveLote(lote);
			this.animalService.saveAnimalesLote(lote, loteForm);
			modelMap.addAttribute("message", "!El lote de animales ha sido registrado con éxito! Los animales que contienen han sido creados automáticamente con el identificador del lote, seguido de una combinación de 3 números.");
			view = "redirect:/explotacion-ganadera/" + lote.getExplotacionGanadera().getId() + "/ganado/" + lote.getTipoGanado().getId() + "/animal/esArchivado/false/list";
		}
		return view;
	}

	@GetMapping(value = "/lote/{loteId}/show")
	public String showLote(final ModelMap modelMap, @PathVariable("loteId") final int loteId) {
		String view = "lote/LoteShow";
		Optional<Lote> lote = this.loteService.findLoteById(loteId);
		Optional<Animal> animal = this.animalService.findAnimalByAnimalIdentificadorAnimal(lote.get().getIdentificadorLote() + "-001");
		Integer numMachos = this.loteService.numeroMachosByLoteId(loteId, lote.get().getEsArchivado());
		Integer numHembras = this.loteService.numeroHembrasByLoteId(loteId, lote.get().getEsArchivado());
		Iterable<Animal> animales = this.animalService.findAllByLoteId(loteId, null);
		modelMap.addAttribute("identificadorLote", lote.get().getIdentificadorLote());
		modelMap.addAttribute("expGanaderaId", lote.get().getExplotacionGanadera().getId());
		modelMap.addAttribute("animal", animal.get());
		modelMap.addAttribute("machos", numMachos);
		modelMap.addAttribute("hembras", numHembras);
		modelMap.addAttribute("animales", animales);
		modelMap.addAttribute("lote", lote.get());
		return view;
	}

}
