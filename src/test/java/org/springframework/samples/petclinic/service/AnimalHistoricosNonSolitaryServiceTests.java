
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
import org.springframework.samples.petclinic.repository.springdatajpa.AnimalHistoricoRepository;
import org.springframework.stereotype.Service;

@DisplayName("Animal historico Service Non-solitary tests")
@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class AnimalHistoricosNonSolitaryServiceTests {

	@Autowired
	private AnimalHistoricoRepository animalHistoricoRepository;


	@DisplayName("Animal historico Service custom queries tests")
	@Nested
	class AnimalHistoricoCustomServiceQueries {

		@Test
		public void findAnimalHistoricoByAnimalIdTest() throws Exception {
			Assertions.assertThat(AnimalHistoricosNonSolitaryServiceTests.this.animalHistoricoRepository.findAnimalHistoricoByAnimalId(61)).isNotEmpty();
		}
		@Test
		public void findAnimalHistoricoByAnimalIdTestNotFound() throws Exception {
			Assertions.assertThat(AnimalHistoricosNonSolitaryServiceTests.this.animalHistoricoRepository.findAnimalHistoricoByAnimalId(1)).isEmpty();
		}

	}

}
