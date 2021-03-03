
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
public class CreateRecetaUITest {

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
		whenLoginAsVeterinario().
		andIGoToTheListOfRecetasAndIClickOnDoANewReceta().
		iFillTheFormToCreateARecetaCorrectly().
		iSeeTheRecetaInTheList();
	}
	
	@Test
	public void testNegativoCreateReceta() throws Exception {
		whenLoginAsVeterinario().
		andIGoToTheListOfRecetasAndIClickOnDoANewReceta().
		iFillTheFormToCreateARecetaWithoutProductos().
		iSeeTheErrorMessageInThePageWeb();
	}

	public CreateRecetaUITest whenLoginAsVeterinario() {
		this.driver.get("http://localhost:" + port);
		this.driver.findElement(By.xpath("//div[@id='main-navbar']/ul[2]/li/a")).click();
		this.driver.findElement(By.id("username")).clear();
		this.driver.findElement(By.id("username")).sendKeys("veterinario1");
		this.driver.findElement(By.id("password")).click();
		this.driver.findElement(By.id("password")).clear();
		this.driver.findElement(By.id("password")).sendKeys("veterinario1");
		this.driver.findElement(By.xpath("//button[@type='submit']")).click();
		return this;
	}
	
	public CreateRecetaUITest andIGoToTheListOfRecetasAndIClickOnDoANewReceta() {
		this.driver.findElement(By.xpath("//a[contains(@href, '/veterinario/receta/list')]")).click();
		driver.findElement(By.xpath("//div[@id='listaRecetas']/a/input")).click();
		return this;
	}
	
	public CreateRecetaUITest iFillTheFormToCreateARecetaCorrectly() {
		this.driver.findElement(By.id("descripcion")).click();
		this.driver.findElement(By.id("descripcion")).clear();
		this.driver.findElement(By.id("descripcion")).sendKeys("Receta para dar a las cabras 1 vez cada dia durante 1 semana");
		new Select(this.driver.findElement(By.id("cantidad"))).selectByVisibleText("3");
		this.driver.findElement(By.xpath("//option[@value='3']")).click();
		new Select(this.driver.findElement(By.xpath("(//select[@id='cantidad'])[2]"))).selectByVisibleText("2");
		this.driver.findElement(By.xpath("(//option[@value='2'])[2]")).click();
		this.driver.findElement(By.xpath("//button[@type='submit']")).click();
		return this;
	}
	
	public void iSeeTheRecetaInTheList() {
		Assert.assertEquals("Perpetuo Melgar Rios", this.driver.findElement(By.xpath("//table[@id='listaRecetasTable']/tbody/tr/td")).getText());
		Assert.assertEquals("Receta para dar a las cabras 1 vez cada dia durante 1 semana", this.driver.findElement(By.xpath("//table[@id='listaRecetasTable']/tbody/tr/td[2]")).getText());
	}
	
	public CreateRecetaUITest iFillTheFormToCreateARecetaWithoutProductos() {
		driver.findElement(By.id("descripcion")).click();
	    driver.findElement(By.id("descripcion")).clear();
	    driver.findElement(By.id("descripcion")).sendKeys("Para dar");
	    driver.findElement(By.id("descripcion")).sendKeys(Keys.DOWN);
	    driver.findElement(By.id("descripcion")).clear();
	    driver.findElement(By.id("descripcion")).sendKeys("Receta para dar a las cabras 1 vez cada dia durante 1 semana");
	    driver.findElement(By.xpath("//button[@type='submit']")).click();
	    return this;
	}
	
	public void iSeeTheErrorMessageInThePageWeb() {
		Assert.assertEquals("La suma de las cantidades de los productos debe ser superior a 0", driver.findElement(By.xpath("//form[@id='create-receta-form']/div/div")).getText());
	}
	
//	@Test
//	public void testUntitledTestCase() throws Exception {
//		this.driver.findElement(By.id("descripcion")).click();
//		this.driver.findElement(By.id("descripcion")).clear();
//		this.driver.findElement(By.id("descripcion")).sendKeys("Receta para dar a las cabras 1 vez cada dia durante 1 semana");
//		new Select(this.driver.findElement(By.id("cantidad"))).selectByVisibleText("3");
//		this.driver.findElement(By.xpath("//option[@value='3']")).click();
//		new Select(this.driver.findElement(By.xpath("(//select[@id='cantidad'])[2]"))).selectByVisibleText("2");
//		this.driver.findElement(By.xpath("(//option[@value='2'])[2]")).click();
//		this.driver.findElement(By.xpath("//button[@type='submit']")).click();
//		Assert.assertEquals("Perpetuo Melgar Rios", this.driver.findElement(By.xpath("//table[@id='listaRecetasTable']/tbody/tr/td")).getText());
//		Assert.assertEquals("Receta para dar a las cabras 1 vez cada dia durante 1 semana", this.driver.findElement(By.xpath("//table[@id='listaRecetasTable']/tbody/tr/td[2]")).getText());
//	}

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
