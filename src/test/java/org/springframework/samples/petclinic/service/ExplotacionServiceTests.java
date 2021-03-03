
package org.springframework.samples.petclinic.service;

import java.text.ParseException;
import java.util.stream.Stream;

import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.samples.petclinic.model.ExplotacionGanadera;
import org.springframework.samples.petclinic.model.Ganadero;
import org.springframework.samples.petclinic.repository.springdatajpa.ExplotacionGanaderaRepository;

@ExtendWith(MockitoExtension.class)
public class ExplotacionServiceTests {

	@Mock
	private ExplotacionGanaderaRepository	expGanRepository;

	@Mock
	private GanaderoService					ganaderoService;

	@Mock
	private VetService						veterinarioService;

	@InjectMocks
	protected ExplotacionGanaderaService	explotacionGanaderaService;


	/*
	 * Método a probar: saveExpGanadera()
	 * Caso (Positivo) : Se guarda una nueva explotacion, que corresponde al ganadero que esta logueado.
	 */
	@Test
	void shouldInsertExpGanadera() {
		/* 1. Arrange */
		Ganadero mockGanadero = GanaderoServiceTests.generateGanadero("Antonio", "Banderas", "0123456789", "anton@mail.com", "Sevilla", "Dos Hermanas", "123456789A", "41345", "Calle Antonio Mairena, 8", "antonio1", "12345");
		ExplotacionGanadera explotacionPos1 = ExplotacionServiceTests.generateExplotacion("53728343", "Granada", null, "Moncayo", null);
		Mockito.when(this.ganaderoService.findGanaderoByLogedUser()).thenReturn(mockGanadero);
		ArgumentCaptor<ExplotacionGanadera> argument = ArgumentCaptor.forClass(ExplotacionGanadera.class);
		/* 2. Act */
		this.explotacionGanaderaService.saveExpGanadera(explotacionPos1);
		/* 3. Assert */
		Mockito.verify(this.expGanRepository).save(argument.capture());
		/* La explotacion se guarda con el Ganadero logueado, y con esArchivado a falso */
		Assertions.assertThat(argument.getValue().getGanadero()).isEqualTo(mockGanadero);
		Assertions.assertThat(argument.getValue().getEsArchivado()).isEqualTo(false);
	}

	private static Stream<ExplotacionGanadera> shouldArchivarExplotacionGanaderaData() throws ParseException {
		Ganadero ganadero1 = GanaderoServiceTests.generateGanadero("Sora", "Riku", "622222222", "kingdom@gmail.com", "Badajoz", "Hinojosa del Valle", "45556839L", "06226", "Calle Islas del Destino", "soriku", "kairi4e");
		ExplotacionGanadera expGanadera1 = ExplotacionServiceTests.generateExplotacion("1", "Los Antruejos", false, "Villaricos", ganadero1);

		Ganadero ganadero2 = GanaderoServiceTests.generateGanadero("Antonio", "Ossorio", "722324576", "emisorio@gmail.com", "Badajoz", "Hinojosa del Valle", "45559312L", "06220", "Calle Constitucion", "ossorio", "galleta");
		ExplotacionGanadera expGanadera2 = ExplotacionServiceTests.generateExplotacion("2", "Cañada Real", false, "Tomorrowland", ganadero2);

		Stream<ExplotacionGanadera> res = Stream.of(expGanadera1, expGanadera2);
		return res;
	}

	@ParameterizedTest()
	@MethodSource("shouldArchivarExplotacionGanaderaData")
	void shouldArchivarExplotacionGanadera(final ExplotacionGanadera expGanadera) {
		this.explotacionGanaderaService.archivarExpGanadera(expGanadera);
		Mockito.verify(this.expGanRepository).save(expGanadera);
		Assert.assertEquals(true, expGanadera.getEsArchivado());
	}

	public static ExplotacionGanadera generateExplotacion(final String numeroRegistro, final String terminoMunicipal, final Boolean esArchivado, final String name, final Ganadero ganadero) {
		ExplotacionGanadera res = new ExplotacionGanadera();
		res.setNumeroRegistro(numeroRegistro);
		res.setTerminoMunicipal(terminoMunicipal);
		res.setEsArchivado(esArchivado);
		res.setName(name);
		res.setGanadero(ganadero);

		return res;
	}

}
