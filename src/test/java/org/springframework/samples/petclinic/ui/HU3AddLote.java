
package org.springframework.samples.petclinic.ui;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HU3AddLote {

	@LocalServerPort
	private int				port;

	private String			username;
	private WebDriver		driver;
	private String			baseUrl;
	private boolean			acceptNextAlert		= true;
	private StringBuffer	verificationErrors	= new StringBuffer();


	@BeforeEach
	public void setUp() throws Exception {
		if (System.getProperty("os.name").toLowerCase().contains("windows")) {
			System.setProperty("webdriver.gecko.driver", System.getenv("webdriver.gecko.driver"));
		}
		this.driver = new FirefoxDriver();
		this.baseUrl = "https://www.google.com/";
		this.driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	}

	@Test
	public void testHU3Positive() throws Exception {
		this.driver.get("http://localhost:" + this.port);
		this.driver.findElement(By.xpath("//div[@id='main-navbar']/ul[2]/li/a")).click();
		this.driver.findElement(By.id("password")).clear();
		this.driver.findElement(By.id("password")).sendKeys("veterinario2");
		this.driver.findElement(By.id("username")).clear();
		this.driver.findElement(By.id("username")).sendKeys("veterinario2");
		this.driver.findElement(By.xpath("//button[@type='submit']")).click();
		this.driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[3]/a/span[2]")).click();
		this.driver.findElement(By.xpath("//img[@alt='Porcino']")).click();
		this.driver.findElement(By.id("lotesCheck")).click();
		this.driver.findElement(By.xpath("//input[@value='Añade un nuevo lote']")).click();
		this.driver.findElement(By.id("identificadorLote")).click();
		this.driver.findElement(By.id("identificadorLote")).clear();
		this.driver.findElement(By.id("identificadorLote")).sendKeys("L0983741082394");
		this.driver.findElement(By.id("numeroMachos")).click();
		this.driver.findElement(By.id("numeroMachos")).clear();
		this.driver.findElement(By.id("numeroMachos")).sendKeys("2");
		this.driver.findElement(By.id("numeroHembras")).click();
		this.driver.findElement(By.id("numeroHembras")).clear();
		this.driver.findElement(By.id("numeroHembras")).sendKeys("3");
		this.driver.findElement(By.id("fechaIdentificacion")).click();
		this.driver.findElement(By.linkText("1")).click();
		this.driver.findElement(By.id("fechaNacimiento")).click();
		this.driver.findElement(By.linkText("Prev")).click();
		this.driver.findElement(By.linkText("20")).click();
		this.driver.findElement(By.xpath("//button[@type='submit']")).click();
		this.driver.findElement(By.id("lotesCheck")).click();
		this.driver.findElement(By.linkText("L0983741082394")).click();
		try {
			Assertions.assertTrue(this.driver.findElement(By.xpath("//body/div/div")).getText().contains("Número de Machos actuales:2"));
		} catch (Error e) {
			this.verificationErrors.append(e.toString());
		}

		try {
			Assertions.assertTrue(this.driver.findElement(By.xpath("//body/div/div")).getText().contains("Número de hembras actuales:3"));
		} catch (Error e) {
			this.verificationErrors.append(e.toString());
		}
		try {
			Assertions.assertEquals(5, this.driver.findElements(By.xpath("//table[@id='animalTable']/tbody/tr")).size());
		} catch (Error e) {
			this.verificationErrors.append(e.toString());
		}
		this.driver.findElement(By.xpath("//div[@id='main-navbar']/ul[2]/li/a/strong")).click();
		this.driver.findElement(By.xpath("//a[contains(text(),'Logout')]")).click();
		this.driver.findElement(By.xpath("//button[@type='submit']")).click();
	}

	@Test
	public void testHU3Negative() throws Exception {
		this.driver.get("http://localhost:" + this.port);
		this.driver.findElement(By.xpath("//div[@id='main-navbar']/ul[2]/li/a")).click();
		this.driver.findElement(By.id("password")).clear();
		this.driver.findElement(By.id("password")).sendKeys("veterinario2");
		this.driver.findElement(By.id("username")).clear();
		this.driver.findElement(By.id("username")).sendKeys("veterinario2");
		this.driver.findElement(By.xpath("//button[@type='submit']")).click();
		this.driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[3]/a/span[2]")).click();
		this.driver.findElement(By.xpath("//img[@alt='Porcino']")).click();
		this.driver.findElement(By.id("lotesCheck")).click();
		this.driver.findElement(By.xpath("//input[@value='Añade un nuevo lote']")).click();
		this.driver.findElement(By.xpath("//button[@type='submit']")).click();
		try {
			Assertions.assertEquals("no puede estar vacío", this.driver.findElement(By.xpath("//form[@id='add-lote-form']/div/div/span[2]")).getText());
		} catch (Error e) {
			this.verificationErrors.append(e.toString());
		}
		try {
			Assertions.assertEquals("no puede ser null", this.driver.findElement(By.xpath("//form[@id='add-lote-form']/div[2]/div/span[2]")).getText());
		} catch (Error e) {
			this.verificationErrors.append(e.toString());
		}
		try {
			Assertions.assertEquals("no puede ser null", this.driver.findElement(By.xpath("//form[@id='add-lote-form']/div[3]/div/span[2]")).getText());
		} catch (Error e) {
			this.verificationErrors.append(e.toString());
		}
		try {
			Assertions.assertEquals("no puede ser null", this.driver.findElement(By.xpath("//form[@id='add-lote-form']/div[4]/div/div/span[2]")).getText());
		} catch (Error e) {
			this.verificationErrors.append(e.toString());
		}
		try {
			Assertions.assertEquals("no puede ser null", this.driver.findElement(By.xpath("//form[@id='add-lote-form']/div[4]/div[2]/div/span[2]")).getText());
		} catch (Error e) {
			this.verificationErrors.append(e.toString());
		}
		this.driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[3]/a/span[2]")).click();
		this.driver.findElement(By.xpath("//img[@alt='Porcino']")).click();
		try {
			Assertions.assertEquals(0, this.driver.findElements(By.xpath("//table[@id='animalTable']/tbody/tr")).size());
		} catch (Error e) {
			this.verificationErrors.append(e.toString());
		}
		this.driver.findElement(By.xpath("//div[@id='main-navbar']/ul[2]/li/a/strong")).click();
		this.driver.findElement(By.xpath("//a[contains(text(),'Logout')]")).click();
		this.driver.findElement(By.xpath("//button[@type='submit']")).click();
	}

	/*
	 * try {
	 * assertEquals(0, driver.findElements(By.xpath("//table[@id='animalTable']/tbody/tr")).size());
	 * } catch (Error e) {verificationErrors.append(e.toString());}
	 *
	 */

	@AfterEach
	public void tearDown() throws Exception {
		this.driver.quit();
		String verificationErrorString = this.verificationErrors.toString();
		if (!"".equals(verificationErrorString)) {
			Assertions.fail(verificationErrorString);
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
