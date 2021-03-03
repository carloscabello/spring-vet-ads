
package org.springframework.samples.petclinic.model;

import java.time.LocalDate;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
@Entity
public class AnimalHistorico extends BaseEntity {

	@DateTimeFormat(pattern = "yyyy/MM/dd")
	private LocalDate	fechaFallecimiento;

	@DateTimeFormat(pattern = "yyyy/MM/dd")
	private LocalDate	fechaVenta;

	private String		masInfo;

	@OneToOne(optional = false)
	private Animal		animal;

}
