
package org.springframework.samples.petclinic.model;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Range;

import lombok.Data;

@Data
@Entity
public class Producto extends NamedEntity {

	@NotNull
	@Range(min = 0)
	private Integer		cantidad;

	@NotNull
	@Range(min = 0)
	private Double		precio;

	private Boolean		necesitaReceta;

	@ManyToOne(optional = false)
	private Veterinario	veterinario;


	public Boolean getEsDisponible() {
		Boolean res = true;
		if (this.cantidad == 0) {
			res = false;
		}
		return res;
	}

}
