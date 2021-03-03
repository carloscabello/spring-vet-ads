
package org.springframework.samples.petclinic.forms;

import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class CitaForm {

	@NotNull
	@DateTimeFormat(pattern = "yyyy/MM/dd")
	private Date	fecha;

	@NotNull
	@NotBlank
	@Pattern(regexp = "^(0|1)[0-9]:00$")
	private String	horaInicio;

	@NotNull
	@Range(min = 1, max = 6)
	private Integer	duracion;

	@NotBlank
	@NotNull
	@Size(max = 350)
	private String	motivo;

	private Integer	contratoId;

}
