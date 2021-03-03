package org.springframework.samples.petclinic.forms;

import javax.validation.constraints.NotNull;

import org.springframework.samples.petclinic.model.Contrato;
import org.springframework.samples.petclinic.model.ExplotacionGanadera;

import lombok.Data;

@Data
public class SelectCreateCitaForm {
	
	@NotNull
	private ExplotacionGanadera explotacionGanadera;
	
	@NotNull
	private Contrato contrato;

}
