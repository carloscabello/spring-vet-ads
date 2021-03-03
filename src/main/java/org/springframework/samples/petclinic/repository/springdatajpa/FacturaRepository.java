
package org.springframework.samples.petclinic.repository.springdatajpa;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.model.Factura;

public interface FacturaRepository extends CrudRepository<Factura, Integer> {

	@Query("select f from Factura f where f.contrato.explotacionGanadera.ganadero.id = ?1")
	List<Factura> findFacturasByGanaderoIdBeingGanadero(int ganaderoId);

	@Query("select f from Factura f where f.contrato.id = ?1")
	List<Factura> findFacturasByContratoId(int contratoId);

	@Query("select sum(c.cantidad * c.precioVenta) from LineaFactura c where c.factura.id = ?1")
	Double getPrecioTotalFacturaByFacturaid(int facturaId);

}
