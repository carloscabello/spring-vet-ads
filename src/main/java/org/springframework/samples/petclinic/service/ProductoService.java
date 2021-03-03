
package org.springframework.samples.petclinic.service;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Producto;
import org.springframework.samples.petclinic.model.Veterinario;
import org.springframework.samples.petclinic.repository.springdatajpa.ProductoRepository;
import org.springframework.stereotype.Service;

@Service
public class ProductoService {

	@Autowired
	private ProductoRepository	productoRepository;

	@Autowired
	private VetService			veterinarioService;


	@Autowired
	public ProductoService(final ProductoRepository productoRepository) {
		this.productoRepository = productoRepository;
	}

	public Iterable<Producto> findAllProductosByLoguedVeterinario() {
		Veterinario vet = this.veterinarioService.findVeterinarioByLogedUser();
		return this.productoRepository.findAllProductosByVeterinarioId(vet.getId());
	}

	public void saveProducto(@Valid final Producto producto) {
		this.productoRepository.save(producto);
	}

	public void deleteProducto(final Producto producto) {
		this.productoRepository.delete(producto);
	}

	public Optional<Producto> findProductoByNameYVetId(final String name) {
		Veterinario vet = this.veterinarioService.findVeterinarioByLogedUser();
		return this.productoRepository.findProductoByNameYVetId(name, vet.getId());
	}

	public Optional<Producto> findProductoById(final int productoId) {
		return this.productoRepository.findById(productoId);
	}

	public Integer numeroProductosConExistenciasByVeterinarioId() {
		Veterinario vet = this.veterinarioService.findVeterinarioByLogedUser();
		return this.productoRepository.numeroProductosConExistenciasByVeterinarioId(vet.getId());
	}

	public Iterable<Producto> findAllProductosConExistenciasByVeterinarioIdNoNecesitanReceta() {
		return this.productoRepository.findAllProductosConExistenciasByVeterinarioIdAndNoNecesitanReceta(this.veterinarioService.findVeterinarioByLogedUser().getId());
	}

}
