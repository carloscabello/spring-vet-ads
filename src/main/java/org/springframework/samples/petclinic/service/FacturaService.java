
package org.springframework.samples.petclinic.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.forms.FacturaForm;
import org.springframework.samples.petclinic.model.Factura;
import org.springframework.samples.petclinic.model.Receta;
import org.springframework.samples.petclinic.repository.springdatajpa.FacturaRepository;
import org.springframework.stereotype.Service;

@Service
public class FacturaService {

	@Autowired
	private FacturaRepository	facturaRepository;

	@Autowired
	private LineaRecetaService	lineaRecetaService;

	@Autowired
	private ContratoService		contratoService;


	public void saveFactura(final Factura f) {
		this.facturaRepository.save(f);
	}

	public List<Factura> findFacturasByGanaderoIdBeingGanadero(final int ganaderoId) {
		return this.facturaRepository.findFacturasByGanaderoIdBeingGanadero(ganaderoId);
	}

	public List<Factura> findFacturasByContratoId(final int contratoId) {
		return this.facturaRepository.findFacturasByContratoId(contratoId);
	}

	public Optional<Factura> findFacturaById(final int facturaId) {
		return this.facturaRepository.findById(facturaId);
	}

	public void marcarComoPagado(final Factura factura) {
		factura.setEsPagado(true);
		this.facturaRepository.save(factura);
	}

	public Factura reconstruct(final FacturaForm facturaForm, final int contratoId) {
		Factura factura = new Factura();
		factura.setEsPagado(false);
		factura.setFechaEmision(java.util.Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
		factura.setContrato(this.contratoService.findContratoById(contratoId).get());
		factura.setRecetas(facturaForm.getRecetas());
		return factura;
	}

	public Double getPrecioTotalFacturaByFacturaId(final Integer facturaId) {
		Double res = 0.;

		Factura fac = this.facturaRepository.findById(facturaId).get();

		if (this.facturaRepository.getPrecioTotalFacturaByFacturaid(facturaId) != null) {
			res += this.facturaRepository.getPrecioTotalFacturaByFacturaid(facturaId);
		}
		for (Receta R : fac.getRecetas()) {
			res += this.lineaRecetaService.getPrecioTotalRecetaByRecetaId(R.getId());
		}
		return res;
	}

}
