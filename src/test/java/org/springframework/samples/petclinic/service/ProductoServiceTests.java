
package org.springframework.samples.petclinic.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.samples.petclinic.model.Producto;
import org.springframework.samples.petclinic.model.TiposGanado;
import org.springframework.samples.petclinic.model.Veterinario;
import org.springframework.samples.petclinic.repository.springdatajpa.ProductoRepository;

@ExtendWith(MockitoExtension.class)
public class ProductoServiceTests {

	@Mock
	private ProductoRepository	productoRepository;

	@Mock
	private VetService			vetService;

	@InjectMocks
	private ProductoService		ProductoService;


	private static Stream<Producto> shouldInsertProductoData() {
		Stream<Producto> res;
		List<TiposGanado> tiposGanado = new ArrayList<TiposGanado>();
		List<TiposGanado> tiposGanado2 = new ArrayList<TiposGanado>();
		TiposGanado p = new TiposGanado();
		p.setTipoGanado("porcino");
		TiposGanado c = new TiposGanado();
		p.setTipoGanado("caprino");
		tiposGanado.add(c);
		tiposGanado.add(p);
		tiposGanado2.add(p);
		Veterinario vet1 = VeterinarioServiceTests.generateVeterinario("Joseph", "Joestar", "622222222", "jojo@gmail.com", "Sevilla", "Los palacios", "12345678A", tiposGanado, true, "hola", "hola");
		Veterinario vet2 = VeterinarioServiceTests.generateVeterinario("Jotaro", "kujo", "622222223", "jojo2@gmail.com", "Sevilla", "Los palacios", "12345678B", tiposGanado2, true, "hola2", "hola2");

		Producto prodPos1 = ProductoServiceTests.generateProducto("Ibrupofenomon", 4, 4.44, false);
		Producto prodPos2 = ProductoServiceTests.generateProducto("paracetamon", 123, 123.21, true);
		Producto prodPos3 = ProductoServiceTests.generateProducto("medicamenton", 12, 12.12, false);

		prodPos1.setVeterinario(vet1);
		prodPos2.setVeterinario(vet2);
		prodPos3.setVeterinario(vet2);

		res = Stream.of(prodPos1, prodPos2, prodPos3);
		return res;
	}

	@ParameterizedTest
	@MethodSource("shouldInsertProductoData")
	void shouldInsertProducto(final Producto producto) {
		this.ProductoService.saveProducto(producto);
		Mockito.verify(this.productoRepository).save(producto);
	}

	private static Stream<Producto> shouldNotInsertProductoData() {
		Stream<Producto> res;
		List<TiposGanado> tiposGanado = new ArrayList<TiposGanado>();
		List<TiposGanado> tiposGanado2 = new ArrayList<TiposGanado>();
		TiposGanado p = new TiposGanado();
		p.setTipoGanado("porcino");
		TiposGanado c = new TiposGanado();
		p.setTipoGanado("caprino");
		tiposGanado.add(c);
		tiposGanado.add(p);
		tiposGanado2.add(p);
		Veterinario vet1 = VeterinarioServiceTests.generateVeterinario("Joseph", "Joestar", "622222222", "jojo@gmail.com", "Sevilla", "Los palacios", "12345678A", tiposGanado, true, "hola", "hola");
		Veterinario vet2 = VeterinarioServiceTests.generateVeterinario("Jotaro", "kujo", "622222223", "jojo2@gmail.com", "Sevilla", "Los palacios", "12345678B", tiposGanado2, true, "hola2", "hola2");

		vet2.setId(4);

		Producto prodPos1 = ProductoServiceTests.generateProducto("Ibrupofenomon", -5, 4.44, false);
		Producto prodPos2 = ProductoServiceTests.generateProducto("paracetamon", 123, -123.0, true);
		Producto prodPos3 = ProductoServiceTests.generateProducto("medicamenton", -12, -12.12, false);

		prodPos1.setVeterinario(vet1);
		prodPos2.setVeterinario(vet2);
		prodPos3.setVeterinario(vet2);

		res = Stream.of(prodPos1, prodPos2, prodPos3);
		return res;
	}

	@ParameterizedTest
	@MethodSource("shouldNotInsertProductoData")
	void shouldNotInsertProducto(final Producto producto) {
		this.ProductoService.saveProducto(producto);
		Mockito.verify(this.productoRepository).save(producto);
	}

	@ParameterizedTest
	@MethodSource("shouldInsertProductoData")
	void deleteProducto(final Producto producto) {
		this.ProductoService.deleteProducto(producto);
		Mockito.verify(this.productoRepository).delete(producto);
	}

	public static Producto generateProducto(final String name, final Integer cantidad, final Double precio, final boolean necesitaReceta) {
		Producto producto = new Producto();
		producto.setCantidad(cantidad);
		producto.setName(name);
		producto.setPrecio(precio);
		producto.setNecesitaReceta(necesitaReceta);
		return producto;
	}
}
