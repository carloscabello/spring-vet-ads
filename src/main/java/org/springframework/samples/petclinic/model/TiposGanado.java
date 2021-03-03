
package org.springframework.samples.petclinic.model;

import javax.persistence.Entity;
import javax.validation.constraints.NotEmpty;

import lombok.Data;

@Data
@Entity
public class TiposGanado extends BaseEntity {

	@NotEmpty
	private String tipoGanado;

}
