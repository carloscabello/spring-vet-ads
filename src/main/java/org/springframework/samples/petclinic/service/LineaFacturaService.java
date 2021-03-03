
package org.springframework.samples.petclinic.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.forms.FacturaForm;
import org.springframework.samples.petclinic.model.Factura;
import org.springframework.samples.petclinic.model.LineaFactura;
import org.springframework.samples.petclinic.model.Producto;
import org.springframework.samples.petclinic.repository.springdatajpa.LineaFacturaRepository;
import org.springframework.stereotype.Service;

@Service
public class LineaFacturaService {

	@Autowired
	private LineaFacturaRepository	lineaFacturaRepository;

	@Autowired
	private ProductoService			productoService;


	public List<LineaFactura> reconstruct(final FacturaForm facturaForm, final Factura factura) {
		int aux = 0;
		List<LineaFactura> lineasFactura = new ArrayList<>();
		for (Integer productoId : facturaForm.getProductoId()) {
			Integer cantidad = facturaForm.getCantidad().get(aux);
			aux++;
			if (cantidad != 0) {
				Producto producto = this.productoService.findProductoById(productoId).get();
				LineaFactura lineaFactura = new LineaFactura();
				lineaFactura.setCantidad(cantidad);
				Double precioVenta = producto.getPrecio();
				lineaFactura.setPrecioVenta(precioVenta);
				lineaFactura.setProducto(producto);
				lineaFactura.setFactura(factura);
				this.saveLineaFactura(lineaFactura);
				lineasFactura.add(lineaFactura);

			}
		}
		return lineasFactura;
	}

	private void saveLineaFactura(final LineaFactura lineaFactura) {
		this.lineaFacturaRepository.save(lineaFactura);

	}

	public Integer numeroDeLineasFacturaByProductoId(final Integer productoId) {
		return this.lineaFacturaRepository.numeroDeLineasFacturaByProductoId(productoId);
	}

	public Iterable<LineaFactura> findLineasFacturaByFacturaId(final int id) {
		return this.lineaFacturaRepository.findLineasFacturaByFacturaId(id);
	}
}
