
package org.springframework.samples.petclinic.ui;

import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CreateFacturaUITest {

	private WebDriver		driver;
	private String			baseUrl;
	private boolean			acceptNextAlert		= true;
	private StringBuffer	verificationErrors	= new StringBuffer();

	@LocalServerPort
	private int				port;


	@BeforeEach
	public void setUp() throws Exception {
		System.setProperty("webdriver.gecko.driver", System.getenv("webdriver.gecko.driver"));
		this.driver = new FirefoxDriver();
		this.baseUrl = "https://www.google.com/";
		this.driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	}

	@Test
	public void testPositivoCreateReceta() throws Exception {
		this.whenLoginAsVeterinario().iGoToSelectContratoScene().andIGoToTheListOfFacturasAndIClickOnDoANewFactura().iFillTheFormToCreateAFacturaCorrectly().iSeeTheRecetaInTheList();
	}

	@Test
	public void testNegativoCreateReceta() throws Exception {
		this.whenLoginAsVeterinario().iGoToSelectContratoScene().andIGoToTheListOfFacturasAndIClickOnDoANewFactura().iFillTheFormToCreateAFacturaIncorrectly().iSeeTheErrorMessageInThePageWeb();
	}

	public CreateFacturaUITest whenLoginAsVeterinario() {
		this.driver.get("http://localhost:" + this.port);
		this.driver.findElement(By.xpath("//div[@id='main-navbar']/ul[2]/li/a")).click();
		this.driver.findElement(By.id("username")).clear();
		this.driver.findElement(By.id("username")).sendKeys("veterinario1");
		this.driver.findElement(By.id("password")).click();
		this.driver.findElement(By.id("password")).clear();
		this.driver.findElement(By.id("password")).sendKeys("veterinario1");
		this.driver.findElement(By.xpath("//button[@type='submit']")).click();
		return this;
	}

	private CreateFacturaUITest iGoToSelectContratoScene() {
		this.driver.findElement(By.xpath("//a[contains(@href, '/factura/select')]")).click();
		this.driver.findElement(By.xpath("//select[@id='contratoId']")).click();
		this.driver.findElement(By.xpath("//select[@id='contratoId']/option")).click();
		this.driver.findElement(By.xpath("//div[@class='form-group']/div/button")).click();
		return this;
	}

	public CreateFacturaUITest andIGoToTheListOfFacturasAndIClickOnDoANewFactura() {
		this.driver.findElement(By.xpath("//input[@value='Añadir una nueva factura']")).click();
		return this;
	}

	public CreateFacturaUITest iFillTheFormToCreateAFacturaCorrectly() {
		this.driver.findElement(By.xpath("//select[@id='productosAAnadir']")).click();
		this.driver.findElement(By.xpath("//select[@id='productosAAnadir']/option[2]")).click();
		this.driver.findElement(By.xpath("//input[@id='cantidadAAnadir']")).sendKeys("1");
		this.driver.findElement(By.xpath("//input[@value='Añadir producto']")).click();
		this.driver.findElement(By.xpath("//button[@type='submit']")).click();
		return this;
	}

	public CreateFacturaUITest iFillTheFormToCreateAFacturaIncorrectly() {
		this.driver.findElement(By.xpath("//select[@id='productosAAnadir']")).click();
		this.driver.findElement(By.xpath("//select[@id='productosAAnadir']/option[2]")).click();
		this.driver.findElement(By.xpath("//input[@id='cantidadAAnadir']")).sendKeys("1");
		this.driver.findElement(By.xpath("//button[@type='submit']")).click();
		return this;
	}

	public void iSeeTheRecetaInTheList() {
		Assert.assertEquals("No", this.driver.findElement(By.xpath("//table[@id='productosTable']/tbody/tr[last()]/td[2]/img")).getAttribute("alt"));
	}

	public void iSeeTheErrorMessageInThePageWeb() {
		Assert.assertEquals("No puede crear una factura sin recetas y sin productos no recetables\n" + "Nueva Factura\n" + "Recetas\n" + "Recetas pendientes de pago\n" + "2019-08-21 21:01:00.0\n" + "\n" + "\n" + "Productos\n" + "Productos añadidos:\n"
			+ "Eliga el producto a añadir\n" + "Thrombocid\n" + "Frontline\n" + "Crear factura", this.driver.findElement(By.xpath("//div[@class='container xd-container']")).getText());
	}

	@AfterEach
	public void tearDown() throws Exception {
		this.driver.quit();
		String verificationErrorString = this.verificationErrors.toString();
		if (!"".equals(verificationErrorString)) {
			Assert.fail(verificationErrorString);
		}
	}

}
