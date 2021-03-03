
package org.springframework.samples.petclinic.service;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Map;
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
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.samples.petclinic.forms.LoteForm;
import org.springframework.samples.petclinic.model.ExplotacionGanadera;
import org.springframework.samples.petclinic.model.Ganadero;
import org.springframework.samples.petclinic.model.Lote;
import org.springframework.samples.petclinic.model.TiposGanado;
import org.springframework.samples.petclinic.repository.springdatajpa.LoteRepository;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class LoteServiceTests {

	@Mock
	protected LoteRepository				loteRepository;

	@InjectMocks
	protected LoteService					loteService;

	/* Varible de utilidad para consultar los tipos de ganado */
	private static Map<String, TiposGanado>	tiposGanadoMap	= TipoGanadoServiceTests.defaultTiposGanado();


	void resetMockUps() {
		Mockito.reset(this.loteRepository);
	}

	@Test
	void shouldSaveLote() {
		/* 1. Arrange */
		ExplotacionGanadera explotacion = new ExplotacionGanadera();
		Lote lote = LoteServiceTests.generateLote("L012374", false, explotacion, LoteServiceTests.tiposGanadoMap.get("Porcino"));

		ArgumentCaptor<Lote> argument = ArgumentCaptor.forClass(Lote.class);
		/* 2. Act */
		this.loteService.saveLote(lote);
		/* 3. Assert */
		Mockito.verify(this.loteRepository).save(argument.capture());
		Assertions.assertThat(argument.getValue().getEsArchivado()).isFalse();
	}

	private static Stream<Lote> shouldArchivarLoteData() throws ParseException {
		Ganadero ganadero1 = GanaderoServiceTests.generateGanadero("Sora", "Riku", "622222222", "kingdom@gmail.com", "Badajoz", "Hinojosa del Valle", "45556839L", "06226", "Calle Islas del Destino", "soriku", "kairi4e");
		ExplotacionGanadera expGanadera1 = ContratoServiceTests.generateExplotacionGanadera("1", "Los Antruejos", false, ganadero1, "Villaricos");
		TiposGanado tipoGanado1 = new TiposGanado();
		tipoGanado1.setTipoGanado("Ovino");
		Ganadero ganadero2 = GanaderoServiceTests.generateGanadero("Antonio", "Ossorio", "722324576", "emisorio@gmail.com", "Badajoz", "Hinojosa del Valle", "45559312L", "06220", "Calle Constitucion", "ossorio", "galleta");
		ExplotacionGanadera expGanadera2 = ContratoServiceTests.generateExplotacionGanadera("2", "Cañada Real", false, ganadero2, "Tomorrowland");
		TiposGanado tipoGanado2 = new TiposGanado();
		tipoGanado2.setTipoGanado("Porcino");
		Lote lote1 = LoteServiceTests.generateLote("1", false, expGanadera1, tipoGanado1);
		Lote lote2 = LoteServiceTests.generateLote("2", false, expGanadera2, tipoGanado2);

		Stream<Lote> res = Stream.of(lote1, lote2);
		return res;
	}

	@ParameterizedTest()
	@MethodSource("shouldArchivarLoteData")
	void shouldArchivarLote(final Lote lote) {
		this.loteService.archivarLote(lote);
		Mockito.verify(this.loteRepository).save(lote);
		Assert.assertEquals(true, lote.getEsArchivado());
	}

	static public Lote generateLote(final String identificador, final Boolean esArchivado, final ExplotacionGanadera explotacion, final TiposGanado ganado) {
		Lote res = new Lote();

		res.setIdentificadorLote(identificador);
		res.setEsArchivado(esArchivado);
		res.setExplotacionGanadera(explotacion);
		res.setTipoGanado(ganado);

		return res;
	}

	static public LoteForm generateLoteForm(final String identificador, final Integer machos, final Integer hembras, final String fechaNacimiento, final String fechaIdentificacion, final Boolean comprado, final String fechaEntrada) {
		LoteForm res = new LoteForm();

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
		formatter = formatter.withLocale(new Locale("es", "ES"));

		res.setIdentificadorLote(identificador);
		res.setNumeroMachos(machos);
		res.setNumeroHembras(hembras);
		if (fechaNacimiento != null) {
			res.setFechaNacimiento(LocalDate.parse(fechaNacimiento, formatter));
		}
		if (fechaIdentificacion != null) {
			res.setFechaIdentificacion(LocalDate.parse(fechaIdentificacion, formatter));
		}
		if (fechaEntrada != null) {
			res.setFechaEntrada(LocalDate.parse(fechaEntrada, formatter));
		}
		res.setComprado(comprado);

		return res;
	}

}
