
package org.springframework.samples.petclinic.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.ExplotacionGanadera;
import org.springframework.samples.petclinic.model.Ganadero;
import org.springframework.samples.petclinic.model.Veterinario;
import org.springframework.samples.petclinic.repository.springdatajpa.ExplotacionGanaderaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ExplotacionGanaderaService {

	@Autowired
	private ExplotacionGanaderaRepository	expGanRepository;

	@Autowired
	private GanaderoService					ganaderoService;

	@Autowired
	private VetService						veterinarioService;


	@Autowired
	public ExplotacionGanaderaService(final ExplotacionGanaderaRepository explotacionGanaderaRepository,
			GanaderoService ganaderoService, VetService	veterinarioService) {
		this.expGanRepository = explotacionGanaderaRepository;
		this.ganaderoService = ganaderoService;
		this.veterinarioService = veterinarioService;
	}

	public Optional<ExplotacionGanadera> findExpGanaderaById(final int id) throws DataAccessException {
		return this.expGanRepository.findById(id);
	}

	@Transactional
	public void saveExpGanadera(final ExplotacionGanadera expGanadera) throws DataAccessException {
		expGanadera.setGanadero(this.ganaderoService.findGanaderoByLogedUser());
		expGanadera.setEsArchivado(false);
		this.expGanRepository.save(expGanadera);
	}

	@Transactional
	public Iterable<ExplotacionGanadera> findExpGanaderaByGanaderoId(final Boolean esArchivado) {
		Ganadero ganadero = this.ganaderoService.findGanaderoByLogedUser();
		return this.expGanRepository.findAllExpGanaderaByGanaderoId(ganadero.getId(), esArchivado);
	}

	@Transactional
	public Iterable<ExplotacionGanadera> findExpGanaderaByVeterinarioId() {
		Veterinario veterinario = this.veterinarioService.findVeterinarioByLogedUser();
		return this.expGanRepository.findAllExpGanaderaByVeterinarioId(veterinario.getId());
	}

	@Transactional
	public Optional<ExplotacionGanadera> findExpGanaderaByNumeroRegistro(final String numeroRegistro) {
		return this.expGanRepository.findExpGanaderaByNumeroRegistro(numeroRegistro);
	}

	@Transactional
	public void archivarExpGanadera(final ExplotacionGanadera expGanadera) throws DataAccessException {
		expGanadera.setEsArchivado(true);
		this.expGanRepository.save(expGanadera);
	}

}
