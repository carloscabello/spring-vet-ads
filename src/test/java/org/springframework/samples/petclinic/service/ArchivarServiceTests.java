
package org.springframework.samples.petclinic.service;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.stream.Stream;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.samples.petclinic.model.Animal;
import org.springframework.samples.petclinic.model.AnimalHistorico;
import org.springframework.samples.petclinic.model.ExplotacionGanadera;
import org.springframework.samples.petclinic.model.Ganadero;
import org.springframework.samples.petclinic.model.Lote;
import org.springframework.samples.petclinic.model.TipoSexo;
import org.springframework.samples.petclinic.model.TiposGanado;
import org.springframework.samples.petclinic.repository.springdatajpa.AnimalHistoricoRepository;
import org.springframework.samples.petclinic.repository.springdatajpa.AnimalRepository;

@ExtendWith(MockitoExtension.class)
public class ArchivarServiceTests {

	@Mock
	private AnimalHistoricoRepository	animalHistoricoRepository;

	@Mock
	private AnimalService				animalService;

	@InjectMocks
	private AnimalHistoricoService		animalHistoricoService;

	@Mock
	private AnimalRepository			animalRepository;


	private static Stream<AnimalHistorico> shouldInsertAnimalHistoricoData() throws ParseException {
		Ganadero ganadero1 = GanaderoServiceTests.generateGanadero("Sora", "Riku", "622222222", "kingdom@gmail.com", "Badajoz", "Hinojosa del Valle", "45556839L", "06226", "Calle Islas del Destino", "soriku", "kairi4e");
		ExplotacionGanadera expGanadera1 = ContratoServiceTests.generateExplotacionGanadera("1", "Los Antruejos", false, ganadero1, "Villaricos");
		TiposGanado tipoGanado1 = new TiposGanado();
		tipoGanado1.setTipoGanado("Ovino");
		Animal animal1 = ArchivarServiceTests.generateAnimal(false, true, expGanadera1, null, LocalDate.of(2020, 1, 15), LocalDate.of(2020, 1, 10), "OV12345", null, null, TipoSexo.Macho, tipoGanado1);

		AnimalHistorico animalH1 = ArchivarServiceTests.generateAnimalHistorico(animal1, null, LocalDate.of(2020, 3, 18), "Venta a Marco");

		Ganadero ganadero2 = GanaderoServiceTests.generateGanadero("Antonio", "Ossorio", "722324576", "emisorio@gmail.com", "Badajoz", "Hinojosa del Valle", "45559312L", "06220", "Calle Constitucion", "ossorio", "galleta");
		ExplotacionGanadera expGanadera2 = ContratoServiceTests.generateExplotacionGanadera("2", "Cañada Real", false, ganadero2, "Tomorrowland");
		TiposGanado tipoGanado2 = new TiposGanado();
		tipoGanado2.setTipoGanado("Porcino");
		Animal animal2 = ArchivarServiceTests.generateAnimal(false, true, expGanadera2, null, LocalDate.of(2019, 1, 15), LocalDate.of(2019, 1, 10), "PO12345", null, null, TipoSexo.Hembra, tipoGanado2);

		AnimalHistorico animalH2 = ArchivarServiceTests.generateAnimalHistorico(animal2, LocalDate.of(2020, 4, 22), null, "Muerte natural");

		Stream<AnimalHistorico> res = Stream.of(animalH1, animalH2);
		return res;
	}

	@ParameterizedTest()
	@MethodSource("shouldInsertAnimalHistoricoData")
	void shouldInsertAnimalHistorico(final AnimalHistorico animalH) {
		this.animalHistoricoService.saveAnimalHistorico(animalH);
		Mockito.verify(this.animalHistoricoRepository).save(animalH);
	}

