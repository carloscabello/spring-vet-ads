
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
import org.springframework.samples.petclinic.repository.springdatajpa.LineaRecetaRepository;
import org.springframework.stereotype.Service;

@DisplayName("LineaReceta Service Non-solitary tests")
@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class LineaRecetaNonSolitaryServiceTests {

	@Autowired
	protected LineaRecetaRepository lineaRecetaRepository;


	@DisplayName("LineaReceta Service custom queries tests")
	@Nested
	class LineaRecetaCustomServiceQueries {

		//1 - Query findLineaRecetasByRecetaId(Integer recetaId)
		@Test
		public void findLineaRecetasByRecetaIdTest() throws Exception {
			Assertions.assertThat(LineaRecetaNonSolitaryServiceTests.this.lineaRecetaRepository.findLineaRecetasByRecetaId(1)).size().isEqualTo(2);
		}

		@Test
		public void findLineaRecetasByRecetaIdTestNotFound() throws Exception {
			Assertions.assertThat(LineaRecetaNonSolitaryServiceTests.this.lineaRecetaRepository.findLineaRecetasByRecetaId(999)).isEmpty();
		}

		//2 - Query findLineaRecetasByProductoId(Integer productoId)
		@Test
		public void findLineaRecetasByProductoIdTest() throws Exception {
			Assertions.assertThat(LineaRecetaNonSolitaryServiceTests.this.lineaRecetaRepository.findLineaRecetasByProductoId(4)).size().isEqualTo(3);
		}

		@Test
		public void findLineaRecetasByProductoIdTestNotFound() throws Exception {
			Assertions.assertThat(LineaRecetaNonSolitaryServiceTests.this.lineaRecetaRepository.findLineaRecetasByProductoId(999)).isEmpty();
		}

		//3 - Query findLineaRecetasByProductoAndRecetaId(Integer productoId, Integer recetaId)
		@Test
		public void findLineaRecetasByProductoAndRecetaIdTest() throws Exception {
			Assertions.assertThat(LineaRecetaNonSolitaryServiceTests.this.lineaRecetaRepository.findLineaRecetasByProductoAndRecetaId(1, 1)).size().isEqualTo(1);
		}

		@Test
		public void findLineaRecetasByProductoAndRecetaIdTestNotFound() throws Exception {
			Assertions.assertThat(LineaRecetaNonSolitaryServiceTests.this.lineaRecetaRepository.findLineaRecetasByProductoAndRecetaId(999, 999)).isEmpty();
		}

		//4 - Query numeroDeLineasRecetaByProductoId(Integer productoId)
		@Test
		public void findNumeroDeLineasRecetaByProductoIdTest() throws Exception {
			Assertions.assertThat(LineaRecetaNonSolitaryServiceTests.this.lineaRecetaRepository.numeroDeLineasRecetaByProductoId(2)).isEqualTo(2);
		}

		@Test
		public void findNumeroDeLineasRecetaByProductoIdTestNotFound() throws Exception {
			Assertions.assertThat(LineaRecetaNonSolitaryServiceTests.this.lineaRecetaRepository.numeroDeLineasRecetaByProductoId(999)).isEqualTo(0);
		}

		//5 - Query de obtener el precio total de una receta por medio de sus lineas recetas
		@Test
		public void getPrecioTotalRecetaByRecetaId() throws Exception {
			Assertions.assertThat(LineaRecetaNonSolitaryServiceTests.this.lineaRecetaRepository.getPrecioTotalRecetaByRecetaId(1)).isEqualTo(61.50);
		}

		@Test
		public void getPrecioTotalRecetaByRecetaIdRecetaNotFound() throws Exception {
			Assertions.assertThat(LineaRecetaNonSolitaryServiceTests.this.lineaRecetaRepository.getPrecioTotalRecetaByRecetaId(999)).isNull();
		}

	}

}
