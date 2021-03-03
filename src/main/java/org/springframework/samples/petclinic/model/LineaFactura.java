
package org.springframework.samples.petclinic.model;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Range;

import lombok.Data;

@Data
@Entity
public class LineaFactura extends BaseEntity {

	@NotNull
	@Range(min = 1)
	private Integer		cantidad;

	@NotNull
	@Range(min = 0)
	private Double		precioVenta;

	@NotNull
	@ManyToOne(optional = false)
	private Factura		factura;

	@NotNull
	@ManyToOne(optional = false)
	private Producto	producto;

}
