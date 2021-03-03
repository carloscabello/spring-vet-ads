
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
import org.springframework.stereotype.Service;

@DisplayName("Archivar Service Non-solitary tests")
@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class ArchivarNonSolitaryServiceTests {

	@Autowired
	protected AnimalHistoricoService animalHistoricoService;


	@DisplayName("Archivar Service custom queries tests")
	@Nested
	class ArchivarGanadoCustomServiceQueries {

		@Test
		public void findOneTest() throws Exception {
			Assertions.assertThat(ArchivarNonSolitaryServiceTests.this.animalHistoricoService.findAnimalHistoricoByAnimalId(61)).isNotEmpty();
		}

		@Test
		public void findOneTestNotFound() throws Exception {
			Assertions.assertThat(ArchivarNonSolitaryServiceTests.this.animalHistoricoService.findAnimalHistoricoByAnimalId(9999)).isEmpty();
		}

	}
}
