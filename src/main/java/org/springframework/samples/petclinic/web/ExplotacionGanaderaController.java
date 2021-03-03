
package org.springframework.samples.petclinic.web;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Animal;
import org.springframework.samples.petclinic.model.Contrato;
import org.springframework.samples.petclinic.model.ExplotacionGanadera;
import org.springframework.samples.petclinic.model.TipoEstadoContrato;
import org.springframework.samples.petclinic.repository.springdatajpa.TiposGanadoRepository;
import org.springframework.samples.petclinic.service.AnimalService;
import org.springframework.samples.petclinic.service.ContratoService;
import org.springframework.samples.petclinic.service.ExplotacionGanaderaService;
import org.springframework.samples.petclinic.util.ExplotacionGanaderaValidator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ExplotacionGanaderaController {

	@Autowired
	private final ExplotacionGanaderaService	expService;

	@Autowired
	private AnimalService						animalService;

	@Autowired
	private ContratoService						contratoService;

	@Autowired
	private TiposGanadoRepository				tiposGanadoRepository;	

  
	public ExplotacionGanaderaController(ExplotacionGanaderaService expService, AnimalService animalService,
			ContratoService contratoService, TiposGanadoRepository tiposGanadoRepository) {
		super();
		this.expService = expService;
		this.animalService = animalService;
		this.contratoService = contratoService;
		this.tiposGanadoRepository = tiposGanadoRepository;
	}

	@InitBinder
	public void setAllowedFields(final WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

	@InitBinder("explotacionGanadera")
	public void initBinder(final WebDataBinder dataBinder) {
		dataBinder.addValidators(new ExplotacionGanaderaValidator(this.expService));
	}

	@GetMapping(value = "/ganadero/explotacion-ganadera/new")
	public String crearExplotacionGanadera(final ModelMap modelMap) {
		String view = "explotacionGanadera/editExpGanadera";
		modelMap.addAttribute("explotacionGanadera", new ExplotacionGanadera());
		return view;
	}

	@PostMapping(value = "/ganadero/explotacion-ganadera/new")
	public String salvarExplotacionGanadera(@Valid final ExplotacionGanadera explotacionGanadera, final BindingResult result, final ModelMap modelMap) {
		String view = "explotacionGanadera/listaExplotacionesGanadero";
		if (result.hasErrors()) {
			modelMap.addAttribute("explotacionGanadera", explotacionGanadera);
			return "explotacionGanadera/editExpGanadera";
		} else {
			this.expService.saveExpGanadera(explotacionGanadera);
			modelMap.addAttribute("message", "¡Tu explotación ganadera ha sido creada con éxito!");
			view = this.listadoExplotacionGanaderaGanadero(modelMap);
		}
		return view;
	}

	@GetMapping(value = "ganadero/explotacion-ganadera/list")
	public String listadoExplotacionGanaderaGanadero(final ModelMap modelMap) {
		String view = "explotacionGanadera/listaExplotacionesGanadero";
		Iterable<ExplotacionGanadera> exps = this.expService.findExpGanaderaByGanaderoId(false);
		modelMap.addAttribute("expGanaderas", exps);
		modelMap.addAttribute("todosTiposG", this.tiposGanadoRepository.findAll());
		return view;
	}

	@GetMapping(value = "veterinario/explotacion-ganadera/list")
	public String listadoExplotacionGanaderaVeterinario(final ModelMap modelMap) {
		String view = "explotacionGanadera/listaExplotacionesVeterinario";
		modelMap.addAttribute("contratos", this.contratoService.findAllContratoVigentesByLoguedVeterinario());
		modelMap.addAttribute("todosTiposG", this.tiposGanadoRepository.findAll());
		return view;
	}

	@GetMapping(value = "ganadero/explotacion-ganadera/{expId}/archivarExpGanadera")
	public String salvarExplotacionGanaderaArchivada(final ModelMap modelMap, @PathVariable("expId") final int expId) {
		String view = "explotacionGanadera/listaExplotacionesGanadero";
		List<Animal> animales = (List<Animal>) this.animalService.findAllAnimalByExpId(expId, false);
		if (animales.isEmpty()) {
			ExplotacionGanadera expGanadera = this.expService.findExpGanaderaById(expId).get();
			this.expService.archivarExpGanadera(expGanadera);
			Iterable<Contrato> contratosAceptados = this.contratoService.findAllContratoByExpGanaderaAndEstado(expGanadera.getId(), TipoEstadoContrato.ACEPTADO);
			for (Contrato c : contratosAceptados) {
				c.setEstado(TipoEstadoContrato.FINALIZADO);
			}
			Iterable<Contrato> contratosPendientes = this.contratoService.findAllContratoByExpGanaderaAndEstado(expGanadera.getId(), TipoEstadoContrato.PENDIENTE);
			for (Contrato c : contratosPendientes) {
				this.contratoService.deleteContrato(c);
			}
		} else {
			modelMap.addAttribute("message", "La explotación ganadera elegida para archivar, tiene algún animal no archivado. Por lo tanto, no se puede archivar.");
		}
		view = this.listadoExplotacionGanaderaGanadero(modelMap);

		return view;
	}

}
