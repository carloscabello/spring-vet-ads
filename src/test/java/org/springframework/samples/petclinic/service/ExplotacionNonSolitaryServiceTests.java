
package org.springframework.samples.petclinic.service;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.ExplotacionGanadera;
import org.springframework.samples.petclinic.repository.springdatajpa.ExplotacionGanaderaRepository;
import org.springframework.stereotype.Service;

@DisplayName("Explotaciones ganaderas Non-solitary tests")
@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class ExplotacionNonSolitaryServiceTests {

	@Autowired
	private ExplotacionGanaderaRepository	explotacionGanaderaRepository;

	@Autowired
	protected ExplotacionGanaderaService	explotacionGanaderaService;


	@DisplayName("ExplotacionGanaderaService custom queries tests")
	@Nested
	class ExplotacionesCustomQueries {

		//private final Map<String, TiposGanado> tiposGanadoMap = TipoGanadoServiceTests.defaultTiposGanado();

		@ParameterizedTest()
		@ValueSource(strings = {
			"53728343", "56728942", "89012380"
		})
		public void shouldFindExpGanaderaByNumeroRegistro(final String numeroRegistro) throws Exception {
			Optional<ExplotacionGanadera> res = ExplotacionNonSolitaryServiceTests.this.explotacionGanaderaService.findExpGanaderaByNumeroRegistro(numeroRegistro);
			Assertions.assertThat(res).isNotEmpty();
		}

		@ParameterizedTest()
		@ValueSource(strings = {
			"1", "2", "3"
		})
		public void shouldNotFindExpGanaderaByNumeroRegistro(final String numeroRegistro) throws Exception {
			Optional<ExplotacionGanadera> res = ExplotacionNonSolitaryServiceTests.this.explotacionGanaderaService.findExpGanaderaByNumeroRegistro(numeroRegistro);
			Assertions.assertThat(res).isEmpty();
		}

		@Test
		public void findAllExplotacionesByGanaderoID() throws Exception {
			Assertions.assertThat(ExplotacionNonSolitaryServiceTests.this.explotacionGanaderaRepository.findAllExpGanaderaByGanaderoId(2, false)).isNotEmpty();
		}

		@Test
		public void findAllExplotacionesByGanaderoIDArchived() throws Exception {
			Assertions.assertThat(ExplotacionNonSolitaryServiceTests.this.explotacionGanaderaRepository.findAllExpGanaderaByGanaderoId(2, true)).isNotEmpty();
		}

		@Test
		public void findAllExplotacionesByGanaderoIDNotFound() throws Exception {
			Assertions.assertThat(ExplotacionNonSolitaryServiceTests.this.explotacionGanaderaRepository.findAllExpGanaderaByGanaderoId(6, false)).isEmpty();
		}

		@Test
		public void findAllExplotacionesByGanaderoIDArchivedNotFound() throws Exception {
			Assertions.assertThat(ExplotacionNonSolitaryServiceTests.this.explotacionGanaderaRepository.findAllExpGanaderaByGanaderoId(3, true)).isEmpty();
		}

		@Test
		public void findExplotacionByNumero() throws Exception {
			Assertions.assertThat(ExplotacionNonSolitaryServiceTests.this.explotacionGanaderaRepository.findExpGanaderaByNumeroRegistro("53728343")).isNotEmpty();
		}

		@Test
		public void findExplotacionByNumeroNotFound() throws Exception {
			Assertions.assertThat(ExplotacionNonSolitaryServiceTests.this.explotacionGanaderaRepository.findExpGanaderaByNumeroRegistro("20")).isEmpty();
		}

		@Test
		public void findExplotacionByVeterinarioID() throws Exception {
			Assertions.assertThat(ExplotacionNonSolitaryServiceTests.this.explotacionGanaderaRepository.findAllExpGanaderaByVeterinarioId(1)).isNotEmpty();
		}

		@Test
		public void findExplotacionByVeterinarioIDNotFound() throws Exception {
			Assertions.assertThat(ExplotacionNonSolitaryServiceTests.this.explotacionGanaderaRepository.findAllExpGanaderaByVeterinarioId(6)).isEmpty();
		}
	}

}
