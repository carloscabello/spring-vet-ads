
package org.springframework.samples.petclinic.model;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import org.springframework.data.annotation.Id;
import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
@Entity
public class Animal extends BaseEntity {

	@Id
	//No tiene column unique = true, porque cuando se archiva, otro ganadero
	//podría registrar un animal con un identificador de un animal archivado
	@NotEmpty
	@NotNull
	private String				identificadorAnimal;

	@NotNull
	@DateTimeFormat(pattern = "yyyy/MM/dd")
	@Past
	private LocalDate			fechaNacimiento;

	@NotNull
	@DateTimeFormat(pattern = "yyyy/MM/dd")
	@Past
	private LocalDate			fechaIdentificacion;

	private Boolean				comprado;

	private String				procedencia;

	@DateTimeFormat(pattern = "yyyy/MM/dd")
	@Past
	private LocalDate			fechaEntrada;

	@NotNull
	private TipoSexo			sexo;

	private Boolean				esArchivado;

	//dependencias
	@ManyToOne(optional = false)
	private TiposGanado			tipoGanado;

	@ManyToOne(optional = false)
	private ExplotacionGanadera	explotacionGanadera;

	@ManyToOne(optional = true)
	private Lote				lote;

}
