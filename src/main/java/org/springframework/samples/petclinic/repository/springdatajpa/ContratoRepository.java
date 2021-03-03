
package org.springframework.samples.petclinic.repository.springdatajpa;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.model.Contrato;
import org.springframework.samples.petclinic.model.TipoEstadoContrato;

public interface ContratoRepository extends CrudRepository<Contrato, Integer> {

	@Query("select c from Contrato c where c.veterinario.id=?1")
	Iterable<Contrato> findContratosByVeterinarioId(Integer id);
	
	@Query("select c from Contrato c where c.explotacionGanadera.ganadero.id=?1")
	Iterable<Contrato> findContratosByGanaderoId(Integer id);
	
	@Query("select c from Contrato c where c.explotacionGanadera.ganadero.id=?1 and (CURRENT_DATE <= c.fechaFinal) and (c.fechaInicial < CURRENT_DATE) and c.estado=0")
	Iterable<Contrato> findContratosVigentesAceptadosByGanaderoId(Integer id);

	@Query("select e from Contrato e where e.veterinario.id = ?1 and e.estado = 0 and CURRENT_DATE < e.fechaFinal")
	Iterable<Contrato> findAllContratosVigentesByVeterinarioId(Integer vetId);

	@Query("select e from Contrato e where e.explotacionGanadera.id = ?1 and e.estado = ?2")
	Iterable<Contrato> findAllContratosByExpGanaderaAndEstado(Integer expId, TipoEstadoContrato estado);

	@Query("select e from Contrato e where e.explotacionGanadera.id = ?1 and e.veterinario.id = ?2 and e.estado = ?3 and CURRENT_DATE < e.fechaFinal")
	Optional<Contrato> findAllContratosVigentesByExpGanaderaAndVeterinarioAndEstado(Integer expId, Integer vetId, TipoEstadoContrato estado);

	@Query("select e from Contrato e where e.explotacionGanadera.id = ?1")
	Iterable<Contrato> findAllContratosByExpGanaderaId(Integer expId);
}
