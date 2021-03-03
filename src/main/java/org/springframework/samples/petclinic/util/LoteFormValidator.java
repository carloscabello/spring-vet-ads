
package org.springframework.samples.petclinic.util;

import org.springframework.samples.petclinic.forms.LoteForm;
import org.springframework.samples.petclinic.service.LoteService;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class LoteFormValidator implements Validator {

	private LoteService	loteService;

	private Integer		explotacionGanaderaId;


	public LoteFormValidator(final LoteService loteService, final Integer explotacionGanaderaId) {
		this.loteService = loteService;
		this.explotacionGanaderaId = explotacionGanaderaId;
	}

	@Override
	public boolean supports(final Class<?> clazz) {
		return LoteForm.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(final Object target, final Errors errors) {
		LoteForm loteForm = (LoteForm) target;

		//Restricción 1: No puede haber un lote con un mismo identificador no archivado en una misma explotación ganadera
		if (loteForm.getIdentificadorLote() != null) {
			if (this.loteService.findLoteByIdentificadorLoteAndExpIdAndEsArchivado(loteForm.getIdentificadorLote(), this.explotacionGanaderaId, false).isPresent()) {
				errors.rejectValue("identificadorLote", "loteForm.identificadorLote.repetido");
			}
		}

		//Restricción 2: Número de machos + número de hembras < 1000. El número total de animales debe ser inferior a 1000
		if (loteForm.getNumeroHembras() != null && loteForm.getNumeroMachos() != null)

		{
			if (loteForm.getNumeroHembras() + loteForm.getNumeroMachos() > 999) {
				errors.rejectValue("numeroMachos", "lote.numeroAnimales.noMayorA999");
			}
		}

		//Restricción 3: Fecha identificación anterior a fecha nacimiento. La fecha de identificación debe ser posterior a la fecha de nacimiento.
		if (loteForm.getFechaNacimiento() != null && loteForm.getFechaIdentificacion() != null) {
			if (loteForm.getFechaNacimiento().isAfter(loteForm.getFechaIdentificacion())) {
				errors.rejectValue("fechaIdentificacion", "animal.fechaIdentificacion.anteriorAFechaNac");
			}
		}

		if (loteForm.getComprado() != null) {

			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "procedencia", "animal.procedencia.vacia");
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "fechaEntrada", "animal.fechaEntrada.vacia");

			if (loteForm.getFechaEntrada() != null) {
				//Restricción 4: Fecha entrada anterior a fecha de nacimiento. La fecha de entrada debe ser posterior a la fecha de nacimiento
				if (loteForm.getFechaNacimiento() != null) {
					if (loteForm.getFechaEntrada().isBefore(loteForm.getFechaNacimiento())) {
						errors.rejectValue("fechaEntrada", "animal.fechaEntrada.anteriorAFechaNac");
					}
				}

				//Restricción 5: Fecha entrada anterior a fecha identificación. La fecha de entrada debe ser posterior a la fecha de identificación
				if (loteForm.getFechaIdentificacion() != null) {
					if (loteForm.getFechaEntrada().isBefore(loteForm.getFechaIdentificacion())) {
						errors.rejectValue("fechaEntrada", "animal.fechaEntrada.anteriorAFechaIden");
					}
				}
			}
		}
	}
}
