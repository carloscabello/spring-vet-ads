package org.springframework.samples.petclinic.service;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.samples.petclinic.model.TiposGanado;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.model.Veterinario;
import org.springframework.samples.petclinic.repository.springdatajpa.VetRepository;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class VeterinarioServiceTests {
	
	/* Mockear el los servicios auxiliares */
	@Mock
	protected UserService			userService;

	@Mock
	protected AuthoritiesService	authoritiesService;
	
	/* Mockear el repositorio */
	@Mock
	private VetRepository vetRepository;
	
	/* Inyectamos los mocks al servicio que estamos probando*/
	@InjectMocks
	protected VetService vetService;
	
	/* Varible de utilidad para consultar los tipos de ganado */
	private static Map<String, TiposGanado> tiposGanadoMap = TipoGanadoServiceTests.defaultTiposGanado();
	
	
	private static Stream<Veterinario> shouldInsertVeterinarioData(){
		Stream<Veterinario> res;
		
		List<TiposGanado> especialidadesPos1 = Stream.of(
				tiposGanadoMap.get("Porcino"),
				tiposGanadoMap.get("Ovino"))
				.collect(Collectors.toList());
		Veterinario veterinarioPos1 = generateVeterinario("Julián", "Banderas", "0123456789", 
				"anton@mail.com", "Sevilla", "Dos Hermanas", "12345678A", especialidadesPos1, true, "julian", "12345");
		
		List<TiposGanado> especialidadesPos2 = Stream.of(
				tiposGanadoMap.get("Vacuno"),
				tiposGanadoMap.get("Caprino"))
				.collect(Collectors.toList());
		Veterinario veterinarioPos2 = generateVeterinario("Carlo", "Magno Alexandrio", "987654321", 
				"carlo@mail.com", "Extremadura", "El Gasco", "12344321G", especialidadesPos2, false, "carlo", "54321");
		
		List<TiposGanado> especialidadesPos3 = Stream.of(
				tiposGanadoMap.get("Equino"),
				tiposGanadoMap.get("Asnal"),
				tiposGanadoMap.get("Avicola"))
				.collect(Collectors.toList());
		Veterinario veterinarioPos3 = generateVeterinario("Tulio", "Iglesias Romero", "987654321", 
				"iglesias@mail", "Extremadura", "El Gasco", "12344321X", especialidadesPos3, true, "carlo", "54321");
		
		res = Stream.of(veterinarioPos1, veterinarioPos2, veterinarioPos3);
		
		return res;
	}
	
	@ParameterizedTest()
	@MethodSource("shouldInsertVeterinarioData")
	void shouldInsertVeterinario(Veterinario veterinario) {
		//1. Arrange
		//2. Act
		this.vetService.saveVet(veterinario);
		//3.Assert
		verify(this.vetRepository).save(veterinario);
		verify(this.userService).saveUser(veterinario.getUser());
		verify(this.authoritiesService).saveAuthorities(veterinario.getUser().getUsername(),"veterinario");
	}
	
	/*Método a probar: findVeterinarioByLogedUser()
	 * Caso (Positivo) : El username del usuario loguado corresponde a un Ganadero en la BD.*/
	@Test
	void shouldFindVeterinarioByLoggedUser() {
		/*1. Arrange
		 * hacemos los stubs necesarios para el mockVet1*/
		Veterinario mockVet1 = generateVeterinario("Tulio", "Iglesias Romero", "987654321", 
				"iglesias@mail", "Extremadura", "El Gasco", "12344321X", new ArrayList<TiposGanado>(), true, "mockVet1", "12345");
		when(vetRepository.findVeterinarioByUsername("mockVet1")).thenReturn(mockVet1);
		when(userService.getAuthenticatedUsername()).thenReturn("mockVet1");
		//2. Act
		Veterinario res = vetService.findVeterinarioByLogedUser();
		verify(vetRepository).findVeterinarioByUsername("mockVet1");
		Assertions.assertThat(res).isEqualTo(mockVet1);
	}
	
	/*Método a probar: findVeterinarioByLogedUser()
	 * Caso (Negativo) : El username del usuario loguado NO corresponde a un Ganadero en la BD.
	 * Comportamiento esperado: Se devuelve el veterinario como null.*/
	@Test
	void shouldNotFindVeterinarioByLoggedUser() {
		/*1. Arrange
		 * hacemos los stubs necesarios para el mockVet1*/
		when(userService.getAuthenticatedUsername()).thenReturn("mockBadUsername");
		when(vetRepository.findVeterinarioByUsername("mockBadUsername")).thenReturn(null);
		//2. Act
		Veterinario res = vetService.findVeterinarioByLogedUser();
		verify(vetRepository).findVeterinarioByUsername("mockBadUsername");
		Assertions.assertThat(res).isNull();
	}
	
	static public Veterinario generateVeterinario(final String firstName, final String lastName, final String telephone, 
			final String mail, final String province, final String city, final String dni, List<TiposGanado> tiposGanado, 
			Boolean esDisponible, final String username, final String password) {
			Veterinario veterinario = new Veterinario();
			User user = new User();

			//Asignaciones user
			user.setUsername(username);
			user.setPassword(password);
			veterinario.setUser(user);

			//Asignaciones ganadero
			veterinario.setFirstName(firstName);
			veterinario.setLastName(lastName);
			veterinario.setTelephone(telephone);
			veterinario.setMail(mail);
			veterinario.setProvince(province);
			veterinario.setCity(city);
			veterinario.setDni(dni);
			veterinario.setEsDisponible(esDisponible);
			veterinario.setTiposGanado(tiposGanado);

			return veterinario;
		}

}
