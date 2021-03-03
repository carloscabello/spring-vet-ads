
package org.springframework.samples.petclinic.repository.springdatajpa;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.model.Animal;

public interface AnimalRepository extends CrudRepository<Animal, Integer> {

	//Devuelve todos los animales de una explotación ganadera según el tipo de
	//ganado y si son archivados o no.
	//Si el lote no es archivado, aunque sea de lote, si el animal es archivado, también se mostrará
	@Query("select a from Animal a where a.explotacionGanadera.id = ?1 and a.tipoGanado.id = ?2 and a.esArchivado = ?3 and a.lote = null")
	Iterable<Animal> findAllAnimalNoLoteByExpIdYGanadoId(Integer expId, Integer tipoGanadoId, Boolean esArchivado);

	//Devuelve un animal no archivado según su identificador animal.
	@Query("select a from Animal a where a.identificadorAnimal = ?1 and a.explotacionGanadera.id = ?2 and a.esArchivado = ?3")
	Optional<Animal> findAnimalByIdentificadorAnimalAndExpIdAndEsArchivado(String identificadorAnimal, Integer expId, Boolean esArchivado);

	//Devuelve los animales de un lote según si son archivados o no.
	@Query("select a from Animal a where a.lote.id = ?1 and (?2 is null or a.esArchivado = ?2)")
	Iterable<Animal> findAllAnimalByLoteId(Integer loteId, Boolean esArchivado);

	//Devuelve todos los animales de una explotación ganadera según si son
	//archivados o no.
	@Query("select a from Animal a where a.explotacionGanadera.id = ?1 and a.esArchivado = ?2")
	Iterable<Animal> findAllAnimalByExpId(Integer expId, Boolean esArchivado);

	//Devuelve un animal según el identificador animal
	@Query("select a from Animal a where a.identificadorAnimal = ?1")
	Optional<Animal> findAnimalByAnimalIdentificadorAnimal(String identAnimal);

	@Query("select a from Animal a where a.explotacionGanadera.id = ?1 and a.tipoGanado.id = ?2 and a.esArchivado = ?3")
	Iterable<Animal> findAllAnimalByExpIdYGanadoIYEsArchivado(Integer expId, Integer tipoGanadoId, Boolean esArchivado);

}
