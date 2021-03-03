
package org.springframework.samples.petclinic.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.forms.RecetaForm;
import org.springframework.samples.petclinic.model.Ganadero;
import org.springframework.samples.petclinic.model.Receta;
import org.springframework.samples.petclinic.model.TipoEstadoCita;
import org.springframework.samples.petclinic.model.Veterinario;
import org.springframework.samples.petclinic.repository.springdatajpa.RecetaRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
public class RecetaService {

	@Autowired
	private RecetaRepository	recetaRepository;

	@Autowired
	private GanaderoService		ganaderoService;

	@Autowired
	private CitaService			citaService;

	@Autowired
	private VetService			vetService;


	@Autowired
	public RecetaService(final RecetaRepository recetaRepository) {
		this.recetaRepository = recetaRepository;
	}

	public Optional<Receta> findRecetaById(final int recetaId) throws DataAccessException {
		return this.recetaRepository.findById(recetaId);
	}

	@Transactional
	public void saveReceta(final Receta receta) throws DataAccessException {
		Assert.isTrue(receta.getCita().getEstado().equals(TipoEstadoCita.ACEPTADA), "La cita elegida para la receta debe estar en estado aceptada");
		Assert.isTrue(receta.getCita().getFechaHoraInicio().after(this.fechaActualMenosUnMes()) && receta.getCita().getFechaHoraFin().before(this.fechaActual()),
			"La cita elegida para la receta debe ser una cita que se haya tenido entre los tiempos del momento actual y un mes hacia atrás");
		this.recetaRepository.save(receta);
	}

	public Iterable<Receta> findRecetasByLoggedGanadero() {
		Ganadero ganadero = this.ganaderoService.findGanaderoByLogedUser();
		return this.recetaRepository.findRecetasByGanaderoId(ganadero.getId());
	}

	@Transactional
	public Iterable<Receta> findRecetasByLoggedVeterinario() {
		Veterinario vet = this.vetService.findVeterinarioByLogedUser();
		return this.recetaRepository.findRecetasByVetId(vet.getId());
	}

	public Receta reconstruct(final RecetaForm recetaForm) {
		Receta res = new Receta();

		Date date = java.util.Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());
		res.setFechaRealizacion(date);
		res.setDescripcion(recetaForm.getDescripcion());
		res.setCita(this.citaService.findCitaById(recetaForm.getCitaId()).get());
		res.setEsFacturado(false);

		return res;

	}

	public Iterable<Receta> findRecetasByContratoIdAndNoEsFacturado(final int contratoId) {
		return this.recetaRepository.findRecetasByContratoIdAndNoEsFacturado(contratoId);
	}

	public void marcarRecetasComoFacturadas(final Receta receta) {
		receta.setEsFacturado(true);
		this.recetaRepository.save(receta);
	}

	//Métodos auxiliares
	public Date fechaActual() {
		Calendar calendario = Calendar.getInstance();
		return calendario.getTime();
	}

	public Date fechaActualMenosUnMes() {
		Calendar calendario = Calendar.getInstance();
		calendario.add(Calendar.MONTH, -1);
		return calendario.getTime();
	}

}
