
package org.springframework.samples.petclinic.service;

import java.util.Formatter;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.forms.LoteForm;
import org.springframework.samples.petclinic.model.Animal;
import org.springframework.samples.petclinic.model.AnimalHistorico;
import org.springframework.samples.petclinic.model.Lote;
import org.springframework.samples.petclinic.model.TipoSexo;
import org.springframework.samples.petclinic.repository.springdatajpa.AnimalRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
public class AnimalService {

	@Autowired
	private AnimalRepository		animalRepository;

	@Autowired
	private AnimalHistoricoService	animalHistoricoService;


	@Autowired
	public AnimalService(final AnimalRepository animalRepository, final AnimalHistoricoService animalHistoricoService) {
		this.animalRepository = animalRepository;
		this.animalHistoricoService = animalHistoricoService;
	}

	public Optional<Animal> findAnimalById(final int id) throws DataAccessException {
		return this.animalRepository.findById(id);
	}

	@Transactional
	public void saveAnimal(final Animal animal) throws DataAccessException {
		animal.setEsArchivado(false);
		if (animal.getComprado() == null || !animal.getComprado()) {
			animal.setComprado(false);
			animal.setProcedencia(null);
			animal.setFechaEntrada(null);
		}
		this.animalRepository.save(animal);
	}

	@Transactional
	public void archivarAnimal(final Animal animal) throws DataAccessException {
		animal.setEsArchivado(true);
		this.animalRepository.save(animal);
	}

	@Transactional
	public void deleteAnimal(final Animal animal) throws DataAccessException {
		Assert.isTrue(animal.getEsArchivado(), "Para que un animal sea eliminado, debe de estar archivado.");
		Assert.isTrue(this.animalHistoricoService.findAnimalHistoricoByAnimalId(animal.getId()).isPresent(), "Para que un animal sea eliminado, debe haber un archivo historico previo.");
		AnimalHistorico historico = this.animalHistoricoService.findAnimalHistoricoByAnimalId(animal.getId()).get();
		/* 1. Eliminamos el historico asociado al animal */
		this.animalHistoricoService.deleteAnimalHistorico(historico);
		/* 2. Eliminamos al animal */
		this.animalRepository.delete(animal);
	}

	//MÉTODOS PARA GUARDAR LOS ANIMALES DE UN LOTE
	public void saveAnimalesLote(final Lote lote, final LoteForm loteForm) {
		for (Integer i = 0; loteForm.getNumeroMachos() > i; i++) {
			this.metodoAuxiliarsaveAnimalesLote(lote, loteForm, TipoSexo.Macho, i + 1);
		}
		//Sumamos a i el numero de machos para que al crear un nuevo animal, su identificador siga con el número siguiente
		//con el que se creo el ultimo macho
		for (Integer i = 0 + loteForm.getNumeroMachos(); loteForm.getNumeroMachos() + loteForm.getNumeroHembras() > i; i++) {
			this.metodoAuxiliarsaveAnimalesLote(lote, loteForm, TipoSexo.Hembra, i + 1);
		}
	}

	public void metodoAuxiliarsaveAnimalesLote(final Lote lote, final LoteForm loteForm, final TipoSexo tipoSexo, final Integer i) {
		Animal animal = new Animal();
		Formatter fmt = new Formatter();
		animal.setIdentificadorAnimal(loteForm.getIdentificadorLote() + "-" + fmt.format("%03d", i));
		animal.setFechaNacimiento(loteForm.getFechaNacimiento());
		animal.setFechaIdentificacion(loteForm.getFechaIdentificacion());
		animal.setComprado(loteForm.getComprado());
		if (animal.getComprado() != null) {
			animal.setProcedencia(loteForm.getProcedencia());
			animal.setFechaEntrada(loteForm.getFechaEntrada());
		}
		animal.setSexo(tipoSexo);
		animal.setEsArchivado(false);
		animal.setTipoGanado(lote.getTipoGanado());
		animal.setLote(lote);
		animal.setExplotacionGanadera(lote.getExplotacionGanadera());
		this.animalRepository.save(animal);
	}

	@Transactional
	public Iterable<Animal> findAllAnimalNoLoteByExpIdYGanadoId(final Integer expId, final Integer tipoGanadoId, final Boolean esArchivado) {
		return this.animalRepository.findAllAnimalNoLoteByExpIdYGanadoId(expId, tipoGanadoId, esArchivado);
	}

	@Transactional
	public Optional<Animal> findAnimalByIdentificadorAnimalAndExpIdAndEsArchivado(final String identificadorAnimal, final Integer expId, final Boolean esArchivado) {
		return this.animalRepository.findAnimalByIdentificadorAnimalAndExpIdAndEsArchivado(identificadorAnimal, expId, esArchivado);
	}

	@Transactional
	public Iterable<Animal> findAllByLoteId(final Integer loteId, final Boolean esArchivado) {
		return this.animalRepository.findAllAnimalByLoteId(loteId, esArchivado);
	}

	@Transactional
	public Iterable<Animal> findAllAnimalByExpId(final Integer expId, final Boolean esArchivado) {
		return this.animalRepository.findAllAnimalByExpId(expId, esArchivado);
	}

	@Transactional
	public Optional<Animal> findAnimalByAnimalIdentificadorAnimal(final String identAnimal) {
		return this.animalRepository.findAnimalByAnimalIdentificadorAnimal(identAnimal);
	}

	@Transactional
	public Iterable<Animal> findAllAnimalByExpIdYGanadoIYEsArchivado(final Integer expId, final Integer tipoGanadoId, final Boolean esArchivado) {
		return this.animalRepository.findAllAnimalByExpIdYGanadoIYEsArchivado(expId, tipoGanadoId, esArchivado);
	}

}
