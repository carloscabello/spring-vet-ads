
package org.springframework.samples.petclinic.web;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Contrato;
import org.springframework.samples.petclinic.model.ExplotacionGanadera;
import org.springframework.samples.petclinic.model.TipoEstadoContrato;
import org.springframework.samples.petclinic.model.TiposGanado;
import org.springframework.samples.petclinic.model.Veterinario;
import org.springframework.samples.petclinic.service.ContratoService;
import org.springframework.samples.petclinic.service.ExplotacionGanaderaService;
import org.springframework.samples.petclinic.service.VetService;
import org.springframework.samples.petclinic.util.ContratoValidator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ContratoController {

	private static final String			VISTA_CREAR_CONTRATO_FORM	= "contrato/createContratoForm";

	@Autowired
	private ContratoService				contratoService;

	@Autowired
	private VetService					vetService;

	@Autowired
	private ExplotacionGanaderaService	expGanaderaService;


	@InitBinder("contrato")
	public void initBinder(final WebDataBinder dataBinder) {
		dataBinder.addValidators(new ContratoValidator(this.contratoService));
	}

	@GetMapping(value = "/veterinario/{vetId}/contrato")
	public String crearContrato(final ModelMap modelMap, @PathVariable("vetId") final int vetId) {
		Contrato contrato = new Contrato();
		contrato.setFechaPeticion(LocalDate.now());

		Veterinario vet = this.vetService.encontrarPorID(vetId).get();
		contrato.setVeterinario(vet);
		Iterable<ExplotacionGanadera> expGanaderas = this.expGanaderaService.findExpGanaderaByGanaderoId(false);
		modelMap.addAttribute("tiposG", vet.getTiposGanado());
		modelMap.addAttribute("expGs", expGanaderas);
		modelMap.addAttribute("contrato", contrato);
		modelMap.addAttribute("veterinario", vet);
		return ContratoController.VISTA_CREAR_CONTRATO_FORM;
	}

	@PostMapping(value = "/veterinario/{vetId}/contrato")
	public String guardarContrato(@Valid final Contrato contrato, final BindingResult result, final ModelMap modelMap, @PathVariable("vetId") final int vetId) {
		String view = "contrato/listarContratos";

		contrato.setFechaPeticion(LocalDate.now());
		contrato.setEstado(TipoEstadoContrato.PENDIENTE);
		if (result.hasErrors()) {
			Veterinario vet = this.vetService.encontrarPorID(vetId).get();
			modelMap.addAttribute("contrato", contrato);
			modelMap.addAttribute("tiposG", contrato.getVeterinario().getTiposGanado());
			modelMap.addAttribute("expGs", this.expGanaderaService.findExpGanaderaByGanaderoId(false));
			modelMap.addAttribute("veterinario", vet);
			return ContratoController.VISTA_CREAR_CONTRATO_FORM;
		} else {
			this.contratoService.saveContrato(contrato);
			modelMap.addAttribute("message", "!Tu petición de contrato ha sido enviada con éxito!");
			view = "redirect:/ganadero/explotacion-ganadera/" + contrato.getExplotacionGanadera().getId() + "/contrato/list";
		}
		return view;
	}

	@GetMapping(value = "/contrato/list")
	public String listarContrato(final ModelMap modelMap) {
		String view = "contrato/listarContratos";
		modelMap.addAttribute("contratos", this.contratoService.findContratoByVeterinarioId());
		return view;
	}

	@GetMapping(value = "/contrato/{contratoId}/show")
	public String showContrato(final ModelMap modelMap, @PathVariable("contratoId") final int contratoId) {
		String view = "contrato/showContrato";
		Optional<Contrato> contrato = this.contratoService.findContratoById(contratoId);
		modelMap.addAttribute("contratoMostrado", contrato.get());
		return view;
	}

	@GetMapping(value = "/contrato/{contratoId}/accept")
	public String aceptarContrato(final ModelMap modelMap, @PathVariable("contratoId") final int contratoId) {
		String view = this.listarContrato(modelMap);
		Contrato contrato = this.contratoService.findContratoById(contratoId).get();
		if (contrato.getEstado() == TipoEstadoContrato.PENDIENTE) {
			contrato.setEstado(TipoEstadoContrato.ACEPTADO);
			this.contratoService.saveContrato(contrato);
			view = this.listarContrato(modelMap);
		} else {
			view = "exception";
		}
		return view;
	}

	@GetMapping(value = "/contrato/{contratoId}/refuse")
	public String rechazarContrato(final ModelMap modelMap, @PathVariable("contratoId") final int contratoId) {
		String view = this.listarContrato(modelMap);
		Contrato contrato = this.contratoService.findContratoById(contratoId).get();
		if (contrato.getEstado() == TipoEstadoContrato.PENDIENTE) {
			contrato.setEstado(TipoEstadoContrato.RECHAZADO);
			this.contratoService.saveContrato(contrato);
			view = this.listarContrato(modelMap);
		} else {
			view = "exception";
		}
		return view;
	}

	@GetMapping(value = "/contrato/{contratoId}/conclude")
	public String finalizarContrato(final ModelMap modelMap, @PathVariable("contratoId") final int contratoId) {
		String view = "exception";
		Contrato contrato = this.contratoService.findContratoById(contratoId).get();
		if (contrato.getEstado() == TipoEstadoContrato.ACEPTADO) {
			contrato.setEstado(TipoEstadoContrato.FINALIZADO);
			this.contratoService.saveContrato(contrato);
			//Comprobamos si es veterinario para mostrarle la vista de contratos del veterinario
			if (this.vetService.findVeterinarioByLogedUser() != null) {
				view = this.listarContrato(modelMap);
				//Y si no es veterinario tiene que ser ganadero, por lo que le mostramos la vista de los contratos de la explotacion ganadera
			} else {
				view = this.listarContratosExplotacionGanadera(modelMap, contrato.getExplotacionGanadera().getId());
			}
		} else {
			view = "exception";
		}
		return view;
	}

	@GetMapping(value = "/ganadero/explotacion-ganadera/{expGanaderaId}/contrato/list")
	public String listarContratosExplotacionGanadera(final ModelMap modelMap, @PathVariable("expGanaderaId") final int expGanaderaId) {
		String view = "contrato/listarContratos";
		modelMap.addAttribute("contratos", this.contratoService.findAllContratoByExpGanaderaId(expGanaderaId));
		return view;
	}

	@GetMapping(value = "/contrato/{contratoId}/edit")
	public String editarContrato(final ModelMap modelMap, @PathVariable("contratoId") final int contratoId) {
		String view = "contrato/updateContrato";
		Contrato contratoAuxiliar = this.contratoService.findContratoById(contratoId).get();
		modelMap.addAttribute("contratoAuxiliar", contratoAuxiliar);
		List<TiposGanado> tiposGanadoContrato = contratoAuxiliar.getGanados();
		List<TiposGanado> tiposGanadoVeterinarioContratado = contratoAuxiliar.getVeterinario().getTiposGanado();
		tiposGanadoVeterinarioContratado.removeAll(tiposGanadoContrato);
		if (tiposGanadoVeterinarioContratado.isEmpty()) {
			modelMap.addAttribute("message", "El contrato ya contiene todo los tipos de ganado que puede llevar el veterinario contratado");
			view = this.showContrato(modelMap, contratoAuxiliar.getId());
		} else {
			modelMap.addAttribute("contratoAuxiliar", contratoAuxiliar);
			modelMap.addAttribute("nuevosGanadosContratoPosibles", tiposGanadoVeterinarioContratado);
		}
		return view;
	}

	@PostMapping(value = "/contrato/{contratoId}/edit")
	public String salvarContratoEditado(final Contrato contratoAuxiliar, final ModelMap modelMap, @PathVariable("contratoId") final int contratoId) {
		String view = "contrato/listarContratos";
		//No hay que comprobar si hay errores, porque simplemente es seleccionar parámetros de una lista.
		//El formulario solo se enviará si seleccionas elementos de esa lista, y eso nunca da error
		Contrato contratoAEditar = this.contratoService.findContratoById(contratoId).get();
		contratoAEditar.getGanados().addAll(contratoAuxiliar.getGanados());
		this.contratoService.saveContrato(contratoAEditar);
		view = this.showContrato(modelMap, contratoAEditar.getId());
		return view;
	}

}
