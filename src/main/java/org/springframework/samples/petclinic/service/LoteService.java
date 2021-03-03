
package org.springframework.samples.petclinic.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Lote;
import org.springframework.samples.petclinic.repository.springdatajpa.LoteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LoteService {

	@Autowired
	private LoteRepository loteRepository;


	public LoteService(final LoteRepository loteRepository) {
		this.loteRepository = loteRepository;
	}

	public Optional<Lote> findLoteById(final int id) {
		return this.loteRepository.findById(id);
	}

	@Transactional
	public void saveLote(final Lote lote) throws DataAccessException {
		lote.setEsArchivado(false);
		this.loteRepository.save(lote);
	}

	public void archivarLote(final Lote lote) throws DataAccessException {
		lote.setEsArchivado(true);
		this.loteRepository.save(lote);
	}

	@Transactional
	public Iterable<Lote> findAllLoteByExpIdYGanadoId(final Integer expId, final Integer tipoGanadoId, final Boolean esArchivado) {
		return this.loteRepository.findAllLoteByExpIdYGanadoId(expId, tipoGanadoId, esArchivado);
	}

	@Transactional
	public Integer numeroMachosByLoteId(final Integer loteId, final Boolean esArchivado) {
		return this.loteRepository.numeroMachosByLoteId(loteId, esArchivado);
	}

	@Transactional
	public Integer numeroHembrasByLoteId(final Integer loteId, final Boolean esArchivado) {
		return this.loteRepository.numeroHembrasByLoteId(loteId, esArchivado);
	}

	@Transactional
	public Optional<Lote> findLoteByIdentificadorLoteAndExpIdAndEsArchivado(final String identificadorLote, final Integer explotacionGanaderaId, final Boolean esArchivado) {
		return this.loteRepository.findLoteByIdentificadorLoteAndExpIdAndEsArchivado(identificadorLote, explotacionGanaderaId, esArchivado);
	}

	@Transactional
	public Iterable<Lote> findAllLoteFromAnimalByExpIdAndGanadoIdAndEsArchivado(final Integer expId, final Integer tipoGanadoId, final Boolean esArchivado) {
		return this.loteRepository.findAllLoteFromAnimalByExpIdAndGanadoIdAndEsArchivado(expId, tipoGanadoId, esArchivado);
	}

}
