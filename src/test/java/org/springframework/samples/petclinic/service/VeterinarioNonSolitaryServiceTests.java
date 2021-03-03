package org.springframework.samples.petclinic.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.TiposGanado;
import org.springframework.samples.petclinic.model.Veterinario;
import org.springframework.samples.petclinic.repository.springdatajpa.VetRepository;
import org.springframework.stereotype.Service;

import com.google.common.collect.ImmutableList;

@DisplayName("Ganadero Service Non-solitary tests")
@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class VeterinarioNonSolitaryServiceTests {
	
	@Autowired
	protected VetRepository vetRepository;
	
	@Autowired
	protected VetService vetService;
	
	@DisplayName("VeterinarioService custom queries tests")
	@Nested
	class VeterinarioCustomServiceQueries{
		
		private final Map<String, TiposGanado> tiposGanadoMap = TipoGanadoServiceTests.defaultTiposGanado();
		
		@ParameterizedTest()
		@CsvSource({
			"Porcino, 3",
			"Caprino, 4",
			"Avicola, 3"
		})
		public void encontrarPorEspecialidadTests(String especialidad, Integer expectedVets) {
			Iterable<Veterinario> queryResult =vetService.encontrarPorEspecialidad(tiposGanadoMap.get(especialidad).getId());
			List<Veterinario> vetListResult = ImmutableList.copyOf(queryResult);
			
			Assertions.assertThat(vetListResult).isNotNull();
			Assertions.assertThat(vetListResult).isNotEmpty();
			Assertions.assertThat(expectedVets).isEqualTo(vetListResult.size());
		}
		
		@ParameterizedTest()
		@ValueSource(strings = {"45556952J", "25625652A", "19026652Q"})
		public void findVeterinarioByDni(String dni) {
			Optional<Veterinario> queryResult =vetService.findVeterinarioByDni(dni);
			
			Assertions.assertThat(queryResult).isNotNull();
			Assertions.assertThat(queryResult).isNotEmpty();
			Assertions.assertThat(queryResult.get().getDni()).isEqualTo(dni);
		}
		
		/*Los findOne no hace falta probarlos*/
		
	}

}
