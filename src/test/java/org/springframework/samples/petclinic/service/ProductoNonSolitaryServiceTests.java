
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
import org.springframework.samples.petclinic.repository.springdatajpa.ProductoRepository;
import org.springframework.stereotype.Service;

@DisplayName("Producto Service Non-solitary tests")
@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class ProductoNonSolitaryServiceTests {

	@Autowired
	private ProductoRepository	productoRepository;

	@Autowired
	private VetService			vetService;

	@Autowired
	private ProductoService		ProductoService;


	@DisplayName("Producto Service custom queries tests")
	@Nested
	class ProductoCustomServiceQueries {

		@Test
		public void findAllProductosByVeterinarioIdTest() throws Exception {
			Assertions.assertThat(ProductoNonSolitaryServiceTests.this.productoRepository.findAllProductosByVeterinarioId(1)).isNotEmpty();
		}

		@Test
		public void findAllProductosByVeterinarioIdTestNotFound() throws Exception {
			Assertions.assertThat(ProductoNonSolitaryServiceTests.this.productoRepository.findAllProductosByVeterinarioId(3)).isEmpty();
		}

		@Test
		public void findProductoByNameYVetIdTest() throws Exception {
			Assertions.assertThat(ProductoNonSolitaryServiceTests.this.productoRepository.findProductoByNameYVetId("Thrombocid", 1)).isNotEmpty();
		}

		@Test
		public void findProductoByNameYVetIdTestNotFound() throws Exception {
			Assertions.assertThat(ProductoNonSolitaryServiceTests.this.productoRepository.findProductoByNameYVetId("Medicamento que no existe", 3)).isEmpty();
		}
		@Test
		public void numeroProductosConExistenciasByVeterinarioId() throws Exception {
			Assertions.assertThat(ProductoNonSolitaryServiceTests.this.productoRepository.numeroProductosConExistenciasByVeterinarioId(1)).isEqualTo(2);
		}
		@Test
		public void numeroProductosConExistenciasByVeterinarioIdNotFound() throws Exception {
			Assertions.assertThat(ProductoNonSolitaryServiceTests.this.productoRepository.numeroProductosConExistenciasByVeterinarioId(3)).isEqualTo(0);
		}
		@Test
		public void findAllProductosConExistenciasByVeterinarioIdAndNoNecesitanReceta() throws Exception {
			Assertions.assertThat(ProductoNonSolitaryServiceTests.this.productoRepository.findAllProductosConExistenciasByVeterinarioIdAndNoNecesitanReceta(1)).isNotEmpty();
		}
		@Test
		public void findAllProductosConExistenciasByVeterinarioIdAndNoNecesitanRecetaNotFound() throws Exception {
			Assertions.assertThat(ProductoNonSolitaryServiceTests.this.productoRepository.findAllProductosConExistenciasByVeterinarioIdAndNoNecesitanReceta(3)).isEmpty();
		}
	}


	@Test
	public void findProductoByIdTestNotFound() throws Exception {
		Assertions.assertThat(ProductoNonSolitaryServiceTests.this.ProductoService.findProductoById(1)).isNotEmpty();
	}

	@Test
	public void findProductoByIdTest() throws Exception {
		Assertions.assertThat(ProductoNonSolitaryServiceTests.this.ProductoService.findProductoById(99)).isEmpty();
	}

}
