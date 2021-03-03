
package org.springframework.samples.petclinic.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
@Entity
public class Factura extends BaseEntity {

	private Boolean			esPagado;

	@DateTimeFormat(pattern = "yyyy/MM/dd HH:mm")
	private Date			fechaEmision;

	@OneToMany
	private List<Receta>	recetas;

	@ManyToOne(optional = false)
	private Contrato		contrato;

}
