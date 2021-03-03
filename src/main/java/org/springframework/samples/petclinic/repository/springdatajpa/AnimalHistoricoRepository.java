
package org.springframework.samples.petclinic.repository.springdatajpa;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.model.AnimalHistorico;

public interface AnimalHistoricoRepository extends CrudRepository<AnimalHistorico, Integer> {

	@Query("Select a from AnimalHistorico a where a.animal.id = ?1")
	Optional<AnimalHistorico> findAnimalHistoricoByAnimalId(Integer animalId);

}
