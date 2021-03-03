
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
import org.springframework.samples.petclinic.repository.springdatajpa.RecetaRepository;
import org.springframework.stereotype.Service;

@DisplayName("Cita Service Non-solitary tests")
@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class RecetaNonSolitaryServiceTests {

	@Autowired
	protected RecetaRepository recetaRepository;


	@DisplayName("Receta Service custom queries tests")
	@Nested
	class RecetaCustomServiceQueries {

		//1 - Query findRecetasByContratoId(Integer contratoId)
		@Test
		public void findRecetasByContratoIdTest() throws Exception {
			Assertions.assertThat(RecetaNonSolitaryServiceTests.this.recetaRepository.findRecetasByContratoId(1)).size().isEqualTo(2);
		}

		@Test
		public void findRecetasByContratoIdTestNotFound() throws Exception {
			Assertions.assertThat(RecetaNonSolitaryServiceTests.this.recetaRepository.findRecetasByContratoId(999)).isEmpty();
		}

		//2 - Query findRecetasByCitaId(Integer citaId)
		@Test
		public void findRecetasByCitaIdTest() throws Exception {
			Assertions.assertThat(RecetaNonSolitaryServiceTests.this.recetaRepository.findRecetasByCitaId(10)).size().isEqualTo(3);
		}

		@Test
		public void findRecetasByCitaIdTestNotFound() throws Exception {
			Assertions.assertThat(RecetaNonSolitaryServiceTests.this.recetaRepository.findRecetasByCitaId(999)).isEmpty();
		}

		//3 - Query findRecetasByGanaderoId(Integer ganaderoId)
		@Test
		public void findRecetasByGanaderoIdTest() throws Exception {
			Assertions.assertThat(RecetaNonSolitaryServiceTests.this.recetaRepository.findRecetasByGanaderoId(1)).size().isEqualTo(5);
		}

		@Test
		public void findRecetasByGanaderoIdTestNotFound() throws Exception {
			Assertions.assertThat(RecetaNonSolitaryServiceTests.this.recetaRepository.findRecetasByGanaderoId(999)).isEmpty();
		}

		//4 - Query findRecetasByVetId(Integer vetId)
		@Test
		public void findRecetasByVetIdTest() throws Exception {
			Assertions.assertThat(RecetaNonSolitaryServiceTests.this.recetaRepository.findRecetasByVetId(1)).size().isEqualTo(6);
		}

		@Test
		public void findRecetasByVetIdTestNotFound() throws Exception {
			Assertions.assertThat(RecetaNonSolitaryServiceTests.this.recetaRepository.findRecetasByVetId(999)).isEmpty();
		}

		//5 - Query findRecetasByContratoIdAndNoEsFacturado(int contratoId)
		//Esta query se prueba porque esta creada manualmente para cosas a la query
		@Test
		public void findRecetasByContratoIdAndNoEsFacturado() throws Exception {
			Assertions.assertThat(RecetaNonSolitaryServiceTests.this.recetaRepository.findRecetasByContratoIdAndNoEsFacturado(2)).size().isEqualTo(1);
		}

		@Test
		public void findRecetasByContratoIdAndNoEsFacturadoNotFound() throws Exception {
			Assertions.assertThat(RecetaNonSolitaryServiceTests.this.recetaRepository.findRecetasByContratoIdAndNoEsFacturado(999)).isEmpty();
		}

	}

}
