
package org.springframework.samples.petclinic.service;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Stream;

import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.samples.petclinic.model.Animal;
import org.springframework.samples.petclinic.model.ExplotacionGanadera;
import org.springframework.samples.petclinic.model.Ganadero;
import org.springframework.samples.petclinic.model.Lote;
import org.springframework.samples.petclinic.model.TipoSexo;
import org.springframework.samples.petclinic.model.TiposGanado;
import org.springframework.samples.petclinic.repository.springdatajpa.AnimalRepository;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class AnimalServiceTests {

	@Mock
	private AnimalRepository				animalRepository;

	@Mock
	private AnimalHistoricoService			animalHistoricoService;

	/* Inyectamos los mocks al servicio que estamos probando */
	@InjectMocks
	protected AnimalService					animalService;

	/* Varible de utilidad para consultar los tipos de ganado */
	private static Map<String, TiposGanado>	tiposGanadoMap	= TipoGanadoServiceTests.defaultTiposGanado();


	@BeforeEach
	/* This is necessary because all calls are registered and preserved for all the tests */
	void resetMockUps() {
		Mockito.reset(this.animalRepository, this.animalHistoricoService);
	}

	/*
	 * Método a probar: saveAnimal()
	 * Caso (Positivo) : Se guarda un animal que no ha sido comprado con las propiedades:
	 * esArchivado=false
	 * esComprado=false
	 * procedencia=null
	 * fechaEntrada=null.
	 */
	@Test
	void shouldSaveAnimal() {
		/* 1. Arrange */
		ExplotacionGanadera explotacion = new ExplotacionGanadera();

		Animal animal = AnimalServiceTests.generateAnimal("POR09809", "2020/02/01", "2020/02/20", null, TipoSexo.Macho, AnimalServiceTests.tiposGanadoMap.get("Porcino"), explotacion, null);
		ArgumentCaptor<Animal> argument = ArgumentCaptor.forClass(Animal.class);
		/* 2. Act */
		this.animalService.saveAnimal(animal);
		/* 3. Assert */
		Mockito.verify(this.animalRepository).save(argument.capture());

		/*
		 * Comprobamos que el animal no ha sido comprado, asi que tiene las
		 * propiedades correspondientes vacias
		 */
		Assertions.assertThat(animal).isEqualTo(argument.getValue());
		Assertions.assertThat(argument.getValue().getEsArchivado()).isFalse();
		Assertions.assertThat(argument.getValue().getComprado()).isFalse();
		Assertions.assertThat(argument.getValue().getProcedencia()).isNull();
		Assertions.assertThat(argument.getValue().getFechaEntrada()).isNull();
	}

	/*
	 * Método a probar: saveAnimal()
	 * Caso (Positivo) : Se guarda un animal que comprado con las propiedades:
	 * esArchivado= false
	 * esComprado= true
	 * procedencia= LocalDate introducida
	 * fechaEntrada= LocalDate introducida.
	 */
	@Test
	void shouldSaveAnimalComprado() {
		/* 1. Arrange */
		ExplotacionGanadera explotacion = new ExplotacionGanadera();

		Animal animal = AnimalServiceTests.generateAnimal("VAC-1098309727", "2016/08/01", "2016/08/20", "2017/01/20", TipoSexo.Hembra, AnimalServiceTests.tiposGanadoMap.get("Vacuno"), explotacion, null);
		animal.setComprado(true);
		animal.setProcedencia("Finca El Moncayo");
		ArgumentCaptor<Animal> argument = ArgumentCaptor.forClass(Animal.class);
		/* 2. Act */
		this.animalService.saveAnimal(animal);
		/* 3. Assert */
		Mockito.verify(this.animalRepository).save(argument.capture());
		/*
		 * Comprobamos que el animal es comprado, asi que tiene las
		 * propiedades correspondientes correctas
		 */
		Assertions.assertThat(animal).isEqualTo(argument.getValue());
		Assertions.assertThat(argument.getValue().getEsArchivado()).isFalse();
		Assertions.assertThat(argument.getValue().getComprado()).isTrue();
		Assertions.assertThat(argument.getValue().getProcedencia()).isEqualTo(animal.getProcedencia());
		Assertions.assertThat(argument.getValue().getFechaEntrada()).isEqualTo(animal.getFechaEntrada());
	}

	/*
	 * Método a probar: saveAnimal()
	 * Caso (Positivo) : Aunque se intente introducir un animal archivado, se guarda un animal que no esta archivado.
	 * esArchivado= false
	 */
	@Test
	void shouldSaveAnimalArchivadoAsNoArvhivado() {
		/* 1. Arrange */
		ExplotacionGanadera explotacion = new ExplotacionGanadera();

		Animal animal = AnimalServiceTests.generateAnimal("POR09809", "2020/02/01", "2020/02/20", null, TipoSexo.Macho, AnimalServiceTests.tiposGanadoMap.get("Porcino"), explotacion, null);
		animal.setEsArchivado(true);
		ArgumentCaptor<Animal> argument = ArgumentCaptor.forClass(Animal.class);
		/* 2. Act */
		this.animalService.saveAnimal(animal);
		Mockito.verify(this.animalRepository).save(argument.capture());

		/*
		 * Comprobamos que el animal no ha sido comprado, asi que tiene las
		 * propiedades correspondientes vacias
		 */
		Assertions.assertThat(animal).isEqualTo(argument.getValue());
		Assertions.assertThat(argument.getValue().getEsArchivado()).isFalse();
		Assertions.assertThat(argument.getValue().getComprado()).isFalse();
		Assertions.assertThat(argument.getValue().getProcedencia()).isNull();
		Assertions.assertThat(argument.getValue().getFechaEntrada()).isNull();
	}

	private static Stream<Animal> shouldArchivarAnimalData() throws ParseException {
		Ganadero ganadero1 = GanaderoServiceTests.generateGanadero("Sora", "Riku", "622222222", "kingdom@gmail.com", "Badajoz", "Hinojosa del Valle", "45556839L", "06226", "Calle Islas del Destino", "soriku", "kairi4e");
		ExplotacionGanadera expGanadera1 = ContratoServiceTests.generateExplotacionGanadera("1", "Los Antruejos", false, ganadero1, "Villaricos");
		TiposGanado tipoGanado1 = new TiposGanado();
		tipoGanado1.setTipoGanado("Ovino");
		Animal animal1 = ArchivarServiceTests.generateAnimal(false, true, expGanadera1, null, LocalDate.of(2020, 1, 15), LocalDate.of(2020, 1, 10), "OV12345", null, null, TipoSexo.Macho, tipoGanado1);

		Ganadero ganadero2 = GanaderoServiceTests.generateGanadero("Antonio", "Ossorio", "722324576", "emisorio@gmail.com", "Badajoz", "Hinojosa del Valle", "45559312L", "06220", "Calle Constitucion", "ossorio", "galleta");
		ExplotacionGanadera expGanadera2 = ContratoServiceTests.generateExplotacionGanadera("2", "Cañada Real", false, ganadero2, "Tomorrowland");
		TiposGanado tipoGanado2 = new TiposGanado();
		tipoGanado2.setTipoGanado("Porcino");
		Animal animal2 = ArchivarServiceTests.generateAnimal(false, true, expGanadera2, null, LocalDate.of(2019, 1, 15), LocalDate.of(2019, 1, 10), "PO12345", null, null, TipoSexo.Hembra, tipoGanado2);
		animal1.setId(1);
		animal2.setId(2);
		Stream<Animal> res = Stream.of(animal1, animal2);
		return res;
	}

	@ParameterizedTest()
	@MethodSource("shouldArchivarAnimalData")
	void shouldArchivarAnimal(final Animal animal) {
		this.animalService.archivarAnimal(animal);
		Mockito.verify(this.animalRepository).save(animal);
		Assert.assertEquals(true, animal.getEsArchivado());
	}

	private static Stream<Arguments> shouldNotDeleteAnimalData() throws ParseException {
		Ganadero ganadero1 = GanaderoServiceTests.generateGanadero("Sora", "Riku", "622222222", "kingdom@gmail.com", "Badajoz", "Hinojosa del Valle", "45556839L", "06226", "Calle Islas del Destino", "soriku", "kairi4e");
		ExplotacionGanadera expGanadera1 = ContratoServiceTests.generateExplotacionGanadera("1", "Los Antruejos", false, ganadero1, "Villaricos");
		TiposGanado tipoGanado1 = new TiposGanado();
		tipoGanado1.setTipoGanado("Ovino");
		Animal animal1 = ArchivarServiceTests.generateAnimal(false, false, expGanadera1, null, LocalDate.of(2020, 1, 15), LocalDate.of(2020, 1, 10), "OV12345", null, null, TipoSexo.Macho, tipoGanado1);
		IllegalArgumentException errorEsperado1 = new IllegalArgumentException("Para que un animal sea eliminado, debe de estar archivado.");

		Ganadero ganadero2 = GanaderoServiceTests.generateGanadero("Antonio", "Ossorio", "722324576", "emisorio@gmail.com", "Badajoz", "Hinojosa del Valle", "45559312L", "06220", "Calle Constitucion", "ossorio", "galleta");
		ExplotacionGanadera expGanadera2 = ContratoServiceTests.generateExplotacionGanadera("2", "Cañada Real", false, ganadero2, "Tomorrowland");
		TiposGanado tipoGanado2 = new TiposGanado();
		tipoGanado2.setTipoGanado("Porcino");
		Animal animal2 = ArchivarServiceTests.generateAnimal(false, true, expGanadera2, null, LocalDate.of(2019, 1, 15), LocalDate.of(2019, 1, 10), "PO12345", null, null, TipoSexo.Hembra, tipoGanado2);
		IllegalArgumentException errorEsperado2 = new IllegalArgumentException("Para que un animal sea eliminado, debe haber un archivo historico previo.");
		animal1.setId(1);
		animal2.setId(2);
		Stream<Arguments> res = Stream.of(Arguments.of(animal1, errorEsperado1), Arguments.of(animal2, errorEsperado2));
		return res;
	}

	@ParameterizedTest()
	@MethodSource("shouldNotDeleteAnimalData")
	void shouldEliminarAnimal(final Animal animal, final IllegalArgumentException errorEsperado) {
		Exception errorDeleteAnimal = org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, () -> {
			this.animalService.deleteAnimal(animal);
		});
		Assert.assertEquals(errorDeleteAnimal.getMessage(), errorEsperado.getMessage());

	}

	static public Animal generateAnimal(final String identificador, final String fechaNacimiento, final String fechaIdentificacion, final String fechaEntrada, final TipoSexo sexo, final TiposGanado ganado, final ExplotacionGanadera explotacion,
		final Lote lote) {
		Animal res = new Animal();

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
		formatter = formatter.withLocale(new Locale("es", "ES"));

		res.setIdentificadorAnimal(identificador);
		if (fechaNacimiento != null) {
			res.setFechaNacimiento(LocalDate.parse(fechaNacimiento, formatter));
		}
		if (fechaIdentificacion != null) {
			res.setFechaIdentificacion(LocalDate.parse(fechaIdentificacion, formatter));
		}
		if (fechaEntrada != null) {
			res.setFechaEntrada(LocalDate.parse(fechaEntrada, formatter));
		}
		res.setSexo(sexo);
		res.setTipoGanado(ganado);
		res.setExplotacionGanadera(explotacion);
		res.setLote(lote);

		return res;
	}

}
