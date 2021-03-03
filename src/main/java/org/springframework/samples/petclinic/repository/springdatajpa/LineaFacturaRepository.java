
package org.springframework.samples.petclinic.repository.springdatajpa;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.model.LineaFactura;

public interface LineaFacturaRepository extends CrudRepository<LineaFactura, Integer> {

	@Query("select count(lf) from LineaFactura lf where lf.producto.id = ?1")
	Integer numeroDeLineasFacturaByProductoId(Integer productoId);

	@Query("select c from LineaFactura c where c.factura.id = ?1 order by c.id asc")
	Iterable<LineaFactura> findLineasFacturaByFacturaId(Integer id);

}
