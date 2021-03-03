
package org.springframework.samples.petclinic.repository.springdatajpa;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.model.Producto;

public interface ProductoRepository extends CrudRepository<Producto, Integer> {

	@Query("Select a from Producto a where a.veterinario.id = ?1")
	Iterable<Producto> findAllProductosByVeterinarioId(Integer vetId);

	@Query("Select a from Producto a where a.name = ?1 and a.veterinario.id = ?2")
	Optional<Producto> findProductoByNameYVetId(String name, Integer vetId);

	@Query("Select count(a) from Producto a where a.veterinario.id = ?1 and cantidad > 0")
	Integer numeroProductosConExistenciasByVeterinarioId(Integer vetId);

	@Query("Select p from Producto p where p.necesitaReceta=false and p.veterinario.id =?1 and p.cantidad > 0")
	Iterable<Producto> findAllProductosConExistenciasByVeterinarioIdAndNoNecesitanReceta(int veterinarioId);

}