	private static Stream<AnimalHistorico> shouldNotInsertAnimalHistoricoData() {
		Ganadero ganadero1 = GanaderoServiceTests.generateGanadero("Sora", "Riku", "622222222", "kingdom@gmail.com", "Badajoz", "Hinojosa del Valle", "45556839L", "06226", "Calle Islas del Destino", "soriku", "kairi4e");
		ExplotacionGanadera expGanadera1 = ContratoServiceTests.generateExplotacionGanadera("1", "Los Antruejos", false, ganadero1, "Villaricos");
		TiposGanado tipoGanado1 = new TiposGanado();
		tipoGanado1.setTipoGanado("Ovino");
		Animal animal1 = ArchivarServiceTests.generateAnimal(false, true, expGanadera1, null, LocalDate.of(2020, 1, 15), LocalDate.of(2020, 1, 10), "OV12345", null, null, TipoSexo.Macho, tipoGanado1);
		AnimalHistorico animalH1 = ArchivarServiceTests.generateAnimalHistorico(animal1, null, LocalDate.of(2020, 2, 23), "Venta a Marco");

		Ganadero ganadero2 = GanaderoServiceTests.generateGanadero("Antonio", "Ossorio", "722324576", "emisorio@gmail.com", "Badajoz", "Hinojosa del Valle", "45559312L", "06220", "Calle Constitucion", "ossorio", "galleta");
		ExplotacionGanadera expGanadera2 = ContratoServiceTests.generateExplotacionGanadera("2", "Cañada Real", false, ganadero2, "Tomorrowland");
		TiposGanado tipoGanado2 = new TiposGanado();
		tipoGanado2.setTipoGanado("Porcino");
		Animal animal2 = ArchivarServiceTests.generateAnimal(false, true, expGanadera2, null, LocalDate.of(2019, 1, 15), LocalDate.of(2019, 1, 10), "PO12345", null, null, TipoSexo.Hembra, tipoGanado2);
		AnimalHistorico animalH2 = ArchivarServiceTests.generateAnimalHistorico(animal2, LocalDate.of(2020, 4, 22), null, "Muerte natural");

		Stream<AnimalHistorico> res = Stream.of(animalH1, animalH2);
		return res;
	}

	@ParameterizedTest()
	@MethodSource("shouldNotInsertAnimalHistoricoData")
	void shouldNotInsertGanadero(final AnimalHistorico animalH) throws Exception {
		this.animalHistoricoService.saveAnimalHistorico(animalH);
		Mockito.verify(this.animalHistoricoRepository).save(animalH);
	}

	static public Animal generateAnimal(final Boolean comprado, final Boolean esArchivado, final ExplotacionGanadera explotacionGanadera, final LocalDate fechaEntrada, final LocalDate fechaIdentificacion, final LocalDate fechaNacimiento,
		final String identificadorAnimal, final Lote lote, final String procedencia, final TipoSexo sexo, final TiposGanado tipoGanado) {
		Animal animal = new Animal();

		animal.setComprado(comprado);
		animal.setEsArchivado(esArchivado);
		animal.setExplotacionGanadera(explotacionGanadera);
		animal.setFechaEntrada(fechaEntrada);
		animal.setFechaIdentificacion(fechaIdentificacion);
		animal.setFechaNacimiento(fechaNacimiento);
		animal.setIdentificadorAnimal(identificadorAnimal);
		animal.setLote(lote);
		animal.setProcedencia(procedencia);
		animal.setSexo(sexo);
		animal.setTipoGanado(tipoGanado);

		return animal;
	}

	public static AnimalHistorico generateAnimalHistorico(final Animal animal, final LocalDate fechaFallecimiento, final LocalDate fechaVenta, final String masInfo) {
		AnimalHistorico animalHistorico = new AnimalHistorico();

		animalHistorico.setAnimal(animal);
		animalHistorico.setFechaFallecimiento(fechaFallecimiento);
		animalHistorico.setFechaVenta(fechaVenta);
		animalHistorico.setMasInfo(masInfo);

		return animalHistorico;
	}

	public static Lote generateLote(final Boolean esArchivado, final ExplotacionGanadera explotacionGanadera, final String identificadorLote, final TiposGanado tipoGanado) {
		Lote lote = new Lote();
		lote.setEsArchivado(esArchivado);
		lote.setExplotacionGanadera(explotacionGanadera);
		lote.setIdentificadorLote(identificadorLote);
		lote.setTipoGanado(tipoGanado);
		return lote;
	}

}
