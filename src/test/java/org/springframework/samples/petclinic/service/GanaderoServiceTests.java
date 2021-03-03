
package org.springframework.samples.petclinic.service;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
import org.springframework.samples.petclinic.model.Ganadero;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.repository.springdatajpa.GanaderoRepository;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class GanaderoServiceTests {
	
	/* Mockear el los servicios auxiliares */

	@Mock
	protected UserService			userService;

	@Mock
	protected AuthoritiesService	authoritiesService;

	/* Mockear el repositorio */
	@Mock
	private GanaderoRepository		ganaderoRepository;

	/* Inyectamos los mocks al servicio que estamos probando */
	@InjectMocks
	protected GanaderoService ganaderoService;
	
	private static Stream<Ganadero> shouldInsertGanaderoData(){
		Stream<Ganadero> res;

		Ganadero ganaderoPos1 = GanaderoServiceTests
				.generateGanadero("Antonio", "Banderas", "0123456789", "anton@mail.com", 
						"Sevilla", "Dos Hermanas", "123456789A", "41345", "Calle Antonio Mairena, 8", "antonio1", "12345");
		Ganadero ganaderoPos2 = GanaderoServiceTests
				.generateGanadero("Lucas", "Contreras Domenech", "189892902", "l12222@mail.com", 
						"Málaga", "Marbella", "123456789I", "10100", "Avenida de los ilustrísimos 81, portal A", "lucas1", "12345");
		Ganadero ganaderoPos3 = GanaderoServiceTests
				.generateGanadero("Julio", "Iglesias Gañán", "809809872", "julio@mail.com", 
						"Bormujos", "Sevilla", "000000009B", "41089", "Calle Julio Iglesias 9", "julio", "54321");
		
		res = Stream.of(ganaderoPos1, ganaderoPos2, ganaderoPos3);
		return res;

	}

	@ParameterizedTest()
	@MethodSource("shouldInsertGanaderoData")
	void shouldInsertGanadero(final Ganadero ganadero) {

		this.ganaderoService.saveGanadero(ganadero);
		verify(this.ganaderoRepository).save(ganadero);
		verify(this.userService).saveUser(ganadero.getUser());
		verify(this.authoritiesService).saveAuthorities(ganadero.getUser().getUsername(), "ganadero");

	}
	
	
	/*Método a probar: findGanaderoByLogedUser()
	 * Caso (Positivo) : El username del usuario loguado corresponde a un Ganadero en la BD.*/
	@Test
	public void shouldfindGanaderoByLogedUser() {
		/*1. Arrange
		 * hacemos los stubs necesarios para el mockGanadero1*/
		Ganadero mockGanadero1 = GanaderoServiceTests
				.generateGanadero("", "", "", "", "", "", "", "", "", "mockGanadero1", "12345");
		when(ganaderoRepository.findGanaderoByUsername("mockGanadero1")).thenReturn(mockGanadero1);
		when(userService.getAuthenticatedUsername()).thenReturn("mockGanadero1");
		//2. Act
		Ganadero res = ganaderoService.findGanaderoByLogedUser();
		//3.Assert
		verify(ganaderoRepository).findGanaderoByUsername("mockGanadero1");
		Assertions.assertThat(res).isEqualTo(mockGanadero1);
	}
	
	
	/*Método a probar: findGanaderoByLogedUser()
	 * Caso (Negativo) : El username del usuario loguado NO corresponde a un Ganadero en la BD.
	 * Comportamiento esperado: Se devuelve el ganadero como null.*/
	@Test
	public void shouldNotfindGanaderoByLogedUser() {
		/*1. Arrange
		 * hacemos los stubs necesarios para el mockBadUsername*/
		when(userService.getAuthenticatedUsername()).thenReturn("mockBadUsername");
		when(ganaderoRepository.findGanaderoByUsername("mockBadUsername")).thenReturn(null);

		//2. Act
			Ganadero res = ganaderoService.findGanaderoByLogedUser();
		//3.Assert
		verify(ganaderoRepository).findGanaderoByUsername("mockBadUsername");
		Assertions.assertThat(res).isNull();
	}
//	@Test
//	@Disabled
//	public void shouldNotfindGanaderoByLogedUser() {
//		/*1. Arrange
//		 * hacemos los stubs necesarios para el mockBadUsername*/
//		Ganadero mockGanadero1 = GanaderoServiceTests
//				.generateGanadero("", "", "", "", "", "", "", "", "", "mockGanadero1", "12345");
//		when(ganaderoRepository.findGanaderoByUsername(any(String.class)))
//			.thenThrow(new DataAccessException("") {});
//		when(userService.getAuthenticatedUsername()).thenReturn("mockBadUsername");
//
//		//2. Act
//		Assertions.assertThatThrownBy(() -> {
//			Ganadero res = ganaderoService.findGanaderoByLogedUser();
//		}).isInstanceOf(DataAccessException.class);
//		//3.Assert
//		verify(ganaderoRepository).findGanaderoByUsername("mockBadUsername");
//	}

	static public Ganadero generateGanadero(final String firstName, final String lastName, final String telephone, final String mail, final String province, final String city, final String dni, final String postalCode, final String address,
		final String username, final String password) {
		Ganadero ganadero = new Ganadero();
		User user = new User();

		//Asignaciones user
		user.setUsername(username);
		user.setPassword(password);
		ganadero.setUser(user);

		//Asignaciones ganadero
		ganadero.setFirstName(firstName);
		ganadero.setLastName(lastName);
		ganadero.setTelephone(telephone);
		ganadero.setMail(mail);
		ganadero.setProvince(province);
		ganadero.setCity(city);
		ganadero.setDni(dni);
		ganadero.setPostalCode(postalCode);
		ganadero.setAddress(address);

		return ganadero;
	}
}
