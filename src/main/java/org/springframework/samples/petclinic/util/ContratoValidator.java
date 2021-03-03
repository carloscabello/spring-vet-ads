
package org.springframework.samples.petclinic.util;

import java.time.LocalDate;

import org.springframework.samples.petclinic.model.Contrato;
import org.springframework.samples.petclinic.model.TipoEstadoContrato;
import org.springframework.samples.petclinic.service.ContratoService;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class ContratoValidator implements Validator {

	private ContratoService contratoService;


	public ContratoValidator(final ContratoService contratoService) {
		this.contratoService = contratoService;
	}

	@Override
	public boolean supports(final Class<?> clazz) {
		return Contrato.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(final Object target, final Errors errors) {
		Contrato contrato = (Contrato) target;

		if (this.contratoService.findAllContratosVigentesByExpGanaderaAndVeterinarioAndEstado(contrato.getExplotacionGanadera().getId(), contrato.getVeterinario().getId(), TipoEstadoContrato.ACEPTADO).isPresent()) {
			errors.rejectValue("fechaFinal", "contrato.existeUnContratoAceptado");
		}

		if (this.contratoService.findAllContratosVigentesByExpGanaderaAndVeterinarioAndEstado(contrato.getExplotacionGanadera().getId(), contrato.getVeterinario().getId(), TipoEstadoContrato.PENDIENTE).isPresent()) {
			errors.rejectValue("fechaFinal", "contrato.existeUnContratoPendiente");
		}

		if (contrato.getFechaInicial() != null && contrato.getFechaFinal() != null) {
			if (contrato.getFechaInicial().isAfter(contrato.getFechaFinal())) {
				errors.rejectValue("fechaFinal", "contrato.fechaFinal.isBeforeFechaInic");
			}
		}

		if (contrato.getFechaInicial() != null) {
			if (LocalDate.now().isAfter(contrato.getFechaInicial())) {
				errors.rejectValue("fechaInicial", "contrato.fechaInicial.isBeforeFechaPet");
			}
		}

	}

}
