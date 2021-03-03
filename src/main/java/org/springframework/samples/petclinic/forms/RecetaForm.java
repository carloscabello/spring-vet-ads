
package org.springframework.samples.petclinic.forms;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class RecetaForm {

	@NotBlank
	@NotNull
	@Size(max = 350)
	private String			descripcion;

	private Integer			citaId;

	private List<Integer>	productoId;

	private List<Integer>	cantidad;

}
