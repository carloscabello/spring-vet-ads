
package org.springframework.samples.petclinic.forms;

import java.util.Date;
import java.util.List;

import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.samples.petclinic.model.Contrato;
import org.springframework.samples.petclinic.model.Receta;

import lombok.Data;

@Data
public class FacturaForm {

	private Boolean			esPagado;

	@DateTimeFormat(pattern = "yyyy/MM/dd HH:mm")
	private Date			fechaEmision;

	@OneToMany
	private List<Receta>	recetas;

	private List<Integer>	productoId;

	private List<Integer>	cantidad;

	@ManyToOne(optional = false)
	private Contrato		contrato;

}
