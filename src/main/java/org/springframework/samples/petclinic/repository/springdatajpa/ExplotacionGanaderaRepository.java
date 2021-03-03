
package org.springframework.samples.petclinic.repository.springdatajpa;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.model.ExplotacionGanadera;

public interface ExplotacionGanaderaRepository extends CrudRepository<ExplotacionGanadera, Integer> {

	//Devuelve todas las explotaciones ganaderas de un ganadero, según si estas
	//son archivadas o no.
	@Query("select e from ExplotacionGanadera e where e.ganadero.id = ?1 and e.esArchivado = ?2")
	Iterable<ExplotacionGanadera> findAllExpGanaderaByGanaderoId(Integer id, Boolean esArchivado);

	//Devuelve una explotación ganadera según su número de registro
	@Query("select e from ExplotacionGanadera e where e.numeroRegistro = ?1")
	Optional<ExplotacionGanadera> findExpGanaderaByNumeroRegistro(String numeroRegistro);

	@Query("select e.explotacionGanadera from Contrato e where e.veterinario.id = ?1 and e.explotacionGanadera.esArchivado = 'false' and e.estado = 0 and CURRENT_DATE < e.fechaFinal")
	Iterable<ExplotacionGanadera> findAllExpGanaderaByVeterinarioId(Integer vetId);
}
