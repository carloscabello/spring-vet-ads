
package org.springframework.samples.petclinic.forms;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class LineaRecetaForm {

	@NotNull
	private Integer	recetaId;

	@NotNull
	private Integer	productoId;

	@NotNull
	@NotBlank
	private Integer	cantidad;

}
