
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
import org.openqa.selenium.support.ui.Select;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CreateCitaUITest {

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

	// 1 -- Test positivo crear cita
	@Test
	public void testPositivoCreateCita() throws Exception {
		whenLoginAsGanadero().
		andIhaveSelectedOneExpGanaderaAndContratoForSendACita().
		iFillTheFormToCreateACitaCorrectly().
		thenISeeTheNewCitaThatIHaveCreatedInTheListOfPendingCitas();
	}

	// 2 -- Test negativo crear cita
	@Test
	public void testNegativoCreateCita() throws Exception {
		whenLoginAsGanadero().
		andIhaveSelectedOneExpGanaderaAndContratoForSendACita().
		iFillTheFormToCreateACitaWithoutPropertyMotivo().
		thenISeeTheErrorMessageInThePageWeb();
	}

	public CreateCitaUITest whenLoginAsGanadero() {
		this.driver.get("http://localhost:" + port);
		this.driver.findElement(By.xpath("//div[@id='main-navbar']/ul[2]/li/a")).click();
		this.driver.findElement(By.id("username")).clear();
		this.driver.findElement(By.id("username")).sendKeys("ganadero1");
		this.driver.findElement(By.id("password")).click();
		this.driver.findElement(By.id("password")).clear();
		this.driver.findElement(By.id("password")).sendKeys("ganadero1");
		this.driver.findElement(By.xpath("//button[@type='submit']")).click();
		return this;
	}

	public CreateCitaUITest andIhaveSelectedOneExpGanaderaAndContratoForSendACita() {
		this.driver.findElement(By.xpath("//a[contains(@href, '/ganadero/cita/list')]")).click();
		this.driver.findElement(By.id("select-contrato-cita-form-explotacionGanadera")).click();
		new Select(this.driver.findElement(By.id("select-contrato-cita-form-explotacionGanadera"))).selectByVisibleText("Finca La Cueva");
		this.driver.findElement(By.xpath("//option[@value='1']")).click();
		this.driver.findElement(By.id("select-contrato-cita-form-button")).click();
		return this;
	}

	public CreateCitaUITest iFillTheFormToCreateACitaCorrectly() {
		this.driver.findElement(By.id("horaInicio")).click();
		this.driver.findElement(By.id("horaInicio")).clear();
		this.driver.findElement(By.id("horaInicio")).sendKeys("10:00");
		this.driver.findElement(By.id("motivo")).click();
		this.driver.findElement(By.id("motivo")).clear();
		this.driver.findElement(By.id("motivo")).sendKeys("Mis vacas tienen manchas raras por el cuerpo");
		this.driver.findElement(By.xpath("//button[@type='submit']")).click();
		return this;
	}

	public void thenISeeTheNewCitaThatIHaveCreatedInTheListOfPendingCitas() {
		this.driver.findElement(By.id("citasPendientesCheck")).click();
		Assert.assertEquals("Emilio Sevillano", this.driver.findElement(By.xpath("//table[@id='citasPendientesTable']/tbody/tr/td")).getText());
		Assert.assertEquals("Finca La Cueva", this.driver.findElement(By.xpath("//table[@id='citasPendientesTable']/tbody/tr/td[2]")).getText());
		Assert.assertEquals("Mis vacas tienen manchas raras por el cuerpo", this.driver.findElement(By.xpath("//table[@id='citasPendientesTable']/tbody/tr/td[3]")).getText());
	}
	
	public CreateCitaUITest iFillTheFormToCreateACitaWithoutPropertyMotivo() {
		this.driver.findElement(By.id("horaInicio")).click();
		this.driver.findElement(By.id("horaInicio")).clear();
		this.driver.findElement(By.id("horaInicio")).sendKeys("10:00");
		this.driver.findElement(By.xpath("//button[@type='submit']")).click();
		return this;
	}
	
	public void thenISeeTheErrorMessageInThePageWeb(){
		Assert.assertEquals("no puede estar vacío", this.driver.findElement(By.xpath("//form[@id='create-cita-form']/div/div[4]/div/span[2]")).getText());
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
