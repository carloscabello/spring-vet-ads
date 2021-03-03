
package org.springframework.samples.petclinic.service;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.forms.RecetaForm;
import org.springframework.samples.petclinic.model.LineaReceta;
import org.springframework.samples.petclinic.model.Producto;
import org.springframework.samples.petclinic.model.Receta;
import org.springframework.samples.petclinic.repository.springdatajpa.LineaRecetaRepository;
import org.springframework.stereotype.Service;

@Service
public class LineaRecetaService {

	@Autowired
	private LineaRecetaRepository	lineaRecetaRepository;

	@Autowired
	private ProductoService			productoService;


	@Autowired
	public LineaRecetaService(final LineaRecetaRepository lineaRecetaRepository) {
		this.lineaRecetaRepository = lineaRecetaRepository;
	}

	public Optional<LineaReceta> findLineaRecetaById(final int lineaRecetaId) throws DataAccessException {
		return this.lineaRecetaRepository.findById(lineaRecetaId);
	}

	public Iterable<LineaReceta> findLineasRecetaByRecetaId(final int recetaId) {
		return this.lineaRecetaRepository.findLineaRecetasByRecetaId(recetaId);
	}

	public Double getPrecioTotalRecetaByRecetaId(final Integer recetaId) {
		return this.lineaRecetaRepository.getPrecioTotalRecetaByRecetaId(recetaId);
	}

	@Transactional
	public void saveLineaReceta(final LineaReceta lineaReceta) throws DataAccessException {
		this.lineaRecetaRepository.save(lineaReceta);
	}

	@Transactional
	public void deleteLineaReceta(final LineaReceta lineaReceta) throws DataAccessException {
		this.lineaRecetaRepository.delete(lineaReceta);
	}

	public void reconstructAndSaveLineasReceta(final RecetaForm recetaForm, final Receta receta) {
		int aux = 0;
		for (Integer productoId : recetaForm.getProductoId()) {
			Integer cantidad = recetaForm.getCantidad().get(aux);
			aux++;
			if (cantidad != 0) {
				Producto producto = this.productoService.findProductoById(productoId).get();
				LineaReceta linea = new LineaReceta();
				linea.setCantidad(cantidad);
				Double precioVenta = producto.getPrecio();
				linea.setPrecioVenta(precioVenta);
				linea.setProducto(producto);
				linea.setReceta(receta);
				this.saveLineaReceta(linea);

			}
		}
	}

	public Integer numeroDeLineasRecetaByProductoId(final Integer productoId) {
		return this.lineaRecetaRepository.numeroDeLineasRecetaByProductoId(productoId);
	}

}
