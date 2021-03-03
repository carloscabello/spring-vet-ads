
package org.springframework.samples.petclinic.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.forms.CitaForm;
import org.springframework.samples.petclinic.model.Cita;
import org.springframework.samples.petclinic.model.ExplotacionGanadera;
import org.springframework.samples.petclinic.model.Ganadero;
import org.springframework.samples.petclinic.model.TipoEstadoCita;
import org.springframework.samples.petclinic.model.Veterinario;
import org.springframework.samples.petclinic.repository.springdatajpa.CitaRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
public class CitaService {

	@Autowired
	private CitaRepository	citaRepository;

	@Autowired
	private ContratoService	contratoService;

	@Autowired
	private GanaderoService	ganaderoService;

	@Autowired
	private VetService		vetService;


	@Autowired
	public CitaService(final CitaRepository citaRepository) {
		this.citaRepository = citaRepository;
	}

	public CitaService(final CitaRepository citaRepository, final ContratoService contratoService, final GanaderoService ganaderoService, final VetService vetService) {
		this.citaRepository = citaRepository;
		this.contratoService = contratoService;
		this.ganaderoService = ganaderoService;
		this.vetService = vetService;
	}

	// ---- CRUD basico

	public Optional<Cita> findCitaById(final int citaId) throws DataAccessException {
		return this.citaRepository.findById(citaId);
	}

	public CitaForm createCitaForm() {
		CitaForm res = new CitaForm();

		/* Sugerimos a partir de mañana para la creacion de una cita */
		Calendar calendarHoraAux = Calendar.getInstance();
		calendarHoraAux.setTime(new Date());
		calendarHoraAux.add(Calendar.DAY_OF_MONTH, 1);

		res.setFecha(calendarHoraAux.getTime());
		res.setDuracion(1);
		return res;
	}

	@Transactional
	public void saveCita(final Cita cita) throws DataAccessException {
		//TODO: Comprobaciones extensivas de la creacion de citas
		this.citaRepository.save(cita);
	}

	// ---- CRUD mas complejo

	public void acceptCita(final Cita cita) {
		Veterinario loggedVeterinario = this.vetService.findVeterinarioByLogedUser();
		Assert.isTrue(cita.getContrato().getVeterinario().equals(loggedVeterinario), "La cita que se quiere aceptar no corresponde a un contrato del veterinario logueado");
		Assert.isTrue(cita.getFechaHoraInicio().after(new Date()), "La cita que se quiere aceptar es de una fecha que ya ha pasado.");
		Assert.isTrue(cita.getEstado().equals(TipoEstadoCita.PENDIENTE), "La cita que se pretende aceptar no está en estado PENDIENTE");
		cita.setEstado(TipoEstadoCita.ACEPTADA);
		this.saveCita(cita);
	}

	public void declineCita(final Cita cita) {
		Veterinario loggedVeterinario = this.vetService.findVeterinarioByLogedUser();
		Assert.isTrue(cita.getContrato().getVeterinario().equals(loggedVeterinario), "La cita que se quiere rechazar no corresponde a un contrato del veterinario logueado");
		Assert.isTrue(cita.getFechaHoraInicio().after(new Date()), "La cita que se quiere rechazar es de una fecha que ya ha pasado.");
		//Cita citaAnterior = findCitaById(cita.getId()).get();
		Assert.isTrue(cita.getEstado().equals(TipoEstadoCita.PENDIENTE), "La cita que se pretende rechazar no estaba en estado PENDIENTE");
		Assert.isTrue(cita.getRechazoJustificacion() != null && !cita.getRechazoJustificacion().isEmpty(), "La cita debe rechazarse con algún motivo");
		cita.setEstado(TipoEstadoCita.RECHAZADA);
		this.saveCita(cita);
	}

	public Iterable<Cita> findCitasFuturasByLoggedGanaderoAndEstado(final TipoEstadoCita estado) {
		Ganadero ganadero = this.ganaderoService.findGanaderoByLogedUser();
		return this.findCitasFuturasByGanaderoAndEstado(ganadero, estado);
	}

	public Iterable<Cita> findCitasFuturasByLoggedVeterinarioAndEstado(final TipoEstadoCita estado) {
		Veterinario veterinario = this.vetService.findVeterinarioByLogedUser();
		return this.findCitasFuturasByVeterinarioAndEstado(veterinario, estado);
	}

	public Iterable<Cita> findCitasByLoggedGanaderoAndFilters(final TipoEstadoCita estado, final Date fechaInicio, final Date fechaFin) {
		Ganadero ganadero = this.ganaderoService.findGanaderoByLogedUser();
		return this.findCitasByGanaderoIdOrVeterinarioIdAndEstadoAndDateTimeRange(ganadero, null, estado, fechaInicio, fechaFin);
	}

	public Iterable<Cita> findCitasByLoggedVeterinarioAndFilters(final TipoEstadoCita estado, final Date fechaInicio, final Date fechaFin) {
		Veterinario veterinario = this.vetService.findVeterinarioByLogedUser();
		return this.findCitasByGanaderoIdOrVeterinarioIdAndEstadoAndDateTimeRange(null, veterinario, estado, fechaInicio, fechaFin);
	}

	// ---- Consultas personalizadas

