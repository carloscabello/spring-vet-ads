
package org.springframework.samples.petclinic.repository.springdatajpa;

import java.util.Date;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.model.Cita;
import org.springframework.samples.petclinic.model.TipoEstadoCita;

public interface CitaRepository extends CrudRepository<Cita, Integer> {

	/* Citas futuras del veterinario, se puede filtrar opcionalmente por estado tambien */
	@Query("select c from Cita c where c.contrato.veterinario.id = ?1 and (?2 is null or c.estado = ?2) and CURRENT_DATE <= c.fechaHoraInicio order by c.fechaHoraInicio asc")
	Iterable<Cita> findCitasFuturasByVeterinarioIdAndEstado(Integer veterinarioId, Integer estado);

	/* Citas futuras del ganadero, se puede filtrar opcionalmente por estado tambien */
	@Query("select c from Cita c where c.contrato.explotacionGanadera.ganadero.id = ?1 and (?2 is null or c.estado = ?2) and CURRENT_DATE <= c.fechaHoraInicio order by c.fechaHoraInicio asc")
	Iterable<Cita> findCitasFuturasByGanaderoIdAndEstado(Integer ganaderoId, Integer estado);

	/* Citas futuras del ganadero, se puede filtrar opcionalmente por estado tambien */
	@Query("select c from Cita c where c.contrato.explotacionGanadera.id = ?1 and (?2 is null or c.estado = ?2) and CURRENT_DATE <= c.fechaHoraInicio order by c.fechaHoraInicio desc")
	Iterable<Cita> findCitasFuturasByExplotacionGanaderaIdAndEstado(Integer explotacionGanaderaId, Integer estado);

	/* Citas filtrando por ganadero, veterinario, estado y rango de fecha-hora (Todos los parámetros son opcionales) */
	@Query("select c from Cita c where " + "(?1 is null or c.contrato.explotacionGanadera.ganadero.id = ?1) " + "and (?2 is null or c.contrato.veterinario.id = ?2) " + "and (?3 is null or c.estado = ?3) "
		+ "and (?4 is null or ?4 <= c.fechaHoraInicio) and (?5 is null or c.fechaHoraFin <= ?5)" + "order by c.fechaHoraInicio desc")
	Iterable<Cita> findCitasByGanaderoIdOrVeterinarioIdAndEstadoAndDateTimeRange(Integer ganaderoId, Integer veterinarioId, Integer estado, Date fechaInicio, Date fechaFin);

	/* Query enfocada al validador de citaForm. Para saber si cuando se va a crear una nueva cita, hay una cita que
	   se solapa con la cita nueva que se pretende crear */
	@Query("select c from Cita c where c.contrato.explotacionGanadera.ganadero.id = ?1 and (c.estado != 1)"
	   + "and ( (?2 > c.fechaHoraInicio and ?3 < fechaHoraFin)" + " or (?2 > c.fechaHoraInicio and ?2 < c.fechaHoraInicio)" 
	   + "or (?3 > c.fechaHoraInicio and ?3 < c.fechaHoraFin)" + " or (?2 < c.fechaHoraInicio and ?3 > c.fechaHoraFin) "
	   + "or (?2 = c.fechaHoraInicio) or (?3 = c.fechaHoraFin) )")
	Iterable<Cita> findCitasAceptadasYPendientesByGanaderoIdAndDateTimeRange(Integer ganaderoId, Date fechaInicio, Date fechaFinal); 
}
