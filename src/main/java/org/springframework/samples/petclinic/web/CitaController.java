
package org.springframework.samples.petclinic.web;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.forms.CitaForm;
import org.springframework.samples.petclinic.forms.SelectCreateCitaForm;
import org.springframework.samples.petclinic.model.Cita;
import org.springframework.samples.petclinic.model.Contrato;
import org.springframework.samples.petclinic.model.ExplotacionGanadera;
import org.springframework.samples.petclinic.model.TipoEstadoCita;
import org.springframework.samples.petclinic.service.CitaService;
import org.springframework.samples.petclinic.service.ContratoService;
import org.springframework.samples.petclinic.service.ExplotacionGanaderaService;
import org.springframework.samples.petclinic.util.CitaFormValidator;
import org.springframework.samples.petclinic.util.CitaValidator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class CitaController {

	@Autowired
	private CitaService					citaService;

	@Autowired
	private ExplotacionGanaderaService	explotacionGanaderaService;

	@Autowired
	private ContratoService				contratoService;


	@InitBinder("citaForm")
	public void setAllowedFieldsCitaForm(final WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("contratoId");
	}

	@InitBinder("citaForm")
	public void initBinder2(final WebDataBinder dataBinder, @PathVariable("contratoId") final int contratoId) {
		dataBinder.addValidators(new CitaFormValidator(this.contratoService, this.citaService, contratoId));
	}

	@InitBinder("cita")
	public void initBinder(final WebDataBinder dataBinder) {
		dataBinder.addValidators(new CitaValidator());
	}

	@GetMapping(value = "/ganadero/contrato/{contratoId}/cita/new")
	public String createCita(final ModelMap modelMap, @PathVariable("contratoId") final int contratoId) {
		modelMap.addAttribute("citaForm", this.citaService.createCitaForm());
		return "cita/createCitaForm";
	}

	@PostMapping(value = "/ganadero/contrato/{contratoId}/cita/new")
	public String createCita(@Valid final CitaForm citaForm, final BindingResult result, final ModelMap modelMap, @PathVariable("contratoId") final int contratoId) {

		String res = "redirect:/ganadero/cita/list";
		citaForm.setContratoId(contratoId);
		if (result.hasErrors()) {
			modelMap.addAttribute("citaForm", citaForm);
			res = "cita/createCitaForm";

		} else {
			Cita cita = this.citaService.reconstruct(citaForm);
			this.citaService.saveCita(cita);
		}
		return res;
	}

	@PostMapping(value = "/ganadero/cita/list")
	public String listCitasGanadero(@Valid final SelectCreateCitaForm selectCreateCitaForm, final BindingResult result, final ModelMap modelMap) {
		String res = "/ganadero/cita/list";
		if (result.hasErrors()) {
			modelMap.addAttribute("selectCreateCitaForm", selectCreateCitaForm);
			res = "/ganadero/cita/list";
		} else {
			Contrato contrato = selectCreateCitaForm.getContrato();
			res = "redirect:/ganadero/contrato/" + contrato.getId() + "/cita/new";
		}

		return res;
	}

	@GetMapping(value = "/ganadero/cita/list")
	public String listCitasGanadero(final ModelMap modelMap) {
		this.initializeListCitas(modelMap);
		modelMap.addAttribute("selectCreateCitaForm", new SelectCreateCitaForm());

		return "cita/listCitasForm";
	}

	private void initializeListCitas(final ModelMap modelMap) {
		Iterable<Cita> citasAceptadas = this.citaService.findCitasFuturasByLoggedGanaderoAndEstado(TipoEstadoCita.ACEPTADA);
		Iterable<Cita> citasPendientes = this.citaService.findCitasFuturasByLoggedGanaderoAndEstado(TipoEstadoCita.PENDIENTE);
		Iterable<Cita> citasTodas = this.citaService.findCitasByLoggedGanaderoAndFilters(null, null, null);
		Iterable<ExplotacionGanadera> explotacionesGanadero = this.explotacionGanaderaService.findExpGanaderaByGanaderoId(false);
		Iterable<Contrato> contratos = this.contratoService.findContratosVigentesAceptadosByLogguedGanadero();
		modelMap.addAttribute("citasAceptadas", citasAceptadas);
		modelMap.addAttribute("citasPendientes", citasPendientes);
		modelMap.addAttribute("citasTodas", citasTodas);
		modelMap.addAttribute("explotacionesGanadero", explotacionesGanadero);
		modelMap.addAttribute("contratos", contratos);
	}

	@GetMapping(value = "/veterinario/cita/list")
	public String listCitasVeterinario(final ModelMap modelMap) {
		Iterable<Cita> citasAceptadas = this.citaService.findCitasFuturasByLoggedVeterinarioAndEstado(TipoEstadoCita.ACEPTADA);
		Iterable<Cita> citasPendientes = this.citaService.findCitasFuturasByLoggedVeterinarioAndEstado(TipoEstadoCita.PENDIENTE);
		Iterable<Cita> citasTodas = this.citaService.findCitasByLoggedVeterinarioAndFilters(null, null, null);
		modelMap.addAttribute("citasAceptadas", citasAceptadas);
		modelMap.addAttribute("citasPendientes", citasPendientes);
		modelMap.addAttribute("citasTodas", citasTodas);
		return "cita/listCitasForm";
	}

	@GetMapping(value = "/veterinario/cita/{citaId}/decline")
	public String declineCitaVeterinario(final ModelMap modelMap, @PathVariable("citaId") final int citaId) {
		Cita cita = this.citaService.findCitaById(citaId).get();
		modelMap.addAttribute("cita", cita);
		return "cita/citaDeclineForm";
	}

	@PostMapping(value = "/veterinario/cita/{citaId}/decline")
	public String declineCitaVeterinario(@Valid final Cita cita, final BindingResult result, @PathVariable("citaId") final int citaId, final ModelMap modelMap) {
		String res = "redirect:/veterinario/cita/list";
		if (result.hasErrors()) {
			modelMap.addAttribute("cita", cita);
			res = "cita/citaDeclineForm";
		} else {
			Cita citaAntigua = this.citaService.findCitaById(citaId).get();
			cita.setId(citaAntigua.getId());
			cita.setContrato(citaAntigua.getContrato());
			this.citaService.declineCita(cita);
		}
		return res;
	}

	@GetMapping(value = "/veterinario/cita/{citaId}/accept")
	public String acceptCitasVeterinario(@PathVariable("citaId") final int citaId) {
		Cita cita = this.citaService.findCitaById(citaId).get();
		this.citaService.acceptCita(cita);
		return "redirect:/veterinario/cita/list";
	}

}