	/* Citas futuras del veterinario, se puede filtrar opcionalmente por estado tambien */
	public Iterable<Cita> findCitasFuturasByVeterinarioAndEstado(final Veterinario veterinario, final TipoEstadoCita estado) {
		return this.citaRepository.findCitasFuturasByVeterinarioIdAndEstado(veterinario.getId(), estado.ordinal());

	}

	/* Citas futuras del ganadero, se puede filtrar opcionalmente por estado tambien */
	public Iterable<Cita> findCitasFuturasByGanaderoAndEstado(final Ganadero ganadero, final TipoEstadoCita estado) {
		return this.citaRepository.findCitasFuturasByGanaderoIdAndEstado(ganadero.getId(), estado.ordinal());

	}

	/* Citas futuras del ganadero, se puede filtrar opcionalmente por estado tambien */
	public Iterable<Cita> findCitasFuturasByExplotacionGanaderaAndEstado(final ExplotacionGanadera explotacionGanadera, final TipoEstadoCita estado) {
		return this.citaRepository.findCitasFuturasByExplotacionGanaderaIdAndEstado(explotacionGanadera.getId(), estado.ordinal());
	}

	/* Citas filtrando por ganadero, veterinario, estado y rango de fecha-hora (Todos los parámetros son opcionales) */
	public Iterable<Cita> findCitasFuturasByGanaderoIdOrVeterinarioIdAndEstadoAndDateTimeRange(final Ganadero ganadero, final Veterinario veterinario, final TipoEstadoCita estado, final Date fechaInicio, final Date fechaFin) {
		return this.citaRepository.findCitasByGanaderoIdOrVeterinarioIdAndEstadoAndDateTimeRange(ganadero.getId(), veterinario.getId(), estado.ordinal(), fechaInicio, fechaFin);
	}

	Iterable<Cita> findCitasByGanaderoIdOrVeterinarioIdAndEstadoAndDateTimeRange(final Ganadero ganadero, final Veterinario veterinario, final TipoEstadoCita estado, final Date fechaInicio, final Date fechaFin) {

		/*
		 * La query del repositorio esta preparada para recibir valores nulos, pero dado que recibimos los objetos,
		 * debemos comprobar que no son nulos antes
		 */
		Integer ganaderoId = null;
		Integer veterinarioId = null;
		Integer estadoInteger = null;
		Iterable<Cita> res;

		if (ganadero != null) {
			ganaderoId = ganadero.getId();
		}

		if (veterinario != null) {
			veterinarioId = veterinario.getId();
		}

		if (estado != null) {
			estadoInteger = estado.ordinal();
		}
		try {

			res = this.citaRepository.findCitasByGanaderoIdOrVeterinarioIdAndEstadoAndDateTimeRange(ganaderoId, veterinarioId, estadoInteger, fechaInicio, fechaFin);
		} catch (Throwable e) {
			e.printStackTrace();
			res = new ArrayList<Cita>();
		}
		return res;
	}

	/* Citas en conflicto */
	public Iterable<Cita> findCitasAceptadasYPendientesByGanaderoIdAndDateTimeRange(final Date fechaInicio, final Date fechaFinal) {
		Ganadero ganadero = this.ganaderoService.findGanaderoByLogedUser();
		return this.citaRepository.findCitasAceptadasYPendientesByGanaderoIdAndDateTimeRange(ganadero.getId(), fechaInicio, fechaFinal);
	}

	/* Citas */

	// ---- Metodos auxiliares

	public Cita reconstruct(final CitaForm citaForm) {
		Cita res = new Cita();

		/* Reconstuimos las fechaHoraInicio y fechaHoraFin */
		Date fechaHoraInicio = this.reconstructFechaHoraInicioCita(citaForm.getFecha(), citaForm.getHoraInicio());

		Date fechaHoraFin = this.reconstructFechaHoraFinCita(fechaHoraInicio, citaForm.getDuracion());

		/* Reconstruimos la cita */
		res.setFechaHoraInicio(fechaHoraInicio);
		res.setFechaHoraFin(fechaHoraFin);
		res.setMotivo(citaForm.getMotivo());
		res.setEstado(TipoEstadoCita.PENDIENTE);
		res.setContrato(this.contratoService.findContratoById(citaForm.getContratoId()).get());

		return res;

	}

	public Date reconstructFechaHoraInicioCita(final Date fecha, final String horaInicio) {
		Calendar calendarHoraAuxInicio = Calendar.getInstance();
		calendarHoraAuxInicio.setTime(fecha);
		calendarHoraAuxInicio.set(Calendar.HOUR_OF_DAY, new Integer(horaInicio.substring(0, 2)));
		return calendarHoraAuxInicio.getTime();
	}

	public Date reconstructFechaHoraFinCita(final Date fechaHoraInicio, final Integer duracion) {
		Calendar calendarHoraAuxFin = Calendar.getInstance();
		calendarHoraAuxFin.setTime(fechaHoraInicio);
		calendarHoraAuxFin.add(Calendar.HOUR_OF_DAY, duracion);
		return calendarHoraAuxFin.getTime();
	}

	public Cita reconstruct(final Cita cita) {
		/* Reconstruimos la cita */
		Cita res = this.findCitaById(cita.getId()).get();

		res.setRechazoJustificacion(cita.getRechazoJustificacion());
		res.setEstado(TipoEstadoCita.RECHAZADA);

		return res;
	}

}
