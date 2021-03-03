
package org.springframework.samples.petclinic.web;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.forms.FacturaForm;
import org.springframework.samples.petclinic.forms.SelectContratoForm;
import org.springframework.samples.petclinic.model.Contrato;
import org.springframework.samples.petclinic.model.Factura;
import org.springframework.samples.petclinic.model.LineaFactura;
import org.springframework.samples.petclinic.model.Producto;
import org.springframework.samples.petclinic.model.Receta;
import org.springframework.samples.petclinic.service.ContratoService;
import org.springframework.samples.petclinic.service.FacturaService;
import org.springframework.samples.petclinic.service.GanaderoService;
import org.springframework.samples.petclinic.service.LineaFacturaService;
import org.springframework.samples.petclinic.service.ProductoService;
import org.springframework.samples.petclinic.service.RecetaService;
import org.springframework.samples.petclinic.service.VetService;
import org.springframework.samples.petclinic.util.FacturaFormValidator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class FacturaController {

	@Autowired
	private FacturaService		facturaService;

	@Autowired
	private LineaFacturaService	lineaFacturaService;

	@Autowired
	private ContratoService		contratoService;

	@Autowired
	private RecetaService		recetaService;

	@Autowired
	private GanaderoService		ganaderoService;

	@Autowired
	private ProductoService		productoService;

	@Autowired
	private VetService			vetService;


	@InitBinder("facturaForm")
	public void initBinder(final WebDataBinder dataBinder) {
		dataBinder.addValidators(new FacturaFormValidator());
	}

	@GetMapping(value = "/factura/select")
	public String seleccionarContrato(final ModelMap modelMap) {
		String view = "factura/selectContrato";
		Iterable<Contrato> contratos = this.contratoService.findAllContratoVigentesByLoguedVeterinario();
		SelectContratoForm contratoForm = new SelectContratoForm();
		modelMap.addAttribute("contratos", contratos);
		modelMap.addAttribute("selectContrato", contratoForm);
		return view;
	}

	@PostMapping(value = "/factura/select")
	public String mostrarFacturasGanadero(@Valid final SelectContratoForm contratoForm, final BindingResult result, final ModelMap modelMap) {
		String view = "factura/selectContrato";
		if (result.hasErrors()) {
			modelMap.addAttribute("selectContrato", contratoForm);
		} else {
			view = "redirect:/contrato/" + contratoForm.getContrato().getId() + "/factura/list";
		}
		return view;
	}

	@GetMapping(value = "/ganadero/factura/list")
	public String listarFacturasGanadero(final ModelMap modelMap) {
		String view = "factura/listarFacturas";
		List<Factura> facturas = this.facturaService.findFacturasByGanaderoIdBeingGanadero(this.ganaderoService.findGanaderoByLogedUser().getId());
		modelMap.addAttribute("facturas", facturas);
		return view;
	}

	@GetMapping(value = "/contrato/{contratoId}/factura/list")
	public String listarFacturasVeterinario(final ModelMap modelMap, @PathVariable("contratoId") final int contratoId) {
		String view = "factura/listarFacturas";
		List<Factura> facturas = this.facturaService.findFacturasByContratoId(contratoId);
		modelMap.addAttribute("facturas", facturas);
		modelMap.addAttribute("contratoId", contratoId);
		return view;
	}

	@GetMapping(value = "/factura/{facturaId}/show")
	public String showReceta(final ModelMap modelMap, @PathVariable("facturaId") final int facturaId) {
		String view = "factura/showFacturaForm";
		Optional<Factura> facturaOptional = this.facturaService.findFacturaById(facturaId);
		if (facturaOptional.isPresent()) {
			Factura factura = facturaOptional.get();
			Iterable<LineaFactura> lineasFactura = this.lineaFacturaService.findLineasFacturaByFacturaId(facturaId);
			Double precioTotal = this.facturaService.getPrecioTotalFacturaByFacturaId(facturaId);
			modelMap.addAttribute("factura", factura);
			modelMap.addAttribute("lineasFactura", lineasFactura);
			modelMap.addAttribute("precioTotalFactura", precioTotal);
		} else {
			modelMap.addAttribute("message", "La Factura seleccionada no existe.");
			if (this.vetService.findVeterinarioByLogedUser() != null) {
				view = this.listarFacturasVeterinario(modelMap, (int) modelMap.getAttribute("contratoId"));
			} else {
				//Si no es veterinario, tiene que ser ganadero si o si. Son los únicos usuarios que pueden entrar
				//en esta página.
				view = this.listarFacturasGanadero(modelMap);
			}
		}
		return view;
	}

	@GetMapping(value = "/contrato/{contratoId}/factura/new")
	public String crearFactura(final ModelMap modelMap, @PathVariable("contratoId") final int contratoId) {
		String view = "factura/createFactura";
		Iterable<Receta> recetas = this.recetaService.findRecetasByContratoIdAndNoEsFacturado(contratoId);
		Iterable<Producto> productos = this.productoService.findAllProductosConExistenciasByVeterinarioIdNoNecesitanReceta();
		if (((List<Producto>) productos).isEmpty() && ((List<Receta>) recetas).isEmpty()) {
			modelMap.addAttribute("message", "No tienes productos existentes o con existencias que no necesitan receta ni recetas pendientes de pago");
			return this.listarFacturasVeterinario(modelMap, contratoId);
		} else {
			Factura factura = new Factura();
			modelMap.addAttribute("productos", productos);
			modelMap.addAttribute("recetas", recetas);
			modelMap.addAttribute("factura", factura);
		}
		return view;
	}

	@PostMapping(value = "/contrato/{contratoId}/factura/new")
	public String salvarFactura(final ModelMap modelMap, @Valid final FacturaForm facturaForm, final BindingResult result, @PathVariable("contratoId") final int contratoId) {
		if (result.hasErrors()) {
			if (result.getFieldError("recetas") != null) {
				modelMap.addAttribute("message", result.getFieldError("recetas").getDefaultMessage());
			}
			Iterable<Receta> recetas = this.recetaService.findRecetasByContratoIdAndNoEsFacturado(contratoId);
			Iterable<Producto> productos = this.productoService.findAllProductosConExistenciasByVeterinarioIdNoNecesitanReceta();
			modelMap.addAttribute("productos", productos);
			modelMap.addAttribute("recetas", recetas);
			modelMap.addAttribute("facturaForm", facturaForm);
			return "factura/createFactura";
		} else {
			Factura factura = this.facturaService.reconstruct(facturaForm, contratoId);
			List<Receta> recetas = factura.getRecetas();
			if (recetas != null) {
				for (Receta r : recetas) {
					this.recetaService.marcarRecetasComoFacturadas(r);
				}
			}
			this.facturaService.saveFactura(factura);
			if (facturaForm.getProductoId() != null) {
				this.lineaFacturaService.reconstruct(facturaForm, factura);
			}
			modelMap.addAttribute("message", "¡Factura creada con éxito!");
		}
		System.out.println("el id dle contrato es" + contratoId);
		return this.listarFacturasVeterinario(modelMap, contratoId);
	}

	@GetMapping(value = "/contrato/{contratoId}/factura/{facturaId}/edit")
	public String marcarComoPagadaFactura(final ModelMap modelMap, @PathVariable("contratoId") final int contratoId, @PathVariable("facturaId") final int facturaId) {
		Optional<Factura> facturaO = this.facturaService.findFacturaById(facturaId);
		if (facturaO.isPresent()) {
			Factura factura = facturaO.get();
			if (factura.getEsPagado() == true) {
				modelMap.addAttribute("message", "La factura que se está intentado marcar como pagada ya lo está");
			} else {
				this.facturaService.marcarComoPagado(factura);
				modelMap.addAttribute("message", "¡Factura marcada como pagada con éxito!");
			}
		} else {
			modelMap.addAttribute("message", "La factura que se está intentado marcar como pagada no existe");
		}
		return this.listarFacturasVeterinario(modelMap, contratoId);
	}

}
