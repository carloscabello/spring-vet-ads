
package org.springframework.samples.petclinic.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.samples.petclinic.model.Contrato;
import org.springframework.samples.petclinic.model.Factura;
import org.springframework.samples.petclinic.model.LineaFactura;
import org.springframework.samples.petclinic.model.Producto;
import org.springframework.samples.petclinic.model.Receta;
import org.springframework.samples.petclinic.repository.springdatajpa.FacturaRepository;

@ExtendWith(MockitoExtension.class)
public class FacturaServiceTests {

	@Mock
	private FacturaRepository	facturaRepository;

	@Mock
	private ContratoService		contratoService;

	@InjectMocks
	private FacturaService		facturaService;

	//1 ----- MÉTODO GUARDAR UNA FACTURA


	//1.1 -> Insertar factura correctamente
	private static Stream<Factura> shouldInsertFacturaData() throws ParseException {
		Stream<Factura> fatura;

		Contrato contratoParaFacturaPos1 = CitaServiceTests.contrato1ParaPruebas();
		List<Receta> recetas = new ArrayList<Receta>();
		recetas.add(FacturaServiceTests.receta1ParaPruebas());
		recetas.add(FacturaServiceTests.receta2ParaPruebas());

		//Generando una factura
		Factura facturaPos1 = FacturaServiceTests.generateFactura(CitaNonSolitaryServiceTests.newDateFormatted("2020/09/21 16:00"), false, contratoParaFacturaPos1, recetas);
		Producto p1 = ProductoServiceTests.generateProducto("Espidifen", 19, 4.9, true);
		//Creando una línea factura para la factura
		FacturaServiceTests.generateLineaFactura(4, 4.9, facturaPos1, p1);

		fatura = Stream.of(facturaPos1);
		return fatura;

	}

	@ParameterizedTest()
	@MethodSource("shouldInsertFacturaData")
	void shouldInsertFactura(final Factura factura) {
		this.facturaService.saveFactura(factura);
		Mockito.verify(this.facturaRepository).save(factura);
	}

	//1.2 ->  No se puede insertar receta correctamente

	private static Stream<Factura> shouldNotInsertFacturaData() throws ParseException {
		Stream<Factura> fatura;

		Contrato contratoParaFacturaPos1 = CitaServiceTests.contrato1ParaPruebas();
		List<Receta> recetas = new ArrayList<Receta>();
		recetas.add(FacturaServiceTests.receta1ParaPruebas());
		recetas.add(FacturaServiceTests.receta2ParaPruebas());

		//Generando una factura
		Factura facturaNeg1 = FacturaServiceTests.generateFactura(null, false, contratoParaFacturaPos1, recetas);
		Producto p1 = ProductoServiceTests.generateProducto("Espidifen", 19, 4.9, true);
		//Creando una línea factura para la factura
		FacturaServiceTests.generateLineaFactura(4, 4.9, facturaNeg1, p1);

		//Generando otra factura
		Factura facturaNeg2 = FacturaServiceTests.generateFactura(CitaNonSolitaryServiceTests.newDateFormatted("2020/09/21 16:00"), false, null, recetas);
		//Creando una línea factura para la factura
		FacturaServiceTests.generateLineaFactura(4, 4.9, facturaNeg2, p1);

		fatura = Stream.of(facturaNeg1, facturaNeg2);
		return fatura;

	}

	@ParameterizedTest()
	@MethodSource("shouldNotInsertFacturaData")
	void shouldNotInsertFactura(final Factura factura) {
		this.facturaService.saveFactura(factura);
		Mockito.verify(this.facturaRepository).save(factura);
	}

	//Métodos auxiliares

	// Método para generar una factura
	public static Factura generateFactura(final Date fechaEmision, final Boolean esPagado, final Contrato contrato, final List<Receta> recetas) {
		Factura factura = new Factura();

		//Asignaciones factura
		factura.setFechaEmision(fechaEmision);
		factura.setEsPagado(esPagado);
		factura.setContrato(contrato);
		factura.setRecetas(recetas);

		return factura;
	}

	//Método para generar una líneaFactura
	public static LineaFactura generateLineaFactura(final Integer cantidad, final Double precioVenta, final Factura factura, final Producto producto) {
		LineaFactura lf = new LineaFactura();

		//Asignaciones lineaFactura
		lf.setCantidad(cantidad);
		lf.setPrecioVenta(precioVenta);
		lf.setFactura(factura);
		lf.setProducto(producto);

		return lf;
	}
	//DATOS DE PRUEBA

	// --- Receta1 ---
	public static Receta receta1ParaPruebas() throws ParseException {

		Receta receta = RecetaServiceTests.generateReceta(CitaNonSolitaryServiceTests.newDateFormatted("2020/05/31 17:00"), "Para dar semanalmente durante una semana a las cabras", false, RecetaServiceTests.cita1ParaPruebas(), null);
		return receta;
	}
	// --- Receta2 ---
	public static Receta receta2ParaPruebas() throws ParseException {

		Receta receta = RecetaServiceTests.generateReceta(CitaNonSolitaryServiceTests.newDateFormatted("2020/06/01 16:00"), "Para dar semanalmente durante una semana a las otras cabras", false, RecetaServiceTests.cita2ParaPruebas(), null);
		return receta;
	}
	// --- Receta3 ---
	public static Receta receta3ParaPruebas() throws ParseException {

		Receta receta = RecetaServiceTests.generateReceta(CitaNonSolitaryServiceTests.newDateFormatted("2020/05/31 17:00"), "Para dar semanalmente durante una semana a las cabras", false, RecetaServiceTests.cita1ParaPruebas(), null);
		return receta;
	}

}
