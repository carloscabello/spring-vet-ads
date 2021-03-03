
package org.springframework.samples.petclinic.service;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Contrato;
import org.springframework.samples.petclinic.model.Ganadero;
import org.springframework.samples.petclinic.model.TipoEstadoContrato;
import org.springframework.samples.petclinic.model.Veterinario;
import org.springframework.samples.petclinic.repository.springdatajpa.ContratoRepository;
import org.springframework.stereotype.Service;

@Service
public class ContratoService {

	@Autowired
	private ContratoRepository	contratoRepository;

	@Autowired
	private VetService			veterinarioService;
	
	@Autowired
	private GanaderoService			ganaderoService;


	@Autowired
	public ContratoService(final ContratoRepository contratoRepository) {
		this.contratoRepository = contratoRepository;
	}

	public ContratoService(final ContratoRepository contratoRepository, final VetService vetService) {
		this.contratoRepository = contratoRepository;
		this.veterinarioService = vetService;
	}

	public Optional<Contrato> findContratoById(final int id) throws DataAccessException {
		return this.contratoRepository.findById(id);
	}

	@Transactional
	public void saveContrato(final Contrato contrato) throws DataAccessException {
		this.contratoRepository.save(contrato);
	}

	@Transactional
	public void deleteContrato(final Contrato contrato) throws DataAccessException {
		this.contratoRepository.delete(contrato);
	}

	public Iterable<Contrato> findContratoByVeterinarioId() {
		Veterinario veterinario = this.veterinarioService.findVeterinarioByLogedUser();
		return this.contratoRepository.findContratosByVeterinarioId(veterinario.getId());
	}
	
	public Iterable<Contrato> findContratosByLogguedGanadero(){
		Ganadero ganadero = ganaderoService.findGanaderoByLogedUser();
		return contratoRepository.findContratosByGanaderoId(ganadero.getId());
	}
	
	public Iterable<Contrato> findContratosVigentesAceptadosByLogguedGanadero(){
		Ganadero ganadero = ganaderoService.findGanaderoByLogedUser();
		return contratoRepository.findContratosVigentesAceptadosByGanaderoId(ganadero.getId());
		}

	public Iterable<Contrato> findAllContratoVigentesByLoguedVeterinario() {
		Veterinario veterinario = this.veterinarioService.findVeterinarioByLogedUser();
		return this.contratoRepository.findAllContratosVigentesByVeterinarioId(veterinario.getId());
	}

	public Iterable<Contrato> findAllContratoByExpGanaderaAndEstado(final Integer expGanId, final TipoEstadoContrato estado) {
		return this.contratoRepository.findAllContratosByExpGanaderaAndEstado(expGanId, estado);
	}

	public Optional<Contrato> findAllContratosVigentesByExpGanaderaAndVeterinarioAndEstado(final Integer expGanId, final Integer vetId, final TipoEstadoContrato estado) {
		return this.contratoRepository.findAllContratosVigentesByExpGanaderaAndVeterinarioAndEstado(expGanId, vetId, estado);
	}

	public Iterable<Contrato> findAllContratoByExpGanaderaId(final Integer expId) {
		return this.contratoRepository.findAllContratosByExpGanaderaId(expId);
	}
}
