
package org.springframework.samples.petclinic.forms;

import java.time.LocalDate;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class LoteForm {

	@NotEmpty
	private String		identificadorLote;

	@NotNull
	private Integer		numeroMachos;

	@NotNull
	private Integer		numeroHembras;

	@NotNull
	@DateTimeFormat(pattern = "yyyy/MM/dd")
	private LocalDate	fechaNacimiento;

	@NotNull
	@DateTimeFormat(pattern = "yyyy/MM/dd")
	private LocalDate	fechaIdentificacion;

	private Boolean		comprado;

	private String		procedencia;

	@DateTimeFormat(pattern = "yyyy/MM/dd")
	private LocalDate	fechaEntrada;

}
