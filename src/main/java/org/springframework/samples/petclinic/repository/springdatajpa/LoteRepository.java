
package org.springframework.samples.petclinic.repository.springdatajpa;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.model.Lote;

public interface LoteRepository extends CrudRepository<Lote, Integer> {

	//Devuelve todos los lotes de una explotación ganadera según el tipo de
	//ganado y si son archivados o no
	@Query("select l from Lote l where l.explotacionGanadera.id = ?1 and l.tipoGanado.id = ?2 and l.esArchivado = ?3")
	Iterable<Lote> findAllLoteByExpIdYGanadoId(Integer expId, Integer tipoGanadoId, Boolean esArchivado);

	//Cuenta el número de machos de un lote
	@Query("select count (a) from Animal a where a.lote.id = ?1 and a.sexo=1 and a.esArchivado = ?2")
	Integer numeroMachosByLoteId(Integer loteId, Boolean esArchivado);

	//Cuenta el número de hembras de un lote
	@Query("select count (a) from Animal a where a.lote.id = ?1 and a.sexo=0 and a.esArchivado = ?2")
	Integer numeroHembrasByLoteId(Integer loteId, Boolean esArchivado);

	@Query("select l from Lote l where l.identificadorLote = ?1 and l.explotacionGanadera.id = ?2 and l.esArchivado = ?3")
	Optional<Lote> findLoteByIdentificadorLoteAndExpIdAndEsArchivado(String identificadorLote, Integer explotacionGanaderaId, Boolean esArchivado);

	@Query("select distinct a.lote from Animal a where a.explotacionGanadera.id = ?1 and a.tipoGanado.id = ?2 and a.esArchivado = ?3")
	Iterable<Lote> findAllLoteFromAnimalByExpIdAndGanadoIdAndEsArchivado(Integer expId, Integer tipoGanadoId, Boolean esArchivado);
}
