
package org.springframework.samples.petclinic.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.TipoEstadoCita;
import org.springframework.samples.petclinic.repository.springdatajpa.CitaRepository;
import org.springframework.stereotype.Service;

@DisplayName("Cita Service Non-solitary tests")
@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class CitaNonSolitaryServiceTests {

	@Autowired
	protected CitaRepository citaRepository;


	@DisplayName("Cita Service custom queries tests")
	@Nested
	class CitaCustomServiceQueries {

		//1 -- Probando la query con el estado de la cita a null --> En realidad estamos comprobando solo por veterinarioId
		@Test
		public void findAllCitasFuturasByVeterinarioIdTest() throws Exception {
			Assertions.assertThat(CitaNonSolitaryServiceTests.this.citaRepository.findCitasFuturasByVeterinarioIdAndEstado(1, null)).size().isEqualTo(7);
		}

		//2 -- Probando la misma query que en el anterior método pero con un estado en concreto
		@Test
		public void findAllCitasFuturasByVeterinarioIdAndEstadoTest() throws Exception {
			Assertions.assertThat(CitaNonSolitaryServiceTests.this.citaRepository.findCitasFuturasByVeterinarioIdAndEstado(1, TipoEstadoCita.RECHAZADA.ordinal())).size().isEqualTo(2);
		}

		//3 -- Probando la query con el estado de la cita a null --> En realidad estamos comprobando solo por ganaderoId
		@Test
		public void findAllCitasFuturasByGanaderoIdTest() throws Exception {
			Assertions.assertThat(CitaNonSolitaryServiceTests.this.citaRepository.findCitasFuturasByGanaderoIdAndEstado(1, null)).size().isEqualTo(5);
		}

		//4 -- Probando la misma query que en el anterior método pero con un estado concreto
		@Test
		public void findAllCitasFuturasByGanaderoIdAndEstadoTest() throws Exception {
			Assertions.assertThat(CitaNonSolitaryServiceTests.this.citaRepository.findCitasFuturasByGanaderoIdAndEstado(1, TipoEstadoCita.ACEPTADA.ordinal())).size().isEqualTo(1);
		}

		//5 -- Probando la query con el estado de la cita a null --> En realidad estamos comprobando solo por explotacionGanaderaId
		@Test
		public void findAllCitasFuturasByExplotacionGanaderaIdTest() throws Exception {
			Assertions.assertThat(CitaNonSolitaryServiceTests.this.citaRepository.findCitasFuturasByExplotacionGanaderaIdAndEstado(1, null)).size().isEqualTo(5);
		}

		//6 -- Probando la misma query pero con un estado en concreto
		@Test
		public void findAllCitasFuturasByExplotacionGanaderaIdAndEstadoTest() throws Exception {
			Assertions.assertThat(CitaNonSolitaryServiceTests.this.citaRepository.findCitasFuturasByExplotacionGanaderaIdAndEstado(1, TipoEstadoCita.PENDIENTE.ordinal())).size().isEqualTo(3);
		}

		//7 -- Probando la query con todo a null, excepto el ganaderoId
		// Parámetros de la query --> (ganaderoId, veterinarioId, estadoCita, fechaInicio, fechaFin)
		@Test
		public void findAllCitasByGanaderoIdTest() throws Exception {
			Assertions.assertThat(CitaNonSolitaryServiceTests.this.citaRepository.findCitasByGanaderoIdOrVeterinarioIdAndEstadoAndDateTimeRange(2, null, null, null, null)).size().isEqualTo(8);
		}

		//8 -- Probando la query por veterinarioId, un estado en concreto y un rango de fecha (fechaInicio y fechaFin)
		// Parámetros de la query --> (ganaderoId, veterinarioId, estadoCita, fechaInicio, fechaFin)
		@Test
		public void findAllCitasByVeterinarioIdAndEstadoAndDateTimeRangeTest() throws Exception {
			Assertions.assertThat(CitaNonSolitaryServiceTests.this.citaRepository.findCitasByGanaderoIdOrVeterinarioIdAndEstadoAndDateTimeRange(null, 2, TipoEstadoCita.ACEPTADA.ordinal(), CitaNonSolitaryServiceTests.newDateFormatted("2020/10/01 11:00"),
				CitaNonSolitaryServiceTests.newDateFormatted("2020/10/01 12:00"))).size().isEqualTo(1);
		}

		//9 -- Probando la query por ganaderoId, fechaInicio y fechaFin
		// Parámetros de la query --> (ganaderoId, fechaInicio, fechaFin)
		/*
		 * Esta query se usa para comprobar cuando un ganadero va a pedir una cita para tal fecha, que no tiene
		 * otra cita en ese momento
		 */
		@Test
		public void findAllCitasAceptadasYPendientesByGanaderoIdAndDateTimeRange() throws Exception {
			Assertions.assertThat(CitaNonSolitaryServiceTests.this.citaRepository.findCitasAceptadasYPendientesByGanaderoIdAndDateTimeRange(1, CitaNonSolitaryServiceTests.newDateFormatted("2020/08/01 11:00"),
				CitaNonSolitaryServiceTests.newDateFormatted("2020/08/01 12:00"))).size().isEqualTo(1);
		}

	}


	//Métodos auxiliares
	public static Date newDateFormatted(final String fechaHoraYMinutos) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
		return sdf.parse(fechaHoraYMinutos);
	}

}
