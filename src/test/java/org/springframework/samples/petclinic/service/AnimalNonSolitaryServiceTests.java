
package org.springframework.samples.petclinic.service;

import java.util.Map;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.TiposGanado;
import org.springframework.samples.petclinic.repository.springdatajpa.AnimalRepository;
import org.springframework.stereotype.Service;

@DisplayName("Animal Service Non-solitary tests")
@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class AnimalNonSolitaryServiceTests {

	@Autowired
	private AnimalRepository animalRepository;


	@DisplayName("AnimalService custom queries tests")
	@Nested
	class AnimalCustomQueries {

		private final Map<String, TiposGanado> tiposGanadoMap = TipoGanadoServiceTests.defaultTiposGanado();


		@Test
		public void findAllAnimalNoLotebyExpIdGanadoIdynoArchivadoTest() throws Exception {
			Assertions.assertThat(AnimalNonSolitaryServiceTests.this.animalRepository.findAllAnimalNoLoteByExpIdYGanadoId(1, 1, false)).isNotEmpty();
		}
		@Test
		public void findAllAnimalNoLotebyExpIdGanadoIdynoArchivadoTestNotFound() throws Exception {
			Assertions.assertThat(AnimalNonSolitaryServiceTests.this.animalRepository.findAllAnimalNoLoteByExpIdYGanadoId(1, 2, false)).isEmpty();
		}

		@Test
		public void findAllAnimalNoLotebyExpIdGanadoIdyArchivadoTest() throws Exception {
			Assertions.assertThat(AnimalNonSolitaryServiceTests.this.animalRepository.findAllAnimalNoLoteByExpIdYGanadoId(1, 1, true)).isNotEmpty();
		}
		@Test
		public void findAllAnimalNoLotebyExpIdGanadoIdyArchivadoTestNotFound() throws Exception {
			Assertions.assertThat(AnimalNonSolitaryServiceTests.this.animalRepository.findAllAnimalNoLoteByExpIdYGanadoId(1, 2, true)).isEmpty();
		}

		@Test
		public void findAnimalByIdentificadorAnimalAndExpIdAndNoArchivadoTest() throws Exception {
			Assertions.assertThat(AnimalNonSolitaryServiceTests.this.animalRepository.findAnimalByIdentificadorAnimalAndExpIdAndEsArchivado("EQ09808", 1, false)).isNotEmpty();
		}
		@Test
		public void findAnimalByIdentificadorAnimalAndExpIdAndNoArchivadoTestNotFound() throws Exception {
			Assertions.assertThat(AnimalNonSolitaryServiceTests.this.animalRepository.findAnimalByIdentificadorAnimalAndExpIdAndEsArchivado("IRS9808", 1, false)).isEmpty();
		}
		@Test
		public void findAnimalByIdentificadorAnimalAndExpIdAndArchivadoTest() throws Exception {
			Assertions.assertThat(AnimalNonSolitaryServiceTests.this.animalRepository.findAnimalByIdentificadorAnimalAndExpIdAndEsArchivado("POR09814", 1, true)).isNotEmpty();
		}
		@Test
		public void findAnimalByIdentificadorAnimalAndExpIdAndArchivadoTestNotFound() throws Exception {
			Assertions.assertThat(AnimalNonSolitaryServiceTests.this.animalRepository.findAnimalByIdentificadorAnimalAndExpIdAndEsArchivado("POR09813", 1, true)).isEmpty();
		}
		@Test
		public void findAllAnimalByLoteIdNoArchivadoTest() throws Exception {
			Assertions.assertThat(AnimalNonSolitaryServiceTests.this.animalRepository.findAllAnimalByLoteId(1, false)).isNotEmpty();
		}
		@Test
		public void findAllAnimalByLoteIdNoArchivadoTestNotFound() throws Exception {
			Assertions.assertThat(AnimalNonSolitaryServiceTests.this.animalRepository.findAllAnimalByLoteId(3, false)).isEmpty();
		}
		@Test
		public void findAllAnimalByLoteIdArchivadoTest() throws Exception {
			Assertions.assertThat(AnimalNonSolitaryServiceTests.this.animalRepository.findAllAnimalByLoteId(3, true)).isNotEmpty();
		}
		@Test
		public void findAllAnimalByLoteIdArchivadoTestNotFound() throws Exception {
			Assertions.assertThat(AnimalNonSolitaryServiceTests.this.animalRepository.findAllAnimalByLoteId(1, true)).isEmpty();
		}

		@Test
		public void findAllAnimalByExplotacionIdNoArchivadoTest() throws Exception {
			Assertions.assertThat(AnimalNonSolitaryServiceTests.this.animalRepository.findAllAnimalByExpId(1, false)).isNotEmpty();
		}
		@Test
		public void findAllAnimalByExplotacionIdArchivadoTest() throws Exception {
			Assertions.assertThat(AnimalNonSolitaryServiceTests.this.animalRepository.findAllAnimalByExpId(1, true)).isNotEmpty();
		}
		@Test
		public void findAllAnimalByExplotacionIdNoArchivadoTestNotFound() throws Exception {
			Assertions.assertThat(AnimalNonSolitaryServiceTests.this.animalRepository.findAllAnimalByExpId(8, false)).isEmpty();
		}
		@Test
		public void findAllAnimalByExplotacionIdArchivadoTestNotFound() throws Exception {
			Assertions.assertThat(AnimalNonSolitaryServiceTests.this.animalRepository.findAllAnimalByExpId(4, true)).isEmpty();
		}
		@Test
		public void findAnimalByAnimalIdentificadorAnimalTest() throws Exception {
			Assertions.assertThat(AnimalNonSolitaryServiceTests.this.animalRepository.findAnimalByAnimalIdentificadorAnimal("L292820380492-001")).isNotEmpty();
		}
		@Test
		public void findAnimalByAnimalIdentificadorAnimalTestNotFound() throws Exception {
			Assertions.assertThat(AnimalNonSolitaryServiceTests.this.animalRepository.findAnimalByAnimalIdentificadorAnimal("L292828282-001")).isEmpty();
		}

		@Test
		public void findAllAnimalByExplotacionIdyGanadoIDNoArchivadoTest() throws Exception {
			Assertions.assertThat(AnimalNonSolitaryServiceTests.this.animalRepository.findAllAnimalByExpIdYGanadoIYEsArchivado(1, 1, false)).isNotEmpty();
		}
		@Test
		public void findAllAnimalByExplotacionIdyGanadoIDArchivadoTest() throws Exception {
			Assertions.assertThat(AnimalNonSolitaryServiceTests.this.animalRepository.findAllAnimalByExpIdYGanadoIYEsArchivado(1, 1, true)).isNotEmpty();
		}
		@Test
		public void findAllAnimalByExplotacionIdyGanadoIDNoArchivadoTestNotFound() throws Exception {
			Assertions.assertThat(AnimalNonSolitaryServiceTests.this.animalRepository.findAllAnimalByExpIdYGanadoIYEsArchivado(3, 1, false)).isEmpty();
		}
		@Test
		public void findAllAnimalByExplotacionIdyGanadoIDArchivadoTestNotFound() throws Exception {
			Assertions.assertThat(AnimalNonSolitaryServiceTests.this.animalRepository.findAllAnimalByExpIdYGanadoIYEsArchivado(3, 1, true)).isEmpty();
		}

	}

}
