
package org.springframework.samples.petclinic.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Range;

import lombok.Data;

@Data
@Entity
public class LineaReceta extends BaseEntity {

	@NotNull
	@Range(min = 1)
	private Integer		cantidad;

	@NotNull
	@Range(min = 0)
	private Double		precioVenta;

	@NotNull
	@ManyToOne(optional = false)
	@JoinColumn(name = "receta_id")
	private Receta		receta;

	@NotNull
	@ManyToOne(optional = false)
	private Producto	producto;

}
