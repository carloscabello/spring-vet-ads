
package org.springframework.samples.petclinic.web;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.forms.RecetaForm;
import org.springframework.samples.petclinic.model.Cita;
import org.springframework.samples.petclinic.model.LineaReceta;
import org.springframework.samples.petclinic.model.Producto;
import org.springframework.samples.petclinic.model.Receta;
import org.springframework.samples.petclinic.model.TipoEstadoCita;
import org.springframework.samples.petclinic.service.CitaService;
import org.springframework.samples.petclinic.service.LineaRecetaService;
import org.springframework.samples.petclinic.service.ProductoService;
import org.springframework.samples.petclinic.service.RecetaService;
import org.springframework.samples.petclinic.service.VetService;
import org.springframework.samples.petclinic.util.RecetaFormValidator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RecetaController {

	@Autowired
	private RecetaService		recetaService;

	@Autowired
	private LineaRecetaService	lineaRecetaService;

	@Autowired
	private CitaService			citaService;

	@Autowired
	private ProductoService		productoService;

	@Autowired
	private VetService			vetService;


	public RecetaController(final RecetaService recetaService, final LineaRecetaService lineaRecetaService, final CitaService citaService, final ProductoService productoService) {

		this.recetaService = recetaService;
		this.lineaRecetaService = lineaRecetaService;
		this.citaService = citaService;
		this.productoService = productoService;
	}

	@InitBinder("recetaForm")
	public void initBinder(final WebDataBinder dataBinder) {
		dataBinder.addValidators(new RecetaFormValidator());
	}

	@GetMapping(value = "/veterinario/receta/new")
	public String createReceta(final ModelMap modelMap) {
		String view = "receta/createRecetaForm";

		List<Cita> citas = (List<Cita>) this.citaService.findCitasByLoggedVeterinarioAndFilters(TipoEstadoCita.ACEPTADA, RecetaController.fechaActualMenosUnMes(), RecetaController.fechaActual());
		if (this.productoService.numeroProductosConExistenciasByVeterinarioId() == 0) {
			modelMap.addAttribute("message", "No tienes productos con existencias para añadir a la receta. Ve a la sección de almacén y añade un nuevo producto o añade existencias a los existentes");
			view = this.listRecetasVeterinario(modelMap);
		} else if (citas.isEmpty() == true) {
			modelMap.addAttribute("message", "No tienes citas sobre las que poder realizar una receta. Para poder realizar una receta para una cita esta debe estar aceptada y estar en el rango de tiempo del momento actual hasta 1 mes hacia atrás");
			view = this.listRecetasVeterinario(modelMap);
		} else {
			Iterable<Producto> productos = this.productoService.findAllProductosByLoguedVeterinario();
			modelMap.addAttribute("recetaForm", new RecetaForm());
			modelMap.addAttribute("productos", productos);
			modelMap.addAttribute("citas", citas);
		}
		return view;
	}

	@PostMapping(value = "/veterinario/receta/new")
	public String guardarReceta(@Valid final RecetaForm recetaForm, final BindingResult result, final ModelMap modelMap) {

		String res = "redirect:/veterinario/receta/list";
		if (result.hasErrors()) {
			Iterable<Producto> productos = this.productoService.findAllProductosByLoguedVeterinario();
			Iterable<Cita> citas = this.citaService.findCitasByLoggedVeterinarioAndFilters(TipoEstadoCita.ACEPTADA, RecetaController.fechaActualMenosUnMes(), RecetaController.fechaActual());
			modelMap.addAttribute("recetaForm", recetaForm);
			modelMap.addAttribute("productos", productos);
			modelMap.addAttribute("citas", citas);
			if (result.getFieldError("cantidad") != null) {
				modelMap.addAttribute("cantidadError", result.getFieldError("cantidad").getDefaultMessage());
			}
			res = "receta/createRecetaForm";

		} else {
			Receta receta = this.recetaService.reconstruct(recetaForm);
			this.recetaService.saveReceta(receta);
			this.lineaRecetaService.reconstructAndSaveLineasReceta(recetaForm, receta);
		}

		return res;
	}

	@GetMapping(value = "/veterinario/receta/list")
	public String listRecetasVeterinario(final ModelMap modelMap) {
		Iterable<Receta> recetas = this.recetaService.findRecetasByLoggedVeterinario();
		modelMap.addAttribute("recetas", recetas);
		return "receta/listRecetaForm";
	}

	@GetMapping(value = "/ganadero/receta/list")
	public String listRecetasGanadero(final ModelMap modelMap) {
		Iterable<Receta> recetas = this.recetaService.findRecetasByLoggedGanadero();
		modelMap.addAttribute("recetas", recetas);
		return "receta/listRecetaForm";
	}

	@GetMapping(value = "/receta/{recetaId}/show")
	public String showReceta(final ModelMap modelMap, @PathVariable("recetaId") final int recetaId) {
		String view = "receta/showRecetaForm";
		Optional<Receta> recetaOptional = this.recetaService.findRecetaById(recetaId);
		if (recetaOptional.isPresent()) {
			Receta receta = recetaOptional.get();
			Iterable<LineaReceta> lineasReceta = this.lineaRecetaService.findLineasRecetaByRecetaId(receta.getId());
			Double precioTotal = this.lineaRecetaService.getPrecioTotalRecetaByRecetaId(receta.getId());
			modelMap.addAttribute("receta", receta);
			modelMap.addAttribute("lineasReceta", lineasReceta);
			modelMap.addAttribute("precioTotalReceta", precioTotal);
		} else {
			modelMap.addAttribute("message", "La receta seleccionada no existe.");
			if (this.vetService.findVeterinarioByLogedUser() != null) {
				view = this.listRecetasVeterinario(modelMap);
			} else {
				//Si no es veterinario, tiene que ser ganadero si o si. Son los únicos usuarios que pueden entrar
				//en esta página.
				view = this.listRecetasGanadero(modelMap);
			}
		}
		return view;
	}

	//Métodos auxiliares
	public static Date fechaActual() {
		Calendar calendario = Calendar.getInstance();
		return calendario.getTime();
	}

	public static Date fechaActualMenosUnMes() {
		Calendar calendario = Calendar.getInstance();
		calendario.add(Calendar.MONTH, -1);
		return calendario.getTime();
	}

}
