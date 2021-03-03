
package org.springframework.samples.petclinic.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
@Entity
public class Receta extends BaseEntity {

	@NotNull
	@DateTimeFormat(pattern = "yyyy/MM/dd HH:mm")
	private Date	fechaRealizacion;

	@NotBlank
	@NotNull
	@Size(max = 350)
	private String	descripcion;

	private Boolean	esFacturado;

	@ManyToOne(optional = false)
	private Cita	cita;
}
