
package org.springframework.samples.petclinic.model;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
@Entity
public class Contrato extends BaseEntity {

	@NotNull
	@ManyToMany
	private List<TiposGanado>	ganados;

	private TipoEstadoContrato	estado;

	@DateTimeFormat(pattern = "yyyy/MM/dd")
	private LocalDate			fechaPeticion;

	@NotNull
	@DateTimeFormat(pattern = "yyyy/MM/dd")
	private LocalDate			fechaInicial;

	@NotNull
	@DateTimeFormat(pattern = "yyyy/MM/dd")
	private LocalDate			fechaFinal;

	//Dependencias
	@ManyToOne(optional = false)
	private ExplotacionGanadera	explotacionGanadera;

	@ManyToOne(optional = false)
	private Veterinario			veterinario;

}
