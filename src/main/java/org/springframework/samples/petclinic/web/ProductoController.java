
package org.springframework.samples.petclinic.web;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Producto;
import org.springframework.samples.petclinic.model.Veterinario;
import org.springframework.samples.petclinic.service.LineaFacturaService;
import org.springframework.samples.petclinic.service.LineaRecetaService;
import org.springframework.samples.petclinic.service.ProductoService;
import org.springframework.samples.petclinic.service.VetService;
import org.springframework.samples.petclinic.util.ProductoValidator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ProductoController {

	@Autowired
	private final ProductoService		productoService;

	@Autowired
	private final VetService			vetService;

	@Autowired
	private final LineaRecetaService	lineaRecetaService;

	@Autowired
	private final LineaFacturaService	lineaFacturaService;


	public ProductoController(final ProductoService productoService, final VetService vetService, final LineaRecetaService lineaRecetaService, final LineaFacturaService lineaFacturaService) {
		this.productoService = productoService;
		this.vetService = vetService;
		this.lineaRecetaService = lineaRecetaService;
		this.lineaFacturaService = lineaFacturaService;
	}

	@InitBinder
	public void setAllowedFields(final WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

	@InitBinder("producto")
	public void initBinder(final WebDataBinder dataBinder) {
		dataBinder.addValidators(new ProductoValidator(this.productoService));
	}

	@GetMapping(value = "/producto/new")
	public String crearProducto(final ModelMap modelMap) {
		String view = "producto/newProducto";
		Producto producto = new Producto();
		modelMap.addAttribute("producto", producto);
		return view;
	}

	@PostMapping(value = "/producto/new")
	public String salvarProducto(@Valid final Producto producto, final BindingResult result, final ModelMap modelMap) {
		String view = "producto/listarProductos";
		if (result.hasErrors()) {
			modelMap.addAttribute("producto", producto);
			return "producto/newProducto";
		} else {
			Veterinario vet = this.vetService.findVeterinarioByLogedUser();
			producto.setVeterinario(vet);
			if (producto.getNecesitaReceta() == null) {
				producto.setNecesitaReceta(false);
			}
			this.productoService.saveProducto(producto);
			modelMap.addAttribute("message", "!Tu producto ha sido registrado con éxito!");
			view = this.listadoProductos(modelMap);
		}
		return view;
	}

	@GetMapping(value = "/producto/list")
	public String listadoProductos(final ModelMap modelMap) {
		String view = "producto/listarProductos";
		Iterable<Producto> productos = this.productoService.findAllProductosByLoguedVeterinario();
		modelMap.addAttribute("productos", productos);
		return view;
	}

	@GetMapping(value = "/producto/{productoId}/edit")
	public String editarProducto(final ModelMap modelMap, @PathVariable("productoId") final int productoId) {
		String view = "producto/editProducto";
		Producto producto = this.productoService.findProductoById(productoId).get();
		modelMap.addAttribute(producto);
		return view;
	}

	@PostMapping(value = "/producto/{productoId}/edit")
	public String salvarProductoEditado(@Valid final Producto producto, final BindingResult result, final ModelMap modelMap, @PathVariable("productoId") final int productoId) {
		String view = "producto/listarProductos";
		Producto productoAntiguo = this.productoService.findProductoById(productoId).get();
		//Le hago un set del nombre, porque esta propiedad no se puede editar. Entonces tengo que ponerle
		//el nombre anterior que tenia para que se guarde correctamente en el sistema
		producto.setName(productoAntiguo.getName());
		if (result.hasErrors()) {
			modelMap.addAttribute("producto", producto);
			return "producto/editProducto";
		} else {
			producto.setVeterinario(productoAntiguo.getVeterinario());
			if (producto.getNecesitaReceta() == null) {
				producto.setNecesitaReceta(false);
			}
			producto.setId(productoId);
			this.productoService.saveProducto(producto);
			modelMap.addAttribute("message", "!Tu producto ha sido editado con éxito!");
			view = this.listadoProductos(modelMap);
		}
		return view;
	}

	@GetMapping(value = "/producto/{productoId}/delete")
	public String deleteVisit(@PathVariable("productoId") final int productoId, final ModelMap modelMap) {
		Optional<Producto> producto = this.productoService.findProductoById(productoId);
		if (producto.isPresent()) {
			if (this.lineaRecetaService.numeroDeLineasRecetaByProductoId(productoId) != 0 || this.lineaFacturaService.numeroDeLineasFacturaByProductoId(productoId) != 0) {
				modelMap.addAttribute("message", "El producto no puede ser borrado, porque existe una receta o una factura realizada con este producto");
			} else {
				this.productoService.deleteProducto(producto.get());
				modelMap.addAttribute("message", "Producto borrado con éxito.");
			}
		} else {
			modelMap.addAttribute("message", "Producto no encontrado.");
		}
		return this.listadoProductos(modelMap);
	}

}
