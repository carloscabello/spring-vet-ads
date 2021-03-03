
package org.springframework.samples.petclinic.repository.springdatajpa;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.model.LineaReceta;

public interface LineaRecetaRepository extends CrudRepository<LineaReceta, Integer> {

	@Query("select c from LineaReceta c where c.receta.id = ?1 order by c.id asc")
	Iterable<LineaReceta> findLineaRecetasByRecetaId(Integer recetaId);

	@Query("select c from LineaReceta c where c.producto.id = ?1 order by c.id asc")
	Iterable<LineaReceta> findLineaRecetasByProductoId(Integer productoId);

	@Query("select c from LineaReceta c where c.producto.id = ?1 and c.receta.id = ?2 order by c.id asc")
	Iterable<LineaReceta> findLineaRecetasByProductoAndRecetaId(Integer productoId, Integer recetaId);

	@Query("select count(c) from LineaReceta c where c.producto.id = ?1")
	Integer numeroDeLineasRecetaByProductoId(Integer productoId);

	@Query("select sum(c.cantidad * c.precioVenta) from LineaReceta c where c.receta.id = ?1")
	Double getPrecioTotalRecetaByRecetaId(Integer receta);
}
