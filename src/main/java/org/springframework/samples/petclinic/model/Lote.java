
package org.springframework.samples.petclinic.model;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;

import lombok.Data;

@Data
@Entity
public class Lote extends BaseEntity {

	@Id
	@NotEmpty
	@NotNull
	private String				identificadorLote;

	private Boolean				esArchivado;

	@ManyToOne(optional = false)
	private ExplotacionGanadera	explotacionGanadera;

	@ManyToOne(optional = false)
	@NotNull
	private TiposGanado			tipoGanado;
}
