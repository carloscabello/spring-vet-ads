
package org.springframework.samples.petclinic.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.TipoEstadoContrato;
import org.springframework.samples.petclinic.repository.springdatajpa.ContratoRepository;
import org.springframework.stereotype.Service;

@DisplayName("Contrato Service Non-solitary tests")
@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class ContratoNonSolitaryServiceTests {

	@Autowired
	protected ContratoService		contratoService;

	@Autowired
	protected ContratoRepository	contratoRepository;

	@Autowired
	protected VetService			vetService;


	@DisplayName("Contrato Service custom queries tests")
	@Nested
	class ContratoCustomServiceQueries {

		@Test
		public void findOneContratoByVetIdTest() throws Exception {
			Assertions.assertThat(ContratoNonSolitaryServiceTests.this.contratoRepository.findContratosByVeterinarioId(1)).isNotEmpty();
		}

		@Test
		public void findOneContratoByVetIdTestNotFound() throws Exception {
			Assertions.assertThat(ContratoNonSolitaryServiceTests.this.contratoRepository.findContratosByVeterinarioId(999)).isEmpty();
		}

		@Test
		public void findOneContratoByGanaderoIdTest() throws Exception {
			Assertions.assertThat(ContratoNonSolitaryServiceTests.this.contratoRepository.findContratosByGanaderoId(1)).isNotEmpty();
		}

		@Test
		public void findOneContratoByGanaderoIdTestNotFound() throws Exception {
			Assertions.assertThat(ContratoNonSolitaryServiceTests.this.contratoRepository.findContratosByGanaderoId(999)).isEmpty();
		}
		@Test
		public void findContratosVigentesAceptadosByGanaderoIdTest() throws Exception {
			Assertions.assertThat(ContratoNonSolitaryServiceTests.this.contratoRepository.findContratosVigentesAceptadosByGanaderoId(1)).isNotEmpty();
		}

		@Test
		public void findContratosVigentesAceptadosByGanaderoIdTestNotFound() throws Exception {
			Assertions.assertThat(ContratoNonSolitaryServiceTests.this.contratoRepository.findContratosVigentesAceptadosByGanaderoId(999)).isEmpty();
		}
		@Test
		public void findAllContratosVigentesByVeterinarioIdTest() throws Exception {
			Assertions.assertThat(ContratoNonSolitaryServiceTests.this.contratoRepository.findAllContratosVigentesByVeterinarioId(1)).isNotEmpty();
		}

		@Test
		public void findAllContratosVigentesByVeterinarioIdTestNotFound() throws Exception {
			Assertions.assertThat(ContratoNonSolitaryServiceTests.this.contratoRepository.findAllContratosVigentesByVeterinarioId(999)).isEmpty();
		}
		@Test
		public void findAllContratosByExpGanaderaAndEstadoTest() throws Exception {
			Assertions.assertThat(ContratoNonSolitaryServiceTests.this.contratoRepository.findAllContratosByExpGanaderaAndEstado(1, TipoEstadoContrato.ACEPTADO)).isNotEmpty();
		}

		@Test
		public void findAllContratosByExpGanaderaAndEstadoIdTestNotFound() throws Exception {
			Assertions.assertThat(ContratoNonSolitaryServiceTests.this.contratoRepository.findAllContratosByExpGanaderaAndEstado(999, TipoEstadoContrato.FINALIZADO)).isEmpty();
		}
		@Test
		public void findAllContratosVigentesByExpGanaderaAndVeterinarioAndEstadoTest() throws Exception {
			Assertions.assertThat(ContratoNonSolitaryServiceTests.this.contratoRepository.findAllContratosVigentesByExpGanaderaAndVeterinarioAndEstado(1, 1, TipoEstadoContrato.ACEPTADO)).isNotEmpty();
		}

		@Test
		public void findAllContratosVigentesByExpGanaderaAndVeterinarioAndEstadoTestNotFound() throws Exception {
			Assertions.assertThat(ContratoNonSolitaryServiceTests.this.contratoRepository.findAllContratosVigentesByExpGanaderaAndVeterinarioAndEstado(999, 12, TipoEstadoContrato.FINALIZADO)).isEmpty();
		}
		@Test
		public void findAllContratosByExpGanaderaIdTest() throws Exception {
			Assertions.assertThat(ContratoNonSolitaryServiceTests.this.contratoRepository.findAllContratosByExpGanaderaId(1)).isNotEmpty();
		}

		@Test
		public void findAllContratosByExpGanaderaIdTestNotFound() throws Exception {
			Assertions.assertThat(ContratoNonSolitaryServiceTests.this.contratoRepository.findAllContratosByExpGanaderaId(999)).isEmpty();
		}
	}


	@Test
	public void findOneContratoTest() throws Exception {
		Assertions.assertThat(ContratoNonSolitaryServiceTests.this.contratoService.findContratoById(1)).isNotEmpty();
	}

	@Test
	public void findOneContratoTestNotFound() throws Exception {
		Assertions.assertThat(ContratoNonSolitaryServiceTests.this.contratoService.findContratoById(99)).isEmpty();
	}
}
