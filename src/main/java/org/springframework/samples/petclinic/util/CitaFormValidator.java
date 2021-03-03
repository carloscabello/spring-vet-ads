
package org.springframework.samples.petclinic.util;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import org.springframework.samples.petclinic.forms.CitaForm;
import org.springframework.samples.petclinic.model.Cita;
import org.springframework.samples.petclinic.model.Contrato;
import org.springframework.samples.petclinic.service.CitaService;
import org.springframework.samples.petclinic.service.ContratoService;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class CitaFormValidator implements Validator {

	private ContratoService	contratoService;

	private CitaService		citaService;

	private Integer			contratoId;


	public CitaFormValidator(final ContratoService contratoService, final CitaService citaService, final Integer contratoId) {
		this.contratoService = contratoService;
		this.citaService = citaService;
		this.contratoId = contratoId;
	}

	@Override
	public boolean supports(final Class<?> clazz) {
		return CitaForm.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(final Object target, final Errors errors) {
		CitaForm citaForm = (CitaForm) target;

		if (citaForm.getFecha() != null) {
			LocalDate fechaCita = citaForm.getFecha().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

			//Restricción 1: La fecha de la cita pedida debe ser como mínimo de mañana.
			if (!fechaCita.isAfter(LocalDate.now())) {
				errors.rejectValue("fecha", "citaForm.fecha.fechaDeLaCitaNoDespuesDeLaFechaActual");
			}

			//Restricción 2: La fecha de la cita debe estar entre la fecha inicio del contrato y la fecha final.
			Contrato contrato = this.contratoService.findContratoById(this.contratoId).get();

			if (!(contrato.getFechaInicial().isBefore(fechaCita) && fechaCita.isBefore(contrato.getFechaFinal()))) {
				errors.rejectValue("fecha", "citaForm.fecha.fueraDelRangoDeFechasDelContrato");
			}

			/*
			 * Restricción 3: No se puede pedir una cita que solape a otra, es decir, si tienes una pendiente o aceptada
			 * para ese rango de tiempo, no puedes pedir otra
			 */
			if (citaForm.getHoraInicio() != null && citaForm.getHoraInicio().matches("^(0|1)[0-9]:00$") && citaForm.getDuracion() != null) {
				Date fechaHoraInicio = this.citaService.reconstructFechaHoraInicioCita(citaForm.getFecha(), citaForm.getHoraInicio());
				Date fechaHoraFin = this.citaService.reconstructFechaHoraFinCita(fechaHoraInicio, citaForm.getDuracion());
				List<Cita> citasSolapadas = (List<Cita>) this.citaService.findCitasAceptadasYPendientesByGanaderoIdAndDateTimeRange(fechaHoraInicio, fechaHoraFin);
				if (!citasSolapadas.isEmpty()) {
					errors.rejectValue("fecha", "citaForm.fecha.existeCitaSolapada");
				}
			}

		}

	}

}
