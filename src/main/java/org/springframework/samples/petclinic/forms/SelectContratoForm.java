
package org.springframework.samples.petclinic.forms;

import javax.validation.constraints.NotNull;

import org.springframework.samples.petclinic.model.Contrato;

import lombok.Data;

@Data
public class SelectContratoForm {

	@NotNull
	private Contrato contrato;

}
