package org.springframework.samples.petclinic.model;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
@Entity
public class Cita extends BaseEntity{
	
	// ---- Atributos
	
	@NotNull
	@DateTimeFormat(pattern = "yyyy/MM/dd HH:mm")
	private Date fechaHoraInicio;
	
	@NotNull
	@DateTimeFormat(pattern = "yyyy/MM/dd HH:mm")
	private Date fechaHoraFin;
 
	@NotBlank
	@NotNull
	@Size(max=350)
	private String motivo;
	
	@Size(max=350)
	private String rechazoJustificacion;
	
	@NotNull
	@Enumerated
	private TipoEstadoCita estado;
	
	// ---- Relaciones 
	
	@ManyToOne(optional = false)
	private Contrato contrato;

}
