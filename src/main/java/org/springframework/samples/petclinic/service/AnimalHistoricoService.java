
package org.springframework.samples.petclinic.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.AnimalHistorico;
import org.springframework.samples.petclinic.repository.springdatajpa.AnimalHistoricoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AnimalHistoricoService {

	@Autowired
	private AnimalHistoricoRepository animalHistoricoRepository;


	@Autowired
	public AnimalHistoricoService(final AnimalHistoricoRepository animalHistoricoRepository) {
		this.animalHistoricoRepository = animalHistoricoRepository;
	}

	public void deleteAnimalHistorico(final AnimalHistorico animalHistorico) {
		this.animalHistoricoRepository.delete(animalHistorico);
	}

	@Transactional
	public void saveAnimalHistorico(final AnimalHistorico animalHistorico) throws DataAccessException {
		this.animalHistoricoRepository.save(animalHistorico);
	}

	@Transactional
	public Optional<AnimalHistorico> findAnimalHistoricoByAnimalId(final Integer animalId) {
		return this.animalHistoricoRepository.findAnimalHistoricoByAnimalId(animalId);
	}

}
