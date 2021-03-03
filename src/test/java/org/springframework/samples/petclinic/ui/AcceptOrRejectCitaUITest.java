
package org.springframework.samples.petclinic.ui;

import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AcceptOrRejectCitaUITest {

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

	// 1 -- Test de aceptar cita
	@Test
	public void testAcceptCita() throws Exception {
		this.whenLoginAsVeterinario().
		andIWatchTheListOfPendingCitas();

		/*
		 * Guardando en una variable los elementos de la cita que se va aceptar.
		 * En principio se encuentra en la lista de citas pendientes
		 */
		String explotacionGanaderaCita = this.driver.findElement(By.xpath("//table[@id='citasPendientesTable']/tbody/tr/td")).getText();
		String motivoCita = this.driver.findElement(By.xpath("//table[@id='citasPendientesTable']/tbody/tr/td[2]")).getText();
		String fechaInicioCita = this.driver.findElement(By.xpath("//table[@id='citasPendientesTable']/tbody/tr/td[3]")).getText();
		String fechaFinCita = this.driver.findElement(By.xpath("//table[@id='citasPendientesTable']/tbody/tr/td[4]")).getText();

		this.iAcceptACita().
		/*
		 * Comprobando que en la lista de citas aceptadas, salen los mismos valores que
		 * recogimos arriba. COMPROBANDO QUE ES LA MISMA CITA
		 */
			thenISeeTheCitaInTheListOfAcceptsCita(explotacionGanaderaCita, motivoCita, fechaInicioCita, fechaFinCita);
	}

	// 2 -- Test positivo de rechazar cita
	@Test
	public void testPositivoRejectCita() throws Exception {
		this.whenLoginAsVeterinario().
		andIWatchTheListOfPendingCitas().
		iFillTheFormToRejectACitaCorrectly().
		thenIseeThatTheCitaIsRejectInTheListOfPendingsCita();
	}
	
	// 3 -- Test negativo de rechazar cita
	@Test
	public void testNegativoRejectCita() throws Exception {
		this.whenLoginAsVeterinario().
		andIWatchTheListOfPendingCitas().
		iFillTheFormToRejectACitaWithoutRechazoJustificacion();
	}

	public AcceptOrRejectCitaUITest whenLoginAsVeterinario() {
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

	public AcceptOrRejectCitaUITest andIWatchTheListOfPendingCitas() {
		this.driver.findElement(By.xpath("//a[contains(@href, '/veterinario/cita/list')]")).click();
		this.driver.findElement(By.xpath("//button[@id='citasPendientesCheck']")).click();
		return this;
	}

	public AcceptOrRejectCitaUITest iAcceptACita() {
		this.driver.findElement(By.xpath("//button[@id='aceptar-cita-6']")).click();
		return this;
	}

	public void thenISeeTheCitaInTheListOfAcceptsCita(final String explotacionGanaderaCita, final String motivoCita, final String fechaInicioCita, final String fechaFinCita) {
		Assert.assertEquals(explotacionGanaderaCita, this.driver.findElement(By.xpath("//table[@id='citasAceptadasTable']/tbody/tr[2]/td")).getText());
		Assert.assertEquals(motivoCita, this.driver.findElement(By.xpath("//table[@id='citasAceptadasTable']/tbody/tr[2]/td[2]")).getText());
		Assert.assertEquals(fechaInicioCita, this.driver.findElement(By.xpath("//table[@id='citasAceptadasTable']/tbody/tr[2]/td[3]")).getText());
		Assert.assertEquals(fechaFinCita, this.driver.findElement(By.xpath("//table[@id='citasAceptadasTable']/tbody/tr[2]/td[4]")).getText());
	}

	public AcceptOrRejectCitaUITest iFillTheFormToRejectACitaCorrectly() {
		this.driver.findElement(By.xpath("//button[@id='rechazar-cita-2']")).click();
		this.driver.findElement(By.id("rechazoJustificacion")).click();
		this.driver.findElement(By.id("rechazoJustificacion")).clear();
		this.driver.findElement(By.id("rechazoJustificacion")).sendKeys("En esa hora estoy ocupado. Pida otra cita en otro momento.");
		this.driver.findElement(By.xpath("//button[@type='submit']")).click();
		return this;
	}

	public void thenIseeThatTheCitaIsRejectInTheListOfPendingsCita() {
		driver.findElement(By.xpath("//button[@id='citasTodasCheck']")).click();
		Assert.assertEquals("RECHAZADA", driver.findElement(By.xpath("//table[@id='citasTodasTable']/tbody/tr/td[5]")).getText());
		Assert.assertEquals("En esa hora estoy ocupado. Pida otra cita en otro momento.", this.driver.findElement(By.xpath("//table[@id='citasTodasTable']/tbody/tr/td[6]")).getText());
	}
	
	public AcceptOrRejectCitaUITest iFillTheFormToRejectACitaWithoutRechazoJustificacion() {
		/*No relleno el campo de rechazoJustificacion, el cual es obligatorio para rechazar
		  una cita*/
		this.driver.findElement(By.xpath("//button[@id='rechazar-cita-2']")).click();
		this.driver.findElement(By.xpath("//button[@type='submit']")).click();
		return this;
	}
	
	public void thenISeeTheErrorMessageInThePageWeb() {
		Assert.assertEquals("Debe darse una justificación al rechazar una cita", driver.findElement(By.xpath("//form[@id='create-cita-form']/div/div[5]/div/span[2]")).getText());
	}

	@AfterEach
	public void tearDown() throws Exception {
		this.driver.quit();
		String verificationErrorString = this.verificationErrors.toString();
		if (!"".equals(verificationErrorString)) {
			Assert.fail(verificationErrorString);
		}
	}

	private boolean isElementPresent(final By by) {
		try {
			this.driver.findElement(by);
			return true;
		} catch (NoSuchElementException e) {
			return false;
		}
	}

	private boolean isAlertPresent() {
		try {
			this.driver.switchTo().alert();
			return true;
		} catch (NoAlertPresentException e) {
			return false;
		}
	}

	private String closeAlertAndGetItsText() {
		try {
			Alert alert = this.driver.switchTo().alert();
			String alertText = alert.getText();
			if (this.acceptNextAlert) {
				alert.accept();
			} else {
				alert.dismiss();
			}
			return alertText;
		} finally {
			this.acceptNextAlert = true;
		}
	}
}
