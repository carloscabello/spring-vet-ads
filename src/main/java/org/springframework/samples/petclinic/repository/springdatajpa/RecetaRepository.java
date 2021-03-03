
package org.springframework.samples.petclinic.repository.springdatajpa;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.model.Receta;

public interface RecetaRepository extends CrudRepository<Receta, Integer> {

	@Query("select c from Receta c where c.cita.contrato.id = ?1 order by c.fechaRealizacion asc")
	Iterable<Receta> findRecetasByContratoId(Integer contratoId);

	@Query("select c from Receta c where c.cita.id = ?1 order by c.fechaRealizacion asc")
	Iterable<Receta> findRecetasByCitaId(Integer citaId);

	@Query("select c from Receta c where c.cita.contrato.explotacionGanadera.ganadero.id = ?1 order by c.fechaRealizacion desc")
	Iterable<Receta> findRecetasByGanaderoId(Integer ganaderoId);

	@Query("select c from Receta c where c.cita.contrato.veterinario.id = ?1 order by c.fechaRealizacion desc")
	Iterable<Receta> findRecetasByVetId(Integer vetId);

	@Query("select r from Receta r where r.cita.contrato.id = ?1 and r.esFacturado = false")
	Iterable<Receta> findRecetasByContratoIdAndNoEsFacturado(int contratoId);

}
