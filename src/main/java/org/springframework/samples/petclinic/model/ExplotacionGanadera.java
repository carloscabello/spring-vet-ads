
package org.springframework.samples.petclinic.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;

import lombok.Data;

@Data
@Entity
public class ExplotacionGanadera extends NamedEntity{

	@Id
	@Column(unique = true)
	@NotEmpty
	@NotNull
	private String		numeroRegistro;

	@NotEmpty
	@NotNull
	private String		terminoMunicipal;

	private Boolean		esArchivado;

	@ManyToOne(optional = false)
	private Ganadero	ganadero;

}
